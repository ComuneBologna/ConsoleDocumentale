package it.eng.portlet.consolepec.gwt.server.fascicolo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.consolepec.spagicclient.SpagicClientModificaFascicolo;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.xmlplugin.factory.Pratica;
import it.eng.portlet.consolepec.gwt.server.XMLPluginToDTOConverter;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.ModificaFascicoloAction;
import it.eng.portlet.consolepec.gwt.shared.action.ModificaFascicoloResult;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;
import lombok.NoArgsConstructor;

/**
 * @author GiacomoFM
 * @since 13/lug/2017
 */
@NoArgsConstructor
public class ModificaFascicoloActionHandler implements ActionHandler<ModificaFascicoloAction, ModificaFascicoloResult> {

	private static final Logger logger = LoggerFactory.getLogger(ModificaFascicoloActionHandler.class);

	@Autowired
	private SpagicClientModificaFascicolo spagicClientModificaFascicolo;
	@Autowired
	private PraticaSessionUtil praticaSessionUtil;
	@Autowired
	private UserSessionUtil userSessionUtil;
	@Autowired
	private XMLPluginToDTOConverter util;

	@Override
	public ModificaFascicoloResult execute(ModificaFascicoloAction action, ExecutionContext context) throws ActionException {
		try {
			Pratica<?> pratica = praticaSessionUtil.loadPraticaFromEncodedPath(action.getPratica().getClientID());
			LockedPratica praticaLock = spagicClientModificaFascicolo.modifica(pratica.getAlfrescoPath(), action.getTitolo(), action.getTipoFascicolo(), action.getDatiAggiuntivi(),
					userSessionUtil.getUtenteSpagic());

			praticaSessionUtil.removePraticaFromEncodedPath(action.getPratica().getClientID());
			pratica = praticaSessionUtil.loadPraticaInSessione(praticaLock);

			PraticaDTO praticaDTO = util.praticaToDTO(pratica);
			return new ModificaFascicoloResult(praticaDTO);

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			return new ModificaFascicoloResult(e.getErrorMessage());

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			return new ModificaFascicoloResult(ConsolePecConstants.ERROR_MESSAGE);

		}
	}

	@Override
	public Class<ModificaFascicoloAction> getActionType() {
		return ModificaFascicoloAction.class;
	}

	@Override
	public void undo(ModificaFascicoloAction arg0, ModificaFascicoloResult arg1, ExecutionContext arg2) throws ActionException {
		//
	}

}
