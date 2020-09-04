package it.eng.portlet.consolepec.gwt.client.presenter.fascicolo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;

import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
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
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest.Builder;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaWorklist;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaWorklist.Counter;
import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.util.pratica.PraticaUtil;
import it.eng.portlet.consolepec.gwt.client.command.RicercaPraticheServerAdapter;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaFascicoloLoaded;
import it.eng.portlet.consolepec.gwt.client.event.ApertoDettaglioEvent;
import it.eng.portlet.consolepec.gwt.client.event.ChiudiDettaglioAllegatoEvent;
import it.eng.portlet.consolepec.gwt.client.event.ChiudiDettaglioAllegatoEvent.ChiudiDettaglioAllegatoHandler;
import it.eng.portlet.consolepec.gwt.client.event.GoToPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.event.MostraDettaglioAllegatoEvent;
import it.eng.portlet.consolepec.gwt.client.event.MostraElencoPraticheEvent;
import it.eng.portlet.consolepec.gwt.client.event.MostraElencoPraticheEvent.MostraElencoPraticheHandler;
import it.eng.portlet.consolepec.gwt.client.event.RichiestaConfermaAnnullaEvent;
import it.eng.portlet.consolepec.gwt.client.event.SceltaConfermaAnnullaEvent;
import it.eng.portlet.consolepec.gwt.client.event.SceltaConfermaAnnullaEvent.SceltaConfermaAnnullaHandler;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.WorklistHandler.WorklistHandlerCallback;
import it.eng.portlet.consolepec.gwt.client.place.NameTokens;
import it.eng.portlet.consolepec.gwt.client.place.NameTokensParams;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter.Place;
import it.eng.portlet.consolepec.gwt.client.presenter.RicercaCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.event.GoToAssegnaFromWorklistFascicoloEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.MostraWorklistFascicoliEvent.MostraWorklistFascicoliHandler;
import it.eng.portlet.consolepec.gwt.client.widget.ElementoComunicazioneElencoWidget.MostraComunicazione;
import it.eng.portlet.consolepec.gwt.client.widget.ElementoPECElencoWidget.MostraPEC;
import it.eng.portlet.consolepec.gwt.client.widget.ElementoPraticaModulisticaElencoWidget.MostraPraticaModulistica;
import it.eng.portlet.consolepec.gwt.client.widget.GroupSuggestBoxProtocollazione;
import it.eng.portlet.consolepec.gwt.client.widget.SitemapMenu;
import it.eng.portlet.consolepec.gwt.client.worklist.AbstractWorklistStrategy;
import it.eng.portlet.consolepec.gwt.client.worklist.FascicoloGenericoWorklistStrategy;
import it.eng.portlet.consolepec.gwt.client.worklist.RigaEspansaFascicoloGenericoStrategy;
import it.eng.portlet.consolepec.gwt.client.worklist.WorklistStrategy;
import it.eng.portlet.consolepec.gwt.client.worklist.WorklistStrategy.EspandiRigaEventListener;
import it.eng.portlet.consolepec.gwt.client.worklist.WorklistStrategy.RicercaCallback;
import it.eng.portlet.consolepec.gwt.shared.Base64Utils;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.UriMapping;
import it.eng.portlet.consolepec.gwt.shared.action.CercaPratiche;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CambiaStatoFascicolo;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CambiaStatoFascicoloEnum;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CambiaStatoFascicoloResult;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CaricaPraticaFascicoloAction;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CaricaPraticaFascicoloActionResult;
import it.eng.portlet.consolepec.gwt.shared.action.pec.TipoRiferimentoPEC;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.ColonnaWorklist;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.TipologiaCaricamento;

public class WorklistFascicoliGenericoPresenter extends Presenter<WorklistFascicoliGenericoPresenter.MyView, WorklistFascicoliGenericoPresenter.MyProxy> implements MostraWorklistFascicoliHandler, MostraElencoPraticheHandler, ChiudiDettaglioAllegatoHandler, SceltaConfermaAnnullaHandler {

	public interface MyView extends View {

		void init(it.eng.portlet.consolepec.gwt.client.presenter.Command<Object, Set<String>> archiviaCommand,
				it.eng.portlet.consolepec.gwt.client.presenter.Command<Object, Set<String>> riportaInGestioneCommand, Command selezionePraticaCommand,
				it.eng.portlet.consolepec.gwt.client.presenter.Command<Object, Set<String>> eliminaCommand, WorklistStrategy strategy,
				it.eng.portlet.consolepec.gwt.client.presenter.Command<Object, Set<String>> assegnaCommand);

		void espandiRiga(String rowAlfrescoPath, PraticaDTO pratica);

		void nascondiRiga(String rowAlfrescoPath);

		Set<ConstraintViolation<CercaPratiche>> formRicercaToCercaPratiche(CercaPratiche dto);

		List<PraticaDTO> getRigheEspanse();

		Set<String> getIDRigheSelezionate();

		void setArchiviaButtonEnabled(boolean b);

		void setAssegnaButtonEnabled(boolean b);

		void setEliminaButtonEnabled(boolean b);

		void setRiportaInGestioneEnabled(boolean b);

		void aggiornaRiga(FascicoloDTO fascicoli);

		void sendDownload(SafeUri uri);

		void updateRigheEspanse();

		void updateRigheSelezionate();

		void refreshGrid();

		void resetSelezioni();

		public void impostaTitolo(String titolo);

		public void resetRicerca();

		void setGruppiAbilitati(List<AnagraficaRuolo> gruppiAbilitati);

		void setTipoPratiche(List<TipologiaPratica> suggestions);

		void setTipiPraticaGestite(List<TipologiaPratica> anagraficheFascicoli);

		void setFormRicerca(AnagraficaWorklist worklistConfiguration, RicercaCommand ricercaCommand, GroupSuggestBoxProtocollazione groupSuggestBoxProtocollazione);

	}

	@ProxyCodeSplit
	@NameToken({ NameTokens.worklistfascicolo })
	public interface MyProxy extends ProxyPlace<WorklistFascicoliGenericoPresenter> {
		//
	}

	private final DispatchAsync dispatcher;
	private final PecInPraticheDB pecInDb;
	private final PlaceManager placeManager;
	private WorklistStrategy strategy;
	private final SitemapMenu sitemapMenu;
	private final EventBus eventBus;
	private final RicercaPraticheServerAdapter ricercaAdapter;
	private final GroupSuggestBoxProtocollazione groupSuggestBoxProtocollazione;
	private boolean firstReveal = true;
	private AnagraficaWorklist worklistConfiguration;
	private ConfigurazioniHandler configurazioniHandler;
	private String idWorklist;
	private RicercaCommand ricercaCommand;
	private ProfilazioneUtenteHandler profilazioneUtenteHandler;

	@Inject
	public WorklistFascicoliGenericoPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, final DispatchAsync dispatcher, final PecInPraticheDB pecInDb,
			final PlaceManager placeManager, final RicercaPraticheServerAdapter ricercaAdapter, final SitemapMenu sitemapMenu, final GroupSuggestBoxProtocollazione groupSuggestBoxProtocollazione,
			ConfigurazioniHandler configurazioniHandler, ProfilazioneUtenteHandler profilazioneUtenteHandler) {
		super(eventBus, view, proxy);
		this.eventBus = eventBus;
		this.dispatcher = dispatcher;
		this.pecInDb = pecInDb;
		this.placeManager = placeManager;
		this.ricercaAdapter = ricercaAdapter;
		this.sitemapMenu = sitemapMenu;
		this.groupSuggestBoxProtocollazione = groupSuggestBoxProtocollazione;
		this.configurazioniHandler = configurazioniHandler;
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
				idWorklist = request.getParameter(NameTokensParams.identificativoWorklist, null);
				worklistConfiguration = profilazioneUtenteHandler.getWorklist(idWorklist);
				if (worklistConfiguration != null) {

					if (firstReveal) {
						strategy = new FascicoloGenericoWorklistStrategy();
						strategy.addEspandiRigaEventListener(new EspandiRigaFascicoloGenericoEvent());
						strategy.setRicercaEventListener(new RicercaEventListener());
						strategy.setPraticheDB(pecInDb);
						strategy.setSitemapMenu(sitemapMenu);
						strategy.setEventBus(eventBus);
						strategy.setRigaEspansaStrategy(new RigaEspansaFascicoloGenericoStrategy(pecInDb, new DownloadAllegatoCommand(), new MostraDettaglioAllegatoCommand(),
								new MostraDettaglioPECCommand(), new MostraDettaglioPraticaModulisticaCommand(), new MostraDettaglioComunicazioneCommand(), sitemapMenu));

						ricercaCommand = new RicercaCommand(eventBus, (AbstractWorklistStrategy) strategy, new HashSet<ConstraintViolation<CercaPratiche>>());
					}

					getView().setFormRicerca(worklistConfiguration, ricercaCommand, groupSuggestBoxProtocollazione);
					getView().impostaTitolo(worklistConfiguration.getTitoloWorklist());
					impostaElencoGruppi();

					if (firstReveal) {

						firstReveal = false;
						getView().init(new CambiaStatoCommand(CambiaStatoFascicoloEnum.ARCHIVIA), new CambiaStatoCommand(CambiaStatoFascicoloEnum.RIPORTAINGESTIONE), new SelezionePraticaCommand(),
								new CambiaStatoCommand(CambiaStatoFascicoloEnum.ELIMINATO), strategy, new AssegnaCommand());

					}

					sitemapMenu.setActiveVoce(worklistConfiguration.getTitoloMenu());
					getView().updateRigheEspanse();
					getView().updateRigheSelezionate();
					getView().resetSelezioni();
					impostaElencoGruppi();
					getView().setTipiPraticaGestite(PraticaUtil.fascicoliToTipologiePratiche(configurazioniHandler.getAnagraficheFascicoli(true)));

					if (!firstReveal) {
						ricercaCommand.refreshDatiGrid();
					}
				}
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
		place.addParam(NameTokensParams.resetComposizioneFascicolo, Boolean.toString(true));
		GoToPlaceEvent goToPlaceEvent = new GoToPlaceEvent(place);
		eventBus.fireEvent(goToPlaceEvent);
	}

	@Override
	@ProxyEvent
	public void onMostraElencoPratiche(MostraElencoPraticheEvent event) {
		this.revealInParent();

	}

	@Override
	@ProxyEvent
	public void onChiudiDettaglioAllegato(ChiudiDettaglioAllegatoEvent event) {
		if (worklistConfiguration != null && placeManager.getCurrentPlaceRequest().getNameToken().equals(worklistConfiguration.getNameTokenWorklist())) {
			ShowAppLoadingEvent.fire(this, false);
			placeManager.revealCurrentPlace();
		}
	}

	public void impostaAbilitazioniPulsantiera() {
		final Set<String> righeSelezionate = getView().getIDRigheSelezionate();
		if (righeSelezionate.size() == 0) {
			disabilitaTuttiPulsanti();
			return;
		}
		final List<FascicoloDTO> temp = new ArrayList<FascicoloDTO>();
		for (String clientID : righeSelezionate) {
			pecInDb.getFascicoloByPath(clientID, sitemapMenu.containsLink(clientID), new PraticaFascicoloLoaded() {
				@Override
				public void onPraticaLoaded(FascicoloDTO pec) {
					temp.add(pec);
					if (righeSelezionate.size() == temp.size()) {
						configuraPulsanti(temp);
					}
				}

				@Override
				public void onPraticaError(String error) {
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
					eventBus.fireEvent(event);
				}
			});
		}

	}

	private void impostaElencoGruppi() {
		getView().setGruppiAbilitati(profilazioneUtenteHandler.getAnagraficheRuoloUtente());
	}

	protected void configuraPulsanti(List<FascicoloDTO> fascicoliSelezionati) {
		boolean archiviabile = true;
		boolean riassegnaAbilitato = true;
		boolean eliminabile = true;
		boolean riportaInGestione = true;
		for (FascicoloDTO f : fascicoliSelezionati) {
			archiviabile &= f.isConcludi();
			riassegnaAbilitato &= f.isRiassegna();
			eliminabile &= f.isEliminaFascicoloAbilitato();
			riportaInGestione &= f.isRiportaInGestioneAbilitato();
		}

		getView().setArchiviaButtonEnabled(archiviabile);
		getView().setEliminaButtonEnabled(eliminabile);
		getView().setRiportaInGestioneEnabled(riportaInGestione);
		getView().setAssegnaButtonEnabled(riassegnaAbilitato);

	}

	public void disabilitaTuttiPulsanti() {

		getView().setArchiviaButtonEnabled(false);
		getView().setAssegnaButtonEnabled(false);
		getView().setEliminaButtonEnabled(false);
		getView().setRiportaInGestioneEnabled(false);

	}

	public void lanciaCambioStatoAction(CambiaStatoFascicolo action) {
		/* reset dei messaggi di errore */
		ShowAppLoadingEvent.fire(WorklistFascicoliGenericoPresenter.this, true);
		ShowMessageEvent event = new ShowMessageEvent();
		event.setMessageDropped(true);
		eventBus.fireEvent(event);
		dispatcher.execute(action, new AsyncCallback<CambiaStatoFascicoloResult>() {

			@Override
			public void onFailure(Throwable caught) {
				ShowAppLoadingEvent.fire(WorklistFascicoliGenericoPresenter.this, false);
				ShowMessageEvent event = new ShowMessageEvent();
				event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
				eventBus.fireEvent(event);
			}

			@Override
			public void onSuccess(CambiaStatoFascicoloResult result) {
				ShowAppLoadingEvent.fire(WorklistFascicoliGenericoPresenter.this, false);
				if (result.isError()) {
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(result.getErrorMsg());
					eventBus.fireEvent(event);
				} else {

					for (FascicoloDTO fascicolo : result.getFascicoli()) {
						getView().aggiornaRiga(fascicolo);
						pecInDb.update(fascicolo.getClientID(), fascicolo, sitemapMenu.containsLink(fascicolo.getClientID()));
					}
					impostaAbilitazioniPulsantiera();
					getView().refreshGrid();

				}
			}
		});

	}

	/* Classi interne */

	private class EspandiRigaFascicoloGenericoEvent implements EspandiRigaEventListener {

		@Override
		public void onEspandiRiga(final String clientID, TipologiaPratica tipologiaPratica, boolean isEspansa) {
			int clientWidth = Window.getClientWidth();
			if (clientWidth > ConsolePecConstants.MIN_DESKTOP_WIDTH_PIXELS) {
				if (!isEspansa) {

					CaricaPraticaFascicoloAction recuperaDettaglioPratica = new CaricaPraticaFascicoloAction(clientID,
							TipologiaCaricamento.getTipologiaCaricamento(sitemapMenu.containsLink(clientID)));
					/* reset dei messaggi di errore */
					ShowMessageEvent event = new ShowMessageEvent();
					event.setMessageDropped(true);
					eventBus.fireEvent(event);
					WorklistFascicoliGenericoPresenter.this.dispatcher.execute(recuperaDettaglioPratica, new AsyncCallback<CaricaPraticaFascicoloActionResult>() {

						@Override
						public void onSuccess(CaricaPraticaFascicoloActionResult result) {
							if (!result.getError()) {

								PraticaDTO pratica = result.getPratica();
								pecInDb.insertOrUpdate(clientID, pratica, sitemapMenu.containsLink(clientID));
								ApertoDettaglioEvent.fire(WorklistFascicoliGenericoPresenter.this, pratica);
								getView().espandiRiga(clientID, pratica);
							} else {
								ShowMessageEvent event = new ShowMessageEvent();
								event.setErrorMessage(result.getMessError());
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
				WorklistFascicoliGenericoPresenter.this.mostraDettaglio(clientID, false);
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
			Set<ConstraintViolation<CercaPratiche>> violations = getView().formRicercaToCercaPratiche(action);
			if (violations.size() == 0) {
				ricercaAdapter.startRicerca(action, callback);
			}
		}

	}

	private String eventId;
	private Set<String> fascicoliDaEliminare = new HashSet<String>();

	public void eliminaAction(Set<String> id) {
		eventId = DOM.createUniqueId();
		fascicoliDaEliminare.clear();
		fascicoliDaEliminare.addAll(id);
		RichiestaConfermaAnnullaEvent.fire(WorklistFascicoliGenericoPresenter.this, "<h4>Procedere con la cancellazione del fascicolo?<h4>", eventId);
	}

	@Override
	@ProxyEvent
	public void onSceltaConfermaAnnulla(SceltaConfermaAnnullaEvent sceltaConfermaCancellazioneFascicoloEvent) {
		if (sceltaConfermaCancellazioneFascicoloEvent.isConfermato() && sceltaConfermaCancellazioneFascicoloEvent.getEventId().equals(eventId)) {
			CambiaStatoFascicoloEnum stato = CambiaStatoFascicoloEnum.ELIMINATO;
			CambiaStatoFascicolo action = new CambiaStatoFascicolo(fascicoliDaEliminare, stato);
			eliminaAction(action);
		}

		fascicoliDaEliminare.clear();
	}

	// per archivia riporta in gestione
	public class CambiaStatoCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Object, Set<String>> {
		private final CambiaStatoFascicoloEnum stato;

		public CambiaStatoCommand(CambiaStatoFascicoloEnum stato) {
			this.stato = stato;
		}

		@Override
		public Object exe(Set<String> ids) {
			CambiaStatoFascicolo action = new CambiaStatoFascicolo(ids, stato);

			if (stato == CambiaStatoFascicoloEnum.ELIMINATO) {
				eliminaAction(ids);

			} else {
				lanciaCambioStatoAction(action);
			}

			return null;
		}

	}

	public void eliminaAction(CambiaStatoFascicolo action) {
		/* reset dei messaggi di errore */
		ShowAppLoadingEvent.fire(WorklistFascicoliGenericoPresenter.this, true);
		ShowMessageEvent event = new ShowMessageEvent();
		event.setMessageDropped(true);
		eventBus.fireEvent(event);
		dispatcher.execute(action, new AsyncCallback<CambiaStatoFascicoloResult>() {

			@Override
			public void onFailure(Throwable caught) {
				ShowAppLoadingEvent.fire(WorklistFascicoliGenericoPresenter.this, false);
				ShowMessageEvent event = new ShowMessageEvent();
				event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
				eventBus.fireEvent(event);
			}

			@Override
			public void onSuccess(CambiaStatoFascicoloResult result) {
				ShowAppLoadingEvent.fire(WorklistFascicoliGenericoPresenter.this, false);
				if (result.isError()) {
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(result.getErrorMsg());
					eventBus.fireEvent(event);
				} else {
					for (String id : result.getClientIdEliminati()) {
						// v.aggiornaRiga( fascicolo );
						pecInDb.remove(id);
					}
					getView().refreshGrid();
				}
			}
		});

	}

	public class AssegnaCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Object, Set<String>> {

		@Override
		public Object exe(Set<String> ids) {
			GoToAssegnaFromWorklistFascicoloEvent event = new GoToAssegnaFromWorklistFascicoloEvent(ids, worklistConfiguration.getNome());
			eventBus.fireEvent(event);
			return null;
		}

	}

	public class DownloadAllegatoCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, AllegatoDTO> {

		@Override
		public Void exe(final AllegatoDTO allegato) {
			SafeUri uri = UriMapping.generaDownloadAllegatoServletURL(allegato.getClientID(), allegato);
			getView().sendDownload(uri);
			return null;
		}
	}

	private class MostraDettaglioAllegatoCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, AllegatoDTO> {

		@Override
		public Void exe(AllegatoDTO allegato) {
			MostraDettaglioAllegatoEvent.fire(WorklistFascicoliGenericoPresenter.this, allegato.getClientID(), allegato);
			return null;
		}

	}

	private class MostraDettaglioPECCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, MostraPEC> {

		@Override
		public Void exe(MostraPEC m) {
			String nameToken = null;
			if (m.getTipo().equals(TipoRiferimentoPEC.IN)) {
				nameToken = NameTokens.dettagliopecin;
			} else {
				nameToken = NameTokens.dettagliopecout;
			}
			Place place = new Place();
			place.setToken(nameToken);
			place.addParam(NameTokensParams.idPratica, m.getClientID());
			getEventBus().fireEvent(new GoToPlaceEvent(place));
			return null;
		}

	}

	private class MostraDettaglioPraticaModulisticaCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, MostraPraticaModulistica> {

		@Override
		public Void exe(MostraPraticaModulistica m) {
			Place place = new Place();
			place.setToken(NameTokens.dettagliopraticamodulistica);
			place.addParam(NameTokensParams.idPratica, m.getClientID());
			getEventBus().fireEvent(new GoToPlaceEvent(place));
			return null;
		}

	}

	private class MostraDettaglioComunicazioneCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, MostraComunicazione> {

		@Override
		public Void exe(MostraComunicazione t) {
			Place place = new Place();
			place.setToken(NameTokens.dettagliocomunicazione);
			place.addParam(NameTokensParams.idPratica, t.getClientID());
			getEventBus().fireEvent(new GoToPlaceEvent(place));
			return null;
		}

	}

	private class SelezionePraticaCommand implements Command {
		@Override
		public void execute() {
			impostaAbilitazioniPulsantiera();
		}

	}

	@Override
	@ProxyEvent
	public void onMostraWorklistFascicoli(MostraWorklistFascicoliEvent event) {

		if (firstReveal || ricercaCommand == null) {
			Builder builder = new PlaceRequest.Builder();
			builder.nameToken(NameTokens.worklistfascicolo);
			builder.with(NameTokensParams.identificativoWorklist, Base64Utils.URLencodeAlfrescoPath(event.getIdentificativoWorklist()));
			prepareFromRequest(builder.build());

		} else {
			getView().resetSelezioni();
			revealInParent();
			ricercaCommand.refreshDatiGrid();
		}
	}

}
