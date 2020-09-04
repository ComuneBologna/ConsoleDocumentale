package it.eng.portlet.consolepec.gwt.shared.action.pec;

import it.eng.portlet.consolepec.gwt.shared.model.PecOutDTO;

import com.gwtplatform.dispatch.shared.Result;

public class CancellaAllegatoPecOutResult implements Result {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String messError;
	private Boolean error;
	private PecOutDTO pecOut;

	@SuppressWarnings("unused")
	private CancellaAllegatoPecOutResult() {
	
		// For serialization only
	}

	public CancellaAllegatoPecOutResult(
		String messError, Boolean error, PecOutDTO pecOut) {
	
		this.messError = messError;
		this.error = error;
		this.pecOut = pecOut;
	}

	public String getMessError() {
	
		return messError;
	}

	public Boolean getError() {
	
		return error;
	}

	public PecOutDTO getPecOut() {
	
		return pecOut;
	}
}
