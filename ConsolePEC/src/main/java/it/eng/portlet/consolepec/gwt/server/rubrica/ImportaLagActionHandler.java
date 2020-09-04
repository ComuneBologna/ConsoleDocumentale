package it.eng.portlet.consolepec.gwt.server.rubrica;

import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.cobo.consolepec.commons.rubrica.Anagrafica;
import it.eng.cobo.consolepec.util.validation.ValidationUtilities;
import it.eng.consolepec.client.LagClient;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.rubrica.ImportaLagAction;
import it.eng.portlet.consolepec.gwt.shared.action.rubrica.ImportaLagResult;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author GiacomoFM
 * @since 14/nov/2018
 */
@Slf4j
public class ImportaLagActionHandler implements ActionHandler<ImportaLagAction, ImportaLagResult> {

	@Autowired
	private LagClient lagClient;
	@Autowired
	private UserSessionUtil userSessionUtil;

	@Override
	public ImportaLagResult execute(ImportaLagAction action, ExecutionContext context) throws ActionException {
		try {
			if (!ValidationUtilities.isCodiceFiscaleValid(action.getCodiceFiscale())) {
				return new ImportaLagResult("Il codice fiscale non &egrave; nel formato corretto");
			}
			Anagrafica a = lagClient.importaAnagrafica(action.getCodiceFiscale(), userSessionUtil.getUtenteSpagic());
			return new ImportaLagResult(a);

		} catch (SpagicClientException e) {
			log.error("Errore", e);
			return new ImportaLagResult(e.getErrorMessage());

		} catch (Exception e) {
			log.error("Errore imprevisto", e);
			return new ImportaLagResult(ConsolePecConstants.ERROR_MESSAGE);

		}
	}

	@Override
	public Class<ImportaLagAction> getActionType() {
		return ImportaLagAction.class;
	}

	@Override
	public void undo(ImportaLagAction action, ImportaLagResult result, ExecutionContext context) throws ActionException {/**/}

}
