package it.eng.consolepec.client.impl;

import it.eng.cobo.consolepec.commons.pratica.fascicolo.collegamenti.Permessi;
import it.eng.consolepec.client.AbstractConsolePecClient;
import it.eng.consolepec.client.ClientInvoker;
import it.eng.consolepec.client.CollegamentoPraticheClient;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.spagicclient.servicename.ServiceNamesUtil;

public class CollegamentoPraticheClientImpl extends AbstractConsolePecClient implements CollegamentoPraticheClient {

	public CollegamentoPraticheClientImpl(ClientInvoker clientInvoker) {
		super(clientInvoker);
	}

	@Override
	public LockedPratica updateCollegamento(String pathPraticaSorgente, String pathPraticaRemota, Permessi permessi, Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.COLLEGA_PRATICA, utente, pathPraticaSorgente, pathPraticaRemota, permessi);
	}

	@Override
	public LockedPratica updateCollegamentoGruppo(String pathPraticaSorgente, String pathPraticaRemota, Permessi permessi, String ruolo, Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.COLLEGA_PRATICA_GRUPPO, utente, pathPraticaSorgente, pathPraticaRemota, permessi, ruolo);
	}

}
