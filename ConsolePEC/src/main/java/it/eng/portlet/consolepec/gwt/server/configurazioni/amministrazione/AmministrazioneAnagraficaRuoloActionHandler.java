package it.eng.portlet.consolepec.gwt.server.configurazioni.amministrazione;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;
import com.liferay.portal.model.RoleConstants;
import com.liferay.portal.service.RoleLocalServiceUtil;

import it.eng.cobo.consolepec.commons.configurazioni.AbilitazioniRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.Azione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.Abilitazione;
import it.eng.cobo.consolepec.util.validation.ValidationUtilities;
import it.eng.consolepec.client.AmministrazioneAnagraficaRuoloClient;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.portlet.consolepec.gwt.server.rest.RestClientInvoker;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.configurazioni.amministrazione.AmministrazioneAnagraficaRuoloAction;
import it.eng.portlet.consolepec.gwt.shared.action.configurazioni.amministrazione.AmministrazioneAnagraficaRuoloAction.Operazione;
import it.eng.portlet.consolepec.gwt.shared.action.configurazioni.amministrazione.AmministrazioneAnagraficaRuoloResult;
import it.eng.portlet.consolepec.spring.bean.configurazioni.GestioneConfigurazioni;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

/**
 *
 * @author biagiot
 *
 */
public class AmministrazioneAnagraficaRuoloActionHandler implements ActionHandler<AmministrazioneAnagraficaRuoloAction, AmministrazioneAnagraficaRuoloResult> {

	private static final Logger logger = LoggerFactory.getLogger(AmministrazioneAnagraficaRuoloActionHandler.class);

	@Autowired
	AmministrazioneAnagraficaRuoloClient amministrazioneRuoliClient;

	@Autowired
	GestioneConfigurazioni gestioneConfigurazioni;

	@Autowired
	UserSessionUtil userSessionUtil;

	@Autowired
	RestClientInvoker restClientInvoker;

	@Override
	public AmministrazioneAnagraficaRuoloResult execute(AmministrazioneAnagraficaRuoloAction action, ExecutionContext arg1) throws ActionException {

		AmministrazioneAnagraficaRuoloResult result = null;

		if (action.getOperazione() == null) {
			return new AmministrazioneAnagraficaRuoloResult("Nessuna operazione specificata");
		}

		if (!Operazione.RICERCA.equals(action.getOperazione())
				&& (action.getAnagraficaRuolo() == null || action.getAnagraficaRuolo().getRuolo() == null || action.getAnagraficaRuolo().getRuolo().trim().isEmpty()
						|| action.getAnagraficaRuolo().getEtichetta() == null || action.getAnagraficaRuolo().getEtichetta().trim().isEmpty() || action.getAnagraficaRuolo().getStato() == null)) {

			return new AmministrazioneAnagraficaRuoloResult("Gruppo non valido");
		}

		try {

			logger.info("Inizio operazione {}", action.getOperazione().toString());
			List<String> erroriAbilitazioni = null;

			switch (action.getOperazione()) {
			case INSERIMENTO:
				AbilitazioniRuolo ar = new AbilitazioniRuolo();
				ar.setRuolo(action.getAnagraficaRuolo().getRuolo());
				ar.getAbilitazioni().addAll(action.getAbilitazioni());

				if (!(erroriAbilitazioni = gestioneConfigurazioni.controlloValiditaAbilitazioni(ar)).isEmpty()) {
					result = new AmministrazioneAnagraficaRuoloResult(erroriAbilitazioni.toString());

				} else if (gestioneConfigurazioni.getAnagraficaRuolo(action.getAnagraficaRuolo().getRuolo()) != null) {
					result = new AmministrazioneAnagraficaRuoloResult("Il gruppo ldap " + action.getAnagraficaRuolo().getRuolo() + " esiste già");

				} else if (gestioneConfigurazioni.getAnagraficaRuoloByEtichetta(action.getAnagraficaRuolo().getEtichetta()) != null) {
					result = new AmministrazioneAnagraficaRuoloResult("Il gruppo " + action.getAnagraficaRuolo().getEtichetta() + " esiste già");

				} else if (action.getAnagraficaRuolo().getEmailPredefinita() != null && !action.getAnagraficaRuolo().getEmailPredefinita().isEmpty()
						&& !ValidationUtilities.validateEmailAddress(action.getAnagraficaRuolo().getEmailPredefinita())) {
					result = new AmministrazioneAnagraficaRuoloResult("Indirizzo email non valido");

				} else {

					if (action.getAzioniRuolo() != null) {
						for (Azione a : action.getAzioniRuolo()) {
							a.setData(new Date());
							a.setUsernameUtente(userSessionUtil.getUtenteSpagic().getUsername());
						}

						action.getAnagraficaRuolo().getAzioni().addAll(action.getAzioniRuolo());
					}

					result = new AmministrazioneAnagraficaRuoloResult(amministrazioneRuoliClient.crea(action.getAnagraficaRuolo(), action.getAbilitazioni(), action.getSettore(),
							action.getAzioniAbilitazioni(), userSessionUtil.getUtenteSpagic()));

					try {
						restClientInvoker.refreshAPI();

					} catch (Exception e) {
						logger.error("Errore durante il refresh dei microservizi del documentale", e);
					}

					String ruolo = result.getAnagraficaRuoloResponse().getAnagraficaRuolo().getRuolo();

					try {
						RoleLocalServiceUtil.addRole(userSessionUtil.getUtenteConsolePEC().getUser().getUserId(), null, 0, ruolo, null, null, RoleConstants.TYPE_REGULAR, null);

					} catch (Exception e) {
						logger.error("Errore durante la creazione del ruolo {} su Liferay", ruolo, e);
					}

					gestioneConfigurazioni.reloadRuoli(userSessionUtil.getUtenteSpagic());
				}

				break;

			case MODIFICA:

				AbilitazioniRuolo arM = new AbilitazioniRuolo();
				arM.setRuolo(action.getAnagraficaRuolo().getRuolo());
				arM.getAbilitazioni().addAll(action.getAbilitazioni());
				if (!(erroriAbilitazioni = gestioneConfigurazioni.controlloValiditaAbilitazioni(arM)).isEmpty()) {
					result = new AmministrazioneAnagraficaRuoloResult(erroriAbilitazioni.toString());

				} else if (action.getAnagraficaRuolo().getEmailPredefinita() != null && !action.getAnagraficaRuolo().getEmailPredefinita().isEmpty()
						&& !ValidationUtilities.validateEmailAddress(action.getAnagraficaRuolo().getEmailPredefinita())) {
					result = new AmministrazioneAnagraficaRuoloResult("Indirizzo email non valido");

				} else {

					List<Abilitazione> abilitazioni = null;

					if ((action.getAbilitazioni() != null && !action.getAbilitazioni().isEmpty()) || (action.getAbilitazioniDaRimuovere() != null && !action.getAbilitazioniDaRimuovere().isEmpty())) {

						AbilitazioniRuolo abilitazioniRuolo = gestioneConfigurazioni.getAbilitazioniRuolo(action.getAnagraficaRuolo().getRuolo());

						if (abilitazioniRuolo != null) {

							abilitazioni = new ArrayList<Abilitazione>();

							if (abilitazioniRuolo.getAbilitazioni() != null) {
								abilitazioni = abilitazioniRuolo.getAbilitazioni();
							}

							if (action.getAbilitazioniDaRimuovere() != null) {
								for (Abilitazione a : action.getAbilitazioniDaRimuovere()) {
									if (abilitazioni.contains(a)) {
										abilitazioni.remove(a);
									}
								}
							}

							if (action.getAbilitazioni() != null) {
								for (Abilitazione a : action.getAbilitazioni()) {
									if (!abilitazioni.contains(a)) {
										a.setDataCreazione(new Date());
										a.setUsernameCreazione(userSessionUtil.getUtenteSpagic().getUsername());
										abilitazioni.add(a);
									}
								}
							}

						} else {
							result = new AmministrazioneAnagraficaRuoloResult("Abilitazioni non trovate per il ruolo");
						}

					}

					if (action.getAzioniRuolo() != null) {
						for (Azione a : action.getAzioniRuolo()) {
							a.setData(new Date());
							a.setUsernameUtente(userSessionUtil.getUtenteSpagic().getUsername());
						}

						action.getAnagraficaRuolo().getAzioni().addAll(action.getAzioniRuolo());
					}

					result = new AmministrazioneAnagraficaRuoloResult(
							amministrazioneRuoliClient.modifica(action.getAnagraficaRuolo(), abilitazioni, action.getSettore(), action.getAzioniAbilitazioni(), userSessionUtil.getUtenteSpagic()));

					try {
						restClientInvoker.refreshAPI();

					} catch (Exception e) {
						logger.error("Errore durante il refresh dei microservizi del documentale", e);
					}

					gestioneConfigurazioni.reloadRuoli(userSessionUtil.getUtenteSpagic());
				}

				break;

			case RICERCA:
				result = new AmministrazioneAnagraficaRuoloResult(
						amministrazioneRuoliClient.ricerca(action.getFiltriRicerca(), action.getLimit(), action.getOffset(), action.getOrderBy(), action.getSort(), userSessionUtil.getUtenteSpagic()),
						amministrazioneRuoliClient.count(action.getFiltriRicerca(), userSessionUtil.getUtenteSpagic()));
				break;

			default:
				result = new AmministrazioneAnagraficaRuoloResult(ConsolePecConstants.ERROR_MESSAGE);
				break;
			}

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			result = new AmministrazioneAnagraficaRuoloResult(e.getErrorMessage());

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			result = new AmministrazioneAnagraficaRuoloResult(ConsolePecConstants.ERROR_MESSAGE);

		}

		return result;
	}

	@Override
	public Class<AmministrazioneAnagraficaRuoloAction> getActionType() {
		return AmministrazioneAnagraficaRuoloAction.class;
	}

	@Override
	public void undo(AmministrazioneAnagraficaRuoloAction arg0, AmministrazioneAnagraficaRuoloResult arg1, ExecutionContext arg2) throws ActionException {}

}
