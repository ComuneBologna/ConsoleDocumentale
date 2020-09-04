package it.eng.portlet.consolepec.gwt.shared.action.fascicolo;

import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;

import com.gwtplatform.dispatch.shared.Result;

public class PubblicazioneAllegatiResult implements Result {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7225557240919670713L;
	private FascicoloDTO fascicolo;
	private String nomeAllegato;
	private boolean error = false;
	private String errorMsg;

	@SuppressWarnings("unused")
	private PubblicazioneAllegatiResult() {
		// For serialization only
	}

	public PubblicazioneAllegatiResult(FascicoloDTO fascicolo, String nomeAllegato) {
		this.fascicolo = fascicolo;
		this.nomeAllegato = nomeAllegato;
	}
	
	public PubblicazioneAllegatiResult(FascicoloDTO fascicolo, String nomeAllegato, String errorMsg, boolean error) {
		this(fascicolo, nomeAllegato);
		this.error = error;
		this.errorMsg = errorMsg;
	}

	public String getNomeAllegato() {
		return nomeAllegato;
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
