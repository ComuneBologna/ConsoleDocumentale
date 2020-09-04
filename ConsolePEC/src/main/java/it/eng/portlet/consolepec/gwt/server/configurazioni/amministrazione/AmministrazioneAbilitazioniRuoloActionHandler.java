package it.eng.portlet.consolepec.gwt.server.configurazioni.amministrazione;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.consolepec.client.AmministrazioneAbilitazioniRuoloClient;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.configurazioni.amministrazione.AmministrazioneAbilitazioniRuoloAction;
import it.eng.portlet.consolepec.gwt.shared.action.configurazioni.amministrazione.AmministrazioneAbilitazioniRuoloResult;
import it.eng.portlet.consolepec.spring.bean.configurazioni.GestioneConfigurazioni;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

/**
 *
 * @author biagiot
 *
 */
public class AmministrazioneAbilitazioniRuoloActionHandler implements ActionHandler<AmministrazioneAbilitazioniRuoloAction, AmministrazioneAbilitazioniRuoloResult> {

	private static final Logger logger = LoggerFactory.getLogger(AmministrazioneAbilitazioniRuoloActionHandler.class);

	@Autowired
	AmministrazioneAbilitazioniRuoloClient amministrazioneAbilitazioniRuoloClient;

	@Autowired
	GestioneConfigurazioni gestioneConfigurazioni;

	@Autowired
	UserSessionUtil userSessionUtil;

	@Override
	public AmministrazioneAbilitazioniRuoloResult execute(AmministrazioneAbilitazioniRuoloAction action, ExecutionContext arg1) throws ActionException {
		AmministrazioneAbilitazioniRuoloResult result = null;

		if (action.getOperazione() == null) {
			return new AmministrazioneAbilitazioniRuoloResult("Nessuna operazione specificata");
		}

		try {

			switch (action.getOperazione()) {
			case CARICAMENTO:
				result = new AmministrazioneAbilitazioniRuoloResult(gestioneConfigurazioni.getAbilitazioniRuoli());
				break;

			case AGGREGAZIONE:
				result = new AmministrazioneAbilitazioniRuoloResult(amministrazioneAbilitazioniRuoloClient.aggregate(action.getFiltriRicerca(), action.getLimit(), action.getOffset(),
						action.getOrderBy(), action.getSort(), userSessionUtil.getUtenteSpagic()),
						amministrazioneAbilitazioniRuoloClient.countAggregate(action.getFiltriRicerca(), userSessionUtil.getUtenteSpagic()));
				break;

			default:
				return new AmministrazioneAbilitazioniRuoloResult("Nessuna operazione specificata");
			}

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			result = new AmministrazioneAbilitazioniRuoloResult(e.getErrorMessage());

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			result = new AmministrazioneAbilitazioniRuoloResult(ConsolePecConstants.ERROR_MESSAGE);

		}

		return result;
	}

	@Override
	public Class<AmministrazioneAbilitazioniRuoloAction> getActionType() {
		return AmministrazioneAbilitazioniRuoloAction.class;
	}

	@Override
	public void undo(AmministrazioneAbilitazioniRuoloAction arg0, AmministrazioneAbilitazioniRuoloResult arg1, ExecutionContext arg2) throws ActionException {}

}
