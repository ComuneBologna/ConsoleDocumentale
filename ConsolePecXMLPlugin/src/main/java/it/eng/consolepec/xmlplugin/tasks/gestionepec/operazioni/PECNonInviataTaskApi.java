package it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni;

import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.ITaskApi;

public interface PECNonInviataTaskApi extends ITaskApi {

	void pecNonInviata(String messageId);
}
