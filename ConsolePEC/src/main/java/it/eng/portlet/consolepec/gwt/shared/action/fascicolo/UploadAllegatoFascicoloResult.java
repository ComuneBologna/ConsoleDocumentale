package it.eng.portlet.consolepec.gwt.shared.action.fascicolo;

import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;

import com.gwtplatform.dispatch.shared.Result;

public class UploadAllegatoFascicoloResult implements Result {

	private static final long serialVersionUID = 1L;
	private FascicoloDTO fascicolo;
	private boolean error;
	private String errorMsg;

	@SuppressWarnings("unused")
	private UploadAllegatoFascicoloResult() {
		// For serialization only
	}

	public UploadAllegatoFascicoloResult(FascicoloDTO fascicolo, boolean error, String errorMsg) {
		this.fascicolo = fascicolo;
		this.setError(error);
		this.setErrorMsg(errorMsg);
	}

	public FascicoloDTO getFascicolo() {
		return fascicolo;
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
}
