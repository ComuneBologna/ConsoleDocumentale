package it.eng.portlet.consolepec.gwt.shared.action.template.eliminazione;

import com.gwtplatform.dispatch.shared.Result;

/**
 *
 * @author biagiot
 *
 */
public class EliminaTemplateResult implements Result {

	private static final long serialVersionUID = 1L;

	private boolean esito;
	private String message;

	protected EliminaTemplateResult() {

	}

	public EliminaTemplateResult(boolean esito, String message) {
		this.esito = esito;
		this.message = message;
	}

	public boolean isEsito() {
		return esito;
	}

	public void setEsito(boolean esito) {
		this.esito = esito;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
