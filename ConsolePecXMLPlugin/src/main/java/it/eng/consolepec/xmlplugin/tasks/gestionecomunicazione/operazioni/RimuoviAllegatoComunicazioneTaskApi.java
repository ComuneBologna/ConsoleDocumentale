package it.eng.consolepec.xmlplugin.tasks.gestionecomunicazione.operazioni;

import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.ITaskApi;

public interface RimuoviAllegatoComunicazioneTaskApi extends ITaskApi {

	public void rimuoviAllegato(Allegato allegato);

}
