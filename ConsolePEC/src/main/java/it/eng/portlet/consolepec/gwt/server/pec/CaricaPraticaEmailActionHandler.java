package it.eng.portlet.consolepec.gwt.server.pec;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.xmlplugin.factory.Task;
import it.eng.consolepec.xmlplugin.pratica.email.PraticaEmailIn;
import it.eng.portlet.consolepec.gwt.server.XMLPluginToDTOConverter;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.pec.CaricaPraticaEmailInAction;
import it.eng.portlet.consolepec.gwt.shared.action.pec.CaricaPraticaEmailInActionResult;
import it.eng.portlet.consolepec.gwt.shared.model.PecInDTO;
import it.eng.portlet.consolepec.spring.bean.gestionepratiche.GestioneTaskPratiche;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

public class CaricaPraticaEmailActionHandler implements ActionHandler<CaricaPraticaEmailInAction, CaricaPraticaEmailInActionResult> {

	Logger logger = LoggerFactory.getLogger(CaricaPraticaEmailActionHandler.class);

	@Autowired
	HttpServletRequest httpServletRequest;
	@Autowired
	XMLPluginToDTOConverter utilPratica;
	@Autowired
	PraticaSessionUtil praticaSessionUtil;
	@Autowired
	UserSessionUtil userSessionUtil;
	@Autowired
	GestioneTaskPratiche gestioneTaskPratiche;

	public CaricaPraticaEmailActionHandler() {}

	@Override
	public CaricaPraticaEmailInActionResult execute(CaricaPraticaEmailInAction action, ExecutionContext context) throws ActionException {
		CaricaPraticaEmailInActionResult res = new CaricaPraticaEmailInActionResult();

		try {
			PraticaEmailIn mail = praticaSessionUtil.loadPraticaEmailInFromEncodedPath(action.getClientID(), action.getTipologiaCaricamento());
			Task<?> estraiTaskCorrente = gestioneTaskPratiche.estraiTaskCorrenteSoloAssegnatario(mail);

			if ((!mail.getDati().isLetto()) && (estraiTaskCorrente != null)) {
				mail.getDati().setLetto(true);
				praticaSessionUtil.updatePratica(mail);
			}

			/*
			 * da ritornare sulla gui : oggetto,allegati, body (estrazione) , id documento email.getUuid()
			 */
			PecInDTO dettaglio = utilPratica.emailToDettaglioIN(mail);

			res.setDettaglio(dettaglio);

			return res;

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			res.setError(true);
			res.setErrorMessage(e.getErrorMessage());
			return res;

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			res.setError(true);
			res.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
			return res;
		}

	}

	@Override
	public void undo(CaricaPraticaEmailInAction action, CaricaPraticaEmailInActionResult result, ExecutionContext context) throws ActionException {}

	@Override
	public Class<CaricaPraticaEmailInAction> getActionType() {
		return CaricaPraticaEmailInAction.class;
	}
}
