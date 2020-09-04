package it.eng.portlet.consolepec.gwt.server.urbanistica;

import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.consolepec.client.CollegaPraticaProcediClient;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.xmlplugin.factory.Pratica;
import it.eng.portlet.consolepec.gwt.server.XMLPluginToDTOConverter;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.urbanistica.CollegaPraticaProcediAction;
import it.eng.portlet.consolepec.gwt.shared.action.urbanistica.CollegaPraticaProcediResult;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author GiacomoFM
 * @since 08/nov/2017
 */
@Slf4j
public class CollegaPraticaProcediActionHandler implements ActionHandler<CollegaPraticaProcediAction, CollegaPraticaProcediResult> {

	@Autowired
	private UserSessionUtil userSessionUtil;
	@Autowired
	private PraticaSessionUtil praticaSessionUtil;
	@Autowired
	private XMLPluginToDTOConverter util;

	@Autowired
	private CollegaPraticaProcediClient collegaPraticaProcediClient;

	@Override
	public CollegaPraticaProcediResult execute(CollegaPraticaProcediAction action, ExecutionContext arg1) throws ActionException {
		try {
			Pratica<?> pratica = praticaSessionUtil.loadPraticaFromEncodedPath(action.getEncodedPath());
			LockedPratica praticaLock = collegaPraticaProcediClient.collega(pratica.getAlfrescoPath(), action.getPraticaProcedi(), userSessionUtil.getUtenteSpagic());

			praticaSessionUtil.removePraticaFromEncodedPath(action.getEncodedPath());
			pratica = praticaSessionUtil.loadPraticaInSessione(praticaLock);

			PraticaDTO praticaDTO = util.praticaToDTO(pratica);
			return new CollegaPraticaProcediResult(praticaDTO);

		} catch (SpagicClientException e) {
			log.error("Errore", e);
			return new CollegaPraticaProcediResult(e.getErrorMessage());

		} catch (Exception e) {
			log.error("Errore imprevisto", e);
			return new CollegaPraticaProcediResult(ConsolePecConstants.ERROR_MESSAGE);

		}
	}

	@Override
	public Class<CollegaPraticaProcediAction> getActionType() {
		return CollegaPraticaProcediAction.class;
	}

	@Override
	public void undo(CollegaPraticaProcediAction arg0, CollegaPraticaProcediResult arg1, ExecutionContext arg2) throws ActionException {
		//
	}

}
