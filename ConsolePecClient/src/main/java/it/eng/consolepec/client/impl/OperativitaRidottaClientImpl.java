package it.eng.consolepec.client.impl;

import java.util.List;

import it.eng.cobo.consolepec.commons.pratica.OperativitaRidotta.OperazioneOperativitaRidotta.TipoAccesso;
import it.eng.consolepec.client.AbstractConsolePecClient;
import it.eng.consolepec.client.ClientInvoker;
import it.eng.consolepec.client.OperativitaRidottaClient;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.spagicclient.servicename.ServiceNamesUtil;

public class OperativitaRidottaClientImpl extends AbstractConsolePecClient implements OperativitaRidottaClient {

	public OperativitaRidottaClientImpl(ClientInvoker clientInvoker) {
		super(clientInvoker);
	}

	@Override
	public LockedPratica applicaOperativitaRidotta(String idDocumentale, String operazione, List<TipoAccesso> accessiConsentiti, Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.APPLICA_OPERATIVITA_RIDOTTA, utente, idDocumentale, operazione, accessiConsentiti);
	}

}
