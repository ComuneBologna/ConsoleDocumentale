package it.eng.portlet.consolepec.gwt.server.protocollazione;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.RecuperaTipologieProcedimenti;
import it.eng.portlet.consolepec.gwt.shared.action.RecuperaTipologieProcedimentiResult;
import it.eng.portlet.consolepec.gwt.shared.dto.TipologiaProcedimentoDto;
import it.eng.portlet.consolepec.spring.bean.tipologiaprocedimenti.RecuperoTipologiaProcedimenti;

public class RecuperaTipologieProcedimentiActionHandler implements ActionHandler<RecuperaTipologieProcedimenti, RecuperaTipologieProcedimentiResult> {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	RecuperoTipologiaProcedimenti recuperoTipologiaProcedimenti;

	public RecuperaTipologieProcedimentiActionHandler() {}

	@Override
	public RecuperaTipologieProcedimentiResult execute(RecuperaTipologieProcedimenti action, ExecutionContext context) throws ActionException {

		logger.info("Inizio recupero tipologie procedimenti");

		try {
			List<TipologiaProcedimentoDto> elencoTipologieProcedimenti = recuperoTipologiaProcedimenti.getElencoTipologieProcedimenti();
			return new RecuperaTipologieProcedimentiResult(elencoTipologieProcedimenti);

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			RecuperaTipologieProcedimentiResult res = new RecuperaTipologieProcedimentiResult();
			res.setError(true);
			res.setMessageError(e.getErrorMessage());
			return res;

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			RecuperaTipologieProcedimentiResult res = new RecuperaTipologieProcedimentiResult();
			res.setError(true);
			res.setMessageError(ConsolePecConstants.ERROR_MESSAGE);
			return res;

		} finally {
			logger.info("Termine recupero tipologie procedimenti");

		}

	}

	@Override
	public void undo(RecuperaTipologieProcedimenti action, RecuperaTipologieProcedimentiResult result, ExecutionContext context) throws ActionException {}

	@Override
	public Class<RecuperaTipologieProcedimenti> getActionType() {
		return RecuperaTipologieProcedimenti.class;
	}
}
