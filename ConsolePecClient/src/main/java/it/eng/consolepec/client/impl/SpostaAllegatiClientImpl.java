package it.eng.consolepec.client.impl;

import java.util.List;

import it.eng.consolepec.client.AbstractConsolePecClient;
import it.eng.consolepec.client.ClientInvoker;
import it.eng.consolepec.client.SpostaAllegatiClient;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.spagicclient.servicename.ServiceNamesUtil;

public class SpostaAllegatiClientImpl extends AbstractConsolePecClient implements SpostaAllegatiClient {

	public SpostaAllegatiClientImpl(ClientInvoker clientInvoker) {
		super(clientInvoker);
	}

	@Override
	public LockedPratica spostaAllegati(String idDocumentaleSorgente, String idDocumentaleDestinatario, List<String> nomiAllegati, Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.SPOSTA_ALLEGATI, utente, idDocumentaleSorgente, idDocumentaleDestinatario, nomiAllegati);
	}

}
