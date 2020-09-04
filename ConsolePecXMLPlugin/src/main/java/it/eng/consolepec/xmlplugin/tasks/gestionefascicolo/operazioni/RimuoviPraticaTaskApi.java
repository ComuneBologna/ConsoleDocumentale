package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni;

import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica;
import it.eng.consolepec.xmlplugin.factory.Pratica;

public interface RimuoviPraticaTaskApi extends ITaskApi {

	public void removePratica(Pratica<? extends DatiPratica> pratica) throws PraticaException;

}
