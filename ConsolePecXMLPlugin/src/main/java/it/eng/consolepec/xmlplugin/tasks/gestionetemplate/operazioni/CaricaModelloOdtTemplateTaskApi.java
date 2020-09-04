package it.eng.consolepec.xmlplugin.tasks.gestionetemplate.operazioni;

import it.eng.cobo.consolepec.commons.exception.ApplicationException;
import it.eng.cobo.consolepec.commons.exception.InvalidArgumentException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.factory.TaskObserver.AggiungiAllegato;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.ITaskApi;

public interface CaricaModelloOdtTemplateTaskApi extends ITaskApi {

	public void caricaODT(Allegato doc, AggiungiAllegato handler) throws ApplicationException, InvalidArgumentException;

}
