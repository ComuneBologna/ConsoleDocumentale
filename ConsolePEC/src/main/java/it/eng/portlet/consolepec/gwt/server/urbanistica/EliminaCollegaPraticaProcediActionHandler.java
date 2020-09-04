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
import it.eng.portlet.consolepec.gwt.shared.action.urbanistica.EliminaCollegaPraticaProcediAction;
import it.eng.portlet.consolepec.gwt.shared.action.urbanistica.EliminaCollegaPraticaProcediResult;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author GiacomoFM
 * @since 08/nov/2017
 */
@Slf4j
public class EliminaCollegaPraticaProcediActionHandler implements ActionHandler<EliminaCollegaPraticaProcediAction, EliminaCollegaPraticaProcediResult> {

	@Autowired
	private UserSessionUtil userSessionUtil;
	@Autowired
	private PraticaSessionUtil praticaSessionUtil;
	@Autowired
	private XMLPluginToDTOConverter util;

	@Autowired
	private CollegaPraticaProcediClient collegaPraticaProcediClient;

	@Override
	public EliminaCollegaPraticaProcediResult execute(EliminaCollegaPraticaProcediAction action, ExecutionContext arg1) throws ActionException {
		try {
			Pratica<?> pratica = praticaSessionUtil.loadPraticaFromEncodedPath(action.getEncodedPath());
			LockedPratica praticaLock = collegaPraticaProcediClient.eliminaCollega(pratica.getAlfrescoPath(), action.getPraticheProcedi(), userSessionUtil.getUtenteSpagic());

			praticaSessionUtil.removePraticaFromEncodedPath(action.getEncodedPath());
			pratica = praticaSessionUtil.loadPraticaInSessione(praticaLock);

			PraticaDTO praticaDTO = util.praticaToDTO(pratica);
			return new EliminaCollegaPraticaProcediResult(praticaDTO);

		} catch (SpagicClientException e) {
			log.error("Errore", e);
			return new EliminaCollegaPraticaProcediResult(e.getErrorMessage());

		} catch (Exception e) {
			log.error("Errore imprevisto", e);
			return new EliminaCollegaPraticaProcediResult(ConsolePecConstants.ERROR_MESSAGE);
		}

	}

	@Override
	public Class<EliminaCollegaPraticaProcediAction> getActionType() {
		return EliminaCollegaPraticaProcediAction.class;
	}

	@Override
	public void undo(EliminaCollegaPraticaProcediAction arg0, EliminaCollegaPraticaProcediResult arg1, ExecutionContext arg2) throws ActionException {
		//
	}

}
