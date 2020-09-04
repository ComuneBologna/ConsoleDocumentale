package it.eng.portlet.consolepec.gwt.client.presenter.template;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaWorklist;
import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaWorklist.Counter;
import it.eng.portlet.consolepec.gwt.client.command.RicercaPraticheServerAdapter;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB;
import it.eng.portlet.consolepec.gwt.client.event.ApertoDettaglioEvent;
import it.eng.portlet.consolepec.gwt.client.event.GoToPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.event.MostraElencoPraticheEvent;
import it.eng.portlet.consolepec.gwt.client.event.MostraElencoPraticheEvent.MostraElencoPraticheHandler;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.WorklistHandler.WorklistHandlerCallback;
import it.eng.portlet.consolepec.gwt.client.place.NameTokens;
import it.eng.portlet.consolepec.gwt.client.place.NameTokensParams;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.RicercaCommand;
import it.eng.portlet.consolepec.gwt.client.widget.SitemapMenu;
import it.eng.portlet.consolepec.gwt.client.worklist.AbstractWorklistStrategy;
import it.eng.portlet.consolepec.gwt.client.worklist.RigaEspansaTemplateStrategy;
import it.eng.portlet.consolepec.gwt.client.worklist.TemplateWorklistStrategy;
import it.eng.portlet.consolepec.gwt.client.worklist.WorklistStrategy;
import it.eng.portlet.consolepec.gwt.client.worklist.WorklistStrategy.EspandiRigaEventListener;
import it.eng.portlet.consolepec.gwt.client.worklist.WorklistStrategy.RicercaCallback;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.CercaPratiche;
import it.eng.portlet.consolepec.gwt.shared.action.template.CaricaModelloAction;
import it.eng.portlet.consolepec.gwt.shared.action.template.CaricaModelloResult;
import it.eng.portlet.consolepec.gwt.shared.model.ColonnaWorklist;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.TipologiaCaricamento;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
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

public class WorklistTemplatePresenter extends Presenter<WorklistTemplatePresenter.MyView, WorklistTemplatePresenter.MyProxy> implements MostraElencoPraticheHandler {

	public interface MyView extends View {

		void init(Command cercaCommand, WorklistStrategy strategy);

		void espandiRiga(String rowAlfrescoPath, PraticaDTO pratica);

		void nascondiRiga(String rowAlfrescoPath);

		Set<ConstraintViolation<CercaPratiche>> formRicercaToCercaPratiche(CercaPratiche dto);

		void updateRigheEspanse();

		void updateRigheSelezionate();

		void resetSelezioni();

		public void impostaTitolo(String titolo);

		public void resetRicerca();

		void setGruppiAbilitati(List<AnagraficaRuolo> gruppiAbilitati);

	}

	@ProxyCodeSplit
	@NameToken({ NameTokens.worklisttemplate })
	public interface MyProxy extends ProxyPlace<WorklistTemplatePresenter> {
	}

	private final DispatchAsync dispatcher;
	private final PecInPraticheDB pecInDb;
	private WorklistStrategy strategy;
	private final SitemapMenu sitemapMenu;
	private final EventBus eventBus;
	private final RicercaPraticheServerAdapter ricercaAdapter;
	private boolean firstReveal = true;
	private AnagraficaWorklist worklistConfiguration;
	private ProfilazioneUtenteHandler profilazioneUtenteHandler;

	@Inject
	public WorklistTemplatePresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, final DispatchAsync dispatcher, final PecInPraticheDB pecInDb, final RicercaPraticheServerAdapter ricercaAdapter, final SitemapMenu sitemapMenu,
			ProfilazioneUtenteHandler profilazioneUtenteHandler) {
		super(eventBus, view, proxy);
		this.eventBus = eventBus;
		this.dispatcher = dispatcher;
		this.pecInDb = pecInDb;
		this.ricercaAdapter = ricercaAdapter;
		this.sitemapMenu = sitemapMenu;
		this.profilazioneUtenteHandler = profilazioneUtenteHandler;
	}

	@Override
	protected void onReveal() {
		super.onReveal();
		revealInParent();
		Window.scrollTo(0, 0);
	}
	
	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPresenter.TYPE_SetMainContent, this);
	}

	@Override
	public void prepareFromRequest(final PlaceRequest request) {
		super.prepareFromRequest(request);

		profilazioneUtenteHandler.getWorklist(false, new WorklistHandlerCallback() {
			
			@Override
			public void onSuccess(Map<AnagraficaWorklist, Counter> worklist) {
				worklistConfiguration = profilazioneUtenteHandler.getWorklist(request.getParameter(NameTokensParams.identificativoWorklist, null));
				
				if (worklistConfiguration == null)
					throw new IllegalStateException("Non è stata trovata nessuna worklist.");
				
				getView().impostaTitolo(worklistConfiguration.getTitoloWorklist());
				
				if (firstReveal) {
					strategy = new TemplateWorklistStrategy();
					strategy.addEspandiRigaEventListener(new EspandiRigaTemplateEvent());
					strategy.setRicercaEventListener(new RicercaEventListener());
					strategy.setPraticheDB(pecInDb);
					strategy.setSitemapMenu(sitemapMenu);
					strategy.setEventBus(eventBus);
					strategy.setRigaEspansaStrategy(new RigaEspansaTemplateStrategy());
					firstReveal = false;
					getView().init(new RicercaCommand(eventBus, (AbstractWorklistStrategy) strategy, getView().formRicercaToCercaPratiche(new CercaPratiche())), strategy);
					impostaElencoGruppi();
				}
				
				sitemapMenu.setActiveVoce(worklistConfiguration.getTitoloMenu());
				getView().updateRigheEspanse();
				getView().updateRigheSelezionate();
				getView().resetSelezioni();
			}
			
			@Override
			public void onFailure(String error) {
				throw new IllegalStateException(error);

			}
		});
	}

	public void mostraDettaglio(String clientID, boolean showAction) {
		MainPresenter.Place place = new MainPresenter.Place();
		place.setToken(worklistConfiguration.getNameTokenDettaglio());
		place.addParam(NameTokensParams.showActions, Boolean.toString(showAction));
		place.addParam(NameTokensParams.idPratica, clientID);
		GoToPlaceEvent goToPlaceEvent = new GoToPlaceEvent(place);
		eventBus.fireEvent(goToPlaceEvent);
	}

	@Override
	@ProxyEvent
	public void onMostraElencoPratiche(MostraElencoPraticheEvent event) {
		this.revealInParent();

	}

	public void impostaAbilitazioniPulsantiera() {
	}

	private void impostaElencoGruppi() {
		getView().setGruppiAbilitati(profilazioneUtenteHandler.getAnagraficheRuoloUtente());
	}

	/* Classi interne */

	private class EspandiRigaTemplateEvent implements EspandiRigaEventListener {

		@Override
		public void onEspandiRiga(final String clientID, TipologiaPratica tipologiaPratica, boolean isEspansa) {
			int clientWidth = Window.getClientWidth();
			if (clientWidth > ConsolePecConstants.MIN_DESKTOP_WIDTH_PIXELS) {
				if (!isEspansa) {

					CaricaModelloAction recuperaDettaglioPratica = new CaricaModelloAction(clientID, TipologiaCaricamento.getTipologiaCaricamento(sitemapMenu.containsLink(clientID)));
					/* reset dei messaggi di errore */
					ShowMessageEvent event = new ShowMessageEvent();
					event.setMessageDropped(true);
					eventBus.fireEvent(event);
					WorklistTemplatePresenter.this.dispatcher.execute(recuperaDettaglioPratica, new AsyncCallback<CaricaModelloResult>() {

						@Override
						public void onSuccess(CaricaModelloResult result) {
							if (!result.isError()) {
								PraticaDTO pratica = result.getModello();
								pecInDb.insertOrUpdate(clientID, pratica, sitemapMenu.containsLink(clientID));
								ApertoDettaglioEvent.fire(WorklistTemplatePresenter.this, pratica);
								getView().espandiRiga(clientID, pratica);
								
							} else {
								ShowMessageEvent event = new ShowMessageEvent();
								event.setErrorMessage(result.getErrorMessage());
								eventBus.fireEvent(event);
							}

						}

						@Override
						public void onFailure(Throwable caught) {
							ShowMessageEvent event = new ShowMessageEvent();
							event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
							eventBus.fireEvent(event);
						}
					});
				} else {
					getView().nascondiRiga(clientID);
				}
			} else {
				// mobile
				WorklistTemplatePresenter.this.mostraDettaglio(clientID, false);
			}

		}

	}

	private class RicercaEventListener implements WorklistStrategy.RicercaEventListener {

		@Override
		public void onStartRicerca(int start, int length, ColonnaWorklist campoOrdinamento, boolean asc, RicercaCallback callback) {
			CercaPratiche action = new CercaPratiche();
			action.setFine(start + length);
			action.setInizio(start);
			action.setCampoOrdinamento(campoOrdinamento);
			action.setOrdinamentoAsc(asc);
			action.setSoloWorklist(true);
			action.setParametriFissi(worklistConfiguration.getParametriFissiWorklist());

			getView().formRicercaToCercaPratiche(action);
			ricercaAdapter.startRicerca(action, callback);
		}

	}

}
