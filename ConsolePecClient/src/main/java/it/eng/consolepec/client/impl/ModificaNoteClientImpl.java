package it.eng.consolepec.client.impl;

import it.eng.consolepec.client.AbstractConsolePecClient;
import it.eng.consolepec.client.ClientInvoker;
import it.eng.consolepec.client.ModificaNoteClient;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.spagicclient.servicename.ServiceNamesUtil;

public class ModificaNoteClientImpl extends AbstractConsolePecClient implements ModificaNoteClient {

	public ModificaNoteClientImpl(ClientInvoker clientInvoker) {
		super(clientInvoker);
	}

	@Override
	public LockedPratica aggiungiNote(String idDocumentale, String note, Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.AGGIUNGI_NOTE, utente, idDocumentale, note);
	}

	@Override
	public LockedPratica modificaNote(String idDocumentale, String note, Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.MODIFICA_NOTE, utente, idDocumentale, note);
	}

}
