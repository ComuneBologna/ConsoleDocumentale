package it.eng.consolepec.spagicclient.remoteproxy.impl;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.xml.bind.JAXBException;

import it.bologna.comune.alfresco.verifica.firma.REQUEST;
import it.bologna.comune.alfresco.verifica.firma.SERVICERESPONSE;
import it.eng.cobo.consolepec.commons.firmadigitale.FirmaDigitale;
import it.eng.consolepec.firmadigitale.FirmaDigitaleClient;
import it.eng.consolepec.spagicclient.SpagicClientVerifySignatureFile;
import it.eng.consolepec.spagicclient.remoteproxy.abs.AbstractSpagicClientRemoteProxy;
import it.eng.consolepec.spagicclient.remoteproxy.abs.SpagicClientSerializationUtil;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientErrorCode;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.servicename.ServiceNamesUtil;

public class SpagicClientVerifySignatureFileRemoteProxy extends AbstractSpagicClientRemoteProxy<REQUEST, SERVICERESPONSE> implements SpagicClientVerifySignatureFile {

	protected SpagicClientVerifySignatureFileRemoteProxy(String serviceProxyUrl, String alfrescoUsername, String alfrescoPassword, String serviceUsername, String servicePassword,
			String restServiceUrl) {
		super(serviceProxyUrl, alfrescoUsername, alfrescoPassword, serviceUsername, servicePassword, restServiceUrl);
	}

	@Override
	public FirmaDigitale verificaFirmaDigitaleFile(File file) throws SpagicClientException {
		FileDataSource fds = new FileDataSource(file);
		Map<String, DataSource> streams = new HashMap<>();
		streams.put("f0", fds);

		REQUEST request = new REQUEST();
		request.setUSER(getAlfrescoUsername());
		request.setPASSWORD(getAlfrescoPassword());
		request.setIDDOC("f0");
		request.setNOMEDOC(file.getName());
		request.setNOMEFILE(file.getName());
		request.setFORCEDOWNLOAD(false);
		FirmaDigitale firmaDigitale = FirmaDigitaleClient.cast(invokeSpagicServiceWhitAttachementRequest(request, "application/octet-stream", streams));

		if ("ERR".equalsIgnoreCase(firmaDigitale.getCodice()))
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);

		return firmaDigitale;
	}

	@Override
	protected String getJaxbRequestToXml(REQUEST request) throws JAXBException {
		return SpagicClientSerializationUtil.getVerificaFirmaRequestToString(request);
	}

	@Override
	protected SERVICERESPONSE getXmlResponseToJaxb(String response) throws JAXBException {
		return SpagicClientSerializationUtil.getVerificaFirmaServiceResponsetToObject(response);
	}

	@Override
	protected String getSpagicServiceId() {
		return ServiceNamesUtil.VERIFICA_FIRMA_FILE;
	}
}
