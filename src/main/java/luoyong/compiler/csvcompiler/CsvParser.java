package luoyong.compiler.csvcompiler;

import java.util.LinkedList;

public abstract class CsvParser {
   
	public abstract void writeField(String data);
	
	public abstract void startNewRecord();
	
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
			}else {
				return;
			}
		}else if(currentByte == '\n') {
			if (checkIfFieldEnds()) {
				endField(); // End current field and start a new field.
				this.startNewRecord(); // Start a new row.
				return;
			}else {
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
		for (int i=0; i<_fieldBuffer.size(); i++) {
			byte headByte = _fieldBuffer.get(i);
			if (headByte == ' ') {
				// Ignore leading white spaces.
				continue;
			}else if (headByte == '"') {
				// Save current position as start quote position.
				startQuotePosition = i;
				// Start matching the end quote.
				break;
			}else {
				// If start quote not found, this field is not double-quoted,
				// consider current field is end.
				return true;
			}
		}
		
		// Start matching end quote.
		for (int i=(_fieldBuffer.size()-2); i>startQuotePosition; i--) {
			byte endByte = _fieldBuffer.get(i);
			if (endByte == ' ') {
				// Ignore trailing white spaces.
				continue;
			}else if (endByte == '"') {
				// If end quote found, consider current field is end.
				return true;
			}else {
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
		//TODO Implement endField().
		this.writeField(null);
		_fieldBuffer.clear();
	}
}
