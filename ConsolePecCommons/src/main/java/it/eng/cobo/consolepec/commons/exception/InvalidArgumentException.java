package it.eng.cobo.consolepec.commons.exception;

public class InvalidArgumentException extends ConsoleDocumentaleException {

	private static final long serialVersionUID = 5535302910975038583L;

	public InvalidArgumentException(String message, boolean output) {
		super(message, output);
	}

	public InvalidArgumentException(Throwable cause) {
		super(cause);
	}

	public InvalidArgumentException(Throwable cause, String message, boolean output) {
		super(cause, message, output);
	}

	public InvalidArgumentException(String message) {
		super(message);
	}

	public InvalidArgumentException(Throwable cause, String message) {
		super(cause, message);
	}
}
