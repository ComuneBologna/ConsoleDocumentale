package it.eng.portlet.consolepec.gwt.shared.action.pec;

import it.eng.portlet.consolepec.gwt.shared.model.PecOutDTO;

import com.gwtplatform.dispatch.shared.Result;

public class ReinoltroResult implements Result {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1475166984724542300L;

	public ReinoltroResult() {
	}

	public ReinoltroResult(PecOutDTO pecOutDTO) {
		super();
		this.pecOutDTO = pecOutDTO;
	}

	public ReinoltroResult(String messageError) {
		super();
		this.messageError = messageError;
		this.error = true;
	}

	private PecOutDTO pecOutDTO;
	private String messageError;
	private boolean error;

	public PecOutDTO getPecOutDTO() {
		return pecOutDTO;
	}

	public void setPecOutDTO(PecOutDTO pecOutDTO) {
		this.pecOutDTO = pecOutDTO;
	}

	public String getMessageError() {
		return messageError;
	}

	public void setMessageError(String messageError) {
		this.messageError = messageError;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

}
