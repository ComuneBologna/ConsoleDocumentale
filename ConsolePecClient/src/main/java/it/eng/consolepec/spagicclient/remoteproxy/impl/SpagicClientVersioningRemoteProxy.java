package it.eng.consolepec.spagicclient.remoteproxy.impl;

import javax.xml.bind.JAXBException;

import it.bologna.comune.alfresco.generic.service.Request;
import it.bologna.comune.alfresco.generic.service.Response;
import it.bologna.comune.alfresco.versioning.AllVersions;
import it.bologna.comune.alfresco.versioning.GetVersions;
import it.eng.consolepec.spagicclient.SpagicClientVersioning;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.abs.AbstractSpagicClientRemoteProxy;
import it.eng.consolepec.spagicclient.remoteproxy.abs.SpagicClientRemoteProxyUtil;
import it.eng.consolepec.spagicclient.remoteproxy.abs.SpagicClientSerializationUtil;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientErrorCode;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.servicename.ServiceNamesUtil;

public class SpagicClientVersioningRemoteProxy extends AbstractSpagicClientRemoteProxy<Request, Response> implements SpagicClientVersioning {

	protected SpagicClientVersioningRemoteProxy(String serviceProxyUrl, String alfrescoUsername, String alfrescoPassword, String serviceUsername, String servicePassword, String restServiceUrl) {
		super(serviceProxyUrl, alfrescoUsername, alfrescoPassword, serviceUsername, servicePassword, restServiceUrl);
	}

	@Override
	public AllVersions getAllVersions(String path, String nomeFile, Utente utente) throws SpagicClientException {
		GetVersions getVersions = new GetVersions();
		getVersions.setPathfile(path);
		getVersions.setNamefile(nomeFile);
		Request request = new Request();
		request.setRequestparam(SpagicClientSerializationUtil.getVersionsToXml(getVersions));
		request.setServicename(ServiceNamesUtil.GETVERSIONS);
		request.setAlfrescousername(getAlfrescoUsername());
		request.setAlfrescopassword(getAlfrescoPassword());
		request.setUtente(SpagicClientRemoteProxyUtil.convert(utente));
		Response response = invokeSpagicService(request);
		if (response.getError() != null)
			throw SpagicClientRemoteProxyUtil.processErrorResponse(response.getError());
		AllVersions versions = SpagicClientSerializationUtil.getAllVersionsToObject(response.getResponseparam());
		if (versions == null || versions.getCode().equalsIgnoreCase("KO"))
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		return versions;
	}

	@Override
	protected String getSpagicServiceId() {
		return ServiceNamesUtil.CONSOLE_PEC_DISPATCHER;
	}

	@Override
	protected String getJaxbRequestToXml(Request request) throws JAXBException {
		return SpagicClientSerializationUtil.getRequestToString(request);
	}

	@Override
	protected Response getXmlResponseToJaxb(String response) throws JAXBException {
		return SpagicClientSerializationUtil.getResponseXmlToObject(response);
	}

	@Override
	public AllVersions getAllVersions(String uuid, Utente utente) throws SpagicClientException {
		GetVersions getVersions = new GetVersions();
		getVersions.setUuid(uuid);
		Request request = new Request();
		request.setRequestparam(SpagicClientSerializationUtil.getVersionsToXml(getVersions));
		request.setServicename(ServiceNamesUtil.GETVERSIONS);
		request.setAlfrescousername(getAlfrescoUsername());
		request.setAlfrescopassword(getAlfrescoPassword());
		request.setUtente(SpagicClientRemoteProxyUtil.convert(utente));
		Response response = invokeSpagicService(request);
		if (response.getError() != null)
			throw SpagicClientRemoteProxyUtil.processErrorResponse(response.getError());
		AllVersions versions = SpagicClientSerializationUtil.getAllVersionsToObject(response.getResponseparam());
		if (versions == null || versions.getCode().equalsIgnoreCase("KO"))
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		return versions;
	}
}
