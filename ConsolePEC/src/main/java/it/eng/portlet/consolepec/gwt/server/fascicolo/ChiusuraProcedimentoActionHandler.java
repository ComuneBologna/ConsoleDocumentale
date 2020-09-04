package it.eng.portlet.consolepec.gwt.server.fascicolo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.cobo.consolepec.commons.procedimento.ChiusuraProcedimentoInput;
import it.eng.cobo.consolepec.commons.procedimento.ChiusuraProcedimentoOutput;
import it.eng.cobo.consolepec.util.console.ConsoleConstants;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.ChiusuraProcedimentoAction;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.ChiusuraProcedimentoResult;
import it.eng.portlet.consolepec.spring.bean.procedimenti.GestioneProcedimenti;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;
import it.eng.portlet.consolepec.spring.bean.tipologiaprocedimenti.RecuperoTipologiaProcedimenti;

public class ChiusuraProcedimentoActionHandler implements ActionHandler<ChiusuraProcedimentoAction, ChiusuraProcedimentoResult> {

	@Autowired
	UserSessionUtil userSessionUtil;

	@Autowired
	GestioneProcedimenti gestioneProcedimenti;

	@Autowired
	RecuperoTipologiaProcedimenti recuperoTipologiaProcedimenti;

	@Autowired
	PraticaSessionUtil praticaSessionUtil;

	private static final Logger logger = LoggerFactory.getLogger(ChiusuraProcedimentoActionHandler.class);

	public ChiusuraProcedimentoActionHandler() {}

	@Override
	public ChiusuraProcedimentoResult execute(ChiusuraProcedimentoAction action, ExecutionContext context) throws ActionException {

		logger.info("Inizio esecuzione servizio di Chiusura Procedimento: {}", action);
		ChiusuraProcedimentoResult result = null;

		try {
			ChiusuraProcedimentoInput dto = new ChiusuraProcedimentoInput();
			// fissi
			dto.setCodUtente(ConsoleConstants.Procedimenti.COD_UTENTE_PROCEDIMENTI);
			// obbligatori
			dto.setAnnoProtocollazione(action.getAnnoProtocollazione());
			dto.setNumProtocollazione(action.getNumProtocollazione());
			dto.setCodTipologiaProcedimento(action.getCodTipologiaProcedimento());
			dto.setDataChiusura(action.getDataChiusura());
			dto.setModalitaChiusura(action.getModalitaChiusura());
			// facoltativi (dipendono dalla modalità di chiusura)
			dto.setAnnoProtocolloDocChiusura(action.getAnnoProtocolloDocChiusura());
			dto.setNumProtocolloDocChiusura(action.getNumProtocolloDocChiusura());

			logger.info("Chiusura procedimento protocollazione {}/{} pratica {}", dto.getNumProtocollazione(), dto.getAnnoProtocollazione(), action.getIdDocumentaleFascicoloConProcedimento());
			ChiusuraProcedimentoOutput chiusuraProcedimentoOutput = gestioneProcedimenti.chiudiProcedimento(dto, action.getIdDocumentaleFascicoloConProcedimento());

			result = new ChiusuraProcedimentoResult(action.getFascicoloCorrentePath(), recuperoTipologiaProcedimenti.getDescrizioneProcedimento(dto.getCodTipologiaProcedimento()),
					dto.getCodTipologiaProcedimento(), action.getDataInizio(), action.getDataChiusura(), action.getTermine(), chiusuraProcedimentoOutput.getDurata());

			praticaSessionUtil.removePraticaFromEncodedPath(action.getFascicoloCorrentePath());

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			result = new ChiusuraProcedimentoResult(e.getErrorMessage());

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			result = new ChiusuraProcedimentoResult(ConsolePecConstants.ERROR_MESSAGE);

		} finally {
			logger.info("Fine esecuzione servizio di Chiusura Procedimento: {}", action);
		}

		return result;

	}

	@Override
	public void undo(ChiusuraProcedimentoAction action, ChiusuraProcedimentoResult result, ExecutionContext context) throws ActionException {}

	@Override
	public Class<ChiusuraProcedimentoAction> getActionType() {
		return ChiusuraProcedimentoAction.class;
	}
}
