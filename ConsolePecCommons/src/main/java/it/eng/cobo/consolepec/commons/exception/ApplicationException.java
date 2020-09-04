package it.eng.cobo.consolepec.commons.exception;

public class ApplicationException extends ConsoleDocumentaleException {

	private static final long serialVersionUID = -1307267271815186206L;

	public ApplicationException(String message, boolean output) {
		super(message, output);
	}

	public ApplicationException(Throwable cause) {
		super(cause);
	}

	public ApplicationException(Throwable cause, String message, boolean output) {
		super(cause, message, output);
	}

	public ApplicationException(Throwable cause, String message) {
		super(cause, message);
	}

	public ApplicationException(String message) {
		super(message);
	}

}
