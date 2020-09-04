package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;

/**
 * @author GiacomoFM
 * @since 12/lug/2017
 */
public interface ModificaFascicoloTaskApi extends ITaskApi {

	boolean modifica(String titolo, TipologiaPratica tipologiaPratica);

}
