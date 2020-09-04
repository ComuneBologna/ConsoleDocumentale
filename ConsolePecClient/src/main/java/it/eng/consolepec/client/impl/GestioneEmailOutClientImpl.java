package it.eng.consolepec.client.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.eng.cobo.consolepec.commons.services.InputStreamMapper;
import it.eng.consolepec.client.AbstractConsolePecClient;
import it.eng.consolepec.client.ClientInvoker;
import it.eng.consolepec.client.GestioneEmailOutClient;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.spagicclient.servicename.ServiceNamesUtil;

public class GestioneEmailOutClientImpl extends AbstractConsolePecClient implements GestioneEmailOutClient {

	public GestioneEmailOutClientImpl(ClientInvoker clientInvoker) {
		super(clientInvoker);
	}

	@Override
	public LockedPratica inviaBozza(String idDocumentaleBozza, Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.INVIA_BOZZA, utente, idDocumentaleBozza);
	}

	@Override
	public LockedPratica eliminaBozza(String idDocumentaleBozza, String idDocumentaleFascicolo, Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.ELIMINA_BOZZA, utente, idDocumentaleBozza, idDocumentaleFascicolo);

	}

	@Override
	public LockedPratica creaBozza(String idDocumentaleFascicolo, String assegnatario, String mittente, List<String> destinatari, List<String> destinatariCC, String oggetto, String body, String firma,
			String replyTo, List<String> allegatiReplyTO, String messageIDReinoltro, Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.CREA_BOZZA, utente, idDocumentaleFascicolo, assegnatario, mittente, destinatari, destinatariCC, oggetto, body, firma, replyTo, allegatiReplyTO,
				new ArrayList<InputStreamMapper>(), messageIDReinoltro);
	}

	@Override
	public LockedPratica creaBozzaDaTemplate(String idDocumentaleFascicolo, String idDocumentaleTemplate, Map<String, Object> campiTemplate, String assegnatario, String idDocumentaleReplyTO,
			Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.CREA_BOZZA_DA_TEMPLATE_2, utente, idDocumentaleFascicolo, idDocumentaleTemplate, campiTemplate, assegnatario, idDocumentaleReplyTO);
	}

	@Override
	public LockedPratica modificaBozza(String idDocumentaleFascicolo, String idDocumentaleBozza, String mittente, List<String> destinatari, List<String> destinatariCC, String oggetto, String body,
			String firma, Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.MODIFICA_BOZZA, utente, idDocumentaleFascicolo, idDocumentaleBozza, mittente, destinatari, destinatariCC, oggetto, body, firma);
	}

}
