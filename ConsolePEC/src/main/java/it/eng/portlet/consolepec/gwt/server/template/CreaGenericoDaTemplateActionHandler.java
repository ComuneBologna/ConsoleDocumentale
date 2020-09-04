package it.eng.portlet.consolepec.gwt.server.template;

import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.portlet.consolepec.gwt.server.XMLPluginToDTOConverter;
import it.eng.portlet.consolepec.gwt.shared.Base64Utils;
import it.eng.portlet.consolepec.gwt.shared.action.template.CreaGenericoDaTemplate;
import it.eng.portlet.consolepec.gwt.shared.action.template.CreaGenericoDaTemplateResult;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;

public abstract class CreaGenericoDaTemplateActionHandler<A extends CreaGenericoDaTemplate<R>, R extends CreaGenericoDaTemplateResult> implements ActionHandler<A, R> {

	@Autowired
	PraticaSessionUtil praticaSessionUtil;

	@Autowired
	XMLPluginToDTOConverter utilPratica;

	public CreaGenericoDaTemplateActionHandler() {}

	@Override
	public R execute(A action, ExecutionContext context) throws ActionException {

		return getResult(action);
	}

	protected abstract R getResult(A action) throws ActionException;

	protected String decodePathFascicolo(A action) {
		return Base64Utils.URLdecodeAlfrescoPath(action.getPathFascicolo());
	}

	protected String decodePathTemplate(A action) {
		return Base64Utils.URLdecodeAlfrescoPath(action.getPathTemplate());
	}

	protected FascicoloDTO ricaricaFascicolo(LockedPratica lockedPratica) {
		Fascicolo fascicolo = (Fascicolo) praticaSessionUtil.loadPraticaInSessione(lockedPratica);
		FascicoloDTO fascicoloDTO = utilPratica.fascicoloToDettaglio(fascicolo);
		return fascicoloDTO;
	}
}
