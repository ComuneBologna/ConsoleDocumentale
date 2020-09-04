package it.eng.consolepec.spagicclient.remoteproxy.exception;

import lombok.Getter;

@Getter
public class SpagicClientException extends RuntimeException {

	private static final long serialVersionUID = 5884492320090886607L;

	private SpagicClientErrorCode code;
	private String errorMessage;

	public SpagicClientException(SpagicClientErrorCode code, String errorMessage) {
		super(errorMessage);
		this.errorMessage = errorMessage;
		this.code = code;
	}

	public SpagicClientException(SpagicClientErrorCode code) {
		super(code.getDefaultMessage());
		this.errorMessage = code.getDefaultMessage();
		this.code = code;
	}

	@Override
	public String getLocalizedMessage() {
		return getCode() + " " + getMessage();
	}

}
