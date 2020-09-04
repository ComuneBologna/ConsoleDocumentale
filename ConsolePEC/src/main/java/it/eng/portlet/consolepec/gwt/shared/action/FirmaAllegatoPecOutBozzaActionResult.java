package it.eng.portlet.consolepec.gwt.shared.action;

import it.eng.portlet.consolepec.gwt.shared.model.PecOutDTO;

import com.gwtplatform.dispatch.shared.Result;

public class FirmaAllegatoPecOutBozzaActionResult implements Result {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5190289180513729059L;
	private Boolean error;
	private String messageError;
	private PecOutDTO pecOutDto;

	public FirmaAllegatoPecOutBozzaActionResult() {
		// For serialization only
	}

	public FirmaAllegatoPecOutBozzaActionResult(PecOutDTO pecout, String messageError, Boolean error) {
		this.error = error;
		this.messageError = messageError;
		this.pecOutDto = pecout;
	}

	public Boolean getError() {
		return error;
	}

	public String getMessageError() {
		return messageError;
	}

	public void setPecOutDto(PecOutDTO dto) {
		this.pecOutDto = dto;
	}

	public PecOutDTO getPecOutDto() {
		return pecOutDto;
	}
}
