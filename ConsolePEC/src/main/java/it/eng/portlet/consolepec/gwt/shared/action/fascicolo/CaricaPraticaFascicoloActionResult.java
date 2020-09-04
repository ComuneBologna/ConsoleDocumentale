package it.eng.portlet.consolepec.gwt.shared.action.fascicolo;

import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;

import com.gwtplatform.dispatch.shared.Result;

public class CaricaPraticaFascicoloActionResult implements Result {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String messError;
	private Boolean error;
	private FascicoloDTO pratica;
	private String errorCode;
	
	@SuppressWarnings("unused")
	private CaricaPraticaFascicoloActionResult() {
		// For serialization only
	}

	public CaricaPraticaFascicoloActionResult(String messError, Boolean error, FascicoloDTO pratica) {
		this.messError = messError;
		this.error = error;
		this.pratica = pratica;
		
	}

	public CaricaPraticaFascicoloActionResult(String messError, Boolean error, String errorCode, FascicoloDTO pratica) {
		this.messError = messError;
		this.error = error;
		this.pratica = pratica;
		this.errorCode = errorCode;
		
	}

	public String getMessError() {
		return messError;
	}

	public void setMessError(String messError) {
		this.messError = messError;
	}

	public Boolean getError() {
		return error;
	}

	public void setError(Boolean error) {
		this.error = error;
	}

	public FascicoloDTO getPratica() {
		return pratica;
	}

	public void setPratica(FascicoloDTO pratica) {
		this.pratica = pratica;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}


}
