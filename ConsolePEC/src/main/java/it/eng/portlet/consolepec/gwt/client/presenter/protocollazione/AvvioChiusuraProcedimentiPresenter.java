package it.eng.portlet.consolepec.gwt.client.presenter.protocollazione;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.google.common.base.Strings;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.google.gwt.user.datepicker.client.DateBox;
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

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaFascicolo;
import it.eng.cobo.consolepec.commons.procedimento.ChiusuraProcedimentoInput.ModalitaChiusuraProcedimento;
import it.eng.cobo.consolepec.util.validation.ValidationUtilities;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaFascicoloLoaded;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaLoaded;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command.BackToFascicoloCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command.IBackToFascicolo;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.command.AvviaProcedimentoCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.command.ChiudiProcedimentoCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.GestioneProcedimentiDaDettaglioFascicoloEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.GestioneProcedimentiDaDettaglioFascicoloEvent.GestioneProcedimentiDaDettaglioFascicoloHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.GestioneProcedimentiDaProtocollazioneEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.GestioneProcedimentiDaProtocollazioneEvent.GestioneProcedimentiDaProtocollazioneHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.MostraEsitoProtocollazioneEvent;
import it.eng.portlet.consolepec.gwt.client.util.GestionePraticaModulisticaUtil;
import it.eng.portlet.consolepec.gwt.client.widget.GroupSuggestBoxProtocollazione;
import it.eng.portlet.consolepec.gwt.client.widget.suggestBox.InputListWidget;
import it.eng.portlet.consolepec.gwt.client.widget.suggestBox.oracle.ProcedimentiMultiWordSuggestOracle;
import it.eng.portlet.consolepec.gwt.client.widget.suggestBox.oracle.SpacebarSuggestOracle;
import it.eng.portlet.consolepec.gwt.shared.Base64Utils;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.RecuperaTipologieProcedimenti;
import it.eng.portlet.consolepec.gwt.shared.action.RecuperaTipologieProcedimentiResult;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.AvvioProcedimento;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.ChiusuraProcedimentoAction;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.PropostaChiusuraProcedimentiAction;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.PropostaChiusuraProcedimentiResult;
import it.eng.portlet.consolepec.gwt.shared.dto.Element;
import it.eng.portlet.consolepec.gwt.shared.dto.TipologiaProcedimentoDto;
import it.eng.portlet.consolepec.gwt.shared.model.Destinatario;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoElenco;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoGruppoProtocollatoCapofila;
import it.eng.portlet.consolepec.gwt.shared.model.PecInDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PecOutDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.ProcedimentoMiniDto;
import it.eng.portlet.consolepec.gwt.shared.procedimenti.OperazioniProcedimento;

public class AvvioChiusuraProcedimentiPresenter extends Presenter<AvvioChiusuraProcedimentiPresenter.MyView, AvvioChiusuraProcedimentiPresenter.MyProxy> implements GestioneProcedimentiDaProtocollazioneHandler, GestioneProcedimentiDaDettaglioFascicoloHandler, IBackToFascicolo {

	public interface MyView extends View {

		public void initializeRiepilogoProtocollazione(String anno, String pg, String pgCapofila, String annoCapofila, String tipologiaDocumento, String titolo, String rubrica, String sezione);

		public void setProcedimentiSuggestBox(SuggestBox suggestBox, it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, String> selectionCommand);

		public void setQuartieriSuggestBox(SuggestBox suggestBox, it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, String> selectionCommand);

		public void setModAvvioListBox(HashMap<String, String> modAvvio);

		public void setAnnullaCommand(Command command);

		public void setIndietroCommand(Command command);

		public void setConfermaCommand(Command command);

		public InputListWidget getCampoEmail();

		public DateBox getCampoDataInizio();

		public ListBox getCampoModAvvio();

		public void abilitaCampoModAvvio(boolean abilita);

		public void abilitaCampoDataInizio(boolean abilita);

		public void abilitaCampoEmail(boolean abilita);

		public String getSelectedModAvvio();

		public void initCheckBoxes();

		public void getUpdatePrevalorizzazioni(Date dataInizio, List<String> destinatariEmail);

		public void mostraAvvio();

		public void mostraChiusura();

		public void abilitaCampoDataChiusura(boolean abilita);

		public void abilitaCampoModChiusura(boolean abilita);

		public DateBox getCampoDataChiusura();

		public ListBox getCampoModChiusura();

		public void popolaProcedimenti(List<ProcedimentoMiniDto> procedimenti);

		public void setModChiusuraListBox(List<String> modalitaChiusura);

		public void abilitaChiusura(boolean abilita);

		public ModalitaChiusuraProcedimento getSelectedModChiusura();

		public void setWarningChiusura(String message);

		public void setDataPropostaAvvioProcedimento(Date dataPropostaAvvioProcedimento);

		public void setEmailPropostaAvvioProcedimento(List<String> emailPropostaAvvioProcedimento);

		public void abilitaAvvio(boolean abilita);

		public boolean getSceltaInvioMailAvvio();

		void resetDestinatariEmail();

		public String getTextFromInputListWidgetDestinatari();

	}

	private String clientId;
	private DispatchAsync dispatchAsync;
	private List<TipologiaProcedimentoDto> tipologiaProcedimenti = new ArrayList<TipologiaProcedimentoDto>();
	private SuggestBox suggestBoxQuartiere;
	private SuggestBox suggestBoxProcedimenti;
	private PecInPraticheDB praticheDB;
	private String pgCapofila;
	private String annoCapofila;
	private String pg;
	private String annoPg;
	private Command indietroCommand;
	private Map<String, Map<String, Element>> map = new HashMap<String, Map<String, Element>>();
	private ArrayList<String> listaSettoriQuartieri = new ArrayList<String>();
	private String idPraticaProtocollataSelezionata;
	private Date dataProtocollazione;
	private boolean forzaChiusuraDiUfficio;
	private int codTipologiaProcedimentoDaChiudere;
	private Date dataInizioProcedimentoDaChiudere;
	private PlaceManager placeManager;
	private Set<String> praticheSelezionateProtocollate = new TreeSet<String>();

	private ConfigurazioniHandler configurazioniHandler;

	private String idDocumentaleFascicoloChiusuraProcedimento;

	@ProxyCodeSplit
	public interface MyProxy extends Proxy<AvvioChiusuraProcedimentiPresenter> {}

	@Inject
	public AvvioChiusuraProcedimentiPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, final PecInPraticheDB pecInDb, final DispatchAsync dispatchAsync,
			final PlaceManager placeManager, ConfigurazioniHandler configurazioniHandler) {
		super(eventBus, view, proxy);
		this.praticheDB = pecInDb;
		this.dispatchAsync = dispatchAsync;
		this.placeManager = placeManager;
		this.configurazioniHandler = configurazioniHandler;
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPresenter.TYPE_SetMainContent, this);
	}

	@Override
	protected void onBind() {
		super.onBind();
		getView().setAnnullaCommand(new BackToFascicoloCommand<AvvioChiusuraProcedimentiPresenter>(this));
	}

	@Override
	protected void onReveal() {
		super.onReveal();
		Window.scrollTo(0, 0);
	}

	@Override
	@ProxyEvent
	public void onGestioneProcedimentiDaProtocollazione(GestioneProcedimentiDaProtocollazioneEvent event) {
		forzaChiusuraDiUfficio = false;
		indietroCommand = new BackToProtocollazioneCommand();
		getView().setIndietroCommand(indietroCommand);
		clientId = event.getPathFascicolo();
		pg = event.getPg();
		annoPg = event.getAnno();
		pgCapofila = event.getPgCapofila();
		annoCapofila = event.getAnnoCapofila();
		map.clear();
		map.putAll(event.getMap());

		praticheSelezionateProtocollate.clear();
		if (event.getPraticheSelezionateProtocollate() != null && !event.getPraticheSelezionateProtocollate().isEmpty()) {
			praticheSelezionateProtocollate.addAll(event.getPraticheSelezionateProtocollate());
		}

		for (String pr : praticheSelezionateProtocollate) {
			idPraticaProtocollataSelezionata = pr;
			break;
		}
		if (event.getOperazione().equals(OperazioniProcedimento.AVVIO)) {
			initViewAvvio(pgCapofila, annoCapofila, event.getTipologiaDocumento(), event.getTitolo(), event.getRubrica(), event.getSezione());
		} else if (event.getOperazione().equals(OperazioniProcedimento.CHIUSURA)) {
			initViewChiusura(pg, annoPg, pgCapofila, annoCapofila, event.getTipologiaDocumento(), event.getTitolo(), event.getRubrica(), event.getSezione());
		} else {
			ShowMessageEvent event2 = new ShowMessageEvent();
			event2.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
			getEventBus().fireEvent(event2);
		}

	}

	@Override
	@ProxyEvent
	public void onGestioneProcedimentiDaDettaglioFascicolo(GestioneProcedimentiDaDettaglioFascicoloEvent event) {
		indietroCommand = new BackToFascicoloCommand<AvvioChiusuraProcedimentiPresenter>(this);
		getView().setIndietroCommand(indietroCommand);
		clientId = event.getPathFascicolo();
		pg = event.getPg();
		annoPg = event.getAnno();
		pgCapofila = event.getPgCapofila();
		annoCapofila = event.getAnnoCapofila();
		forzaChiusuraDiUfficio = annoPg.equals(annoCapofila) && pg.equals(pgCapofila);
		map.clear();
		idPraticaProtocollataSelezionata = event.getIdPraticaProtocollataSelezionata();

		praticheSelezionateProtocollate.clear();
		if (idPraticaProtocollataSelezionata != null) {
			praticheSelezionateProtocollate.add(idPraticaProtocollataSelezionata);
		}

		String titoloName = event.getTitolo() + " - " + GroupSuggestBoxProtocollazione.getTitoloNameByIdDisplayName(event.getTitolo());
		String rubricaName = event.getRubrica() + " - " + GroupSuggestBoxProtocollazione.getRubricaNameByIdTitolo(event.getRubrica(), event.getTitolo());
		String sezioneName = event.getSezione() + " - " + GroupSuggestBoxProtocollazione.getSezioneNameByIdRubrica(event.getSezione(), event.getTitolo(), event.getRubrica());
		String tipologiaName = event.getTipologiaDocumento() + " - " + GroupSuggestBoxProtocollazione.getTipoDocumentoNameByIdDisplayName(event.getTipologiaDocumento());
		if (event.getOperazione().equals(OperazioniProcedimento.AVVIO)) {
			initViewAvvio(pgCapofila, annoCapofila, tipologiaName, titoloName, rubricaName, sezioneName);
		} else if (event.getOperazione().equals(OperazioniProcedimento.CHIUSURA)) {
			if (event.getCodTipologiaProcedimento() != null) {
				codTipologiaProcedimentoDaChiudere = event.getCodTipologiaProcedimento();
			}
			if (event.getDataInizioDecorrenzaProcedimento() != null) {
				dataInizioProcedimentoDaChiudere = event.getDataInizioDecorrenzaProcedimento();
			}
			initViewChiusura(pg, annoPg, pgCapofila, annoCapofila, tipologiaName, titoloName, rubricaName, sezioneName);
		} else {
			ShowMessageEvent event2 = new ShowMessageEvent();
			event2.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
			getEventBus().fireEvent(event2);
		}
	}

	private void initViewChiusura(String pg, String annoPg, String pgCapofila, String annoCapofila, String tipologiaDocumento, String titolo, String rubrica, String sezione) {
		idDocumentaleFascicoloChiusuraProcedimento = null;

		getView().initializeRiepilogoProtocollazione(annoPg, pg, pgCapofila, annoCapofila, tipologiaDocumento, titolo, rubrica, sezione);
		getView().setConfermaCommand(new ChiudiProcedimentoCommand(this));
		getView().mostraChiusura();
		getView().setWarningChiusura(""); // nascondo il warning

		if (forzaChiusuraDiUfficio) {

			getView().abilitaChiusura(true);
			List<String> modalitaChiusura = new ArrayList<String>();
			modalitaChiusura.add(ModalitaChiusuraProcedimento.U.getDescrizione());
			getView().setModChiusuraListBox(modalitaChiusura);
			getView().abilitaCampoModChiusura(false);
			getView().getCampoDataChiusura().setValue(new Date());
			getView().abilitaCampoDataChiusura(false);
			idDocumentaleFascicoloChiusuraProcedimento = Base64Utils.URLdecodeAlfrescoPath(getFascicoloPath());

		} else {
			PropostaChiusuraProcedimentiAction action = new PropostaChiusuraProcedimentiAction();
			action.setAnnoPgNonCapofila(annoPg);
			action.setNumPgNonCapofila(pg);
			ShowAppLoadingEvent.fire(AvvioChiusuraProcedimentiPresenter.this, true);

			dispatchAsync.execute(action, new AsyncCallback<PropostaChiusuraProcedimentiResult>() {

				@Override
				public void onFailure(Throwable arg0) {
					ShowAppLoadingEvent.fire(AvvioChiusuraProcedimentiPresenter.this, false);
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
					getEventBus().fireEvent(event);
					getView().abilitaChiusura(false);
				}

				@Override
				public void onSuccess(PropostaChiusuraProcedimentiResult result) {
					ShowAppLoadingEvent.fire(AvvioChiusuraProcedimentiPresenter.this, false);
					if (result.isError()) {
						ShowMessageEvent event = new ShowMessageEvent();
						event.setErrorMessage(result.getErrorMsg());
						getEventBus().fireEvent(event);
						getView().abilitaChiusura(false);

					} else {
						List<ProcedimentoMiniDto> procedimentiAttivi = result.getProcedimenti();

						getView().popolaProcedimenti(procedimentiAttivi);
						if (procedimentiAttivi.size() == 1) {
							// questi parametri sono passati nell'event solo per la chiusura di ufficio, quindi me li prendo dai procedimenti proposti
							codTipologiaProcedimentoDaChiudere = procedimentiAttivi.get(0).getCodTipologiaProcedimento();
							dataInizioProcedimentoDaChiudere = procedimentiAttivi.get(0).getDataInizioDecorrenzaProcedimento();

							getView().abilitaChiusura(true);
							List<String> modChiusura = new ArrayList<String>();
							modChiusura.add(ModalitaChiusuraProcedimento.G.getDescrizione());
							modChiusura.add(ModalitaChiusuraProcedimento.R.getDescrizione());
							getView().setModChiusuraListBox(modChiusura);
							getView().abilitaCampoModChiusura(true);
							getView().getCampoDataChiusura().setValue(new Date());
							getView().abilitaCampoDataChiusura(false);
							idDocumentaleFascicoloChiusuraProcedimento = result.getIdDocumentaleFascicolo();

						} else {
							getView().abilitaChiusura(false);
							getView().setWarningChiusura(
									procedimentiAttivi.size() == 0 ? ConsolePecConstants.WARN_CHIUSURA_PROCEDIMENTI_ASSENTI : ConsolePecConstants.WARN_CHIUSURA_PROCEDIMENTI_MULTIPLI);
						}
					}
				}
			});
		}
		revealInParent();
	}

	private void initViewAvvio(String pgCapofila, String annoCapofila, String tipologiaDocumento, String titolo, String rubrica, String sezione) {
		getView().setConfermaCommand(new AvviaProcedimentoCommand(this));
		getView().mostraAvvio();
		getView().initializeRiepilogoProtocollazione(annoCapofila, pgCapofila, pgCapofila, annoCapofila, tipologiaDocumento, titolo, rubrica, sezione);
		dispatchAsync.execute(new RecuperaTipologieProcedimenti(), new AsyncCallback<RecuperaTipologieProcedimentiResult>() {

			@Override
			public void onFailure(Throwable caught) {
				ShowMessageEvent event = new ShowMessageEvent();
				event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
				getEventBus().fireEvent(event);
			}

			@Override
			public void onSuccess(RecuperaTipologieProcedimentiResult result) {

				tipologiaProcedimenti = result.getTipologiaProcedimenti();
				setSuggestBoxProcedimenti(tipologiaProcedimenti);
				initListaQuartieri();
				setSuggestBoxQuartieri();
				getView().getCampoModAvvio().clear();
				getView().abilitaCampoModAvvio(false);
				getView().abilitaCampoDataInizio(false);
				getView().abilitaCampoEmail(true);

				/* prevalorizzazione data di avvio del procedimento e destinatario email di notifica (richiedente per la modulistica) */
				praticheDB.getFascicoloByPath(clientId, false, new PraticaFascicoloLoaded() {

					private Date dataPropostaAvvioProcedimento = null;
					private List<String> emailPropostaAvvioProcedimento = null;

					private void impostaPrevalorizzazione() {
						getView().resetDestinatariEmail();
						getView().setDataPropostaAvvioProcedimento(dataPropostaAvvioProcedimento);
						getView().setEmailPropostaAvvioProcedimento(emailPropostaAvvioProcedimento);
						getView().getUpdatePrevalorizzazioni(dataPropostaAvvioProcedimento, emailPropostaAvvioProcedimento);
					}

					@Override
					public void onPraticaLoaded(FascicoloDTO fascicoloDTO) {

						AnagraficaFascicolo anagraficaFascicolo = configurazioniHandler.getAnagraficaFascicolo(fascicoloDTO.getTipologiaPratica().getNomeTipologia());

						dataProtocollazione = getDataProtocollazione(fascicoloDTO, AvvioChiusuraProcedimentiPresenter.this.pgCapofila, AvvioChiusuraProcedimentiPresenter.this.annoCapofila);
						Date inizioBando = GestionePraticaModulisticaUtil.getDataInizioBando(fascicoloDTO.getValoriDatiAggiuntivi(), idPraticaProtocollataSelezionata);
						Date fineBando = GestionePraticaModulisticaUtil.getDataFineBando(fascicoloDTO.getValoriDatiAggiuntivi(), idPraticaProtocollataSelezionata);

						if (inizioBando != null && fineBando != null && dataProtocollazione != null) {
							// prevalorizzazione data avvio e indirizzo email richiedente del modulo
							// data inizio e fine bando sono valorizzate per i moduli
							dataPropostaAvvioProcedimento = calcolaDataAvvio(inizioBando, fineBando, dataProtocollazione);
							emailPropostaAvvioProcedimento = GestionePraticaModulisticaUtil.getDestinatarioEmail(fascicoloDTO.getValoriDatiAggiuntivi(), idPraticaProtocollataSelezionata);
							impostaPrevalorizzazione();
						} else {
							// procedimenti normali
							// per tutti gli altri procedimenti prendo la data di protocollazione
							dataPropostaAvvioProcedimento = (dataProtocollazione != null) ? dataProtocollazione : new Date();
							if (praticheSelezionateProtocollate.size() == 1) {
								praticheDB.getPraticaByPath(praticheSelezionateProtocollate.iterator().next(), false, new PraticaLoaded() {

									@Override
									public void onPraticaLoaded(PraticaDTO pratica) {
										if (pratica instanceof PecInDTO) {
											emailPropostaAvvioProcedimento = new ArrayList<String>();
											emailPropostaAvvioProcedimento.add(((PecInDTO) pratica).getMittente());
										} else if (pratica instanceof PecOutDTO) {
											TreeSet<Destinatario> destinatari = ((PecOutDTO) pratica).getDestinatari();
											if (destinatari.size() > 0) {
												emailPropostaAvvioProcedimento = new ArrayList<String>();

												for (Destinatario destinatarioValue : destinatari) {
													emailPropostaAvvioProcedimento.add(destinatarioValue.getDestinatario());
												}
											}
										}
										impostaPrevalorizzazione();
									}

									@Override
									public void onPraticaError(String error) {
										ShowMessageEvent event = new ShowMessageEvent();
										event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
										getEventBus().fireEvent(event);
									}

								});
							} else {
								impostaPrevalorizzazione();
							}
						}

						/* prevalorizzazione del quartiere */
						for (TipologiaProcedimentoDto t : tipologiaProcedimenti) {

							if (t.getDescrizioneSettore() != null && (t.getDescrizioneSettore().equalsIgnoreCase(anagraficaFascicolo.getDatiProcedimento().getQuartiere())
									|| (t.getCodiceQuartiere() + " - " + t.getDescrizioneSettore()).equalsIgnoreCase(anagraficaFascicolo.getDatiProcedimento().getQuartiere()))) {
								suggestBoxQuartiere.setValue(t.getCodiceQuartiere() + " - " + t.getDescrizioneSettore());
								break;
							}
						}

						new SelezioneQuartiereCommand().exe(suggestBoxQuartiere.getValue());

						/* prevalorizzazione del procedimento */
						for (TipologiaProcedimentoDto t : tipologiaProcedimenti) {
							if (t.getCodiceProcedimento().intValue() == anagraficaFascicolo.getDatiProcedimento().getCodiceProcedimento()) {
								suggestBoxProcedimenti.setValue(anagraficaFascicolo.getDatiProcedimento().getCodiceProcedimento() + " - " + t.getDescrizione());
								break;
							}
						}
						new SelezioneProcedimentiCommand().exe(suggestBoxProcedimenti.getValue());

					}

					@Override
					public void onPraticaError(String error) {
						ShowMessageEvent event = new ShowMessageEvent();
						event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
						getEventBus().fireEvent(event);
					}
				});
			}

		});
		getView().initCheckBoxes();
		getView().abilitaAvvio(true); // abilito sempre l'avvio dei procedimenti (il controllo dei campi lo fa il command)
		revealInParent();
	}

	private Date getDataProtocollazione(FascicoloDTO fascicoloDTO, String pgCapofila, String annoCapofila) {
		Date dataProtocollazione = null;
		for (ElementoElenco elem : fascicoloDTO.getElenco()) {
			if (elem instanceof ElementoGruppoProtocollatoCapofila) {
				ElementoGruppoProtocollatoCapofila capofila = (ElementoGruppoProtocollatoCapofila) elem;
				if (capofila.getAnnoPG().equals(annoCapofila) && capofila.getNumeroPG().equals(pgCapofila)) {
					dataProtocollazione = capofila.getDataProtocollazione();
				}
			}
		}
		return dataProtocollazione;
	}

	private Date calcolaDataAvvio(Date inizioBando, Date fineBando, Date dataProtocollazione) {
		CalendarUtil.resetTime(inizioBando);
		CalendarUtil.resetTime(fineBando);
		CalendarUtil.resetTime(dataProtocollazione);
		if ((inizioBando.before(dataProtocollazione) || inizioBando.equals(dataProtocollazione)) && (fineBando.equals(dataProtocollazione) || fineBando.after(dataProtocollazione))) {
			CalendarUtil.addDaysToDate(fineBando, 1);
			return fineBando;
		} else {
			return dataProtocollazione;
		}
	}

	public class BackToProtocollazioneCommand implements Command {

		@Override
		public void execute() {
			MostraEsitoProtocollazioneEvent event = new MostraEsitoProtocollazioneEvent(clientId);
			event.setMapForm(map);
			event.setPg(pg + "/" + annoPg);
			event.setPgCapofila(pgCapofila + "/" + annoCapofila);
			getEventBus().fireEvent(event);
		}
	}

	public class SelezioneQuartiereCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, String> {

		@Override
		public Void exe(String quartiereSelezionato) {
			suggestBoxProcedimenti.setValue("");
			ArrayList<TipologiaProcedimentoDto> newList = new ArrayList<TipologiaProcedimentoDto>();
			if (Strings.isNullOrEmpty(quartiereSelezionato)) {
				return null;
			}
			String quartiere = quartiereSelezionato.split("-")[1].trim();
			if (Strings.isNullOrEmpty(quartiere)) {
				newList = new ArrayList<TipologiaProcedimentoDto>(tipologiaProcedimenti);
				return null;
			} else {
				String descrizioneQuartiere = quartiereSelezionato.split("-")[1].trim();

				for (TipologiaProcedimentoDto procedimentoDto : tipologiaProcedimenti) {
					if (descrizioneQuartiere.equals(procedimentoDto.getDescrizioneSettore())) {
						newList.add(procedimentoDto);
					}
				}
				setSuggestBoxProcedimenti(newList);
			}
			return null;
		}

	}

	public class SelezioneProcedimentiCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, String> {

		@Override
		public Void exe(String procedimentoSelezionato) {

			if (Strings.isNullOrEmpty(procedimentoSelezionato)) {
				return null;
			}

			String procedimento = procedimentoSelezionato.split("-")[0].trim();
			if (Strings.isNullOrEmpty(procedimento)) {
				return null;
			} else {
				Integer codiceProcedimento = Integer.parseInt(procedimento);
				String modalitaAvvio = null;
				for (TipologiaProcedimentoDto procedimentoDto : tipologiaProcedimenti) {
					if (codiceProcedimento.equals(procedimentoDto.getCodiceProcedimento())) {
						modalitaAvvio = procedimentoDto.getModalitaAvvio();
					}
				}

				ListBox listBox = getView().getCampoModAvvio();
				listBox.setVisible(true);
				if (modalitaAvvio == null) {
					getView().abilitaCampoModAvvio(true);
				} else {
					getView().abilitaCampoModAvvio(false);
					int num = 0;
					for (String value : getModalitaAvvio().keySet()) {
						listBox.addItem(value);
						if (value.equals(modalitaAvvio)) {
							listBox.setItemSelected(num, true);
						}
						num++;
					}
				}
			}
			return null;
		}

	}

	public boolean checkInput() {
		if (Strings.isNullOrEmpty(suggestBoxQuartiere.getValue())) {
			ShowMessageEvent event = new ShowMessageEvent();
			event.setWarningMessage("Il quartiere è obbligatorio.");
			getEventBus().fireEvent(event);
			return false;
		}

		if (Strings.isNullOrEmpty(suggestBoxProcedimenti.getValue()) || Strings.isNullOrEmpty(suggestBoxProcedimenti.getValue())) {
			ShowMessageEvent event = new ShowMessageEvent();
			event.setWarningMessage("Il tipo di procedimento è obbligatorio.");
			getEventBus().fireEvent(event);
			return false;
		}

		if (getView().getSceltaInvioMailAvvio() && getView().getCampoEmail().getItemSelected().size() == 0) {
			ShowMessageEvent event = new ShowMessageEvent();
			event.setWarningMessage("Specificare un indirizzo per la comunicazione di avvio procedimento.");
			getEventBus().fireEvent(event);
			return false;
		}

		if (!getView().getCampoEmail().isAbilitato()) {

			if (getView().getCampoEmail().getItemSelected().size() > 0) {

				for (String address : getView().getCampoEmail().getItemSelected()) {

					if (!ValidationUtilities.validateEmailAddress(address)) {
						ShowMessageEvent event = new ShowMessageEvent();
						event.setWarningMessage("Indirizzo email:" + address + "non valido.");
						getEventBus().fireEvent(event);
						return false;
					}
				}
			}
		}

		if (!Strings.isNullOrEmpty(getView().getSelectedModAvvio()) && !(getModalitaAvvio().keySet().contains(getView().getSelectedModAvvio()))) {
			ShowMessageEvent event = new ShowMessageEvent();
			event.setWarningMessage("Modalità di avvio non valida.");
			getEventBus().fireEvent(event);
			return false;
		}

		if (getView().getCampoDataInizio().getValue() == null) {
			ShowMessageEvent event = new ShowMessageEvent();
			event.setWarningMessage("La data di avvio del procedimento non può essere vuota.");
			getEventBus().fireEvent(event);
			return false;
		} else {
			if (getView().getCampoDataInizio().getValue().before(dataProtocollazione)) {
				ShowMessageEvent event = new ShowMessageEvent();
				event.setWarningMessage("La data di avvio del procedimento non può essere antecedente alla data di protocollazione.");
				getEventBus().fireEvent(event);
				return false;
			}
		}

		String textFromInputListWidgetDestinatari = getView().getTextFromInputListWidgetDestinatari();
		if (!Strings.isNullOrEmpty(textFromInputListWidgetDestinatari) && !ValidationUtilities.validateEmailAddress(textFromInputListWidgetDestinatari)) {
			ShowMessageEvent event = new ShowMessageEvent();
			event.setWarningMessage("Indirizzo di notifica non valido.");
			getEventBus().fireEvent(event);
			return false;
		}

		return true;
	}

	private TipologiaProcedimentoDto getTipologiaProcedimento(int codiceTipologia, int quartiere) {
		for (TipologiaProcedimentoDto tipologiaProcedimentoDto : tipologiaProcedimenti) {
			if ((tipologiaProcedimentoDto.getCodiceProcedimento().intValue() == codiceTipologia) && (tipologiaProcedimentoDto.getCodiceQuartiere().intValue() == quartiere)) {
				return tipologiaProcedimentoDto;
			}
		}
		throw new RuntimeException();
	}

	private void setSuggestBoxQuartieri() {
		SpacebarSuggestOracle genericMultiWordSuggestOracle = new SpacebarSuggestOracle(listaSettoriQuartieri);
		suggestBoxQuartiere = new SuggestBox(genericMultiWordSuggestOracle);
		getView().setQuartieriSuggestBox(suggestBoxQuartiere, new SelezioneQuartiereCommand());
	}

	private void setSuggestBoxProcedimenti(List<TipologiaProcedimentoDto> tipologiaProcedimenti) {
		ProcedimentiMultiWordSuggestOracle procedimentiMultiWordSuggestOracle = new ProcedimentiMultiWordSuggestOracle();
		procedimentiMultiWordSuggestOracle.setTipologiaProcedimentoDtoList(tipologiaProcedimenti);
		suggestBoxProcedimenti = new SuggestBox(procedimentiMultiWordSuggestOracle);

		getView().setProcedimentiSuggestBox(suggestBoxProcedimenti, new SelezioneProcedimentiCommand());
	}

	private void initListaQuartieri() {
		TreeSet<String> listaQuartieriSettori = new TreeSet<String>();
		for (TipologiaProcedimentoDto tipologiaProcedimentoDto : tipologiaProcedimenti) {
			if (tipologiaProcedimentoDto.getDescrizioneSettore() != null) {
				listaQuartieriSettori.add(tipologiaProcedimentoDto.getCodiceQuartiere() + " - " + tipologiaProcedimentoDto.getDescrizioneSettore());
			}
		}
		listaSettoriQuartieri = new ArrayList<String>(listaQuartieriSettori);
	}

	private HashMap<String, String> getModalitaAvvio() {
		HashMap<String, String> mappa = new HashMap<String, String>();
		mappa.put(ConsolePecConstants.DESCR_AVVIO_DI_PARTE, ConsolePecConstants.FLAG_AVVIO_DI_PARTE);
		mappa.put(ConsolePecConstants.DESCR_AVVIO_DI_UFFICIO, ConsolePecConstants.FLAG_AVVIO_DI_UFFICIO);
		return mappa;
	}

	@Override
	public EventBus _getEventBus() {
		return getEventBus();
	}

	@Override
	public DispatchAsync getDispatchAsync() {
		return dispatchAsync;
	}

	@Override
	public PlaceManager getPlaceManager() {
		return placeManager;
	}

	@Override
	public PecInPraticheDB getPecInPraticheDB() {
		return praticheDB;
	}

	@Override
	public String getFascicoloPath() {
		return clientId;
	}

	public ChiusuraProcedimentoAction getChiusuraProcedimentoAction() {
		Date dataChiusura = getView().getCampoDataChiusura().getValue();
		ModalitaChiusuraProcedimento modChiusura = getView().getSelectedModChiusura();
		ChiusuraProcedimentoAction action = new ChiusuraProcedimentoAction();
		action.setAnnoProtocollazione(Integer.parseInt(annoCapofila));
		action.setNumProtocollazione(pgCapofila);
		action.setCodTipologiaProcedimento(codTipologiaProcedimentoDaChiudere);
		action.setDataInizio(dataInizioProcedimentoDaChiudere);
		action.setModalitaChiusura(modChiusura);
		if (!ModalitaChiusuraProcedimento.U.equals(modChiusura)) {
			action.setNumProtocolloDocChiusura(pg);
			action.setAnnoProtocolloDocChiusura(Integer.parseInt(annoPg));
		}
		action.setDataChiusura(dataChiusura);
		action.setIdDocumentaleFascicoloConProcedimento(idDocumentaleFascicoloChiusuraProcedimento);
		action.setFascicoloCorrentePath(getFascicoloPath());
		return action;
	}

	public AvvioProcedimento getAvvioProcedimentoAction() {
		String procedimenti = suggestBoxProcedimenti.getValue();
		String quartiere = suggestBoxQuartiere.getValue();
		Date dataInizio = getView().getCampoDataInizio().getValue();
		List<String> emails = getView().getCampoEmail().getItemSelected();
		TipologiaProcedimentoDto procedimento = getTipologiaProcedimento(Integer.parseInt(procedimenti.split("-")[0].trim()), Integer.parseInt(quartiere.split("-")[0].trim()));
		AvvioProcedimento action = new AvvioProcedimento();
		action.setAnnoProtocollazione(Integer.parseInt(annoCapofila));
		action.setNumProtocollazione(pgCapofila);
		action.setCodQuartiere(procedimento.getCodiceQuartiere());
		action.setDataInizioProcedimento(dataInizio);
		action.setCodTipologiaProcedimento(procedimento.getCodiceProcedimento());
		if (!getView().getCampoEmail().isAbilitato()) {
			List<String> emailsList = action.getEmails();
			emailsList.addAll(emails);
		}

		action.setFascicoloPath(clientId);
		for (TipologiaProcedimentoDto t : tipologiaProcedimenti) {
			if (t.getCodiceProcedimento().equals(action.getCodTipologiaProcedimento())) {
				action.setModAvvioProcedimento(getModalitaAvvio().get(t.getModalitaAvvio()));
			}
		}
		return action;
	}

}
