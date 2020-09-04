package it.eng.portlet.consolepec.gwt.server.cartellafirma;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Strings;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.portlet.consolepec.gwt.server.XMLPluginToDTOConverter;
import it.eng.portlet.consolepec.gwt.server.XMLPluginToDTOConverterUtil;
import it.eng.portlet.consolepec.gwt.shared.Base64Utils;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.cartellafirma.FineWizardTaskFirmaAction;
import it.eng.portlet.consolepec.gwt.shared.action.cartellafirma.FineWizardTaskFirmaResult;
import it.eng.portlet.consolepec.gwt.shared.action.cartellafirma.InformazioniFirmaDigitaleTaskFirmaDTO;
import it.eng.portlet.consolepec.gwt.shared.action.cartellafirma.InformazioniNotificaTaskFirmaDTO;
import it.eng.portlet.consolepec.gwt.shared.action.cartellafirma.InformazioniRiassegnazioneFascicoliTaskFirmaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FileDTO;
import it.eng.portlet.consolepec.spring.bean.cartellafirma.CartellaFirmaWizardUtil;
import it.eng.portlet.consolepec.spring.bean.configurazioni.GestioneConfigurazioni;
import it.eng.portlet.consolepec.spring.bean.profilazione.GestioneProfilazioneUtente;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;
import it.eng.portlet.consolepec.spring.firma.impl.CredenzialiFirma;
import it.eng.portlet.consolepec.spring.genericdata.IndirizzoEmailCacheSecondoLivello;

/**
 *
 * @author biagiot
 *
 */
public class FineWizardTaskFirmaActionHandler implements ActionHandler<FineWizardTaskFirmaAction, FineWizardTaskFirmaResult> {

	private final Logger logger = LoggerFactory.getLogger(FineWizardTaskFirmaActionHandler.class);

	@Autowired CartellaFirmaWizardUtil cartellaFirmaWizardUtil;
	@Autowired XMLPluginToDTOConverter xmlPluginToDTOConverter;
	@Autowired XMLPluginToDTOConverterUtil xmlPluginToDTOConverterUtil;
	@Autowired PraticaSessionUtil praticaSessionUtil;
	@Autowired IndirizzoEmailCacheSecondoLivello indirizzoEmailCacheSecondoLivello;
	@Autowired GestioneProfilazioneUtente gestioneProfilazioneUtente;
	@Autowired GestioneConfigurazioni gestioneConfigurazioni;

	public FineWizardTaskFirmaActionHandler() {

	}

	@Override
	public FineWizardTaskFirmaResult execute(FineWizardTaskFirmaAction action, ExecutionContext context) throws ActionException {
		logger.info("Start FineWizardTaskFirmaActionHandler");

		FineWizardTaskFirmaResult result = null;
		boolean riassegna = false;
		InformazioniNotificaTaskFirmaDTO infoNotifica = action.getInfoNotifica();

		try {

			if (infoNotifica != null) {

				if (infoNotifica instanceof InformazioniRiassegnazioneFascicoliTaskFirmaDTO) {
					riassegna = true;
				}

				for (String mail : infoNotifica.getIndirizziNotifica()) {
					try {
						indirizzoEmailCacheSecondoLivello.checkIndirizzoEmail(mail, gestioneProfilazioneUtente.getDatiUtente());

					} catch (Exception e) {
						logger.warn("Errore durante il salvataggio degli indirizzi email", e);
					}
				}
			}

		} catch (Exception e) {
			logger.error("Salvataggio dati di riassegnazione non avvenuto", e);
		}

		try {
			List<LockedPratica> lockedPratiche = null;
			Map<String, List<String>> mapPraticaNomiAllegati = createMapPathNomiAllegati(action);

			String ruolo = null;
			if (!Strings.isNullOrEmpty(action.getRuolo())) {
				ruolo = gestioneConfigurazioni.getAnagraficaRuoloByEtichetta(action.getRuolo()).getRuolo();
			}

			List<FileDTO> fileDaAllegare = action.getFileDaAllegare();

			String motivazione = action.getMotivazione();

			switch (action.getOperazioneWizard()) {
			case FIRMA:
				CredenzialiFirma cf = translateCredentials(action.getInfoFirmaDigitale());
				lockedPratiche = cartellaFirmaWizardUtil.firmaDocumenti(mapPraticaNomiAllegati, cf, action.getInfoFirmaDigitale().getTipoFirma(), riassegna, infoNotifica, ruolo, fileDaAllegare,
						motivazione);
				break;

			case VISTO:
				lockedPratiche = cartellaFirmaWizardUtil.vistoDocumenti(mapPraticaNomiAllegati, riassegna, infoNotifica, ruolo, fileDaAllegare, motivazione);
				break;

			case DINIEGO:
				lockedPratiche = cartellaFirmaWizardUtil.diniegoDocumenti(mapPraticaNomiAllegati, riassegna, infoNotifica, ruolo, fileDaAllegare, motivazione);
				break;

			case RITIRO:
				lockedPratiche = cartellaFirmaWizardUtil.ritiroDocumenti(mapPraticaNomiAllegati, infoNotifica, motivazione);
				break;

			case RISPOSTA_PARERE:
				lockedPratiche = cartellaFirmaWizardUtil.rispondiDocumenti(action.getTipoRisposta(), mapPraticaNomiAllegati, riassegna, infoNotifica, ruolo, fileDaAllegare, motivazione);
				break;

			case EVADI:

				Map<String, List<Integer>> praticaIdTask = new HashMap<String, List<Integer>>();

				if (action.getPraticaIdTaskMap() != null) {
					for (Entry<String, List<Integer>> entry : action.getPraticaIdTaskMap().entrySet()) {
						praticaIdTask.put(Base64Utils.URLdecodeAlfrescoPath(entry.getKey()), entry.getValue());
					}
				}

				lockedPratiche = cartellaFirmaWizardUtil.evadiDocumenti(praticaIdTask);
				break;
			}

			List<FascicoloDTO> fascicoli = new ArrayList<FascicoloDTO>();
			for (LockedPratica lockedPratica : lockedPratiche) {
				Fascicolo fascicolo = (Fascicolo) praticaSessionUtil.loadPraticaInSessione(lockedPratica);
				fascicoli.add(xmlPluginToDTOConverter.fascicoloToDettaglio(fascicolo));
			}

			result = new FineWizardTaskFirmaResult(fascicoli);

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			result = new FineWizardTaskFirmaResult(e.getErrorMessage());

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			result = new FineWizardTaskFirmaResult(ConsolePecConstants.ERROR_MESSAGE);

		}

		logger.info("End FineWizardTaskFirmaActionHandler");
		return result;
	}

	@Override
	public Class<FineWizardTaskFirmaAction> getActionType() {
		return FineWizardTaskFirmaAction.class;
	}

	@Override
	public void undo(FineWizardTaskFirmaAction arg0, FineWizardTaskFirmaResult arg1, ExecutionContext arg2) throws ActionException {}

	private CredenzialiFirma translateCredentials(InformazioniFirmaDigitaleTaskFirmaDTO infoFirmaDigitale) {
		CredenzialiFirma cf = new CredenzialiFirma(infoFirmaDigitale.getCredenzialiFirma().isCredenzialeUsernameModificata() ? infoFirmaDigitale.getCredenzialiFirma().getUsername() : null,
				infoFirmaDigitale.getCredenzialiFirma().isCredenzialePasswordModificata() ? infoFirmaDigitale.getCredenzialiFirma().getPassword() : null,
				infoFirmaDigitale.getCredenzialiFirma().getOtp(), infoFirmaDigitale.getCredenzialiFirma().isSalvaCredenziali());
		return cf;
	}

	private Map<String, List<String>> createMapPathNomiAllegati(FineWizardTaskFirmaAction action) {
		Map<String, List<String>> result = new HashMap<String, List<String>>();

		for (Entry<String, List<AllegatoDTO>> entry : action.getPraticaAllegatiMap().entrySet()) {
			List<String> allegatiNomi = xmlPluginToDTOConverterUtil.getAllegatiDaFirmare(entry.getValue().toArray(new AllegatoDTO[entry.getValue().size()]));
			String path = Base64Utils.URLdecodeAlfrescoPath(entry.getKey());
			result.put(path, allegatiNomi);
		}

		return result;
	}
}
