package luoyong.compiler.csvcompiler;

public class CsvParserException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public CsvParserException() {
	   super();
   }

	public CsvParserException(String arg0, Throwable arg1, boolean arg2,
         boolean arg3) {
	   super(arg0, arg1, arg2, arg3);
   }

	public CsvParserException(String arg0, Throwable arg1) {
	   super(arg0, arg1);
   }

	public CsvParserException(String arg0) {
	   super(arg0);
   }
}
