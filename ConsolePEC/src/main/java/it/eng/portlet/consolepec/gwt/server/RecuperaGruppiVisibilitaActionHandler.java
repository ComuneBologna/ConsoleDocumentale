package it.eng.portlet.consolepec.gwt.server;

import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.RecuperaGruppiVisibilita;
import it.eng.portlet.consolepec.gwt.shared.action.RecuperaGruppiVisibilitaResult;
import it.eng.portlet.consolepec.gwt.shared.model.GruppoVisibilita;
import it.eng.portlet.consolepec.spring.bean.visibilita.GestioneGruppiUtentiVisibilita;

public class RecuperaGruppiVisibilitaActionHandler implements ActionHandler<RecuperaGruppiVisibilita, RecuperaGruppiVisibilitaResult> {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private GestioneGruppiUtentiVisibilita gestioneGruppiUtentiVisibilita;

	@Override
	public RecuperaGruppiVisibilitaResult execute(final RecuperaGruppiVisibilita action, ExecutionContext context) throws ActionException {

		try {
			TipologiaPratica tipoPratica = action.getTipologiaPratica();
			logger.info("Recupero utenti per tipo pratica {}", tipoPratica);
			TreeSet<GruppoVisibilita> gruppiVisibilita = gestioneGruppiUtentiVisibilita.getUtentiGruppiVisibilita(tipoPratica);
			RecuperaGruppiVisibilitaResult recuperaGruppiVisibilitaResult = new RecuperaGruppiVisibilitaResult();
			recuperaGruppiVisibilitaResult.setGruppoVisibilita(gruppiVisibilita);
			return recuperaGruppiVisibilitaResult;

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			return new RecuperaGruppiVisibilitaResult(e.getErrorMessage(), true);

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			return new RecuperaGruppiVisibilitaResult(ConsolePecConstants.ERROR_MESSAGE, true);

		}
	}

	@Override
	public void undo(RecuperaGruppiVisibilita action, RecuperaGruppiVisibilitaResult result, ExecutionContext context) throws ActionException {}

	@Override
	public Class<RecuperaGruppiVisibilita> getActionType() {
		return RecuperaGruppiVisibilita.class;
	}
}
