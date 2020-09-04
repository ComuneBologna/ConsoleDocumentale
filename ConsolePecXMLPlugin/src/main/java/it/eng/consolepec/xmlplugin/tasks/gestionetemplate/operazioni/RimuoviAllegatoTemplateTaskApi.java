package it.eng.consolepec.xmlplugin.tasks.gestionetemplate.operazioni;

import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.ITaskApi;

public interface RimuoviAllegatoTemplateTaskApi extends ITaskApi {

	public void rimuoviAllegato(Allegato allegato);

}
