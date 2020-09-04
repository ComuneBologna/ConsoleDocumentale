package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni;

import it.eng.cobo.consolepec.commons.atti.DocumentoTaskFirma;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;

/**
 * @author GiacomoFM
 * @since 08/mar/2018
 */
public interface EstrazioneTaskFirmaTaskApi extends ITaskApi {

	DocumentoTaskFirma estraiTaskFirma(Fascicolo fascicolo, Integer idTaskFirma);

}
