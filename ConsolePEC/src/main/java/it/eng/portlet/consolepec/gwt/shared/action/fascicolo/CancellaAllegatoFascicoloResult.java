package it.eng.portlet.consolepec.gwt.shared.action.fascicolo;

import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;

import com.gwtplatform.dispatch.shared.Result;

public class CancellaAllegatoFascicoloResult implements Result {

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
	private CancellaAllegatoFascicoloResult() {
		// For serialization only
	}

	public CancellaAllegatoFascicoloResult(FascicoloDTO fascicolo) {
		this.fascicolo = fascicolo;
	}

	public FascicoloDTO getFascicolo() {
		return fascicolo;
	}
}
