package it.eng.consolepec.client.impl;

import java.util.List;
import java.util.Map;

import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo;
import it.eng.consolepec.client.AbstractConsolePecClient;
import it.eng.consolepec.client.ClientInvoker;
import it.eng.consolepec.client.GestioneAllegatoClient;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.spagicclient.servicename.ServiceNamesUtil;

/**
 * @author GiacomoFM
 * @since 15/mar/2018
 */
public class GestioneAllegatoClientImpl extends AbstractConsolePecClient implements GestioneAllegatoClient {

	public GestioneAllegatoClientImpl(ClientInvoker clientInvoker) {
		super(clientInvoker);
	}

	@Override
	public void inserisciModificaDatiAggiuntivi(Utente utente, String idDocumentale, String nomeAllegato, List<DatoAggiuntivo> datiAggiuntivi) throws SpagicClientException {
		clientInvoker.invoke(ServiceNamesUtil.MODIFICA_METADATI_ALLEGATO, utente, idDocumentale, nomeAllegato, datiAggiuntivi);
	}

	@Override
	public void eliminaDatiAggiuntivi(Utente utente, String idDocumentale, String nomeAllegato, List<String> nomiDatiAggiuntivi) throws SpagicClientException {
		clientInvoker.invoke(ServiceNamesUtil.ELIMINA_METADATI_ALLEGATO, utente, idDocumentale, nomeAllegato, nomiDatiAggiuntivi);
	}

	@Override
	public LockedPratica modificaTipologieAllegati(Utente utente, String idDocumentale, Map<String, List<String>> mappaAllegatoTipologie) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.MODIFICA_TIPOLOGIE_ALLEGATI, utente, idDocumentale, mappaAllegatoTipologie);
	}

}
