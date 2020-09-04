package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni;

import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;

public interface EliminaCollegamentoFascicoloTaskApi extends ITaskApi {

	public void eliminaCollegamento(Fascicolo fascicolo) throws PraticaException;
}
