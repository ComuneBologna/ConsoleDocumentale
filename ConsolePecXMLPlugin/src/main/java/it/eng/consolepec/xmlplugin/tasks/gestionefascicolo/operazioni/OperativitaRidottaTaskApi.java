package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni;

import java.util.List;

import it.eng.cobo.consolepec.commons.pratica.OperativitaRidotta.OperazioneOperativitaRidotta.TipoAccesso;

public interface OperativitaRidottaTaskApi extends ITaskApi {

	void applicaOperativitaRidotta(String operazione, List<TipoAccesso> accessiConsentiti);
}
