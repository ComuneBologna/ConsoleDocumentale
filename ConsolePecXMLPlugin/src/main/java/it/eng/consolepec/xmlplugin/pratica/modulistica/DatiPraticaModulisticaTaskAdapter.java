package it.eng.consolepec.xmlplugin.pratica.modulistica;

import it.eng.consolepec.xmlplugin.factory.DatiPraticaTaskAdapter;
import it.eng.consolepec.xmlplugin.pratica.modulistica.DatiModulistica.ProtocollazionePraticaModulistica;
import it.eng.consolepec.xmlplugin.pratica.modulistica.DatiModulistica.Stato;

/**
 * Classe wrapper per modificare l'accesso a DatiModulistica
 * 
 * 
 */

public class DatiPraticaModulisticaTaskAdapter extends DatiPraticaTaskAdapter {
	private DatiModulistica datiModulistica;

	DatiPraticaModulisticaTaskAdapter(DatiModulistica dp) {
		super(dp);
		this.datiModulistica = dp;
	}
	
	public void setStato(Stato nuovo){
		datiModulistica.setStato(nuovo);
	}

	public void setProtocollazionePaticaModulistica(ProtocollazionePraticaModulistica protocollazionePraticaModulistica) {
		if (datiModulistica.getProtocollazionePraticaModulistica()!= null)
			throw new RuntimeException("La pratica e' gia' protocollata");
		datiModulistica.setProtocollazionePraticaModulistica(protocollazionePraticaModulistica);
	}


}
