package it.eng.portlet.consolepec.gwt.server.sara;

import it.eng.cobo.consolepec.commons.services.ServiceResponse;
import it.eng.consolepec.client.EmissionePermessoClient;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.portlet.consolepec.gwt.server.XMLPluginToDTOConverter;
import it.eng.portlet.consolepec.gwt.shared.Base64Utils;
import it.eng.portlet.consolepec.gwt.shared.action.sara.EmissionePermessoAction;
import it.eng.portlet.consolepec.gwt.shared.action.sara.EmissionePermessoResult;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class EmissionePermessoActionHandler implements ActionHandler<EmissionePermessoAction, EmissionePermessoResult> {

	@Autowired private EmissionePermessoClient emissionePermessoClient;
	@Autowired private UserSessionUtil userSessionUtil;
	@Autowired private PraticaSessionUtil praticaSessionUtil;
	@Autowired private XMLPluginToDTOConverter util;


	@Override
	public EmissionePermessoResult execute(EmissionePermessoAction action, ExecutionContext arg1) throws ActionException {
		
		ServiceResponse<LockedPratica> rpp = emissionePermessoClient.emettiPermesso(userSessionUtil.getUtenteSpagic(), Base64Utils.URLdecodeAlfrescoPath(action.getIdDocumentale()));
		if (rpp.isError()) {
			return new EmissionePermessoResult(rpp.getServiceError().getMessage());
		}
		
		LockedPratica lockedPratica = rpp.getResponse();
		Fascicolo fascicolo = (Fascicolo) praticaSessionUtil.loadPraticaInSessione(lockedPratica);
		FascicoloDTO fascicoloDTO = util.fascicoloToDettaglio(fascicolo);
		
		return new EmissionePermessoResult(fascicoloDTO);
	}

	@Override
	public Class<EmissionePermessoAction> getActionType() {
		return EmissionePermessoAction.class;
	}

	@Override
	public void undo(EmissionePermessoAction arg0, EmissionePermessoResult arg1, ExecutionContext arg2) throws ActionException {
		//
	}

}