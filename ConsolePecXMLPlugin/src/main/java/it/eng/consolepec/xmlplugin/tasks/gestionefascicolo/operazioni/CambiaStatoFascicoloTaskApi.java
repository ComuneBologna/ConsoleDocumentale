package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni;

import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Stato;

public interface CambiaStatoFascicoloTaskApi extends ITaskApi {
	
	
	public void cambiaStato(Stato stato) throws PraticaException;

}
