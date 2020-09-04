package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni;

import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.pratica.email.PraticaEmailOut;

public interface AggiungiReinoltroAProtocollazione extends ITaskApi {

	public void aggiungiReinoltroAProtocollazione(PraticaEmailOut praticaEmailOut, String numeroProtocollo, int anno) throws PraticaException;

}
