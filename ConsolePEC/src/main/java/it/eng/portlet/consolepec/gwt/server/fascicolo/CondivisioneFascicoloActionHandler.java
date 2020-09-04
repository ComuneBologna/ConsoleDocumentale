package it.eng.portlet.consolepec.gwt.server.fascicolo;

import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CondivisioneFascicolo;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CondivisioneFascicoloResult;
import it.eng.portlet.consolepec.spring.bean.gestionepratiche.GestioneCondivisioniFascicolo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class CondivisioneFascicoloActionHandler implements ActionHandler<CondivisioneFascicolo, CondivisioneFascicoloResult> {

	@Autowired
	GestioneCondivisioniFascicolo gestioneCondivisioneFascicolo;
	
	private final Logger logger = LoggerFactory.getLogger(CondivisioneFascicoloActionHandler.class);
	
	public CondivisioneFascicoloActionHandler() {
	}

	@Override
	public CondivisioneFascicoloResult execute(CondivisioneFascicolo action, ExecutionContext context) throws ActionException {
		logger.debug("Gestione condivisione singolo gruppo");
		return gestioneCondivisioneFascicolo.gestioneCondivisioneFascicolo(action);
	}

	@Override
	public void undo(CondivisioneFascicolo action, CondivisioneFascicoloResult result, ExecutionContext context) throws ActionException {
	}

	@Override
	public Class<CondivisioneFascicolo> getActionType() {
		return CondivisioneFascicolo.class;
	}
}
