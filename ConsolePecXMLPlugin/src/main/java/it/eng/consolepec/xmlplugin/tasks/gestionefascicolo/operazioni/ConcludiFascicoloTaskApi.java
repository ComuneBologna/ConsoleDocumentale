package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni;

import it.eng.consolepec.xmlplugin.exception.PraticaException;

public interface ConcludiFascicoloTaskApi extends ITaskApi {

	public void concludiFascicolo() throws PraticaException;

}
