package it.eng.portlet.consolepec.gwt.shared.action.inviomassivo;

import it.eng.portlet.consolepec.gwt.shared.model.ComunicazioneDTO;

import com.gwtplatform.dispatch.shared.Result;

public class NuovoInvioComunicazioneActionResult implements Result {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String messError;
	private boolean error;
	private ComunicazioneDTO comunicazione;
	
	protected NuovoInvioComunicazioneActionResult() {
		
	}
	
	public NuovoInvioComunicazioneActionResult(ComunicazioneDTO comunicazione) {
		super();
		this.comunicazione = comunicazione;
	}

	public NuovoInvioComunicazioneActionResult(String messError) {
		this.messError = messError;
		this.error = true;
		
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
