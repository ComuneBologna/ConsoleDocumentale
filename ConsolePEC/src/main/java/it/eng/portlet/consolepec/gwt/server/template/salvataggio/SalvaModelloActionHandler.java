package it.eng.portlet.consolepec.gwt.server.template.salvataggio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.cobo.consolepec.util.objects.Ref;
import it.eng.consolepec.spagicclient.SpagicClientSaveTemplate;
import it.eng.consolepec.spagicclient.bean.request.template.CreaTemplateEmailRequest;
import it.eng.consolepec.spagicclient.bean.request.template.CreaTemplatePdfRequest;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.xmlplugin.pratica.template.TemplateDocumentoPDF;
import it.eng.consolepec.xmlplugin.pratica.template.TemplateEmail;
import it.eng.portlet.consolepec.gwt.server.XMLPluginToDTOConverter;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.template.salvataggio.SalvaModelloAction;
import it.eng.portlet.consolepec.gwt.shared.action.template.salvataggio.SalvaModelloResult;
import it.eng.portlet.consolepec.gwt.shared.model.BaseTemplateDTO.ModelloVisitor;
import it.eng.portlet.consolepec.gwt.shared.model.TemplateDTO;
import it.eng.portlet.consolepec.gwt.shared.model.TemplatePdfDTO;
import it.eng.portlet.consolepec.spring.bean.converters.requestfascicolo.RequestFascicoloConverter;
import it.eng.portlet.consolepec.spring.bean.profilazione.GestioneProfilazioneUtente;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;
import it.eng.portlet.consolepec.spring.genericdata.IndirizzoEmailCacheSecondoLivello;

public class SalvaModelloActionHandler implements ActionHandler<SalvaModelloAction, SalvaModelloResult> {

	@Autowired
	SpagicClientSaveTemplate spagicClientSaveTemplate;
	@Autowired
	RequestFascicoloConverter requestFascicoloConverter;
	@Autowired
	UserSessionUtil userSessionUtil;
	@Autowired
	XMLPluginToDTOConverter utilPratica;
	@Autowired
	PraticaSessionUtil praticaSessionUtil;
	@Autowired
	XMLPluginToDTOConverter xmlPluginToDTOConverter;
	@Autowired
	IndirizzoEmailCacheSecondoLivello indirizzoEmailCacheSecondoLivello;
	@Autowired
	GestioneProfilazioneUtente gestioneProfilazioneUtente;

	private static final Logger logger = LoggerFactory.getLogger(SalvaModelloActionHandler.class);

	@Override
	public SalvaModelloResult execute(final SalvaModelloAction action, ExecutionContext arg1) throws ActionException {
		logger.info("Start SalvaModelloActionHandler");

		SalvaModelloResult res = new SalvaModelloResult();

		try {
			if (action.getModello() != null && action.getPathModello() != null) {

				final Ref<SalvaModelloResult> ref = Ref.of(res);

				action.getModello().accept(new ModelloVisitor() {

					@Override
					public void visit(TemplateDTO modelloMail) {
						CreaTemplateEmailRequest templateRequest = requestFascicoloConverter.convertTemplateRequest(modelloMail);
						LockedPratica lockedPratica = spagicClientSaveTemplate.saveTemplateMail(action.getPathModello(), templateRequest, userSessionUtil.getUtenteSpagic());
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
						LockedPratica lockedPratica = spagicClientSaveTemplate.saveTemplatePDF(action.getPathModello(), templateRequest, userSessionUtil.getUtenteSpagic());
						TemplateDocumentoPDF templateDocumentoPdf = (TemplateDocumentoPDF) praticaSessionUtil.loadPraticaInSessione(lockedPratica);
						TemplatePdfDTO modello = xmlPluginToDTOConverter.modelloToDettaglio(templateDocumentoPdf);
						praticaSessionUtil.removePraticaFromEncodedPath(modello.getClientID());
						ref.get().setModello(modello);
					}
				});

				res = ref.get();

			} else {
				logger.error("Modello {} o path modello {} non valido", action.getModello(), action.getPathModello());
				return new SalvaModelloResult("Modello non valido");
			}

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			return new SalvaModelloResult(e.getErrorMessage());

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			return new SalvaModelloResult(ConsolePecConstants.ERROR_MESSAGE);
		}

		logger.info("End SalvaModelloActionHandler");
		return res;
	}

	@Override
	public Class<SalvaModelloAction> getActionType() {
		return SalvaModelloAction.class;
	}

	@Override
	public void undo(SalvaModelloAction arg0, SalvaModelloResult arg1, ExecutionContext arg2) throws ActionException {}

}
