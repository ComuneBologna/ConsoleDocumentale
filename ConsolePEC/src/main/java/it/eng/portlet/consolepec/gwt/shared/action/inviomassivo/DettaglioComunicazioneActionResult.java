package it.eng.portlet.consolepec.gwt.shared.action.inviomassivo;

import it.eng.portlet.consolepec.gwt.shared.model.ComunicazioneDTO;

import com.gwtplatform.dispatch.shared.Result;

public class DettaglioComunicazioneActionResult implements Result {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String messError;
	private boolean error;
	
	private ComunicazioneDTO comunicazione;
	
	protected DettaglioComunicazioneActionResult() {
		
	}

	public DettaglioComunicazioneActionResult(String messError, Boolean error) {
		this.messError = messError;
		this.error = error;
		
	}
	
	public DettaglioComunicazioneActionResult(ComunicazioneDTO comunicazione) {
		super();
		this.comunicazione = comunicazione;
	}

	public String getMessError() {
		return messError;
	}

	public Boolean getError() {
		return error;
	}

	public ComunicazioneDTO getComunicazione() {
		return comunicazione;
	}


}
