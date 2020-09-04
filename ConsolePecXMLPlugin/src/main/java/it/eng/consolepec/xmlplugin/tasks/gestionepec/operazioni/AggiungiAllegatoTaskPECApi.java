package it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni;

import it.eng.cobo.consolepec.commons.exception.ApplicationException;
import it.eng.cobo.consolepec.commons.exception.InvalidArgumentException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.factory.Pratica;
import it.eng.consolepec.xmlplugin.factory.TaskObserver.AggiungiAllegato;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.ITaskApi;

public interface AggiungiAllegatoTaskPECApi extends ITaskApi {

	public void aggiungiAllegato(Allegato allegato, AggiungiAllegato handler) throws ApplicationException, InvalidArgumentException;

	public void aggiungiAllegato(Allegato allegato, Pratica<?> pratica, AggiungiAllegato handler) throws ApplicationException, InvalidArgumentException;

}
