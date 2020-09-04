package it.eng.consolepec.client;

import java.util.List;

import it.eng.cobo.consolepec.commons.pratica.OperativitaRidotta.OperazioneOperativitaRidotta.TipoAccesso;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;

public interface OperativitaRidottaClient {

	LockedPratica applicaOperativitaRidotta(String idDocumentale, String operazione, List<TipoAccesso> accessiConsentiti, Utente utente) throws SpagicClientException;
}
