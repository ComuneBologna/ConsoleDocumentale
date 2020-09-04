package it.eng.portlet.consolepec.gwt.client.presenter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.google.common.base.Strings;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest.Builder;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.datigenerici.PreferenzeUtente.PreferenzeRiassegnazione;
import it.eng.cobo.consolepec.commons.profilazione.Settore;
import it.eng.cobo.consolepec.util.validation.ValidationUtilities;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaFascicoloLoaded;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.event.cartellafirma.AssegnaFascicoliFineEvent;
import it.eng.portlet.consolepec.gwt.client.event.cartellafirma.AssegnaFascicoliInizioEvent;
import it.eng.portlet.consolepec.gwt.client.event.cartellafirma.AssegnaFascicoliInizioEvent.AssegnaFascicoliInizioHandler;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.place.NameTokens;
import it.eng.portlet.consolepec.gwt.client.place.NameTokensParams;
import it.eng.portlet.consolepec.gwt.client.presenter.event.GoToAssegnaFromDettaglioFascicoloEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.event.GoToAssegnaFromDettaglioFascicoloEvent.GoToAssegnaFromDettaglioFascicoloHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.event.GoToAssegnaFromDettaglioPecInEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.event.GoToAssegnaFromDettaglioPecInEvent.GoToAssegnaFromDettaglioPecInHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.event.GoToAssegnaFromDettaglioPraticaModulisticaEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.event.GoToAssegnaFromDettaglioPraticaModulisticaEvent.GoToAssegnaFromDettaglioPraticaModulisticaHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.event.GoToAssegnaFromWorklistFascicoloEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.event.GoToAssegnaFromWorklistFascicoloEvent.GoToAssegnaFromWorklistFascicoloHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.event.GoToAssegnaFromWorklistPecInEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.event.GoToAssegnaFromWorklistPecInEvent.GoToAssegnaFromWorklistPecInHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.event.GoToAssegnaFromWorklistPraticaModulisticaEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.event.GoToAssegnaFromWorklistPraticaModulisticaEvent.GoToAssegnaFromWorklistPraticaModulisticaHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.MostraWorklistFascicoliEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.pec.event.MostraWorklistPecInEvent;
import it.eng.portlet.consolepec.gwt.client.widget.SitemapMenu;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.assegna.fasciolo.RiassegnaFascicolo;
import it.eng.portlet.consolepec.gwt.shared.action.assegna.fasciolo.RiassegnaFascicoloResult;
import it.eng.portlet.consolepec.gwt.shared.action.assegna.modulo.RiassegnaModulo;
import it.eng.portlet.consolepec.gwt.shared.action.assegna.modulo.RiassegnaModuloResult;
import it.eng.portlet.consolepec.gwt.shared.action.assegna.pec.RiassegnaPecIn;
import it.eng.portlet.consolepec.gwt.shared.action.assegna.pec.RiassegnaPecInResult;
import it.eng.portlet.consolepec.gwt.shared.action.cartellafirma.InformazioniRiassegnazioneFascicoliTaskFirmaDTO;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CercaProcedimentiCollegati;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CercaProcedimentiCollegatiResult;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.StatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PecInDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaModulisticaDTO;

public class GruppiPresenter extends Presenter<GruppiPresenter.MyView, GruppiPresenter.MyProxy> implements AssegnaFascicoliInizioHandler, GoToAssegnaFromWorklistPecInHandler, GoToAssegnaFromDettaglioPecInHandler, GoToAssegnaFromDettaglioFascicoloHandler, GoToAssegnaFromDettaglioPraticaModulisticaHandler, GoToAssegnaFromWorklistFascicoloHandler, GoToAssegnaFromWorklistPraticaModulisticaHandler {

	private final DispatchAsync dispatcher;
	private final PecInPraticheDB praticheDB;
	private Set<String> ids = new TreeSet<String>();
	private final EventBus eventBus;
	private final SitemapMenu sitemapMenu;
	private Command indietroCommand;
	private Command assegnaCommand;
	private PlaceManager placeManager;
	private String identificativoWorklistFascicolo;
	private String identificativoWorklistPecIn;
	private ConfigurazioniHandler configurazioniHandler;
	private ProfilazioneUtenteHandler profilazioneUtenteHandler;

	public interface MyView extends View {

		public Button getConfermaCambioGruppo();

		public Button getAnnullaButton();

		public void initStatiFascicolo(List<StatoDTO> stati, StatoDTO current);

		public void setStatiFascicoloVisible(boolean isVisible);

		public boolean isStatiFascicoloVisible();

		public StatoDTO getStatoFascicoloSelected();

		public String getNote();

		void mostraAvvisoProcedimenti(boolean mosta);

		void impostaAvvisoProcedimenti(String message);

		void setNoteVisible(boolean isVisible);

		void configuraIndirizziNotifica(boolean visible, List<String> indirizziNotifica);

		List<String> getIndirizziNotifica();

		void setIndirizzoNotifica(String indirizzo);

		boolean isRicordaSceltaEnabled();

		void setRicordaScelta(boolean ricordaScelta);

		void clearForm();

		List<String> getInputListWidgetDestinatari();

		public String getTextFromInputListWidgetDestinatari();

		Settore getSettoreSelezionato();

		AnagraficaRuolo getAnagraficaRuoloSelezionata();

		String getOperatoreSelezionato();

		void showErrors(List<String> errors);

		void showWidget(Settore settore, AnagraficaRuolo anagraficaRuolo);

		void registerRuoliSelectionHandler();

	}

	@ProxyCodeSplit
	public interface MyProxy extends Proxy<GruppiPresenter> {}

	@Inject
	public GruppiPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, final DispatchAsync dispatcher, PecInPraticheDB db, final SitemapMenu sitemapMenu,
			final PlaceManager placeManager, ConfigurazioniHandler configurazioniHandler, ProfilazioneUtenteHandler profilazioneUtenteHandler) {
		super(eventBus, view, proxy);
		this.praticheDB = db;
		this.dispatcher = dispatcher;
		this.eventBus = eventBus;
		this.sitemapMenu = sitemapMenu;
		this.placeManager = placeManager;
		this.configurazioniHandler = configurazioniHandler;
		this.profilazioneUtenteHandler = profilazioneUtenteHandler;
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPresenter.TYPE_SetMainContent, this);
	}

	@Override
	protected void onBind() {
		super.onBind();

		getView().registerRuoliSelectionHandler();
		getView().getConfermaCambioGruppo().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				List<String> errors = controllaForm();

				if (errors.isEmpty()) {
					assegnaCommand.execute();

				} else {
					getView().showErrors(errors);
				}
			}
		});

		getView().getAnnullaButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				indietroCommand.execute();
			}
		});
	}

	private List<String> controllaForm() {

		List<String> errors = new ArrayList<String>();

		if (getView().getAnagraficaRuoloSelezionata() == null) {
			errors.add("Selezionare almeno un gruppo");
		}

		Set<String> set = new HashSet<String>(getView().getInputListWidgetDestinatari());
		if (set.size() < getView().getInputListWidgetDestinatari().size()) {
			errors.add("Ci sono duplicati tra gli indirizzi notifica.");
		}

		String textFromInputListWidgetDestinatari = getView().getTextFromInputListWidgetDestinatari();
		if (!Strings.isNullOrEmpty(textFromInputListWidgetDestinatari) && !ValidationUtilities.validateEmailAddress(textFromInputListWidgetDestinatari)) {
			errors.add("Indirizzo di notifica non valido.");
		}

		return errors;
	}

	@Override
	protected void onHide() {
		super.onHide();
		getView().impostaAvvisoProcedimenti(null);
		getView().mostraAvvisoProcedimenti(false);
		getView().clearForm();
		ShowMessageEvent event = new ShowMessageEvent();
		event.setMessageDropped(true);
		eventBus.fireEvent(event);
	}

	private void initWidgetRuoli() {
		ShowMessageEvent event = new ShowMessageEvent();
		event.setMessageDropped(true);
		eventBus.fireEvent(event);

		getView().clearForm();

		PreferenzeRiassegnazione preferenzeRiassegnazione = profilazioneUtenteHandler.getPreferenzeRiassegnazioneUtente();

		if (preferenzeRiassegnazione != null) {
			Settore settore = configurazioniHandler.getSettore(preferenzeRiassegnazione.getSettore());
			AnagraficaRuolo ar = configurazioniHandler.getAnagraficaRuolo(preferenzeRiassegnazione.getRuolo());
			getView().showWidget(settore, ar);
			getView().setRicordaScelta(preferenzeRiassegnazione.isRicordaScelta());

		} else {
			getView().showWidget(null, null);
		}
	}

	private void dropMessage() {
		ShowMessageEvent event = new ShowMessageEvent();
		event.setMessageDropped(true);
		eventBus.fireEvent(event);
	}

	@Override
	@ProxyEvent
	public void onGoToAssegnaFromWorklistPecIn(GoToAssegnaFromWorklistPecInEvent event) {
		identificativoWorklistPecIn = event.getIdentificativoWorklist();
		ids = new TreeSet<String>(event.getPecinid());
		initWidgetRuoli();
		indietroCommand = new GoToWorklistPecIn();
		assegnaCommand = new AssegnaPecInCommand();
		getView().setStatiFascicoloVisible(false);
		getView().setNoteVisible(true);
		getView().configuraIndirizziNotifica(true,
				profilazioneUtenteHandler.getPreferenzeRiassegnazioneUtente() != null ? profilazioneUtenteHandler.getPreferenzeRiassegnazioneUtente().getIndirizziNotifica() : null);
		revealInParent();
	}

	@Override
	public void onGoToAssegnaFromWorklistPraticaModulistica(GoToAssegnaFromWorklistPraticaModulisticaEvent event) {
		ids = new TreeSet<String>(event.getPraticheModulistiche());
		initWidgetRuoli();
		indietroCommand = new GoToWorklistPraticaModulistica();
		assegnaCommand = new AssegnaPraticaModulisticaCommand();
		getView().setStatiFascicoloVisible(false);
		getView().configuraIndirizziNotifica(true,
				profilazioneUtenteHandler.getPreferenzeRiassegnazioneUtente() != null ? profilazioneUtenteHandler.getPreferenzeRiassegnazioneUtente().getIndirizziNotifica() : null);

		revealInParent();
	}

	@Override
	@ProxyEvent
	public void onGoToAssegnaFromDettaglioPecIn(GoToAssegnaFromDettaglioPecInEvent event) {
		ids = new TreeSet<String>();
		ids.add(event.getPecInId());
		initWidgetRuoli();
		indietroCommand = new GoToDettaglioPecIn();
		assegnaCommand = new AssegnaPecInCommand();
		getView().setStatiFascicoloVisible(false);
		getView().setNoteVisible(true);
		getView().configuraIndirizziNotifica(true,
				profilazioneUtenteHandler.getPreferenzeRiassegnazioneUtente() != null ? profilazioneUtenteHandler.getPreferenzeRiassegnazioneUtente().getIndirizziNotifica() : null);

		revealInParent();
	}

	@Override
	@ProxyEvent
	public void onGoToAssegnaFromDettaglioPraticaModulistica(GoToAssegnaFromDettaglioPraticaModulisticaEvent event) {
		ids = new TreeSet<String>();
		ids.add(event.getPraticaModulisticaId());
		initWidgetRuoli();
		indietroCommand = new GoToDettaglioPraticaModulistica();
		assegnaCommand = new AssegnaPraticaModulisticaCommand();
		getView().setStatiFascicoloVisible(false);
		getView().configuraIndirizziNotifica(false,
				profilazioneUtenteHandler.getPreferenzeRiassegnazioneUtente() != null ? profilazioneUtenteHandler.getPreferenzeRiassegnazioneUtente().getIndirizziNotifica() : null);
		revealInParent();
	}

	@Override
	@ProxyEvent
	public void onGoToAssegnaFromDettaglioFascicolo(GoToAssegnaFromDettaglioFascicoloEvent event) {
		ids = new TreeSet<String>();
		ids.add(event.getIdFascicolo());
		initWidgetRuoli();
		indietroCommand = new GoToDettaglioFascicolo();
		assegnaCommand = new AssegnaFascicoloCommand();
		getView().setStatiFascicoloVisible(true);

		praticheDB.getFascicoloByPath(event.getIdFascicolo(), false, new PraticaFascicoloLoaded() {

			@Override
			public void onPraticaLoaded(FascicoloDTO fascicolo) {

				HashSet<StatoDTO> stati = new HashSet<StatoDTO>();

				switch (fascicolo.getStato()) {
				case IN_GESTIONE:
					if (fascicolo.isAffissioneAbilitato()) {
						stati.add(StatoDTO.IN_AFFISSIONE);
					}
					stati.add(StatoDTO.ARCHIVIATO);
					stati.add(StatoDTO.IN_VISIONE);
					break;
				case IN_VISIONE:
					if (fascicolo.isAffissioneAbilitato()) {
						stati.add(StatoDTO.IN_AFFISSIONE);
					}
					stati.add(StatoDTO.ARCHIVIATO);
					stati.add(StatoDTO.IN_GESTIONE);
					break;
				case IN_AFFISSIONE:
					stati.add(StatoDTO.ARCHIVIATO);
					stati.add(StatoDTO.IN_GESTIONE);
					stati.add(StatoDTO.IN_VISIONE);
					break;
				case ARCHIVIATO:
					if (fascicolo.isRiportaInGestioneAbilitato()) {
						stati.add(StatoDTO.IN_GESTIONE);
					}
					break;
				default:
					break;
				}
				stati.add(fascicolo.getStato());

				getView().initStatiFascicolo(new ArrayList<StatoDTO>(stati), fascicolo.getStato());
				if (fascicolo.getProcedimenti().size() > 0) {
					getView().impostaAvvisoProcedimenti(ConsolePecConstants.AVVISO_PROCEDIMENTI);
					getView().mostraAvvisoProcedimenti(true);
				}

				getView().configuraIndirizziNotifica(true,
						profilazioneUtenteHandler.getPreferenzeRiassegnazioneUtente() != null ? profilazioneUtenteHandler.getPreferenzeRiassegnazioneUtente().getIndirizziNotifica() : null);

			}

			@Override
			public void onPraticaError(String error) {
				ShowMessageEvent event = new ShowMessageEvent();
				event.setErrorMessage(error);
				eventBus.fireEvent(event);
			}
		});

		revealInParent();
	}

	@Override
	@ProxyEvent
	public void onGoToAssegnaFromWorklistFascicolo(GoToAssegnaFromWorklistFascicoloEvent event) {
		ids = new TreeSet<String>(event.getIds());
		identificativoWorklistFascicolo = event.getIdentificativoWorklist();
		ShowMessageEvent event2 = new ShowMessageEvent();
		event2.setMessageDropped(true);
		eventBus.fireEvent(event2);
		CercaProcedimentiCollegati action = new CercaProcedimentiCollegati(ids);
		ShowAppLoadingEvent.fire(GruppiPresenter.this, true);
		dispatcher.execute(action, new AsyncCallback<CercaProcedimentiCollegatiResult>() {

			@Override
			public void onFailure(Throwable arg0) {
				ShowAppLoadingEvent.fire(GruppiPresenter.this, false);
				ShowMessageEvent showMessageEvent = new ShowMessageEvent();
				showMessageEvent.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
				getEventBus().fireEvent(showMessageEvent);
			}

			@Override
			public void onSuccess(CercaProcedimentiCollegatiResult result) {
				ShowAppLoadingEvent.fire(GruppiPresenter.this, false);
				if (result.isError()) {
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(result.getErrorMsg());
					eventBus.fireEvent(event);
				} else {
					if (result.getNumProcedimenti() > 0) {
						getView().impostaAvvisoProcedimenti(ConsolePecConstants.AVVISO_PROCEDIMENTI);
						getView().mostraAvvisoProcedimenti(true);
					}
					initWidgetRuoli();
					indietroCommand = new GoToWorklistFascicolo();
					assegnaCommand = new AssegnaFascicoloCommand();
					getView().setStatiFascicoloVisible(false);
					getView().setNoteVisible(true);
					getView().configuraIndirizziNotifica(true,
							profilazioneUtenteHandler.getPreferenzeRiassegnazioneUtente() != null ? profilazioneUtenteHandler.getPreferenzeRiassegnazioneUtente().getIndirizziNotifica() : null);
					revealInParent();

				}
			}
		});

	}

	@Override
	@ProxyEvent
	public void onGoToAssegnaFascicoli(final AssegnaFascicoliInizioEvent event) {

		ids = new TreeSet<String>(event.getClientIds());

		ShowMessageEvent event2 = new ShowMessageEvent();
		event2.setMessageDropped(true);
		eventBus.fireEvent(event2);
		CercaProcedimentiCollegati action = new CercaProcedimentiCollegati(ids);
		ShowAppLoadingEvent.fire(GruppiPresenter.this, true);
		dispatcher.execute(action, new AsyncCallback<CercaProcedimentiCollegatiResult>() {

			@Override
			public void onFailure(Throwable arg0) {
				ShowAppLoadingEvent.fire(GruppiPresenter.this, false);
				ShowMessageEvent showMessageEvent = new ShowMessageEvent();
				showMessageEvent.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
				getEventBus().fireEvent(showMessageEvent);
			}

			@Override
			public void onSuccess(CercaProcedimentiCollegatiResult result) {
				ShowAppLoadingEvent.fire(GruppiPresenter.this, false);
				if (result.isError()) {
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(result.getErrorMsg());
					eventBus.fireEvent(event);
				} else {
					if (result.getNumProcedimenti() > 0) {
						getView().impostaAvvisoProcedimenti(ConsolePecConstants.AVVISO_PROCEDIMENTI);
						getView().mostraAvvisoProcedimenti(true);
					}
					initWidgetRuoli();

					indietroCommand = new Command() {

						@Override
						public void execute() {
							AssegnaFascicoliFineEvent.fire(GruppiPresenter.this, true);

						}
					};

					assegnaCommand = new AssegnaFascicoliCommand();
					getView().setStatiFascicoloVisible(false);
					getView().configuraIndirizziNotifica(true,
							profilazioneUtenteHandler.getPreferenzeRiassegnazioneUtente() != null ? profilazioneUtenteHandler.getPreferenzeRiassegnazioneUtente().getIndirizziNotifica() : null);
					getView().setNoteVisible(true);
					getView().getAnnullaButton().setText("Indietro");
					revealInParent();
				}
			}
		});
	}

	private class GoToDettaglioFascicolo implements Command {

		@Override
		public void execute() {
			String idPratica = ids.iterator().next();
			praticheDB.remove(idPratica);
			Builder builder = new PlaceRequest.Builder();
			builder.nameToken(NameTokens.dettagliofascicolo);
			builder.with(NameTokensParams.idPratica, idPratica);
			placeManager.revealPlace(builder.build());

		}
	}

	private class GoToDettaglioPraticaModulistica implements Command {

		@Override
		public void execute() {
			String idPratica = ids.iterator().next();
			praticheDB.remove(idPratica);
			Builder builder = new PlaceRequest.Builder();
			builder.nameToken(NameTokens.dettagliopraticamodulistica);
			builder.with(NameTokensParams.idPratica, idPratica);
			placeManager.revealPlace(builder.build());

		}
	}

	private class GoToDettaglioPecIn implements Command {

		@Override
		public void execute() {
			String idPratica = ids.iterator().next();
			praticheDB.remove(idPratica);
			Builder builder = new PlaceRequest.Builder();
			builder.nameToken(NameTokens.dettagliopecin);
			builder.with(NameTokensParams.idPratica, idPratica);
			placeManager.revealPlace(builder.build());

		}
	}

	private class GoToWorklistFascicolo implements Command {

		@Override
		public void execute() {
			for (String id : ids) {
				praticheDB.remove(id);
			}

			eventBus.fireEvent(new MostraWorklistFascicoliEvent(identificativoWorklistFascicolo));
		}
	}

	private class GoToWorklistPecIn implements Command {

		private List<PecInDTO> pecModificate = new ArrayList<PecInDTO>();

		public void setPecModificate(List<PecInDTO> pecModificate) {
			this.pecModificate = pecModificate;
		}

		@Override
		public void execute() {
			for (String id : ids) {
				praticheDB.remove(id);
			}
			eventBus.fireEvent(new MostraWorklistPecInEvent(identificativoWorklistPecIn));
		}
	}

	private class GoToWorklistPraticaModulistica implements Command {

		@Override
		public void execute() {
			for (String id : ids) {
				praticheDB.remove(id);
			}
			Builder builder = new PlaceRequest.Builder();
			builder.nameToken(NameTokens.worklistpraticamodulistica);
			String nomeModulo = placeManager.getCurrentPlaceRequest().getParameter(NameTokensParams.nomeModulo, null);
			builder.with(NameTokensParams.nomeModulo, nomeModulo);
			String worklistModulistica = placeManager.getCurrentPlaceRequest().getParameter(NameTokensParams.worklistModulistica, null);
			builder.with(NameTokensParams.worklistModulistica, worklistModulistica);
			placeManager.revealPlace(builder.build());
		}
	}

	private class AssegnaFascicoliCommand implements Command {

		@Override
		public void execute() {
			ShowAppLoadingEvent.fire(GruppiPresenter.this, true);
			InformazioniRiassegnazioneFascicoliTaskFirmaDTO riassegnaFascicoloInfo = new InformazioniRiassegnazioneFascicoliTaskFirmaDTO(ids, getView().getAnagraficaRuoloSelezionata());
			riassegnaFascicoloInfo.setNote(getView().getNote());
			riassegnaFascicoloInfo.setSettore(getView().getSettoreSelezionato());
			riassegnaFascicoloInfo.getIndirizziNotifica().addAll(getView().getIndirizziNotifica());
			riassegnaFascicoloInfo.setRicordaScelta(getView().isRicordaSceltaEnabled());
			riassegnaFascicoloInfo.setOperatore(getView().getOperatoreSelezionato());
			AssegnaFascicoliFineEvent.fire(GruppiPresenter.this, riassegnaFascicoloInfo);
		}
	}

	private class AssegnaFascicoloCommand implements Command {

		@Override
		public void execute() {
			ShowAppLoadingEvent.fire(GruppiPresenter.this, true);
			RiassegnaFascicolo action = new RiassegnaFascicolo(ids, getView().getAnagraficaRuoloSelezionata());
			action.setNote(getView().getNote());
			action.setSettore(getView().getSettoreSelezionato());
			action.getIndirizziNotifica().addAll(getView().getIndirizziNotifica());
			action.setRicordaScelta(getView().isRicordaSceltaEnabled());
			action.setOperatore(getView().getOperatoreSelezionato());

			if (getView().isStatiFascicoloVisible()) {
				action.setStato(getView().getStatoFascicoloSelected());
			}

			try {
				profilazioneUtenteHandler.aggiornaPreferenzeRiassegnazione(getView().isRicordaSceltaEnabled(), //
						getView().getSettoreSelezionato() != null ? getView().getSettoreSelezionato().getNome() : null, //
						getView().getAnagraficaRuoloSelezionata() != null ? getView().getAnagraficaRuoloSelezionata().getRuolo() : null, //
						getView().getIndirizziNotifica(), null); //

			} catch (Exception e) {

			}

			dispatcher.execute(action, new AsyncCallback<RiassegnaFascicoloResult>() {

				@Override
				public void onSuccess(RiassegnaFascicoloResult result) {
					ShowAppLoadingEvent.fire(GruppiPresenter.this, false);
					if (!result.getError()) {

						for (FascicoloDTO doc : result.getFascicoli()) {
							praticheDB.insertOrUpdate(doc.getClientID(), doc, sitemapMenu.containsLink(doc.getClientID()));
						}
						dropMessage();
						indietroCommand.execute();
					} else {
						ShowMessageEvent event = new ShowMessageEvent();
						event.setErrorMessage(result.getErrorMsg());
						eventBus.fireEvent(event);
					}
				}

				@Override
				public void onFailure(Throwable caught) {
					ShowAppLoadingEvent.fire(GruppiPresenter.this, false);
					ShowMessageEvent showMessageEvent = new ShowMessageEvent();
					showMessageEvent.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
					getEventBus().fireEvent(showMessageEvent);
				}
			});

		}
	}

	private class AssegnaPecInCommand implements Command {

		@Override
		public void execute() {
			ShowAppLoadingEvent.fire(GruppiPresenter.this, true);
			RiassegnaPecIn action = new RiassegnaPecIn(ids, getView().getAnagraficaRuoloSelezionata());
			action.setNote(getView().getNote());
			action.setSettore(getView().getSettoreSelezionato());
			action.getIndirizziNotifica().addAll((getView().getIndirizziNotifica()));
			action.setRicordaScelta(getView().isRicordaSceltaEnabled());
			action.setOperatore(getView().getOperatoreSelezionato());

			try {
				profilazioneUtenteHandler.aggiornaPreferenzeRiassegnazione(getView().isRicordaSceltaEnabled(), //
						getView().getSettoreSelezionato() != null ? getView().getSettoreSelezionato().getNome() : null, //
						getView().getAnagraficaRuoloSelezionata() != null ? getView().getAnagraficaRuoloSelezionata().getRuolo() : null, //
						getView().getIndirizziNotifica(), //
						null); //
			} catch (Exception e) {

			}

			dispatcher.execute(action, new AsyncCallback<RiassegnaPecInResult>() {

				@Override
				public void onSuccess(RiassegnaPecInResult result) {
					ShowAppLoadingEvent.fire(GruppiPresenter.this, false);
					if (!result.getError()) {

						for (PecInDTO doc : result.getListPecInDTO()) {
							praticheDB.insertOrUpdate(doc.getClientID(), doc, sitemapMenu.containsLink(doc.getClientID()));
						}

						if (indietroCommand instanceof GoToWorklistPecIn) {
							((GoToWorklistPecIn) indietroCommand).setPecModificate(result.getListPecInDTO());
						}

						dropMessage();
						indietroCommand.execute();
					} else {
						ShowMessageEvent event = new ShowMessageEvent();
						event.setErrorMessage(result.getMessageError());
						eventBus.fireEvent(event);
					}
				}

				@Override
				public void onFailure(Throwable caught) {
					ShowAppLoadingEvent.fire(GruppiPresenter.this, false);
					ShowMessageEvent showMessageEvent = new ShowMessageEvent();
					showMessageEvent.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
					getEventBus().fireEvent(showMessageEvent);
				}
			});
		}
	}

	private class AssegnaPraticaModulisticaCommand implements Command {

		@Override
		public void execute() {
			ShowAppLoadingEvent.fire(GruppiPresenter.this, true);
			RiassegnaModulo action = new RiassegnaModulo(ids, getView().getAnagraficaRuoloSelezionata());
			action.setNote(getView().getNote());
			action.setSettore(getView().getSettoreSelezionato());
			action.getIndirizziNotifica().addAll((getView().getIndirizziNotifica()));
			action.setRicordaScelta(getView().isRicordaSceltaEnabled());
			action.setOperatore(getView().getOperatoreSelezionato());

			try {
				profilazioneUtenteHandler.aggiornaPreferenzeRiassegnazione(getView().isRicordaSceltaEnabled(), //
						getView().getSettoreSelezionato() != null ? getView().getSettoreSelezionato().getNome() : null, //
						getView().getAnagraficaRuoloSelezionata() != null ? getView().getAnagraficaRuoloSelezionata().getRuolo() : null, //
						getView().getIndirizziNotifica(), //
						null); //

			} catch (Exception e) {

			}

			dispatcher.execute(action, new AsyncCallback<RiassegnaModuloResult>() {

				@Override
				public void onSuccess(RiassegnaModuloResult result) {
					ShowAppLoadingEvent.fire(GruppiPresenter.this, false);
					ShowAppLoadingEvent.fire(GruppiPresenter.this, false);
					if (!result.getError()) {

						for (PraticaModulisticaDTO doc : result.getPratiche()) {
							praticheDB.insertOrUpdate(doc.getClientID(), doc, sitemapMenu.containsLink(doc.getClientID()));
						}
						dropMessage();
						indietroCommand.execute();
					} else {
						ShowMessageEvent event = new ShowMessageEvent();
						event.setErrorMessage(result.getErrorMsg());
						eventBus.fireEvent(event);
					}
				}

				@Override
				public void onFailure(Throwable caught) {
					ShowAppLoadingEvent.fire(GruppiPresenter.this, false);
					ShowMessageEvent showMessageEvent = new ShowMessageEvent();
					showMessageEvent.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
					getEventBus().fireEvent(showMessageEvent);
				}
			});
		}
	}
}
