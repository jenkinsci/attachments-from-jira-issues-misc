package hudson.util;

import junit.framework.TestCase;

public class ArgumentListBuilderTest extends TestCase {

	/** Need 4 backslashes to create double backslash because they need to be escaped */
	private static final String DOUBLE_BACKSLASH = "\\\\";

	private static final String PATH_WITH_TRAILING_BACKSLASH = "c:\\temp\\";

	public void testAddTokenized_doubleBackslashShouldNotBeReducedToSingleBackslash() {
		ArgumentListBuilder args = new ArgumentListBuilder();
		args.addTokenized(DOUBLE_BACKSLASH);
		String[] commandArray = args.toCommandArray();

		assertEquals(DOUBLE_BACKSLASH, commandArray[0]);

	}

	public void testAddTokenized_TrailingBackslashShouldNotBeRemoved() {
		ArgumentListBuilder args = new ArgumentListBuilder();
		args.addTokenized(PATH_WITH_TRAILING_BACKSLASH);
		String[] commandArray = args.toCommandArray();
		assertEquals(PATH_WITH_TRAILING_BACKSLASH, commandArray[0]);

	}

}
