package it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni;

import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.ITaskApi;

public interface PECInAttesaDiPresaInCaricoTaskApi extends ITaskApi {

	void pecInAttesaDiPresaInCarico(Integer identificativoEmail);
}
