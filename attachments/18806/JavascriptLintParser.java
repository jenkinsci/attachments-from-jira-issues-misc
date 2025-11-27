package hudson.plugins.violations.types.javascriptlint;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hudson.plugins.violations.ViolationsParser;
import hudson.plugins.violations.model.FullBuildModel;
import hudson.plugins.violations.model.FullFileModel;
import hudson.plugins.violations.model.Severity;
import hudson.plugins.violations.model.Violation;
import hudson.plugins.violations.util.AbsoluteFileFinder;

/**
 * Parser for parsing JavascriptLint reports.
 *
 * The parser only supports JavascriptLint report that has the output format
 * 'parseable'.
 *
 * @author Chad Metcalf
 */
public class JavascriptLintParser implements ViolationsParser {

    /** Regex pattern for the JavascriptLint errors. */
    private final transient Pattern pattern;
    private transient AbsoluteFileFinder absoluteFileFinder = new AbsoluteFileFinder(); 

    /**
     * Constructor - create the pattern.
     */
    public JavascriptLintParser() {
        pattern = Pattern.compile("(.*?)\\((\\d+)\\):\\s+([\\w\\s]+):\\s+(.*)");
    }

    /** {@inheritDoc} */
    public void parse( FullBuildModel model, File projectPath, String fileName,
        String[] sourcePaths) throws IOException {
        
    	BufferedReader reader = null;
        
    	absoluteFileFinder.addSourcePath(projectPath.getAbsolutePath());
    	absoluteFileFinder.addSourcePaths(sourcePaths);
        
        try {
            reader = new BufferedReader(
                new FileReader(new File(projectPath, fileName)));
            String line = reader.readLine();
            while (line != null) {
                parseLine(model, line, projectPath);
                line = reader.readLine();
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    /**
     * Parses a JavascriptLint line and adding a violation if regex
     * @param model build model to add violations to
     * @param line the line in the file.
     * @param projectPath the path to use to resolve the file.
     */
    public void parseLine(FullBuildModel model, String line, File projectPath) {
        JavascriptLintViolation jsLintViolation = getJavascriptLintViolation(line);

        if (jsLintViolation != null) {

            Violation violation = new Violation();
            violation.setType("javascriptlint");
            violation.setLine(jsLintViolation.getLineStr());
            violation.setMessage(jsLintViolation.getMessage());
            violation.setSource(jsLintViolation.getViolationId());
            setServerityLevel(violation, jsLintViolation.getViolationId());

            FullFileModel fileModel = getFileModel(model, 
            		jsLintViolation.getFileName(), 
            		absoluteFileFinder.getFileForName(jsLintViolation.getFileName()));
            fileModel.addViolation(violation);
        }
    }
    
    private FullFileModel getFileModel(FullBuildModel model, String name, File sourceFile) {
        FullFileModel fileModel = model.getFileModel(name);
        File other = fileModel.getSourceFile();

        if (sourceFile == null
            || ((other != null) && (
                    other.equals(sourceFile)
                    || other.exists()))) {
            return fileModel;
        }
        
        fileModel.setSourceFile(sourceFile);
        fileModel.setLastModified(sourceFile.lastModified());
        return fileModel;
    }
    

    /**
     * Returns a jslint violation (if it is one)
     * @param line a line from the JavascriptLint parseable report
     * @return a JavascriptLintViolation if the line contains one; null otherwise
     */
    JavascriptLintViolation getJavascriptLintViolation(String line) {
        Matcher matcher = pattern.matcher(line);
        if (matcher.matches() && matcher.groupCount() == 4) {
            return new JavascriptLintViolation(matcher);
        }
        return null;
    }

    /**
     * Returns the Severity level as an int from the JavascriptLint message type.
     *
     * The different message types are:
     * lint warning:
     * warning:
     *
     * @param messageType the type of JavascriptLint message
     * @return an int is matched to the message type.
     */
    private void setServerityLevel(Violation violation, String messageType) {


        if (messageType.contains("warning")) {
            violation.setSeverity(Severity.MEDIUM);
            violation.setSeverityLevel(Severity.MEDIUM_VALUE);
        } else {      
            violation.setSeverity(Severity.HIGH);
            violation.setSeverityLevel(Severity.HIGH_VALUE);
        }
    }
    
    class JavascriptLintViolation {
        private final transient String lineStr;
        private final transient String message;
        private final transient String fileName;
        private final transient String violationId;

        public JavascriptLintViolation(Matcher matcher) {
            if (matcher.groupCount() < 4) {
                throw new IllegalArgumentException(
                    "The Regex matcher could not find enough information");
            }
            lineStr = matcher.group(2);
            message = matcher.group(4);
            violationId = matcher.group(3);
            fileName = matcher.group(1);
        }

        public String getLineStr() {
            return lineStr;
        }

        public String getMessage() {
            return message;
        }

        public String getFileName() {
            return fileName;
        }

        public String getViolationId() {
            return violationId;
        }
    }
}
