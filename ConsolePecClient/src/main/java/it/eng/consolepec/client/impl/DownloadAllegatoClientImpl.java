package it.eng.consolepec.client.impl;

import it.eng.cobo.consolepec.commons.atti.AllegatoAlfresco;
import it.eng.consolepec.client.AbstractConsolePecClient;
import it.eng.consolepec.client.ClientInvoker;
import it.eng.consolepec.client.DownloadAllegatoClient;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.servicename.ServiceNamesUtil;

/**
 *
 * @author biagiot
 *
 */
public class DownloadAllegatoClientImpl extends AbstractConsolePecClient implements DownloadAllegatoClient {

	public DownloadAllegatoClientImpl(ClientInvoker clientInvoker) {
		super(clientInvoker);
	}

	@Override
	public AllegatoAlfresco downloadAllegato(Utente utente, String pathFile, String nomeFile, Float versione) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.DOWNLOAD_ALLEGATO_VERSIONATO, utente, pathFile, nomeFile, versione);
	}

	@Override
	public AllegatoAlfresco downloadAllegatoSbustato(Utente utente, String pathFile, String nomeFile, Float versione) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.DOWNLOAD_ALLEGATO_VERSIONATO_SBUSTATO, utente, pathFile, nomeFile, versione);
	}

	@Override
	public AllegatoAlfresco downloadAllegato(Utente utente, String pathPratica, String nomeFile, String versione) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.DOWNLOAD_ALLEGATO_VERSIONATO_PRATICA, utente, pathPratica, nomeFile, versione);
	}

}
