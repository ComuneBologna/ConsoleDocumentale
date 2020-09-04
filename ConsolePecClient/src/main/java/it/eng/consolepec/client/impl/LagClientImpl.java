package it.eng.consolepec.client.impl;

import it.eng.cobo.consolepec.commons.rubrica.Anagrafica;
import it.eng.consolepec.client.AbstractConsolePecClient;
import it.eng.consolepec.client.ClientInvoker;
import it.eng.consolepec.client.LagClient;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.servicename.ServiceNamesUtil;

/**
 * @author GiacomoFM
 * @since 14/nov/2018
 */
public class LagClientImpl extends AbstractConsolePecClient implements LagClient {

	public LagClientImpl(ClientInvoker clientInvoker) {
		super(clientInvoker);
	}

	@Override
	public Anagrafica importaAnagrafica(String cf, Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.LAG_IMPORTA_CODICE_FISCALE, utente, cf);
	}

}
