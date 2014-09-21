package fourchangrabber;

public class MismatchedHashException extends Exception {
	private static final long serialVersionUID = 4494385183731761143L;

	public MismatchedHashException(String got, String expected) {
		super("File received with hash " + got + " but expected " + expected);
	}
}
