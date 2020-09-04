package it.eng.portlet.consolepec.gwt.client.operazioni.template.impl;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.proxy.PlaceManager;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.ModelloLoaded;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaFascicoloLoaded;
import it.eng.portlet.consolepec.gwt.client.event.GoToPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.operazioni.template.TemplateSceltaWizardApiClient;
import it.eng.portlet.consolepec.gwt.client.place.NameTokens;
import it.eng.portlet.consolepec.gwt.client.place.NameTokensParams;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter.Place;
import it.eng.portlet.consolepec.gwt.client.presenter.template.event.GoToInserimentoCampiTemplateEmailEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.template.event.GoToInserimentoCampiTemplatePdfEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.template.event.MostraSceltaTemplateEvent;
import it.eng.portlet.consolepec.gwt.client.widget.SitemapMenu;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.template.CreaBozzaDaTemplateAction;
import it.eng.portlet.consolepec.gwt.shared.action.template.CreaBozzaDaTemplateActionResult;
import it.eng.portlet.consolepec.gwt.shared.action.template.CreaPdfDaTemplateAction;
import it.eng.portlet.consolepec.gwt.shared.action.template.CreaPdfDaTemplateActionResult;
import it.eng.portlet.consolepec.gwt.shared.model.BaseTemplateDTO;
import it.eng.portlet.consolepec.gwt.shared.model.BaseTemplateDTO.ModelloVisitor;
import it.eng.portlet.consolepec.gwt.shared.model.CampoTemplateDTO;
import it.eng.portlet.consolepec.gwt.shared.model.CampoTemplateDTO.TipoCampoTemplateDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.TemplateDTO;
import it.eng.portlet.consolepec.gwt.shared.model.TemplatePdfDTO;

/**
 *
 * @author biagiot
 *
 */
public class TemplateSceltaWizardApiClientImpl implements TemplateSceltaWizardApiClient, HasHandlers {

	private final DispatchAsync dispatcher;
	private final EventBus eventBus;
	private final PecInPraticheDB pecInPraticheDB;
	private final SitemapMenu sitemapMenu;
	private final PlaceManager placeManager;

	private FascicoloDTO fascicoloDTO;
	private TipologiaPratica tipo;
	private BaseTemplateDTO template;

	@Inject
	public TemplateSceltaWizardApiClientImpl(DispatchAsync dispatcher, EventBus eventBus, PecInPraticheDB pecInPraticheDB, SitemapMenu sitemapMenu, PlaceManager placeManager) {
		this.dispatcher = dispatcher;
		this.pecInPraticheDB = pecInPraticheDB;
		this.sitemapMenu = sitemapMenu;
		this.eventBus = eventBus;
		this.placeManager = placeManager;
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		eventBus.fireEventFromSource(event, this);
	}

	@Override
	public void goBackToPratica() {

		reset();
		placeManager.revealCurrentPlace();
	}

	private void reset() {

		fascicoloDTO = null;
		tipo = null;
	}

	/*
	 * Inizio Winzard
	 */

	@Override
	public void goToCreaDaTemplate(String fascicoloPath, final TipologiaPratica tipo) {

		this.tipo = tipo;

		ShowAppLoadingEvent.fire(TemplateSceltaWizardApiClientImpl.this, true);
		pecInPraticheDB.getFascicoloByPath(fascicoloPath, sitemapMenu.containsLink(fascicoloPath), new PraticaFascicoloLoaded() {

			@Override
			public void onPraticaLoaded(FascicoloDTO fascicolo) {
				TemplateSceltaWizardApiClientImpl.this.fascicoloDTO = fascicolo;
				ShowAppLoadingEvent.fire(TemplateSceltaWizardApiClientImpl.this, false);
				MostraSceltaTemplateEvent event = new MostraSceltaTemplateEvent(fascicoloDTO, tipo);
				eventBus.fireEvent(event);
			}

			@Override
			public void onPraticaError(String error) {
				ShowAppLoadingEvent.fire(TemplateSceltaWizardApiClientImpl.this, false);
				ShowMessageEvent event = new ShowMessageEvent();
				event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
				eventBus.fireEvent(event);

			}
		});
	}

	@Override
	public void goToCompilaCampiTemplate(final String templatePath) {

		ShowAppLoadingEvent.fire(TemplateSceltaWizardApiClientImpl.this, true);

		pecInPraticheDB.getModelloByPath(templatePath, sitemapMenu.containsLink(templatePath), new ModelloLoaded() {

			@Override
			public <T extends BaseTemplateDTO> void onPraticaLoaded(T template) {
				ShowAppLoadingEvent.fire(TemplateSceltaWizardApiClientImpl.this, false);

				template.accept(new ModelloVisitor() {

					@Override
					public void visit(TemplateDTO modelloMail) {
						TemplateSceltaWizardApiClientImpl.this.template = modelloMail;

						/*
						 * se il template non ha campi (o ne ha solo di tipo METADATO) vado direttamente alla bozza
						 */

						boolean onlyMetadata = true;
						for (CampoTemplateDTO campo : modelloMail.getCampi()) {
							if (!TipoCampoTemplateDTO.METADATA.equals(campo.getTipo())) {
								onlyMetadata = false;
								break;
							}
						}

						if (modelloMail.getCampi().isEmpty() || onlyMetadata) {
							creaBozzaDaTemplate(new HashMap<String, String>());

						} else {
							eventBus.fireEvent(new GoToInserimentoCampiTemplateEmailEvent(TemplateSceltaWizardApiClientImpl.this.template.getCampi()));
						}
					}

					@Override
					public void visit(TemplatePdfDTO modelloPdf) {
						ShowAppLoadingEvent.fire(TemplateSceltaWizardApiClientImpl.this, false);

						TemplateSceltaWizardApiClientImpl.this.template = modelloPdf;

						// devo mostrare per forza la maschera di inserimento del nome del file
						eventBus.fireEvent(new GoToInserimentoCampiTemplatePdfEvent(TemplateSceltaWizardApiClientImpl.this.template.getCampi(),
								modelloPdf.getTitoloFile() != null && !modelloPdf.getTitoloFile().trim().isEmpty()));
					}
				});
			}

			@Override
			public void onPraticaError(String error) {
				ShowAppLoadingEvent.fire(TemplateSceltaWizardApiClientImpl.this, false);
				ShowMessageEvent event = new ShowMessageEvent();
				event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
				eventBus.fireEvent(event);
			}
		});
	}

	@Override
	public void creaBozzaDaTemplate(Map<String, String> valori) {

		ShowAppLoadingEvent.fire(TemplateSceltaWizardApiClientImpl.this, true);
		ShowMessageEvent event = new ShowMessageEvent();
		event.setMessageDropped(true);
		eventBus.fireEvent(event);

		CreaBozzaDaTemplateAction action = new CreaBozzaDaTemplateAction(fascicoloDTO.getClientID(), TemplateSceltaWizardApiClientImpl.this.template.getClientID(), valori);

		dispatcher.execute(action, new AsyncCallback<CreaBozzaDaTemplateActionResult>() {

			@Override
			public void onFailure(Throwable caught) {
				ShowAppLoadingEvent.fire(TemplateSceltaWizardApiClientImpl.this, false);
				ShowMessageEvent event = new ShowMessageEvent();
				event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
				eventBus.fireEvent(event);
			}

			@Override
			public void onSuccess(CreaBozzaDaTemplateActionResult result) {
				if (!result.isError()) {
					// inserisco il fascicolo(aggiornato) e la nuova bozza,
					// sul db locale
					pecInPraticheDB.insertOrUpdate(result.getFascicolo().getClientID(), result.getFascicolo(), sitemapMenu.containsLink(result.getFascicolo().getClientID()));
					pecInPraticheDB.insertOrUpdate(result.getBozza().getClientID(), result.getBozza(), sitemapMenu.containsLink(result.getBozza().getClientID()));

					Place place = new Place();
					place.setToken(NameTokens.dettagliopecout);
					place.addParam(NameTokensParams.idPratica, result.getBozza().getClientID());

					eventBus.fireEvent(new GoToPlaceEvent(place));
					ShowAppLoadingEvent.fire(TemplateSceltaWizardApiClientImpl.this, false);

				} else {
					ShowAppLoadingEvent.fire(TemplateSceltaWizardApiClientImpl.this, false);
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
					eventBus.fireEvent(event);
				}
			}
		});
	}

	@Override
	public void backToSceltaTemplate() {

		String fascicoloPath = this.fascicoloDTO.getClientID();
		TipologiaPratica tipoTemplate = this.tipo;
		reset();
		goToCreaDaTemplate(fascicoloPath, tipoTemplate);
	}

	@Override
	public void creaPdfDaTemplate(Map<String, String> valori, String filename) {

		ShowAppLoadingEvent.fire(TemplateSceltaWizardApiClientImpl.this, true);
		ShowMessageEvent event = new ShowMessageEvent();
		event.setMessageDropped(true);
		eventBus.fireEvent(event);

		String fascicoloId = TemplateSceltaWizardApiClientImpl.this.fascicoloDTO.getClientID();
		String templateId = TemplateSceltaWizardApiClientImpl.this.template.getClientID();

		CreaPdfDaTemplateAction action = new CreaPdfDaTemplateAction(fascicoloId, templateId, valori, filename);
		dispatcher.execute(action, new AsyncCallback<CreaPdfDaTemplateActionResult>() {

			@Override
			public void onFailure(Throwable caught) {
				ShowAppLoadingEvent.fire(TemplateSceltaWizardApiClientImpl.this, false);
				ShowMessageEvent event = new ShowMessageEvent();
				event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
				eventBus.fireEvent(event);
			}

			@Override
			public void onSuccess(CreaPdfDaTemplateActionResult result) {

				if (!result.isError()) {

					pecInPraticheDB.insertOrUpdate(result.getFascicolo().getClientID(), result.getFascicolo(), sitemapMenu.containsLink(result.getFascicolo().getClientID()));
					goBackToPratica();
					ShowAppLoadingEvent.fire(TemplateSceltaWizardApiClientImpl.this, false);

				} else {
					ShowAppLoadingEvent.fire(TemplateSceltaWizardApiClientImpl.this, false);
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(result.getErrorMsg());
					eventBus.fireEvent(event);
				}
			}

		});
	}
}
