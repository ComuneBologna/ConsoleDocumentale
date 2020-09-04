package it.eng.portlet.consolepec.gwt.shared.action.pec;

import com.gwtplatform.dispatch.shared.Result;

public class AggiungiPraticaAFascicoloResult implements Result {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6608614038291441257L;
	private String messError;
	private Boolean isError;

	@SuppressWarnings("unused")
	private AggiungiPraticaAFascicoloResult() {
	
		// For serialization only
	}

	public AggiungiPraticaAFascicoloResult(String messError, Boolean isError) {
	
		this.messError = messError;
		this.isError = isError;
	}

	public String getMessError() {
	
		return messError;
	}

	public Boolean getIsError() {
	
		return isError;
	}
}
