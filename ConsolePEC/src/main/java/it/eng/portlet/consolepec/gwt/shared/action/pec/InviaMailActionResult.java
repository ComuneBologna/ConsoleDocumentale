package it.eng.portlet.consolepec.gwt.shared.action.pec;

import it.eng.portlet.consolepec.gwt.shared.model.PecOutDTO;

import com.gwtplatform.dispatch.shared.Result;

public class InviaMailActionResult implements Result {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String messError;
	private Boolean error;
	private PecOutDTO pecOut;

	@SuppressWarnings("unused")
	private InviaMailActionResult() {

		// For serialization only
	}

	public InviaMailActionResult(String messError, Boolean error) {

		this.messError = messError;
		this.error = error;
	}

	public String getMessError() {
		return messError;
	}

	public void setMessError(String messError) {
		this.messError = messError;
	}

	public Boolean getError() {
		return error;
	}

	public void setError(Boolean error) {
		this.error = error;
	}

	public void setPecOuDTO(PecOutDTO pecOut) {
		this.pecOut = pecOut;
	}
	public PecOutDTO getPecOutDTO(){
		return this.pecOut;
	}
}
