package it.eng.portlet.consolepec.gwt.shared.action.pec;

import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;

import com.gwtplatform.dispatch.shared.Result;

public class UploadAllegatoPraticaResult implements Result {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8651131386381592887L;
	private String messError;
	private Boolean error;
	private PraticaDTO pratica;

	@SuppressWarnings("unused")
	private UploadAllegatoPraticaResult() {

		// For serialization only
	}

	public UploadAllegatoPraticaResult(String messError, Boolean error, PraticaDTO pratica) {
		this.messError = messError;
		this.error = error;
		this.pratica = pratica;
	}

	public String getMessError() {
		return messError;
	}

	public Boolean getError() {
		return error;
	}

	public PraticaDTO getPratica() {
		return pratica;
	}

}
