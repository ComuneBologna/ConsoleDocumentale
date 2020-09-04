package it.eng.portlet.consolepec.gwt.client.operazioni.cartellafirma.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.proxy.PlaceManager;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.datigenerici.PreferenzeUtente.PreferenzeCartellaAttivita;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaFascicoloLoaded;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.event.cartellafirma.AccettaFirmaVistoFineEvent;
import it.eng.portlet.consolepec.gwt.client.event.cartellafirma.AccettaFirmaVistoFineEvent.AccettaFirmaVistoFineHandler;
import it.eng.portlet.consolepec.gwt.client.event.cartellafirma.AccettaFirmaVistoInizioEvent;
import it.eng.portlet.consolepec.gwt.client.event.cartellafirma.AssegnaFascicoliFineEvent;
import it.eng.portlet.consolepec.gwt.client.event.cartellafirma.AssegnaFascicoliFineEvent.AssegnaFascicoliFineHandler;
import it.eng.portlet.consolepec.gwt.client.event.cartellafirma.AssegnaFascicoliInizioEvent;
import it.eng.portlet.consolepec.gwt.client.event.cartellafirma.DiniegaFirmaVistoFineEvent;
import it.eng.portlet.consolepec.gwt.client.event.cartellafirma.DiniegaFirmaVistoFineEvent.DiniegaFirmaVistoFineHandler;
import it.eng.portlet.consolepec.gwt.client.event.cartellafirma.DiniegaFirmaVistoInizioEvent;
import it.eng.portlet.consolepec.gwt.client.event.cartellafirma.GoToWorklistCartellaFirmaEvent;
import it.eng.portlet.consolepec.gwt.client.event.cartellafirma.NotificaTaskFirmaFineEvent;
import it.eng.portlet.consolepec.gwt.client.event.cartellafirma.NotificaTaskFirmaFineEvent.NotificaTaskFirmaFineHandler;
import it.eng.portlet.consolepec.gwt.client.event.cartellafirma.NotificaTaskFirmaInizioEvent;
import it.eng.portlet.consolepec.gwt.client.event.cartellafirma.RispondiParereFineEvent;
import it.eng.portlet.consolepec.gwt.client.event.cartellafirma.RispondiParereFineEvent.RispondiParereFineHandler;
import it.eng.portlet.consolepec.gwt.client.event.cartellafirma.RispondiParereInizioEvent;
import it.eng.portlet.consolepec.gwt.client.event.firmadigitale.FirmaDigitaleFineEvent;
import it.eng.portlet.consolepec.gwt.client.event.firmadigitale.FirmaDigitaleFineEvent.FirmaDigitaleFineHandler;
import it.eng.portlet.consolepec.gwt.client.event.firmadigitale.FirmaDigitaleInizioEvent;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.operazioni.cartellafirma.CartellaFirmaWizardApiClient;
import it.eng.portlet.consolepec.gwt.client.util.TaskFirmaUtils;
import it.eng.portlet.consolepec.gwt.client.widget.SitemapMenu;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.EliminaFileDaTmpAction;
import it.eng.portlet.consolepec.gwt.shared.action.EliminaFileDaTmpResult;
import it.eng.portlet.consolepec.gwt.shared.action.RecuperaFileDaTmpAction;
import it.eng.portlet.consolepec.gwt.shared.action.RecuperaFileDaTmpResult;
import it.eng.portlet.consolepec.gwt.shared.action.cartellafirma.FineWizardTaskFirmaAction;
import it.eng.portlet.consolepec.gwt.shared.action.cartellafirma.FineWizardTaskFirmaResult;
import it.eng.portlet.consolepec.gwt.shared.action.cartellafirma.InformazioniFirmaDigitaleTaskFirmaDTO;
import it.eng.portlet.consolepec.gwt.shared.action.cartellafirma.InformazioniNotificaTaskFirmaDTO;
import it.eng.portlet.consolepec.gwt.shared.action.cartellafirma.InformazioniRiassegnazioneFascicoliTaskFirmaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FileDTO;
import it.eng.portlet.consolepec.gwt.shared.model.TmpFileUploadDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.DestinatarioDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.DestinatarioGruppoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.DestinatarioUtenteDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.DocumentoFirmaVistoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.StatoDestinatarioTaskFirmaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.TipoRispostaParereDTO;

/**
 *
 * @author biagiot
 *
 */
public class CartellaFirmaWizardApiClientImpl implements CartellaFirmaWizardApiClient, HasHandlers, AccettaFirmaVistoFineHandler, DiniegaFirmaVistoFineHandler, RispondiParereFineHandler, AssegnaFascicoliFineHandler, FirmaDigitaleFineHandler, NotificaTaskFirmaFineHandler {

	private final DispatchAsync dispatcher;
	private final EventBus eventBus;
	private final PecInPraticheDB pecInPraticheDB;
	private final SitemapMenu sitemapMenu;
	private final PlaceManager placeManager;
	private final ProfilazioneUtenteHandler profilazioneUtenteHandler;
	private Set<DocumentoFirmaVistoDTO> documentiFirmaVisto;
	private Map<String, List<AllegatoDTO>> praticaAllegatiMap;
	private List<FascicoloDTO> fascicoliCaricati;
	private OperazioneWizardTaskFirma operazioneRichiesta;
	private InformazioniNotificaTaskFirmaDTO infoNotifica;
	private InformazioniFirmaDigitaleTaskFirmaDTO informazioniFirmaDigitale;
	private boolean fromDettaglioFascicolo = false;
	private TipoRispostaParereDTO tipoRisposta;
	private List<FileDTO> fileDaAllegare;
	private Set<String> ruoliAbilitati;
	private boolean isTaskRuoli = false;
	private String ruoloSelezionato;
	private String motivazione;

	@Inject
	public CartellaFirmaWizardApiClientImpl(DispatchAsync dispatcher, EventBus eventBus, PecInPraticheDB pecInPraticheDB, SitemapMenu sitemapMenu, PlaceManager placeManager,
			ProfilazioneUtenteHandler profilazioneUtenteHandler) {
		this.dispatcher = dispatcher;
		this.pecInPraticheDB = pecInPraticheDB;
		this.sitemapMenu = sitemapMenu;
		eventBus.addHandler(AccettaFirmaVistoFineEvent.getType(), this);
		eventBus.addHandler(RispondiParereFineEvent.getType(), this);
		eventBus.addHandler(FirmaDigitaleFineEvent.getType(), this);
		eventBus.addHandler(AssegnaFascicoliFineEvent.getType(), this);
		eventBus.addHandler(DiniegaFirmaVistoFineEvent.getType(), this);
		eventBus.addHandler(NotificaTaskFirmaFineEvent.getType(), this);
		this.eventBus = eventBus;
		this.placeManager = placeManager;
		this.profilazioneUtenteHandler = profilazioneUtenteHandler;
	}

	// ///////////////////////////////////////////////////// 1 STEP ///////////////////////////////////////////////////////

	@Override
	public void startWizardTaskFirma(final Set<DocumentoFirmaVistoDTO> documentiFirmaVisto, final OperazioneWizardTaskFirma operazione) {
		if (documentiFirmaVisto == null || operazione == null || profilazioneUtenteHandler.getAnagraficheRuoloUtente() == null || profilazioneUtenteHandler.getAnagraficheRuoloUtente().isEmpty()) {
			showErrorEvent(ConsolePecConstants.ERROR_MESSAGE, false);
		} else {
			if (checkDestinatariGruppo(documentiFirmaVisto)) {
				operazioniDestinatarioGruppoAbilitate(documentiFirmaVisto, new OperazioneDestinatarioGruppoAbilitataResult() {
					@Override
					public void onComplete(Boolean enabled, Set<String> ruoli) {
						if (!enabled) {
							showErrorEvent(ConsolePecConstants.ERROR_MESSAGE, false);
						} else {
							start(documentiFirmaVisto, operazione, enabled, ruoli, null, null);
						}
					}
				});
			} else {
				start(documentiFirmaVisto, operazione, false, null, null, null);
			}
		}
	}

	@Override
	public void ritiraDocumenti(Map<String, List<AllegatoDTO>> praticaAllegatiMap, boolean fromDettaglioFascicolo) {
		this.praticaAllegatiMap = praticaAllegatiMap;
		this.operazioneRichiesta = OperazioneWizardTaskFirma.RITIRO;
		this.fromDettaglioFascicolo = fromDettaglioFascicolo;
		goToNotifica(OperazioneWizardTaskFirma.RITIRO);
	}

	private void start(final Set<DocumentoFirmaVistoDTO> documentiFirmaVisto, final OperazioneWizardTaskFirma operazione, final Boolean isTaskRuoli, final Set<String> ruoliAbilitati,
			final List<FileDTO> fileDaAllegare, final String motivazione) {

		this.documentiFirmaVisto = documentiFirmaVisto;
		this.operazioneRichiesta = operazione;
		this.isTaskRuoli = isTaskRuoli;
		this.fascicoliCaricati = new ArrayList<FascicoloDTO>();
		this.ruoliAbilitati = ruoliAbilitati;
		this.fileDaAllegare = fileDaAllegare;
		this.motivazione = motivazione;

		final Set<String> clientIdSet = getClientIdSet(documentiFirmaVisto);

		ShowAppLoadingEvent.fire(CartellaFirmaWizardApiClientImpl.this, true);

		for (String clientId : clientIdSet) {

			pecInPraticheDB.getFascicoloByPath(clientId, true, new PraticaFascicoloLoaded() {

				@Override
				public void onPraticaLoaded(FascicoloDTO pec) {
					boolean isRiassegnabilePerRuolo = true;
					for (DocumentoFirmaVistoDTO doc : documentiFirmaVisto) {
						isRiassegnabilePerRuolo &= doc.isRiassegnabilePerProponente();
					}

					fascicoliCaricati.add(pec);

					if (fascicoliCaricati.size() == clientIdSet.size()) {
						ShowAppLoadingEvent.fire(CartellaFirmaWizardApiClientImpl.this, false);

						Map<FascicoloDTO, List<AllegatoDTO>> map = createFascicoloAllegatiMap(fascicoliCaricati, documentiFirmaVisto);
						boolean riassegnazioneAbilitata = TaskFirmaUtils.riassegnazioneAbilitata(map, operazione) && isRiassegnabilePerRuolo;

						switch (operazione) {
						case FIRMA:
						case VISTO:
							AccettaFirmaVistoInizioEvent.fire(CartellaFirmaWizardApiClientImpl.this, documentiFirmaVisto, operazione, riassegnazioneAbilitata, isTaskRuoli ? ruoliAbilitati : null,
									fileDaAllegare, motivazione);
							break;
						case DINIEGO:
							DiniegaFirmaVistoInizioEvent.fire(CartellaFirmaWizardApiClientImpl.this, documentiFirmaVisto, operazione, riassegnazioneAbilitata, isTaskRuoli ? ruoliAbilitati : null,
									fileDaAllegare, motivazione);
							break;
						case RISPOSTA_PARERE:
							RispondiParereInizioEvent.fire(CartellaFirmaWizardApiClientImpl.this, documentiFirmaVisto, riassegnazioneAbilitata, isTaskRuoli ? ruoliAbilitati : null, fileDaAllegare,
									motivazione);
							break;
						default:
							showErrorEvent(ConsolePecConstants.ERROR_MESSAGE, true);
						}
					}
				}

				@Override
				public void onPraticaError(String error) {
					showErrorEvent(ConsolePecConstants.ERROR_MESSAGE, true);
				}
			});
		}
	}

	// ///////////////////////////////////////////////////// EVENT LISTENERS ///////////////////////////////////////////////////////
	/*
	 * Event listeners 1 STEP
	 */

	@Override
	@ProxyEvent
	public void onAccettaFirmaVistoFine(AccettaFirmaVistoFineEvent event) {
		if (isTaskRuoli) {
			this.ruoloSelezionato = event.getRuolo();
		}
		if (event.getAllegati() != null) {
			this.fileDaAllegare = event.getAllegati();
		}

		this.motivazione = event.getMotivazione();
		wizardDispatcher(event.isAnnulla(), event.isRiassegna());
	}

	@Override
	@ProxyEvent
	public void onDiniegaFirmaVistoFine(DiniegaFirmaVistoFineEvent event) {
		if (isTaskRuoli) {
			this.ruoloSelezionato = event.getRuolo();
		}
		if (event.getAllegati() != null) {
			this.fileDaAllegare = event.getAllegati();
		}

		this.motivazione = event.getMotivazione();
		wizardDispatcher(event.isAnnulla(), event.isRiassegna());
	}

	@Override
	public void onRispondiParereFine(RispondiParereFineEvent event) {
		if (isTaskRuoli) {
			this.ruoloSelezionato = event.getRuolo();
		}
		if (event.getAllegati() != null) {
			this.fileDaAllegare = event.getAllegati();
		}

		this.motivazione = event.getMotivazione();
		this.tipoRisposta = event.getTipoRisposta();
		wizardDispatcher(event.isAnnulla(), event.isRiassegna());
	}

	/*
	 * Event listeners 2 STEP
	 */

	@Override
	@ProxyEvent
	public void onAssegnaFascicoliFine(AssegnaFascicoliFineEvent event) {
		ShowAppLoadingEvent.fire(CartellaFirmaWizardApiClientImpl.this, false);
		if (event.isAnnulla()) {
			annullaAssegnazioneFascicoli();
		} else {
			if (event.getRiassegnaFascicoloInfo() != null) {
				profilazioneUtenteHandler.aggiornaPreferenzeRiassegnazione(event.getRiassegnaFascicoloInfo().isRicordaScelta(),
						event.getRiassegnaFascicoloInfo().getSettore() != null ? event.getRiassegnaFascicoloInfo().getSettore().getNome() : null,
						event.getRiassegnaFascicoloInfo().getAnagraficaRuolo() != null ? event.getRiassegnaFascicoloInfo().getAnagraficaRuolo().getRuolo() : null,
						event.getRiassegnaFascicoloInfo().getIndirizziNotifica(), null);

			}

			assegnaFascicoli(event.getRiassegnaFascicoloInfo());
		}
	}

	@Override
	@ProxyEvent
	public void onNotificaTaskFirmaFine(NotificaTaskFirmaFineEvent event) {
		if (event.isAnnulla()) {
			annullaNotifica();
		} else {
			notifica(event.getInfoNotificaTaskFirma());
		}
	}

	/*
	 * Event listeners 3 STEP
	 */

	@Override
	@ProxyEvent
	public void onFirmaDigitaleEnd(FirmaDigitaleFineEvent event) {
		if (CartellaFirmaWizardApiClientImpl.this.equals(event.getOpeningRequestor())) {
			if (event.isAnnulla()) {
				goToWorklistCartellaFirma();
			} else {
				firmaDocumenti(event);
			}
		}
	}

	// ///////////////////////////////////////////////////// DISPATCHER 1 STEP ///////////////////////////////////////////////////////

	private void wizardDispatcher(boolean isAnnulla, boolean isRiassegna) {
		if (isAnnulla) {
			goToWorklistCartellaFirma();
		} else {
			if (isRiassegna) {
				goToAssegna(operazioneRichiesta);
			} else {
				goToNotifica(operazioneRichiesta);
			}
		}
	}

	// ///////////////////////////////////////////////////// GO TO 2 STEP ///////////////////////////////////////////////////////

	private void goToAssegna(OperazioneWizardTaskFirma operazioneWizardTaskFirma) {
		AssegnaFascicoliInizioEvent.fire(CartellaFirmaWizardApiClientImpl.this, getClientIDSet(operazioneWizardTaskFirma));
	}

	private void goToNotifica(OperazioneWizardTaskFirma operazioneWizard) {
		NotificaTaskFirmaInizioEvent.fire(CartellaFirmaWizardApiClientImpl.this);
	}

	// ///////////////////////////////////////////////////// OPERAZIONI 2 STEP ///////////////////////////////////////////////////////

	private void assegnaFascicoli(InformazioniRiassegnazioneFascicoliTaskFirmaDTO riassegnaFascicoloInfo) {
		this.infoNotifica = riassegnaFascicoloInfo;

		switch (operazioneRichiesta) {
		case FIRMA:
			goToFirmaDigitale(getAllegati(documentiFirmaVisto));
			break;
		case VISTO:
		case DINIEGO:
		case RISPOSTA_PARERE:
			startFineWizardActionHandler(documentiFirmaVistoToMap(), operazioneRichiesta);
			break;
		default:
			goToWorklistCartellaFirma();
			break;
		}
	}

	private void notifica(InformazioniNotificaTaskFirmaDTO infoNotifica) {
		this.infoNotifica = infoNotifica;
		if (infoNotifica.isRicordaScelta()) {
			PreferenzeCartellaAttivita preferenzeCartellaAttivita = profilazioneUtenteHandler.getPreferenzeUtente().getPreferenzeCartellaAttivita() != null
					? profilazioneUtenteHandler.getPreferenzeUtente().getPreferenzeCartellaAttivita() : new PreferenzeCartellaAttivita();
			if (infoNotifica.getIndirizziNotifica() != null) {
				preferenzeCartellaAttivita.setIndirizziNotifica(infoNotifica.getIndirizziNotifica());
			}
			profilazioneUtenteHandler.aggiornaPreferenzeUtente(null, null, preferenzeCartellaAttivita, null);
		}

		switch (operazioneRichiesta) {
		case FIRMA:
			goToFirmaDigitale(getAllegati(documentiFirmaVisto));
			break;
		case VISTO:
		case DINIEGO:
		case RISPOSTA_PARERE:
			startFineWizardActionHandler(documentiFirmaVistoToMap(), operazioneRichiesta);
			break;
		case RITIRO:
			startFineWizardActionHandler(praticaAllegatiMap, operazioneRichiesta);
			break;
		default:
			showErrorEvent(ConsolePecConstants.ERROR_MESSAGE, true);
			break;
		}
	}

	private void annullaAssegnazioneFascicoli() {
		start(documentiFirmaVisto, operazioneRichiesta, isTaskRuoli, ruoliAbilitati, fileDaAllegare, motivazione);
	}

	private void annullaNotifica() {
		if (OperazioneWizardTaskFirma.RITIRO.equals(operazioneRichiesta)) {
			if (fromDettaglioFascicolo) {
				gotoFascicolo();
			} else {
				goToWorklistCartellaFirma();
			}
		} else {
			start(documentiFirmaVisto, operazioneRichiesta, isTaskRuoli, ruoliAbilitati, fileDaAllegare, motivazione);
		}
	}

	// ///////////////////////////////////////////////////// GO TO 3 STEP ///////////////////////////////////////////////////////

	private void goToFirmaDigitale(Set<AllegatoDTO> allegati) {
		FirmaDigitaleInizioEvent.fire(CartellaFirmaWizardApiClientImpl.this, this, allegati);
	}

	// ///////////////////////////////////////////////////// OPERAZIONI 3 STEP ///////////////////////////////////////////////////////

	private void firmaDocumenti(FirmaDigitaleFineEvent event) {

		if (event.getCredenzialiFirma() != null) {
			profilazioneUtenteHandler.aggiornaPreferenzeFirmaDigitale(event.getCredenzialiFirma().isSalvaCredenziali(), event.getCredenzialiFirma().getUsername(),
					event.getCredenzialiFirma().getPassword(), null);

		}

		this.informazioniFirmaDigitale = new InformazioniFirmaDigitaleTaskFirmaDTO(event.getTipologiaFirma(), event.getCredenzialiFirma());
		startFineWizardActionHandler(documentiFirmaVistoToMap(), OperazioneWizardTaskFirma.FIRMA);
	}

	// ///////////////////////////////////////////////////// FINE WIZARD ///////////////////////////////////////////////////////

	@Override
	public void evadi(Set<DocumentoFirmaVistoDTO> documentiFirmaVisto) {
		this.documentiFirmaVisto = documentiFirmaVisto;
		startFineWizardActionHandler(documentiFirmaVistoToMap(), OperazioneWizardTaskFirma.EVADI);
	}

	private void startFineWizardActionHandler(Map<String, List<AllegatoDTO>> praticaAllegatiMap, OperazioneWizardTaskFirma operazioneWizardTaskFirma) {

		ShowAppLoadingEvent.fire(CartellaFirmaWizardApiClientImpl.this, true);

		FineWizardTaskFirmaAction action = new FineWizardTaskFirmaAction(praticaAllegatiMap, operazioneWizardTaskFirma);
		action.setInfoNotifica(infoNotifica);
		action.setMotivazione(motivazione);

		if (OperazioneWizardTaskFirma.FIRMA.equals(operazioneWizardTaskFirma)) {
			action.setInfoFirmaDigitale(informazioniFirmaDigitale);
		} else if (OperazioneWizardTaskFirma.RISPOSTA_PARERE.equals(operazioneWizardTaskFirma)) {
			action.setTipoRisposta(tipoRisposta);
		} else if (OperazioneWizardTaskFirma.EVADI.equals(operazioneWizardTaskFirma)) {

			Map<String, List<Integer>> map = new HashMap<String, List<Integer>>();

			for (DocumentoFirmaVistoDTO documento : documentiFirmaVisto) {
				if (map.containsKey(documento.getClientIdFascicolo())) {
					map.get(documento.getClientIdFascicolo()).add(documento.getIdTaskFirma());
				} else {
					List<Integer> list = new ArrayList<Integer>();
					list.add(documento.getIdTaskFirma());
					map.put(documento.getClientIdFascicolo(), list);
				}
			}
			action.setPraticaIdTaskMap(map);
		}
		if (isTaskRuoli) {
			action.setRuolo(ruoloSelezionato);
		}
		if (fileDaAllegare != null && !fileDaAllegare.isEmpty()) {
			action.setFileDaAllegare(fileDaAllegare);
		}

		dispatcher.execute(action, new AsyncCallback<FineWizardTaskFirmaResult>() {

			@Override
			public void onFailure(Throwable caught) {
				showErrorEvent(ConsolePecConstants.ERROR_MESSAGE, true);
			}

			@Override
			public void onSuccess(FineWizardTaskFirmaResult result) {
				if (result.isError()) {
					showErrorEvent(result.getErrorMessage(), true);

				} else {
					for (FascicoloDTO fascicolo : result.getFascicoli()) {
						pecInPraticheDB.insertOrUpdate(fascicolo.getClientID(), fascicolo, sitemapMenu.containsLink(fascicolo.getClientID()));
					}
					ShowAppLoadingEvent.fire(CartellaFirmaWizardApiClientImpl.this, false);
					if (fromDettaglioFascicolo) {
						gotoFascicolo();
					} else {
						goToWorklistCartellaFirma();
					}
				}
			}
		});
	}

	// ///////////////////////////////////////////////////// UTILITY ///////////////////////////////////////////////////////

	/*
	 * Metodi per tornare al presenter precedente
	 */

	private void goToWorklistCartellaFirma() {
		clear();
		GoToWorklistCartellaFirmaEvent.fire(CartellaFirmaWizardApiClientImpl.this);
	}

	private void gotoFascicolo() {
		clear();
		placeManager.revealCurrentPlace();
	}

	/*
	 * Utility di conversione
	 */

	private Map<String, List<AllegatoDTO>> documentiFirmaVistoToMap() {
		Map<String, List<AllegatoDTO>> mapPraticaAllegati = new HashMap<String, List<AllegatoDTO>>();
		for (DocumentoFirmaVistoDTO documento : documentiFirmaVisto) {
			String key = documento.getClientIdFascicolo();
			if (mapPraticaAllegati.containsKey(key)) {
				mapPraticaAllegati.get(key).add(documento.getAllegato());
			} else {
				List<AllegatoDTO> temp = new ArrayList<AllegatoDTO>();
				temp.add(documento.getAllegato());
				mapPraticaAllegati.put(key, temp);
			}
		}
		return mapPraticaAllegati;
	}

	private Set<String> getClientIdSet(Set<DocumentoFirmaVistoDTO> documentiFirmaVisto) {
		Set<String> idSet = new TreeSet<String>();
		for (DocumentoFirmaVistoDTO documentoFirmaVistoDTO : documentiFirmaVisto) {
			idSet.add(documentoFirmaVistoDTO.getClientIdFascicolo());
		}
		return idSet;
	}

	private Set<String> getClientIdSet(Map<String, List<AllegatoDTO>> praticaAllegatiMap) {
		Set<String> idSet = new TreeSet<String>();
		for (Entry<String, List<AllegatoDTO>> entry : praticaAllegatiMap.entrySet()) {
			idSet.add(entry.getKey());
		}
		return idSet;
	}

	private Set<String> getClientIDSet(OperazioneWizardTaskFirma operazioneWizard) {
		switch (operazioneWizard) {
		case FIRMA:
		case VISTO:
		case DINIEGO:
		case RISPOSTA_PARERE:
			return getClientIdSet(documentiFirmaVisto);
		case RITIRO:
			return getClientIdSet(praticaAllegatiMap);
		default:
			showErrorEvent(ConsolePecConstants.ERROR_MESSAGE, false);
			return null;
		}
	}

	private Map<FascicoloDTO, List<AllegatoDTO>> createFascicoloAllegatiMap(List<FascicoloDTO> fascicoli, Set<DocumentoFirmaVistoDTO> documentiFirmaVisto) {
		Map<FascicoloDTO, List<AllegatoDTO>> result = new HashMap<FascicoloDTO, List<AllegatoDTO>>();

		for (DocumentoFirmaVistoDTO doc : documentiFirmaVisto) {
			for (FascicoloDTO fascicolo : fascicoli) {
				if (fascicolo.getClientID().equalsIgnoreCase(doc.getClientIdFascicolo())) {
					for (AllegatoDTO allegato : fascicolo.getAllegati()) {
						if (allegato.getNome().equals(doc.getAllegato().getNome())) {
							if (result.containsKey(fascicolo)) {
								result.get(fascicolo).add(allegato);
							} else {
								List<AllegatoDTO> allegati = new ArrayList<AllegatoDTO>();
								allegati.add(allegato);
								result.put(fascicolo, allegati);
							}
						}
					}
				}
			}
		}
		return result;
	}

	private Set<AllegatoDTO> getAllegati(Set<DocumentoFirmaVistoDTO> documentiFirmaVisto) {
		Set<AllegatoDTO> allegati = new HashSet<AllegatoDTO>();
		for (DocumentoFirmaVistoDTO documento : documentiFirmaVisto) {
			allegati.add(documento.getAllegato());
		}
		return allegati;
	}

	private enum DestinatarioGruppoInApprovazione implements Predicate<DestinatarioDTO> {
		INSTANCE;

		@Override
		public boolean apply(DestinatarioDTO input) {
			return StatoDestinatarioTaskFirmaDTO.IN_APPROVAZIONE.equals(input.getStatoRichiesta());
		}

	}

	private enum DestinatarioGruppoToDescrizione implements Function<DestinatarioDTO, String> {
		INSTANCE;

		@Override
		public String apply(DestinatarioDTO dest) {
			DestinatarioGruppoDTO destinatario = (DestinatarioGruppoDTO) dest;
			return destinatario.getNomeGruppoDisplay();
		}
	}

	private enum RuoloToDescrizione implements Function<AnagraficaRuolo, String> {
		INSTANCE;

		@Override
		public String apply(AnagraficaRuolo ruolo) {
			return ruolo.getEtichetta();
		}
	}

	/*
	 * Evento messaggio errori
	 */

	private void showErrorEvent(String errorMessage, boolean stopLoading) {
		if (stopLoading) {
			ShowAppLoadingEvent.fire(CartellaFirmaWizardApiClientImpl.this, false);
		}
		ShowMessageEvent event = new ShowMessageEvent();
		event.setErrorMessage(errorMessage);
		eventBus.fireEvent(event);
	}

	private void clear() {
		if (fileDaAllegare != null && !fileDaAllegare.isEmpty()) {
			for (FileDTO file : fileDaAllegare) {
				eliminaFileDaAllegare(file.getPath());
			}
		}
		this.praticaAllegatiMap = null;
		this.fascicoliCaricati = null;
		this.documentiFirmaVisto = null;
		this.operazioneRichiesta = null;
		this.infoNotifica = null;
		this.fromDettaglioFascicolo = false;
		this.tipoRisposta = null;
		this.ruoliAbilitati = null;
		this.fileDaAllegare = null;
		this.informazioniFirmaDigitale = null;
		this.motivazione = null;
	}

	/*
	 * Gestione abilitazioni dei destinatari
	 */

	/**
	 * Se il tipo dei destinatari dei vari documenti di proposta non è lo stesso (Tipi: ruolo / utente singolo) non posso effettuare operazioni
	 *
	 * @param documenti
	 * @return
	 */
	@Override
	public boolean operazioniDestinariAbilitate(Set<DocumentoFirmaVistoDTO> documenti) {
		Iterator<DocumentoFirmaVistoDTO> iterator = documenti.iterator();
		boolean check = iterator.next().getTaskPerRuoli();
		while (iterator.hasNext()) {
			if (iterator.next().getTaskPerRuoli() != check) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Ritorna true se tutti i documenti di proposta hanno destinatari di tipo ruolo
	 *
	 * @param documenti
	 * @return
	 */
	@Override
	public boolean checkDestinatariGruppo(Set<DocumentoFirmaVistoDTO> documenti) {
		for (DocumentoFirmaVistoDTO documento : documenti) {
			if (!documento.getTaskPerRuoli()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Ritorna false se l'intersezione tra le liste contenenti i destinatari di una singola proposta (con stato in approvazione) e la lista dei ruoli personali è zero. True, altrimenti.
	 */
	@Override
	public boolean operazioniDestinatarioGruppoAbilitate(Set<DocumentoFirmaVistoDTO> documenti) {
		Set<String> intersection = new HashSet<String>();
		Set<AnagraficaRuolo> ruoliPersonali = new HashSet<AnagraficaRuolo>(profilazioneUtenteHandler.getAnagraficheRuoloUtente());
		for (DocumentoFirmaVistoDTO documento : documenti) {
			Set<String> ruoli = new HashSet<String>(Collections2.transform(ruoliPersonali, RuoloToDescrizione.INSTANCE));

			Set<String> destinatari = new HashSet<String>(Collections2.transform(
					new HashSet<DestinatarioDTO>(Collections2.filter(documento.getDestinatariFirma(), DestinatarioGruppoInApprovazione.INSTANCE)), DestinatarioGruppoToDescrizione.INSTANCE));

			Set<String> intersectionSet = Sets.intersection(ruoli, destinatari);
			if (intersectionSet.size() == 0) {
				return false;
			}
			intersection.addAll(intersectionSet);
		}
		return true;
	}

	/**
	 *
	 * Ritorna false se l'intersezione tra le liste contenenti i destinatari di una singola proposta (con stato in approvazione) e la lista dei ruoli personali è zero. Se true fornisce nella callback
	 * i ruoli risultanti dell'intersezione, quindi utilizzabili per le operazioni del task di firma.
	 *
	 * @param documenti
	 * @param ruoliPersonali
	 * @param callback
	 */
	private void operazioniDestinatarioGruppoAbilitate(Set<DocumentoFirmaVistoDTO> documenti, OperazioneDestinatarioGruppoAbilitataResult callback) {
		Set<String> intersection = new HashSet<String>();
		Set<AnagraficaRuolo> ruoliPersonali = new HashSet<AnagraficaRuolo>(profilazioneUtenteHandler.getAnagraficheRuoloUtente());

		for (DocumentoFirmaVistoDTO documento : documenti) {
			Set<String> ruoli = new HashSet<String>(Collections2.transform(ruoliPersonali, RuoloToDescrizione.INSTANCE));

			Set<String> destinatari = new HashSet<String>(Collections2.transform(
					new HashSet<DestinatarioDTO>(Collections2.filter(documento.getDestinatariFirma(), DestinatarioGruppoInApprovazione.INSTANCE)), DestinatarioGruppoToDescrizione.INSTANCE));

			Set<String> intersectionSet = Sets.intersection(ruoli, destinatari);

			if (intersectionSet.size() == 0) {
				callback.onComplete(false, null);
			}
			intersection.addAll(intersectionSet);
		}
		callback.onComplete(true, intersection);
	}

	@Override
	public boolean isOperazioneAbilitata(Set<DocumentoFirmaVistoDTO> documenti) {
		boolean taskPerRuoli = false;
		int i = 0;
		int j = 0;

		for (DocumentoFirmaVistoDTO doc : documenti) {
			if (doc.getTaskPerRuoli()) {
				taskPerRuoli = true;
				for (DestinatarioDTO destinatario : doc.getDestinatariFirma()) {
					for (AnagraficaRuolo ar : profilazioneUtenteHandler.getDatiUtente().getAnagraficheRuoli()) {
						if (ar.getRuolo().equals(((DestinatarioGruppoDTO) destinatario).getNomeGruppoConsole())) {
							if (!destinatario.getStatoRichiesta().equals(StatoDestinatarioTaskFirmaDTO.IN_APPROVAZIONE)) {
								i++;
							} else {
								j++;
							}
						}
					}
				}
			} else if (!taskPerRuoli) {
				for (DestinatarioDTO destinatario : doc.getDestinatariFirma()) {
					if (((DestinatarioUtenteDTO) destinatario).getUserId().equals(profilazioneUtenteHandler.getDatiUtente().getUsername())) {
						if (!destinatario.getStatoRichiesta().equals(StatoDestinatarioTaskFirmaDTO.IN_APPROVAZIONE)) {
							return false;
						}
					}
				}
			} else {
				return false;
			}
		}
		if (taskPerRuoli) {
			return i == 0 || i < j;
		}
		return true;
	}

	/*
	 *
	 */

	@Override
	public void fireEvent(GwtEvent<?> event) {
		eventBus.fireEventFromSource(event, this);
	}

	/*
	 * Gestione file allegati in operazioni destinatario (firma, visto, diniego, parere)
	 */

	@Override
	public void getFileDaAllegare(List<TmpFileUploadDTO> tmpFilesUploadDTO, List<FileDTO> allegati, final UploadAllegatiCallback callback) {
		RecuperaFileDaTmpAction action = new RecuperaFileDaTmpAction(tmpFilesUploadDTO);
		dispatcher.execute(action, new AsyncCallback<RecuperaFileDaTmpResult>() {

			@Override
			public void onFailure(Throwable caught) {
				callback.onError(ConsolePecConstants.ERROR_MESSAGE);
			}

			@Override
			public void onSuccess(RecuperaFileDaTmpResult result) {
				if (result.isError()) {
					callback.onError(result.getErrorMessage());
				} else {
					callback.onAllegatiUploaded(result.getFiles());
				}
			}
		});
	}

	@Override
	public void eliminaFileDaAllegare(String pathFile) {
		EliminaFileDaTmpAction action = new EliminaFileDaTmpAction(pathFile);
		dispatcher.execute(action, new AsyncCallback<EliminaFileDaTmpResult>() {

			@Override
			public void onFailure(Throwable caught) {
				// NOP
			}

			@Override
			public void onSuccess(EliminaFileDaTmpResult result) {
				// NOP
			}
		});
	}
}
