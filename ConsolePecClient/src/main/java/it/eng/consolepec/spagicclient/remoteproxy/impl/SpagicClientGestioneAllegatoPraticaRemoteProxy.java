package it.eng.consolepec.spagicclient.remoteproxy.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.xml.bind.JAXBException;

import it.bologna.comune.alfresco.generic.service.Request;
import it.bologna.comune.alfresco.generic.service.Response;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo;
import it.eng.consolepec.spagicclient.SpagicClientGestioneAllegatoPratica;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.abs.AbstractSpagicClientRemoteProxy;
import it.eng.consolepec.spagicclient.remoteproxy.abs.NewSpagicClientSerializationUtil;
import it.eng.consolepec.spagicclient.remoteproxy.abs.SpagicClientRemoteProxyUtil;
import it.eng.consolepec.spagicclient.remoteproxy.abs.SpagicClientSerializationUtil;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.spagicclient.remoteproxy.util.DatiAggiuntiviUtil;
import it.eng.consolepec.spagicclient.servicename.ServiceNamesUtil;

public class SpagicClientGestioneAllegatoPraticaRemoteProxy extends AbstractSpagicClientRemoteProxy<Request, Response> implements SpagicClientGestioneAllegatoPratica {

	protected SpagicClientGestioneAllegatoPraticaRemoteProxy(String serviceProxyUrl, String alfrescoUsername, String alfrescoPassword, String serviceUsername, String servicePassword,
			String restServiceUrl) {
		super(serviceProxyUrl, alfrescoUsername, alfrescoPassword, serviceUsername, servicePassword, restServiceUrl);
	}

	@Override
	public LockedPratica removeFile(String pathPratica, String nomeFile, Utente utente) throws SpagicClientException {
		List<String> nomiFiles = new ArrayList<String>();
		nomiFiles.add(nomeFile);
		return _removeFiles(pathPratica, nomiFiles, utente);
	}

	@Override
	public LockedPratica removeFiles(String pathPratica, List<String> nomiFiles, Utente utente) throws SpagicClientException {
		return _removeFiles(pathPratica, nomiFiles, utente);
	}

	private LockedPratica _removeFiles(String pathPratica, List<String> nomiFiles, Utente utente) throws SpagicClientException {
		Request request = new Request();
		request.setAlfrescopassword(getAlfrescoPassword());
		request.setAlfrescousername(getAlfrescoUsername());
		request.setPathapplicationxml(pathPratica);
		request.setServicename(ServiceNamesUtil.DELETEALLEGATI);
		request.setUtente(SpagicClientRemoteProxyUtil.convert(utente));

		StringBuilder files = new StringBuilder();
		for (String file : nomiFiles) {
			files.append(file).append("|");
		}

		request.setRequestparam(files.toString());
		Response response = invokeSpagicService(request);
		if (response.getError() != null)
			throw SpagicClientRemoteProxyUtil.processErrorResponse(response.getError());
		return new LockedPratica(response.getResponseparam(), response.getApplicationxml());
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
	public LockedPratica uploadFile(String pathPratica, String pathFile, String nomeFile, Utente utente) throws SpagicClientException {
		Request genericRequest = new Request();
		genericRequest.setAlfrescopassword(getAlfrescoPassword());
		genericRequest.setAlfrescousername(getAlfrescoUsername());
		genericRequest.setPathapplicationxml(pathPratica);
		genericRequest.setServicename(ServiceNamesUtil.UPLOADLINKALLEGATO);
		genericRequest.setRequestparam(pathFile + "|" + nomeFile + "|" + utente.getUsername());
		genericRequest.setUtente(SpagicClientRemoteProxyUtil.convert(utente));
		Response response = invokeSpagicService(genericRequest);
		if (response.getError() != null)
			throw SpagicClientRemoteProxyUtil.processErrorResponse(response.getError());
		return new LockedPratica(response.getResponseparam(), response.getApplicationxml());
	}

	@Override
	public LockedPratica cambiaTipologiaAllegato(String pathPratica, List<String> nomiFiles, String tipologiaAllegato, Utente utente) throws SpagicClientException {

		Request request = new Request();
		request.setAlfrescopassword(getAlfrescoPassword());
		request.setAlfrescousername(getAlfrescoUsername());
		request.setPathapplicationxml(pathPratica);
		request.setServicename(ServiceNamesUtil.CAMBIA_TIPOLOGIA_ALLEGATO);
		request.setUtente(SpagicClientRemoteProxyUtil.convert(utente));

		it.bologna.comune.spagic.gestione.allegato.Request gestioneAllegatoRequest = new it.bologna.comune.spagic.gestione.allegato.Request();

		gestioneAllegatoRequest.setPathPratica(pathPratica);
		gestioneAllegatoRequest.setTipologiaAllegato(tipologiaAllegato);

		for (String nomeFile : nomiFiles) {
			gestioneAllegatoRequest.getNomiAllegati().add(nomeFile);
		}

		request.setInternalrequestparam(NewSpagicClientSerializationUtil.serialize(gestioneAllegatoRequest));

		Response response = invokeSpagicService(request);
		if (response.getError() != null)
			throw SpagicClientRemoteProxyUtil.processErrorResponse(response.getError());
		return new LockedPratica(response.getResponseparam(), response.getApplicationxml());
	}

	@Override
	public LockedPratica uploadFile(String pathPratica, String nomeFile, String tipologia, File allegato, Utente utente) throws SpagicClientException {
		return uploadFile(pathPratica, nomeFile, tipologia, null, allegato, utente);
	}

	@Override
	public LockedPratica uploadFile(String fascicoloPath, String nomeFile, File allegato, Utente utente) throws SpagicClientException {
		return uploadFile(fascicoloPath, nomeFile, null, allegato, utente);
	}

	@Override
	public LockedPratica uploadFile(String pathPratica, String nomeFile, String tipologia, List<DatoAggiuntivo> datiAggiuntivi, File allegato, Utente utente) throws SpagicClientException {

		FileDataSource fds = new FileDataSource(allegato);
		Map<String, DataSource> streams = new HashMap<>();
		streams.put("f0", fds);

		Request genericRequest = new Request();
		genericRequest.setAlfrescopassword(getAlfrescoPassword());
		genericRequest.setAlfrescousername(getAlfrescoUsername());
		genericRequest.setPathapplicationxml(pathPratica);
		genericRequest.setServicename(ServiceNamesUtil.UPLOADALLEGATO);
		StringBuilder requestParam = new StringBuilder(nomeFile).append("|").append(utente.getUsername());
		if (tipologia != null)
			requestParam.append("|").append(tipologia);
		genericRequest.setRequestparam(requestParam.toString());
		genericRequest.setUtente(SpagicClientRemoteProxyUtil.convert(utente));

		if (datiAggiuntivi != null) {
			for (DatoAggiuntivo dag : datiAggiuntivi) {
				genericRequest.getDatiAggiuntivi().add(DatiAggiuntiviUtil.datoCommonToDatoBase(dag));
			}
		}

		Response response = invokeSpagicServiceWhitAttachementRequest(genericRequest, "application/octet-stream", streams);
		if (response.getError() != null)
			throw SpagicClientRemoteProxyUtil.processErrorResponse(response.getError());

		return new LockedPratica(response.getResponseparam(), response.getApplicationxml());
	}
}
