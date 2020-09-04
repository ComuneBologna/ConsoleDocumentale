package it.eng.portlet.consolepec.gwt.shared.action.inviomassivo;

import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;
import it.eng.portlet.consolepec.gwt.shared.model.ComunicazioneDTO;

public class CreaComunicazioneAction extends LiferayPortletUnsecureActionImpl<CreaComunicazioneActionResult> {

	private ComunicazioneDTO comunicazione;
	
	public CreaComunicazioneAction() {
	}

	public CreaComunicazioneAction(ComunicazioneDTO comunicazione) {
		super();
		this.comunicazione = comunicazione;
	}

	public ComunicazioneDTO getComunicazione() {
		return comunicazione;
	}

	
	
	
}
