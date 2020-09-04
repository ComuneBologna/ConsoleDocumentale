package it.eng.portlet.consolepec.gwt.server.fascicolo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.consolepec.client.GestioneAllegatoClient;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.ModificaTipologieAllegatiAction;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.ModificaTipologieAllegatiResult;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author GiacomoFM
 * @since 11/dic/2018
 */
@Slf4j
@NoArgsConstructor
public class ModificaTipologieAllegatiActionHandler implements ActionHandler<ModificaTipologieAllegatiAction, ModificaTipologieAllegatiResult> {

	@Autowired
	UserSessionUtil userSessionUtil;
	@Autowired
	PraticaSessionUtil praticaSessionUtil;
	@Autowired
	GestioneAllegatoClient gestioneAllegatoClient;

	@Override
	public ModificaTipologieAllegatiResult execute(ModificaTipologieAllegatiAction action, ExecutionContext context) throws ActionException {
		log.debug("Modifica tipologie allegati sul fascicolo [{}]", action.getFascicolo().getNumeroRepertorio());
		try {
			Map<String, List<String>> mappaAllegatoTipologie = new HashMap<>();
			for (AllegatoDTO a : action.getAllegati()) {
				mappaAllegatoTipologie.put(a.getNome(), a.getTipologiaAllegato());
			}

			LockedPratica pratica = gestioneAllegatoClient.modificaTipologieAllegati(userSessionUtil.getUtenteSpagic(), action.getFascicolo().getNumeroRepertorio(), mappaAllegatoTipologie);
			praticaSessionUtil.removePraticaFromEncodedPath(action.getFascicolo().getClientID());
			praticaSessionUtil.loadPraticaInSessione(pratica);

			return new ModificaTipologieAllegatiResult();

		} catch (SpagicClientException e) {
			log.error("Errore", e);
			return new ModificaTipologieAllegatiResult(e.getErrorMessage());

		} catch (Exception e) {
			log.error("Errore imprevisto", e);
			return new ModificaTipologieAllegatiResult(ConsolePecConstants.ERROR_MESSAGE);

		} finally {
			log.debug("Fine modifica tipologie allegati sul fascicolo [{}]", action.getFascicolo().getNumeroRepertorio());
		}
	}

	@Override
	public Class<ModificaTipologieAllegatiAction> getActionType() {
		return ModificaTipologieAllegatiAction.class;
	}

	@Override
	public void undo(ModificaTipologieAllegatiAction action, ModificaTipologieAllegatiResult result, ExecutionContext context) throws ActionException {
		// ~
	}

}
