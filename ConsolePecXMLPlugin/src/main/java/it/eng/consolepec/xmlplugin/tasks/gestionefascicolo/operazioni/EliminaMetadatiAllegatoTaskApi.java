package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni;

import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;

import java.util.List;

/**
 * @author GiacomoFM
 * @since 14/mar/2018
 */
public interface EliminaMetadatiAllegatoTaskApi extends ITaskApi {

	void eliminaMetadatiAllegato(final Allegato allegato, final List<String> nomiMetadati);

}
