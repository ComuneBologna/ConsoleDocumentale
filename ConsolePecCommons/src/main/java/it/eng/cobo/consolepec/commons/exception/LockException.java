package it.eng.cobo.consolepec.commons.exception;

public class LockException extends ConsoleDocumentaleException {

	private static final long serialVersionUID = -1307267271815186206L;

	public LockException(String message, boolean output) {
		super(message, output);
	}

	public LockException(Throwable cause) {
		super(cause);
	}

	public LockException(Throwable cause, String message, boolean output) {
		super(cause, message, output);
	}

	public LockException(String message) {
		super(message);
	}

	public LockException(Throwable cause, String message) {
		super(cause, message);
	}
}
