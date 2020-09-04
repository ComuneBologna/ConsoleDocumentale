package it.eng.portlet.consolepec.gwt.server.fascicolo;

import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CambiaStatoFascicolo;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CambiaStatoFascicoloResult;
import it.eng.portlet.consolepec.spring.bean.gestionepratiche.AggiornaStatoFascicolo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class CambiaStatoFascicoloActionHandler implements ActionHandler<CambiaStatoFascicolo, CambiaStatoFascicoloResult> {

	Logger logger = LoggerFactory.getLogger(CambiaStatoFascicoloActionHandler.class);

	@Autowired
	AggiornaStatoFascicolo aggiornaStatoFascicolo;

	public CambiaStatoFascicoloActionHandler() {
	}

	@Override
	public CambiaStatoFascicoloResult execute(CambiaStatoFascicolo action, ExecutionContext context) throws ActionException {
		logger.debug("Richiesto cambio stato Fascicolo");
		return aggiornaStatoFascicolo.cambiaStato(action);

	}

	@Override
	public void undo(CambiaStatoFascicolo action, CambiaStatoFascicoloResult result, ExecutionContext context) throws ActionException {
	}

	@Override
	public Class<CambiaStatoFascicolo> getActionType() {
		return CambiaStatoFascicolo.class;
	}
}
