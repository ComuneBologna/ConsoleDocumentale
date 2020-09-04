package it.eng.portlet.consolepec.gwt.shared.action.pec;

import it.eng.portlet.consolepec.gwt.shared.model.PecOutDTO;

import com.gwtplatform.dispatch.shared.Result;

public class CaricaPraticaEmailOutActionResult implements Result {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PecOutDTO dettaglio;
	private boolean error = false;
	private String errorMessage;
	private String errorCode;

	@SuppressWarnings("unused")
	private CaricaPraticaEmailOutActionResult() {
		// For serialization only
	}

	public CaricaPraticaEmailOutActionResult(PecOutDTO mail) {
		this.dettaglio = mail;
	}

	public PecOutDTO getDettaglio() {
		return dettaglio;
	}

	public void setDettaglio(PecOutDTO dettaglio) {
		this.dettaglio = dettaglio;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

}
