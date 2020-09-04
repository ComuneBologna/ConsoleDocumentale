package it.eng.portlet.consolepec.gwt.shared.action.modulistica;

import it.eng.portlet.consolepec.gwt.shared.model.PraticaModulisticaDTO;

import com.gwtplatform.dispatch.shared.Result;

public class CaricaPraticaModulisticaActionResult implements Result {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String messError;
	private Boolean error;
	private PraticaModulisticaDTO pratica;
	private String errorCode;
	
	public CaricaPraticaModulisticaActionResult() {
		// For serialization only
	}

	public CaricaPraticaModulisticaActionResult(String messError, Boolean error, PraticaModulisticaDTO pratica) {
		this.messError = messError;
		this.error = error;
		this.pratica = pratica;
		
	}

	public CaricaPraticaModulisticaActionResult(String messError, Boolean error, String errorCode, PraticaModulisticaDTO pratica) {
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

	public PraticaModulisticaDTO getPratica() {
		return pratica;
	}

	public void setPratica(PraticaModulisticaDTO pratica) {
		this.pratica = pratica;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}


}
