package it.eng.consolepec.client.impl;

import java.util.List;

import it.eng.consolepec.client.AbstractConsolePecClient;
import it.eng.consolepec.client.ClientInvoker;
import it.eng.consolepec.client.RiassegnaPraticaClient;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.spagicclient.servicename.ServiceNamesUtil;

/**
 *
 * @author biagiot
 *
 */
public class RiassegnaPraticaClientImpl extends AbstractConsolePecClient implements RiassegnaPraticaClient {

	public RiassegnaPraticaClientImpl(ClientInvoker clientInvoker) {
		super(clientInvoker);
	}

	@Override
	public LockedPratica riassegna(String idDocumentale, String nuovoAssegnatario, List<String> indirizziNotifica, String operatore, String note, Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.RIASSEGNA_PRATICA, utente, idDocumentale, nuovoAssegnatario, indirizziNotifica, operatore, note);
	}

}
