package it.eng.consolepec.spagicclient.remoteproxy.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.JAXBException;

import it.bologna.comune.alfresco.generic.service.Request;
import it.bologna.comune.alfresco.generic.service.Response;
import it.bologna.comune.mongodb.search.pratiche.DestinatarioGruppoRichiestaFirma;
import it.bologna.comune.mongodb.search.pratiche.DestinatarioRichiestaFirma;
import it.bologna.comune.mongodb.search.pratiche.DestinatarioUtenteRichiestaFirma;
import it.bologna.comune.mongodb.search.pratiche.Filtro;
import it.bologna.comune.mongodb.search.pratiche.Filtro.ValoreMappa;
import it.bologna.comune.mongodb.search.pratiche.Metadato;
import it.bologna.comune.mongodb.search.pratiche.PraticaResult;
import it.bologna.comune.mongodb.search.pratiche.RichiestaFirmaResult;
import it.bologna.comune.mongodb.search.pratiche.Sort;
import it.bologna.comune.mongodb.search.pratiche.SortType;
import it.eng.cobo.consolepec.commons.dto.TaskFirmaDocumentale;
import it.eng.cobo.consolepec.commons.dto.TaskFirmaDocumentale.DestinatarioDocumentale;
import it.eng.cobo.consolepec.commons.dto.TaskFirmaDocumentale.DestinatarioGruppoDocumentale;
import it.eng.cobo.consolepec.commons.dto.TaskFirmaDocumentale.DestinatarioUtenteDocumentale;
import it.eng.cobo.consolepec.commons.dto.TaskFirmaDocumentale.RiferimentoAllegatoDocumentale;
import it.eng.cobo.consolepec.util.date.DateUtils;
import it.eng.consolepec.client.RicercaPraticheClient;
import it.eng.consolepec.spagicclient.SpagicClientNewSearchPratiche;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.abs.AbstractSpagicClientRemoteProxy;
import it.eng.consolepec.spagicclient.remoteproxy.abs.SpagicClientRemoteProxyUtil;
import it.eng.consolepec.spagicclient.remoteproxy.abs.SpagicClientSerializationUtil;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientErrorCode;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.search.SearchObjectResult;
import it.eng.consolepec.spagicclient.search.SearchPraticheFilter;
import it.eng.consolepec.spagicclient.search.SearchPraticheFilter.SearchFilter;
import it.eng.consolepec.spagicclient.search.SearchPraticheSort;
import it.eng.consolepec.spagicclient.servicename.ServiceNamesUtil;

/**
 * Per utilizzo interno. Per ricerche esterne {@link RicercaPraticheClient}
 */
public class SpagicClientNewSearchPraticheRemoteProxy extends AbstractSpagicClientRemoteProxy<Request, Response> implements SpagicClientNewSearchPratiche {

	protected SpagicClientNewSearchPraticheRemoteProxy(String serviceProxyUrl, String alfrescoUsername, String alfrescoPassword, String serviceUsername, String servicePassword,
			String restServiceUrl) {
		super(serviceProxyUrl, alfrescoUsername, alfrescoPassword, serviceUsername, servicePassword, restServiceUrl);
	}

	@Override
	public List<SearchObjectResult> search(SearchPraticheFilter filtri, SearchPraticheSort searchPraticheSort, int limit, int offset, Utente utente) throws SpagicClientException {
		Request request = new Request();
		request.setAlfrescopassword(getAlfrescoPassword());
		request.setAlfrescousername(getAlfrescoUsername());
		request.setServicename(ServiceNamesUtil.SEARCH_PRATICHE_MONGO);
		request.setUtente(SpagicClientRemoteProxyUtil.convert(utente));

		String searchRequestToString = SpagicClientSerializationUtil.getRequestSearchPraticheToString(createRequest(filtri, searchPraticheSort, limit, offset, false));
		request.setRequestparam(searchRequestToString);
		Response response = invokeSpagicService(request);
		if (response.getError() != null)
			throw SpagicClientRemoteProxyUtil.processErrorResponse(response.getError());
		it.bologna.comune.mongodb.search.pratiche.Response searchResponse = SpagicClientSerializationUtil.getResponseSearchPraticheToObject(response.getResponseparam());
		return createResponse(searchResponse);
	}

	@Override
	public List<SearchObjectResult> search(SearchPraticheFilter filtri, int limit, int offset, Utente utente) throws SpagicClientException {
		Request request = new Request();
		request.setAlfrescopassword(getAlfrescoPassword());
		request.setAlfrescousername(getAlfrescoUsername());
		request.setServicename(ServiceNamesUtil.SEARCH_PRATICHE_MONGO);
		request.setUtente(SpagicClientRemoteProxyUtil.convert(utente));

		String searchRequestToString = SpagicClientSerializationUtil.getRequestSearchPraticheToString(createRequest(filtri, null, limit, offset, false));
		request.setRequestparam(searchRequestToString);
		Response response = invokeSpagicService(request);
		if (response.getError() != null)
			throw SpagicClientRemoteProxyUtil.processErrorResponse(response.getError());
		it.bologna.comune.mongodb.search.pratiche.Response searchResponse = SpagicClientSerializationUtil.getResponseSearchPraticheToObject(response.getResponseparam());
		return createResponse(searchResponse);
	}

	@Override
	public int count(SearchPraticheFilter filtri, Utente utente) throws SpagicClientException {
		Request request = new Request();
		request.setAlfrescopassword(getAlfrescoPassword());
		request.setAlfrescousername(getAlfrescoUsername());
		request.setServicename(ServiceNamesUtil.SEARCH_PRATICHE_MONGO);
		request.setUtente(SpagicClientRemoteProxyUtil.convert(utente));

		String searchRequestToString = SpagicClientSerializationUtil.getRequestSearchPraticheToString(createRequest(filtri, null, -1, -1, true));
		request.setRequestparam(searchRequestToString);
		Response response = invokeSpagicService(request);
		if (response.getError() != null)
			throw SpagicClientRemoteProxyUtil.processErrorResponse(response.getError());
		it.bologna.comune.mongodb.search.pratiche.Response searchResponse = SpagicClientSerializationUtil.getResponseSearchPraticheToObject(response.getResponseparam());
		return searchResponse.getCount();
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

	@SuppressWarnings("unchecked")
	private static it.bologna.comune.mongodb.search.pratiche.Request createRequest(SearchPraticheFilter filtri, SearchPraticheSort searchPraticheSort, int limit, int offset, boolean count) {
		it.bologna.comune.mongodb.search.pratiche.Request searchRequest = new it.bologna.comune.mongodb.search.pratiche.Request();

		searchRequest.setCount(count);
		searchRequest.setLimit(limit);
		searchRequest.setOffset(offset);

		for (SearchFilter filtro : filtri) {
			Filtro f = new Filtro();
			f.setNome(filtro.getName());

			if (filtro.getType() == SearchFilter.STRING_TYPE) {
				f.setValoreSingolo((String) filtro.getValue());
			}
			if (filtro.getType() == SearchFilter.LIST_TYPE) {
				List<String> valori = (List<String>) filtro.getValue();
				f.getValoreLista().addAll(valori);
			}
			if (filtro.getType() == SearchFilter.MAP_TYPE) {
				Map<String, Object> valori = (Map<String, Object>) filtro.getValue();
				for (Entry<String, Object> entry : valori.entrySet()) {
					ValoreMappa vm = new ValoreMappa();
					vm.setChiave(entry.getKey());

					if (entry.getValue() instanceof String) {
						vm.setValore((String) entry.getValue());
					}

					if (entry.getValue() instanceof List) {
						vm.getValori().addAll((Collection<? extends String>) entry.getValue());
					}

					f.getValoreMappa().add(vm);
				}
			}

			searchRequest.getFiltri().add(f);
		}

		if (searchPraticheSort != null) {
			for (Entry<String, Integer> e : searchPraticheSort) {
				Sort s = new Sort();
				s.setNome(e.getKey());
				;
				s.setType(e.getValue() == SearchPraticheSort.ASC ? SortType.ASC : SortType.DESC);
				searchRequest.getSorts().add(s);
			}
		}

		return searchRequest;
	}

	private static List<SearchObjectResult> createResponse(it.bologna.comune.mongodb.search.pratiche.Response searchResponse) {
		List<SearchObjectResult> objectResults = new ArrayList<SearchObjectResult>();
		List<PraticaResult> results = searchResponse.getPratiche();
		for (PraticaResult result : results) {
			SearchObjectResult searchObjectResult = new SearchObjectResult();
			for (Metadato metadato : result.getMetadato()) {
				searchObjectResult.addMetadato(metadato.getNome(), metadato.getValore());
			}
			objectResults.add(searchObjectResult);
		}
		return objectResults;
	}

	@Override
	public List<TaskFirmaDocumentale> searchRichiesteFirma(SearchPraticheFilter filter, SearchPraticheSort searchPraticheSort, int limit, int offset, Utente utente) throws SpagicClientException {
		Request request = new Request();
		request.setAlfrescopassword(getAlfrescoPassword());
		request.setAlfrescousername(getAlfrescoUsername());
		request.setServicename(ServiceNamesUtil.SEARCH_RICHIESTE_FIRMA_MONGO);
		request.setUtente(SpagicClientRemoteProxyUtil.convert(utente));

		String searchRequestToString = SpagicClientSerializationUtil.getRequestSearchPraticheToString(createRequest(filter, searchPraticheSort, limit, offset, false));
		request.setRequestparam(searchRequestToString);
		Response response = invokeSpagicService(request);
		if (response.getError() != null)
			throw SpagicClientRemoteProxyUtil.processErrorResponse(response.getError());
		it.bologna.comune.mongodb.search.pratiche.Response searchResponse = SpagicClientSerializationUtil.getResponseSearchPraticheToObject(response.getResponseparam());
		return createRichiestaFirmaResponse(searchResponse);
	}

	@Override
	public int countRichiesteFirma(SearchPraticheFilter filter, Utente utente) throws SpagicClientException {
		Request request = new Request();
		request.setAlfrescopassword(getAlfrescoPassword());
		request.setAlfrescousername(getAlfrescoUsername());
		request.setServicename(ServiceNamesUtil.SEARCH_RICHIESTE_FIRMA_MONGO);
		request.setUtente(SpagicClientRemoteProxyUtil.convert(utente));

		String searchRequestToString = SpagicClientSerializationUtil.getRequestSearchPraticheToString(createRequest(filter, null, -1, -1, true));
		request.setRequestparam(searchRequestToString);
		Response response = invokeSpagicService(request);
		if (response.getError() != null)
			throw SpagicClientRemoteProxyUtil.processErrorResponse(response.getError());
		it.bologna.comune.mongodb.search.pratiche.Response searchResponse = SpagicClientSerializationUtil.getResponseSearchPraticheToObject(response.getResponseparam());
		return searchResponse.getCount();
	}

	private static List<TaskFirmaDocumentale> createRichiestaFirmaResponse(it.bologna.comune.mongodb.search.pratiche.Response searchResponse) {

		List<TaskFirmaDocumentale> result = new ArrayList<TaskFirmaDocumentale>();
		if (searchResponse.getRichiesteFirma() != null) {
			for (RichiestaFirmaResult r : searchResponse.getRichiesteFirma()) {
				TaskFirmaDocumentale s = new TaskFirmaDocumentale();
				RiferimentoAllegatoDocumentale a = new RiferimentoAllegatoDocumentale();
				a.setCurrentVersion(r.getAllegato().getCurrentVersion());
				a.setNome(r.getAllegato().getNome());
				s.setAllegato(a);
				s.setDataProposta(DateUtils.xmlGrCalToDate(r.getDataProposta()));
				s.getDestinatari().addAll(buildDestinatari(r.getDestinatari()));
				s.setGruppoProponente(r.getGruppoProponente());
				s.setPraticaPath(r.getPraticaPath());
				s.setStatoRichiesta(r.getStato());
				s.setTipoRichiesta(r.getTipo());
				s.setMittenteOriginale(r.getMittenteOriginale());
				s.setIdTaskFirma(r.getIdTaskFirma());
				if (r.getDataScadenza() != null)
					s.setDataScadenza(DateUtils.xmlGrCalToDate(r.getDataScadenza()));

				s.setOperazioniDestinatarioAbilitate(r.isOperazioniDestinatarioAbilitate());
				s.setOperazioniProponenteAbilitate(r.isOperazioniProponenteAbilitate());
				result.add(s);
			}
		}
		return result;
	}

	private static List<DestinatarioDocumentale> buildDestinatari(List<DestinatarioRichiestaFirma> destinatari) {
		List<DestinatarioDocumentale> lista = new ArrayList<DestinatarioDocumentale>();
		if (destinatari != null) {
			for (DestinatarioRichiestaFirma destinatario : destinatari) {
				lista.add(convertDestinatario(destinatario));
			}
		}
		return lista;
	}

	private static DestinatarioDocumentale convertDestinatario(DestinatarioRichiestaFirma destinatario) {

		if (destinatario instanceof DestinatarioUtenteRichiestaFirma) {
			DestinatarioUtenteRichiestaFirma d = (DestinatarioUtenteRichiestaFirma) destinatario;
			DestinatarioUtenteDocumentale result = new DestinatarioUtenteDocumentale();
			result.setStato(d.getStato());
			result.setUserid(d.getUserid());
			result.setNome(d.getNome());
			result.setCognome(d.getCognome());
			result.setSettore(d.getSettore());
			result.setMatricola(d.getMatricola());
			return result;

		} else if (destinatario instanceof DestinatarioGruppoRichiestaFirma) {
			DestinatarioGruppoRichiestaFirma d = (DestinatarioGruppoRichiestaFirma) destinatario;
			DestinatarioGruppoDocumentale result = new DestinatarioGruppoDocumentale();
			result.setStato(d.getStato());
			result.setNomeGruppo(d.getNomeGruppo());
			return result;

		} else {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION, "Tipo destinatario non valido");
		}
	}
}
