/**
 * 
 */
package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni;

import java.util.Map;

import it.eng.cobo.consolepec.commons.exception.ApplicationException;
import it.eng.cobo.consolepec.commons.exception.InvalidArgumentException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.factory.TaskObserver.AggiungiAllegato;

/**
 * @author roger
 * 
 */
public interface RicaricaAllegatoProtocollatoTaskApi extends ITaskApi {

	public void ricaricaAllegatoProtocollato(Allegato allegato, AggiungiAllegato handler, Map<String, String> campi) throws ApplicationException, InvalidArgumentException;

}
