package it.eng.portlet.consolepec.gwt.server.cartellafirma;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.cartellafirma.CercaDocumentoFirmaVistoAction;
import it.eng.portlet.consolepec.gwt.shared.action.cartellafirma.CercaDocumentoFirmaVistoActionResult;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.DocumentoFirmaVistoDTO;
import it.eng.portlet.consolepec.spring.bean.search.NewMongoDbSearchAdapter;
import it.eng.portlet.consolepec.spring.bean.search.NewMongoDbSearchAdapter.CountResponse;

/**
 *
 * @author biagiot
 *
 */
public class CercaDocumentiFirmaVistoActionHandler implements ActionHandler<CercaDocumentoFirmaVistoAction, CercaDocumentoFirmaVistoActionResult> {

	private Logger logger = LoggerFactory.getLogger(CercaDocumentiFirmaVistoActionHandler.class);

	@Autowired
	NewMongoDbSearchAdapter newMongoDbSearchAdapter;

	public CercaDocumentiFirmaVistoActionHandler() {

	}

	@Override
	public CercaDocumentoFirmaVistoActionResult execute(CercaDocumentoFirmaVistoAction action, ExecutionContext context) throws ActionException {
		logger.info("Start CercaDocumentiFirmaVistoActionHandler");

		CercaDocumentoFirmaVistoActionResult result = new CercaDocumentoFirmaVistoActionResult();
		result.setError(false);
		result.setErrorMessage(null);

		try {
			if (action.isCount()) {
				CountResponse countDocumenti = newMongoDbSearchAdapter.countDocumentiFirmaVisto(action);

				if (countDocumenti != null) {
					result.setEstimate(countDocumenti.isEstimate());
					result.setMaxResult(countDocumenti.getCount());

				} else {
					result.setError(true);
					result.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
				}

			} else {

				List<DocumentoFirmaVistoDTO> documentiResult = newMongoDbSearchAdapter.searchDocumentiFirmaVisto(action);

				if (documentiResult != null) {
					result.setDocumentiFirmaVisto(documentiResult);
					logger.debug("Caricati {} documenti firma/visto)", result.getDocumentiFirmaVisto().size());

				} else {
					result.setError(true);
					result.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
				}

			}

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			result.setError(true);
			result.setErrorMessage(e.getErrorMessage());

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			result.setError(true);
			result.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
		}

		logger.info("End CercaDocumentiFirmaVistoActionHandler");
		return result;
	}

	@Override
	public Class<CercaDocumentoFirmaVistoAction> getActionType() {
		return CercaDocumentoFirmaVistoAction.class;
	}

	@Override
	public void undo(CercaDocumentoFirmaVistoAction action, CercaDocumentoFirmaVistoActionResult result, ExecutionContext context) throws ActionException {}

}
