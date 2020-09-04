package it.eng.portlet.consolepec.gwt.server.modulistica;

import it.eng.portlet.consolepec.gwt.shared.action.modulistica.CambiaStatoPraticaModulistica;
import it.eng.portlet.consolepec.gwt.shared.action.modulistica.CambiaStatoPraticaModulisticaResult;
import it.eng.portlet.consolepec.spring.bean.gestionepratiche.AggiornaStatoPraticaModulistica;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class CambiaStatoPraticaModulisticaActionHandler implements ActionHandler<CambiaStatoPraticaModulistica, CambiaStatoPraticaModulisticaResult> {

	Logger logger = LoggerFactory.getLogger(CambiaStatoPraticaModulisticaActionHandler.class);

	@Autowired
	AggiornaStatoPraticaModulistica aggiornaStatoPraticaModulistica;

	@Override
	public void undo(CambiaStatoPraticaModulistica action, CambiaStatoPraticaModulisticaResult result, ExecutionContext context) throws ActionException {
	}

	@Override
	public Class<CambiaStatoPraticaModulistica> getActionType() {
		return CambiaStatoPraticaModulistica.class;
	}

	@Override
	public CambiaStatoPraticaModulisticaResult execute(CambiaStatoPraticaModulistica action, ExecutionContext context)	throws ActionException {
		logger.debug("Richiesto cambio stato PraticaModulistica");
		return aggiornaStatoPraticaModulistica.cambiaStato(action);
	}
}
