package it.eng.consolepec.client.impl;

import java.util.List;

import it.eng.consolepec.client.AbstractConsolePecClient;
import it.eng.consolepec.client.CambiaVisibilitaFascicoloClient;
import it.eng.consolepec.client.ClientInvoker;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.spagicclient.servicename.ServiceNamesUtil;

public class CambiaVisibilitaFascicoloClientImpl extends AbstractConsolePecClient implements CambiaVisibilitaFascicoloClient {

	public CambiaVisibilitaFascicoloClientImpl(ClientInvoker clientInvoker) {
		super(clientInvoker);
	}

	@Override
	public LockedPratica cambiaVisibilitaFascicolo(String idDocumentale, List<String> gruppi, Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.MODIFICA_VISIBILITA_FASCICOLO, utente, idDocumentale, gruppi);
	}

}
