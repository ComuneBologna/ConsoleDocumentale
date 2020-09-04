package it.eng.portlet.consolepec.spring.bean.gestionepratiche.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import it.eng.consolepec.client.CollegamentoPraticheClient;
import it.eng.consolepec.spagicclient.SpagicClientIperFascicoloHandler;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.portlet.consolepec.gwt.server.fascicolo.CollegamentoFascicoliActionHandler;
import it.eng.portlet.consolepec.gwt.shared.Base64Utils;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CollegamentoFascicoli;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CollegamentoFascicoliResult;
import it.eng.portlet.consolepec.spring.bean.gestionepratiche.GestioneCollegamentiFascicolo;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

public class GestioneCollegamentiFascicoloImpl implements GestioneCollegamentiFascicolo {

	@Autowired
	SpagicClientIperFascicoloHandler spagicClientIperFascicoloHandler;
	@Autowired
	UserSessionUtil userSessionUtil;
	@Autowired
	PraticaSessionUtil praticaSessionUtil;

	@Autowired
	CollegamentoPraticheClient collegamentoPraticheClient;

	private final Logger logger = LoggerFactory.getLogger(CollegamentoFascicoliActionHandler.class);

	@Override
	public CollegamentoFascicoliResult gestioneCollegamentoFascicolo(CollegamentoFascicoli collegamentoFascicoli) {
		logger.debug("CollegamentoFascicoliResult {}");
		CollegamentoFascicoliResult actionResult = new CollegamentoFascicoliResult();
		try {
			String fascioloLocale = Base64Utils.URLdecodeAlfrescoPath(collegamentoFascicoli.getPathFascicoloLocale());
			String fascioloRemoto = Base64Utils.URLdecodeAlfrescoPath(collegamentoFascicoli.getPathFascicoloRemoto());
			LockedPratica lockedPratica = null;
			if (CollegamentoFascicoli.LINK_MERGE.equalsIgnoreCase(collegamentoFascicoli.getLink())) {

				if (!collegamentoFascicoli.getPermessi().isRemotoAccessibileInLettura() && !collegamentoFascicoli.getPermessi().isSorgenteAccessibileInLettura()) {
					throw new Exception("Non è possibile inibire l'accesso in lettura in entrambi i versi");
				}

				if (collegamentoFascicoli.getRuolo() != null) {
					lockedPratica = collegamentoPraticheClient.updateCollegamentoGruppo(fascioloLocale, fascioloRemoto, collegamentoFascicoli.getPermessi(), collegamentoFascicoli.getRuolo(),
							userSessionUtil.getUtenteSpagic());

				} else {
					lockedPratica = collegamentoPraticheClient.updateCollegamento(fascioloLocale, fascioloRemoto, collegamentoFascicoli.getPermessi(), userSessionUtil.getUtenteSpagic());

				}

			} else if (CollegamentoFascicoli.LINK_DELETE.equalsIgnoreCase(collegamentoFascicoli.getLink())) {
				lockedPratica = spagicClientIperFascicoloHandler.eliminaCollegamento(fascioloLocale, fascioloRemoto, userSessionUtil.getUtenteSpagic());

			} else {
				throw new Exception("Operazione di collegamento sconosciuta: " + collegamentoFascicoli.getLink());
			}

			praticaSessionUtil.removePraticaFromEncodedPath(collegamentoFascicoli.getPathFascicoloLocale());
			praticaSessionUtil.removePraticaFromEncodedPath(collegamentoFascicoli.getPathFascicoloRemoto());
			Fascicolo fascicolo = (Fascicolo) praticaSessionUtil.loadPraticaInSessione(lockedPratica);
			logger.debug("Effettuata modifica collegamento fascicoli {}", fascicolo.getDati());
		} catch (SpagicClientException e) {
			logger.error(e.getMessage(), e);
			actionResult.setError(true);
			actionResult.setErrorMsg(e.getErrorMessage());
		} catch (Exception e) {
			logger.error("Errore nel collegamento dei fascicoli: {}", e);
			actionResult.setError(true);
			actionResult.setErrorMsg(ConsolePecConstants.ERROR_MESSAGE);
		} finally {
			logger.debug("Fine");
		}
		return actionResult;
	}

}
