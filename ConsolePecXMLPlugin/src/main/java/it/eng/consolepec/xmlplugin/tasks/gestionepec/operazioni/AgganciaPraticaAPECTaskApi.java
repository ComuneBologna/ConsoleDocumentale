package it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni;

import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.factory.Pratica;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.ITaskApi;

public interface AgganciaPraticaAPECTaskApi extends ITaskApi {

	public void agganciaPraticaAPEC(Pratica<?> pratica) throws PraticaException;

}
