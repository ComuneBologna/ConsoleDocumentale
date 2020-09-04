package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni;

import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.factory.TaskObserver.AggiungiAllegato;

/**
 *
 * @author biagiot
 *
 */
public interface VersionaAllegatoTaskFirmaApiTask extends ITaskApi {

	public void versionaAllegato(Allegato allegato, boolean protocollato, AggiungiAllegato handler) throws Exception;
}
