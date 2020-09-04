package it.eng.cobo.consolepec.commons.exception;

public class NotFoundException extends ConsoleDocumentaleException {

	private static final long serialVersionUID = -1307267271815186206L;

	public NotFoundException(String message, boolean output) {
		super(message, output);
	}

	public NotFoundException(Throwable cause) {
		super(cause);
	}

	public NotFoundException(Throwable cause, String message, boolean output) {
		super(cause, message, output);
	}

	public NotFoundException(String message) {
		super(message);
	}

	public NotFoundException(Throwable cause, String message) {
		super(cause, message);
	}

}
