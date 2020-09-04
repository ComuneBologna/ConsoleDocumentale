package it.eng.portlet.consolepec.gwt.shared.action;

import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;

import com.gwtplatform.dispatch.shared.Result;

public class CancellaAllegatoPraticaResult implements Result {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String messError;
	private Boolean error;
	private PraticaDTO pratica;

	@SuppressWarnings("unused")
	private CancellaAllegatoPraticaResult() {
	
		// For serialization only
	}

	public CancellaAllegatoPraticaResult(
		String messError, Boolean error, PraticaDTO pratica) {
	
		this.messError = messError;
		this.error = error;
		this.pratica = pratica;
	}

	public String getMessError() {
	
		return messError;
	}

	public Boolean getError() {
	
		return error;
	}

	public PraticaDTO getPraticaDTO() {
	
		return pratica;
	}
}
