package it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni;

import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.ITaskApi;

public interface RimuoviAllegatoTaskPECApi extends ITaskApi{
	
	public void rimuoviAllegato(Allegato allegato);

}
