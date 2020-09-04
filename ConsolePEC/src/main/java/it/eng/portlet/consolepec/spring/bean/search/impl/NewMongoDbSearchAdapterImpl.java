package it.eng.portlet.consolepec.spring.bean.search.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import it.eng.cobo.consolepec.commons.dto.TaskFirmaDocumentale;
import it.eng.consolepec.spagicclient.SpagicClientNewSearchPratiche;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.search.SearchObjectResult;
import it.eng.consolepec.spagicclient.search.SearchPraticheFilter;
import it.eng.consolepec.spagicclient.search.SearchPraticheSort;
import it.eng.portlet.consolepec.gwt.shared.action.CercaPratiche;
import it.eng.portlet.consolepec.gwt.shared.action.cartellafirma.CercaDocumentoFirmaVistoAction;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.DocumentoFirmaVistoDTO;
import it.eng.portlet.consolepec.spring.bean.search.MongoDbSearchResponseParser;
import it.eng.portlet.consolepec.spring.bean.search.NewMongoDbSearchAdapter;
import it.eng.portlet.consolepec.spring.bean.search.NewMongoDbSearchDocumentRequestGenerator;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

public class NewMongoDbSearchAdapterImpl implements NewMongoDbSearchAdapter {

	private final Logger logger = LoggerFactory.getLogger(NewMongoDbSearchAdapterImpl.class);
	@Autowired
	SpagicClientNewSearchPratiche spagicClientNewSearchPratiche;
	@Autowired
	MongoDbSearchResponseParser mongoDbSearchResponseParser;
	@Autowired
	NewMongoDbSearchDocumentRequestGenerator newMongoDbSearchDocumentRequestGenerator;
	@Autowired
	UserSessionUtil userSessionUtil;

	@Override
	public CountResponse countPratiche(CercaPratiche action) {
		logger.debug("Inizio count pratiche");
		SearchPraticheFilter filter = newMongoDbSearchDocumentRequestGenerator.generaFilter(action);
		int count = spagicClientNewSearchPratiche.count(filter, userSessionUtil.getUtenteSpagic());
		logger.debug("risultato count: {}", count);
		return new CountResponse(count, false);
	}

	@Override
	public List<PraticaDTO> searchPratiche(CercaPratiche action) {
		try {
			logger.debug("Inizio cerca pratiche");
			logger.debug("Invoco il client spagic per la ricerca ");

			SearchPraticheFilter filter = newMongoDbSearchDocumentRequestGenerator.generaFilter(action);
			SearchPraticheSort sort = newMongoDbSearchDocumentRequestGenerator.generaSort(action);

			int limit = action.getFine() - action.getInizio();
			int offset = action.getInizio();

			List<SearchObjectResult> searchMetadata = spagicClientNewSearchPratiche.search(filter, sort, limit, offset, userSessionUtil.getUtenteSpagic());

			return mongoDbSearchResponseParser.metadatiResponseToPraticaDTO(searchMetadata);

		} catch (SpagicClientException e) {
			logger.error("Errore recupero documenti del serizio spagic: controllare i log", e);
			return null;
		}
	}

	@Override
	public CountResponse countDocumentiFirmaVisto(CercaDocumentoFirmaVistoAction documentoFirmaVistoAction) {
		logger.debug("Inizio count pratiche");
		SearchPraticheFilter filter = newMongoDbSearchDocumentRequestGenerator.generaFilter(documentoFirmaVistoAction);
		int count = spagicClientNewSearchPratiche.countRichiesteFirma(filter, userSessionUtil.getUtenteSpagic());
		logger.debug("risultato count: {}", count);
		return new CountResponse(count, false);
	}

	@Override
	public List<DocumentoFirmaVistoDTO> searchDocumentiFirmaVisto(CercaDocumentoFirmaVistoAction documentoFirmaVistoAction) {
		try {
			logger.debug("Inizio cerca pratiche");
			logger.debug("Invoco il client spagic per la ricerca ");

			SearchPraticheFilter filter = newMongoDbSearchDocumentRequestGenerator.generaFilter(documentoFirmaVistoAction);
			SearchPraticheSort sort = newMongoDbSearchDocumentRequestGenerator.generaSort(documentoFirmaVistoAction);

			int limit = documentoFirmaVistoAction.getFine();
			logger.debug("Limit: {}", limit);
			int offset = documentoFirmaVistoAction.getInizio();
			logger.debug("Offset: {}", offset);

			List<TaskFirmaDocumentale> searchMetadata = spagicClientNewSearchPratiche.searchRichiesteFirma(filter, sort, limit, offset, userSessionUtil.getUtenteSpagic());

			return mongoDbSearchResponseParser.metadatiResponseToDocumentoFirmaVistoDTO(searchMetadata);

		} catch (SpagicClientException e) {
			logger.error("Errore recupero documenti del serizio spagic: controllare i log", e);
			return null;

		} catch (IllegalArgumentException e) {
			logger.error("Dati del task di firma recuperati non validi", e);
			return null;
		}
	}
}
