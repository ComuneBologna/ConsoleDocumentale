package it.eng.portlet.consolepec.gwt.server.fascicolo;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.GetDatiAssegnaEsterno;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.GetDatiAssegnaEsternoResult;
import it.eng.portlet.consolepec.spring.bean.configurazioni.GestioneConfigurazioni;

public class GetDatiAssegnaEsternoActionHandler implements ActionHandler<GetDatiAssegnaEsterno, GetDatiAssegnaEsternoResult> {

	private final Logger logger = LoggerFactory.getLogger(GetDatiAssegnaEsternoActionHandler.class);

	@Autowired
	GestioneConfigurazioni gestioneConfigurazioni;

	public GetDatiAssegnaEsternoActionHandler() {}

	@Override
	public Class<GetDatiAssegnaEsterno> getActionType() {
		return GetDatiAssegnaEsterno.class;
	}

	@Override
	public GetDatiAssegnaEsternoResult execute(GetDatiAssegnaEsterno action, ExecutionContext context) throws ActionException {
		try {
			List<String> emails = gestioneConfigurazioni.getEmailAssegnaEsterno();
			return new GetDatiAssegnaEsternoResult(emails);

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			return new GetDatiAssegnaEsternoResult(true, e.getErrorMessage());

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			return new GetDatiAssegnaEsternoResult(true, ConsolePecConstants.ERROR_MESSAGE);

		}
	}

	@Override
	public void undo(GetDatiAssegnaEsterno action, GetDatiAssegnaEsternoResult result, ExecutionContext context) throws ActionException {

	}

}
