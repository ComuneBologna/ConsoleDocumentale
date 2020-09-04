package it.eng.portlet.consolepec.gwt.server.pec;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.consolepec.spagicclient.SpagicClientRiattivaPratica;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.pratica.email.PraticaEmailIn;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.GestionePECInTask;
import it.eng.portlet.consolepec.gwt.server.XMLPluginToDTOConverter;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.pec.CambiaStatoPecInAction;
import it.eng.portlet.consolepec.gwt.shared.action.pec.CambiaStatoPecInAction.Azione;
import it.eng.portlet.consolepec.gwt.shared.action.pec.CambiaStatoPecInActionResult;
import it.eng.portlet.consolepec.gwt.shared.model.PecInDTO;
import it.eng.portlet.consolepec.gwt.shared.model.TipologiaCaricamento;
import it.eng.portlet.consolepec.spring.bean.gestionepratiche.GestioneTaskPratiche;
import it.eng.portlet.consolepec.spring.bean.profilazione.GestioneProfilazioneUtente;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

public class CambiaStatoPecInActionHandler implements ActionHandler<CambiaStatoPecInAction, CambiaStatoPecInActionResult> {

	@Autowired
	XMLPluginToDTOConverter praticaUtil;
	@Autowired
	PraticaSessionUtil praticaSessionUtil;
	@Autowired
	SpagicClientRiattivaPratica spagicClientRiattivaPratica;
	@Autowired
	UserSessionUtil userSessionUtil;
	@Autowired
	GestioneTaskPratiche gestioneTask;
	@Autowired
	GestioneProfilazioneUtente gestioneProfilazioneUtente;

	Logger logger = LoggerFactory.getLogger(CambiaStatoPecInActionHandler.class);

	public CambiaStatoPecInActionHandler() {}

	@Override
	public CambiaStatoPecInActionResult execute(CambiaStatoPecInAction action, ExecutionContext context) throws ActionException {
		Azione tipoAzione = action.getTipoAzione();

		CambiaStatoPecInActionResult res = new CambiaStatoPecInActionResult();
		Set<String> ids = new HashSet<String>();
		// impostare error true e mess error in caso di errore nella risposta
		try {
			switch (tipoAzione) {
			case ARCHIVIA:
				ids.add(action.getId());
				archivia(ids, res);
				break;
			case ARCHIVIAMASSIVA:
				archivia(action.getIds(), res);
				break;
			case ELIMINA:
				ids.add(action.getId());
				elimina(ids, res);
				break;
			case ELIMINAMASSIVO:
				elimina(action.getIds(), res);
				break;
			case RIPORTAINGESTIONE:
				ids.add(action.getId());
				riportaInGestione(ids, res);
				break;
			case RIPORTAINGESTIONEMASSIVO:
				riportaInGestione(action.getIds(), res);
				break;
			case RILASCIA_IN_CARICO:
				ids.add(action.getId());
				rilasciaInCarico(ids, res);
				break;
			}
		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			res.setIsError(true);
			res.setMessErr(e.getErrorMessage());

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			res.setIsError(true);
			res.setMessErr(ConsolePecConstants.ERROR_MESSAGE);
		}

		return res;
	}

	private void rilasciaInCarico(Set<String> ids, CambiaStatoPecInActionResult res) throws Exception {
		for (final String id : ids) {
			final PraticaEmailIn email = praticaSessionUtil.loadPraticaEmailInFromEncodedPath(id, TipologiaCaricamento.CARICA);
			logger.debug("Eseguo operazione di riporta in gestione su pratica email ID {}", email.getDati().getMessageID());

			email.getDati().setLetto(true);
			final GestionePECInTask task = gestioneTask.estraiTaskCorrente(email, GestionePECInTask.class, gestioneProfilazioneUtente.getDatiUtente().getAnagraficheRuoli());
			if (email.getInCaricoA() != null) {
				task.rilasciaInCarico(userSessionUtil.getUtentePratica());
			}

			praticaSessionUtil.updatePraticaEmail(email);
			PecInDTO dto = praticaUtil.emailToDettaglioIN(email);
			res.getDettagliRighe().add(dto);
		}

	}

	private void riportaInGestione(Set<String> ids, final CambiaStatoPecInActionResult res) throws Exception {
		for (final String id : ids) {
			final PraticaEmailIn email = praticaSessionUtil.loadPraticaEmailInFromEncodedPath(id, TipologiaCaricamento.CARICA);
			logger.debug("Eseguo operazione di riporta in gestione su pratica email ID {}", email.getDati().getMessageID());

			if (!email.isRiattivabile()) {
				throw new PraticaException("La pratica non puo' essere riattivata.");
			}

			it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica riattivaPratica = spagicClientRiattivaPratica.riattivaPratica(email.getAlfrescoPath(), userSessionUtil.getUtenteSpagic());

			PecInDTO dto = praticaUtil.emailToDettaglioIN((PraticaEmailIn) praticaSessionUtil.loadPraticaInSessione(riattivaPratica));
			res.getDettagliRighe().add(dto);
		}
	}

	private void elimina(Set<String> ids, final CambiaStatoPecInActionResult res) throws Exception {
		for (final String id : ids) {
			final PraticaEmailIn email = praticaSessionUtil.loadPraticaEmailInFromEncodedPath(id, TipologiaCaricamento.CARICA);
			logger.debug("Eseguo operazione di elimina su pratica email ID {}", email.getDati().getMessageID());

			email.getDati().setLetto(true);
			final GestionePECInTask task = gestioneTask.estraiTaskCorrente(email, GestionePECInTask.class, gestioneProfilazioneUtente.getDatiUtente().getAnagraficheRuoli());
			if (email.getInCaricoA() != null) {
				task.rilasciaInCarico(userSessionUtil.getUtentePratica());
			}
			task.elimina();
			praticaSessionUtil.updatePraticaEmail(email);
			PecInDTO dto = praticaUtil.emailToDettaglioIN(email);
			res.getDettagliRighe().add(dto);
		}

	}

	private void archivia(Set<String> ids, final CambiaStatoPecInActionResult res) throws Exception {
		for (final String id : ids) {
			final PraticaEmailIn email = praticaSessionUtil.loadPraticaEmailInFromEncodedPath(id, TipologiaCaricamento.CARICA);
			logger.debug("Eseguo operazione di archivia su pratica email ID {}", email.getDati().getMessageID());

			email.getDati().setLetto(true);
			final GestionePECInTask task = gestioneTask.estraiTaskCorrente(email, GestionePECInTask.class, gestioneProfilazioneUtente.getDatiUtente().getAnagraficheRuoli());
			if (email.getInCaricoA() != null) {
				task.rilasciaInCarico(userSessionUtil.getUtentePratica());
			}
			task.archivia();
			praticaSessionUtil.updatePraticaEmail(email);
			res.getDettagliRighe().add(praticaUtil.emailToDettaglioIN(email));
		}
	}

	@Override
	public void undo(CambiaStatoPecInAction action, CambiaStatoPecInActionResult result, ExecutionContext context) throws ActionException {}

	@Override
	public Class<CambiaStatoPecInAction> getActionType() {
		return CambiaStatoPecInAction.class;
	}
}
