package luoyong.test.compiler.csvcompiler;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllTests.class.getName());
		
		suite.addTestSuite(CsvParserTest.class);

		return suite;
	}

}
