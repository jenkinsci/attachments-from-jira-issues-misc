3,9d2
< import java.io.File;
< import java.io.IOException;
< import java.io.PrintStream;
< import java.lang.reflect.Array;
< import java.text.SimpleDateFormat;
< import java.util.Map;
< 
11a5
> import hudson.FilePath;
13d6
< import hudson.Util;
20a14,20
> 
> import java.io.IOException;
> import java.io.PrintStream;
> import java.lang.reflect.Array;
> import java.text.SimpleDateFormat;
> import java.util.Map;
> 
98c98
< 		File tempDir = null;
---
> 		FilePath tempDir = rootProject.getWorkspace().createTempDir("hudson", "tmp");
101d100
< 			tempDir = Util.createTempDir();
126,127c125,126
< 					logger.println("cleaning up " + tempDir);
< 					Util.deleteRecursive(tempDir);
---
> 					logger.println("cleaning up: " + tempDir.toURI().toString());
> 					tempDir.deleteRecursive();
