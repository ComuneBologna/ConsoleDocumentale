package it.eng.portlet.consolepec.gwt.client.presenter.fascicolo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
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

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaFascicolo;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaFascicolo.ConfigurazioneEsecuzione;
import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo;
import it.eng.cobo.consolepec.commons.urbanistica.AllegatoProcedi;
import it.eng.cobo.consolepec.commons.urbanistica.PraticaProcedi;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaFascicoloLoaded;
import it.eng.portlet.consolepec.gwt.client.event.ApertoDettaglioEvent;
import it.eng.portlet.consolepec.gwt.client.event.BackFromPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.event.ChiudiAllegatiEmailEvent;
import it.eng.portlet.consolepec.gwt.client.event.ChiudiAllegatiEmailEvent.ChiudiAllegatiEmailHandler;
import it.eng.portlet.consolepec.gwt.client.event.ChiudiCondividiFascicoloEvent;
import it.eng.portlet.consolepec.gwt.client.event.ChiudiCondividiFascicoloEvent.ChiudiCondividiFascicoloHandler;
import it.eng.portlet.consolepec.gwt.client.event.ChiudiDettaglioAllegatoEvent;
import it.eng.portlet.consolepec.gwt.client.event.ChiudiDettaglioAllegatoEvent.ChiudiDettaglioAllegatoHandler;
import it.eng.portlet.consolepec.gwt.client.event.ChiudiDettaglioModuloEvent;
import it.eng.portlet.consolepec.gwt.client.event.ChiudiDettaglioModuloEvent.ChiudiDettaglioModuloHandler;
import it.eng.portlet.consolepec.gwt.client.event.GoToPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.event.MostraDettaglioAllegatoEvent;
import it.eng.portlet.consolepec.gwt.client.event.RichiestaConfermaAnnullaEvent;
import it.eng.portlet.consolepec.gwt.client.event.SceltaConfermaAnnullaEvent;
import it.eng.portlet.consolepec.gwt.client.event.SceltaConfermaAnnullaEvent.SceltaConfermaAnnullaHandler;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.event.TerminaAttesaEvent;
import it.eng.portlet.consolepec.gwt.client.event.TerminaAttesaEvent.TerminaAttesaHandler;
import it.eng.portlet.consolepec.gwt.client.event.collegamento.ChiudiCollegaFascicoloDirettoEvent;
import it.eng.portlet.consolepec.gwt.client.event.collegamento.ChiudiCollegaFascicoloDirettoEvent.ChiudiCollegaFascicoloDirettoHandler;
import it.eng.portlet.consolepec.gwt.client.event.firmadigitale.FirmaDigitaleFineEvent;
import it.eng.portlet.consolepec.gwt.client.event.firmadigitale.FirmaDigitaleFineEvent.FirmaDigitaleFineHandler;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.operazioni.cartellafirma.CartellaFirmaWizardApiClient;
import it.eng.portlet.consolepec.gwt.client.operazioni.pec.PecApiClient;
import it.eng.portlet.consolepec.gwt.client.operazioni.pec.PecApiClient.PraticaCallback;
import it.eng.portlet.consolepec.gwt.client.operazioni.template.TemplateSceltaWizardApiClient;
import it.eng.portlet.consolepec.gwt.client.place.NameTokens;
import it.eng.portlet.consolepec.gwt.client.place.NameTokensParams;
import it.eng.portlet.consolepec.gwt.client.presenter.BiCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter.Place;
import it.eng.portlet.consolepec.gwt.client.presenter.event.ApriInvioCSVEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.event.BackToFascicoloEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.event.BackToFascicoloEvent.BackToFascicoloHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.event.ChiudiPubblicazioneAllegatiEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.event.ChiudiPubblicazioneAllegatiEvent.ChiudiPubblicazioneAllegatiHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.event.MostraModificaTipologieAllegatoEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.event.UploadEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.event.UploadEvent.UploadStatus;
import it.eng.portlet.consolepec.gwt.client.presenter.event.rubrica.MostraListaAnagraficheEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.event.rubrica.MostraListaAnagraficheEvent.MostraListaAnagraficheHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.event.rubrica.SelezionaAnagraficaFineEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.event.rubrica.SelezionaAnagraficaFineEvent.SelezionaAnagraficaFineHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.event.urbanistica.MostraDettaglioAllegatoProcediEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.IndietroSceltaCapofilaFascicoloEvent.IndietroSceltaCapofilaFascicoloHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command.*;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command.sara.EmissionePermessoCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.urbanistica.command.ListaPraticaProcediCommand;
import it.eng.portlet.consolepec.gwt.client.tasks.gestionefascicolo.operazioni.richiedifirma.RichiediFirmaTaskApiClient;
import it.eng.portlet.consolepec.gwt.client.util.GestioneLinkSiteMapUtil;
import it.eng.portlet.consolepec.gwt.client.widget.CatenaDocumentaleWidget.SearchPGParams;
import it.eng.portlet.consolepec.gwt.client.widget.ElementoCatenaDocumentaleWidget;
import it.eng.portlet.consolepec.gwt.client.widget.ElementoFascicoloCollegato;
import it.eng.portlet.consolepec.gwt.client.widget.ElementoPraticaProcediCollegato;
import it.eng.portlet.consolepec.gwt.client.widget.SitemapMenu;
import it.eng.portlet.consolepec.gwt.client.widget.datiaggiuntivi.FormDatiAggiuntiviWidget;
import it.eng.portlet.consolepec.gwt.shared.Base64Utils;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.UriMapping;
import it.eng.portlet.consolepec.gwt.shared.action.SalvaNoteAction;
import it.eng.portlet.consolepec.gwt.shared.action.SalvaNoteResult;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CambiaStatoFascicolo;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CambiaStatoFascicoloEnum;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CambiaStatoFascicoloResult;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.FirmaAllegatoFascicoloAction;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.FirmaAllegatoFascicoloResult;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.SalvaFascicolo;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.SalvaFascicoloResult;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.UploadAllegatoFascicolo;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.UploadAllegatoFascicoloResult;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.UploadFileZipAction;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.UploadFileZipResult;
import it.eng.portlet.consolepec.gwt.shared.action.pec.ComposizioneFascicoliCollegatiAction;
import it.eng.portlet.consolepec.gwt.shared.action.pec.ComposizioneFascicoliCollegatiActionResult;
import it.eng.portlet.consolepec.gwt.shared.action.urbanistica.DettaglioPraticaProcediAction;
import it.eng.portlet.consolepec.gwt.shared.action.urbanistica.DettaglioPraticaProcediResult;
import it.eng.portlet.consolepec.gwt.shared.dto.CollegamentoDto;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.CatenaDocumentaleDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoElenco;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoGruppoProtocollato;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoGruppoProtocollatoCapofila;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoPECRiferimento;
import it.eng.portlet.consolepec.gwt.shared.model.PecDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PecInDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaModulisticaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.ProcedimentoDto;
import it.eng.portlet.consolepec.gwt.shared.model.RispostaFileUploaderDTO;
import it.eng.portlet.consolepec.gwt.shared.model.TmpFileUploadDTO;
import it.eng.portlet.consolepec.gwt.shared.model.ValidazioneDatoAggiuntivoDTO;

public class DettaglioFascicoloGenericoPresenter extends Presenter<DettaglioFascicoloGenericoPresenter.MyView, DettaglioFascicoloGenericoPresenter.MyProxy> implements FirmaDigitaleFineHandler, ChiudiDettaglioAllegatoHandler, TerminaAttesaHandler, SceltaConfermaAnnullaHandler, ChiudiAllegatiEmailHandler, IndietroSceltaCapofilaFascicoloHandler, ChiudiPubblicazioneAllegatiHandler, ChiudiCondividiFascicoloHandler, ChiudiDettaglioModuloHandler, ChiudiCollegaFascicoloDirettoHandler, BackToFascicoloHandler, IStampaRicevute, IBackToFascicolo, SelezionaAnagraficaFineHandler, MostraListaAnagraficheHandler {

	private final EventBus eventBus;
	private String fascicoloPath;
	private final DispatchAsync dispatcher;
	private final PecInPraticheDB praticheDB;
	private final SitemapMenu sitemapMenu;
	private boolean uploadTimeout;
	private CambiaStatoFascicolo cambiaStatoFascicolo;
	private String eventId;
	private final GestioneLinkSiteMapUtil gestioneLinkSiteMapUtil;
	private final PlaceManager placeManager;
	private final RichiediFirmaTaskApiClient richiediFirmaTaskApiClient;
	private final CartellaFirmaWizardApiClient cartellaFirmaWizardApiClient;
	private final TemplateSceltaWizardApiClient templateSceltaWizard;
	private final PecApiClient pecApiClient;
	private ProfilazioneUtenteHandler profilazioneUtenteHandler;
	private ConfigurazioniHandler configurazioniHandler;

	public interface MyView extends View {

		public void setSganciaPecInCommand(Command sganciaPecInCommand);

		public void mostraPratica(FascicoloDTO fascicolo);

		public void setSalvaDatiAggiuntiviCommand(SalvaFascicoloCommand salvaFascicoloCommand);

		public void setSalvaNoteFascicoloCommand(Command command);

		public String getNote();

		public void setAnnullaSalvaFascicoloCommand(Command annullaSalvaFascicoloCommand);

		public void setChiudiDettaglioCommand(Command chiudiDettaglioCommand);

		public void setEliminaFascicoloCommand(Command eliminaFascicoloCommand);

		public FascicoloDTO getFascicolo();

		public void setUploadAllegatoCommand(UploadAllegatoCommand uploadAllegatoCommand);

		public void startUpload();

		void startZipUpload();

		public Set<AllegatoDTO> getAllegatiSelezionati();

		public void setEliminaAllegatoCommand(Command eliminaAllegatoCommand);

		public void setNascondiGruppiCommand(Command mostraNascondiGruppiCommand);

		public void setCambioStatoArchiviaCommand(it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, CambiaStatoFascicoloEnum> cambioStatoCommand);

		public void setCambioStatoInGestioneCommand(it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, CambiaStatoFascicoloEnum> cambioStatoCommand);

		public void setCambioStatoInAffissioneCommand(it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, CambiaStatoFascicoloEnum> cambioStatoCommand);

		public void setFirmaAllegatoCommand(Command firmaAllegatoCommand);

		public void setRispondiMailCommand(Command rispondiMailCommand);

		public String getIdMailSelezionata();

		public void sendDownload(SafeUri uri);

		public void setProtocollaCommand(Command command);

		public Set<ElementoElenco> getPraticheNonProtSelezionate();

		public void setGestioneInCaricoCommand(Command gestioneInCaricoCommand);

		public void setNuovaMailCommand(Command nuovaMailCommand);

		public void setNuovaMailInteroperabileCommand(Command nuovaMailCommand);

		public void resetDisclosurePanels(boolean showActions);

		public void reset();

		public void setScaricaAllegatiCommand(Command scaricaAllegatiCommand);

		public List<PecInDTO> getEmailConAllegati();

		public void setImportaAllegatiCommand(Command importaAllegatiCommand);

		public void mostraPulsantiera(boolean mostra);

		public void mostraTitolo(boolean mostra);

		public void setPubblicaAllegatiCommand(Command pubblicaAllegatiCommand);

		public void setModificaVisibilitaAllegatiCommand(Command pubblicaAllegatiCommand);

		public void setRimuoviPubblicazioneAllegatiCommand(Command rimuoviPubblicazioneAllegatiCommand);

		public void setCollegaFascicoloCommand(Command collegaFascicoloCommand);

		public void setGoToFascicoloCollegatoCommand(GoToFascicoloCollegatoCommand goToFascicoloCollegatoCommand);

		public void setGoToPraticaProcediCollegatoCommand(GoToPraticaProcediCollegatoCommand goToPraticaProcediCollegatoCommand);

		public Set<CollegamentoDto> getCollegamentiSelezionati();

		public void setEliminaCollegamentiSelezionatiCommand(Command eliminaCollegamentiSelezionatiCommand);

		public void setModificaCollegamentiSelezionatiCommand(Command modificaCollegamentiSelezionatiCommand);

		public void setAvviaProcedimentoCommand(GoToAvviaProcedimentoCommand goToAvviaProcedimentoCommand);

		public ElementoGruppoProtocollatoCapofila getCapofilaSelezionato();

		public Set<ElementoElenco> getPraticheProtSelezionate();

		public ElementoGruppoProtocollato getNonCapofilaSelezionato();

		public void setChiudiProcedimentoCommand(final GoToChiudiProcedimentoCommand goToChiudiProcedimentoCommand);

		public Set<ProcedimentoDto> getProcedimentiSelezionati();

		public Set<ElementoElenco> getPecSelezionate();

		public void setRiversamentoCartaceoCommand(Command command);

		public void setRiportaInLetturaCommand(Command command);

		public void setNuovaMailDaTemplateCommand(Command nuovaMailCommand);

		public void setAssegnaEsternoCommand(Command assegnaEsternoCommand);

		public void setModificaAbilitazioniAssegnaEsternoCommand(Command command);

		public void setRitornaDaInoltraEsterno(Command command);

		public void setStampaRicevuteConsegnaCommand(Command command);

		public void setCambiaStepIterCommand(CambiaStepIterCommand cambiaStepIterCommand);

		public void setMostraDettaglioComunicazioneCommand(MostraDettaglioComunicazioneCommand mostraDettaglioComunicazioneCommand);

		public void setModificaOperatoreCommand(ModificaOperatoreCommand command);

		public void resetComposizioneFascicolo();

		public List<DatoAggiuntivo> getDatiAggiuntivi();

		public boolean controlloServerDatiAggiuntivi(List<ValidazioneDatoAggiuntivoDTO> validazioneDatiAggiuntivi);

		public void setRichiediFirmaFascicoloCommand(RichiediFirmaCommand richiediFirmaFascicoloCommand);

		public void setRitiraTaskFirmaCommand(RitiraTaskFirmaCommand command);

		public void setEstraiEMLCommand(EstraiEMLCommand command);

		public void setInviaCSV(InviaDaCsvCommand command);

		public void setDettaglioFascicoloCollegatoCommand(DettaglioFascicoloCollegatoCommand dettaglioFascicoloCollegatoCommand);

		public List<String> getComposizionePraticheCollegateCaricate();

		public void setCercaPGCommand(CercaPgCommand cercaPgCommand);

		public SearchPGParams getSearchPGParams();

		public void showCatenaDocumentale(CatenaDocumentaleDTO catenaDocumentaleDTO);

		public ElementoCatenaDocumentaleWidget getElementoCatenaDocumentaleSelezionato();

		public void setNuovPdfDaTemplateCommand(Command nuovoPdfCommand);

		public void setAggiornaPGCommand(Command aggiornaPGCommand);

		public void setModificaFascicoloCommand(Command modificaFascicoloCommand);

		public void setModificaTipologieAllegatoCommand(Command modificaTipologieAllegatoCommand);

		void loadFormDatiAggiuntivi(EventBus eventBus, Object openingRequestor, DispatchAsync dispatcher);

		FormDatiAggiuntiviWidget getFormDatiAggiuntivi();

		public void setCollegaPraticaProcediCommand(Command collegaPraticaProcediCommand);

		public void setEmissionePermessoCommand(Command emissionePermessoCommand);

		void setSpostaAllegatiCommand(Command command);

		void setSpostaProtocollazioniCommand(Command command);

		void mostraPraticaProcedi(List<PraticaProcedi> praticaProcedi);

		Set<PraticaProcedi> getPraticheProcediSelezionate();

		void rimuoviPraticheProcedi(List<PraticaProcedi> praticheProcedi);

		void setDettaglioPraticaProcediCollegatoCommand(DettaglioPraticaProcediCollegatoCommand dettaglioPraticaProcediCollegatoCommand);

		List<PraticaProcedi> getPraticheProcedi();

		boolean getReloadDettaglio();

		void setReloadDettaglio(boolean reload);

		public void loadAutorizzazioni(EventBus eventBus, DispatchAsync dispatcher, PecInPraticheDB pecInPraticheDB);

		void setUploadZipCommand(UploadZipCommand uploadZipCommand);

		Set<AllegatoDTO> getAllegatiProtSelezionati();

		void setRicaricaCommand(Command command);
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.dettagliofascicolo)
	public interface MyProxy extends ProxyPlace<DettaglioFascicoloGenericoPresenter> {
		//
	}

	@Inject
	public DettaglioFascicoloGenericoPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, final PecInPraticheDB pecInDb, final DispatchAsync dispatcher,
			final SitemapMenu sitemap, final GestioneLinkSiteMapUtil gestioneLinkSiteMapUtil, final PlaceManager placeManager, final RichiediFirmaTaskApiClient richiediFirmaTaskApiClient,
			final CartellaFirmaWizardApiClient cartellaFirmaWizardApiClient, final TemplateSceltaWizardApiClient templateSceltaWizard, PecApiClient pecApiClient,
			ProfilazioneUtenteHandler profilazioneUtenteHandler, ConfigurazioniHandler configurazioniHandler) {
		super(eventBus, view, proxy);
		this.eventBus = eventBus;
		this.dispatcher = dispatcher;
		this.praticheDB = pecInDb;
		this.placeManager = placeManager;
		this.sitemapMenu = sitemap;
		this.gestioneLinkSiteMapUtil = gestioneLinkSiteMapUtil;
		this.richiediFirmaTaskApiClient = richiediFirmaTaskApiClient;
		this.cartellaFirmaWizardApiClient = cartellaFirmaWizardApiClient;
		this.templateSceltaWizard = templateSceltaWizard;
		this.pecApiClient = pecApiClient;
		this.profilazioneUtenteHandler = profilazioneUtenteHandler;
		this.configurazioniHandler = configurazioniHandler;
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPresenter.TYPE_SetMainContent, this);
	}

	@Override
	protected void onHide() {
		super.onHide();
		ShowMessageEvent event = new ShowMessageEvent();
		event.setMessageDropped(true);
		getEventBus().fireEvent(event);
		/*
		 * qui fa perdere lo stato delle selezioni, che serve al ritorno da presenter figli
		 */
		// v.reset();
	}

	@Override
	protected void onBind() {
		super.onBind();
		getView().loadFormDatiAggiuntivi(eventBus, this, dispatcher);
		getView().loadAutorizzazioni(eventBus, dispatcher, praticheDB);
		getView().setAnnullaSalvaFascicoloCommand(new AnnullaSalvaFascicoloCommand(this));
		getView().setSalvaDatiAggiuntiviCommand(new SalvaFascicoloCommand(this));
		getView().setSalvaNoteFascicoloCommand(new SalvaNoteFascicoloCommand());
		getView().setChiudiDettaglioCommand(new ChiudiDettaglioCommand(this));
		getView().setUploadAllegatoCommand(new UploadAllegatoCommand());
		getView().setEliminaAllegatoCommand(new EliminaAllegatoCommand(this));
		getView().setNascondiGruppiCommand(new MostraGruppiCommand(this));
		getView().setCambioStatoArchiviaCommand(new CambioStatoCommand());
		getView().setCambioStatoInGestioneCommand(new CambioStatoCommand());
		getView().setFirmaAllegatoCommand(new FirmaAllegatoCommand(this));
		getView().setRispondiMailCommand(new RispondiMailCommand(this));
		getView().setEliminaFascicoloCommand(new EliminaFascicoloCommand(this));
		getView().setProtocollaCommand(new ProtocollaCommand(this));
		getView().setGestioneInCaricoCommand(new GestioneInCaricoCommand(this));
		getView().setNuovaMailCommand(new NuovaMailCommand(this));
		getView().setNuovaMailInteroperabileCommand(new NuovaMailInteroperabileCommand(this));
		getView().setCambioStatoInAffissioneCommand(new CambioStatoCommand());
		getView().setScaricaAllegatiCommand(new ScaricaAllegatiCommand(this));
		getView().setImportaAllegatiCommand(new ImportaAllegatiModuloCommand(this));
		getView().setPubblicaAllegatiCommand(new PubblicaAllegatiCommand(this));
		getView().setModificaVisibilitaAllegatiCommand(new ModificaVisibilitaAllegatiCommand(this));
		getView().setRimuoviPubblicazioneAllegatiCommand(new RimuoviPubblicazioneAllegatiCommand(this));
		getView().setCollegaFascicoloCommand(new CollegaFascicoloCommand(this));
		getView().setGoToFascicoloCollegatoCommand(new GoToFascicoloCollegatoCommand());
		getView().setGoToPraticaProcediCollegatoCommand(new GoToPraticaProcediCollegatoCommand());
		getView().setEliminaCollegamentiSelezionatiCommand(new EliminaCollegamentiSelezionatiCommand(this));
		getView().setModificaCollegamentiSelezionatiCommand(new ModificaCollegamentiSelezionatiCommand(this));
		getView().setAvviaProcedimentoCommand(new GoToAvviaProcedimentoCommand(this));
		getView().setChiudiProcedimentoCommand(new GoToChiudiProcedimentoCommand(this));
		getView().setRiversamentoCartaceoCommand(new RiversamentoCartaceoDaDettaglioFascicoloCommand(this));
		getView().setSganciaPecInCommand(new SganciaPecInDaFascicoloCommand(this));
		getView().setRiportaInLetturaCommand(new RiportaInLetturaCommand(this));
		getView().setNuovaMailDaTemplateCommand(new NuovaMailDaTemplateCommand());
		getView().setAssegnaEsternoCommand(new AssegnaEsternoCommand(this));
		getView().setModificaAbilitazioniAssegnaEsternoCommand(new ModificaAbilitazioniAssegnaEsternoCommand(this));
		getView().setRitornaDaInoltraEsterno(new RitornaDaInoltrareEsternoCommand(this));
		getView().setStampaRicevuteConsegnaCommand(new StampaRicevuteConsegnaCommand<DettaglioFascicoloGenericoPresenter>(this));
		getView().setCambiaStepIterCommand(new CambiaStepIterCommand(this));
		getView().setMostraDettaglioComunicazioneCommand(new MostraDettaglioComunicazioneCommand(this));
		getView().setModificaOperatoreCommand(new ModificaOperatoreCommand(this));
		getView().setRichiediFirmaFascicoloCommand(new RichiediFirmaCommand());
		getView().setRitiraTaskFirmaCommand(new RitiraTaskFirmaCommand());
		getView().setDettaglioFascicoloCollegatoCommand(new DettaglioFascicoloCollegatoCommand());
		getView().setDettaglioPraticaProcediCollegatoCommand(new DettaglioPraticaProcediCollegatoCommand());
		getView().setCercaPGCommand(new CercaPgCommand(this));
		getView().setNuovPdfDaTemplateCommand(new NuovoPdfDaTemplateCommand());
		getView().setAggiornaPGCommand(new AggiornaPGCommand(this));
		getView().setEstraiEMLCommand(new EstraiEMLCommand());
		getView().setModificaFascicoloCommand(new ModificaFascicoloCommand(this));
		getView().setModificaTipologieAllegatoCommand(new ModificaTipologieAllegatoCommand());
		getView().setCollegaPraticaProcediCommand(new ListaPraticaProcediCommand(this));
		getView().setEmissionePermessoCommand(new EmissionePermessoCommand(this));
		getView().setUploadZipCommand(new UploadZipCommand());
		getView().setSpostaAllegatiCommand(new SpostaAllegatiCommand(this));
		getView().setSpostaProtocollazioniCommand(new SpostaProtocollazioniCommand(this));
		getView().setInviaCSV(new InviaDaCsvCommand());
		getView().setRicaricaCommand(new RicaricaCommand());

	}

	private void mostraPraticaProcedi(List<PraticaProcedi> praticaProcedi) {
		getView().mostraPraticaProcedi(praticaProcedi);
		revealInParent();
	}

	/**
	 * Istruisce la view per mostrare la pratica
	 */
	private void caricaPratica() {
		ShowAppLoadingEvent.fire(DettaglioFascicoloGenericoPresenter.this, true);

		if (getView().getReloadDettaglio()) {
			praticheDB.remove(fascicoloPath);
			getView().setReloadDettaglio(false);
		}

		praticheDB.getFascicoloByPath(fascicoloPath, true, new PraticaFascicoloLoaded() {

			@Override
			public void onPraticaLoaded(FascicoloDTO fascicolo) {
				ShowAppLoadingEvent.fire(DettaglioFascicoloGenericoPresenter.this, false);
				mostraPratica(fascicolo);

				if (fascicolo.getIdPraticheProcedi() != null && !fascicolo.getIdPraticheProcedi().isEmpty()) {

					DettaglioPraticaProcediAction action = new DettaglioPraticaProcediAction(fascicolo.getIdPraticheProcedi());
					dispatcher.execute(action, new AsyncCallback<DettaglioPraticaProcediResult>() {

						@Override
						public void onFailure(Throwable caught) {
							ShowAppLoadingEvent.fire(DettaglioFascicoloGenericoPresenter.this, false);
							ShowMessageEvent event = new ShowMessageEvent();
							event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
							getEventBus().fireEvent(event);
						}

						@Override
						public void onSuccess(DettaglioPraticaProcediResult result) {

							if (!result.isError()) {
								mostraPraticaProcedi(result.getPraticheProcedi());

							} else {
								ShowAppLoadingEvent.fire(DettaglioFascicoloGenericoPresenter.this, false);
								ShowMessageEvent event = new ShowMessageEvent();
								event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
								getEventBus().fireEvent(event);
							}

						}
					});

				} else {
					mostraPraticaProcedi(null);
				}

				gestioneLinkSiteMapUtil.aggiungiLinkInLavorazione(fascicolo);
				getView().mostraTitolo(true);
				getView().mostraPulsantiera(true);
				ApertoDettaglioEvent.fire(DettaglioFascicoloGenericoPresenter.this, fascicolo);
			}

			@Override
			public void onPraticaError(String error) {
				ShowAppLoadingEvent.fire(DettaglioFascicoloGenericoPresenter.this, false);
				ShowMessageEvent event = new ShowMessageEvent();
				event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
				getEventBus().fireEvent(event);
			}
		});
	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
		this.fascicoloPath = request.getParameter(NameTokensParams.idPratica, null);
		getView().resetDisclosurePanels(Boolean.parseBoolean(request.getParameter(NameTokensParams.showActions, null)));
		if (Boolean.parseBoolean(request.getParameter(NameTokensParams.resetComposizioneFascicolo, null))) {
			getView().resetComposizioneFascicolo();
		}
		if (super.isVisible()) {
			getView().mostraTitolo(false);
			getView().mostraPulsantiera(false);
			caricaPratica();
		}

	}

	@Override
	protected void onReveal() {
		if (placeManager != null) {
			PlaceRequest request = placeManager.getCurrentPlaceRequest();
			if (request != null && Boolean.parseBoolean(request.getParameter(ConsolePecConstants.FORCE_RELOAD_PARAM, Boolean.FALSE.toString()))) {
				getView().setReloadDettaglio(true);
			}
		}
		// nascondo la pulsantiera, la modifico (nella view), poi la riabilito
		// nell'onPraticaLoaded()
		getView().mostraTitolo(false);
		getView().mostraPulsantiera(false);
		super.onReveal();
		revealInParent();
		caricaPratica();
		/**/
		ShowMessageEvent event = new ShowMessageEvent();
		event.setMessageDropped(true);
		getEventBus().fireEvent(event);

	}

	private void mostraPratica(FascicoloDTO fascicolo) {
		getView().mostraPratica(fascicolo);

	}

	/**
	 * Al ritorno dalla form di inserimento dati di firma, si verifica se sia necessario procedere alla richiesta ed in tal caso, si salva preventivamente il fascicolo
	 */
	@Override
	@ProxyEvent
	public void onFirmaDigitaleEnd(final FirmaDigitaleFineEvent event) {

		if (DettaglioFascicoloGenericoPresenter.this.equals(event.getOpeningRequestor())) {

			if (!event.isAnnulla()) {
				ShowAppLoadingEvent.fire(DettaglioFascicoloGenericoPresenter.this, true);
				final AllegatoDTO[] allegati = getView().getAllegatiSelezionati().toArray(new AllegatoDTO[0]);

				/* reset dei messaggi di errore */
				ShowMessageEvent event2 = new ShowMessageEvent();
				event2.setMessageDropped(true);
				getEventBus().fireEvent(event2);

				/*
				 * l'utente ha deciso di firmare, prima devo comunque salvare il fascicolo
				 */
				salvaFascicolo(new AsyncCallback<SalvaFascicoloResult>() {

					@Override
					public void onFailure(Throwable caught) {
						ShowAppLoadingEvent.fire(DettaglioFascicoloGenericoPresenter.this, false);
						ShowMessageEvent event = new ShowMessageEvent();
						event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
						getEventBus().fireEvent(event);
					}

					@Override
					public void onSuccess(SalvaFascicoloResult result) {
						/* procedo alla richiesta di firma */
						FirmaAllegatoFascicoloAction action = new FirmaAllegatoFascicoloAction(fascicoloPath, allegati);
						action.setCredenzialiFirma(event.getCredenzialiFirma());
						action.setTipologiaFirma(event.getTipologiaFirma());

						if (event.getCredenzialiFirma() != null) {
							profilazioneUtenteHandler.aggiornaPreferenzeFirmaDigitale(event.getCredenzialiFirma().isSalvaCredenziali(), event.getCredenzialiFirma().getUsername(),
									event.getCredenzialiFirma().getPassword(), null);
						}

						dispatcher.execute(action, new AsyncCallback<FirmaAllegatoFascicoloResult>() {

							@Override
							public void onFailure(Throwable caught) {
								ShowAppLoadingEvent.fire(DettaglioFascicoloGenericoPresenter.this, false);
								ShowMessageEvent event = new ShowMessageEvent();
								event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
								getEventBus().fireEvent(event);
							}

							@Override
							public void onSuccess(FirmaAllegatoFascicoloResult result) {
								ShowAppLoadingEvent.fire(DettaglioFascicoloGenericoPresenter.this, false);
								if (result.isError()) {
									ShowMessageEvent event = new ShowMessageEvent();
									event.setErrorMessage(result.getErrMsg());
									getEventBus().fireEvent(event);
								} else {
									praticheDB.insertOrUpdate(result.getFascicolo().getClientID(), result.getFascicolo(), sitemapMenu.containsLink(result.getFascicolo().getClientID()));
									revealInParent();
								}

							}
						});
					}
				}, false);// non chiama mostra pratiche :non serve
			} else {
				revealInParent();
			}
		}
	}

	@Override
	@ProxyEvent
	public void onChiudiDettaglioAllegato(ChiudiDettaglioAllegatoEvent event) {
		if (event.getClientID().equals(fascicoloPath) && placeManager.getCurrentPlaceRequest().getNameToken().equals(NameTokens.dettagliofascicolo)) {
			ShowAppLoadingEvent.fire(this, false);
			placeManager.revealCurrentPlace();
		}
	}

	@Override
	@ProxyEvent
	public void onTerminaAttesa(TerminaAttesaEvent event) {
		this.uploadTimeout = true;
	}

	public void salvaFascicolo(final AsyncCallback<SalvaFascicoloResult> callback) {
		salvaFascicolo(callback, true);
	}

	private void salvaFascicolo(final AsyncCallback<SalvaFascicoloResult> callback, final boolean aggiornaView) {
		if (callback == null) {// È LA CALLBACK HA GIA APERTO LA FINESTRA DI ATTESA
			ShowAppLoadingEvent.fire(DettaglioFascicoloGenericoPresenter.this, true);
		}

		FascicoloDTO dtoUpdated = getView().getFascicolo();
		SalvaFascicolo action = new SalvaFascicolo(dtoUpdated);
		/* reset dei messaggi di errore */
		ShowMessageEvent event = new ShowMessageEvent();
		event.setMessageDropped(true);
		getEventBus().fireEvent(event);
		dispatcher.execute(action, new AsyncCallback<SalvaFascicoloResult>() {

			@Override
			public void onFailure(Throwable caught) {
				if (callback != null) {
					callback.onFailure(caught);
				} else {
					ShowAppLoadingEvent.fire(DettaglioFascicoloGenericoPresenter.this, false);
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
					getEventBus().fireEvent(event);
				}

			}

			@Override
			public void onSuccess(SalvaFascicoloResult result) {
				if (result.isError()) {
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(result.getErrorMsg());
					getEventBus().fireEvent(event);
					return;
				}
				praticheDB.insertOrUpdate(result.getFascicolo().getClientID(), result.getFascicolo(), sitemapMenu.containsLink(result.getFascicolo().getClientID()));

				if (aggiornaView) {
					getView().mostraPratica(result.getFascicolo());
				}

				if (callback != null) {
					callback.onSuccess(result);
				} else {
					ShowAppLoadingEvent.fire(DettaglioFascicoloGenericoPresenter.this, false);
				}
			}
		});
	}

	public void eliminaAction() {
		eventId = DOM.createUniqueId();
		RichiestaConfermaAnnullaEvent.fire(DettaglioFascicoloGenericoPresenter.this, "<h4>Procedere con la cancellazione del fascicolo?<h4>", eventId);
	}

	/* Elenco definizione command */

	public class MostraDettaglioEmailCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, String> {

		@Override
		public Void exe(String pecId) {
			Place place = new Place();
			place.setToken(NameTokens.dettagliopecin);
			place.addParam(NameTokensParams.idPratica, pecId);
			getEventBus().fireEvent(new GoToPlaceEvent(place));
			return null;
		}

	}

	public class MostraDettaglioBozzaCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, String> {

		@Override
		public Void exe(String t) {
			Place place = new Place();
			place.setToken(NameTokens.dettagliopecout);
			place.addParam(NameTokensParams.idPratica, t);
			getEventBus().fireEvent(new GoToPlaceEvent(place));
			return null;
		}
	}

	public class UploadAllegatoCommand {

		public void onFileSelected(String fileName) {
			// ShowAppLoadingEvent showAppLoadingEvent = new
			// ShowAppLoadingEvent(true, 1000 * 20);
			// getEventBus().fireEvent(showAppLoadingEvent);
			// sono stati selezionati i file da caricare
			getEventBus().fireEvent(new UploadEvent(fascicoloPath, UploadStatus.START));
			DettaglioFascicoloGenericoPresenter.this.getView().startUpload();
		}

		public void onFileUploaded(RispostaFileUploaderDTO dto) {
			// l'upload lato servlet è completato
			if (uploadTimeout) {
				uploadTimeout = false;
				getEventBus().fireEvent(new UploadEvent(fascicoloPath, UploadStatus.ERROR));
				return;
			}
			if (!dto.isError()) {
				// se non ci sono errori procedo con il salvatagggio in alfresco
				UploadAllegatoFascicolo action = new UploadAllegatoFascicolo(DettaglioFascicoloGenericoPresenter.this.fascicoloPath);
				action.setTmpFiles(dto.getTmpFiles());
				/* reset dei messaggi di errore */
				ShowMessageEvent event = new ShowMessageEvent();
				event.setMessageDropped(true);
				getEventBus().fireEvent(event);

				dispatcher.execute(action, new AsyncCallback<UploadAllegatoFascicoloResult>() {

					@Override
					public void onFailure(Throwable caught) {
						// ShowAppLoadingEvent.fire(DettaglioFascicoloGenericoPresenter.this,
						// false);
						getEventBus().fireEvent(new UploadEvent(fascicoloPath, UploadStatus.ERROR));
						ShowMessageEvent event = new ShowMessageEvent();
						event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
						getEventBus().fireEvent(event);
					}

					@Override
					public void onSuccess(UploadAllegatoFascicoloResult result) {
						// ShowAppLoadingEvent.fire(DettaglioFascicoloGenericoPresenter.this, false);
						if (!result.isError()) {
							ShowMessageEvent event = new ShowMessageEvent();
							event.setMessageDropped(true);
							getEventBus().fireEvent(event);

							FascicoloDTO dto = result.getFascicolo();
							praticheDB.insertOrUpdate(dto.getClientID(), dto, sitemapMenu.containsLink(dto.getClientID()));
							DettaglioFascicoloGenericoPresenter.this.mostraPratica(dto);
							getEventBus().fireEvent(new UploadEvent(fascicoloPath, UploadStatus.DONE));

						} else {
							ShowMessageEvent event = new ShowMessageEvent();
							event.setErrorMessage(result.getErrorMsg());
							getEventBus().fireEvent(event);
							getEventBus().fireEvent(new UploadEvent(fascicoloPath, UploadStatus.ERROR));
						}
					}
				});
			} else {
				// ShowAppLoadingEvent.fire(DettaglioFascicoloGenericoPresenter.this, false);
				ShowMessageEvent event = new ShowMessageEvent();
				event.setErrorMessage(dto.getMessError());
				getEventBus().fireEvent(event);
				getEventBus().fireEvent(new UploadEvent(fascicoloPath, UploadStatus.ERROR));
			}
		}
	}

	public class UploadZipCommand {

		public void onFileSelected() {
			DettaglioFascicoloGenericoPresenter.this.getView().startZipUpload();
		}

		public boolean onSubmitUpload(Integer fileNumber, String[] fileNames, Long[] fileLength) {
			ShowMessageEvent event = new ShowMessageEvent();
			if (fileNumber == null || fileNumber != 1) {
				event.setErrorMessage("E' possibile caricare un solo file di tipo zip alla volta.");
				getEventBus().fireEvent(event);
				Window.scrollTo(0, 0);
				return false;
			}
			if (!fileNames[0].endsWith(".zip")) {
				event.setErrorMessage("Il file selezionato deve essere di tipo zip.");
				getEventBus().fireEvent(event);
				Window.scrollTo(0, 0);
				return false;
			}
			if (fileLength[0] > (ConsolePecConstants.MAX_ZIP_FILE_LENGTH_MB * Math.pow(2, 20))) {
				event.setErrorMessage("Il file selezionato non puo' essere piu' grande di " + ConsolePecConstants.MAX_ZIP_FILE_LENGTH_MB + " MB.");
				getEventBus().fireEvent(event);
				Window.scrollTo(0, 0);
				return false;
			}
			ShowAppLoadingEvent.fire(DettaglioFascicoloGenericoPresenter.this, true);
			return true;
		}

		public void onFileUploaded(RispostaFileUploaderDTO dto) {
			if (dto.isError()) {
				ShowMessageEvent event = new ShowMessageEvent();
				event.setErrorMessage(dto.getMessError());
				getEventBus().fireEvent(event);
				return;
			}
			TmpFileUploadDTO tmpDTO = dto.getTmpFiles().get(0);
			String pathFascicolo = Base64Utils.URLdecodeAlfrescoPath(fascicoloPath);
			UploadFileZipAction action = new UploadFileZipAction(pathFascicolo, tmpDTO.getDirName(), tmpDTO.getFileName());
			dispatcher.execute(action, new AsyncCallback<UploadFileZipResult>() {
				@Override
				public void onFailure(Throwable caught) {
					ShowAppLoadingEvent.fire(DettaglioFascicoloGenericoPresenter.this, false);
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage("Si e' verificato un errore durante l'attesa del caricamento del file zip, il caricamento del file potrebbe essere ancora in corso.");
					getEventBus().fireEvent(event);

					consoleLog("Errore nell'attesa del server", caught);
				}

				@Override
				public void onSuccess(UploadFileZipResult result) {
					ShowAppLoadingEvent.fire(DettaglioFascicoloGenericoPresenter.this, false);
					if (result.isError()) {
						ShowMessageEvent event = new ShowMessageEvent();
						event.setErrorMessage("Si e' verificato un errore durante il caricamento del file zip: " + result.getMsgError());
						getEventBus().fireEvent(event);
					} else {
						FascicoloDTO dto = result.getFascicoloDTO();
						praticheDB.insertOrUpdate(dto.getClientID(), dto, sitemapMenu.containsLink(dto.getClientID()));
						DettaglioFascicoloGenericoPresenter.this.mostraPratica(dto);

						ShowMessageEvent event = new ShowMessageEvent();
						event.setInfoMessage("Il caricamento e' stato eseguito correttamente.");
						getEventBus().fireEvent(event);
					}
				}
			});
		}
	}

	native void consoleLog(String message, Throwable t) /*-{
		console.log("Message: [" + message + "] segue eccezione se presente: ");
		console.log(t);
	}-*/;

	public class CambioStatoCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, CambiaStatoFascicoloEnum> {

		@Override
		public Void exe(CambiaStatoFascicoloEnum statoFascicolo) {

			ShowAppLoadingEvent.fire(DettaglioFascicoloGenericoPresenter.this, true);
			Set<String> set = new HashSet<String>();
			set.add(DettaglioFascicoloGenericoPresenter.this.fascicoloPath);
			/* reset dei messaggi di errore */
			ShowMessageEvent event = new ShowMessageEvent();
			event.setMessageDropped(true);
			getEventBus().fireEvent(event);
			DettaglioFascicoloGenericoPresenter.this.dispatcher.execute(new CambiaStatoFascicolo(set, statoFascicolo), new AsyncCallback<CambiaStatoFascicoloResult>() {

				@Override
				public void onFailure(Throwable caught) {
					ShowAppLoadingEvent.fire(DettaglioFascicoloGenericoPresenter.this, false);
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
					getEventBus().fireEvent(event);
				}

				@Override
				public void onSuccess(CambiaStatoFascicoloResult result) {
					ShowAppLoadingEvent.fire(DettaglioFascicoloGenericoPresenter.this, false);
					if (result.isError()) {
						ShowMessageEvent event = new ShowMessageEvent();
						event.setErrorMessage(result.getErrorMsg());
						getEventBus().fireEvent(event);
					} else {
						FascicoloDTO fascicoloRes = result.getFascicoli().get(0);
						DettaglioFascicoloGenericoPresenter.this.praticheDB.insertOrUpdate(fascicoloRes.getClientID(), fascicoloRes, sitemapMenu.containsLink(fascicoloRes.getClientID()));
						DettaglioFascicoloGenericoPresenter.this.getView().mostraPratica(fascicoloRes);

					}
				}
			});

			return null;
		}
	}

	public class RichiediFirmaCommand implements Command {

		@Override
		public void execute() {
			String fascicoloPath = getFascicoloPath();
			Set<AllegatoDTO> allegatiSelezionati = getView().getAllegatiSelezionati();

			richiediFirmaTaskApiClient.goToRichiediVistoFirma(fascicoloPath, allegatiSelezionati);
		}
	}

	public class RitiraTaskFirmaCommand implements Command {

		@Override
		public void execute() {
			String fascicoloPath = getFascicoloPath();
			Set<AllegatoDTO> allegatiSelezionati = getView().getAllegatiSelezionati();
			Map<String, List<AllegatoDTO>> praticaAllegatiMap = new HashMap<String, List<AllegatoDTO>>();
			List<AllegatoDTO> allegatiList = new ArrayList<AllegatoDTO>(allegatiSelezionati);
			praticaAllegatiMap.put(fascicoloPath, allegatiList);
			cartellaFirmaWizardApiClient.ritiraDocumenti(praticaAllegatiMap, true);
		}
	}

	public class InviaDaCsvCommand implements Command {

		@Override
		public void execute() {
			getEventBus().fireEvent(new ApriInvioCSVEvent(fascicoloPath, getView().getAllegatiSelezionati().iterator().next().getNome()));
		}

	}

	public class EstraiEMLCommand implements Command {

		@Override
		public void execute() {

			if (getView().getPecSelezionate().size() == 1) {
				ShowAppLoadingEvent.fire(DettaglioFascicoloGenericoPresenter.this, true);

				ElementoPECRiferimento el = (ElementoPECRiferimento) getView().getPecSelezionate().iterator().next();
				pecApiClient.estraiEML(el.getRiferimento(), getFascicoloPath(), new PraticaCallback() {

					@Override
					public void onError(String error) {
						ShowAppLoadingEvent.fire(DettaglioFascicoloGenericoPresenter.this, false);

						ShowMessageEvent event = new ShowMessageEvent();
						event.setErrorMessage(error);
						getEventBus().fireEvent(event);
					}

					@Override
					public void onComplete(PraticaDTO pratica) {
						ShowAppLoadingEvent.fire(DettaglioFascicoloGenericoPresenter.this, false);

						praticheDB.insertOrUpdate(pratica.getClientID(), pratica, sitemapMenu.containsLink(pratica.getClientID()));
						getView().mostraPratica((FascicoloDTO) pratica);
					}
				});
			}
		}
	}

	public class MostraDettaglioAllegatoProcediCommand implements it.eng.portlet.consolepec.gwt.client.presenter.BiCommand<Object, String, AllegatoProcedi> {

		@Override
		public Object exe(String idPraticaProcedi, AllegatoProcedi allegato) {

			MostraDettaglioAllegatoProcediEvent.fire(DettaglioFascicoloGenericoPresenter.this, idPraticaProcedi, allegato);
			return null;
		}

	}

	public class MostraDettaglioAllegatoCommand implements it.eng.portlet.consolepec.gwt.client.presenter.BiCommand<Object, String, AllegatoDTO> {

		@Override
		public Object exe(String pathPraticaAllegato, AllegatoDTO allegato) {

			MostraDettaglioAllegatoEvent.fire(DettaglioFascicoloGenericoPresenter.this, pathPraticaAllegato, fascicoloPath, allegato);
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

	public class DettaglioPraticaProcediCollegatoCommand implements BiCommand<Void, ElementoPraticaProcediCollegato, PraticaProcedi> {

		@Override
		public Void exe(ElementoPraticaProcediCollegato elementoPraticaProcediCollegato, PraticaProcedi praticaProcedi) {

			addAllegatiPraticaProcediDTOInComposizione(praticaProcedi, elementoPraticaProcediCollegato);

			elementoPraticaProcediCollegato.openDettaglioPanel();

			return null;
		}
	}

	public class DettaglioFascicoloCollegatoCommand implements BiCommand<Void, ElementoFascicoloCollegato, CollegamentoDto> {

		@Override
		public Void exe(ElementoFascicoloCollegato elementoFascicoloCollegato, CollegamentoDto collegamento) {

			if (!getView().getComposizionePraticheCollegateCaricate().contains(collegamento.getClientId())) {

				Timer showAppLoadingEventTimer = new Timer() {
					@Override
					public void run() {
						ShowAppLoadingEvent.fire(DettaglioFascicoloGenericoPresenter.this, true);
					}
				};

				showAppLoadingEventTimer.schedule(2000);
				addAllegatiDTOInComposizione(collegamento, elementoFascicoloCollegato);
				addElementiComposizione(collegamento, elementoFascicoloCollegato, showAppLoadingEventTimer);
				getView().getComposizionePraticheCollegateCaricate().add(collegamento.getClientId());

			} else {
				elementoFascicoloCollegato.openDettaglioPanel();
			}

			return null;
		}
	}

	private void addAllegatiDTOInComposizione(CollegamentoDto collegamentoDTO, ElementoFascicoloCollegato e) {

		for (AllegatoDTO allegato : collegamentoDTO.getAllegati()) {
			e.addAllegatoWidget(allegato, collegamentoDTO.getClientId(), new DownloadAllegatoCommand(), new MostraDettaglioAllegatoCommand());
		}
	}

	private void addAllegatiPraticaProcediDTOInComposizione(PraticaProcedi praticaProcedi, ElementoPraticaProcediCollegato e) {
		e.cleanDettaglioPanel();
		for (AllegatoProcedi allegato : praticaProcedi.getAllegati()) {
			DownloadAllegatoProcediCollegamentoCommand downloadAllegatoProcediCollegamentoCommand = new DownloadAllegatoProcediCollegamentoCommand(this, allegato.getIdAlfresco());

			e.addAllegatoWidget(allegato, downloadAllegatoProcediCollegamentoCommand);
		}
	}

	private void addElementiComposizione(CollegamentoDto collegamento, final ElementoFascicoloCollegato e, final Timer showAppLoadingEventTimer) {

		ComposizioneFascicoliCollegatiAction action = new ComposizioneFascicoliCollegatiAction();
		action.getPecInComposizione().addAll(collegamento.getElencoPEC());
		action.getPraticheModulisticaInComposizione().addAll(collegamento.getElencoPraticheModulistica());

		dispatcher.execute(action, new AsyncCallback<ComposizioneFascicoliCollegatiActionResult>() {

			@Override
			public void onFailure(Throwable caught) {
				ShowMessageEvent event = new ShowMessageEvent();
				event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
				getEventBus().fireEvent(event);
			}

			@Override
			public void onSuccess(ComposizioneFascicoliCollegatiActionResult result) {
				addPecComposizione(result.getPec(), e);
				addPraticheModulisticaWidget(result.getPraticheModulistica(), e);

				ShowAppLoadingEvent.fire(DettaglioFascicoloGenericoPresenter.this, false);
				showAppLoadingEventTimer.cancel();
				e.openDettaglioPanel();
			}
		});
	}

	private void addPecComposizione(List<PecDTO> pec, ElementoFascicoloCollegato e) {
		for (PecDTO pecDTO : pec) {
			e.addPecWidget(pecDTO, new DownloadAllegatoCommand(), new MostraDettaglioAllegatoCommand(), new MostraDettaglioEmailCommand(), new MostraDettaglioBozzaCommand());
		}
	}

	private void addPraticheModulisticaWidget(List<PraticaModulisticaDTO> praticheModulistica, ElementoFascicoloCollegato e) {
		for (PraticaModulisticaDTO praticaModulisticaDTO : praticheModulistica) {
			e.addPraticaModulisticaWidget(praticaModulisticaDTO, new DownloadAllegatoCommand(), new MostraDettaglioAllegatoCommand(), new GoToDettaglioModuloCommand(this));
		}
	}

	@Override
	@ProxyEvent
	public void onSceltaConfermaAnnulla(SceltaConfermaAnnullaEvent sceltaConfermaCancellazioneFascicoloEvent) {
		if (sceltaConfermaCancellazioneFascicoloEvent.isConfermato() && sceltaConfermaCancellazioneFascicoloEvent.getEventId().equals(eventId)) {
			ShowAppLoadingEvent.fire(DettaglioFascicoloGenericoPresenter.this, true);
			/* reset dei messaggi di errore */
			ShowMessageEvent showEventMessage = new ShowMessageEvent();
			showEventMessage.setMessageDropped(true);
			getEventBus().fireEvent(showEventMessage);
			dispatcher.execute(cambiaStatoFascicolo, new AsyncCallback<CambiaStatoFascicoloResult>() {

				@Override
				public void onFailure(Throwable caught) {
					ShowAppLoadingEvent.fire(DettaglioFascicoloGenericoPresenter.this, false);
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
					getEventBus().fireEvent(event);
				}

				@Override
				public void onSuccess(CambiaStatoFascicoloResult result) {
					if (result.isError()) {
						ShowAppLoadingEvent.fire(DettaglioFascicoloGenericoPresenter.this, false);
						ShowMessageEvent event = new ShowMessageEvent();
						event.setErrorMessage(result.getErrorMsg());
						getEventBus().fireEvent(event);
					} else {
						ShowAppLoadingEvent.fire(DettaglioFascicoloGenericoPresenter.this, false);
						ShowMessageEvent event = new ShowMessageEvent();
						event.setMessageDropped(true);
						for (String id : result.getClientIdEliminati()) {
							praticheDB.remove(id);
						}
						ShowAppLoadingEvent.fire(DettaglioFascicoloGenericoPresenter.this, false);
						getEventBus().fireEvent(new BackFromPlaceEvent(fascicoloPath));
					}
				}
			});

		}

	}

	@Override
	@ProxyEvent
	public void onAnnullaImportaAllegatiEmail(ChiudiAllegatiEmailEvent event) {
		if (event.getFascicolo() != null) {
			praticheDB.remove(event.getFascicolo().getClientID());
		}
		revealInParent();
	}

	@Override
	@ProxyEvent
	public void onIndietroSceltaCapofilaFascicolo(IndietroSceltaCapofilaFascicoloEvent event) {
		fascicoloPath = event.getIdFascicolo();
		revealInParent();

	}

	@Override
	@ProxyEvent
	public void onChiudiPubblicazioneAllegati(ChiudiPubblicazioneAllegatiEvent event) {
		praticheDB.remove(event.getIdFascicolo());
		revealInParent();
	}

	public class GoToFascicoloCollegatoCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, CollegamentoDto> {

		@Override
		public Void exe(CollegamentoDto collegamentoDto) {
			praticheDB.remove(collegamentoDto.getClientId());
			Place place = new Place();
			place.forceHistory();
			place.setToken(NameTokens.dettagliofascicolo);
			place.addParam(NameTokensParams.idPratica, collegamentoDto.getClientId());
			getEventBus().fireEvent(new GoToPlaceEvent(place));
			return null;
		}
	}

	public class GoToPraticaProcediCollegatoCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, PraticaProcedi> {

		@Override
		public Void exe(PraticaProcedi praticaProcedi) {
			Place place = new Place();
			place.setToken(NameTokens.praticaprocedi);
			place.addParam(NameTokensParams.idPratica, praticaProcedi.getChiaveAllegati());
			place.addParam(NameTokensParams.nomeClasseDiRitorno, DettaglioFascicoloGenericoPresenter.class.getName());
			place.addParam(NameTokensParams.idFascicolo, fascicoloPath);
			getEventBus().fireEvent(new GoToPlaceEvent(place));
			return null;
		}
	}

	@Override
	@ProxyEvent
	public void onChiudiCondividiFascicolo(ChiudiCondividiFascicoloEvent event) {
		if (event.getFascicoloPath() != null) {
			praticheDB.remove(event.getFascicoloPath());
			revealInParent();
		}
	}

	@Override
	@ProxyEvent
	public void onChiudiDettaglioModulo(ChiudiDettaglioModuloEvent event) {
		revealInParent();
	}

	@Override
	@ProxyEvent
	public void onChiudiCollegaFascicoloDiretto(ChiudiCollegaFascicoloDirettoEvent event) {
		revealInParent();
	}

	@Override
	public DispatchAsync getDispatchAsync() {
		return dispatcher;
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
	public EventBus _getEventBus() {
		return getEventBus();
	}

	@Override
	public void downloadStampa(SafeUri uri) {
		getView().sendDownload(uri);
		// ricarico il fascicolo se no mi restano le protocollazioni selezionate
		getView().mostraPratica(DettaglioFascicoloGenericoPresenter.this.getView().getFascicolo());
	}

	public void setCambiaStatoFascicolo(CambiaStatoFascicolo newCambiaStatoFascicolo) {
		cambiaStatoFascicolo = newCambiaStatoFascicolo;
	}

	@Override
	public String getFascicoloPath() {
		return fascicoloPath;
	}

	public SitemapMenu getSitemapMenu() {
		return sitemapMenu;
	}

	@Override
	@ProxyEvent
	public void onBackToFascicolo(BackToFascicoloEvent event) {
		fascicoloPath = event.getFascicoloID();
		revealInParent();
	}

	@Override
	public String getPecOutPath() {
		// diamo per scontato che nella view abbiamo già controllato che ci sia
		// solo una pec OUT selezionata in stato CONSEGNATA o
		// PARZIALEMNTECONSEGNATA
		ElementoPECRiferimento next = (ElementoPECRiferimento) getView().getPecSelezionate().iterator().next();
		String praticaPath = next.getRiferimento();
		return praticaPath;
	}

	private class NuovaMailDaTemplateCommand implements Command {

		@Override
		public void execute() {
			templateSceltaWizard.goToCreaDaTemplate(fascicoloPath, TipologiaPratica.MODELLO_MAIL);
		}
	}

	private class NuovoPdfDaTemplateCommand implements Command {

		@Override
		public void execute() {
			templateSceltaWizard.goToCreaDaTemplate(fascicoloPath, TipologiaPratica.MODELLO_PDF);
		}
	}

	@Override
	@ProxyEvent
	public void onAnagraficaSelezionata(SelezionaAnagraficaFineEvent event) {
		if (this.equals(event.getOpeningRequestor())) {
			if (!event.isAnnulla()) {
				List<ConfigurazioneEsecuzione> esecuzioni = Collections.emptyList();
				AnagraficaFascicolo af = configurazioniHandler.getAnagraficaFascicolo(getView().getFascicolo().getTipologiaPratica().getNomeTipologia());
				if (af != null) {
					esecuzioni = af.getConfigurazioneEsecuzioni();
				}
				getView().getFormDatiAggiuntivi().setAnagrafica(event.getNomeDatoAggiuntivo(), event.getAnagrafica(), esecuzioni);
			}
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

	public class ModificaTipologieAllegatoCommand implements Command {
		@Override
		public void execute() {
			_getEventBus().fireEvent(new MostraModificaTipologieAllegatoEvent(getFascicoloPath(), getView().getAllegatiSelezionati()));
		}
	}

	public class RicaricaCommand implements Command {

		@Override
		public void execute() {
			ShowAppLoadingEvent.fire(DettaglioFascicoloGenericoPresenter.this, true);
			praticheDB.remove(fascicoloPath);
			praticheDB.getFascicoloByPath(fascicoloPath, false, new PraticaFascicoloLoaded() {

				@Override
				public void onPraticaLoaded(FascicoloDTO dto) {
					ShowAppLoadingEvent.fire(DettaglioFascicoloGenericoPresenter.this, false);
					praticheDB.insertOrUpdate(dto.getClientID(), dto, sitemapMenu.containsLink(dto.getClientID()));
					DettaglioFascicoloGenericoPresenter.this.mostraPratica(dto);
				}

				@Override
				public void onPraticaError(String error) {
					ShowAppLoadingEvent.fire(DettaglioFascicoloGenericoPresenter.this, false);
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
					eventBus.fireEvent(event);
				}

			});
		}

	}

	public class SalvaNoteFascicoloCommand implements Command {

		@Override
		public void execute() {

			ShowAppLoadingEvent.fire(DettaglioFascicoloGenericoPresenter.this, true);
			SalvaNoteAction action = new SalvaNoteAction(fascicoloPath, getView().getNote());

			dispatcher.execute(action, new AsyncCallback<SalvaNoteResult>() {

				@Override
				public void onFailure(Throwable caught) {
					ShowAppLoadingEvent.fire(DettaglioFascicoloGenericoPresenter.this, false);
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
					eventBus.fireEvent(event);
				}

				@Override
				public void onSuccess(SalvaNoteResult result) {
					ShowAppLoadingEvent.fire(DettaglioFascicoloGenericoPresenter.this, false);

					if (result.isError()) {
						ShowMessageEvent event = new ShowMessageEvent();
						event.setErrorMessage(result.getErrorMessage());
						eventBus.fireEvent(event);

					} else {
						FascicoloDTO dto = result.getFascicolo();
						praticheDB.insertOrUpdate(dto.getClientID(), dto, sitemapMenu.containsLink(dto.getClientID()));
						DettaglioFascicoloGenericoPresenter.this.mostraPratica(dto);
					}

				}
			});

		}

	}

}
