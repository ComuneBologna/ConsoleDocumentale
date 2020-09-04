package it.eng.portlet.consolepec.spring.bean.search;

import java.util.List;

import it.eng.cobo.consolepec.commons.dto.TaskFirmaDocumentale;
import it.eng.consolepec.spagicclient.search.SearchObjectResult;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.DocumentoFirmaVistoDTO;

public interface MongoDbSearchResponseParser {

	List<PraticaDTO> metadatiResponseToPraticaDTO(List<SearchObjectResult> searchMetadata);

	List<DocumentoFirmaVistoDTO> metadatiResponseToDocumentoFirmaVistoDTO(List<TaskFirmaDocumentale> searchMetadata);

}
