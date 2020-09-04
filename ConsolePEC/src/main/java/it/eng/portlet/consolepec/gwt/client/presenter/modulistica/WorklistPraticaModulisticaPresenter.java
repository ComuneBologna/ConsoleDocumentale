package it.eng.portlet.consolepec.gwt.client.presenter.modulistica;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaWorklist;
import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaWorklist.Counter;
import it.eng.portlet.consolepec.gwt.client.command.RicercaPraticheServerAdapter;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaModulisticaLoaded;
import it.eng.portlet.consolepec.gwt.client.event.ApertoDettaglioEvent;
import it.eng.portlet.consolepec.gwt.client.event.ChiudiDettaglioAllegatoEvent;
import it.eng.portlet.consolepec.gwt.client.event.ChiudiDettaglioAllegatoEvent.ChiudiDettaglioAllegatoHandler;
import it.eng.portlet.consolepec.gwt.client.event.ChiudiProtocollazionePageEvent;
import it.eng.portlet.consolepec.gwt.client.event.GoToPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.WorklistHandler.WorklistHandlerCallback;
import it.eng.portlet.consolepec.gwt.client.place.NameTokens;
import it.eng.portlet.consolepec.gwt.client.place.NameTokensParams;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.RicercaCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.event.GoToAssegnaFromWorklistPraticaModulisticaEvent;
import it.eng.portlet.consolepec.gwt.client.widget.GroupSuggestBoxProtocollazione;
import it.eng.portlet.consolepec.gwt.client.widget.SitemapMenu;
import it.eng.portlet.consolepec.gwt.client.worklist.AbstractWorklistStrategy;
import it.eng.portlet.consolepec.gwt.client.worklist.PraticaModulisticaWorklistStrategy;
import it.eng.portlet.consolepec.gwt.client.worklist.RigaEspansaPraticaModulisticaStrategy;
import it.eng.portlet.consolepec.gwt.client.worklist.WorklistStrategy;
import it.eng.portlet.consolepec.gwt.client.worklist.WorklistStrategy.EspandiRigaEventListener;
import it.eng.portlet.consolepec.gwt.client.worklist.WorklistStrategy.RicercaCallback;
import it.eng.portlet.consolepec.gwt.shared.Base64Utils;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.CercaPratiche;
import it.eng.portlet.consolepec.gwt.shared.action.modulistica.CambiaStatoPraticaModulistica;
import it.eng.portlet.consolepec.gwt.shared.action.modulistica.CambiaStatoPraticaModulisticaEnum;
import it.eng.portlet.consolepec.gwt.shared.action.modulistica.CambiaStatoPraticaModulisticaResult;
import it.eng.portlet.consolepec.gwt.shared.action.modulistica.CaricaPraticaModulisticaAction;
import it.eng.portlet.consolepec.gwt.shared.action.modulistica.CaricaPraticaModulisticaActionResult;
import it.eng.portlet.consolepec.gwt.shared.model.ColonnaWorklist;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaModulisticaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.TipologiaCaricamento;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;

import com.google.gwt.safehtml.shared.SafeUri;
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
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

public class WorklistPraticaModulisticaPresenter extends Presenter<WorklistPraticaModulisticaPresenter.MyView, WorklistPraticaModulisticaPresenter.MyProxy> implements ChiudiProtocollazionePageEvent.ChiudiTipoProtocollazionePageHandler, ChiudiDettaglioAllegatoHandler {

	public interface MyView extends View {
		void init(WorklistStrategy strategy, Command selezionePraticaCommand);

		void setAssegnaButtonEnabled(boolean enable);

		void setArchiviaButtonEnabled(boolean enable);

		void setRiportaInGestioneEnabled(boolean enable);

		void setEliminaButtonEnabled(boolean enable);

		public Set<ConstraintViolation<CercaPratiche>> formRicercaToCercaPratiche(CercaPratiche action);

		void updateRigheEspanse();

		Set<String> getIDRigheSelezionate();

		void espandiRiga(String rowAlfrescoPath, PraticaDTO pratica);

		void nascondiRiga(String rowAlfrescoPath);

		List<PraticaDTO> getRigheEspanse();

		void aggiornaRiga(PraticaDTO fascicolo);

		void sendDownload(SafeUri uri);

		void setArchiviaCommand(it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, Set<String>> archivia);

		void setEliminaCommand(it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, Set<String>> eliminaCommand);

		void setRiportaInGestioneCommand(it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, Set<String>> riportaInGestione);

		void setAssegnaCommand(AssegnaCommand assegnaCommand);

		public void resetSelezioni();

		void updateRigheSelezionate();

		public void resetRicerca();

		void refreshGrid();

		void impostaTitolo(String titoloWorklistModulo);

		public void setGruppiAbilitati(List<AnagraficaRuolo> gruppiAbilitati);

		void setFormRicerca(AnagraficaWorklist worklistConfiguration,
				RicercaCommand ricercaCommand,
				GroupSuggestBoxProtocollazione gruppoSuggest);

	}

	private final DispatchAsync dispatcher;
	private final PecInPraticheDB praticheDB;
	private final PlaceManager placeManager;
	private WorklistStrategy strategy;
	private final EventBus eventBus;
	private final RicercaPraticheServerAdapter ricercaAdapter;
	private final SitemapMenu sitemapMenu;
	private final GroupSuggestBoxProtocollazione gruppoSuggest;
	private boolean firstReveal = true;
	private String nomeModulo;
	private String worklistModulistica;
	private AnagraficaWorklist worklistConfiguration;
	private RicercaCommand ricercaCommand;
	private ProfilazioneUtenteHandler profilazioneUtenteHandler;

	@ProxyCodeSplit
	@NameToken(NameTokens.worklistpraticamodulistica)
	public interface MyProxy extends ProxyPlace<WorklistPraticaModulisticaPresenter> {
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

		this.nomeModulo = Base64Utils.URLdecodeAlfrescoPath(request.getParameter(NameTokensParams.nomeModulo, null));
		this.worklistModulistica = Base64Utils.URLdecodeAlfrescoPath(request.getParameter(NameTokensParams.worklistModulistica, null));

		profilazioneUtenteHandler.getWorklist(false, new WorklistHandlerCallback() {
			
			@Override
			public void onSuccess(Map<AnagraficaWorklist, Counter> worklist) {
				worklistConfiguration = profilazioneUtenteHandler.getWorklist(request.getParameter(NameTokensParams.identificativoWorklist, null));
				if (worklistConfiguration == null)
					throw new IllegalStateException("Non è stata trovata nessuna worklist.");
				
				if (firstReveal) {
					strategy = new PraticaModulisticaWorklistStrategy();
					strategy.setRigaEspansaStrategy(new RigaEspansaPraticaModulisticaStrategy());
					strategy.addEspandiRigaEventListener(new EspandiRigaPraticaModulisticaEvent());
					strategy.setRicercaEventListener(new RicercaEventListener());
					strategy.setPraticheDB(praticheDB);
					strategy.setSitemapMenu(sitemapMenu);
					strategy.setEventBus(eventBus);
					
					ricercaCommand = new RicercaCommand(eventBus, (AbstractWorklistStrategy) strategy, new HashSet<ConstraintViolation<CercaPratiche>>());
				}
				
				getView().setFormRicerca(worklistConfiguration, ricercaCommand, gruppoSuggest); 
				
				if (firstReveal) {
					getView().init(strategy, new SelezionePraticaCommand());
					firstReveal = false;
				}
				
				getView().impostaTitolo(worklistConfiguration.getTitoloWorklist());// FIXME: uniformare il nome del metodo al fascicolo
				sitemapMenu.setActiveVoce(worklistConfiguration.getTitoloMenu());
				getView().updateRigheEspanse();
				getView().updateRigheSelezionate();
				getView().resetSelezioni();
				impostaElencoGruppi();
			}
			
			@Override
			public void onFailure(String error) {
				throw new IllegalStateException(error);
			}
		});
	}

	@Override
	protected void onBind() {
		super.onBind();
		getView().setArchiviaCommand(new ArchiviaCommandMassivo());
		getView().setEliminaCommand(new EliminaCommandMassivo());
		getView().setAssegnaCommand(new AssegnaCommand());
		getView().setRiportaInGestioneCommand(new RiportaInGestioneCommandMassivo());
	}

	@Inject
	public WorklistPraticaModulisticaPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, final DispatchAsync dispatcher, final PecInPraticheDB pecInDB, final RicercaPraticheServerAdapter ricercaAdapter, final SitemapMenu sitemapMenu,
			final GroupSuggestBoxProtocollazione gruppoSuggest, final PlaceManager placeManager, AnagraficaWorklist worklistConfiguration, ProfilazioneUtenteHandler profilazioneUtenteHandler) {
		super(eventBus, view, proxy);
		this.profilazioneUtenteHandler = profilazioneUtenteHandler;
		this.dispatcher = dispatcher;
		this.praticheDB = pecInDB;
		this.placeManager = placeManager;
		this.eventBus = eventBus;
		this.ricercaAdapter = ricercaAdapter;
		this.sitemapMenu = sitemapMenu;
		this.gruppoSuggest = gruppoSuggest;
		this.worklistConfiguration = worklistConfiguration;
		
	}

	private void mostraDettaglio(String id) {
		MainPresenter.Place place = new MainPresenter.Place();
		place.setToken(NameTokens.dettagliopraticamodulistica);
		place.addParam(NameTokensParams.idPratica, id);
		GoToPlaceEvent goToPlaceEvent = new GoToPlaceEvent(place);
		eventBus.fireEvent(goToPlaceEvent);
	}

	public void impostaAbilitazioniPulsantiera() {
		final Set<String> listIdSelezionateselezionate = getView().getIDRigheSelezionate();
		if (listIdSelezionateselezionate.size() == 0) {
			disabilitaTuttiPulsanti();
			return;
		}

		final List<PraticaModulisticaDTO> temp = new ArrayList<PraticaModulisticaDTO>();
		for (String sel : listIdSelezionateselezionate) {
			praticheDB.getPraticaModulisticaByPath(sel, sitemapMenu.containsLink(sel), new PraticaModulisticaLoaded() {

				@Override
				public void onPraticaModulisticaLoaded(PraticaModulisticaDTO pratica) {
					temp.add(pratica);
					if (listIdSelezionateselezionate.size() == temp.size()) {
						configuraPulsanti(temp);
					}
				}

				@Override
				public void onPraticaModulisticaError(String error) {
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
					eventBus.fireEvent(event);
				}
			});
		}
	}

	protected void configuraPulsanti(List<PraticaModulisticaDTO> temp) {
		boolean archiviabile = true;
		boolean riassegnaAbilitato = true;
		boolean eliminabile = true;
		boolean riportaInGestione = true;

		for (PraticaModulisticaDTO pm : temp) {
			archiviabile &= pm.isArchiviaAbilitato();
			riassegnaAbilitato &= pm.isRiassegnaAbilitato();
			eliminabile &= pm.isEliminaAbilitato();
			riportaInGestione &= pm.isRiportaInGestioneAbilitato();

		}

		getView().setArchiviaButtonEnabled(archiviabile);
		getView().setAssegnaButtonEnabled(riassegnaAbilitato);
		getView().setEliminaButtonEnabled(eliminabile);
		getView().setRiportaInGestioneEnabled(riportaInGestione);
	}

	private void disabilitaTuttiPulsanti() {

		getView().setArchiviaButtonEnabled(false);
		getView().setAssegnaButtonEnabled(false);
		getView().setEliminaButtonEnabled(false);
		getView().setRiportaInGestioneEnabled(false);

	}

	private void impostaElencoGruppi() {
		getView().setGruppiAbilitati(profilazioneUtenteHandler.getAnagraficheRuoloUtente());
	}

	public void archivia(Set<String> ids) {
		CambiaStatoPraticaModulistica action = new CambiaStatoPraticaModulistica(ids, CambiaStatoPraticaModulisticaEnum.ARCHIVIA);
		lanciaOperazioneMassiva(action);

	}

	public void elimina(Set<String> ids) {
		CambiaStatoPraticaModulistica action = new CambiaStatoPraticaModulistica(ids, CambiaStatoPraticaModulisticaEnum.ELIMINATA);
		lanciaOperazioneMassiva(action);

	}

	public void riportaInGestione(Set<String> ids) {
		CambiaStatoPraticaModulistica action = new CambiaStatoPraticaModulistica(ids, CambiaStatoPraticaModulisticaEnum.RIPORTAINGESTIONE);
		lanciaOperazioneMassiva(action);
	}

	private void lanciaOperazioneMassiva(CambiaStatoPraticaModulistica action) {
		ShowAppLoadingEvent.fire(this, true);
		/* reset dei messaggi di errore */
		ShowMessageEvent event = new ShowMessageEvent();
		event.setMessageDropped(true);
		eventBus.fireEvent(event);
		this.dispatcher.execute(action, new AsyncCallback<CambiaStatoPraticaModulisticaResult>() {

			@Override
			public void onSuccess(CambiaStatoPraticaModulisticaResult result) {
				ShowAppLoadingEvent.fire(WorklistPraticaModulisticaPresenter.this, false);
				if (result.isError()) {
					ShowAppLoadingEvent.fire(WorklistPraticaModulisticaPresenter.this, false);
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(result.getErrorMsg());
					eventBus.fireEvent(event);

				} else {
					ShowAppLoadingEvent.fire(WorklistPraticaModulisticaPresenter.this, false);
					for (PraticaModulisticaDTO pm : result.getPraticheOperazioneMassiva()) {
						getView().aggiornaRiga(pm);

						praticheDB.update(pm.getClientID(), pm, sitemapMenu.containsLink(pm.getClientID()));
					}

					impostaAbilitazioniPulsantiera();
					getView().refreshGrid();
					// updateRigheEspanse();
					// v.rilanciaRicerca();
				}

			}

			@Override
			public void onFailure(Throwable caught) {
				ShowAppLoadingEvent.fire(WorklistPraticaModulisticaPresenter.this, false);
				ShowMessageEvent event = new ShowMessageEvent();
				event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
				eventBus.fireEvent(event);
			}
		});

	}

	@Override
	@ProxyEvent
	public void onChiudiTipoProtocollazionePage(ChiudiProtocollazionePageEvent event) {
		this.mostraDettaglio(event.getId());

	}

	@Override
	@ProxyEvent
	public void onChiudiDettaglioAllegato(ChiudiDettaglioAllegatoEvent event) {
		if (placeManager.getCurrentPlaceRequest().getNameToken().equals(NameTokens.worklistpraticamodulistica)) {
			ShowAppLoadingEvent.fire(this, false);
			placeManager.revealCurrentPlace();
		}
	}

	private class EspandiRigaPraticaModulisticaEvent implements EspandiRigaEventListener {

		@Override
		public void onEspandiRiga(final String clientID, TipologiaPratica tipologiaPratica, boolean isEspansa) {
			int clientWidth = Window.getClientWidth();
			if (clientWidth > ConsolePecConstants.MIN_DESKTOP_WIDTH_PIXELS) {
				if (!isEspansa) {

					CaricaPraticaModulisticaAction recuperaDettaglioPratica = new CaricaPraticaModulisticaAction(clientID, TipologiaCaricamento.getTipologiaCaricamento(sitemapMenu.containsLink(clientID)));
					/* reset dei messaggi di errore */
					ShowMessageEvent event = new ShowMessageEvent();
					event.setMessageDropped(true);
					eventBus.fireEvent(event);
					WorklistPraticaModulisticaPresenter.this.dispatcher.execute(recuperaDettaglioPratica, new AsyncCallback<CaricaPraticaModulisticaActionResult>() {

						@Override
						public void onSuccess(CaricaPraticaModulisticaActionResult result) {
							if (!result.getError()) {
								PraticaDTO pratica = result.getPratica();
								praticheDB.insertOrUpdate(clientID, pratica, sitemapMenu.containsLink(clientID));
								ApertoDettaglioEvent.fire(WorklistPraticaModulisticaPresenter.this, pratica);
								getView().espandiRiga(clientID, pratica);
							} else {
								ShowAppLoadingEvent.fire(WorklistPraticaModulisticaPresenter.this, false);
								ShowMessageEvent event = new ShowMessageEvent();
								event.setErrorMessage(result.getMessError());
								eventBus.fireEvent(event);
							}

						}

						@Override
						public void onFailure(Throwable caught) {
							ShowAppLoadingEvent.fire(WorklistPraticaModulisticaPresenter.this, false);
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
				WorklistPraticaModulisticaPresenter.this.mostraDettaglio(clientID);
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
			action.setParametriFissi(worklistConfiguration.getParametriFissiWorklist());
			action.setSoloWorklist(true);
			action.setNomeModulo(nomeModulo);
			action.getValoriModulo().add(worklistModulistica);
			Set<ConstraintViolation<CercaPratiche>>  violations = getView().formRicercaToCercaPratiche(action);
			if(violations.size() == 0) {
				ricercaAdapter.startRicerca(action, callback);
			}
		}

	}

	// /* Ricerca arriva dall'utente */
	// private class RicercaCommand implements Command {
	//
	// @Override
	// public void execute() {
	// Set<ConstraintViolation<CercaPratiche>> violations = getView().formRicercaToCercaPratiche(new CercaPratiche());
	// if (violations.size() == 0) {
	// WorklistPecInPresenter.this.strategy.restartSearchDatiGrid();
	// }
	// }
	//
	// }

	private class SelezionePraticaCommand implements Command {
		@Override
		public void execute() {
			impostaAbilitazioniPulsantiera();
		}

	}

	private class ArchiviaCommandMassivo implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, Set<String>> {

		@Override
		public Void exe(Set<String> t) {
			archivia(getView().getIDRigheSelezionate());
			return null;
		}

	}

	private class RiportaInGestioneCommandMassivo implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, Set<String>> {

		@Override
		public Void exe(Set<String> t) {
			riportaInGestione(t);
			return null;
		}
	}

	private class EliminaCommandMassivo implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, Set<String>> {

		@Override
		public Void exe(Set<String> t) {
			elimina(t);
			return null;
		}

	}

	public class AssegnaCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Object, Set<String>> {
		@Override
		public Void exe(Set<String> ids) {
			GoToAssegnaFromWorklistPraticaModulisticaEvent event = new GoToAssegnaFromWorklistPraticaModulisticaEvent(ids);
			eventBus.fireEvent(event);
			return null;
		}

	}

}
