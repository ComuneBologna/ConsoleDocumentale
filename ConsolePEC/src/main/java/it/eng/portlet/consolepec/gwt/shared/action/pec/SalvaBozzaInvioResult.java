package it.eng.portlet.consolepec.gwt.shared.action.pec;

import it.eng.portlet.consolepec.gwt.shared.model.PecOutDTO;

import com.gwtplatform.dispatch.shared.Result;

public class SalvaBozzaInvioResult implements Result {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Boolean error;
	private String messError;
	private PecOutDTO bozzaPecOut;

	@SuppressWarnings("unused")
	private SalvaBozzaInvioResult() {
		// For serialization only
	}

	public SalvaBozzaInvioResult(Boolean error, String messError, PecOutDTO bozzaPecOut) {

		this.error = error;
		this.messError = messError;
		this.bozzaPecOut = bozzaPecOut;
	}

	public Boolean getError() {
		return error;
	}

	public void setError(Boolean error) {
		this.error = error;
	}

	public String getMessError() {
		return messError;
	}

	public void setMessError(String messError) {
		this.messError = messError;
	}

	public PecOutDTO getBozzaPecOut() {
		return bozzaPecOut;
	}

	public void setBozzaPecOut(PecOutDTO bozzaPecOut) {
		this.bozzaPecOut = bozzaPecOut;
	}

}
