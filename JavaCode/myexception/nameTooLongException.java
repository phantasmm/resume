package myexception;

public class nameTooLongException extends Exception {
		
	public String getError() {
		return "Character name must not contains more than 10 characters.";
	}
}
