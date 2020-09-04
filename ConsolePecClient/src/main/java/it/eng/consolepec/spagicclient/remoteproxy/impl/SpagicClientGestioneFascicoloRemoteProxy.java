package it.eng.consolepec.spagicclient.remoteproxy.impl;

import java.util.List;

import javax.xml.bind.JAXBException;

import it.bologna.comune.alfresco.creazione.fascicolo.Request.Pratiche;
import it.bologna.comune.alfresco.generic.service.Request;
import it.bologna.comune.alfresco.generic.service.Response;
import it.bologna.comune.spagic.aggiornapg.Request.Protocollazioni;
import it.eng.consolepec.client.RiassegnaPraticaClient;
import it.eng.consolepec.spagicclient.SpagicClientGestioneFascicolo;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.bean.request.Protocollazione;
import it.eng.consolepec.spagicclient.remoteproxy.abs.AbstractSpagicClientRemoteProxy;
import it.eng.consolepec.spagicclient.remoteproxy.abs.NewSpagicClientSerializationUtil;
import it.eng.consolepec.spagicclient.remoteproxy.abs.SpagicClientRemoteProxyUtil;
import it.eng.consolepec.spagicclient.remoteproxy.abs.SpagicClientSerializationUtil;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientErrorCode;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.spagicclient.servicename.ServiceNamesUtil;

public class SpagicClientGestioneFascicoloRemoteProxy extends AbstractSpagicClientRemoteProxy<Request, Response> implements SpagicClientGestioneFascicolo {

	private RiassegnaPraticaClient riassegnaPraticaClient;

	protected SpagicClientGestioneFascicoloRemoteProxy(String serviceProxyUrl, String alfrescoUsername, String alfrescoPassword, String serviceUsername, String servicePassword,
			String restServiceUrl) {
		super(serviceProxyUrl, alfrescoUsername, alfrescoPassword, serviceUsername, servicePassword, restServiceUrl);

		// per retrocompatibilità sistemi esterni
		riassegnaPraticaClient = new SpagicClientFactory(serviceProxyUrl, alfrescoUsername, alfrescoPassword, serviceUsername, servicePassword, restServiceUrl).getRiassegnaPraticaClient();

	}

	@Override
	public LockedPratica riassegna(String pathPratica, String nuovoAssegnatario, List<String> indirizziNotifica, String operatore, Utente utente) throws SpagicClientException {
		return riassegnaPraticaClient.riassegna(pathPratica, nuovoAssegnatario, indirizziNotifica, operatore, "", utente);
	}

	@Override
	public boolean elimina(String pathFascicolo, Utente utente) throws SpagicClientException {
		Request request = new Request();
		request.setAlfrescopassword(getAlfrescoPassword());
		request.setAlfrescousername(getAlfrescoUsername());
		request.setServicename(ServiceNamesUtil.ELIMINAFASCICOLOSERVICE);
		request.setPathapplicationxml(pathFascicolo);
		request.setUtente(SpagicClientRemoteProxyUtil.convert(utente));
		Response response = invokeSpagicService(request);
		if (response.getError() != null)
			throw SpagicClientRemoteProxyUtil.processErrorResponse(response.getError());
		return true;
	}

	@Override
	protected String getSpagicServiceId() {
		return ServiceNamesUtil.CONSOLE_PEC_DISPATCHER;
	}

	@Override
	public Response aggiungiPratica(String pathFascicolo, String pathPratica, Utente utente) throws SpagicClientException {
		return aggiungiPratica(pathFascicolo, pathPratica, null, utente);
	}

	private Response aggiungiPratica(String pathFascicolo, String pathPratica, String request, Utente utente) {

		it.bologna.comune.alfresco.creazione.fascicolo.Request creazioneFascicolo = new it.bologna.comune.alfresco.creazione.fascicolo.Request();
		creazioneFascicolo.setPratiche(new Pratiche());
		List<String> listaPratiche = creazioneFascicolo.getPratiche().getPratica();
		listaPratiche.add(pathPratica);
		creazioneFascicolo.setPathfascicolo(pathFascicolo);

		Request requestBean = new Request();
		requestBean.setAlfrescopassword(getAlfrescoPassword());
		requestBean.setAlfrescousername(getAlfrescoUsername());
		if (request != null)
			requestBean.setRequestparam(request);
		requestBean.setServicename(ServiceNamesUtil.AGGIUNGIAFASCICOLO);
		requestBean.setUtente(SpagicClientRemoteProxyUtil.convert(utente));
		requestBean.setInternalrequestparam(SpagicClientSerializationUtil.getCreateFascicoloRequestToString(creazioneFascicolo));

		Response aggiungi = invokeSpagicService(requestBean);
		if (aggiungi == null)
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);

		if (aggiungi.getError() != null)
			throw SpagicClientRemoteProxyUtil.processErrorResponse(aggiungi.getError());
		return aggiungi;
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
	public LockedPratica aggiornaPG(String pathFascicolo, List<Protocollazione> protocollazioni, Utente utente) throws SpagicClientException {
		Request request = new Request();
		request.setAlfrescopassword(getAlfrescoPassword());
		request.setAlfrescousername(getAlfrescoUsername());

		it.bologna.comune.spagic.aggiornapg.Request rq = new it.bologna.comune.spagic.aggiornapg.Request();
		rq.setPathPratica(pathFascicolo);
		for (Protocollazione p : protocollazioni) {
			Protocollazioni prot = new Protocollazioni();
			prot.setAnnoPG(p.getAnnoPG());
			prot.setNumPG(p.getNumPG());
			rq.getProtocollazioni().add(prot);
		}

		request.setRequestparam(NewSpagicClientSerializationUtil.serialize(rq));
		request.setServicename(ServiceNamesUtil.AGGIORNA_PG);
		request.setPathapplicationxml(pathFascicolo);
		request.setUtente(SpagicClientRemoteProxyUtil.convert(utente));
		Response response = invokeSpagicService(request);
		if (response.getError() != null)
			throw SpagicClientRemoteProxyUtil.processErrorResponse(response.getError());
		return new LockedPratica(response.getResponseparam(), response.getApplicationxml());
	}

}
