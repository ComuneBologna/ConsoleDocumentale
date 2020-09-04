package it.eng.portlet.consolepec.gwt.server.fascicolo;

import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CondivisioneFascicolo;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CondivisioneFascicoloMultipla;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CondivisioneFascicoloMultiplaResult;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CondivisioneFascicoloResult;
import it.eng.portlet.consolepec.spring.bean.gestionepratiche.GestioneCondivisioniFascicolo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class CondivisioneFascicoloMultiplaActionHandler implements ActionHandler<CondivisioneFascicoloMultipla, CondivisioneFascicoloMultiplaResult> {

	@Autowired
	GestioneCondivisioniFascicolo gestioneCondivisioneFascicolo;
	
	private final Logger logger = LoggerFactory.getLogger(CondivisioneFascicoloMultiplaActionHandler.class);
	
	public CondivisioneFascicoloMultiplaActionHandler() {
	}

	@Override
	public CondivisioneFascicoloMultiplaResult execute(CondivisioneFascicoloMultipla action, ExecutionContext context) throws ActionException {
		logger.debug("Gestione condivisione gruppi multipli");
		CondivisioneFascicoloMultiplaResult multiplaResult = new CondivisioneFascicoloMultiplaResult();
		for(CondivisioneFascicolo condivisione : action.getCondivisioni()){
			CondivisioneFascicoloResult singolaResult = gestioneCondivisioneFascicolo.gestioneCondivisioneFascicolo(condivisione);
			multiplaResult.getCondivisioniResult().add(singolaResult);
			if(singolaResult.isError()){
				multiplaResult.setError(true);
				multiplaResult.setErrorMessage(singolaResult.getErrorMsg());
			}
		}
		return multiplaResult;
	}

	@Override
	public void undo(CondivisioneFascicoloMultipla action, CondivisioneFascicoloMultiplaResult result, ExecutionContext context) throws ActionException {
	}

	@Override
	public Class<CondivisioneFascicoloMultipla> getActionType() {
		return CondivisioneFascicoloMultipla.class;
	}
}
