package it.eng.portlet.consolepec.gwt.shared.action.inviomassivo;

import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;

public class DettaglioComunicazioneAction extends LiferayPortletUnsecureActionImpl<DettaglioComunicazioneActionResult> {

	private String identificativoComunicazione;
	
	protected DettaglioComunicazioneAction() {
		// For serialization only
	}

	public DettaglioComunicazioneAction(String identificativoComunicazione) {
		super();
		this.identificativoComunicazione = identificativoComunicazione;
	}

	public String getIdentificativoComunicazione() {
		return identificativoComunicazione;
	}

	
	
}
