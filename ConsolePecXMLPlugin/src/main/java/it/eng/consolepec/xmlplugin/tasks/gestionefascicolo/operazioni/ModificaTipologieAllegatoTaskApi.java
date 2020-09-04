package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni;

import java.util.List;

import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;

/**
 * @author GiacomoFM
 * @since 12/dic/2018
 */
public interface ModificaTipologieAllegatoTaskApi extends ITaskApi {

	void modificaTipologieAllegato(Allegato allegato, List<String> tipologie) throws PraticaException;

}
