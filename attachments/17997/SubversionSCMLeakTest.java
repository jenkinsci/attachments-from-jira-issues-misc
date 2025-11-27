package hudson.scm;

import hudson.FilePath;
import hudson.model.BuildListener;
import hudson.model.FreeStyleBuild;
import hudson.model.FreeStyleProject;
import hudson.model.Hudson;
import hudson.model.Result;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;

import junit.framework.TestCase;

public class SubversionSCMLeakTest extends TestCase {

	public void testLeak() throws Exception {

		Hudson hudson = new Hudson(new File("/tmp/hudsontest"), null);

		FreeStyleProject project = new FreeStyleProject(hudson, "test");
		FreeStyleBuild build = new FreeStyleBuild(project);

		SubversionSCM scm = new SubversionSCM(new String[] { "file:///data/SVN/admin/lib" }, new String[] { "/tmp/svntest" }, false, "recht", null);

		BuildListener listener = new BuildListener() {
			public void finished(Result result) {
			}

			public void started() {
			}

			public PrintWriter error(String msg) {
				return null;
			}

			public PrintWriter fatalError(String msg) {
				return null;
			}

			public PrintStream getLogger() {
				try {
					return new PrintStream(new File("/dev/null"));
				} catch (FileNotFoundException e) {
					throw new RuntimeException(e);
				}
			}

		};

		MemoryMXBean mmx = ManagementFactory.getMemoryMXBean();
		System.out.println(mmx.getHeapMemoryUsage());

		for (int i = 0; i < 1000; i++) {
			scm.checkout(build, null, new FilePath(new File("/tmp/hudsontest")), listener, new File("/tmp/changelog"));
			System.out.println(mmx.getHeapMemoryUsage());
		}

		System.out.println(mmx.getHeapMemoryUsage());
	}
}
