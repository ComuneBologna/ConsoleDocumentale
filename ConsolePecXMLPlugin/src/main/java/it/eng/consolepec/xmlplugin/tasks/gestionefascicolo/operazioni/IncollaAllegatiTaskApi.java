package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni;

import it.eng.cobo.consolepec.commons.exception.ApplicationException;
import it.eng.cobo.consolepec.commons.exception.InvalidArgumentException;
import it.eng.consolepec.xmlplugin.factory.Pratica;
import it.eng.consolepec.xmlplugin.factory.TaskObserver.IncollaAllegatoHandler;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TagliaAllegatiTaskApi.TagliaAllegatiOutput;

/**
 * 
 * @author biagiot
 *
 */
public interface IncollaAllegatiTaskApi extends ITaskApi {

	public void incollaAllegati(TagliaAllegatiOutput tagliaAllegatiOutput, Pratica<?> praticaSorgente, IncollaAllegatoHandler handler) throws ApplicationException, InvalidArgumentException;

}
