package it.eng.consolepec.client.impl;

import it.eng.cobo.consolepec.commons.services.ServiceResponse;
import it.eng.consolepec.client.AbstractConsolePecClient;
import it.eng.consolepec.client.ClientInvoker;
import it.eng.consolepec.client.EmissionePermessoClient;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.spagicclient.servicename.ServiceNamesUtil;

public class EmissionePermessoClientImpl extends AbstractConsolePecClient implements EmissionePermessoClient {

	public EmissionePermessoClientImpl(ClientInvoker clientInvoker) {
		super(clientInvoker);
	}

	@Override
	public ServiceResponse<LockedPratica> emettiPermesso(Utente utente, String idPratica) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.EMISSIONE_PERMESSO, utente, idPratica);
	}

}
