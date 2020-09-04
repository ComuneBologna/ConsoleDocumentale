package it.eng.portlet.consolepec.gwt.server.template.creazione;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Strings;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaModello;
import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica.Stato;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.CreazioneModelloAbilitazione;
import it.eng.cobo.consolepec.security.handler.AutorizzazioneHandler.CondizioneAbilitazione;
import it.eng.cobo.consolepec.security.handler.AutorizzazioneHandler.QueryAbilitazione;
import it.eng.cobo.consolepec.util.objects.Ref;
import it.eng.consolepec.spagicclient.SpagicClientCreateTemplate;
import it.eng.consolepec.spagicclient.bean.request.template.CreaTemplateEmailRequest;
import it.eng.consolepec.spagicclient.bean.request.template.CreaTemplatePdfRequest;
import it.eng.consolepec.spagicclient.bean.request.template.CreaTemplatePerCopia;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.xmlplugin.pratica.template.AbstractTemplate;
import it.eng.consolepec.xmlplugin.pratica.template.TemplateDocumentoPDF;
import it.eng.consolepec.xmlplugin.pratica.template.TemplateEmail;
import it.eng.portlet.consolepec.gwt.server.XMLPluginToDTOConverter;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.template.creazione.CreaModelloAction;
import it.eng.portlet.consolepec.gwt.shared.action.template.creazione.CreaModelloResult;
import it.eng.portlet.consolepec.gwt.shared.model.BaseTemplateDTO.ModelloVisitor;
import it.eng.portlet.consolepec.gwt.shared.model.TemplateDTO;
import it.eng.portlet.consolepec.gwt.shared.model.TemplatePdfDTO;
import it.eng.portlet.consolepec.spring.bean.configurazioni.GestioneConfigurazioni;
import it.eng.portlet.consolepec.spring.bean.converters.requestfascicolo.RequestFascicoloConverter;
import it.eng.portlet.consolepec.spring.bean.profilazione.GestioneProfilazioneUtente;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;
import it.eng.portlet.consolepec.spring.genericdata.IndirizzoEmailCacheSecondoLivello;

public class CreaModelloActionHandler implements ActionHandler<CreaModelloAction, CreaModelloResult> {

	private static final Logger logger = LoggerFactory.getLogger(CreaModelloActionHandler.class);

	@Autowired
	SpagicClientCreateTemplate spagicClientCreateTemplate;
	@Autowired
	UserSessionUtil userSessionUtil;
	@Autowired
	GestioneProfilazioneUtente gestioneProfilazioneUtente;
	@Autowired
	RequestFascicoloConverter requestFascicoloConverter;
	@Autowired
	PraticaSessionUtil praticaSessionUtil;
	@Autowired
	XMLPluginToDTOConverter xmlPluginToDTOConverter;
	@Autowired
	IndirizzoEmailCacheSecondoLivello indirizzoEmailCacheSecondoLivello;
	@Autowired
	GestioneConfigurazioni gestioneConfigurazioni;

	@Override
	public CreaModelloResult execute(CreaModelloAction action, ExecutionContext arg1) throws ActionException {
		logger.info("Start CreaModelloActionHandler");

		CreaModelloResult res = new CreaModelloResult();

		try {

			if (!Strings.isNullOrEmpty(action.getIdDocumentale())) {
				logger.info("Creazione per copia del modello {}", action.getIdDocumentale());

				CreaTemplatePerCopia request = new CreaTemplatePerCopia();
				request.setIdDocumentale(action.getIdDocumentale());
				LockedPratica lp = spagicClientCreateTemplate.createTemplatePerCopia(request, userSessionUtil.getUtenteSpagic());
				AbstractTemplate<?> t = (AbstractTemplate<?>) praticaSessionUtil.loadPraticaInSessione(lp);
				res.setModello(xmlPluginToDTOConverter.modelloToDettaglio(t));

			} else if (action.getModello() != null) {

				final AnagraficaModello af = gestioneConfigurazioni.getAnagraficaModello(action.getModello().getTipologiaPratica().getNomeTipologia());
				if (af == null || af.getStato().equals(Stato.DISATTIVA)) {
					return new CreaModelloResult("Modello non attivo");
				}

				QueryAbilitazione<CreazioneModelloAbilitazione> qb = new QueryAbilitazione<CreazioneModelloAbilitazione>();
				qb.addCondition(new CondizioneAbilitazione<CreazioneModelloAbilitazione>() {

					@Override
					protected boolean valutaCondizione(CreazioneModelloAbilitazione abilitazione) {
						return abilitazione.getTipo().equals(af.getNomeTipologia());
					}
				});

				if (!gestioneProfilazioneUtente.getAutorizzazioniUtente().isAbilitato(CreazioneModelloAbilitazione.class, qb)) {
					return new CreaModelloResult("Utente non abilitato alla creazione del modello");
				}

				final Ref<CreaModelloResult> ref = Ref.of(res);

				action.getModello().accept(new ModelloVisitor() {

					@Override
					public void visit(TemplateDTO modelloMail) {
						CreaTemplateEmailRequest templateRequest = requestFascicoloConverter.convertTemplateRequest(modelloMail);
						LockedPratica lockedPratica = spagicClientCreateTemplate.createTemplate(templateRequest, userSessionUtil.getUtenteSpagic());
						TemplateEmail templateEmail = (TemplateEmail) praticaSessionUtil.loadPraticaInSessione(lockedPratica);
						TemplateDTO template = xmlPluginToDTOConverter.modelloToDettaglio(templateEmail);

						praticaSessionUtil.removePraticaFromEncodedPath(template.getClientID());
						ref.get().setModello(template);

						try {
							if (modelloMail.getDestinatari() != null) {
								for (String indirizzoEmail : modelloMail.getDestinatari()) {
									indirizzoEmailCacheSecondoLivello.checkIndirizzoEmail(indirizzoEmail, gestioneProfilazioneUtente.getDatiUtente());
								}
							}

							if (modelloMail.getDestinatariCC() != null) {
								for (String indirizzoEmail : modelloMail.getDestinatariCC()) {
									indirizzoEmailCacheSecondoLivello.checkIndirizzoEmail(indirizzoEmail, gestioneProfilazioneUtente.getDatiUtente());
								}
							}

						} catch (Exception e) {
							logger.warn("Errore durante il salvataggio degli indirizzi email", e);
						}
					}

					@Override
					public void visit(TemplatePdfDTO modelloPdf) {
						CreaTemplatePdfRequest templateRequest = requestFascicoloConverter.convertTemplateRequest(modelloPdf);
						LockedPratica lockedPratica = spagicClientCreateTemplate.createTemplate(templateRequest, userSessionUtil.getUtenteSpagic());
						TemplateDocumentoPDF templateDocumentoPdf = (TemplateDocumentoPDF) praticaSessionUtil.loadPraticaInSessione(lockedPratica);
						TemplatePdfDTO modello = xmlPluginToDTOConverter.modelloToDettaglio(templateDocumentoPdf);
						praticaSessionUtil.removePraticaFromEncodedPath(modello.getClientID());
						ref.get().setModello(modello);
					}
				});

				res = ref.get();

			} else {
				logger.error("Azione non valida: modello {} non valido oppure idDocumentale per copia non specificato: {}", action.getModello(), action.getIdDocumentale());
				return new CreaModelloResult("Modello non valido");
			}

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			return new CreaModelloResult(e.getErrorMessage());

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			return new CreaModelloResult(ConsolePecConstants.ERROR_MESSAGE);

		}

		logger.info("End CreaModelloActionHandler");
		return res;
	}

	@Override
	public Class<CreaModelloAction> getActionType() {
		return CreaModelloAction.class;
	}

	@Override
	public void undo(CreaModelloAction arg0, CreaModelloResult arg1, ExecutionContext arg2) throws ActionException {}

}
