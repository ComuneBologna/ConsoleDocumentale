package it.eng.portlet.consolepec.gwt.server.modulistica;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.xmlplugin.pratica.modulistica.PraticaModulistica;
import it.eng.portlet.consolepec.gwt.server.XMLPluginToDTOConverter;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.modulistica.CaricaPraticaModulisticaAction;
import it.eng.portlet.consolepec.gwt.shared.action.modulistica.CaricaPraticaModulisticaActionResult;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaModulisticaDTO;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

/**
 *
 * @author pluttero
 *
 */
public class CaricaPraticaModulisticaActionHandler implements ActionHandler<CaricaPraticaModulisticaAction, CaricaPraticaModulisticaActionResult> {

	@Autowired
	XMLPluginToDTOConverter utilPratica;

	@Autowired
	UserSessionUtil userSessionUtil;

	@Autowired
	PraticaSessionUtil praticaSessionUtil;

	private static final Logger logger = LoggerFactory.getLogger(CaricaPraticaModulisticaActionHandler.class);

	public CaricaPraticaModulisticaActionHandler() {}

	@Override
	public CaricaPraticaModulisticaActionResult execute(CaricaPraticaModulisticaAction action, ExecutionContext context) throws ActionException {

		CaricaPraticaModulisticaActionResult res = new CaricaPraticaModulisticaActionResult();

		logger.debug("Caricamento pratica modulistica con encoded path: {}", action.getClientID());
		PraticaModulistica modulistica;
		try {
			modulistica = praticaSessionUtil.loadPraticaModulisticaFromEncodedPath(action.getClientID(), action.getTipologiaCaricamento());
			logger.debug("Caricato pratica modulistica: {}", modulistica.getDati());

			if (!modulistica.getDati().isLetto()) {
				modulistica.getDati().setLetto(true);
				praticaSessionUtil.updatePratica(modulistica);
			}

			/*
			 * da ritornare sulla gui : oggetto,allegati, body (estrazione) , id documento email.getUuid()
			 */
			PraticaModulisticaDTO dettaglio = utilPratica.praticaModulisticaToDettaglio(modulistica);

			// verificaAllegati( dettaglio ,mail);

			res.setPratica(dettaglio);
			res.setError(false);

			return res;

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			res.setError(true);
			res.setMessError(e.getErrorMessage());
			return res;

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			res.setError(true);
			res.setMessError(ConsolePecConstants.ERROR_MESSAGE);
			return res;
		}

	}

	@Override
	public void undo(CaricaPraticaModulisticaAction action, CaricaPraticaModulisticaActionResult result, ExecutionContext context) throws ActionException {}

	@Override
	public Class<CaricaPraticaModulisticaAction> getActionType() {
		return CaricaPraticaModulisticaAction.class;
	}

}
