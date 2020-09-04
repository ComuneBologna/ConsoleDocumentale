package it.eng.consolepec.xmlplugin.tasks.operazioni;

import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.ITaskApi;

public interface ModificaNoteTaskApi extends ITaskApi {

	void aggiungiNote(String note);

	void modificaNote(String note);

}
