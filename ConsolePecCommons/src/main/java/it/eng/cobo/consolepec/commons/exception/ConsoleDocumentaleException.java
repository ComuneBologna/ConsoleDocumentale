package it.eng.cobo.consolepec.commons.exception;

import lombok.Getter;

public abstract class ConsoleDocumentaleException extends Exception {

	private static final long serialVersionUID = 5057105361525831270L;

	@Getter
	private String outputMessage;

	protected ConsoleDocumentaleException(String message, boolean output) {
		super(message);

		if (output) {
			this.outputMessage = message;
		}
	}

	protected ConsoleDocumentaleException(Throwable cause) {
		super(cause);
	}

	protected ConsoleDocumentaleException(String message) {
		this(message, false);
	}

	protected ConsoleDocumentaleException(Throwable cause, String message) {
		this(cause, message, false);
	}

	protected ConsoleDocumentaleException(Throwable cause, String message, boolean output) {
		super(message, cause);

		if (output) {
			this.outputMessage = message;
		}
	}
}
