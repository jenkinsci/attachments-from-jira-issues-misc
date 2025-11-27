package hudson.plugins.emailext.plugins.content;

import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.TaskListener;
import hudson.plugins.emailext.EmailType;
import hudson.plugins.emailext.ExtendedEmailPublisher;
import hudson.plugins.emailext.plugins.EmailContent;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PerforceChangeListContent implements EmailContent {

    private static final String TOKEN = "P4_CHANGELIST";

    public String getToken() {
        return TOKEN;
    }

    public List<String> getArguments() {
        return Collections.emptyList();
    }

    public String getHelpText() {
        return "Displays the perforce change list number.";
    }

    public <P extends AbstractProject<P, B>, B extends AbstractBuild<P, B>>
        String getContent(AbstractBuild<P, B> build, ExtendedEmailPublisher publisher,
                EmailType emailType, Map<String, ?> args) throws IOException, InterruptedException {

        Map<String, String> env = build.getEnvironment(TaskListener.NULL);
        String value = env.get("P4_CHANGELIST");
        if (value == null) {
            value = "-1";
        }
        return value;
    }

    public boolean hasNestedContent() {
        return false;
    }
}
