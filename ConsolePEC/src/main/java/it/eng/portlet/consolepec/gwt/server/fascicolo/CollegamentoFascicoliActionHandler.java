package it.eng.portlet.consolepec.gwt.server.fascicolo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CollegamentoFascicoli;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CollegamentoFascicoliResult;
import it.eng.portlet.consolepec.spring.bean.gestionepratiche.GestioneCollegamentiFascicolo;

public class CollegamentoFascicoliActionHandler implements ActionHandler<CollegamentoFascicoli, CollegamentoFascicoliResult> {

	@Autowired GestioneCollegamentiFascicolo gestioneCollegamentiFascicolo;

	private final Logger logger = LoggerFactory.getLogger(CollegamentoFascicoliActionHandler.class);

	public CollegamentoFascicoliActionHandler() {}

	@Override
	public CollegamentoFascicoliResult execute(CollegamentoFascicoli action, ExecutionContext context) throws ActionException {
		logger.debug("Gestione colegamento singolo fascicolo");
		return gestioneCollegamentiFascicolo.gestioneCollegamentoFascicolo(action);
	}

	@Override
	public void undo(CollegamentoFascicoli action, CollegamentoFascicoliResult result, ExecutionContext context) throws ActionException {/**/}

	@Override
	public Class<CollegamentoFascicoli> getActionType() {
		return CollegamentoFascicoli.class;
	}
}
