package it.eng.consolepec.client.impl;

import it.eng.cobo.consolepec.commons.atti.DocumentoTaskFirma;
import it.eng.consolepec.client.AbstractConsolePecClient;
import it.eng.consolepec.client.ClientInvoker;
import it.eng.consolepec.client.TaskFirmaClient;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.servicename.ServiceNamesUtil;

/**
 * @author GiacomoFM
 * @since 21/mar/2018
 */
public class TaskFirmaClientImpl extends AbstractConsolePecClient implements TaskFirmaClient {

	public TaskFirmaClientImpl(ClientInvoker clientInvoker) {
		super(clientInvoker);
	}

	@Override
	public DocumentoTaskFirma getDocumentoTaskFirma(Utente utente, String idDocumentale, Integer idTaskFirma) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.CARICA_DOCUMENTO_TASK_FIRMA, utente, idDocumentale, idTaskFirma);
	}

}
