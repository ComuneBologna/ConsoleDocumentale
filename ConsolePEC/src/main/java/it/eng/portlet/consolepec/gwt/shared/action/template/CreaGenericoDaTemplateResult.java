package it.eng.portlet.consolepec.gwt.shared.action.template;

import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;

import com.gwtplatform.dispatch.shared.Result;

public abstract class CreaGenericoDaTemplateResult implements Result {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean error;
	private String errorMsg;
	private FascicoloDTO fascicolo;
	
	protected CreaGenericoDaTemplateResult() {
		// For serialization only
	}

	public CreaGenericoDaTemplateResult(FascicoloDTO fascicolo) {
		this.fascicolo = fascicolo;
	}
	
	public CreaGenericoDaTemplateResult(boolean isError, String errorMsg) {
		this.error = isError;
		this.errorMsg = errorMsg;
	}
	
	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
	public FascicoloDTO getFascicolo() {
		return fascicolo;
	}
}
