package it.eng.portlet.consolepec.gwt.shared.action.firma;

import com.gwtplatform.dispatch.shared.Result;

public class SendOTPRequestResult implements Result {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SendOTPRequestResult() {
	}
	
	private boolean error;
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}
}
