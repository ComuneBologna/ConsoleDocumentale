package it.eng.portlet.consolepec.gwt.client.presenter.template;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.portlet.consolepec.gwt.client.command.RicercaPraticheServerAdapter;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.ModelloLoaded;
import it.eng.portlet.consolepec.gwt.client.event.GoToPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.operazioni.template.TemplateSceltaWizardApiClient;
import it.eng.portlet.consolepec.gwt.client.place.NameTokens;
import it.eng.portlet.consolepec.gwt.client.place.NameTokensParams;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter.Place;
import it.eng.portlet.consolepec.gwt.client.presenter.RicercaCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.event.ApriInvioCSVEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.event.SceltaTemplateFromInvioCSV;
import it.eng.portlet.consolepec.gwt.client.presenter.event.SceltaTemplateFromInvioCSV.SceltaTemplateFromInvioCSVHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.inviomassivo.event.BackToCreaComunicazioneEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.inviomassivo.event.MostraSceltaTemplateDaCreaComunicazioneEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.inviomassivo.event.MostraSceltaTemplateDaCreaComunicazioneEvent.MostraSceltaTemplateDaCreaComunicazioneHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.template.event.BackToSceltaTemplateEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.template.event.BackToSceltaTemplateEvent.BackToSceltaTemplateHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.template.event.MostraSceltaTemplateEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.template.event.MostraSceltaTemplateEvent.MostraSceltaTemplateHandler;
import it.eng.portlet.consolepec.gwt.client.view.pecout.EsitoInvioDaCSVDialog;
import it.eng.portlet.consolepec.gwt.client.widget.SitemapMenu;
import it.eng.portlet.consolepec.gwt.client.worklist.AbstractWorklistStrategy;
import it.eng.portlet.consolepec.gwt.client.worklist.SceltaTemplateWorklistStrategy;
import it.eng.portlet.consolepec.gwt.client.worklist.WorklistStrategy;
import it.eng.portlet.consolepec.gwt.client.worklist.WorklistStrategy.RicercaCallback;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.CercaPratiche;
import it.eng.portlet.consolepec.gwt.shared.action.inviomassivo.CreaComunicazioneAction;
import it.eng.portlet.consolepec.gwt.shared.action.inviomassivo.CreaComunicazioneActionResult;
import it.eng.portlet.consolepec.gwt.shared.action.pec.InvioMailDaCSVAction;
import it.eng.portlet.consolepec.gwt.shared.action.pec.InvioMailDaCSVResult;
import it.eng.portlet.consolepec.gwt.shared.model.BaseTemplateDTO;
import it.eng.portlet.consolepec.gwt.shared.model.ColonnaWorklist;
import it.eng.portlet.consolepec.gwt.shared.model.ComunicazioneDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.InvioDaCsvBean;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public class SceltaTemplatePresenter extends Presenter<SceltaTemplatePresenter.MyView, SceltaTemplatePresenter.MyProxy> implements MostraSceltaTemplateHandler, BackToSceltaTemplateHandler, MostraSceltaTemplateDaCreaComunicazioneHandler, SceltaTemplateFromInvioCSVHandler {

	@ProxyCodeSplit
	@NameToken(NameTokens.sceltatemplate)
	public interface MyProxy extends ProxyPlace<SceltaTemplatePresenter> {}

	public interface MyView extends View {

		void init(Command cercaCommand, WorklistStrategy strategy);

		Set<ConstraintViolation<CercaPratiche>> formRicercaToCercaPratiche(CercaPratiche dto);

		void resetSelezioni();

		void setGruppiAbilitati(List<AnagraficaRuolo> gruppiAbilitati);

		void aggiornaRigheSelezionate();

		void resetForm();

		void setAvantiCommand(Command avantiCommand);

		void setAnnullaCommand(Command annullaCommand);

		String getClientIdSlezionato();

	}

	private FascicoloDTO fascicolo;
	private TipologiaPratica tipoTemplate;
	private final DispatchAsync dispatcher;
	private final PecInPraticheDB pecInDb;
	private WorklistStrategy strategy;
	private final SitemapMenu sitemapMenu;
	private final EventBus eventBus;
	private final RicercaPraticheServerAdapter ricercaAdapter;
	private boolean firstReveal = true;
	private ProfilazioneUtenteHandler profilazioneUtenteHandler;
	private TemplateSceltaWizardApiClient templateSceltaWizard;

	@Inject
	public SceltaTemplatePresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, final DispatchAsync dispatcher, final PecInPraticheDB pecInDb,
			final RicercaPraticheServerAdapter ricercaAdapter, final SitemapMenu sitemapMenu, final TemplateSceltaWizardApiClient templateSceltaWizard,
			ProfilazioneUtenteHandler profilazioneUtenteHandler) {
		super(eventBus, view, proxy);
		this.eventBus = eventBus;
		this.dispatcher = dispatcher;
		this.pecInDb = pecInDb;
		this.ricercaAdapter = ricercaAdapter;
		this.sitemapMenu = sitemapMenu;
		this.templateSceltaWizard = templateSceltaWizard;
		this.profilazioneUtenteHandler = profilazioneUtenteHandler;
	}

	@Override
	protected void onBind() {
		super.onBind();

	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPresenter.TYPE_SetMainContent, this);
	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);

	}

	@Override
	protected void onReveal() {
		super.onReveal();

		if (firstReveal) {
			strategy = new SceltaTemplateWorklistStrategy();
			strategy.setRicercaEventListener(new RicercaEventListener());
			strategy.setPraticheDB(pecInDb);
			strategy.setSitemapMenu(sitemapMenu);
			getView().init(new RicercaCommand(getEventBus(), (AbstractWorklistStrategy) strategy, getView().formRicercaToCercaPratiche(new CercaPratiche())), strategy);
			firstReveal = false;
			impostaElencoGruppi();

		} else {
			getView().resetForm();
			strategy.refreshDatiGrid();
		}
		getView().aggiornaRigheSelezionate();
		getView().resetSelezioni();

	}

	public void impostaAbilitazioniPulsantiera() {}

	private void impostaElencoGruppi() {
		getView().setGruppiAbilitati(profilazioneUtenteHandler.getAnagraficheRuoloUtente());
	}

	private class RicercaEventListener implements WorklistStrategy.RicercaEventListener {

		@Override
		public void onStartRicerca(int start, int length, ColonnaWorklist campoOrdinamento, boolean asc, RicercaCallback callback) {
			CercaPratiche action = new CercaPratiche();
			action.setFine(start + length);
			action.setInizio(start);
			action.setCampoOrdinamento(campoOrdinamento);
			action.setOrdinamentoAsc(asc);

			if (fascicolo != null)
				action.setTipoFascicoloAbilitatoSceltaTemplate(fascicolo.getTipologiaPratica().getNomeTipologia());

			action.setSuperutente(true);
			action.setIgnoraGruppi(true);
			action.setTipologiePratiche(Arrays.asList(tipoTemplate));
			getView().formRicercaToCercaPratiche(action);
			ricercaAdapter.startRicerca(action, callback);
		}

	}

	private class AvantiCommand implements Command {

		@Override
		public void execute() {
			templateSceltaWizard.goToCompilaCampiTemplate(getView().getClientIdSlezionato());
		}

	}

	private class AnnullaCommand implements Command {

		@Override
		public void execute() {
			templateSceltaWizard.goBackToPratica();
		}

	}

	@Override
	@ProxyEvent
	public void onMostraSceltaTemplate(MostraSceltaTemplateEvent event) {

		this.fascicolo = event.getFascicoloDTO();
		tipoTemplate = event.getTipo();

		getView().setAnnullaCommand(new AnnullaCommand());
		getView().setAvantiCommand(new AvantiCommand());

		revealInParent();
	}

	@Override
	@ProxyEvent
	public void onBackToSceltaTemplate(BackToSceltaTemplateEvent event) {
		getView().resetSelezioni();
		revealInParent();
	}

	@Override
	@ProxyEvent
	public void onMostraSceltaTemplateDaCreaComunicazione(MostraSceltaTemplateDaCreaComunicazioneEvent event) {

		fascicolo = null;
		tipoTemplate = TipologiaPratica.MODELLO_MAIL;

		getView().setAvantiCommand(new AvantiCreaComunicazioneCommand(event.getComuncazione()));
		getView().setAnnullaCommand(new AnnullaCreaComunicazioneCommand());
		revealInParent();
	}

	@Override
	@ProxyEvent
	public void onSceltaTemplate(SceltaTemplateFromInvioCSV event) {
		fascicolo = null;
		tipoTemplate = TipologiaPratica.MODELLO_MAIL;
		getView().setAvantiCommand(new AvantiInvioDaCSVCommand(event.getInvioDaCsvBean()));
		getView().setAnnullaCommand(new AnnullaInvioDaCSVCommand(event.getInvioDaCsvBean()));
		revealInParent();
	}

	@AllArgsConstructor
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	private class AnnullaInvioDaCSVCommand implements Command {

		private InvioDaCsvBean bean;

		@Override
		public void execute() {
			eventBus.fireEvent(new ApriInvioCSVEvent(bean.getClientIdFascicolo(), bean.getNomeAllegato()));
		}

	}

	@AllArgsConstructor
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	private class AvantiInvioDaCSVCommand implements Command {

		private InvioDaCsvBean bean;

		@Override
		public void execute() {
			bean.setClientIdTemplate(getView().getClientIdSlezionato());
			ShowAppLoadingEvent.fire(SceltaTemplatePresenter.this, true);

			dispatcher.execute(new InvioMailDaCSVAction(bean), new AsyncCallback<InvioMailDaCSVResult>() {

				@Override
				public void onFailure(Throwable caught) {
					ShowAppLoadingEvent.fire(SceltaTemplatePresenter.this, false);
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
					getEventBus().fireEvent(event);
				}

				@Override
				public void onSuccess(InvioMailDaCSVResult result) {

					ShowAppLoadingEvent.fire(SceltaTemplatePresenter.this, false);

					if (result.isError()) {
						ShowMessageEvent event = new ShowMessageEvent();
						event.setErrorMessage(result.getErrorMessage());
						getEventBus().fireEvent(event);

					} else {
						EsitoInvioDaCSVDialog dialog = new EsitoInvioDaCSVDialog("Esito invio da CSV", result, bean.getClientIdFascicolo(), eventBus);
						dialog.open();
					}

				}
			});
		}

	}

	private class AvantiCreaComunicazioneCommand implements Command {

		private ComunicazioneDTO comunicazione;

		public AvantiCreaComunicazioneCommand(ComunicazioneDTO comunicazione) {
			super();
			this.comunicazione = comunicazione;
		}

		@Override
		public void execute() {

			ShowAppLoadingEvent.fire(SceltaTemplatePresenter.this, true);

			ShowMessageEvent event = new ShowMessageEvent();
			event.setMessageDropped(true);
			getEventBus().fireEvent(event);

			final String templatePath = getView().getClientIdSlezionato();

			pecInDb.getModelloByPath(templatePath, sitemapMenu.containsLink(templatePath), new ModelloLoaded() {

				@Override
				public <T extends BaseTemplateDTO> void onPraticaLoaded(T template) {
					comunicazione.setIdDocumentaleTemplate(template.getNumeroRepertorio());

					CreaComunicazioneAction creaComunicazioneAction = new CreaComunicazioneAction(comunicazione);

					dispatcher.execute(creaComunicazioneAction, new AsyncCallback<CreaComunicazioneActionResult>() {

						@Override
						public void onFailure(Throwable caught) {
							ShowAppLoadingEvent.fire(SceltaTemplatePresenter.this, false);
							ShowMessageEvent event = new ShowMessageEvent();
							event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
							getEventBus().fireEvent(event);
						}

						@Override
						public void onSuccess(CreaComunicazioneActionResult result) {
							if (!result.getError()) {

								SceltaTemplatePresenter.this.pecInDb.insertOrUpdate(result.getComunicazione().getClientID(), result.getComunicazione(),
										sitemapMenu.containsLink(result.getComunicazione().getClientID()));

								Place place = new Place();
								place.setToken(NameTokens.dettagliocomunicazione);
								place.addParam(NameTokensParams.idPratica, result.getComunicazione().getClientID());

								getEventBus().fireEvent(new GoToPlaceEvent(place));
								ShowAppLoadingEvent.fire(SceltaTemplatePresenter.this, false);

							} else {
								ShowAppLoadingEvent.fire(SceltaTemplatePresenter.this, false);
								ShowMessageEvent event = new ShowMessageEvent();
								event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
								getEventBus().fireEvent(event);
							}
						}
					});
				}

				@Override
				public void onPraticaError(String error) {
					ShowAppLoadingEvent.fire(SceltaTemplatePresenter.this, false);
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
					getEventBus().fireEvent(event);
				}

			});
		}

	}

	private class AnnullaCreaComunicazioneCommand implements Command {

		@Override
		public void execute() {
			eventBus.fireEvent(new BackToCreaComunicazioneEvent());
		}

	}

}
