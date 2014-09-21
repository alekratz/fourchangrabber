package fourchangrabber;

public class MismatchedHashException extends Exception {
	public MismatchedHashException(String got, String expected) {
		super("File received with hash " + got + " but expected " + expected);
	}
}
