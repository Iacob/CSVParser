package luoyong.test.compiler.csvcompiler;

import junit.framework.TestCase;
import luoyong.compiler.csvcompiler.CsvParser;

public class CsvParserTest extends TestCase {

	public void testExtractField() {
		String str = " \"ab\"\"\"\"cd\ne\"\"f\"  ,";
		String result = CsvParser.extractField(str);
		String expected = "ab\"\"cd\ne\"f";
		assertEquals(expected, result);
	}
}
