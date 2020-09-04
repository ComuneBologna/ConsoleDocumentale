package it.eng.portlet.consolepec.gwt.server.rubrica;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.cobo.consolepec.commons.rubrica.Anagrafica;
import it.eng.cobo.consolepec.commons.services.AnagraficaResponse;
import it.eng.cobo.consolepec.commons.services.ServiceResponse;
import it.eng.consolepec.client.RubricaClient;
import it.eng.portlet.consolepec.gwt.shared.action.rubrica.RicercaAnagrafiche;
import it.eng.portlet.consolepec.gwt.shared.action.rubrica.RicercaAnagraficheResult;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

public class RicercaAnagraficheActionHandler implements ActionHandler<RicercaAnagrafiche, RicercaAnagraficheResult> {

	// private Logger logger = LoggerFactory.getLogger(RicercaAnagraficheActionHandler.class);

	@Autowired
	RubricaClient rubricaClient;
	@Autowired
	private UserSessionUtil userSessionUtil;

	public RicercaAnagraficheActionHandler() {}

	@Override
	public Class<RicercaAnagrafiche> getActionType() {
		return RicercaAnagrafiche.class;
	}

	@Override
	public RicercaAnagraficheResult execute(RicercaAnagrafiche ricercaAnagrafiche, ExecutionContext context) throws ActionException {

		ServiceResponse<AnagraficaResponse> serviceResponse = rubricaClient.ricerca(ricercaAnagrafiche.getFiltri(), 0, 0, userSessionUtil.getUtenteSpagic());

		if (!serviceResponse.isError()) {
			List<Anagrafica> anagrafiche = serviceResponse.getResponse().getListaAnagrafiche();
			return new RicercaAnagraficheResult(anagrafiche);

		}

		return new RicercaAnagraficheResult(serviceResponse.getServiceError().getMessage());

	}

	@Override
	public void undo(RicercaAnagrafiche ricercaAnagrafiche, RicercaAnagraficheResult arg1, ExecutionContext arg2) throws ActionException {
		//
	}
}
