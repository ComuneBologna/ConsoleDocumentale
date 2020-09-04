package it.eng.consolepec.spagicclient.remoteproxy.impl;

import java.util.List;

import javax.xml.bind.JAXBException;

import it.bologna.comune.alfresco.generic.service.Request;
import it.bologna.comune.alfresco.generic.service.Response;
import it.eng.cobo.consolepec.util.date.DateUtils;
import it.eng.consolepec.spagicclient.SpagicClientGestioneProcedimenti;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.bean.request.procedimenti.GestioneProcedimentoRequest;
import it.eng.consolepec.spagicclient.bean.request.procedimenti.IterProcedimentoRequest;
import it.eng.consolepec.spagicclient.bean.response.procedimenti.GestioneProcedimentoResponse;
import it.eng.consolepec.spagicclient.bean.response.procedimenti.IterProcedimentoResponse;
import it.eng.consolepec.spagicclient.remoteproxy.abs.AbstractSpagicClientRemoteProxy;
import it.eng.consolepec.spagicclient.remoteproxy.abs.SpagicClientRemoteProxyUtil;
import it.eng.consolepec.spagicclient.remoteproxy.abs.SpagicClientSerializationUtil;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.servicename.ServiceNamesUtil;

public class SpagicClientGestioneProcedimentiRemoteProxy extends AbstractSpagicClientRemoteProxy<Request, Response> implements SpagicClientGestioneProcedimenti {

	protected SpagicClientGestioneProcedimentiRemoteProxy(String serviceProxyUrl, String alfrescoUsername, String alfrescoPassword, String serviceUsername, String servicePassword,
			String restServiceUrl) {
		super(serviceProxyUrl, alfrescoUsername, alfrescoPassword, serviceUsername, servicePassword, restServiceUrl);
	}
	
	@Override
	public GestioneProcedimentoResponse avvioProcedimento(GestioneProcedimentoRequest avvioProcedimento, String praticaPath, List<String> indirizziMail, Utente utente) throws SpagicClientException {
		// richiesta generica per il transactional
		Request genericRequest = new Request();
		genericRequest.setAlfrescopassword(getAlfrescoPassword());
		genericRequest.setAlfrescousername(getAlfrescoUsername());
		genericRequest.setUtente(SpagicClientRemoteProxyUtil.convert(utente));
		genericRequest.setServicename(ServiceNamesUtil.AVVIA_PROCEDIMENTI);
		genericRequest.setPathapplicationxml(praticaPath);

		// richiesta specifica per APPC (contenuta nella precedente come requestparam)
		it.bologna.comune.spagic.procedimenti.gestione.Request myRequest = gestioneProcedimentoConvertiBeanToSpagicRequest(avvioProcedimento);
		List<String> arrayIndirizzi = myRequest.getIndirizzi();
		arrayIndirizzi.addAll(indirizziMail);

		genericRequest.setInternalrequestparam(SpagicClientSerializationUtil.getRequestGestioneProcedimentoToXml(myRequest));

		// invocazione transactional: ritorna solo l'esito (eventualmente si poteva prevedere di ritornare come attributo anche l'xml della pratica)
		Response response = invokeSpagicService(genericRequest);
		if (response.getError() != null)
			throw SpagicClientRemoteProxyUtil.processErrorResponse(response.getError());
		it.bologna.comune.spagic.procedimenti.gestione.Response myResponse = SpagicClientSerializationUtil.getResponseGestioneProcedimentoToObject(response.getResponseparam());
		GestioneProcedimentoResponse esito = gestioneProcedimentoSpagicResponseToBean(myResponse);
		return esito;
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

	@Deprecated
	@Override
	public IterProcedimentoResponse getIterProcedimento(IterProcedimentoRequest iterRequest, Utente utente) throws SpagicClientException {
		// richiesta generica per il transactional
		Request genericRequest = new Request();
		genericRequest.setAlfrescopassword(getAlfrescoPassword());
		genericRequest.setAlfrescousername(getAlfrescoUsername());
		genericRequest.setUtente(SpagicClientRemoteProxyUtil.convert(utente));
		genericRequest.setServicename(ServiceNamesUtil.ITER_PROCEDIMENTI);

		// richiesta specifica per APPC (contenuta nella precedente come requestparam)
		it.bologna.comune.spagic.procedimenti.iter.Request myRequest = new it.bologna.comune.spagic.procedimenti.iter.Request();
		myRequest.setTipoProtocollo(iterRequest.getTipoProtocollo());
		myRequest.setCodComune(iterRequest.getCodComune());
		myRequest.setAnnoProtocollazione(iterRequest.getAnnoProtocollazione());
		myRequest.setNumProtocollazione(iterRequest.getNumProtocollazione());
		genericRequest.setInternalrequestparam(SpagicClientSerializationUtil.getRequestIterProcedimentoToXml(myRequest));

		// risposta transactional
		Response response = invokeSpagicService(genericRequest);
		if (response.getError() != null)
			throw SpagicClientRemoteProxyUtil.processErrorResponse(response.getError());
		it.bologna.comune.spagic.procedimenti.iter.Response myResponse = SpagicClientSerializationUtil.getResponseIterProcedimentoToObject(response.getResponseparam());

		IterProcedimentoResponse esito = new IterProcedimentoResponse();
		esito.setCodComune(myResponse.getCodComune());
		esito.setTipoProtocollo(myResponse.getTipoProtocollo());
		esito.setAnnoProtocollazione(myResponse.getAnnoProtocollazione());
		esito.setNumProtocollazione(myResponse.getNumProtocollazione());
		esito.setTipoRecord(myResponse.getTipoRecord());
		esito.setCodProcedimento(myResponse.getCodProcedimento());
		esito.setDataEvento(myResponse.getDataEvento());
		esito.setCodEvento(myResponse.getCodEvento());
		esito.setProgrEvento(myResponse.getProgrEvento());
		esito.setNumItemCoda(myResponse.getNumItemCoda());
		esito.setCodMessaggio(myResponse.getCodMessaggio());
		esito.setDescMessaggio(myResponse.getDescMessaggio());
		esito.setNumTotProcedimenti(myResponse.getNumTotProcedimenti());
		esito.setError(myResponse.getError());
		esito.getDatiProcedimento().addAll(myResponse.getDatiProcedimento());
		esito.getIterEventi().addAll(myResponse.getIterEventi());
		return esito;
	}

	public GestioneProcedimentoResponse gestioneProcedimentoSpagicResponseToBean(it.bologna.comune.spagic.procedimenti.gestione.Response response) {
		GestioneProcedimentoResponse bean = new GestioneProcedimentoResponse();
		bean.setCodice(response.getCodice());
		bean.setDescrizione(response.getDescrizione());
		bean.setTermine(response.getTermine());
		bean.setDurata(response.getDurata());
		return bean;
	}

	private it.bologna.comune.spagic.procedimenti.gestione.Request gestioneProcedimentoConvertiBeanToSpagicRequest(GestioneProcedimentoRequest bean) {
		it.bologna.comune.spagic.procedimenti.gestione.Request request = new it.bologna.comune.spagic.procedimenti.gestione.Request();
		request.setOperazione(bean.getOperazione());
		request.setTipoProtocollo(bean.getTipoProtocollo());
		request.setCodComune(bean.getCodComune());
		request.setCodUtente(bean.getCodUtente());
		request.setAnnoProtocollazione(bean.getAnnoProtocollazione());
		request.setNumProtocollazione(bean.getNumProtocollazione());
		request.setDataProtocollazione(DateUtils.dateToXMLGrCal(bean.getDataProtocollazione()));
		request.setCodTipologiaProcedimento(bean.getCodTipologiaProcedimento());
		request.setDataInizioDecorrenzaProcedimento(DateUtils.dateToXMLGrCal(bean.getDataInizioDecorrenzaProcedimento()));
		request.setModAvvioProcedimento(bean.getModAvvioProcedimento());
		request.setCodUnitaOrgCompetenza(bean.getCodUnitaOrgCompetenza());
		request.setCodUnitaOrgResponsabile(bean.getCodUnitaOrgResponsabile());
		request.setModalitaChiusura(bean.getModalitaChiusura());
		request.setDataChiusura(DateUtils.dateToXMLGrCal(bean.getDataChiusura()));
		request.setNumProtocolloDocChiusura(bean.getNumProtocolloDocChiusura());
		request.setAnnoProtocolloDocChiusura(bean.getAnnoProtocolloDocChiusura());
		request.setCodiceEventoChiusura(bean.getCodiceEventoChiusura());
		request.setDataInizioInterruzione(DateUtils.dateToXMLGrCal(bean.getDataInizioInterruzione()));
		request.setFlagInterruzione(bean.getFlagInterruzione());
		request.setGiorniInterruzione(bean.getGiorniInterruzione());
		request.setDataFineInterruzione(DateUtils.dateToXMLGrCal(bean.getDataFineInterruzione()));
		request.setNuovoCodTipologiaProcedimento(bean.getNuovoCodTipologiaProcedimento());
		request.setCodQuartiere(bean.getCodQuartiere());
		return request;
	}
	
}
