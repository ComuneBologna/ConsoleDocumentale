package it.eng.portlet.consolepec.gwt.shared.action.fascicolo;

import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;

import com.gwtplatform.dispatch.shared.Result;

public class FirmaAllegatoFascicoloResult implements Result {
	private static final long serialVersionUID = 1L;
	private FascicoloDTO fascicolo;
	private String errMsg;
	private boolean error;

	@SuppressWarnings("unused")
	private FirmaAllegatoFascicoloResult() {
		// For serialization only
	}

	public String getErrMsg() {
		return errMsg;
	}

	public boolean isError() {
		return error;
	}

	public FirmaAllegatoFascicoloResult(FascicoloDTO fascicolo, String errMsg, boolean error) {
		this.fascicolo = fascicolo;
		this.errMsg = errMsg;
		this.error = error;
	}

	public FascicoloDTO getFascicolo() {
		return fascicolo;
	}
}
