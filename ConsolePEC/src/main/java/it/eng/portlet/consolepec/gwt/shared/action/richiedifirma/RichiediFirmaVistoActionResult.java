package it.eng.portlet.consolepec.gwt.shared.action.richiedifirma;

import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;

import com.gwtplatform.dispatch.shared.Result;

/**
 *
 * @author biagiot
 *
 */
public class RichiediFirmaVistoActionResult implements Result{

	public RichiediFirmaVistoActionResult() {

	}

	private static final long serialVersionUID = 4712008101996890062L;
	private String messError;
	private boolean error;
	private PraticaDTO pratica;

	public String getMessError() {
		return messError;
	}
	public void setMessError(String messError) {
		this.messError = messError;
	}
	public boolean getError() {
		return error;
	}
	public void setError(boolean error) {
		this.error = error;
	}
	public PraticaDTO getPratica() {
		return pratica;
	}
	public void setPratica(PraticaDTO pratica) {
		this.pratica = pratica;
	}
}