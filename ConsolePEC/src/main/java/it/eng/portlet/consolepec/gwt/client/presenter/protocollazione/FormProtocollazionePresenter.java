package it.eng.portlet.consolepec.gwt.client.presenter.protocollazione;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaFascicolo;
import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.util.pratica.PraticaUtil;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaFascicoloLoaded;
import it.eng.portlet.consolepec.gwt.client.event.BackFromPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.event.GoToPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.place.NameTokens;
import it.eng.portlet.consolepec.gwt.client.place.NameTokensParams;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter.Place;
import it.eng.portlet.consolepec.gwt.client.presenter.pec.event.RilasciaInCaricoPecInEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.TornaAFormDiProtocollazioneEvent.TornaAFormDiProtocollazioneHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.DettaglioPraticaFromProtocollazioneEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.MostraDettaglioAllegatoDaFormProtocollazioneEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.MostraEsitoProtocollazioneEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.mostraform.MostraFormProtocollazioneFascicoloEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.mostraform.MostraFormProtocollazioneFascicoloEvent.MostraFormProtocollazioneFascicoloHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.mostraform.MostraFormProtocollazionePecInEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.mostraform.MostraFormProtocollazionePecInEvent.MostraFormProtocollazionePecInHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.mostraform.MostraFormProtocollazionePecOutEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.mostraform.MostraFormProtocollazionePecOutEvent.MostraFormProtocollazionePecOutHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.mostraform.MostraFormProtocollazionePraticaModulisticaEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.mostraform.MostraFormProtocollazionePraticaModulisticaEvent.MostraFormProtocollazionePraticaModulisticaHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.mostraform.MostraFormProtocollazioneSceltaFascicoloEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.mostraform.MostraFormProtocollazioneSceltaFascicoloEvent.MostraFormProtocollazioneSceltaFascicoloHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.sceltacapofila.MostraSceltaCapofilaEmailInEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.sceltacapofila.MostraSceltaCapofilaEmailOutEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.sceltacapofila.MostraSceltaCapofilaEvent.SelectedObject;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.sceltacapofila.MostraSceltaCapofilaPraticaModulisticaEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.sceltacapofila.MostraSceltaCapofilaProtocollazioneFascicoloEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.sceltacapofila.MostraSceltaCapofilaSceltaFascicoloEvent;
import it.eng.portlet.consolepec.gwt.client.scan.CompileRequestScan;
import it.eng.portlet.consolepec.gwt.client.scan.VisitableWidget;
import it.eng.portlet.consolepec.gwt.client.util.DatiDefaultProtocollazioneHandler;
import it.eng.portlet.consolepec.gwt.client.util.DatiDefaultProtocollazioneHandler.RecuperoDatiObserver;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.GetConfigurazioneCampiProtocollazione;
import it.eng.portlet.consolepec.gwt.shared.action.GetConfigurazioneCampiProtocollazioneResult;
import it.eng.portlet.consolepec.gwt.shared.action.ProtocollaAction;
import it.eng.portlet.consolepec.gwt.shared.action.ProtocollaFascicoloNuovoAction;
import it.eng.portlet.consolepec.gwt.shared.action.ProtocollaFascicoloNuovoActionResult;
import it.eng.portlet.consolepec.gwt.shared.action.ProtocollaResult;
import it.eng.portlet.consolepec.gwt.shared.action.RicercaCapofilaAction;
import it.eng.portlet.consolepec.gwt.shared.action.RicercaCapofilaResult;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CreaFascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.GeneraTitoloFascicoloAction;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.GeneraTitoloFascicoloResult;
import it.eng.portlet.consolepec.gwt.shared.action.pec.InviaMailAction;
import it.eng.portlet.consolepec.gwt.shared.action.pec.InviaMailActionResult;
import it.eng.portlet.consolepec.gwt.shared.dto.Campo;
import it.eng.portlet.consolepec.gwt.shared.dto.Element;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.DatiDefaultProtocollazione;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.TipoProtocollazione;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.google.common.base.Strings;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ListBox;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

public class FormProtocollazionePresenter extends Presenter<FormProtocollazionePresenter.MyView, FormProtocollazionePresenter.MyProxy> implements MostraFormProtocollazionePecInHandler, MostraFormProtocollazioneFascicoloHandler, MostraFormProtocollazionePecOutHandler,
		MostraFormProtocollazioneSceltaFascicoloHandler, MostraFormProtocollazionePraticaModulisticaHandler, TornaAFormDiProtocollazioneHandler {

	public interface MyView extends View {

		Button getConfermaButton();

		Button getAnnullaButton();

		ListBox getTipoProtocollazioneListBox();

		List<VisitableWidget> getWidget();

		String getAnnoCapoFila();

		String getNumeroCapoFila();
		
		void setOggettoCapoFila(String oggetto);

		void setTipoProtocollo(String string);

		Button getIndietroButton();

		void setTitle(String string);

		void setEnabledListBox(boolean enabled);

		void init(Map<String, Campo> campi, DatiDefaultProtocollazione datiDefaultProtocollazione, String tipoProtocollazione, boolean disableList, DispatchAsync dispatchAsync, Set<PraticaDTO> pratiche, Set<AllegatoDTO> allegati);

		void setCommandDettaglioAllegato(it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, AllegatoDTO> command);

		void setCommandDettaglioPratica(it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, PraticaDTO> command);
		

	}

	@ProxyCodeSplit
	public interface MyProxy extends Proxy<FormProtocollazionePresenter> {
		//
	}

	private Map<String, Campo> campi;
	private final DispatchAsync dispatcher;
	private final PecInPraticheDB praticheDB;
	private ConfigurazioniHandler configurazioniHandler;
	private DatiDefaultProtocollazione datiDefaultProtocollazione;

	private final EventBus eventBus;

	private String tipoProtocollazione;
	private boolean emailInteroperabile;

	private Set<AllegatoDTO> allegati = new TreeSet<AllegatoDTO>();
	private Set<PraticaDTO> pratiche = new TreeSet<PraticaDTO>();
	private Command protocollaCommand;
	private Command indietroCommand;
	private Command annullaCommand;
	private CreaFascicoloDTO creaFascicoloDTO;
	private boolean reloadForm = true;

	private final DatiDefaultProtocollazioneHandler datiDefaultProtocollazioneHandler;

	@Inject
	public FormProtocollazionePresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, final DispatchAsync dispatcher, final PecInPraticheDB pecInDB, final DatiDefaultProtocollazioneHandler datiDefaultProtocollazioneHandler, ConfigurazioniHandler configurazioniHandler) {
		super(eventBus, view, proxy);
		this.dispatcher = dispatcher;
		this.praticheDB = pecInDB;
		this.eventBus = eventBus;
		this.datiDefaultProtocollazioneHandler = datiDefaultProtocollazioneHandler;
		this.configurazioniHandler = configurazioniHandler;
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPresenter.TYPE_SetMainContent, this);
	}

	@Override
	protected void onBind() {
		super.onBind();

		getView().getIndietroButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				indietroCommand.execute();
			}
		});

		this.getView().getConfermaButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				protocollaCommand.execute();
			}
		});

		this.getView().getAnnullaButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				annullaCommand.execute();
			}
		});

		this.getView().setCommandDettaglioAllegato(new it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, AllegatoDTO>() {

			@Override
			public Void exe(AllegatoDTO t) {
				MostraDettaglioAllegatoDaFormProtocollazioneEvent mostraDettaglioAllegatoDaFormProtocollazioneEvent = new MostraDettaglioAllegatoDaFormProtocollazioneEvent(t);
				getEventBus().fireEvent(mostraDettaglioAllegatoDaFormProtocollazioneEvent);
				return null;
			}
		});
		
		this.getView().setCommandDettaglioPratica(new it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, PraticaDTO>() {

			@Override
			public Void exe(PraticaDTO p) {
				DettaglioPraticaFromProtocollazioneEvent event = new DettaglioPraticaFromProtocollazioneEvent(p.getClientID());
				getEventBus().fireEvent(event);
				return null;
			}
		});
		
	

	}

	private void mostraPaginaEsito(Map<String, Map<String, Element>> map, String pg, String pgCapofila, String idFasicolo) {
		MostraEsitoProtocollazioneEvent event = new MostraEsitoProtocollazioneEvent(idFasicolo);
		event.setMapForm(map);
		event.setPg(pg);
		event.setPgCapofila(pgCapofila);
		event.setPraticheProtocollate(datiDefaultProtocollazione.getPratiche());
		eventBus.fireEvent(event);
	}

	@Override
	protected void onReveal() {
		super.onReveal();

		if (!reloadForm) {
			reloadForm = true;
			return;
		}

		ShowMessageEvent event = new ShowMessageEvent();
		event.setMessageDropped(true);
		eventBus.fireEvent(event);

		this.getView().setTitle("Protocollazione");

		datiDefaultProtocollazioneHandler.retrieveDatiDefaultProtocollazione(datiDefaultProtocollazione, this, new RecuperoDatiObserver() {

			@Override
			public void onComplete() {
				configuraCampiProtocollazione(tipoProtocollazione, true);
			}
		});

	}

	private void configuraCampiProtocollazione(final String tipoProtocollazione, final boolean disableList) {
		GetConfigurazioneCampiProtocollazione configurazioneCampi = new GetConfigurazioneCampiProtocollazione(tipoProtocollazione);
		ShowAppLoadingEvent.fire(FormProtocollazionePresenter.this, true);
		/* reset dei messaggi di errore */
		ShowMessageEvent event = new ShowMessageEvent();
		event.setMessageDropped(true);
		eventBus.fireEvent(event);
		dispatcher.execute(configurazioneCampi, new AsyncCallback<GetConfigurazioneCampiProtocollazioneResult>() {

			@Override
			public void onFailure(Throwable caught) {
				ShowAppLoadingEvent.fire(FormProtocollazionePresenter.this, false);
				ShowMessageEvent event = new ShowMessageEvent();
				event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
				eventBus.fireEvent(event);
			}

			@Override
			public void onSuccess(GetConfigurazioneCampiProtocollazioneResult result) {
				initConfigurazioneCampiProtocollazione(tipoProtocollazione, disableList, result);
			}
		});
	}
	
	private void configuraCampiProtocollazione(final String tipoProtocollazione, final boolean disableList,
			final boolean oggettoCapofila, final GetConfigurazioneCampiProtocollazioneResult result) {
		ShowAppLoadingEvent.fire(FormProtocollazionePresenter.this, false);
		if (!result.isError()) {
			campi = result.getCampi();
			if (!oggettoCapofila) {
				getView().setOggettoCapoFila(null);
				campi.remove("il_oggetto_capofila");
			}
			getView().init(campi, datiDefaultProtocollazione, tipoProtocollazione, disableList, dispatcher, pratiche, allegati);
			getView().getTipoProtocollazioneListBox().addChangeHandler(new ChangeHandler() {
				@Override
				public void onChange(ChangeEvent event) {
					int selectedIndex = ((ListBox) event.getSource()).getSelectedIndex();
					String tipoProt = getView().getTipoProtocollazioneListBox().getValue(selectedIndex);
					configuraCampiProtocollazione(tipoProt, false);
				}
			});
		}
	}
	
	private void initConfigurazioneCampiProtocollazione(final String tipoProtocollazione, final boolean disableList,
			final GetConfigurazioneCampiProtocollazioneResult result) {
		
		String annoPG = datiDefaultProtocollazione.getDatiPg().getAnnoPg();
		String numPG = datiDefaultProtocollazione.getDatiPg().getNumeroPg();
		if (Strings.isNullOrEmpty(annoPG) || Strings.isNullOrEmpty(numPG)) {
			configuraCampiProtocollazione(tipoProtocollazione, disableList, false, result);
			return;
		}
		
		dispatcher.execute(createRicercaCapofilaAction(annoPG, numPG), new AsyncCallback<RicercaCapofilaResult>() {

			@Override
			public void onFailure(Throwable caught) {
				configuraCampiProtocollazione(tipoProtocollazione, disableList, false, result);
			}

			@Override
			public void onSuccess(RicercaCapofilaResult rCResult) {
				if (!rCResult.isError() && !rCResult.isWarninig()) {
					getView().setOggettoCapoFila(rCResult.getOggetto());
				}
				configuraCampiProtocollazione(tipoProtocollazione, disableList, true, result);
			}});
	}
	
	private static RicercaCapofilaAction createRicercaCapofilaAction(final String annoPG, final String numPG) {
		RicercaCapofilaAction action = new RicercaCapofilaAction();
		try {
			action.setAnnoPg(Integer.parseInt(annoPG));
		} catch (Exception e) {
			action.setAnnoPg(0);
		}
		action.setNumeroPg(numPG);
		return action;
	}

	private void cleanForm() {
		datiDefaultProtocollazione = new DatiDefaultProtocollazione();
		tipoProtocollazione = null;
		creaFascicoloDTO = null;
		allegati = new TreeSet<AllegatoDTO>();
		pratiche = new TreeSet<PraticaDTO>();
		datiDefaultProtocollazioneHandler.clear();

	}

	private void revealForm() {
		this.revealInParent();
	}

	/**
	 * gestisce la provenienza dal form di scelta di un fascicolo
	 * */
	@ProxyEvent
	@Override
	public void onMostraFormProtocollazioneSceltaFascicolo(MostraFormProtocollazioneSceltaFascicoloEvent event) {
		cleanForm();
		tipoProtocollazione = TipoProtocollazione.ENTRATA.getCodice();
		datiDefaultProtocollazione = event.getDatiPerFormProtocollazione();
		TipologiaPratica tipoDocumento = event.getPraticaDaProtocollare().getTipologiaPratica();

		if (PraticaUtil.isIngresso(tipoDocumento)) {
			datiDefaultProtocollazioneHandler.setEmailInDTO(event.getPraticaDaProtocollare());
		}
		
		if (PraticaUtil.isPraticaModulistica(tipoDocumento)) {
			datiDefaultProtocollazioneHandler.setModulisticaDTO(event.getPraticaDaProtocollare());
		}


		datiDefaultProtocollazioneHandler.setFascicoloDTO(event.getFascicoloDTO());

		pratiche.add(event.getPraticaDaProtocollare());

		indietroCommand = new IndietroSceltaFascicoloCommand();
		annullaCommand = new AnnullaSceltaFascicoloCommand();
		protocollaCommand = new ProtocollaFascicoloEsistenteCommand();
		revealForm();
		
	}

	/**
	 * gestisce la provenienza dal dettaglio di una pec out
	 * */
	@Override
	@ProxyEvent
	public void onMostraFormProtocollazionePecOut(MostraFormProtocollazionePecOutEvent event) {
		cleanForm();
		datiDefaultProtocollazione = event.getDatiDefaultProtocollazione();
		datiDefaultProtocollazioneHandler.setFascicoloDTO(event.getFascicoloDTO());
		datiDefaultProtocollazioneHandler.setEmailOutDTO(event.getPecOutDTO());
		tipoProtocollazione = TipoProtocollazione.USCITA.getCodice();
		emailInteroperabile = event.isEmailInteroperabile();
		indietroCommand = new IndietroPecOutCommand();
		annullaCommand = new AnnullaPecOutCommand();
		protocollaCommand = new ProtocollaFascicoloEsistenteCommand();
		pratiche.add(event.getPecOutDTO());
		revealForm();
	}

	/**
	 * gestisce la provenienza dal dettaglio di un fascicolo
	 * */
	@Override
	@ProxyEvent
	public void onMostraFormProtocollazioneFascicolo(MostraFormProtocollazioneFascicoloEvent event) {
		cleanForm();
		datiDefaultProtocollazione = event.getDatiDefaultProtocollazione();
		datiDefaultProtocollazioneHandler.setFascicoloDTO(event.getFascicoloDTO());
		pratiche.addAll(event.getPraticheDTO());
		allegati.addAll(event.getAllegatiDTO());
		tipoProtocollazione = event.getTipoProtocollazione();
		indietroCommand = new IndietroFascicoloCommand();
		annullaCommand = new AnnullaFascicoloCommand();
		protocollaCommand = new ProtocollaFascicoloEsistenteCommand();
		revealForm();
	}

	/**
	 * gestisce la provenienza dalla creazione di un fascicolo a partire da una pec in
	 * */
	@Override
	@ProxyEvent
	public void onMostraFormProtocollazionePecIn(final MostraFormProtocollazionePecInEvent event) {
		cleanForm();
		datiDefaultProtocollazione = event.getDatiDefaultProtocollazione();
		
		AnagraficaFascicolo af = configurazioniHandler.getAnagraficaFascicolo(event.getCreaFascicoloDTO().getTipologiaFascicolo().getNomeTipologia());
		
		if (af.getTemplateTitolo() != null && !af.getTemplateTitolo().isEmpty()) {
			
			ShowAppLoadingEvent.fire(FormProtocollazionePresenter.this, true);
			
			dispatcher.execute(new GeneraTitoloFascicoloAction(event.getCreaFascicoloDTO()), new AsyncCallback<GeneraTitoloFascicoloResult>() {

				@Override
				public void onFailure(Throwable arg0) {
					ShowAppLoadingEvent.fire(FormProtocollazionePresenter.this, false);

					ShowMessageEvent event = new ShowMessageEvent();
					event.setWarningMessage(ConsolePecConstants.ERROR_MESSAGE);
					getEventBus().fireEvent(event);
				}

				@Override
				public void onSuccess(GeneraTitoloFascicoloResult arg0) {
					
					ShowAppLoadingEvent.fire(FormProtocollazionePresenter.this, false);

					if (arg0.isError()) {
						ShowMessageEvent event = new ShowMessageEvent();
						event.setWarningMessage(arg0.getErrorMessage());
						getEventBus().fireEvent(event);
						
					} else {
						datiDefaultProtocollazione.setTitolo(arg0.getTitolo());
						datiDefaultProtocollazione.setOggettoProtocollazione(arg0.getTitolo());
						datiDefaultProtocollazioneHandler.setEmailInDTO(event.getPecInDTO());
						tipoProtocollazione = TipoProtocollazione.ENTRATA.getCodice();
						pratiche.add(event.getPecInDTO());
						creaFascicoloDTO = event.getCreaFascicoloDTO();
						creaFascicoloDTO.setTitolo(arg0.getTitolo());
						indietroCommand = new IndietroPecInCommand();
						protocollaCommand = new ProtocollaFascicoloNuovoCommand();
						annullaCommand = new AnnullaPecInCommand();
						revealForm();
					}
				}
			});
			
		} else {
			datiDefaultProtocollazione.setTitolo(event.getCreaFascicoloDTO().getTitolo());
			datiDefaultProtocollazioneHandler.setEmailInDTO(event.getPecInDTO());
			tipoProtocollazione = TipoProtocollazione.ENTRATA.getCodice();
			pratiche.add(event.getPecInDTO());
			creaFascicoloDTO = event.getCreaFascicoloDTO();
			indietroCommand = new IndietroPecInCommand();
			protocollaCommand = new ProtocollaFascicoloNuovoCommand();
			annullaCommand = new AnnullaPecInCommand();
			revealForm();
		}
	}

	@Override
	@ProxyEvent
	public void onMostraFormProtocollazionePraticaModulistica(MostraFormProtocollazionePraticaModulisticaEvent event) {
		cleanForm();
		datiDefaultProtocollazione = event.getDatiDefaultProtocollazione();
		datiDefaultProtocollazioneHandler.setModulisticaDTO(event.getPraticaModulisticaDTO());
		tipoProtocollazione = TipoProtocollazione.ENTRATA.getCodice();
		creaFascicoloDTO = event.getCreaFascicoloDTO();
		pratiche.add(event.getPraticaModulisticaDTO());
		indietroCommand = new IndietroPraticaModulisticaCommand();
		protocollaCommand = new ProtocollaFascicoloNuovoCommand();
		annullaCommand = new AnnullaPraticaModulisticaCommand();

		revealForm();
	}

	public class ProtocollaFascicoloEsistenteCommand implements Command {

		@Override
		public void execute() {
			String annoCapoFila = getView().getAnnoCapoFila();
			String numeroCapoFila = getView().getNumeroCapoFila();
			if (!Strings.isNullOrEmpty(numeroCapoFila) && !Strings.isNullOrEmpty(annoCapoFila)) {
				getView().setTipoProtocollo("PG");
			}
			final CompileRequestScan compileRequestScan = new CompileRequestScan(campi);
			compileRequestScan.scanListWidget(getView().getWidget());

			if (!compileRequestScan.isError() && !compileRequestScan.isWarning()) {
				ShowMessageEvent event = new ShowMessageEvent();
				event.setMessageDropped(true);
				eventBus.fireEvent(event);
				ProtocollaAction protocolla = new ProtocollaAction(datiDefaultProtocollazioneHandler.getFascicoloDTO().getClientID());
				FormProtocollazionePresenter.this.datiDefaultProtocollazione.setFascicoloNuovo(false);
				protocolla.setDatiPerProtocollazione(FormProtocollazionePresenter.this.datiDefaultProtocollazione);

				setOggettiDaProtocollare(allegati, pratiche, datiDefaultProtocollazione);

				protocolla.setValueMap(compileRequestScan.getMapChiaveValore());

				callServer(protocolla);
			} else {
				if (compileRequestScan.isError()) {
					ShowMessageEvent event = new ShowMessageEvent();
					event.setWarningMessage(compileRequestScan.getMessageError());
					getEventBus().fireEvent(event);
				} else if (compileRequestScan.isWarning()) {
					ShowMessageEvent event = new ShowMessageEvent();
					event.setWarningMessage(compileRequestScan.getMessageWarning());
					getEventBus().fireEvent(event);
				}
			}
		}

		private void setOggettiDaProtocollare(Set<AllegatoDTO> allegati, Set<PraticaDTO> pratiche, DatiDefaultProtocollazione datiDefaultProtocollazione) {

			for (AllegatoDTO allegato : allegati)
				datiDefaultProtocollazione.getAllegati().add(allegato.getNome());
			for (PraticaDTO pratica : pratiche)
				datiDefaultProtocollazione.getPratiche().add(pratica.getClientID());

		}

		private void callServer(final ProtocollaAction protocolla) {
			ShowAppLoadingEvent event = new ShowAppLoadingEvent(true);
			getEventBus().fireEvent(event);
			/* reset dei messaggi di errore */
			ShowMessageEvent event2 = new ShowMessageEvent();
			event2.setMessageDropped(true);
			eventBus.fireEvent(event2);
			dispatcher.execute(protocolla, new AsyncCallback<ProtocollaResult>() {

				@Override
				public void onSuccess(ProtocollaResult result) {

					/*
					 * controllo se è una mail interoperabile per effettuare l'invio
					 */
					if (emailInteroperabile) {
						inviaMailInteroperabile(protocolla, result);
					} else {
						protocollazioneAvvenutaConSuccesso(protocolla, result);
					}

				}

				private void inviaMailInteroperabile(final ProtocollaAction protocolla, final ProtocollaResult result) {
					/*
					 * secondo specifiche: nell'invio della mail interoperabile devo andare direttamente alla maschera di protocollazione(senza passare dal form di scelta)
					 */
					InviaMailAction action = new InviaMailAction(datiDefaultProtocollazioneHandler.getEmailOutDTO().getClientID());

					ShowAppLoadingEvent.fire(FormProtocollazionePresenter.this, true);
					/* reset dei messaggi di errore */
					ShowMessageEvent event = new ShowMessageEvent();
					event.setMessageDropped(true);
					getEventBus().fireEvent(event);
					dispatcher.execute(action, new AsyncCallback<InviaMailActionResult>() {

						@Override
						public void onSuccess(InviaMailActionResult imresult) {
							ShowAppLoadingEvent.fire(FormProtocollazionePresenter.this, false);
							if (!result.getError()) {
								// praticheDB.remove(datiDefaultProtocollazioneHandler.getIdEmailOut());
								protocollazioneAvvenutaConSuccesso(protocolla, result);
							} else {
								writeErrorMessage(imresult.getMessError());
							}

						}

						@Override
						public void onFailure(Throwable caught) {
							writeErrorMessage(ConsolePecConstants.ERROR_MESSAGE);

						}
					});
				}

				@Override
				public void onFailure(Throwable caught) {
					writeErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
				}
			});

		}

		private void writeErrorMessage(String messError) {
			ShowAppLoadingEvent.fire(FormProtocollazionePresenter.this, false);
			ShowMessageEvent event = new ShowMessageEvent();
			event.setErrorMessage(messError);
			eventBus.fireEvent(event);
			ShowAppLoadingEvent.fire(FormProtocollazionePresenter.this, false);
		}

		private void protocollazioneAvvenutaConSuccesso(final ProtocollaAction protocolla, ProtocollaResult result) {
			ShowAppLoadingEvent event = new ShowAppLoadingEvent(false);
			FormProtocollazionePresenter.super.getEventBus().fireEvent(event);
			if (result.getError()) {
				ShowMessageEvent eventMessage = new ShowMessageEvent();
				eventMessage.setErrorMessage(result.getMessageError());
				eventBus.fireEvent(eventMessage);
			} else if (result.getWarning()) {
				ShowMessageEvent eventMessage = new ShowMessageEvent();
				eventMessage.setWarningMessage(result.getWarningMessage());
				eventBus.fireEvent(eventMessage);
			} else {
				String pg = result.getNumeroPg() + "/" + result.getAnnoPg();
				String pgCapofila = result.getNumeroPgCapofila() + "/" + result.getAnnoPgCapofila();
				praticheDB.remove(datiDefaultProtocollazioneHandler.getFascicoloDTO().getClientID());
				if (datiDefaultProtocollazioneHandler.getEmailOutDTO() != null)
					praticheDB.remove(datiDefaultProtocollazioneHandler.getEmailOutDTO().getClientID());
				if (datiDefaultProtocollazioneHandler.getEmailInDTO() != null)
					praticheDB.remove(datiDefaultProtocollazioneHandler.getEmailInDTO().getClientID());
				praticheDB.remove(result.getFascicoloDTO().getClientID());
				mostraPaginaEsito(protocolla.getValueMap(), pg, pgCapofila, result.getFascicoloDTO().getClientID());
			}
		}
	}

	public class ProtocollaFascicoloNuovoCommand implements Command {

		@Override
		public void execute() {
			String annoCapoFila = getView().getAnnoCapoFila();
			String numeroCapoFila = getView().getNumeroCapoFila();
			if (annoCapoFila != null && !annoCapoFila.trim().equals("") && numeroCapoFila != null && !numeroCapoFila.trim().equals("")) {
				getView().setTipoProtocollo("PG");
			}
			final CompileRequestScan compileRequestScan = new CompileRequestScan(campi);
			compileRequestScan.scanListWidget(getView().getWidget());

			if (!compileRequestScan.isError() && !compileRequestScan.isWarning()) {

				ShowMessageEvent event = new ShowMessageEvent();
				event.setMessageDropped(true);
				eventBus.fireEvent(event);

				ProtocollaFascicoloNuovoAction protocolla = new ProtocollaFascicoloNuovoAction();
				
				setOggettiDaProtocollare(allegati, pratiche, datiDefaultProtocollazione);

				FormProtocollazionePresenter.this.datiDefaultProtocollazione.setFascicoloNuovo(true);
				protocolla.setDatiPerProtocollazione(FormProtocollazionePresenter.this.datiDefaultProtocollazione);
				protocolla.setValueMap(compileRequestScan.getMapChiaveValore());
				protocolla.setCreaFascicoloDTO(creaFascicoloDTO);
				callServer(protocolla);

			} else {
				if (compileRequestScan.isError()) {
					ShowMessageEvent event = new ShowMessageEvent();
					event.setWarningMessage(compileRequestScan.getMessageError());
					getEventBus().fireEvent(event);
				} else if (compileRequestScan.isWarning()) {
					ShowMessageEvent event = new ShowMessageEvent();
					event.setWarningMessage(compileRequestScan.getMessageWarning());
					getEventBus().fireEvent(event);
				}
			}
		}

		
		private void setOggettiDaProtocollare(Set<AllegatoDTO> allegati, Set<PraticaDTO> pratiche, DatiDefaultProtocollazione datiDefaultProtocollazione) {

			for (AllegatoDTO allegato : allegati)
				datiDefaultProtocollazione.getAllegati().add(allegato.getNome());
			for (PraticaDTO pratica : pratiche)
				datiDefaultProtocollazione.getPratiche().add(pratica.getClientID());

		}
		
		private void callServer(final ProtocollaFascicoloNuovoAction protocolla) {
			ShowAppLoadingEvent event = new ShowAppLoadingEvent(true);
			getEventBus().fireEvent(event);
			/* reset dei messaggi di errore */
			ShowMessageEvent event2 = new ShowMessageEvent();
			event2.setMessageDropped(true);
			eventBus.fireEvent(event2);
			dispatcher.execute(protocolla, new AsyncCallback<ProtocollaFascicoloNuovoActionResult>() {

				@Override
				public void onSuccess(ProtocollaFascicoloNuovoActionResult result) {
					ShowAppLoadingEvent event = new ShowAppLoadingEvent(false);
					FormProtocollazionePresenter.super.getEventBus().fireEvent(event);
					if (result.getError()) {
						ShowMessageEvent eventMessage = new ShowMessageEvent();
						eventMessage.setErrorMessage(result.getMessageError());
						eventBus.fireEvent(eventMessage);
					} else if (result.getWarning()) {
						ShowMessageEvent eventMessage = new ShowMessageEvent();
						eventMessage.setWarningMessage(result.getWarningMessage());
						eventBus.fireEvent(eventMessage);
					} else {
						String pg = result.getNumeroPg() + "/" + result.getAnnoPg();
						String pgCapofila = result.getNumeroPgCapofila() + "/" + result.getAnnoPgCapofila();

						if (datiDefaultProtocollazioneHandler.getModulisticaDTO() != null) {
							praticheDB.remove(datiDefaultProtocollazioneHandler.getModulisticaDTO().getClientID());
							mostraPaginaEsito(protocolla.getValueMap(), pg, pgCapofila, result.getFascicoloDTO().getClientID());
						} else {
							mostraPaginaEsito(protocolla.getValueMap(), pg, pgCapofila, result.getFascicoloDTO().getClientID());
						}
					}
				}

				@Override
				public void onFailure(Throwable caught) {
					ShowAppLoadingEvent.fire(FormProtocollazionePresenter.this, false);
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
					eventBus.fireEvent(event);
					ShowAppLoadingEvent.fire(FormProtocollazionePresenter.this, false);
				}
			});
		}
	}

	/* INDIETRO COMMAND */

	public class IndietroPecOutCommand implements Command {

		@Override
		public void execute() {
			MostraSceltaCapofilaEmailOutEvent event = new MostraSceltaCapofilaEmailOutEvent();
			event.setIdFascicolo(datiDefaultProtocollazioneHandler.getFascicoloDTO().getClientID());
			event.setIdPecOut(datiDefaultProtocollazioneHandler.getEmailOutDTO().getClientID());
			event.setInteroperabile(emailInteroperabile);
			getEventBus().fireEvent(event);
		}
	}

	public class IndietroFascicoloCommand implements Command {

		@Override
		public void execute() {
			praticheDB.getFascicoloByPath(datiDefaultProtocollazioneHandler.getFascicoloDTO().getClientID(), false, new PraticaFascicoloLoaded() {

				@Override
				public void onPraticaLoaded(FascicoloDTO fascicolo) {
					if (!PraticaUtil.isFascicoloRiservato(fascicolo.getTipologiaPratica())) {
						MostraSceltaCapofilaProtocollazioneFascicoloEvent event = new MostraSceltaCapofilaProtocollazioneFascicoloEvent();
						event.setIdFascicolo(datiDefaultProtocollazioneHandler.getFascicoloDTO().getClientID());

						Set<SelectedObject> praticheSelezionate = new HashSet<SelectedObject>();
						for (PraticaDTO p : pratiche) {
							praticheSelezionate.add(new SelectedObject(p.getClientID(), SelectedObject.getObjectType(p)));
						}

						event.setListEmail(praticheSelezionate);

						event.setAllegati(allegati);
						getEventBus().fireEvent(event);

					} else
						getEventBus().fireEvent(new BackFromPlaceEvent());
				}

				@Override
				public void onPraticaError(String error) {
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(error);
					eventBus.fireEvent(event);
				}
			});
		}
	}

	public class IndietroSceltaFascicoloCommand implements Command {

		@Override
		public void execute() {
			MostraSceltaCapofilaSceltaFascicoloEvent event = new MostraSceltaCapofilaSceltaFascicoloEvent();
			event.setIdFascicolo(datiDefaultProtocollazioneHandler.getFascicoloDTO().getClientID());
			
			if(datiDefaultProtocollazioneHandler.getEmailInDTO() != null){
				event.setIdEmailIn(datiDefaultProtocollazioneHandler.getEmailInDTO().getClientID());
			} else {
				event.setIdPraticaModulistica(datiDefaultProtocollazioneHandler.getModulisticaDTO().getClientID());
			}
			getEventBus().fireEvent(event);

		}
	}

	public class IndietroPecInCommand implements Command {

		@Override
		public void execute() {
			MostraSceltaCapofilaEmailInEvent event = new MostraSceltaCapofilaEmailInEvent();
			event.setCreaFascicoloDTO(creaFascicoloDTO);
			getEventBus().fireEvent(event);

		}
	}

	public class IndietroPraticaModulisticaCommand implements Command {

		@Override
		public void execute() {
			MostraSceltaCapofilaPraticaModulisticaEvent event = new MostraSceltaCapofilaPraticaModulisticaEvent();
			event.setCreaFascicoloDTO(creaFascicoloDTO);
			getEventBus().fireEvent(event);

		}
	}

	/* ANNULLA COMMAND */

	public class AnnullaFascicoloCommand implements Command {

		@Override
		public void execute() {
			praticheDB.getFascicoloByPath(datiDefaultProtocollazioneHandler.getFascicoloDTO().getClientID(), false, new PraticaFascicoloLoaded() {

				@Override
				public void onPraticaLoaded(FascicoloDTO fascicolo) {
					Place place = new Place();
					place.setToken(NameTokens.dettagliofascicolo);
					place.addParam(NameTokensParams.idPratica, datiDefaultProtocollazioneHandler.getFascicoloDTO().getClientID());
					getEventBus().fireEvent(new GoToPlaceEvent(place));
				}

				@Override
				public void onPraticaError(String error) {
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(error);
					eventBus.fireEvent(event);
				}
			});
		}
	}

	public class AnnullaSceltaFascicoloCommand implements Command {

		@Override
		public void execute() {
			
			if(datiDefaultProtocollazioneHandler.getEmailInDTO() != null){
				RilasciaInCaricoPecInEvent event = new RilasciaInCaricoPecInEvent(datiDefaultProtocollazioneHandler.getEmailInDTO().getClientID());
				eventBus.fireEvent(event);	
			} else {
				Place place = new Place();
				place.setToken(NameTokens.dettagliopraticamodulistica);
				place.addParam(NameTokensParams.idPratica, datiDefaultProtocollazioneHandler.getModulisticaDTO().getClientID());
				getEventBus().fireEvent(new GoToPlaceEvent(place));
			}
			
			
		}
	}

	public class AnnullaPecInCommand implements Command {

		@Override
		public void execute() {
			RilasciaInCaricoPecInEvent event = new RilasciaInCaricoPecInEvent(datiDefaultProtocollazioneHandler.getEmailInDTO().getClientID());
			eventBus.fireEvent(event);
		}
	}

	public class AnnullaPraticaModulisticaCommand implements Command {

		@Override
		public void execute() {
			Place place = new Place();
			place.setToken(NameTokens.dettagliopraticamodulistica);
			place.addParam(NameTokensParams.idPratica, datiDefaultProtocollazioneHandler.getModulisticaDTO().getClientID());
			getEventBus().fireEvent(new GoToPlaceEvent(place));
		}
	}

	public class AnnullaPecOutCommand implements Command {

		@Override
		public void execute() {
			praticheDB.getFascicoloByPath(datiDefaultProtocollazioneHandler.getFascicoloDTO().getClientID(), false, new PraticaFascicoloLoaded() {

				@Override
				public void onPraticaLoaded(FascicoloDTO fascicolo) {
					Place place = new Place();
					place.setToken(NameTokens.dettagliofascicolo);
					place.addParam(NameTokensParams.idPratica, datiDefaultProtocollazioneHandler.getFascicoloDTO().getClientID());
					getEventBus().fireEvent(new GoToPlaceEvent(place));
				}

				@Override
				public void onPraticaError(String error) {
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(error);
					eventBus.fireEvent(event);
				}
			});
		}
	}

	@Override
	@ProxyEvent
	public void onTornaAFormDiProtocollazione(TornaAFormDiProtocollazioneEvent event) {
		reloadForm = false;
		revealForm();
	}

}