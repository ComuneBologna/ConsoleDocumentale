package it.eng.portlet.consolepec.gwt.client.presenter.fascicolo;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

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

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.util.pratica.PraticaUtil;
import it.eng.portlet.consolepec.gwt.client.command.RicercaPraticheServerAdapter;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaFascicoloLoaded;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaModulisticaLoaded;
import it.eng.portlet.consolepec.gwt.client.event.BackFromPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.event.GoToPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.event.collegamento.AzioneCollegamento;
import it.eng.portlet.consolepec.gwt.client.event.collegamento.ChiudiCollegaFascicoloConRicercaEvent;
import it.eng.portlet.consolepec.gwt.client.event.collegamento.ChiudiCollegaFascicoloConRicercaEvent.ChiudiCollegaFascicoloConRicercaHandler;
import it.eng.portlet.consolepec.gwt.client.event.collegamento.MostraCollegaFascicoloEvent;
import it.eng.portlet.consolepec.gwt.client.event.collegamento.MostraCollegaFascicoloEvent.TipoMostraCollegaFascicolo;
import it.eng.portlet.consolepec.gwt.client.event.confermaprotocollazione.ConfermaProtocollazioneSceltaFascicoloEvent;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.place.NameTokens;
import it.eng.portlet.consolepec.gwt.client.place.NameTokensParams;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter.Place;
import it.eng.portlet.consolepec.gwt.client.presenter.RicercaCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.event.BackToFascicoloEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.event.SpostaAllegatiInizioEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.event.SpostaAllegatiInizioEvent.SpostaAllegatiEventHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.event.SpostaProtocollazioniInizioEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.event.SpostaProtocollazioniInizioEvent.SpostaProtocollazioniEventHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.modulistica.event.RilasciaInCaricoPraticaModulisticaEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.pec.event.MostraSceltaFascicoloEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.pec.event.MostraSceltaFascicoloEvent.MostraSceltaFascicoloHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.pec.event.RilasciaInCaricoPecInEvent;
import it.eng.portlet.consolepec.gwt.client.util.GestionePraticaModulisticaUtil;
import it.eng.portlet.consolepec.gwt.client.widget.SitemapMenu;
import it.eng.portlet.consolepec.gwt.client.worklist.SceltaFascicoloWorklistStrategy;
import it.eng.portlet.consolepec.gwt.client.worklist.WorklistStrategy;
import it.eng.portlet.consolepec.gwt.client.worklist.WorklistStrategy.RicercaCallback;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecUtils;
import it.eng.portlet.consolepec.gwt.shared.action.CercaPratiche;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.SalvaFascicolo;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.SalvaFascicoloResult;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.SpostaAllegatiAction;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.SpostaAllegatiResult;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.SpostaProtocollazioniAction;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.SpostaProtocollazioniResult;
import it.eng.portlet.consolepec.gwt.shared.action.modulistica.CambiaStatoPraticaModulistica;
import it.eng.portlet.consolepec.gwt.shared.action.modulistica.CambiaStatoPraticaModulisticaEnum;
import it.eng.portlet.consolepec.gwt.shared.action.modulistica.CambiaStatoPraticaModulisticaResult;
import it.eng.portlet.consolepec.gwt.shared.action.pec.AggiungiPraticaAFascicolo;
import it.eng.portlet.consolepec.gwt.shared.action.pec.AggiungiPraticaAFascicoloResult;
import it.eng.portlet.consolepec.gwt.shared.action.pec.CambiaStatoPecInAction;
import it.eng.portlet.consolepec.gwt.shared.action.pec.CambiaStatoPecInActionResult;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.ColonnaWorklist;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoElenco;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.StatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaModulisticaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.ValoreModuloDTO;

public class SceltaFascicoloPresenter extends Presenter<SceltaFascicoloPresenter.MyView, SceltaFascicoloPresenter.MyProxy> implements ChiudiCollegaFascicoloConRicercaHandler, MostraSceltaFascicoloHandler, SpostaAllegatiEventHandler, SpostaProtocollazioniEventHandler {

	public interface MyView extends View {

		Set<ConstraintViolation<CercaPratiche>> formRicercaToCercaPratiche(CercaPratiche action);

		void init(WorklistStrategy worklistStrategy, Command command);

		String getClientIdSlezionato();

		void setAvantiCommand(Command avantiCommand);

		void setAnnullaCommand(Command annullaCommand);

		public String getClientIdFascicoloSelezionato();

		void resetSelezioni();

		void aggiornaRigheSelezionate();

		void setProvenienza(String indirizzoMail);

		public void resetForm();

		public String getProvenienza();

		void setGruppiAbilitati(List<AnagraficaRuolo> gruppiAbilitati);
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.sceltafascicolo)
	public interface MyProxy extends ProxyPlace<SceltaFascicoloPresenter> {}

	private enum SceltaDa {
		DETTAGLIOPECIN, DETTAGLIOFASCIOLO, DETTAGLIOPRATICAMODULISTICA, SPOSTA_ALLEGATI, SPOSTA_PROTOCOLLAZIONI;
	}

	private String praticaPath;
	private String fascicoloId;
	private final PecInPraticheDB pecInDb;
	private SceltaFascicoloWorklistStrategy strategy;
	private final SitemapMenu sitemapMenu;
	private final RicercaPraticheServerAdapter ricercaAdapter;
	private final DispatchAsync dispatchAsync;
	private boolean firstReveal = true;
	private String mittente;
	private SceltaDa sceltaDa;
	private ProfilazioneUtenteHandler profilazioneUtenteHandler;

	private Set<AllegatoDTO> allegatiDaSpostare = new TreeSet<AllegatoDTO>();
	private Set<ElementoElenco> praticheProtocollateDaSpostare = new TreeSet<ElementoElenco>();
	private String fascicoloSorgenteClientId;

	@Inject
	public SceltaFascicoloPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, final PecInPraticheDB pecInDb, final DispatchAsync dispatchAsync, final SitemapMenu sitemapMenu,
			final RicercaPraticheServerAdapter ricercaAdapter, ProfilazioneUtenteHandler profilazioneUtenteHandler) {
		super(eventBus, view, proxy);
		this.pecInDb = pecInDb;
		this.sitemapMenu = sitemapMenu;
		this.ricercaAdapter = ricercaAdapter;
		this.dispatchAsync = dispatchAsync;
		this.profilazioneUtenteHandler = profilazioneUtenteHandler;
	}

	@Override
	protected void revealInParent() {
		Window.scrollTo(0, 0);
		RevealContentEvent.fire(this, MainPresenter.TYPE_SetMainContent, this);
	}

	@Override
	public void onHide() {
		dropMessage();
	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
		praticaPath = request.getParameter(NameTokensParams.idPratica, null);
		mittente = request.getParameter(NameTokensParams.mittente, null);
		String daPraticaModulistica = request.getParameter(NameTokensParams.praticaModulistica, null);
		init(daPraticaModulistica != null);
	}

	private void init(boolean fromPraticaModulistica) {
		if (!SceltaDa.SPOSTA_ALLEGATI.equals(sceltaDa) && !SceltaDa.SPOSTA_PROTOCOLLAZIONI.equals(sceltaDa)) {
			if (fromPraticaModulistica) {
				// vengo da una pratica modulistica e voglio assegnarla ad un fascicolo
				sceltaDa = SceltaDa.DETTAGLIOPRATICAMODULISTICA;

			} else if (mittente != null) {
				// vengo da un dettaglio pecin e voglio assegnare un'email ad un fascicolo
				sceltaDa = SceltaDa.DETTAGLIOPECIN;
				getView().setProvenienza(mittente);

			} else {
				// vengo da un dettaglio fascicolo e voglio collegare un fascicolo ad un altro: al momento è l'unico altro caso possibile
				sceltaDa = SceltaDa.DETTAGLIOFASCIOLO;
			}
		}

		if (firstReveal) {
			strategy = new SceltaFascicoloWorklistStrategy();
			strategy.setRicercaEventListener(new RicercaEventListener());
			strategy.setPraticheDB(pecInDb);
			strategy.setSitemapMenu(sitemapMenu);
			getView().init(strategy, new RicercaCommand(getEventBus(), strategy, getView().formRicercaToCercaPratiche(new CercaPratiche())));
			getView().setAvantiCommand(getAvantiCommand());
			getView().setAnnullaCommand(getAnnullaCommand());
			firstReveal = false;
		} else {
			getView().resetForm();
			if (mittente != null)
				getView().setProvenienza(mittente); // dopo il reset della forma va forzato nuovamente
			strategy.refreshDatiGrid();
		}

		getView().aggiornaRigheSelezionate();
		getView().resetSelezioni();

		if (SceltaDa.DETTAGLIOPECIN.equals(sceltaDa))
			getView().setGruppiAbilitati(profilazioneUtenteHandler.getAllAnagraficheRuoliSubordinati());
		else
			getView().setGruppiAbilitati(profilazioneUtenteHandler.getAnagraficheRuoloUtente());

	}

	private Command getAvantiCommand() {
		return new Command() {

			@Override
			public void execute() {
				switch (sceltaDa) {
				case DETTAGLIOPRATICAMODULISTICA:
					avantiPraticaModulistica();
					break;
				case DETTAGLIOPECIN:
					avanti();
					break;
				case DETTAGLIOFASCIOLO:
					avantiCollegamento();
					break;
				case SPOSTA_ALLEGATI:
					spostaAllegati();
					break;
				case SPOSTA_PROTOCOLLAZIONI:
					spostaProtocollazioni();
					break;
				}
			}
		};
	}

	private Command getAnnullaCommand() {
		return new Command() {

			@Override
			public void execute() {
				switch (sceltaDa) {
				case DETTAGLIOPRATICAMODULISTICA:
					annullaPraticaModulistica();
					break;
				case DETTAGLIOPECIN:
					annulla();
					break;
				case DETTAGLIOFASCIOLO:
					annullaCollegamento();
					break;
				case SPOSTA_ALLEGATI:
					backToFascicoloSorgente(fascicoloSorgenteClientId);
					break;
				case SPOSTA_PROTOCOLLAZIONI:
					backToFascicoloSorgente(fascicoloSorgenteClientId);
					break;
				}
			}
		};
	}

	protected void avantiCollegamento() {
		getEventBus().fireEvent(new MostraCollegaFascicoloEvent(praticaPath, getView().getClientIdFascicoloSelezionato(), TipoMostraCollegaFascicolo.CON_RICERCA));
	}

	protected void annullaCollegamento() {
		getEventBus().fireEvent(new BackFromPlaceEvent());
	}

	private void spostaProtocollazioni() {
		ShowAppLoadingEvent.fire(SceltaFascicoloPresenter.this, true);

		dispatchAsync.execute(new SpostaProtocollazioniAction(fascicoloSorgenteClientId, getView().getClientIdFascicoloSelezionato(), allegatiDaSpostare, praticheProtocollateDaSpostare),
				new AsyncCallback<SpostaProtocollazioniResult>() {

					@Override
					public void onFailure(Throwable caught) {
						ShowAppLoadingEvent.fire(SceltaFascicoloPresenter.this, false);
						ShowMessageEvent event = new ShowMessageEvent();
						event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
						getEventBus().fireEvent(event);
					}

					@Override
					public void onSuccess(SpostaProtocollazioniResult result) {
						ShowAppLoadingEvent.fire(SceltaFascicoloPresenter.this, false);

						if (result.isError()) {
							ShowMessageEvent event = new ShowMessageEvent();
							event.setErrorMessage(result.getErrorMessage());
							getEventBus().fireEvent(event);

						} else {
							pecInDb.remove(fascicoloSorgenteClientId);
							goToFascicoloDestinatario(result.getFascicoloDestinatario().getClientID());
						}
					}

				});
	}

	private void spostaAllegati() {
		ShowAppLoadingEvent.fire(SceltaFascicoloPresenter.this, true);

		dispatchAsync.execute(new SpostaAllegatiAction(fascicoloSorgenteClientId, getView().getClientIdFascicoloSelezionato(), allegatiDaSpostare), new AsyncCallback<SpostaAllegatiResult>() {

			@Override
			public void onFailure(Throwable caught) {
				ShowAppLoadingEvent.fire(SceltaFascicoloPresenter.this, false);
				ShowMessageEvent event = new ShowMessageEvent();
				event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
				getEventBus().fireEvent(event);
			}

			@Override
			public void onSuccess(SpostaAllegatiResult result) {
				ShowAppLoadingEvent.fire(SceltaFascicoloPresenter.this, false);

				if (result.isError()) {
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(result.getErrorMessage());
					getEventBus().fireEvent(event);

				} else {
					pecInDb.remove(fascicoloSorgenteClientId);
					goToFascicoloDestinatario(result.getFascicoloDestinatario().getClientID());
				}
			}

		});
	}

	private void backToFascicoloSorgente(String clientId) {
		getEventBus().fireEvent(new BackToFascicoloEvent(clientId));
	}

	private void goToFascicoloDestinatario(String clientId) {
		Place place = new Place();
		place.setToken(NameTokens.dettagliofascicolo);
		place.addParam(NameTokensParams.idPratica, clientId);
		getEventBus().fireEvent(new GoToPlaceEvent(place));
	}

	protected void avanti() {
		fascicoloId = getView().getClientIdFascicoloSelezionato();
		pecInDb.getFascicoloByPath(fascicoloId, false, new PraticaFascicoloLoaded() {

			@Override
			public void onPraticaLoaded(FascicoloDTO pec) {
				if (PraticaUtil.isFascicoloPersonale(pec.getTipologiaPratica()) || PraticaUtil.isConfigurazioneGenericaFascicolo(pec.getTipologiaPratica().getNomeTipologia())) {
					new AbbandonaSceltaFascicoloCommand().execute();

				} else {
					ConfermaProtocollazioneSceltaFascicoloEvent event = new ConfermaProtocollazioneSceltaFascicoloEvent();
					event.setIdFascicolo(getView().getClientIdFascicoloSelezionato());
					event.setIdEmail(praticaPath);
					event.setProvenienza(mittente);
					getEventBus().fireEvent(event);
				}
			}

			@Override
			public void onPraticaError(String error) {
				ShowMessageEvent event = new ShowMessageEvent();
				event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
				getEventBus().fireEvent(event);

			}
		});

	}

	protected void avantiPraticaModulistica() {
		fascicoloId = getView().getClientIdFascicoloSelezionato();
		ShowAppLoadingEvent.fire(SceltaFascicoloPresenter.this, true);
		pecInDb.getPraticaModulisticaByPath(praticaPath, true, new PraticaModulisticaLoaded() {

			@Override
			public void onPraticaModulisticaLoaded(final PraticaModulisticaDTO pratica) {

				pecInDb.getFascicoloByPath(fascicoloId, false, new PraticaFascicoloLoaded() {

					@Override
					public void onPraticaLoaded(final FascicoloDTO dto) {

						if (PraticaUtil.isFascicoloPersonale(dto.getTipologiaPratica()) || PraticaUtil.isConfigurazioneGenericaFascicolo(dto.getTipologiaPratica().getNomeTipologia())) {

							ShowAppLoadingEvent.fire(SceltaFascicoloPresenter.this, false);
							new AbbandonaSceltaFascicoloCommand().execute();

						} else {

							// imposto i dati aggiuntivi
							GestionePraticaModulisticaUtil.aggiungiValoreAggiuntivo(dto, praticaPath);
							Map<String, ValoreModuloDTO> mappaValori = ConsolePecUtils.buildValoriTotaliMapByNome(pratica);
							ValoreModuloDTO dataInizioBando = mappaValori.get(GestionePraticaModulisticaUtil.MODULO_DATA_INIZIO_BANDO);
							if (dataInizioBando != null)
								GestionePraticaModulisticaUtil.aggiungiDataInizioBando(dto, dataInizioBando.getDescrizione(), praticaPath);
							ValoreModuloDTO dataFineBando = mappaValori.get(GestionePraticaModulisticaUtil.MODULO_DATA_FINE_BANDO);
							if (dataFineBando != null)
								GestionePraticaModulisticaUtil.aggiungiDataFineBando(dto, dataFineBando.getDescrizione(), praticaPath);
							ValoreModuloDTO emailRichiedente = mappaValori.get(GestionePraticaModulisticaUtil.MODULO_EMAIL_PROVENIENZA_MODULO);
							if (emailRichiedente != null)
								GestionePraticaModulisticaUtil.aggiungiEmailProvenienzaModulo(dto, emailRichiedente.getDescrizione(), praticaPath);
							// e salvo il fascicolo
							SalvaFascicolo action = new SalvaFascicolo(dto);
							/* reset dei messaggi di errore */
							ShowMessageEvent event = new ShowMessageEvent();
							event.setMessageDropped(true);
							getEventBus().fireEvent(event);
							dispatchAsync.execute(action, new AsyncCallback<SalvaFascicoloResult>() {

								@Override
								public void onFailure(Throwable caught) {
									ShowAppLoadingEvent.fire(SceltaFascicoloPresenter.this, false);
									ShowMessageEvent event = new ShowMessageEvent();
									event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
									getEventBus().fireEvent(event);
								}

								@Override
								public void onSuccess(SalvaFascicoloResult result) {
									ShowAppLoadingEvent.fire(SceltaFascicoloPresenter.this, false);
									if (result.isError()) {
										ShowMessageEvent event = new ShowMessageEvent();
										event.setErrorMessage(result.getErrorMsg());
										getEventBus().fireEvent(event);
										return;
									} else {
										ConfermaProtocollazioneSceltaFascicoloEvent event = new ConfermaProtocollazioneSceltaFascicoloEvent();
										event.setIdFascicolo(getView().getClientIdFascicoloSelezionato());
										event.setIdPraticaModulistica(praticaPath);
										getEventBus().fireEvent(event);
									}
								}
							});
						}
					}

					@Override
					public void onPraticaError(String error) {
						ShowAppLoadingEvent.fire(SceltaFascicoloPresenter.this, false);
						ShowMessageEvent event = new ShowMessageEvent();
						event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
						getEventBus().fireEvent(event);

					}
				});

			}

			@Override
			public void onPraticaModulisticaError(String error) {
				ShowAppLoadingEvent.fire(SceltaFascicoloPresenter.this, false);
				ShowMessageEvent event = new ShowMessageEvent();
				event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
				getEventBus().fireEvent(event);
			}
		});
	}

	protected void annulla() {
		if (praticaPath != null) {
			ShowAppLoadingEvent.fire(SceltaFascicoloPresenter.this, true);
			CambiaStatoPecInAction action = new CambiaStatoPecInAction(praticaPath, CambiaStatoPecInAction.Azione.RILASCIA_IN_CARICO);
			dispatchAsync.execute(action, new AsyncCallback<CambiaStatoPecInActionResult>() {

				@Override
				public void onSuccess(CambiaStatoPecInActionResult result) {
					ShowAppLoadingEvent.fire(SceltaFascicoloPresenter.this, false);
					if (!result.getIsError()) {
						pecInDb.remove(praticaPath);
						RilasciaInCaricoPecInEvent event = new RilasciaInCaricoPecInEvent(praticaPath);
						getEventBus().fireEvent(event);
					} else {
						ShowMessageEvent event = new ShowMessageEvent();
						event.setErrorMessage(result.getMessErr());
						getEventBus().fireEvent(event);
					}
				}

				@Override
				public void onFailure(Throwable caught) {
					ShowAppLoadingEvent.fire(SceltaFascicoloPresenter.this, false);
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
					getEventBus().fireEvent(event);
				}
			});
		} else
			getEventBus().fireEvent(new BackFromPlaceEvent());
	}

	protected void annullaPraticaModulistica() {
		if (praticaPath != null) {
			ShowAppLoadingEvent.fire(SceltaFascicoloPresenter.this, true);
			Set<String> listId = new HashSet<String>();
			listId.add(praticaPath);
			CambiaStatoPraticaModulistica action = new CambiaStatoPraticaModulistica(listId, CambiaStatoPraticaModulisticaEnum.RILASCIA_IN_CARICO);
			dispatchAsync.execute(action, new AsyncCallback<CambiaStatoPraticaModulisticaResult>() {

				@Override
				public void onFailure(Throwable caught) {
					ShowAppLoadingEvent.fire(SceltaFascicoloPresenter.this, false);
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
					getEventBus().fireEvent(event);
				}

				@Override
				public void onSuccess(CambiaStatoPraticaModulisticaResult result) {
					ShowAppLoadingEvent.fire(SceltaFascicoloPresenter.this, false);
					if (!result.isError()) {
						pecInDb.remove(praticaPath);
						RilasciaInCaricoPraticaModulisticaEvent event = new RilasciaInCaricoPraticaModulisticaEvent(praticaPath);
						getEventBus().fireEvent(event);
					} else {
						ShowMessageEvent event = new ShowMessageEvent();
						event.setErrorMessage(result.getErrorMsg());
						getEventBus().fireEvent(event);
					}
				}
			});
		} else
			getEventBus().fireEvent(new BackFromPlaceEvent());
	}

	private void dropMessage() {
		ShowMessageEvent event = new ShowMessageEvent();
		event.setMessageDropped(true);
		getEventBus().fireEvent(event);
	}

	private class RicercaEventListener implements WorklistStrategy.RicercaEventListener {

		@Override
		public void onStartRicerca(int start, int length, ColonnaWorklist campoOrdinamento, boolean asc, RicercaCallback callback) {
			CercaPratiche action = new CercaPratiche();
			action.setFine(start + length);
			action.setInizio(start);
			action.setCampoOrdinamento(campoOrdinamento);
			action.setOrdinamentoAsc(asc);
			action.setProvenienza(getView().getProvenienza());
			getView().formRicercaToCercaPratiche(action);
			action.setStato(StatoDTO.toStrings());

			if (sceltaDa.equals(SceltaDa.DETTAGLIOPECIN)) {
				action.setSuperutente(true);

			} else if (sceltaDa.equals(SceltaDa.DETTAGLIOFASCIOLO)) {
				action.setRicercaPerCollegamento(praticaPath);

			} else if (sceltaDa.equals(SceltaDa.DETTAGLIOPRATICAMODULISTICA)) {
				action.setSuperutente(true);

			} else if (sceltaDa.equals(SceltaDa.SPOSTA_ALLEGATI)) {
				action.setSuperutente(true);
				action.setIdDocumentaleDaEscludere(fascicoloSorgenteClientId);
				action.setStato(new String[] { StatoDTO.IN_GESTIONE.name() });

			} else if (sceltaDa.equals(SceltaDa.SPOSTA_PROTOCOLLAZIONI)) {
				action.setSuperutente(true);
				action.setIdDocumentaleDaEscludere(fascicoloSorgenteClientId);
				action.setStato(new String[] { StatoDTO.IN_GESTIONE.name() });
			}

			ricercaAdapter.startRicerca(action, callback);
		}

	}

	private class AbbandonaSceltaFascicoloCommand implements Command {

		@Override
		public void execute() {
			AggiungiPraticaAFascicolo action = new AggiungiPraticaAFascicolo(fascicoloId, praticaPath);
			ShowAppLoadingEvent.fire(SceltaFascicoloPresenter.this, true);
			/* reset dei messaggi di errore */
			ShowMessageEvent event = new ShowMessageEvent();
			event.setMessageDropped(true);
			getEventBus().fireEvent(event);
			dispatchAsync.execute(action, new AsyncCallback<AggiungiPraticaAFascicoloResult>() {
				@Override
				public void onSuccess(AggiungiPraticaAFascicoloResult result) {
					ShowAppLoadingEvent.fire(SceltaFascicoloPresenter.this, false);
					if (!result.getIsError()) {
						pecInDb.remove(fascicoloId);
						pecInDb.remove(praticaPath);
						Place place = new Place();
						place.setToken(NameTokens.dettagliofascicolo);
						place.addParam(NameTokensParams.idPratica, fascicoloId);
						getEventBus().fireEvent(new GoToPlaceEvent(place));
					} else {
						ShowMessageEvent event = new ShowMessageEvent();
						event.setErrorMessage(result.getMessError());
						getEventBus().fireEvent(event);
					}
				}

				@Override
				public void onFailure(Throwable caught) {
					ShowAppLoadingEvent.fire(SceltaFascicoloPresenter.this, false);
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
					getEventBus().fireEvent(event);
				}
			});
		}
	}

	@Override
	@ProxyEvent
	public void onChiudiCollegaFascicolo(ChiudiCollegaFascicoloConRicercaEvent event) {
		if (event.getAzioneCollegamento().equals(AzioneCollegamento.ANNULLA)) {
			// NON effettuo reset della form o refresh della ricerca se ritorno dalla maschera delle operazioni.
			revealInParent();
		} else {
			// torno al fascicolo originale
			annullaCollegamento();
		}
	}

	@Override
	@ProxyEvent
	public void onMostraSceltaFascicolo(MostraSceltaFascicoloEvent event) {
		revealInParent();
	}

	@Override
	@ProxyEvent
	public void onSpostaAllegati(SpostaAllegatiInizioEvent event) {
		this.sceltaDa = SceltaDa.SPOSTA_ALLEGATI;
		this.fascicoloSorgenteClientId = event.getFascicoloSorgente();
		this.allegatiDaSpostare = event.getAllegatiDaSpostare();
		init(false);
		revealInParent();
	}

	@Override
	@ProxyEvent
	public void onSpostaAllegati(SpostaProtocollazioniInizioEvent event) {
		this.sceltaDa = SceltaDa.SPOSTA_PROTOCOLLAZIONI;
		this.fascicoloSorgenteClientId = event.getFascicoloSorgente();
		this.allegatiDaSpostare = event.getAllegatiProtocollati();
		this.praticheProtocollateDaSpostare = event.getPraticheProtocollate();
		init(false);
		revealInParent();
	}
}
