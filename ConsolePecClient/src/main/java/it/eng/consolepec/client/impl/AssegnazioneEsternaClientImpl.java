package it.eng.consolepec.client.impl;

import java.util.List;

import it.eng.consolepec.client.AbstractConsolePecClient;
import it.eng.consolepec.client.AssegnazioneEsternaClient;
import it.eng.consolepec.client.ClientInvoker;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.spagicclient.servicename.ServiceNamesUtil;

public class AssegnazioneEsternaClientImpl extends AbstractConsolePecClient implements AssegnazioneEsternaClient {

	public AssegnazioneEsternaClientImpl(ClientInvoker clientInvoker) {
		super(clientInvoker);
	}

	@Override
	public LockedPratica concludiAssegnazioneEsterna(String idDocumentale, Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.CONCLUDI_ASSEGNAZIONE_ESTERNA, utente, idDocumentale);
	}

	@Override
	public LockedPratica assegna(String idDocumentale, List<String> destinatari, String testo, List<String> operazioni, Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.AVVIA_ASSEGNAZIONE_ESTERNA, utente, idDocumentale, destinatari, testo, operazioni);
	}

	@Override
	public LockedPratica modificaAssegnazioneEsterna(String idDocumentale, List<String> operazioni, Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.MODIFICA_ASSEGNAZIONE_ESTERNA, utente, idDocumentale, operazioni);
	}

}
