package it.eng.consolepec.spagicclient.remoteproxy.impl;

import java.io.InputStream;

import javax.xml.bind.JAXBException;

import it.bologna.comune.alfresco.verifica.firma.REQUEST;
import it.bologna.comune.alfresco.verifica.firma.SERVICERESPONSE;
import it.eng.cobo.consolepec.commons.firmadigitale.FirmaDigitale;
import it.eng.consolepec.firmadigitale.FirmaDigitaleClient;
import it.eng.consolepec.spagicclient.SpagicClientDownlodAllegatoOriginale;
import it.eng.consolepec.spagicclient.remoteproxy.ResponseWithAttachementsDto;
import it.eng.consolepec.spagicclient.remoteproxy.abs.AbstractSpagicClientRemoteProxy;
import it.eng.consolepec.spagicclient.remoteproxy.abs.SpagicClientSerializationUtil;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientErrorCode;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.servicename.ServiceNamesUtil;

public class SpagicClientDownlodAllegatoOriginaleRemoteProxy extends AbstractSpagicClientRemoteProxy<REQUEST, SERVICERESPONSE> implements SpagicClientDownlodAllegatoOriginale {

	protected SpagicClientDownlodAllegatoOriginaleRemoteProxy(String serviceProxyUrl, String alfrescoUsername, String alfrescoPassword, String serviceUsername, String servicePassword,
			String restServiceUrl) {
		super(serviceProxyUrl, alfrescoUsername, alfrescoPassword, serviceUsername, servicePassword, restServiceUrl);
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

	@Override
	public InputStream getDocumentByUuid(String uuid) throws SpagicClientException {
		String nomefile = "" + System.currentTimeMillis();
		REQUEST request = new REQUEST();
		request.setUUID(uuid);
		request.setNOMEFILE(nomefile);
		request.setUSER(getAlfrescoUsername());
		request.setPASSWORD(getAlfrescoPassword());
		request.setFORCEDOWNLOAD(true);

		ResponseWithAttachementsDto<SERVICERESPONSE> invokeSpagicServiceWhitAttachementResponse = invokeSpagicServiceWhitAttachementResponse(request);
		FirmaDigitale firmaDigitale = FirmaDigitaleClient.cast(invokeSpagicServiceWhitAttachementResponse.getResponse());
		if ("ERR".equalsIgnoreCase(firmaDigitale.getCodice())) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
		InputStream inputStream = invokeSpagicServiceWhitAttachementResponse.getAttachements().get(nomefile);
		if (inputStream == null)
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		return inputStream;
	}
}
