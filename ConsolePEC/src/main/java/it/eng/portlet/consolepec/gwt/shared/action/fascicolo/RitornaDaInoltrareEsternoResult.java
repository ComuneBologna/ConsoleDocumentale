package it.eng.portlet.consolepec.gwt.shared.action.fascicolo;

import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;

import com.gwtplatform.dispatch.shared.Result;

public class RitornaDaInoltrareEsternoResult implements Result {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8091184682717165867L;

	private String messageError;
	private boolean error;
	private PraticaDTO pratica;
	
	public RitornaDaInoltrareEsternoResult() {

	}

	public RitornaDaInoltrareEsternoResult(String messErr) {
		this.messageError = messErr;
		this.error = true;
	}
	
	public RitornaDaInoltrareEsternoResult(PraticaDTO pratica) {
		super();
		this.pratica = pratica;
	}

	public PraticaDTO getPratica() {
		return pratica;
	}

	public String getMessageError() {
		return messageError;
	}

	public boolean isError() {
		return error;
	}

}
