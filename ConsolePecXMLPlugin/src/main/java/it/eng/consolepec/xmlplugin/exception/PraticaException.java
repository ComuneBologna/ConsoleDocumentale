package it.eng.consolepec.xmlplugin.exception;

public class PraticaException extends RuntimeException {
	private static final long serialVersionUID = -5973802804954393596L;

	public PraticaException(Throwable cause, String message) {
		super(message, cause);
	}

	public PraticaException(String message) {
		super(message);
	}

}
