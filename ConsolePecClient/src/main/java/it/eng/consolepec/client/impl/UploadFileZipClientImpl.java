package it.eng.consolepec.client.impl;

import java.util.Arrays;

import it.eng.cobo.consolepec.commons.services.InputStreamMapper;
import it.eng.consolepec.client.AbstractConsolePecClient;
import it.eng.consolepec.client.ClientInvoker;
import it.eng.consolepec.client.UploadFileZipClient;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.spagicclient.servicename.ServiceNamesUtil;

/**
 * @author GiacomoFM
 * @since 13/mar/2019
 */
public class UploadFileZipClientImpl extends AbstractConsolePecClient implements UploadFileZipClient {

	public UploadFileZipClientImpl(ClientInvoker clientInvoker) {
		super(clientInvoker);
	}

	@Override
	public LockedPratica uploadZip(String pathFascicolo, InputStreamMapper inputStreamMapper, Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.UPLOAD_FILE_ZIP, utente, Arrays.asList(inputStreamMapper), pathFascicolo);
	}

}
