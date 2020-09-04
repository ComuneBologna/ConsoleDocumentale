package it.eng.portlet.consolepec.gwt.shared.action.fascicolo;

import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;

import com.gwtplatform.dispatch.shared.Result;

public class RimozionePubblicazioneAllegatiResult implements Result {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6409907250330145437L;
	private FascicoloDTO fascicolo;
	private boolean error = false;
	private String errorMsg;

	@SuppressWarnings("unused")
	private RimozionePubblicazioneAllegatiResult() {
		// For serialization only
	}

	public RimozionePubblicazioneAllegatiResult(FascicoloDTO fascicolo) {
		this.fascicolo = fascicolo;
	}

	public RimozionePubblicazioneAllegatiResult(FascicoloDTO fascicolo, String errorMsg, boolean error) {
		this(fascicolo);
		this.error = error;
		this.errorMsg = errorMsg;
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
