package it.eng.portlet.consolepec.gwt.client.presenter.pec;

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
import com.gwtplatform.mvp.shared.proxy.PlaceRequest.Builder;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaWorklist;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaWorklist.Counter;
import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.portlet.consolepec.gwt.client.command.RicercaPraticheServerAdapter;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaEmaiInlLoaded;
import it.eng.portlet.consolepec.gwt.client.event.ApertoDettaglioEvent;
import it.eng.portlet.consolepec.gwt.client.event.ChiudiDettaglioAllegatoEvent;
import it.eng.portlet.consolepec.gwt.client.event.ChiudiDettaglioAllegatoEvent.ChiudiDettaglioAllegatoHandler;
import it.eng.portlet.consolepec.gwt.client.event.ChiudiProtocollazionePageEvent;
import it.eng.portlet.consolepec.gwt.client.event.GoToPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.event.MostraDettaglioAllegatoEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.WorklistHandler.WorklistHandlerCallback;
import it.eng.portlet.consolepec.gwt.client.place.NameTokens;
import it.eng.portlet.consolepec.gwt.client.place.NameTokensParams;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.RicercaCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.event.GoToAssegnaFromWorklistPecInEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.pec.event.MostraWorklistPecInEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.pec.event.MostraWorklistPecInEvent.MostraWorklistPecInHandler;
import it.eng.portlet.consolepec.gwt.client.widget.GroupSuggestBoxProtocollazione;
import it.eng.portlet.consolepec.gwt.client.widget.SitemapMenu;
import it.eng.portlet.consolepec.gwt.client.worklist.AbstractWorklistStrategy;
import it.eng.portlet.consolepec.gwt.client.worklist.PecInWorklistStrategy;
import it.eng.portlet.consolepec.gwt.client.worklist.RigaEspansaPecInStrategy;
import it.eng.portlet.consolepec.gwt.client.worklist.WorklistStrategy;
import it.eng.portlet.consolepec.gwt.client.worklist.WorklistStrategy.EspandiRigaEventListener;
import it.eng.portlet.consolepec.gwt.client.worklist.WorklistStrategy.RicercaCallback;
import it.eng.portlet.consolepec.gwt.shared.Base64Utils;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.UriMapping;
import it.eng.portlet.consolepec.gwt.shared.action.CercaPratiche;
import it.eng.portlet.consolepec.gwt.shared.action.GestionePresaInCaricoFascicoloAction;
import it.eng.portlet.consolepec.gwt.shared.action.GestionePresaInCaricoFascicoloActionResult;
import it.eng.portlet.consolepec.gwt.shared.action.pec.CambiaStatoPecInAction;
import it.eng.portlet.consolepec.gwt.shared.action.pec.CambiaStatoPecInActionResult;
import it.eng.portlet.consolepec.gwt.shared.action.pec.CaricaPraticaEmailInAction;
import it.eng.portlet.consolepec.gwt.shared.action.pec.CaricaPraticaEmailInActionResult;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.ColonnaWorklist;
import it.eng.portlet.consolepec.gwt.shared.model.PecInDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO.TipoPresaInCarico;
import it.eng.portlet.consolepec.gwt.shared.model.TipologiaCaricamento;

public class WorklistPecInPresenter extends Presenter<WorklistPecInPresenter.MyView, WorklistPecInPresenter.MyProxy> implements ChiudiProtocollazionePageEvent.ChiudiTipoProtocollazionePageHandler, ChiudiDettaglioAllegatoHandler, MostraWorklistPecInHandler {

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

		public void setGruppiAbilitati(List<AnagraficaRuolo> gruppiAbilitati);

		public void impostaTitolo(String titolo);

		public void setFormRicerca(final AnagraficaWorklist worklistConfiguration, RicercaCommand ricercaCommand, GroupSuggestBoxProtocollazione groupSuggest);
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
	private String emailAccount;
	private AnagraficaWorklist worklistConfiguration;
	private ProfilazioneUtenteHandler profilazioneUtenteHandler;
	private RicercaCommand ricercaCommand;

	@ProxyCodeSplit
	@NameToken(NameTokens.worklistpecin)
	public interface MyProxy extends ProxyPlace<WorklistPecInPresenter> {/**/}

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
				if (worklistConfiguration == null) {
					throw new IllegalStateException("Non è stata trovata nessuna worklist.");
				}

				getView().impostaTitolo(worklistConfiguration.getTitoloWorklist());

				if (firstReveal) {
					strategy = new PecInWorklistStrategy();
					strategy.setRigaEspansaStrategy(new RigaEspansaPecInStrategy(new MostraDettaglioCommand(), new ArchiviaCommand(), new EliminaCommand(), new MostraFormCreaFascicoloCommand(),
							new DownloadAllegatoCommand(), new MostraDettaglioAllegatoCommand()));
					strategy.addEspandiRigaEventListener(new EspandiRigaPecINEvent());
					strategy.setRicercaEventListener(new RicercaEventListener());
					strategy.setPraticheDB(praticheDB);
					strategy.setSitemapMenu(sitemapMenu);
					strategy.setEventBus(eventBus);

					ricercaCommand = new RicercaCommand(eventBus, (AbstractWorklistStrategy) strategy, new HashSet<ConstraintViolation<CercaPratiche>>());
				}

				getView().setFormRicerca(worklistConfiguration, ricercaCommand, gruppoSuggest);
				impostaElencoGruppi();

				if (firstReveal) {
					getView().init(strategy, new SelezionePraticaCommand());

					firstReveal = false;
				}

				// il setActiveVoice funziona perchè in GestioneLinkDaLavorare il getIdLink mi ritorna il customTitoloLink: verdere se si riesce a trovare qualcosa di meglio...
				sitemapMenu.setActiveVoce(emailAccount);
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
	public WorklistPecInPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, final DispatchAsync dispatcher, final PecInPraticheDB pecInDB,
			final RicercaPraticheServerAdapter ricercaAdapter, final SitemapMenu sitemapMenu, final GroupSuggestBoxProtocollazione gruppoSuggest, final PlaceManager placeManager,
			ProfilazioneUtenteHandler profilazioneUtenteHandler) {
		super(eventBus, view, proxy);
		this.dispatcher = dispatcher;
		this.praticheDB = pecInDB;
		this.placeManager = placeManager;
		this.eventBus = eventBus;
		this.ricercaAdapter = ricercaAdapter;
		this.sitemapMenu = sitemapMenu;
		this.gruppoSuggest = gruppoSuggest;
		this.profilazioneUtenteHandler = profilazioneUtenteHandler;

	}

	private void mostraDettaglio(String id) {
		MainPresenter.Place place = new MainPresenter.Place();
		place.setToken(NameTokens.dettagliopecin);
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

		final List<PecInDTO> temp = new ArrayList<PecInDTO>();
		for (String sel : listIdSelezionateselezionate) {
			praticheDB.getPecInByPath(sel, sitemapMenu.containsLink(sel), new PraticaEmaiInlLoaded() {

				@Override
				public void onPraticaLoaded(PecInDTO pec) {
					// configuraPulsanti(pec);
					temp.add(pec);
					if (listIdSelezionateselezionate.size() == temp.size()) {
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

	protected void configuraPulsanti(List<PecInDTO> listPec) {
		boolean archiviabile = true;
		boolean riassegnaAbilitato = true;
		boolean eliminabile = true;
		boolean riportaInGestione = true;

		for (PecInDTO pec : listPec) {
			archiviabile &= pec.isArchiviabile();
			riassegnaAbilitato &= pec.isRiassegnaAbilitato();
			eliminabile &= pec.isEliminabile();
			riportaInGestione &= pec.isRiportaInGestioneAbilitato();

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

	public void archivia(Set<String> ids) {
		CambiaStatoPecInAction action = new CambiaStatoPecInAction(ids, CambiaStatoPecInAction.Azione.ARCHIVIAMASSIVA);
		lanciaOperazioneMassiva(action);

	}

	public void elimina(Set<String> ids) {
		CambiaStatoPecInAction action = new CambiaStatoPecInAction(ids, CambiaStatoPecInAction.Azione.ELIMINAMASSIVO);
		lanciaOperazioneMassiva(action);

	}

	public void riportaInGestione(Set<String> ids) {
		CambiaStatoPecInAction action = new CambiaStatoPecInAction(ids, CambiaStatoPecInAction.Azione.RIPORTAINGESTIONEMASSIVO);
		lanciaOperazioneMassiva(action);
	}

	private void lanciaOperazioneMassiva(CambiaStatoPecInAction action) {
		ShowAppLoadingEvent.fire(this, true);
		/* reset dei messaggi di errore */
		ShowMessageEvent event = new ShowMessageEvent();
		event.setMessageDropped(true);
		eventBus.fireEvent(event);
		this.dispatcher.execute(action, new AsyncCallback<CambiaStatoPecInActionResult>() {

			@Override
			public void onSuccess(CambiaStatoPecInActionResult result) {
				ShowAppLoadingEvent.fire(WorklistPecInPresenter.this, false);
				if (result.getIsError()) {
					ShowAppLoadingEvent.fire(WorklistPecInPresenter.this, false);
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(result.getMessErr());
					eventBus.fireEvent(event);

				} else {
					ShowAppLoadingEvent.fire(WorklistPecInPresenter.this, false);
					for (PecInDTO pec : result.getDettagliRighe()) {
						getView().aggiornaRiga(pec);

						praticheDB.update(pec.getClientID(), pec, sitemapMenu.containsLink(pec.getClientID()));
					}

					impostaAbilitazioniPulsantiera();
					// updateRigheEspanse();
					// v.rilanciaRicerca();
				}

			}

			@Override
			public void onFailure(Throwable caught) {
				ShowAppLoadingEvent.fire(WorklistPecInPresenter.this, false);
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
		if (placeManager.getCurrentPlaceRequest().getNameToken().equals(NameTokens.worklistpecin)) {
			ShowAppLoadingEvent.fire(this, false);
			placeManager.revealCurrentPlace();
		}
	}

	/* classi interne */

	private class DownloadAllegatoCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, AllegatoDTO> {

		@Override
		public Void exe(final AllegatoDTO allegato) {
			SafeUri uri = UriMapping.generaDownloadAllegatoServletURL(allegato.getClientID(), allegato);
			getView().sendDownload(uri);
			return null;
		}
	}

	private class EspandiRigaPecINEvent implements EspandiRigaEventListener {

		@Override
		public void onEspandiRiga(final String clientID, TipologiaPratica tipologiaPratica, boolean isEspansa) {
			int clientWidth = Window.getClientWidth();
			if (clientWidth > ConsolePecConstants.MIN_DESKTOP_WIDTH_PIXELS) {
				if (!isEspansa) {

					CaricaPraticaEmailInAction recuperaDettaglioPratica = new CaricaPraticaEmailInAction(clientID, TipologiaCaricamento.getTipologiaCaricamento(sitemapMenu.containsLink(clientID)));
					/* reset dei messaggi di errore */
					ShowMessageEvent event = new ShowMessageEvent();
					event.setMessageDropped(true);
					eventBus.fireEvent(event);
					WorklistPecInPresenter.this.dispatcher.execute(recuperaDettaglioPratica, new AsyncCallback<CaricaPraticaEmailInActionResult>() {

						@Override
						public void onSuccess(CaricaPraticaEmailInActionResult result) {
							if (!result.isError()) {
								PraticaDTO pratica = result.getDettaglio();
								praticheDB.insertOrUpdate(clientID, pratica, sitemapMenu.containsLink(clientID));
								ApertoDettaglioEvent.fire(WorklistPecInPresenter.this, pratica);
								getView().espandiRiga(clientID, pratica);
							} else {
								ShowAppLoadingEvent.fire(WorklistPecInPresenter.this, false);
								ShowMessageEvent event = new ShowMessageEvent();
								event.setErrorMessage(result.getErrorMessage());
								eventBus.fireEvent(event);
							}

						}

						@Override
						public void onFailure(Throwable caught) {
							ShowAppLoadingEvent.fire(WorklistPecInPresenter.this, false);
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
				WorklistPecInPresenter.this.mostraDettaglio(clientID);
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
			Set<ConstraintViolation<CercaPratiche>> violations = getView().formRicercaToCercaPratiche(action);

			action.setParametriFissi(worklistConfiguration.getParametriFissiWorklist());
			if (violations.size() == 0) {
				ricercaAdapter.startRicerca(action, callback);
			}
		}

	}

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

	private class ArchiviaCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, String> {

		@Override
		public Void exe(String idPratica) {
			Set<String> ids = new HashSet<String>();
			ids.add(idPratica);
			archivia(ids);
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

	private class EliminaCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, String> {

		@Override
		public Void exe(String idPratica) {
			Set<String> ids = new HashSet<String>();
			ids.add(idPratica);
			elimina(ids);
			return null;
		}

	}

	public class AssegnaCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Object, Set<String>> {
		@Override
		public Void exe(Set<String> ids) {
			GoToAssegnaFromWorklistPecInEvent event = new GoToAssegnaFromWorklistPecInEvent(ids, worklistConfiguration.getNome());
			eventBus.fireEvent(event);
			return null;
		}

	}

	private class MostraDettaglioCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, String> {

		@Override
		public Void exe(String idPratica) {
			mostraDettaglio(idPratica);
			return null;
		}
	}

	private class MostraDettaglioAllegatoCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, AllegatoDTO> {
		@Override
		public Void exe(AllegatoDTO allegato) {
			MostraDettaglioAllegatoEvent.fire(WorklistPecInPresenter.this, allegato.getClientID(), allegato);
			return null;
		}
	}

	private abstract class PrendiInCaricoPecInCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, PecInDTO> {

		@Override
		public Void exe(final PecInDTO pecInDTO) {

			// se arrivo a questo punto se la mail è già in carico a qualcuno è sempre in carico a me
			if (pecInDTO.getTipoPresaInCarico().equals(TipoPresaInCarico.NESSUNO)) {

				GestionePresaInCaricoFascicoloAction action = new GestionePresaInCaricoFascicoloAction();
				action.setClientID(pecInDTO.getClientID());
				ShowAppLoadingEvent.fire(WorklistPecInPresenter.this, true);
				/* reset dei messaggi di errore */
				ShowMessageEvent event = new ShowMessageEvent();
				event.setMessageDropped(true);
				eventBus.fireEvent(event);
				dispatcher.execute(action, new AsyncCallback<GestionePresaInCaricoFascicoloActionResult>() {

					@Override
					public void onFailure(Throwable caught) {
						ShowAppLoadingEvent.fire(WorklistPecInPresenter.this, false);
						ShowMessageEvent event = new ShowMessageEvent();
						event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
						eventBus.fireEvent(event);
					}

					@Override
					public void onSuccess(GestionePresaInCaricoFascicoloActionResult result) {
						ShowAppLoadingEvent.fire(WorklistPecInPresenter.this, false);
						if (!result.isError()) {
							ShowMessageEvent event = new ShowMessageEvent();
							event.setMessageDropped(true);
							eventBus.fireEvent(event);
							praticheDB.update(pecInDTO.getClientID(), result.getPraticaDTO(), true);
							handlePraticaInCarico(result.getPraticaDTO());
							ShowAppLoadingEvent.fire(WorklistPecInPresenter.this, false);
						} else {
							ShowAppLoadingEvent.fire(WorklistPecInPresenter.this, false);
							ShowMessageEvent event = new ShowMessageEvent();
							event.setErrorMessage(result.getErrorMsg());
							eventBus.fireEvent(event);
						}
					}
				});
			} else {
				handlePraticaInCarico(pecInDTO);
			}
			return null;
		}

		public abstract void handlePraticaInCarico(PraticaDTO pratica);
	}

	private class MostraFormCreaFascicoloCommand extends PrendiInCaricoPecInCommand {

		@Override
		public void handlePraticaInCarico(PraticaDTO pratica) {
			MainPresenter.Place place = new MainPresenter.Place();
			place.setToken(NameTokens.creafascicolo);
			place.addParam(NameTokensParams.idPratica, pratica.getClientID());
			GoToPlaceEvent goToPlaceEvent = new GoToPlaceEvent(place);
			eventBus.fireEvent(goToPlaceEvent);
		}

	}

	@Override
	@ProxyEvent
	public void onMostraWorklistPecInHandler(MostraWorklistPecInEvent event) {

		if (firstReveal || ricercaCommand == null) {
			Builder builder = new PlaceRequest.Builder();
			builder.nameToken(NameTokens.worklistpecin);
			builder.with(NameTokensParams.identificativoWorklist, Base64Utils.URLencodeAlfrescoPath(event.getIdentificativoWorklist()));
			prepareFromRequest(builder.build());

		} else {
			getView().resetSelezioni();
			revealInParent();
			ricercaCommand.refreshDatiGrid();
		}
	}

}
