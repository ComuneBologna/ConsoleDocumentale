package it.eng.portlet.consolepec.gwt.client.presenter.fascicolo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.common.base.Strings;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SuggestBox;
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

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaFascicolo;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaFascicolo.ConfigurazioneEsecuzione;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.CreazioneFascicoloAbilitazione;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo;
import it.eng.cobo.consolepec.security.handler.AutorizzazioneHandler.CondizioneAbilitazione;
import it.eng.cobo.consolepec.security.handler.AutorizzazioneHandler.QueryAbilitazione;
import it.eng.cobo.consolepec.util.generics.GenericsUtil;
import it.eng.cobo.consolepec.util.pratica.PraticaUtil;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaEmaiInlLoaded;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaModulisticaLoaded;
import it.eng.portlet.consolepec.gwt.client.event.BackFromPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.event.GoToPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.event.MostraDatiPraticaEvent;
import it.eng.portlet.consolepec.gwt.client.event.MostraDatiPraticaEvent.MostraDatiPraticaHandler;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.event.confermaprotocollazione.ConfermaProtocollazionePecInEvent;
import it.eng.portlet.consolepec.gwt.client.event.confermaprotocollazione.ConfermaProtocollazionePraticaModulisticaEvent;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.place.NameTokens;
import it.eng.portlet.consolepec.gwt.client.place.NameTokensParams;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter.Place;
import it.eng.portlet.consolepec.gwt.client.presenter.event.rubrica.MostraListaAnagraficheEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.event.rubrica.MostraListaAnagraficheEvent.MostraListaAnagraficheHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.event.rubrica.SelezionaAnagraficaFineEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.event.rubrica.SelezionaAnagraficaFineEvent.SelezionaAnagraficaFineHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.pec.event.RilasciaInCaricoPecInEvent;
import it.eng.portlet.consolepec.gwt.client.util.GestionePraticaModulisticaUtil;
import it.eng.portlet.consolepec.gwt.client.widget.SitemapMenu;
import it.eng.portlet.consolepec.gwt.client.widget.datiaggiuntivi.FormDatiAggiuntiviWidget;
import it.eng.portlet.consolepec.gwt.client.widget.suggestBox.oracle.AnagraficheRuoliAbilitatiSuggestOracle;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants.VociRootSiteMap;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecUtils;
import it.eng.portlet.consolepec.gwt.shared.action.ValidazioneDatiAggiuntivi;
import it.eng.portlet.consolepec.gwt.shared.action.ValidazioneDatiAggiuntiviResult;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CreaFascicoloAction;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CreaFascicoloActionResult;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CreaFascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.action.pec.CambiaStatoPecInAction;
import it.eng.portlet.consolepec.gwt.shared.action.pec.CambiaStatoPecInActionResult;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PecInDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaModulisticaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.ValidazioneDatoAggiuntivoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.ValoreModuloDTO;

public class CreaFascicoloFormPresenter extends Presenter<CreaFascicoloFormPresenter.MyView, CreaFascicoloFormPresenter.MyProxy> implements MostraDatiPraticaHandler, SelezionaAnagraficaFineHandler, MostraListaAnagraficheHandler {
	private final EventBus eventBus;
	private String clientID;
	private final DispatchAsync dispatcher;
	private final PecInPraticheDB praticheDB;
	private Boolean isSvuotaCampi = true;
	private SuggestBox gruppiSuggestBox;
	private AnagraficheRuoliAbilitatiSuggestOracle<CreazioneFascicoloAbilitazione> suggestOracleRuoli;
	private final SitemapMenu siteMapMenu;
	private FascicoloDTO fascicoloDTO;
	private String creazioneDaTipoPratica;
	private ProfilazioneUtenteHandler profilazioneUtenteHandler;
	private ConfigurazioniHandler configurazioniHandler;
	private String fascicoloDefault;
	private boolean backFromSelezioneAnagrafica = false;

	public interface MyView extends View {
		public String getTitolo();

		public void setRequiredField();

		public void svuotaCampi();

		public Date getDataCreazionePratica();

		public void setDataCreazionePratica(Date date);

		public String getUtente();

		public void setUtente(String utente);

		public void init();

		public String getNote();

		public void hideButtonAnnulla();

		public void showButtonAnnulla();

		public void setAnnullaCommand(AnnullaCreaFascicoloCommand annullaCreaFascicoloCommand);

		public void setAvantiCommand(AvantiCreaFascicoloCommand avantiCreaFascicoloCommand);

		public void setGruppiSuggestBox(SuggestBox gruppiSuggestBox);

		public void setAvantiEnabled(boolean b);

		public void setOnChangeTipoFascicoloCommand(Command command);

		public TipologiaPratica getTipologiaFascicolo();

		public void setTipoFascicolo(String tipoFascicolo);

		public void clearListBox();

		public void setTipologiaFascicoloDefault(AnagraficaFascicolo tipoFascicolo);

		public void enableListTipologiePratiche();

		public void setDatiAggiuntivi(List<DatoAggiuntivo> set);

		public void abilitaTitolo(boolean enable, boolean forceClear);

		public List<DatoAggiuntivo> getDatiAggiuntivi();

		public boolean controlloDatiAggiuntivi();

		public boolean controlloServerDatiAggiuntivi(List<ValidazioneDatoAggiuntivoDTO> validazioneDatiAggiuntivi);

		void setSalvaFascicoloDefault(boolean salvaFascicoloDefault);

		boolean isSalvaFascicoloDefault();

		void loadFormDatiAggiuntivi(EventBus eventBus, Object openingRequestor, DispatchAsync dispatcher);

		FormDatiAggiuntiviWidget getFormDatiAggiuntivi();
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.creafascicolo)
	public interface MyProxy extends ProxyPlace<CreaFascicoloFormPresenter> {
		//
	}

	@Inject
	public CreaFascicoloFormPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, final DispatchAsync dispatcher, PecInPraticheDB db, final SitemapMenu siteMapMenu,
			ProfilazioneUtenteHandler profilazioneUtenteHandler, ConfigurazioniHandler configurazioniHandler) {

		super(eventBus, view, proxy);
		this.eventBus = eventBus;
		this.dispatcher = dispatcher;
		this.praticheDB = db;
		this.siteMapMenu = siteMapMenu;
		this.profilazioneUtenteHandler = profilazioneUtenteHandler;
		this.configurazioniHandler = configurazioniHandler;
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPresenter.TYPE_SetMainContent, this);

	}

	@Override
	protected void onBind() {
		super.onBind();
		getView().init();
		getView().loadFormDatiAggiuntivi(eventBus, this, dispatcher);
		getView().setAnnullaCommand(new AnnullaCreaFascicoloCommand());
		getView().setAvantiCommand(new AvantiCreaFascicoloCommand());
		getView().setOnChangeTipoFascicoloCommand(new OnChangeTipoFascicoloCommand());

	}

	@Override
	protected void onHide() {
		super.onHide();
		dropMessage();
	}

	@Override
	protected void onReveal() {
		super.onReveal();
		siteMapMenu.setActiveVoce(VociRootSiteMap.CREA_FASCICOLO.getId());
		initFascicoliGruppi();
	}

	protected void mostraFormCreaFascicolo(String utente, Boolean isCreazioneFascicoloFromPEC) {
		if (isSvuotaCampi) {
			getView().svuotaCampi();
		}
		getView().setUtente(utente);
		getView().setDataCreazionePratica(new Date());
		ShowMessageEvent event = new ShowMessageEvent();
		event.setMessageDropped(true);
		eventBus.fireEvent(event);
		if (isCreazioneFascicoloFromPEC) {
			getView().showButtonAnnulla();
		} else {
			getView().hideButtonAnnulla();
		}
	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);

		if (profilazioneUtenteHandler.isAbilitato(CreazioneFascicoloAbilitazione.class)) {
			clientID = request.getParameter(NameTokensParams.idPratica, null);
			isSvuotaCampi = Boolean.parseBoolean(request.getParameter(NameTokensParams.svuotaCampi, Boolean.toString(false)));
			creazioneDaTipoPratica = request.getParameter(NameTokensParams.creazioneDaTipoPratica, null);
			mostraFormCreaFascicolo(profilazioneUtenteHandler.getDatiUtente().getNomeCompleto(), true);

		} else {
			throw new IllegalArgumentException("Utente non abilitato alla creazione di fascicoli");

		}
	}

	private void manageCheckBoxFascicoloDefault(String tipoFascicolo) {
		if (tipoFascicolo.equalsIgnoreCase(profilazioneUtenteHandler.getPreferenzeUtente().getFascicoloDefault())) {
			getView().setSalvaFascicoloDefault(true);
		} else
			getView().setSalvaFascicoloDefault(false);
	}

	public class OnChangeTipoFascicoloCommand implements com.google.gwt.user.client.Command {
		@Override
		public void execute() {
			final TipologiaPratica tf = getView().getTipologiaFascicolo();

			QueryAbilitazione<CreazioneFascicoloAbilitazione> qab = new QueryAbilitazione<CreazioneFascicoloAbilitazione>();
			qab.addCondition(new CondizioneAbilitazione<CreazioneFascicoloAbilitazione>() {

				@Override
				protected boolean valutaCondizione(CreazioneFascicoloAbilitazione abilitazione) {
					return abilitazione.getTipo().equals(tf.getNomeTipologia());
				}
			});

			suggestOracleRuoli.filtra(CreazioneFascicoloAbilitazione.class, qab);
			gruppiSuggestBox.setEnabled(suggestOracleRuoli.getAnagraficheRuoli().size() > 1);

			if (suggestOracleRuoli.getAnagraficheRuoli().size() == 1) {
				gruppiSuggestBox.setValue(suggestOracleRuoli.getAnagraficheRuoli().iterator().next().getEtichetta());
				gruppiSuggestBox.setStyleName("testo disabilitato");
				gruppiSuggestBox.setEnabled(false);

			} else if (suggestOracleRuoli.getAnagraficheRuoli().isEmpty()) {
				ShowMessageEvent event = new ShowMessageEvent();
				event.setErrorMessage("Tipologia fascicolo non abilitata");
				eventBus.fireEvent(event);

			} else {
				gruppiSuggestBox.setText("");
				gruppiSuggestBox.removeStyleName("disabilitato");
				gruppiSuggestBox.setEnabled(true);
			}

			AnagraficaFascicolo anagraficaFascicolo = configurazioniHandler.getAnagraficaFascicolo(tf.getNomeTipologia());
			getView().setDatiAggiuntivi(anagraficaFascicolo.getDatiAggiuntivi());
			getView().abilitaTitolo(Strings.isNullOrEmpty(anagraficaFascicolo.getTemplateTitolo()), false);
			manageCheckBoxFascicoloDefault(tf.getNomeTipologia());
		}
	}

	public class AnnullaCreaFascicoloCommand implements com.google.gwt.user.client.Command {

		@Override
		public void execute() {
			getView().svuotaCampi();
			if (clientID != null && !TipologiaPratica.PRATICA_MODULISTICA.getNomeTipologia().equals(creazioneDaTipoPratica)) {
				ShowAppLoadingEvent.fire(CreaFascicoloFormPresenter.this, true);
				CambiaStatoPecInAction action = new CambiaStatoPecInAction(clientID, CambiaStatoPecInAction.Azione.RILASCIA_IN_CARICO);
				dispatcher.execute(action, new AsyncCallback<CambiaStatoPecInActionResult>() {

					@Override
					public void onSuccess(CambiaStatoPecInActionResult result) {
						ShowAppLoadingEvent.fire(CreaFascicoloFormPresenter.this, false);
						if (!result.getIsError()) {
							praticheDB.remove(clientID);
							RilasciaInCaricoPecInEvent event = new RilasciaInCaricoPecInEvent(clientID);
							eventBus.fireEvent(event);
						} else {
							ShowMessageEvent event = new ShowMessageEvent();
							event.setErrorMessage(result.getMessErr());
							eventBus.fireEvent(event);
						}
					}

					@Override
					public void onFailure(Throwable caught) {
						ShowAppLoadingEvent.fire(CreaFascicoloFormPresenter.this, false);
						ShowMessageEvent event = new ShowMessageEvent();
						event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
						eventBus.fireEvent(event);
					}
				});
			} else
				getEventBus().fireEvent(new BackFromPlaceEvent());
		}

	}

	public class AvantiCreaFascicoloCommand implements com.google.gwt.user.client.Command {

		@Override
		public void execute() {
			String ruolo = gruppiSuggestBox.getValue();
			boolean condizioneTitolo = getView().getTitolo() != null && !getView().getTitolo().trim().equals("");
			boolean condizioneGruppo = !ruolo.trim().equals("") && profilazioneUtenteHandler.getAnagraficaRuoloUtenteByEtichetta(ruolo) != null;
			boolean condizioneDatiAggiuntivi = getView().controlloDatiAggiuntivi();
			boolean condizioneTipo = getView().getTipologiaFascicolo() != null && configurazioniHandler.getAnagraficaFascicolo(getView().getTipologiaFascicolo().getNomeTipologia()) != null;

			if (!condizioneTitolo || !condizioneGruppo || !condizioneDatiAggiuntivi || !condizioneTipo) {
				// parametri mancanti
				String messaggio = "I campi in rosso devono essere valorizzati correttamente";
				getView().setRequiredField();
				if (!condizioneGruppo) {
					gruppiSuggestBox.getElement().setAttribute("required", "required");
				} else {
					gruppiSuggestBox.getElement().removeAttribute("required");
				}
				ShowMessageEvent event = new ShowMessageEvent();
				event.setWarningMessage(messaggio);
				eventBus.fireEvent(event);
			} else {

				ShowMessageEvent event = new ShowMessageEvent();
				event.setMessageDropped(true);
				eventBus.fireEvent(event);

				ValidazioneDatiAggiuntivi validazioneDatiAggiuntivi = new ValidazioneDatiAggiuntivi(getView().getDatiAggiuntivi());

				ShowAppLoadingEvent.fire(CreaFascicoloFormPresenter.this, true);

				CreaFascicoloFormPresenter.this.dispatcher.execute(validazioneDatiAggiuntivi, new AsyncCallback<ValidazioneDatiAggiuntiviResult>() {

					@Override
					public void onFailure(Throwable error) {
						ShowAppLoadingEvent.fire(CreaFascicoloFormPresenter.this, false);
						ShowMessageEvent event = new ShowMessageEvent();
						event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
						eventBus.fireEvent(event);
					}

					@Override
					public void onSuccess(ValidazioneDatiAggiuntiviResult result) {
						ShowAppLoadingEvent.fire(CreaFascicoloFormPresenter.this, false);

						if (result.getError()) {
							ShowMessageEvent event = new ShowMessageEvent();
							event.setErrorMessage(result.getMessError());
							eventBus.fireEvent(event);

						} else if (!result.getErroriDaVisualizzare().isEmpty()) {
							getView().controlloServerDatiAggiuntivi(result.getValidazioneDatiAggiuntivi());
							ShowMessageEvent event = new ShowMessageEvent();
							event.setErrorMessage(GenericsUtil.format(result.getErroriDaVisualizzare()));
							eventBus.fireEvent(event);

						} else if (getView().controlloServerDatiAggiuntivi(result.getValidazioneDatiAggiuntivi())) {

							if (getFascicoloDefault() != null) {
								profilazioneUtenteHandler.aggiornaPreferenzeUtente(null, getFascicoloDefault(), null, null);
							}

							creaFascicolo();
						}
					}
				});
			}
		}
	}

	private String getFascicoloDefault() {
		/**
		 * 
		 * Gestione salvataggio fascicolo default: 1) Se l'utente ha selezionato la checkbox per salvare allora salva/aggiorna il fascicolo di default 2) Se la checkbox è deselezionata si gestiscono
		 * due casi: - Se c'era un fascicolo di default e il tipo di fascicolo scelto corrisponde allora va eliminata l'impostazione fascicolo di default - Altrimenti resta tutto invariato
		 * 
		 */
		boolean isSalvaFascicoloDefault = getView().isSalvaFascicoloDefault();
		String fascicoloDefault = null;

		if (isSalvaFascicoloDefault) {
			fascicoloDefault = getView().getTipologiaFascicolo().getNomeTipologia();

		} else if (!Strings.isNullOrEmpty(this.fascicoloDefault) && this.fascicoloDefault.equalsIgnoreCase(getView().getTipologiaFascicolo().getNomeTipologia())) {
			fascicoloDefault = "";
		}

		return fascicoloDefault;
	}

	private void creaFascicolo() {
		/* reset dei messaggi di errore */
		ShowMessageEvent event = new ShowMessageEvent();
		event.setMessageDropped(true);
		eventBus.fireEvent(event);
		creaFascicoloINT(getView().getTipologiaFascicolo());
	}

	private void creaFascicoloINT(TipologiaPratica tipologiaFascicolo) {

		AnagraficaFascicolo af = configurazioniHandler.getAnagraficaFascicolo(getView().getTipologiaFascicolo().getNomeTipologia());

		if (clientID != null) {
			ShowAppLoadingEvent.fire(CreaFascicoloFormPresenter.this, false);
			final CreaFascicoloDTO creaFascicoloDTO = new CreaFascicoloDTO();
			creaFascicoloDTO.setTitolazione(af.getTitolazione());
			creaFascicoloDTO.setNote(getView().getNote());
			creaFascicoloDTO.setTipologiaFascicolo(tipologiaFascicolo);
			creaFascicoloDTO.setTitolo(getView().getTitolo());
			creaFascicoloDTO.setUtente(getView().getUtente());
			creaFascicoloDTO.setAssegnatario(gruppiSuggestBox.getValue());
			creaFascicoloDTO.setClientID(clientID);
			creaFascicoloDTO.getDatiAggiuntivi().addAll(getView().getDatiAggiuntivi());
			creaFascicoloDTO.setProtocollazioneRiservata(af.isProtocollazioneRiservata());

			if (PraticaUtil.isSport(getView().getTipologiaFascicolo())) {
				ShowAppLoadingEvent.fire(CreaFascicoloFormPresenter.this, true);
				praticheDB.getPraticaModulisticaByPath(clientID, true, new PraticaModulisticaLoaded() {

					@Override
					public void onPraticaModulisticaLoaded(PraticaModulisticaDTO pratica) {
						ShowAppLoadingEvent.fire(CreaFascicoloFormPresenter.this, false);
						GestionePraticaModulisticaUtil.aggiungiValoreAggiuntivo(creaFascicoloDTO, clientID);
						Map<String, ValoreModuloDTO> mappaValori = ConsolePecUtils.buildValoriTotaliMapByNome(pratica);
						ValoreModuloDTO dataInizioBando = mappaValori.get(GestionePraticaModulisticaUtil.MODULO_DATA_INIZIO_BANDO);
						if (dataInizioBando != null)
							GestionePraticaModulisticaUtil.aggiungiDataInizioBando(creaFascicoloDTO, dataInizioBando.getDescrizione(), clientID);
						ValoreModuloDTO dataFineBando = mappaValori.get(GestionePraticaModulisticaUtil.MODULO_DATA_FINE_BANDO);
						if (dataFineBando != null)
							GestionePraticaModulisticaUtil.aggiungiDataFineBando(creaFascicoloDTO, dataFineBando.getDescrizione(), clientID);
						ValoreModuloDTO emailRichiedente = mappaValori.get(GestionePraticaModulisticaUtil.MODULO_EMAIL_PROVENIENZA_MODULO);
						if (emailRichiedente != null)
							GestionePraticaModulisticaUtil.aggiungiEmailProvenienzaModulo(creaFascicoloDTO, emailRichiedente.getDescrizione(), clientID);
						avanti(creaFascicoloDTO);
					}

					@Override
					public void onPraticaModulisticaError(String error) {
						ShowAppLoadingEvent.fire(CreaFascicoloFormPresenter.this, false);
						ShowMessageEvent event = new ShowMessageEvent();
						event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
						getEventBus().fireEvent(event);
					}
				});
			} else {
				avanti(creaFascicoloDTO);
			}
		} else {
			CreaFascicoloAction creaFascicoloAction = new CreaFascicoloAction();
			creaFascicoloAction.setNote(getView().getNote());
			creaFascicoloAction.setTipologiaFascicolo(tipologiaFascicolo);
			creaFascicoloAction.setTitolo(getView().getTitolo());
			creaFascicoloAction.setUtente(getView().getUtente());
			creaFascicoloAction.setAssegnatario(gruppiSuggestBox.getValue());
			creaFascicoloAction.setClientID(clientID);
			creaFascicoloAction.getDatiAggiuntivi().addAll(getView().getDatiAggiuntivi());

			ShowAppLoadingEvent.fire(CreaFascicoloFormPresenter.this, true);

			this.dispatcher.execute(creaFascicoloAction, new AsyncCallback<CreaFascicoloActionResult>() {

				@Override
				public void onFailure(Throwable caught) {
					ShowAppLoadingEvent.fire(CreaFascicoloFormPresenter.this, false);
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
					eventBus.fireEvent(event);
				}

				@Override
				public void onSuccess(CreaFascicoloActionResult result) {
					ShowAppLoadingEvent.fire(CreaFascicoloFormPresenter.this, false);

					if (result.getError()) {
						ShowMessageEvent event = new ShowMessageEvent();
						event.setErrorMessage(result.getMessageError());
						eventBus.fireEvent(event);

					} else {
						ShowMessageEvent event = new ShowMessageEvent();
						event.setMessageDropped(true);
						eventBus.fireEvent(event);
						fascicoloDTO = result.getFascicoloDTO();

						mostraDettaglioFascicolo(fascicoloDTO);
					}
				}
			});
		}
	}

	private void avanti(final CreaFascicoloDTO creaFascicoloDTO) {

		/* porta direttamente il DTO della creazione del fascicolo alla parte di scelta prot si/no */

		if (TipologiaPratica.PRATICA_MODULISTICA.getNomeTipologia().equals(creazioneDaTipoPratica)) {
			praticheDB.getPraticaModulisticaByPath(clientID, siteMapMenu.containsLink(clientID), new PraticaModulisticaLoaded() {

				@Override
				public void onPraticaModulisticaLoaded(PraticaModulisticaDTO pratica) {
					ConfermaProtocollazionePraticaModulisticaEvent event = new ConfermaProtocollazionePraticaModulisticaEvent();
					event.setCreaFascicoloDTO(creaFascicoloDTO);
					event.setIdPraticaModulistica(clientID);
					getEventBus().fireEvent(event);
				}

				@Override
				public void onPraticaModulisticaError(String error) {
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
					eventBus.fireEvent(event);
				}
			});

		} else {
			praticheDB.getPecInByPath(clientID, siteMapMenu.containsLink(clientID), new PraticaEmaiInlLoaded() {

				@Override
				public void onPraticaLoaded(PecInDTO pec) {
					ConfermaProtocollazionePecInEvent event = new ConfermaProtocollazionePecInEvent();
					event.setCreaFascicoloDTO(creaFascicoloDTO);
					event.setIdEmailIn(clientID);
					getEventBus().fireEvent(event);

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

	private void mostraDettaglioFascicolo(FascicoloDTO fascicoloDTO) {
		Place place = new Place();
		place.setToken(NameTokens.dettagliofascicolo);
		place.addParam(NameTokensParams.idPratica, fascicoloDTO.getClientID());
		getEventBus().fireEvent(new GoToPlaceEvent(place));
	}

	@Override
	@ProxyEvent
	public void onMostraDatiPratica(MostraDatiPraticaEvent event) {
		clientID = event.getClientID();
		isSvuotaCampi = event.isSvuotaCampi();
		this.revealInParent();
	}

	private void initFascicoliGruppi() {
		if (backFromSelezioneAnagrafica) {
			backFromSelezioneAnagrafica = false;
			return;
		}

		List<AnagraficaFascicolo> anagraficheFascicoli = new ArrayList<AnagraficaFascicolo>(profilazioneUtenteHandler.getAnagraficheFascicoliAbilitati(CreazioneFascicoloAbilitazione.class));

		if (clientID != null) {
			anagraficheFascicoli = configurazioniHandler.filtraFascicoloPersonale(anagraficheFascicoli);
		}

		getView().clearListBox();

		for (final AnagraficaFascicolo af : anagraficheFascicoli) {
			QueryAbilitazione<CreazioneFascicoloAbilitazione> qab = new QueryAbilitazione<CreazioneFascicoloAbilitazione>();
			qab.addCondition(new CondizioneAbilitazione<CreazioneFascicoloAbilitazione>() {

				@Override
				protected boolean valutaCondizione(CreazioneFascicoloAbilitazione abilitazione) {
					return abilitazione.getTipo().equals(af.getNomeTipologia());
				}
			});

			List<AnagraficaRuolo> aruoli = new ArrayList<AnagraficaRuolo>(profilazioneUtenteHandler.getAnagraficheRuoliAbilitati(CreazioneFascicoloAbilitazione.class, qab));

			if (aruoli != null && !aruoli.isEmpty() && !af.getNomeTipologia().equals(TipologiaPratica.FASCICOLO_PERSONALE.getNomeTipologia())) {
				getView().setTipoFascicolo(af.getEtichettaTipologia());
			}

			if (clientID == null && af.getNomeTipologia().equals(TipologiaPratica.FASCICOLO_PERSONALE.getNomeTipologia())) {
				getView().setTipoFascicolo(af.getEtichettaTipologia());
			}
		}

		final AnagraficaFascicolo defaultFascicolo = manageFascicoloDefault(clientID == null);

		List<DatoAggiuntivo> datiAggiuntiviDefault = defaultFascicolo.getDatiAggiuntivi();
		if (datiAggiuntiviDefault != null && !datiAggiuntiviDefault.isEmpty()) {
			getView().setDatiAggiuntivi(datiAggiuntiviDefault);
		}

		QueryAbilitazione<CreazioneFascicoloAbilitazione> qab = new QueryAbilitazione<CreazioneFascicoloAbilitazione>();
		qab.addCondition(new CondizioneAbilitazione<CreazioneFascicoloAbilitazione>() {

			@Override
			protected boolean valutaCondizione(CreazioneFascicoloAbilitazione abilitazione) {
				return abilitazione.getTipo().equals(defaultFascicolo.getNomeTipologia());
			}
		});

		suggestOracleRuoli = new AnagraficheRuoliAbilitatiSuggestOracle<CreazioneFascicoloAbilitazione>(CreazioneFascicoloAbilitazione.class, qab, profilazioneUtenteHandler);
		gruppiSuggestBox = new SuggestBox(suggestOracleRuoli);

		List<AnagraficaRuolo> ar = new ArrayList<AnagraficaRuolo>(profilazioneUtenteHandler.getAnagraficheRuoliAbilitati(CreazioneFascicoloAbilitazione.class, qab));

		if (ar == null || ar.isEmpty()) {
			throw new IllegalArgumentException("Il tipo pratica: " + defaultFascicolo.getEtichettaTipologia() + " non ha nessun ruolo associato.");

		} else if (ar.size() == 1) {
			gruppiSuggestBox.setValue(ar.iterator().next().getEtichetta());
			gruppiSuggestBox.setEnabled(false);
			gruppiSuggestBox.setStyleName("testo disabilitato");

		} else {
			gruppiSuggestBox.removeStyleName("disabilitato");
		}

		getView().setGruppiSuggestBox(gruppiSuggestBox);
		getView().enableListTipologiePratiche();
	}

	private AnagraficaFascicolo manageFascicoloDefault(boolean fascicoloPersonaleEnabled) {

		AnagraficaFascicolo tipoDefault = null;
		boolean hasTipoDefault = false;
		List<AnagraficaFascicolo> listaPratiche = new ArrayList<AnagraficaFascicolo>(profilazioneUtenteHandler.getAnagraficheFascicoliAbilitati(CreazioneFascicoloAbilitazione.class));

		/**
		 * PRENDO IL FASCICOLO DI DEFAULT DALLE PREFERENZE DELL'UTENTE SE PRESENTE
		 */
		if (!Strings.isNullOrEmpty(profilazioneUtenteHandler.getPreferenzeUtente().getFascicoloDefault())) {

			for (AnagraficaFascicolo pratica : listaPratiche) {
				if (profilazioneUtenteHandler.getPreferenzeUtente().getFascicoloDefault().equalsIgnoreCase(pratica.getNomeTipologia())) {
					tipoDefault = pratica;
					hasTipoDefault = true;
					break;
				}
			}

			if ((tipoDefault == null) || (tipoDefault != null && tipoDefault.getNomeTipologia().equals(TipologiaPratica.FASCICOLO_PERSONALE.getNomeTipologia()) && !fascicoloPersonaleEnabled))
				tipoDefault = getTipologiaPraticaDefault(listaPratiche);

		} else {
			tipoDefault = getTipologiaPraticaDefault(listaPratiche);
		}

		if (tipoDefault == null)
			throw new IllegalStateException();

		getView().setTipologiaFascicoloDefault(tipoDefault);
		getView().abilitaTitolo(Strings.isNullOrEmpty(tipoDefault.getTemplateTitolo()), true);

		if (hasTipoDefault) {
			getView().setSalvaFascicoloDefault(true);
			this.fascicoloDefault = tipoDefault.getNomeTipologia();

		} else
			getView().setSalvaFascicoloDefault(false);

		return tipoDefault;
	}

	private AnagraficaFascicolo getTipologiaPraticaDefault(List<AnagraficaFascicolo> listaPratiche) {
		if (listaPratiche.size() == 1)
			return listaPratiche.get(0);

		List<AnagraficaFascicolo> af = configurazioniHandler.filtraFascicoloPersonale(listaPratiche);

		if (!af.isEmpty())
			return af.get(0);

		throw new IllegalStateException();
	}

	private void dropMessage() {
		ShowMessageEvent event = new ShowMessageEvent();
		event.setMessageDropped(true);
		eventBus.fireEvent(event);
	}

	@Override
	@ProxyEvent
	public void onAnagraficaSelezionata(SelezionaAnagraficaFineEvent event) {
		if (this.equals(event.getOpeningRequestor())) {
			if (!event.isAnnulla()) {
				List<ConfigurazioneEsecuzione> esecuzioni = Collections.emptyList();
				AnagraficaFascicolo af = configurazioniHandler.getAnagraficaFascicolo(getView().getTipologiaFascicolo().getNomeTipologia());
				if (af != null) {
					esecuzioni = af.getConfigurazioneEsecuzioni();
				}
				getView().getFormDatiAggiuntivi().setAnagrafica(event.getNomeDatoAggiuntivo(), event.getAnagrafica(), esecuzioni);
			}
			backFromSelezioneAnagrafica = true;
			revealInParent();
		}
	}

	@Override
	@ProxyEvent
	public void onMostraListaAnagrafiche(MostraListaAnagraficheEvent event) {
		if (this.equals(event.getOpeningRequestor())) {
			revealInParent();
		}
	}

}
