package it.eng.portlet.consolepec.gwt.server.fascicolo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.portlet.consolepec.gwt.server.XMLPluginToDTOConverter;
import it.eng.portlet.consolepec.gwt.servermock.fascicolo.CancellaAllegatoFascicoloMockActionHandler;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CancellaAllegatoFascicolo;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CancellaAllegatoFascicoloResult;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.spring.bean.gestionepratiche.IGestioneAllegati;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;

public class CancellaAllegatoFascicoloActionHandler implements ActionHandler<CancellaAllegatoFascicolo, CancellaAllegatoFascicoloResult> {
	@Autowired
	XMLPluginToDTOConverter utilPratica;
	@Autowired
	IGestioneAllegati gestioneAllegati;
	Logger logger = LoggerFactory.getLogger(CancellaAllegatoFascicoloMockActionHandler.class);
	@Autowired
	PraticaSessionUtil praticaSessionUtil;

	public CancellaAllegatoFascicoloActionHandler() {}

	@Override
	public CancellaAllegatoFascicoloResult execute(CancellaAllegatoFascicolo action, ExecutionContext context) throws ActionException {
		String clientID = action.getClientID();
		logger.debug("Cancellazione allegati per pratica: {}", clientID);
		StringBuilder sb = new StringBuilder();
		for (AllegatoDTO dto : action.getAllegati()) {
			sb.append(dto.getNome()).append(" ");
		}
		logger.debug("Allegati da cancellare: {}", sb.toString());
		CancellaAllegatoFascicoloResult result = null;
		try {
			if (action.getAllegati() != null && action.getAllegati().size() > 0) {
				logger.debug("Invocazione servizio cancellazione");
				Fascicolo fascicolo = null;
				for (AllegatoDTO allegato : action.getAllegati()) {
					fascicolo = (Fascicolo) gestioneAllegati.rimuoviAllegato(clientID, allegato);
				}
				logger.debug("Serializzo il nuovo fascicolo");
				FascicoloDTO dto = utilPratica.fascicoloToDettaglio(fascicolo);
				praticaSessionUtil.updateFascicolo(fascicolo);
				result = new CancellaAllegatoFascicoloResult(dto);
				result.setError(false);
			} else {
				logger.error("Allegati non coerenti con contenuto pratica");
				// erro
				result = new CancellaAllegatoFascicoloResult(null);
				result.setError(true);
				result.setErrorMsg("Elenco allegati non coerente");
			}

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			result = new CancellaAllegatoFascicoloResult(null);
			result.setError(true);
			result.setErrorMsg(e.getErrorMessage());

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			result = new CancellaAllegatoFascicoloResult(null);
			result.setError(true);
			result.setErrorMsg(ConsolePecConstants.ERROR_MESSAGE);
		}

		return result;

	}

	@Override
	public void undo(CancellaAllegatoFascicolo action, CancellaAllegatoFascicoloResult result, ExecutionContext context) throws ActionException {}

	@Override
	public Class<CancellaAllegatoFascicolo> getActionType() {
		return CancellaAllegatoFascicolo.class;
	}

}
