package it.eng.portlet.consolepec.gwt.shared.action.fascicolo;

import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;

import com.gwtplatform.dispatch.shared.Result;

public class SalvaFascicoloResult implements Result {

	private static final long serialVersionUID = 1L;
	private FascicoloDTO fascicolo;

	private boolean error;
	private String errorMsg;
	
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

	@SuppressWarnings("unused")
	private SalvaFascicoloResult() {
		// For serialization only
	}

	public SalvaFascicoloResult(FascicoloDTO fascicolo, boolean isError, String errorMsg) {
		this.fascicolo = fascicolo;
		this.error = isError;
		this.errorMsg = errorMsg;
	}

	public FascicoloDTO getFascicolo() {
		return fascicolo;
	}
}
