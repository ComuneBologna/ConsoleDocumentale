package it.eng.portlet.consolepec.gwt.shared.action.pec;

import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;

import com.gwtplatform.dispatch.shared.Result;

public class EliminaBozzaResult implements Result {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String messError;
	private Boolean error;
	private FascicoloDTO fascicolo;

	@SuppressWarnings("unused")
	private EliminaBozzaResult() {
	
		// For serialization only
	}

	public EliminaBozzaResult(FascicoloDTO fascicolo, String messError, Boolean error) {
	this.setFascicolo(fascicolo);
		this.messError = messError;
		this.error = error;
	}

	public String getMessError() {
	
		return messError;
	}

	public Boolean getError() {
	
		return error;
	}

	public FascicoloDTO getFascicolo() {
		return fascicolo;
	}

	public void setFascicolo(FascicoloDTO fascicolo) {
		this.fascicolo = fascicolo;
	}
}
