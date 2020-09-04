
package it.eng.portlet.consolepec.gwt.shared.action.inviomassivo;

import it.eng.portlet.consolepec.gwt.shared.model.ComunicazioneDTO;

import com.gwtplatform.dispatch.shared.Result;

public class CreaComunicazioneActionResult implements Result {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String messageError = "";
	private Boolean error = false;
	private ComunicazioneDTO comunicazione;
	
	
	public CreaComunicazioneActionResult() {

	}

	public CreaComunicazioneActionResult(String messageError) {
		super();
		this.messageError = messageError;
		this.error = true;
	}
	
	public CreaComunicazioneActionResult(ComunicazioneDTO comunicazione) {
		super();
		this.comunicazione = comunicazione;
		this.error = false;
	}

	public String getMessageError() {
		return messageError;
	}

	public Boolean getError() {
		return error;
	}

	public ComunicazioneDTO getComunicazione() {
		return comunicazione;
	}
	
	
}
