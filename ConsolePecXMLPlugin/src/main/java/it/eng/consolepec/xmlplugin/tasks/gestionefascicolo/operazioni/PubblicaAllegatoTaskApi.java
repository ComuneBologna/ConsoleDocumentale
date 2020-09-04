package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni;

import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;

import java.util.Date;

public interface PubblicaAllegatoTaskApi extends ITaskApi {

	public void pubblicaAllegato(Allegato allegato, Date inizioPubblicazione, Date finePubblicazione);

}
