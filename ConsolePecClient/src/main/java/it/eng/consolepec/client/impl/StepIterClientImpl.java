package it.eng.consolepec.client.impl;

import it.eng.consolepec.client.AbstractConsolePecClient;
import it.eng.consolepec.client.ClientInvoker;
import it.eng.consolepec.client.StepIterClient;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.spagicclient.servicename.ServiceNamesUtil;

/**
 *
 * @author biagiot
 *
 */
public class StepIterClientImpl extends AbstractConsolePecClient implements StepIterClient {

	public StepIterClientImpl(ClientInvoker clientInvoker) {
		super(clientInvoker);
	}

	@Override
	public LockedPratica cambiaStepIter(String idDocumentale, String stepIter, Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.CAMBIA_STEP_ITER, utente, idDocumentale, stepIter);
	}

}
