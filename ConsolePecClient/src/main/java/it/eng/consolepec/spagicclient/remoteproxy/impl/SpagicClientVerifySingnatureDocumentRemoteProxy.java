package it.eng.consolepec.spagicclient.remoteproxy.impl;

import javax.xml.bind.JAXBException;

import it.bologna.comune.alfresco.verifica.firma.REQUEST;
import it.bologna.comune.alfresco.verifica.firma.SERVICERESPONSE;
import it.eng.cobo.consolepec.commons.firmadigitale.FirmaDigitale;
import it.eng.consolepec.firmadigitale.FirmaDigitaleClient;
import it.eng.consolepec.spagicclient.SpagicClientVerifySingnatureDocument;
import it.eng.consolepec.spagicclient.remoteproxy.abs.AbstractSpagicClientRemoteProxy;
import it.eng.consolepec.spagicclient.remoteproxy.abs.SpagicClientSerializationUtil;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientErrorCode;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.servicename.ServiceNamesUtil;

public class SpagicClientVerifySingnatureDocumentRemoteProxy extends AbstractSpagicClientRemoteProxy<REQUEST, SERVICERESPONSE> implements SpagicClientVerifySingnatureDocument {

	protected SpagicClientVerifySingnatureDocumentRemoteProxy(String serviceProxyUrl, String alfrescoUsername, String alfrescoPassword, String serviceUsername, String servicePassword,
			String restServiceUrl) {
		super(serviceProxyUrl, alfrescoUsername, alfrescoPassword, serviceUsername, servicePassword, restServiceUrl);
	}

	@Override
	public FirmaDigitale verificaFirmaDocumentoByUuid(String uuid) throws SpagicClientException {
		REQUEST request = new REQUEST();
		request.setUUID(uuid);
		request.setUSER(getAlfrescoUsername());
		request.setPASSWORD(getAlfrescoPassword());

		FirmaDigitale firmaDigitale = FirmaDigitaleClient.cast(invokeSpagicService(request));
		if ("ERR".equalsIgnoreCase(firmaDigitale.getCodice())) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
		return firmaDigitale;
	}

	@Override
	public FirmaDigitale verificaFirmaDocumentoByPath(String path) throws SpagicClientException {
		REQUEST request = new REQUEST();
		request.setPATH(path);
		request.setUSER(getAlfrescoUsername());
		request.setPASSWORD(getAlfrescoPassword());

		FirmaDigitale firmaDigitale = FirmaDigitaleClient.cast(invokeSpagicService(request));
		if ("ERR".equalsIgnoreCase(firmaDigitale.getCodice())) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
		return firmaDigitale;
	}

	@Override
	protected String getSpagicServiceId() {
		return ServiceNamesUtil.VERIFICA_FIRMA_ALFRESCO;
	}

	@Override
	protected String getJaxbRequestToXml(REQUEST request) throws JAXBException {
		return SpagicClientSerializationUtil.getVerificaFirmaRequestToString(request);
	}

	@Override
	protected SERVICERESPONSE getXmlResponseToJaxb(String response) throws JAXBException {
		return SpagicClientSerializationUtil.getVerificaFirmaServiceResponsetToObject(response);
	}
}
