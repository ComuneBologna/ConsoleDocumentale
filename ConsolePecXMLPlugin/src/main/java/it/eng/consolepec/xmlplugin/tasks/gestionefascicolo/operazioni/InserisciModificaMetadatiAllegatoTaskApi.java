package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni;

import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;

import java.util.List;

/**
 * @author GiacomoFM
 * @since 14/mar/2018
 */
public interface InserisciModificaMetadatiAllegatoTaskApi extends ITaskApi {

	void inserisciModificaMetadatiAllegato(final Allegato allegato, final List<DatoAggiuntivo> datiAggiuntivi);

}
