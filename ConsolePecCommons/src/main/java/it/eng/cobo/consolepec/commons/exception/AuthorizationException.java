package it.eng.cobo.consolepec.commons.exception;

public class AuthorizationException extends ConsoleDocumentaleException {

	private static final long serialVersionUID = -1307267271815186206L;

	public AuthorizationException(String message, boolean output) {
		super(message, output);
	}

	public AuthorizationException(Throwable cause) {
		super(cause);
	}

	public AuthorizationException(Throwable cause, String message, boolean output) {
		super(cause, message, output);
	}

	public AuthorizationException(String message) {
		super(message);
	}

	public AuthorizationException(Throwable cause, String message) {
		super(cause, message);
	}
}
