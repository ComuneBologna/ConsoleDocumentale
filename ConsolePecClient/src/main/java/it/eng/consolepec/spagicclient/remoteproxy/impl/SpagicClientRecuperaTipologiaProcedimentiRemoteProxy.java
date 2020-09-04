package it.eng.consolepec.spagicclient.remoteproxy.impl;

import java.util.Date;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import it.bologna.comune.alfresco.generic.service.Request;
import it.bologna.comune.alfresco.generic.service.Response;
import it.eng.consolepec.spagicclient.SpagicClientRecuperaTipologiaProcedimenti;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.abs.AbstractSpagicClientRemoteProxy;
import it.eng.consolepec.spagicclient.remoteproxy.abs.SpagicClientRemoteProxyUtil;
import it.eng.consolepec.spagicclient.remoteproxy.abs.SpagicClientSerializationUtil;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientErrorCode;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.servicename.ServiceNamesUtil;

public class SpagicClientRecuperaTipologiaProcedimentiRemoteProxy extends AbstractSpagicClientRemoteProxy<Request, Response> implements SpagicClientRecuperaTipologiaProcedimenti {

	protected SpagicClientRecuperaTipologiaProcedimentiRemoteProxy(String serviceProxyUrl, String alfrescoUsername, String alfrescoPassword, String serviceUsername, String servicePassword,
			String restServiceUrl) {
		super(serviceProxyUrl, alfrescoUsername, alfrescoPassword, serviceUsername, servicePassword, restServiceUrl);
	}

	@Override
	public it.bologna.comune.spagic.procedimenti.tipologie.Response getTipologieProcedimenti(Date currentDate, Utente utente) throws SpagicClientException {

		Request request = new Request();
		request.setServicename(ServiceNamesUtil.RECUPERA_TIPOLOGIE_PROCEDIMENTI);
		request.setUtente(SpagicClientRemoteProxyUtil.convert(utente));

		it.bologna.comune.spagic.procedimenti.tipologie.Request requestProcedimenti = new it.bologna.comune.spagic.procedimenti.tipologie.Request();
		try {
			requestProcedimenti.setCurrentDateTime(DatatypeFactory.newInstance().newXMLGregorianCalendar());
		} catch (DatatypeConfigurationException e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}

		String tipologieProcedimentiRequestToXml = SpagicClientSerializationUtil.getTipologieProcedimentiRequestToXml(requestProcedimenti);

		request.setRequestparam(tipologieProcedimentiRequestToXml);

		Response invokeSpagicServiceResponse = invokeSpagicService(request);

		if (invokeSpagicServiceResponse.getError() != null)
			throw SpagicClientRemoteProxyUtil.processErrorResponse(invokeSpagicServiceResponse.getError());

		String responseparam = invokeSpagicServiceResponse.getResponseparam();

		return SpagicClientSerializationUtil.getTipologieProcedimentiResponseToObject(responseparam);
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
	protected String getSpagicServiceId() {
		return ServiceNamesUtil.CONSOLE_PEC_DISPATCHER;
	}

}
