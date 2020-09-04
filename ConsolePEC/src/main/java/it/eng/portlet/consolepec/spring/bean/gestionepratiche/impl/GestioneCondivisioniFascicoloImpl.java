package it.eng.portlet.consolepec.spring.bean.gestionepratiche.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.consolepec.spagicclient.SpagicClientIperFascicoloHandler;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.portlet.consolepec.gwt.shared.Base64Utils;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CondivisioneFascicolo;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CondivisioneFascicoloResult;
import it.eng.portlet.consolepec.spring.bean.configurazioni.GestioneConfigurazioni;
import it.eng.portlet.consolepec.spring.bean.gestionepratiche.GestioneCondivisioniFascicolo;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

public class GestioneCondivisioniFascicoloImpl implements GestioneCondivisioniFascicolo {

	@Autowired
	SpagicClientIperFascicoloHandler spagicClientIperFascicoloHandler;
	@Autowired
	UserSessionUtil userSessionUtil;
	@Autowired
	PraticaSessionUtil praticaSessionUtil;
	@Autowired
	GestioneConfigurazioni gestioneConfigurazioni;

	private final Logger logger = LoggerFactory.getLogger(GestioneCondivisioniFascicoloImpl.class);

	@Override
	public CondivisioneFascicoloResult gestioneCondivisioneFascicolo(CondivisioneFascicolo condivisioneFascicolo) {
		logger.debug("CondivisioneFascicoliResult {}");
		CondivisioneFascicoloResult actionResult = new CondivisioneFascicoloResult();
		try {
			String nomeGruppo = null;
			String displayGruppo = condivisioneFascicolo.getNomeGruppo();
			List<AnagraficaRuolo> ruoli = gestioneConfigurazioni.getAnagraficheRuoli();
			for (AnagraficaRuolo ruolo : ruoli) {
				if (ruolo.getEtichetta().equalsIgnoreCase(displayGruppo)) {
					nomeGruppo = ruolo.getRuolo();
				}
			}

			if (nomeGruppo == null) {
				throw new Exception("Gruppo sconosciuto: " + displayGruppo);
			}

			String pathFascicolo = Base64Utils.URLdecodeAlfrescoPath(condivisioneFascicolo.getPathFascicolo());
			LockedPratica lockedPratica = null;
			if (CondivisioneFascicolo.SHARE_MERGE.equalsIgnoreCase(condivisioneFascicolo.getShare())) {
				lockedPratica = spagicClientIperFascicoloHandler.mergeCondivisione(nomeGruppo, pathFascicolo, condivisioneFascicolo.getOperazioniCondivise(), userSessionUtil.getUtenteSpagic());
			} else if (CondivisioneFascicolo.SHARE_DELETE.equalsIgnoreCase(condivisioneFascicolo.getShare())) {
				lockedPratica = spagicClientIperFascicoloHandler.eliminaCondivisione(nomeGruppo, pathFascicolo, userSessionUtil.getUtenteSpagic());
			} else {
				throw new Exception("Operazione di condivisione sconosciuta: " + condivisioneFascicolo.getShare());
			}
			Fascicolo fascicolo = (Fascicolo) praticaSessionUtil.loadPraticaInSessione(lockedPratica);
			logger.debug("Effettuata modifica condivisione fascicoli {}", fascicolo.getDati());
		} catch (SpagicClientException e) {
			logger.error(e.getMessage(), e);
			actionResult.setError(true);
			actionResult.setErrorMsg(e.getErrorMessage());
		} catch (Exception e) {
			logger.error("Errore nella condivisione dei fascicoli: {}", e);
			actionResult.setError(true);
			actionResult.setErrorMsg(ConsolePecConstants.ERROR_MESSAGE);
		} finally {
			logger.debug("Fine");
		}
		return actionResult;
	}

}
