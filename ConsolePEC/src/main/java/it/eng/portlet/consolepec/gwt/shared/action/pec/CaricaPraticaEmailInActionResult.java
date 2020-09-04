package it.eng.portlet.consolepec.gwt.shared.action.pec;

import it.eng.portlet.consolepec.gwt.shared.model.PecInDTO;

import com.gwtplatform.dispatch.shared.Result;

public class CaricaPraticaEmailInActionResult implements Result {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8587971274564046063L;
	private PecInDTO dettaglio = null;
	private boolean error = false;
	private String errorMessage;
	private String errorCode;

	public CaricaPraticaEmailInActionResult() {
	}

	public PecInDTO getDettaglio() {
		return dettaglio;
	}

	public void setDettaglio(PecInDTO dettaglio) {
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
