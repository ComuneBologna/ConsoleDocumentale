package it.eng.consolepec.client.impl;

import java.util.List;

import it.eng.cobo.consolepec.commons.datigenerici.IndirizzoEmail;
import it.eng.consolepec.client.AbstractConsolePecClient;
import it.eng.consolepec.client.ClientInvoker;
import it.eng.consolepec.client.IndirizziEmailRubricaClient;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.servicename.ServiceNamesUtil;

public class IndirizziEmailRubricaClientImpl extends AbstractConsolePecClient implements IndirizziEmailRubricaClient {

	public IndirizziEmailRubricaClientImpl(ClientInvoker clientInvoker) {
		super(clientInvoker);
	}

	@Override
	public boolean inserisci(IndirizzoEmail indirizzoEmail, Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.INDIRIZZI_EMAIL_RUBRICA_INSERT, utente, indirizzoEmail);
	}

	@Override
	public List<IndirizzoEmail> getIndirizziEmail(Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.INDIRIZZI_EMAIL_RUBRICA_GET, utente);
	}

}
