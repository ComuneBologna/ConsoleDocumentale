package it.eng.consolepec.xmlplugin.pratica.comunicazione;

import it.eng.consolepec.xmlplugin.factory.DatiPraticaTaskAdapter;

/**
 * Classe wrapper per modificare l'accesso a DatiModulistica
 * 
 * 
 */

public class DatiComunicazioneTaskAdapter extends DatiPraticaTaskAdapter {
	private DatiComunicazione datiComunicazione;

	DatiComunicazioneTaskAdapter(DatiComunicazione dp) {
		super(dp);
		this.datiComunicazione = dp;
	}
	
	

}
