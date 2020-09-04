package it.eng.consolepec.spagicclient.remoteproxy.impl;

import javax.xml.bind.JAXBException;

import it.bologna.comune.alfresco.generic.service.Request;
import it.bologna.comune.alfresco.generic.service.Response;
import it.bologna.comune.spagic.elettorale.EsitoElettorale;
import it.eng.consolepec.spagicclient.SpagicClientGestioneElettorale;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.abs.AbstractSpagicClientRemoteProxy;
import it.eng.consolepec.spagicclient.remoteproxy.abs.SpagicClientRemoteProxyUtil;
import it.eng.consolepec.spagicclient.remoteproxy.abs.SpagicClientSerializationUtil;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientErrorCode;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.servicename.ServiceNamesUtil;

public class SpagicClientGestioneElettoraleRemoteProxy extends AbstractSpagicClientRemoteProxy<Request, Response> implements SpagicClientGestioneElettorale {

	protected SpagicClientGestioneElettoraleRemoteProxy(String serviceProxyUrl, String alfrescoUsername, String alfrescoPassword, String serviceUsername, String servicePassword,
			String restServiceUrl) {
		super(serviceProxyUrl, alfrescoUsername, alfrescoPassword, serviceUsername, servicePassword, restServiceUrl);
	}

	@Override
	public void importa(String pathPratica, Utente utente) throws SpagicClientException {

		Request request = new Request();
		request.setAlfrescopassword(getAlfrescoPassword());
		request.setAlfrescousername(getAlfrescoUsername());
		request.setServicename(ServiceNamesUtil.IMPORTA_ELETTORALE);

		it.bologna.comune.spagic.elettorale.Request internalRequest = new it.bologna.comune.spagic.elettorale.Request();
		internalRequest.setPathPratica(pathPratica);
		String xmlInternalRequest = SpagicClientSerializationUtil.getRequestElettoraleToString(internalRequest);
		request.setPathapplicationxml(xmlInternalRequest);

		request.setUtente(SpagicClientRemoteProxyUtil.convert(utente));
		Response response = invokeSpagicService(request);
		if (response.getError() != null)
			throw SpagicClientRemoteProxyUtil.processErrorResponse(response.getError());
		it.bologna.comune.spagic.elettorale.Response responseElettoraleToObject = SpagicClientSerializationUtil.getResponseElettoraleToObject(response.getResponseparam());
		EsitoElettorale esito = responseElettoraleToObject.getEsito();
		if (!EsitoElettorale.OK.equals(esito))
			generaEccezione(esito);
	}

	@Override
	public void annulla(String pathPratica, Utente utente) throws SpagicClientException {
		Request request = new Request();
		request.setAlfrescopassword(getAlfrescoPassword());
		request.setAlfrescousername(getAlfrescoUsername());
		request.setServicename(ServiceNamesUtil.ANNULLA_ELETTORALE);

		it.bologna.comune.spagic.elettorale.Request internalRequest = new it.bologna.comune.spagic.elettorale.Request();
		internalRequest.setPathPratica(pathPratica);
		String xmlInternalRequest = SpagicClientSerializationUtil.getRequestElettoraleToString(internalRequest);
		request.setPathapplicationxml(xmlInternalRequest);

		request.setUtente(SpagicClientRemoteProxyUtil.convert(utente));
		Response response = invokeSpagicService(request);
		if (response.getError() != null)
			throw SpagicClientRemoteProxyUtil.processErrorResponse(response.getError());

		it.bologna.comune.spagic.elettorale.Response responseElettoraleToObject = SpagicClientSerializationUtil.getResponseElettoraleToObject(response.getResponseparam());
		EsitoElettorale esito = responseElettoraleToObject.getEsito();
		if (!EsitoElettorale.OK.equals(esito))
			generaEccezione(esito);
	}

	private static void generaEccezione(EsitoElettorale esito) {
		if (EsitoElettorale.ERR_NOT_FOUND.equals(esito))
			throw new SpagicClientException(SpagicClientErrorCode.ENOTFOUND, "Errore elaborazione elettorale: pratica non trovata");
		else if (EsitoElettorale.ERR_PARAMS.equals(esito))
			throw new SpagicClientException(SpagicClientErrorCode.EINVALIDARGUMENT, "Errore elaborazione elettorale: parametri non corretti");
		else if (EsitoElettorale.ERROR.equals(esito))
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION, "Errore generico elaborazione elettorale");
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
