package it.eng.consolepec.spagicclient.remoteproxy.impl;

import javax.xml.bind.JAXBException;

import it.bologna.comune.alfresco.generic.service.Request;
import it.bologna.comune.alfresco.generic.service.Response;
import it.eng.consolepec.spagicclient.SpagicClientGestioneComunicazioniMassive;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.bean.request.inviomassivo.ComunicazioneRequest;
import it.eng.consolepec.spagicclient.remoteproxy.abs.AbstractSpagicClientRemoteProxy;
import it.eng.consolepec.spagicclient.remoteproxy.abs.SpagicClientRemoteProxyUtil;
import it.eng.consolepec.spagicclient.remoteproxy.abs.SpagicClientSerializationUtil;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.spagicclient.servicename.ServiceNamesUtil;

public class SpagicClientGestioneComunicazioniMassiveRemoteProxy extends AbstractSpagicClientRemoteProxy<Request, Response> implements SpagicClientGestioneComunicazioniMassive {

	protected SpagicClientGestioneComunicazioniMassiveRemoteProxy(String serviceProxyUrl, String alfrescoUsername, String alfrescoPassword, String serviceUsername, String servicePassword,
			String restServiceUrl) {
		super(serviceProxyUrl, alfrescoUsername, alfrescoPassword, serviceUsername, servicePassword, restServiceUrl);
	}

	@Override
	public LockedPratica createComunicazione(ComunicazioneRequest crequest, Utente utente) throws SpagicClientException {
		Request request = new Request();
		request.setServicename(ServiceNamesUtil.CREATE_COMUNICAZIONE);
		request.setUtente(SpagicClientRemoteProxyUtil.convert(utente));

		it.bologna.comune.alfresco.creazione.comunicazione.Request req = new it.bologna.comune.alfresco.creazione.comunicazione.Request();
		req.setCodice(crequest.getCodice());
		req.setDescrizione(crequest.getDescrizione());
		req.setIdTemplate(crequest.getIdTemplate());
		req.setUtente(utente.getFullName());
		req.setRuolo(crequest.getAssegnatario());
		req.setUsername(utente.getUsername());

		request.setInternalrequestparam(SpagicClientSerializationUtil.getCreateComunicazioneRequestToString(req));

		Response response = invokeSpagicService(request);
		if (response.getError() != null)
			throw SpagicClientRemoteProxyUtil.processErrorResponse(response.getError());
		return new LockedPratica(response.getResponseparam(), response.getApplicationxml());
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

	@Override
	public LockedPratica creaInvio(String pathComunicazione, String csv, Utente utente) throws SpagicClientException {
		Request request = new Request();
		request.setServicename(ServiceNamesUtil.NUOVO_INVIO_COMUNICAZIONE);
		request.setUtente(SpagicClientRemoteProxyUtil.convert(utente));

		it.bologna.comune.spagic.gestione.comunicazione.Request rq = new it.bologna.comune.spagic.gestione.comunicazione.Request();
		rq.setTest(false);
		rq.setPathPratica(pathComunicazione);
		rq.setCsv(csv);

		request.setRequestparam(SpagicClientSerializationUtil.getRequestGestioneComunicazioniToString(rq));

		Response response = invokeSpagicService(request);

		if (response.getError() != null)
			throw SpagicClientRemoteProxyUtil.processErrorResponse(response.getError());

		return new LockedPratica(response.getResponseparam(), response.getApplicationxml());
	}

	@Override
	public LockedPratica creaInvioTest(String pathComunicazione, String csv, String destinatarioTest, int numeroTest, Utente utente) throws SpagicClientException {
		Request request = new Request();
		request.setServicename(ServiceNamesUtil.NUOVO_INVIO_COMUNICAZIONE);
		request.setUtente(SpagicClientRemoteProxyUtil.convert(utente));

		it.bologna.comune.spagic.gestione.comunicazione.Request rq = new it.bologna.comune.spagic.gestione.comunicazione.Request();
		rq.setTest(true);
		rq.setPathPratica(pathComunicazione);
		rq.setCsv(csv);
		rq.setDestinatarioTest(destinatarioTest);
		rq.setNumeroTest(numeroTest);

		request.setRequestparam(SpagicClientSerializationUtil.getRequestGestioneComunicazioniToString(rq));

		Response response = invokeSpagicService(request);

		if (response.getError() != null)
			throw SpagicClientRemoteProxyUtil.processErrorResponse(response.getError());

		return new LockedPratica(response.getResponseparam(), response.getApplicationxml());
	}

}
