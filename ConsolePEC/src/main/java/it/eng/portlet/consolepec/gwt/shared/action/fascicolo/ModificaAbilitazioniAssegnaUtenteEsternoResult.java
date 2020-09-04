package it.eng.portlet.consolepec.gwt.shared.action.fascicolo;

import com.gwtplatform.dispatch.shared.Result;


public class ModificaAbilitazioniAssegnaUtenteEsternoResult implements Result {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6153976179803687829L;
	private String errorMsg;
	private boolean error;
	
	public ModificaAbilitazioniAssegnaUtenteEsternoResult() {
		this.error = false;
		this.errorMsg = null;
	}

	public ModificaAbilitazioniAssegnaUtenteEsternoResult( String errorMsg, Boolean error) {
		this.error = error;
		this.errorMsg = errorMsg;
	}

	
	public String getErrorMsg() {
		return errorMsg;
	}

	public boolean isError() {
		return error;
	}

	
}
