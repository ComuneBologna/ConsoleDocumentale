package it.eng.consolepec.client.impl;

import it.eng.cobo.consolepec.commons.services.ServiceResponse;
import it.eng.consolepec.client.AbstractConsolePecClient;
import it.eng.consolepec.client.ArchiviaPraticaClient;
import it.eng.consolepec.client.ClientInvoker;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.spagicclient.servicename.ServiceNamesUtil;

/**
 *
 * @author biagiot
 *
 */
public class ArchiviaPraticaClientImpl extends AbstractConsolePecClient implements ArchiviaPraticaClient {

	public ArchiviaPraticaClientImpl(ClientInvoker clientInvoker) {
		super(clientInvoker);
	}

	@Override
	public ServiceResponse<LockedPratica> archiviaPratica(String idDocumentalePratica, Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.ARCHIVIA_PRATICA, utente, idDocumentalePratica);
	}
}
