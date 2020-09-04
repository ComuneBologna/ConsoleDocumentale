package it.eng.consolepec.client.impl;

import java.util.List;

import it.eng.consolepec.client.AbstractConsolePecClient;
import it.eng.consolepec.client.ClientInvoker;
import it.eng.consolepec.client.SpostaProtocollazioniClient;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.spagicclient.servicename.ServiceNamesUtil;

public class SpostaProtocollazioniClientImpl extends AbstractConsolePecClient implements SpostaProtocollazioniClient {

	public SpostaProtocollazioniClientImpl(ClientInvoker clientInvoker) {
		super(clientInvoker);
	}

	@Override
	public LockedPratica spostaProtocollazioni(String idDocumentaleSorgente, String idDocumentaleDestinatario, List<String> allegatiProtocollati, List<String> pathPraticheProtocollate,
			Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.SPOSTA_PROTOCOLLAZIONI, utente, idDocumentaleSorgente, idDocumentaleDestinatario, allegatiProtocollati, pathPraticheProtocollate);
	}

}
