package it.eng.cobo.consolepec.integration.sit.exception;

public class SitWsClientException extends Exception {

	private static final long serialVersionUID = -7907093496011262149L;

	public SitWsClientException(String message) {
		super(message);
	}

	public SitWsClientException(String message, Throwable cause) {
		super(message, cause);
	}

}
