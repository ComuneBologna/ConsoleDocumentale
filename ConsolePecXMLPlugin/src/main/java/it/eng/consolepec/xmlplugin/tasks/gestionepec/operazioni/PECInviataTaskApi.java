package it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni;

import java.util.Date;

import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.ITaskApi;

public interface PECInviataTaskApi extends ITaskApi {

	void pecInviata(String messageId, Date dataInvio);
}
