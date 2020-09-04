package it.eng.portlet.consolepec.gwt.shared.action.fascicolo;

import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;

import com.gwtplatform.dispatch.shared.Result;

public class CambiaTipologiaAllegatoResult implements Result {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8091184682717165867L;

	private String messageError;
	private boolean error;
	private FascicoloDTO fascicolo;
	
	public CambiaTipologiaAllegatoResult() {

	}

	public CambiaTipologiaAllegatoResult(String messErr) {
		this.messageError = messErr;
		this.error = true;
	}
	
	public CambiaTipologiaAllegatoResult(FascicoloDTO fascicolo) {
		super();
		this.fascicolo = fascicolo;
	}

	public String getMessageError() {
		return messageError;
	}

	public boolean isError() {
		return error;
	}

	public FascicoloDTO getFascicolo() {
		return fascicolo;
	}


}
