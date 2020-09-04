package it.eng.consolepec.spagicclient.remoteproxy.impl;

import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;

import it.bologna.comune.alfresco.creazione.fascicolo.Request.Allegati;
import it.bologna.comune.alfresco.creazione.fascicolo.Request.Pratiche;
import it.bologna.comune.alfresco.generic.service.Request;
import it.bologna.comune.alfresco.generic.service.Response;
import it.eng.consolepec.spagicclient.SpagicClientProtocollazioneCompleta;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.abs.AbstractSpagicClientRemoteProxy;
import it.eng.consolepec.spagicclient.remoteproxy.abs.SpagicClientRemoteProxyUtil;
import it.eng.consolepec.spagicclient.remoteproxy.abs.SpagicClientSerializationUtil;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientErrorCode;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.CreaFascicoloDto;
import it.eng.consolepec.spagicclient.servicename.ServiceNamesUtil;

public class SpagicClientProtocollazioneCompletaRemoteProxy extends AbstractSpagicClientRemoteProxy<Request, Response> implements SpagicClientProtocollazioneCompleta {

	protected SpagicClientProtocollazioneCompletaRemoteProxy(String serviceProxyUrl, String alfrescoUsername, String alfrescoPassword, String serviceUsername, String servicePassword,
			String restServiceUrl) {
		super(serviceProxyUrl, alfrescoUsername, alfrescoPassword, serviceUsername, servicePassword, restServiceUrl);
	}

	@Override
	public Response protocolla(String[] pathPratiche, String[] allegati, String pathFascicolo, String request, Utente utente) throws SpagicClientException {
		return protocollazione(pathPratiche, allegati, pathFascicolo, request, null, null, null, null, false, utente);
	}

	@Override
	public Response protocolla(String[] pathPratiche, String[] allegati, String pathFascicolo, String request, String numeroCapofila, String annoCapofila, Utente utente) throws SpagicClientException {
		return protocollazione(pathPratiche, allegati, pathFascicolo, request, numeroCapofila, annoCapofila, null, null, false, utente);
	}

	@Override
	public Response protocollaFromBA01(String[] pathPratiche, String[] allegati, String pathFascicolo, String request, String numeroCapofila, String annoCapofila,
			Utente utente) throws SpagicClientException {
		return protocollazione(pathPratiche, allegati, pathFascicolo, request, numeroCapofila, annoCapofila, null, null, true, utente);
	}

	@Override
	public Response protocollaFascicoloNuovo(CreaFascicoloDto creaFascicoloDto, String request, String pathEmailIn, Utente utente) throws SpagicClientException {
		return protocollaFascicoloNuovoInt(creaFascicoloDto, request, pathEmailIn, null, null, false, utente);
	}

	@Override
	public Response protocollaFascicoloNuovo(CreaFascicoloDto creaFascicoloDto, String request, String pathEmailIn, String numeroCapofila, String annoCapofila,
			Utente utente) throws SpagicClientException {
		return protocollaFascicoloNuovoInt(creaFascicoloDto, request, pathEmailIn, numeroCapofila, annoCapofila, false, utente);
	}

	@Override
	public Response protocollaFascicoloNuovoFromBA01(CreaFascicoloDto creaFascicoloDto, String request, String pathEmailIn, String numeroCapofila, String annoCapofila,
			Utente utente) throws SpagicClientException {
		return protocollaFascicoloNuovoInt(creaFascicoloDto, request, pathEmailIn, numeroCapofila, annoCapofila, true, utente);
	}

	@Override
	protected String getSpagicServiceId() {
		return ServiceNamesUtil.CONSOLE_PEC_DISPATCHER;
	}

	/**
	 * @param pathPratica
	 * @param request
	 * @param titolo
	 * @param utente
	 * @param note
	 * @return
	 * @throws SpagicClientException
	 * @throws DatatypeConfigurationException
	 */
	public Response protocollazione(String[] pathPratiche, String[] allegati, String pathFascicolo, String request, String numerocapofila, String annocapofila, String titolo, String note,
			boolean fromBA01, Utente utente) throws SpagicClientException {

		it.bologna.comune.alfresco.creazione.fascicolo.Request protocollazioneRequest = new it.bologna.comune.alfresco.creazione.fascicolo.Request();

		if (note != null)
			protocollazioneRequest.setNote(note);

		if (titolo != null)
			protocollazioneRequest.setTitolo(titolo);

		if (utente != null)
			protocollazioneRequest.setUtente(utente.getUsername());

		if (numerocapofila != null)
			protocollazioneRequest.setNumerocapofila(numerocapofila);

		if (annocapofila != null)
			protocollazioneRequest.setAnnocapofila(annocapofila);

		if (fromBA01) {
			it.bologna.comune.alfresco.creazione.fascicolo.CapofilaDetail detail = new it.bologna.comune.alfresco.creazione.fascicolo.CapofilaDetail();
			detail.setAnnoPgCapofila(Integer.valueOf(annocapofila));
			detail.setNumeroPgCapofila(numerocapofila);
			protocollazioneRequest.setCapofilaDetail(detail);
		}

		protocollazioneRequest.setAllegati(new Allegati());
		if (allegati != null) {
			List<String> listaAllegati = protocollazioneRequest.getAllegati().getAllegato();
			for (String allegato : allegati) {
				listaAllegati.add(allegato);
			}
		}
		protocollazioneRequest.setPratiche(new Pratiche());
		if (pathPratiche != null) {
			List<String> listaPratiche = protocollazioneRequest.getPratiche().getPratica();
			for (String pratiche : pathPratiche) {
				listaPratiche.add(pratiche);
			}
		}

		protocollazioneRequest.setPathfascicolo(pathFascicolo);

		Request requestBean = new Request();
		requestBean.setAlfrescopassword(getAlfrescoPassword());
		requestBean.setAlfrescousername(getAlfrescoUsername());
		requestBean.setRequestparam(request);
		requestBean.setServicename(ServiceNamesUtil.PROTOCOLLAZIONECOMPLETA);
		requestBean.setUtente(SpagicClientRemoteProxyUtil.convert(utente));
		requestBean.setInternalrequestparam(SpagicClientSerializationUtil.getCreateFascicoloRequestToString(protocollazioneRequest));

		Response protocolla = invokeSpagicService(requestBean);
		if (protocolla == null)
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);

		if (protocolla.getError() != null)
			throw SpagicClientRemoteProxyUtil.processErrorResponse(protocolla.getError());

		return protocolla;
	}

	private Response protocollaFascicoloNuovoInt(CreaFascicoloDto creaFascicoloDto, String request, String pathEmailIn, String numeroCapofila, String annoCapofila, boolean fromBa01, Utente utente) {

		it.bologna.comune.alfresco.creazione.fascicolo.Request protocollazioneRequest = new it.bologna.comune.alfresco.creazione.fascicolo.Request();

		protocollazioneRequest.setNote(creaFascicoloDto.getNote());
		protocollazioneRequest.setTitolo(creaFascicoloDto.getTitolo());
		protocollazioneRequest.setTipoFascicolo(creaFascicoloDto.getTipoPratica());
		protocollazioneRequest.setRuolo(creaFascicoloDto.getAssegnatario());

		Pratiche pratiche = new Pratiche();
		pratiche.getPratica().add(pathEmailIn);
		protocollazioneRequest.setPratiche(pratiche);
		protocollazioneRequest.getDatoAggiuntivo().addAll(creaFascicoloDto.getDatiAggiuntivi());

		protocollazioneRequest.setAllegati(new Allegati());

		if (utente != null)
			protocollazioneRequest.setUtente(utente.getUsername());

		if (numeroCapofila != null)
			protocollazioneRequest.setNumerocapofila(numeroCapofila);

		if (annoCapofila != null)
			protocollazioneRequest.setAnnocapofila(annoCapofila);

		if (fromBa01) {
			it.bologna.comune.alfresco.creazione.fascicolo.CapofilaDetail detail = new it.bologna.comune.alfresco.creazione.fascicolo.CapofilaDetail();
			detail.setAnnoPgCapofila(Integer.valueOf(annoCapofila));
			detail.setNumeroPgCapofila(numeroCapofila);
			protocollazioneRequest.setCapofilaDetail(detail);
		}

		Request requestBean = new Request();
		requestBean.setAlfrescopassword(getAlfrescoPassword());
		requestBean.setAlfrescousername(getAlfrescoUsername());
		requestBean.setRequestparam(request);
		requestBean.setServicename(ServiceNamesUtil.PROTOCOLLAZIONECOMPLETA);
		requestBean.setUtente(SpagicClientRemoteProxyUtil.convert(utente));
		requestBean.setInternalrequestparam(SpagicClientSerializationUtil.getCreateFascicoloRequestToString(protocollazioneRequest));

		Response protocolla = invokeSpagicService(requestBean);
		if (protocolla == null)
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);

		if (protocolla.getError() != null)
			throw SpagicClientRemoteProxyUtil.processErrorResponse(protocolla.getError());

		return protocolla;
	}

	@Override
	protected String getJaxbRequestToXml(Request request) throws JAXBException {
		return SpagicClientSerializationUtil.getRequestToString(request);
	}

	@Override
	protected Response getXmlResponseToJaxb(String response) throws JAXBException {
		return SpagicClientSerializationUtil.getResponseXmlToObject(response);
	}

}
