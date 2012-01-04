package luoyong.compiler.csvcompiler;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class CsvParser {

	public abstract void writeField(String data);

	public abstract void startNewRecord();

	public static final String DEFAULT_CHARSET = "UTF-8";

	/**
	 * Double-quoted CSV field pattern.
	 */
	public static final String CSV_DOUBLE_QUOTED_PATTERN_REGEXP
		= "\\A\\s*?\"((.|\\n|\r\n)*?)\"\\s*?(,|\\n)\\s*?\\z";

	/**
	 * Buffer of bytes of the field.
	 */
	private LinkedList<Byte> _fieldBuffer = null;

	public CsvParser() {
		_fieldBuffer = new LinkedList<Byte>();
	}

	public void parseByte(byte currentByte) throws CsvParserException {

		_fieldBuffer.addLast(currentByte);

		if (currentByte == ',') {
			if (checkIfFieldEnds()) {
				endField(); // End current field and start a new field.
				return;
			} else {
				return;
			}
		} else if (currentByte == '\n') {
			if (checkIfFieldEnds()) {
				endField(); // End current field and start a new field.
				this.startNewRecord(); // Start a new row.
				return;
			} else {
				return;
			}
		}
	}

	protected boolean checkIfFieldEnds() {

		int startQuotePosition = -1;

		// If last byte in buffer is not a comma,
		// consider current field is not end.
		byte lastByte = _fieldBuffer.getLast();
		if (lastByte != ',') {
			return false;
		}

		// Matching the start quote.
		// Consider this field as end if start quote not found.
		for (int i = 0; i < _fieldBuffer.size(); i++) {
			byte headByte = _fieldBuffer.get(i);
			if (headByte == ' ') {
				// Ignore leading white spaces.
				continue;
			} else if (headByte == '"') {
				// Save current position as start quote position.
				startQuotePosition = i;
				// Start matching the end quote.
				break;
			} else {
				// If start quote not found, this field is not double-quoted,
				// consider current field is end.
				return true;
			}
		}

		// Start matching end quote.
		for (int i = (_fieldBuffer.size() - 2); i > startQuotePosition; i--) {
			byte endByte = _fieldBuffer.get(i);
			if (endByte == ' ') {
				// Ignore trailing white spaces.
				continue;
			} else if (endByte == '"') {
				// If end quote found, consider current field is end.
				return true;
			} else {
				// If end quote not found, consider current field is not end.
				return false;
			}
		}

		// If end quote not found, consider current field is not end.
		return false;
	}

	public void flushField() {
		this.endField();
	}

	/**
	 * End current field.
	 */
	private void endField() {

		Byte[] bufferBytes = (Byte[]) this._fieldBuffer.toArray();

		String bufferString = null;

		try {
			bufferString = new String(convertByteArray(bufferBytes),
					DEFAULT_CHARSET);
		} catch (Throwable t) {
			t.printStackTrace();
		}

		this.writeField(bufferString);
		// Clean current buffer.
		_fieldBuffer.clear();
	}

	/**
	 * Convert Byte[] to byte[].
	 * 
	 * @param bytes
	 * @return
	 */
	private static byte[] convertByteArray(Byte[] bytes) {
		byte[] result = new byte[bytes.length];
		for (int i = 0; i < bytes.length; i++) {
			result[i] = bytes[i];
		}
		return result;
	}

	/**
	 * Extract value from CSV field.
	 * 
	 * @param field
	 *            CSV field.
	 */
	public static String extractField(String field) {
		Pattern pattern = Pattern.compile(CSV_DOUBLE_QUOTED_PATTERN_REGEXP,
				Pattern.MULTILINE);
		Matcher matcher = pattern.matcher(field);
		if (matcher.matches()) {
			String result = matcher.group(1);
			return result.replaceAll("\"\"", "\"");
		} else {
			return field;
		}
	}
}
