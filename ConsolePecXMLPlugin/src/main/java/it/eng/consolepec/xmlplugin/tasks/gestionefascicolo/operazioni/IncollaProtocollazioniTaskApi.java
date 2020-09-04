package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni;

import it.eng.consolepec.xmlplugin.factory.Pratica;
import it.eng.consolepec.xmlplugin.factory.TaskObserver.IncollaAllegatoHandler;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TagliaProtocollazioniTaskApi.TagliaProtocollazioniOutput;

public interface IncollaProtocollazioniTaskApi extends ITaskApi {

	public void incollaProtocollazioni(TagliaProtocollazioniOutput tagliaProtocollazioniOutput, Pratica<?> praticaSorgente, IncollaAllegatoHandler allegatoHandler) throws Exception;
}
