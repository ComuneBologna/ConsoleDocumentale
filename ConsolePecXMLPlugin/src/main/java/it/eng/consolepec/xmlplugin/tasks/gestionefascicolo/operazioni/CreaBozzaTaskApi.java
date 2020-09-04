package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni;

import it.eng.consolepec.xmlplugin.pratica.email.PraticaEmailOut;

public interface CreaBozzaTaskApi extends ITaskApi {

	void creaBozza(PraticaEmailOut bozza);

}
