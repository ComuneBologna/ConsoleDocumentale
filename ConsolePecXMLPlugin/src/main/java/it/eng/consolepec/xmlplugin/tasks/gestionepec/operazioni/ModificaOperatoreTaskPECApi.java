package it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni;

import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.ITaskApi;

public interface ModificaOperatoreTaskPECApi extends ITaskApi {

	public void modificaOperatore(String operatore) throws PraticaException;

}
