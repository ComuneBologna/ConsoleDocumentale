package it.eng.consolepec.client.impl;

import java.util.List;

import it.eng.consolepec.client.AbstractConsolePecClient;
import it.eng.consolepec.client.CambiaVisibilitaAllegatoClient;
import it.eng.consolepec.client.ClientInvoker;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.spagicclient.servicename.ServiceNamesUtil;

public class CambiaVisibilitaAllegatoClientImpl extends AbstractConsolePecClient implements CambiaVisibilitaAllegatoClient {

	public CambiaVisibilitaAllegatoClientImpl(ClientInvoker clientInvoker) {
		super(clientInvoker);
	}

	@Override
	public LockedPratica cambiaVisibilitaAllegato(String idDocumentale, String nomeAllegato, List<String> gruppi, Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.MODIFICA_VISIBILITA_ALLEGATO, utente, idDocumentale, nomeAllegato, gruppi);
	}

}
