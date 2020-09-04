package it.eng.portlet.consolepec.gwt.server.fascicolo;

import it.eng.consolepec.xmlplugin.pratica.comunicazione.DatiComunicazione.Stato;
import it.eng.portlet.consolepec.gwt.shared.model.ComunicazioneDTO.StatoDTO;

public class StatiComunicazioneTranslator {
	
	public static StatoDTO getStatoDTOFromStato(Stato stato) {
		switch (stato) {
		case IN_GESTIONE:
			return StatoDTO.IN_GESTIONE;
		case TERMINATA:
			return StatoDTO.TERMINATA;
		default:
			throw new IllegalArgumentException("Stato non valido:" + stato);
		}
	}

	
}
