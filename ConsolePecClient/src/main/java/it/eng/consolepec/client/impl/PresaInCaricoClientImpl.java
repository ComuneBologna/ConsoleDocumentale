package it.eng.consolepec.client.impl;

import it.eng.consolepec.client.AbstractConsolePecClient;
import it.eng.consolepec.client.ClientInvoker;
import it.eng.consolepec.client.PresaInCaricoClient;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.spagicclient.servicename.ServiceNamesUtil;

public class PresaInCaricoClientImpl extends AbstractConsolePecClient implements PresaInCaricoClient {

	public PresaInCaricoClientImpl(ClientInvoker clientInvoker) {
		super(clientInvoker);
	}

	@Override
	public LockedPratica prendiInCarico(String idDocumentale, Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.PRENDI_IN_CARICO, utente, idDocumentale);
	}

	@Override
	public LockedPratica rilasciaInCarico(String idDocumentale, Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.RILASCIA_IN_CARICO, utente, idDocumentale);
	}

}
