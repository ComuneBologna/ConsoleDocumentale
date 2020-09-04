package it.eng.portlet.consolepec.gwt.shared.action.fascicolo;

import com.gwtplatform.dispatch.shared.Result;


public class AssegnaUtenteEsternoResult implements Result {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6153976179803687829L;
	private String errorMsg;
	private boolean error;
	
	public AssegnaUtenteEsternoResult() {
		this.error = false;
		this.errorMsg = null;
	}

	public AssegnaUtenteEsternoResult( String errorMsg, Boolean error) {
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
