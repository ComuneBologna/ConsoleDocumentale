/**
 *
 */
package it.eng.consolepec.spagicclient.remoteproxy.impl;

import java.util.List;

import javax.xml.bind.JAXBException;

import it.bologna.comune.alfresco.generic.service.Request;
import it.bologna.comune.alfresco.generic.service.Response;
import it.bologna.comune.spagic.iperfascicolo.RequestCreazioneCondivisione;
import it.eng.consolepec.spagicclient.SpagicClientIperFascicoloHandler;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.abs.AbstractSpagicClientRemoteProxy;
import it.eng.consolepec.spagicclient.remoteproxy.abs.SpagicClientRemoteProxyUtil;
import it.eng.consolepec.spagicclient.remoteproxy.abs.SpagicClientSerializationUtil;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.spagicclient.servicename.ServiceNamesUtil;

/**
 * @author roger
 *
 */
public class SpagicClientIperFascicoloHandlerRemoteProxy extends AbstractSpagicClientRemoteProxy<Request, Response> implements SpagicClientIperFascicoloHandler {

	protected SpagicClientIperFascicoloHandlerRemoteProxy(String serviceProxyUrl, String alfrescoUsername, String alfrescoPassword, String serviceUsername, String servicePassword,
			String restServiceUrl) {
		super(serviceProxyUrl, alfrescoUsername, alfrescoPassword, serviceUsername, servicePassword, restServiceUrl);
	}

	@Override
	public LockedPratica mergeCondivisione(String nomeGruppo, String pathFascicolo, List<String> operazioni, Utente utente) throws SpagicClientException {
		Request request = getRequest(ServiceNamesUtil.CONDIVIDI_FASCICOLO, utente);

		RequestCreazioneCondivisione requestCreazioneCondivisione = new RequestCreazioneCondivisione();
		requestCreazioneCondivisione.setNomeGruppo(nomeGruppo);
		requestCreazioneCondivisione.setPathFascicolo(pathFascicolo);
		requestCreazioneCondivisione.getOperazioni().addAll(operazioni);

		String condivisione = SpagicClientSerializationUtil.getRequestCreaCondivisioneToString(requestCreazioneCondivisione);

		request.setRequestparam(condivisione);

		return getLockedPratica(request);
	}

	@Override
	public LockedPratica eliminaCondivisione(String nomeGruppo, String pathFascicolo, Utente utente) throws SpagicClientException {
		Request request = getRequest(ServiceNamesUtil.ELIMINA_CONDIVISIONE, utente);
		request.setRequestparam(new StringBuilder().append(nomeGruppo).append("|").append(pathFascicolo).toString());
		return getLockedPratica(request);
	}

	@Override
	public LockedPratica eliminaCollegamento(String pathFascicolo, String pathCollegamentoFascicoloDaEliminare, Utente utente) throws SpagicClientException {
		Request request = getRequest(ServiceNamesUtil.ELIMINA_COLLEGAMENTO, utente);
		request.setRequestparam(new StringBuilder().append(pathFascicolo).append("|").append(pathCollegamentoFascicoloDaEliminare).toString());
		return getLockedPratica(request);
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

	private Request getRequest(String serviceId, Utente utente) {
		Request request = new Request();
		request.setAlfrescopassword(getAlfrescoPassword());
		request.setAlfrescousername(getAlfrescoUsername());
		request.setServicename(serviceId);
		request.setUtente(SpagicClientRemoteProxyUtil.convert(utente));
		return request;
	}

	private LockedPratica getLockedPratica(Request request) {
		Response response = invokeSpagicService(request);
		if (response.getError() != null)
			throw SpagicClientRemoteProxyUtil.processErrorResponse(response.getError());
		return new LockedPratica(response.getResponseparam(), response.getApplicationxml());
	}

}
