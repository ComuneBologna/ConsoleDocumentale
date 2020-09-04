package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni;

import java.util.List;

import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;

public interface CambiaVisibilitaAllegatoTaskApi extends ITaskApi {
	
	public void cambiaVisibilitaAllegato(Allegato allegato, List<String> gruppi);
}
