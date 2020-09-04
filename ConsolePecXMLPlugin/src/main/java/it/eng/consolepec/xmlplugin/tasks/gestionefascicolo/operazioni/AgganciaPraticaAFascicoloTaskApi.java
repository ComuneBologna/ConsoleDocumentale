package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni;

import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.factory.Pratica;

public interface AgganciaPraticaAFascicoloTaskApi extends ITaskApi {

	public void agganciaPraticaAFascicolo(Pratica<?> pratica) throws PraticaException;

}
