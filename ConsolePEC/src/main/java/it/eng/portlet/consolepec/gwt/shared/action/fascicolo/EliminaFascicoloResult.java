package it.eng.portlet.consolepec.gwt.shared.action.fascicolo;

import com.gwtplatform.dispatch.shared.Result;

public class EliminaFascicoloResult implements Result {

	/**
	 * 
	 */
	private static final long serialVersionUID = -55436337915727236L;
	private String message;
	private Boolean esito;

	@SuppressWarnings("unused")
	private EliminaFascicoloResult() {
		// For serialization only
	}

	public EliminaFascicoloResult(String message, Boolean esito) {
		this.message = message;
		this.esito = esito;
	}

	public String getMessage() {
		return message;
	}

	public Boolean getEsito() {
		return esito;
	}
}
