package it.eng.consolepec.client.impl;

import it.eng.consolepec.client.AbstractConsolePecClient;
import it.eng.consolepec.client.ClientInvoker;
import it.eng.consolepec.client.EstraiEMLPecClient;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.spagicclient.servicename.ServiceNamesUtil;

public class EstraiEMLPecClientImpl extends AbstractConsolePecClient implements EstraiEMLPecClient {

	public EstraiEMLPecClientImpl(ClientInvoker clientInvoker) {
		super(clientInvoker);
	}

	@Override
	public LockedPratica estraiEMLPec(String idDocumentalePEC, String idDocumentalePratica, Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.ESTRAI_EML_PEC, utente, idDocumentalePEC, idDocumentalePratica);
	}

}
