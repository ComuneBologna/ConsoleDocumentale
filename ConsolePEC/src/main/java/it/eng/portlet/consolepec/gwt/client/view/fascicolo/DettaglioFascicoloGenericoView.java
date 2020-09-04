package it.eng.portlet.consolepec.gwt.client.view.fascicolo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import com.google.common.base.Strings;
import com.google.gwt.dom.client.ButtonElement;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.HRElement;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ConsoleDisclosurePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.ViewImpl;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaFascicolo;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaFascicolo.StepIter;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo;
import it.eng.cobo.consolepec.commons.pratica.fascicolo.composizione.ElementoComposizione;
import it.eng.cobo.consolepec.commons.pratica.fascicolo.composizione.EmailComposizione;
import it.eng.cobo.consolepec.commons.urbanistica.PraticaProcedi;
import it.eng.cobo.consolepec.util.console.ConsoleConstants;
import it.eng.cobo.consolepec.util.generics.GenericsUtil;
import it.eng.portlet.consolepec.gwt.client.composizione.RicercaComposizioneFascicoloHandler;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaEmaiInlLoaded;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaFascicoloLoaded;
import it.eng.portlet.consolepec.gwt.client.event.ImpostaPulsantieraFascicoloGenericoEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.operazioni.pec.PecApiClient;
import it.eng.portlet.consolepec.gwt.client.operazioni.pec.PecApiClient.BooleanCallback;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.DettaglioFascicoloGenericoPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.DettaglioFascicoloGenericoPresenter.DettaglioFascicoloCollegatoCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.DettaglioFascicoloGenericoPresenter.DettaglioPraticaProcediCollegatoCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.DettaglioFascicoloGenericoPresenter.EstraiEMLCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.DettaglioFascicoloGenericoPresenter.GoToFascicoloCollegatoCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.DettaglioFascicoloGenericoPresenter.GoToPraticaProcediCollegatoCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.DettaglioFascicoloGenericoPresenter.InviaDaCsvCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.DettaglioFascicoloGenericoPresenter.RichiediFirmaCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.DettaglioFascicoloGenericoPresenter.RitiraTaskFirmaCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.DettaglioFascicoloGenericoPresenter.UploadAllegatoCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.DettaglioFascicoloGenericoPresenter.UploadZipCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command.CambiaStepIterCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command.CercaPgCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command.GoToAvviaProcedimentoCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command.GoToChiudiProcedimentoCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command.ModificaOperatoreCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command.MostraDettaglioComunicazioneCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command.SalvaFascicoloCommand;
import it.eng.portlet.consolepec.gwt.client.tasks.gestionefascicolo.operazioni.richiedifirma.RichiediFirmaTaskApiClient;
import it.eng.portlet.consolepec.gwt.client.view.OpenCloseImagePanel;
import it.eng.portlet.consolepec.gwt.client.view.fascicolo.composizione.PraticheInComposizioneVisitor;
import it.eng.portlet.consolepec.gwt.client.view.fascicolo.composizione.TabAllegati;
import it.eng.portlet.consolepec.gwt.client.view.fascicolo.composizione.TabComunicazioni;
import it.eng.portlet.consolepec.gwt.client.view.fascicolo.composizione.TabDirectory;
import it.eng.portlet.consolepec.gwt.client.view.fascicolo.composizione.TabProtocollazione;
import it.eng.portlet.consolepec.gwt.client.view.fascicolo.composizione.TabSelezionati;
import it.eng.portlet.consolepec.gwt.client.view.fascicolo.composizione.WidgetComposizioneFascicolo;
import it.eng.portlet.consolepec.gwt.client.view.fascicolo.composizione.WidgetComposizioneRicerca;
import it.eng.portlet.consolepec.gwt.client.widget.CatenaDocumentaleWidget;
import it.eng.portlet.consolepec.gwt.client.widget.CatenaDocumentaleWidget.SearchPGParams;
import it.eng.portlet.consolepec.gwt.client.widget.DownloadAllegatoWidget;
import it.eng.portlet.consolepec.gwt.client.widget.ElementoCatenaDocumentaleWidget;
import it.eng.portlet.consolepec.gwt.client.widget.ElementoFascicoloCollegato;
import it.eng.portlet.consolepec.gwt.client.widget.ElementoPraticaProcediCollegato;
import it.eng.portlet.consolepec.gwt.client.widget.ElementoProcedimentoElencoWidget;
import it.eng.portlet.consolepec.gwt.client.widget.ElementoProcedimentoElencoWidget.SelezioneProcedimento;
import it.eng.portlet.consolepec.gwt.client.widget.EventoIterFascicoloWidget;
import it.eng.portlet.consolepec.gwt.client.widget.MessageAlertWidget;
import it.eng.portlet.consolepec.gwt.client.widget.UploadAllegatoWidget;
import it.eng.portlet.consolepec.gwt.client.widget.UploadAllegatoWidget.UploadAllegatoHandler;
import it.eng.portlet.consolepec.gwt.client.widget.configurazioni.AutorizzazioniFascicoloWidget;
import it.eng.portlet.consolepec.gwt.client.widget.datiaggiuntivi.FormDatiAggiuntiviWidget;
import it.eng.portlet.consolepec.gwt.client.widget.images.ConsolePECIcons;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.UriMapping;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CambiaStatoFascicoloEnum;
import it.eng.portlet.consolepec.gwt.shared.action.pec.TipoRiferimentoPEC;
import it.eng.portlet.consolepec.gwt.shared.dto.CollegamentoDto;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.CatenaDocumentaleDTO;
import it.eng.portlet.consolepec.gwt.shared.model.CatenaDocumentaleDTO.PgDTO;
import it.eng.portlet.consolepec.gwt.shared.model.EventoIterDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoAllegato;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoElenco;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoGruppoProtocollato;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoGruppoProtocollatoCapofila;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoPECRiferimento;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoPraticaModulisticaRiferimento;
import it.eng.portlet.consolepec.gwt.shared.model.PecInDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PecOutDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO.TipoPresaInCarico;
import it.eng.portlet.consolepec.gwt.shared.model.ProcedimentoDto;
import it.eng.portlet.consolepec.gwt.shared.model.RispostaFileUploaderDTO;
import it.eng.portlet.consolepec.gwt.shared.model.ValidazioneDatoAggiuntivoDTO;
import it.eng.portlet.consolepec.gwt.shared.procedimenti.StatoProcedimento;

public class DettaglioFascicoloGenericoView extends ViewImpl implements DettaglioFascicoloGenericoPresenter.MyView {

	private static DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat(ConsoleConstants.FORMATO_DATA);

	private final Widget widget;

	@UiField
	HeadingElement titoloFascicolo;
	@UiField
	Label pgLabel;

	@UiField
	Label tipoDocumento;
	@UiField
	Label stato;
	@UiField
	Label stepIter;
	@UiField
	Label dataAggiornamentoIter;
	@UiField
	Label operatore;

	@UiField
	HTMLPanel titoloOriginalePanel;
	@UiField
	Label titoloOriginaleLabel;

	@UiField
	Label titoloLabel;
	@UiField
	Label numeroRepertorioLabel;
	@UiField
	Label utenteCreatoreLabel;
	@UiField
	Label dataCreazioneLabel;
	@UiField
	Label assegnatarioLabel;
	@UiField
	Label inCaricoALabel;
	@UiField
	TextArea noteTextArea;
	@UiField
	HTMLPanel destinatariInoltroLabelPanel;
	@UiField
	HTMLPanel destinatariInoltroPanel;
	@UiField
	HTMLPanel stepIterPanel;
	@UiField
	HTMLPanel operatorePanel;

	@UiField
	Button chiudiButton;
	@UiField
	Button riassegnaButton;
	@UiField
	Button protocollaButton;
	@UiField
	Button caricaAllegatoButton;
	@UiField
	Button caricaZipButton;
	@UiField
	Button firmaButton;
	@UiField
	Button salvaButton;
	@UiField
	Button annullaModificheButton;
	@UiField
	Button ricaricaButton;

	@UiField
	HTMLPanel elencoFascicoliCollegatiPanel;

	@UiField
	HTMLPanel elencoPraticheCollegatePanel;

	@UiField
	HRElement datiAggiuntiviFascicoloHR;
	@UiField
	HRElement elencoFascicoliCollegatiHR;
	@UiField
	HRElement procedimentiHR;

	@UiField
	HRElement catenaDocumentaleHR;

	@UiField(provided = true)
	ConsoleDisclosurePanel datiAggiuntiviFascicoloPanel = new ConsoleDisclosurePanel(OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelOpen(),
			OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelClosed(), "Dati Aggiuntivi");

	@UiField(provided = true)
	ConsoleDisclosurePanel autorizzazioniFascicoloPanel = new ConsoleDisclosurePanel(OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelOpen(),
			OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelClosed(), "Autorizzazioni fascicolo");

	@UiField(provided = true)
	WidgetComposizioneRicerca widgetComposizioneRicerca;
	@UiField(provided = true)
	WidgetComposizioneFascicolo widgetComposizioneFascicolo;

	@UiField(provided = true)
	ConsoleDisclosurePanel composizioneFascicoloPanel = new ConsoleDisclosurePanel(OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelOpen(), OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelClosed(),
			"Composizione fascicolo");

	@UiField(provided = true)
	ConsoleDisclosurePanel dettaglioFascicoloPanel = new ConsoleDisclosurePanel(OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelOpen(), OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelClosed(),
			"Dettaglio fascicolo");

	@UiField(provided = true)
	ConsoleDisclosurePanel fascicoliCollegatiPanel = new ConsoleDisclosurePanel(OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelOpen(), OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelClosed(),
			"Fascicoli collegati");

	@UiField(provided = true)
	ConsoleDisclosurePanel procedimentiPanel = new ConsoleDisclosurePanel(OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelOpen(), OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelClosed(),
			"Procedimenti");

	@UiField(provided = true)
	ConsoleDisclosurePanel catenaDocumentalePanel = new ConsoleDisclosurePanel(OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelOpen(), OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelClosed(),
			"Catena Documentale");

	@UiField(provided = true)
	ConsoleDisclosurePanel iterFascicoloPanel = new ConsoleDisclosurePanel(OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelOpen(), OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelClosed(),
			"Azioni");

	@UiField
	DownloadAllegatoWidget downloadWidget;
	@UiField
	UploadAllegatoWidget uploadWidget;
	@UiField
	UploadAllegatoWidget uploadZipWidget;

	@UiField(provided = true)
	MessageAlertWidget messageWidget;

	@UiField(provided = true)
	EventoIterFascicoloWidget eventiIter;

	@UiField
	HTMLPanel datiAggiuntiviPanel;

	@UiField
	HTMLPanel autorizzazioniSubPanel;

	@UiField
	LIElement newMail;
	@UiField
	LIElement newMailDaTemplate;
	@UiField
	LIElement newRispondiMail;
	@UiField
	LIElement newMailInteroperabile;
	@UiField
	LIElement newSeparaDaFascicolo;
	@UiField
	LIElement newArchivia;
	@UiField
	LIElement newElimina;
	@UiField
	LIElement newRiportaInGestione;
	@UiField
	LIElement newInAffissione;
	@UiField
	LIElement newConcludi;
	@UiField
	LIElement newPrendiInCarico;
	@UiField
	LIElement newRiportaInLettura;
	@UiField
	LIElement newAssegnaEsterno;
	@UiField
	LIElement newModificaAbilitazioni;
	@UiField
	LIElement newCollega;
	@UiField
	LIElement newModificaCollegamento;
	@UiField
	LIElement newEliminaCollegamento;
	@UiField
	LIElement newAvvioProcedimento;
	@UiField
	LIElement newChiudiProcedimento;
	@UiField
	LIElement newEliminaAllegato;
	@UiField
	LIElement newRiversamentoCartaceo;
	@UiField
	LIElement newImportaAllegati;
	@UiField
	LIElement newPubblicaAllegato;
	@UiField
	LIElement newRimuoviPubblicazioneAllegato;
	@UiField
	LIElement newVisibilitaAllegato;
	@UiField
	LIElement newRicevutaDiConsegna;
	@UiField
	LIElement newScaricaAllegati;
	@UiField
	LIElement newModificaOperatore;
	@UiField
	LIElement newPdfDaTemplate;
	@UiField
	LIElement newRichiediFirma;
	@UiField
	LIElement newRitiraTaskFirma;
	@UiField
	LIElement newEstraiEML;
	@UiField
	LIElement newInviaDaCSV;
	@UiField
	LIElement newAggiornaPG;
	@UiField
	LIElement newSpostaAllegati;
	@UiField
	LIElement newSpostaProtocollazioni;
	@UiField
	LIElement newModificaFascicolo;
	@UiField
	LIElement collegaPraticaProcedi;
	@UiField
	LIElement emissionePermesso;

	@UiField
	Button tipologieAllegatoButton;

	@UiField
	Element fildsetPulsantiera;
	@UiField
	UListElement listaStepIter;
	@UiField
	DivElement menuIter;

	@UiField
	ButtonElement buttonMail;
	@UiField
	ButtonElement buttonStato;
	@UiField
	ButtonElement buttonAltroDettaglio;
	@UiField
	ButtonElement buttonAltroComposizione;
	@UiField
	ButtonElement buttonIter;

	@UiField
	HTMLPanel bottoniComposizionePanel;

	private boolean reloadDettaglio = false;

	private Set<AllegatoDTO> allegatiNonProtSelezionati = new HashSet<AllegatoDTO>();
	private Set<AllegatoDTO> allegatiProtSelezionati = new HashSet<AllegatoDTO>();
	private Set<ElementoElenco> pecNonProtSelezionate = new HashSet<FascicoloDTO.ElementoElenco>();
	private Set<ElementoElenco> pecProtSelezionate = new HashSet<FascicoloDTO.ElementoElenco>();
	private Set<ElementoPraticaModulisticaRiferimento> modulisticheNonProtSelezionate = new HashSet<FascicoloDTO.ElementoPraticaModulisticaRiferimento>();
	private Set<ElementoPraticaModulisticaRiferimento> modulisticheProtSelezionate = new HashSet<FascicoloDTO.ElementoPraticaModulisticaRiferimento>();
	private final List<PecInDTO> emailConAllegati = new ArrayList<PecInDTO>();
	private List<ElementoPECRiferimento> pecOutConRicevute = new ArrayList<ElementoPECRiferimento>();

	private AutorizzazioniFascicoloWidget autorizzazioniFascicoloWidget;
	private FormDatiAggiuntiviWidget formDatiAggiuntiviWidget;

	private Set<ProcedimentoDto> procedimentiSelezionati = new HashSet<ProcedimentoDto>();
	private Set<CollegamentoDto> collegamentiSelezionati = new HashSet<CollegamentoDto>();
	private Set<PraticaProcedi> praticheProcediSelezionate = new HashSet<PraticaProcedi>();
	private List<PraticaProcedi> praticaProcedi = null;
	private List<ElementoCatenaDocumentaleWidget> elementiCatenaDocumentaleSelezionati = new ArrayList<ElementoCatenaDocumentaleWidget>();

	private final PecInPraticheDB pecInDb;
	private final PecApiClient pecApiClient;
	private UploadAllegatoCommand uploadAllegatoCommand;
	private UploadZipCommand uploadZipCommand;
	private CambiaStepIterCommand cambiaStepIterCommand;

	/**
	 * Dettaglio mantenuto localmente per avere un riferimento consistente con quello mostrato a video. Ogni volta che si aggiorna il contenuto da praticheDB, va aggiornato anche questo riferimento
	 */
	private FascicoloDTO pratica;
	private final EventBus eventBus;
	private GoToFascicoloCollegatoCommand goToFascicoloCollegatoCommand;
	private GoToPraticaProcediCollegatoCommand goToPraticaProcediCollegatoCommand;
	private DettaglioFascicoloCollegatoCommand dettaglioFascicoloCollegatoCommand;
	private DettaglioPraticaProcediCollegatoCommand dettaglioPraticaProcediCollegatoCommand;
	private List<String> composizionePraticheCollegateCaricate;
	private CatenaDocumentaleWidget catenaDocumentaleWidget;
	private final RichiediFirmaTaskApiClient richiediFirmaTask;
	private CatenaDocumentaleDTO catenaDocumentaleDTO;
	private ConfigurazioniHandler configurazioniHandler;
	private AnagraficaFascicolo anagraficaFascicolo;

	private ProfilazioneUtenteHandler profilazioneUtenteHandler;

	public static final String RIMUOVI_TIPOLIGIA = "Rimuovi Tipologia";

	public interface Binder extends UiBinder<Widget, DettaglioFascicoloGenericoView> {
		//
	}

	@Inject
	public DettaglioFascicoloGenericoView(final Binder binder, final PecInPraticheDB pecInDb, final EventBus eventBus, final RichiediFirmaTaskApiClient rfTask, PecApiClient pecApiCLient,
			ProfilazioneUtenteHandler profilazioneUtenteHandler, ConfigurazioniHandler configurazioniHandler, RicercaComposizioneFascicoloHandler ricercaHandler) {
		this.configurazioniHandler = configurazioniHandler;
		this.profilazioneUtenteHandler = profilazioneUtenteHandler;

		messageWidget = new MessageAlertWidget(eventBus);
		eventiIter = new EventoIterFascicoloWidget();

		widgetComposizioneRicerca = new WidgetComposizioneRicerca(ricercaHandler);
		widgetComposizioneFascicolo = new WidgetComposizioneFascicolo(eventBus, widgetComposizioneRicerca);

		widget = binder.createAndBindUi(this);
		this.pecInDb = pecInDb;
		this.eventBus = eventBus;

		buttonMail.appendChild(new Image(ConsolePECIcons._instance.triangolinoBianco()).getElement());
		buttonStato.appendChild(new Image(ConsolePECIcons._instance.triangolinoBianco()).getElement());
		buttonAltroDettaglio.appendChild(new Image(ConsolePECIcons._instance.triangolinoBianco()).getElement());
		buttonAltroComposizione.appendChild(new Image(ConsolePECIcons._instance.triangolinoBianco()).getElement());
		buttonIter.appendChild(new Image(ConsolePECIcons._instance.triangolinoBianco()).getElement());

		this.richiediFirmaTask = rfTask;
		this.pecApiClient = pecApiCLient;

		this.widgetComposizioneFascicolo.setView(this);
		this.widgetComposizioneFascicolo //
				.addTab(new TabProtocollazione(eventBus)) //
				.addTab(new TabComunicazioni(eventBus)) //
				.addTab(new TabAllegati(eventBus)) //
				.addTab(new TabSelezionati(eventBus)) //
				.addTab(new TabDirectory(eventBus));

		initCatenaDocumenatalePanel();
	}

	@Override
	public FascicoloDTO getFascicolo() {
		/* imposto dettaglio gui su dto */

		pratica.setNote(noteTextArea.getText());
		pratica.setValoriDatiAggiuntivi(formDatiAggiuntiviWidget.getDatiAggiuntivi());
		return pratica;
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	private static String sanitizeNull(String input) {
		if (input == null) {
			return "-";
		} else {
			return input;
		}
	}

	@Override
	public void mostraPratica(FascicoloDTO pratica) {

		this.anagraficaFascicolo = configurazioniHandler.getAnagraficaFascicolo(pratica.getTipologiaPratica().getNomeTipologia());

		boolean newPratica = this.pratica == null || !this.pratica.equals(pratica);

		this.pratica = pratica;

		/*
		 * Field autorizzazioni
		 */
		autorizzazioniFascicoloWidget.render(pratica);

		reset();

		impostaDettagli();

		impostaAbilitazioniPulsantiera();

		impostaIter();

		showHideMenuItem(newImportaAllegati, false);// disabilito il pulsante prima di far partire il visitor (dove in caso verrà riabilitato)

		/* popolo composizione */
		this.widgetComposizioneFascicolo.init(pratica);
		this.widgetComposizioneFascicolo.resetTab();

		// FascicoloVisitor visitor = new FascicoloVisitor(this, elencoPanel, pratica);
		// visitor.start();

		/* popola iter */
		popolaIter(pratica.getEventiIterDTO());

		/* recupera i collegamenti */
		recuperaCollegamenti(pratica.getCollegamenti());

		if (haValoriAggiuntiviVisibili(pratica)) {
			datiAggiuntiviFascicoloPanel.setVisible(true);
			formDatiAggiuntiviWidget.setDatiAggiuntiviPerDettaglio(pratica.getValoriDatiAggiuntivi());
			datiAggiuntiviFascicoloHR.getStyle().setDisplay(Display.BLOCK);
		} else {
			datiAggiuntiviFascicoloPanel.setVisible(false);
			datiAggiuntiviFascicoloHR.getStyle().setDisplay(Display.NONE);
		}

		if (pratica.getProcedimenti().size() > 0) {
			procedimentiPanel.setVisible(true);
			popolaProcedimenti(pratica.getProcedimenti());
			procedimentiHR.getStyle().setDisplay(Display.BLOCK);
		} else {
			procedimentiPanel.setVisible(false);
			procedimentiHR.getStyle().setDisplay(Display.NONE);
		}
		procedimentiPanel.setOpen(false);

		if (pratica.getAssegnazioneEsterna() != null && pratica.getAssegnazioneEsterna().getDestinatari().isEmpty() == false) {
			StringBuilder bld = new StringBuilder();
			Iterator<String> it = pratica.getAssegnazioneEsterna().getDestinatari().iterator();
			while (it.hasNext()) {
				bld.append(it.next() + (it.hasNext() ? "<br/>" : ""));
			}
			destinatariInoltroLabelPanel.clear();
			destinatariInoltroLabelPanel.add(new HTML(bld.toString()));
			destinatariInoltroPanel.setVisible(true);
		} else {
			destinatariInoltroLabelPanel.clear();
			destinatariInoltroPanel.setVisible(false);
		}

		if (newPratica) {
			catenaDocumentaleWidget.reset();
			this.catenaDocumentaleDTO = null;
		} else {
			if (this.catenaDocumentaleDTO != null) {
				showCatenaDocumentale(this.catenaDocumentaleDTO);
			}
		}

	}

	private void impostaIter() {
		List<StepIter> stepIterAbilitati = anagraficaFascicolo.getStepIterAbilitati();
		listaStepIter.removeAllChildren();

		if (stepIterAbilitati != null && !stepIterAbilitati.isEmpty()) {

			for (final StepIter step : stepIterAbilitati) {

				LIElement stepLi = LIElement.as(DOM.createElement("li"));

				SpanElement pgSpan = SpanElement.as(DOM.createSpan());
				pgSpan.setInnerHTML(step.getNome());
				stepLi.appendChild(pgSpan);

				listaStepIter.appendChild(stepLi);

				if (pratica.isCambiaStepIter()) {
					if (step.equals(pratica.getStepIter())) {
						showHideMenuItem(stepLi, false);
					} else {
						Event.sinkEvents(stepLi, Event.ONCLICK);
						Event.setEventListener(stepLi, new EventListener() {

							@Override
							public void onBrowserEvent(Event event) {
								if (Event.ONCLICK == event.getTypeInt()) {
									cambiaStepIterCommand.setStepIterDto(step);
									cambiaStepIterCommand.execute();
								}
							}
						});
					}

				} else {
					showHideMenuItem(stepLi, false);
				}

			}

			stepIterPanel.setVisible(true);
			menuIter.getStyle().setDisplay(Display.INLINE_BLOCK);

		} else {
			stepIterPanel.setVisible(false);
			menuIter.getStyle().setDisplay(Display.NONE);

		}
	}

	private static boolean haValoriAggiuntiviVisibili(FascicoloDTO pratica) {
		for (DatoAggiuntivo valore : pratica.getValoriDatiAggiuntivi()) {
			if (valore.isVisibile()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void mostraPraticaProcedi(List<PraticaProcedi> praticaProcedi) {

		HTMLPanel initPraticaProcedi = null;

		this.praticaProcedi = praticaProcedi;

		removePraticaProcedi();

		if (praticaProcedi != null && !praticaProcedi.isEmpty()) {
			initPraticaProcedi = initPraticaProcedi();
			mostraPraticheCollegamenti(initPraticaProcedi, praticaProcedi);
		}

		if ((pratica.getCollegamenti() == null || pratica.getCollegamenti().isEmpty()) && (praticaProcedi == null || praticaProcedi.isEmpty())) {
			elencoFascicoliCollegatiPanel.getElement().setInnerHTML("");
			elencoFascicoliCollegatiHR.getStyle().setDisplay(Display.NONE);
			fascicoliCollegatiPanel.setVisible(false);

		} else {
			elencoFascicoliCollegatiHR.getStyle().setDisplay(Display.BLOCK);
			fascicoliCollegatiPanel.setVisible(true);
		}

		impostaAbilitazioniPulsantiera();

	}

	private void recuperaCollegamenti(List<CollegamentoDto> fascicoliCondivisi) {
		final TreeSet<CollegamentoDto> collegamentiProt = new TreeSet<CollegamentoDto>();
		final TreeSet<CollegamentoDto> collegamentiNonProt = new TreeSet<CollegamentoDto>();
		collegamentiSelezionati = new HashSet<CollegamentoDto>();
		impostaAbilitazioniPulsantiera();

		elencoFascicoliCollegatiPanel.getElement().setInnerHTML("");
		fascicoliCollegatiPanel.setVisible(false);
		elencoFascicoliCollegatiHR.getStyle().setDisplay(Display.NONE);

		for (final CollegamentoDto coll : fascicoliCondivisi) {
			final String clientID = coll.getClientId();
			pecInDb.getFascicoloByPath(clientID, false, new PraticaFascicoloLoaded() {

				@Override
				public void onPraticaLoaded(FascicoloDTO fascicolo) {

					fascicoliCollegatiPanel.setVisible(true);
					elencoFascicoliCollegatiHR.getStyle().setDisplay(Display.BLOCK);

					// Recupero le pratiche in composizione delle pratiche collegate
					PraticheInComposizioneVisitor praticheInComposizioneVisitor = new PraticheInComposizioneVisitor(fascicolo.getElenco());

					CollegamentoDto tmp = new CollegamentoDto();
					tmp.setAnnoPg(fascicolo.getAnnoPG());
					tmp.setClientId(clientID);
					tmp.setNumeroPg(fascicolo.getNumeroPG());

					String oggetto = GenericsUtil.concatena(" - ", fascicolo.getTipologiaPratica().getEtichettaTipologia(), fascicolo.getTitolo(), fascicolo.getStato().getLabel());
					tmp.setOggetto(oggetto);

					tmp.getElencoPEC().addAll(praticheInComposizioneVisitor.getElencoPEC());
					tmp.getElencoPraticheModulistica().addAll(praticheInComposizioneVisitor.getElencoPraticheModulistica());
					tmp.getAllegati().addAll(fascicolo.getAllegati());

					tmp.setAccessibileInLettura(coll.isAccessibileInLettura() || fascicolo.isCollegamentoVisitabile());

					if (tmp.getAnnoPg() == null && tmp.getNumeroPg() == null) {
						collegamentiNonProt.add(tmp);
					} else {
						collegamentiProt.add(tmp);
					}

					if ((collegamentiNonProt.size() + collegamentiProt.size()) == pratica.getCollegamenti().size()) {

						if (collegamentiNonProt.size() != 0) {
							final HTMLPanel initNonProtocollati = initNonProtocollati();
							for (CollegamentoDto collegamento : collegamentiNonProt) {
								mostraCollegamentiNonProtocollati(initNonProtocollati, collegamento);
							}
						}

						if (collegamentiProt.size() != 0) {
							LIElement initProtocollati = initProtocollati();
							for (CollegamentoDto collegamento : collegamentiProt) {
								mostraCollegamentoProtocollato(initProtocollati, collegamento);
							}
						}

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
		mostraPraticaProcedi(praticaProcedi);

	}

	private LIElement initProtocollati() {
		elencoFascicoliCollegatiPanel.setStyleName("single");

		UListElement ulCapofila = UListElement.as(DOM.createElement("ul"));
		ulCapofila.setClassName("contenitore-lista-gruppi");
		elencoFascicoliCollegatiPanel.getElement().appendChild(ulCapofila);

		LIElement curLi = LIElement.as(DOM.createElement("li"));
		curLi.setClassName("gruppo clearfix");

		ulCapofila.appendChild(curLi);

		return curLi;
	}

	private void removePraticaProcedi() {
		elencoPraticheCollegatePanel.getElement().removeAllChildren();
		elencoPraticheCollegatePanel.setStyleName("");
	}

	private HTMLPanel initPraticaProcedi() {
		elencoPraticheCollegatePanel.setStyleName("single");

		UListElement ulNonProt = UListElement.as(DOM.createElement("ul"));
		ulNonProt.setClassName("contenitore-lista-gruppi");
		elencoPraticheCollegatePanel.getElement().appendChild(ulNonProt);

		LIElement curLi = LIElement.as(DOM.createElement("li"));
		curLi.setClassName("gruppo clearfix");

		ulNonProt.appendChild(curLi);

		SpanElement pgSpan = SpanElement.as(DOM.createSpan());
		pgSpan.setClassName("label nessun-protocollo");
		pgSpan.setInnerHTML("Pratiche Procedi");
		curLi.appendChild(pgSpan);
		/* div corpo */
		HTMLPanel corpoDIV = new HTMLPanel("");
		corpoDIV.setStylePrimaryName("corpo");
		elencoFascicoliCollegatiPanel.add(corpoDIV, curLi);

		HTMLPanel boxDIV = new HTMLPanel("");
		boxDIV.setStyleName("box-mail last");
		corpoDIV.add(boxDIV);

		return boxDIV;
	}

	private HTMLPanel initNonProtocollati() {

		elencoFascicoliCollegatiPanel.setStyleName("single");

		UListElement ulNonProt = UListElement.as(DOM.createElement("ul"));
		ulNonProt.setClassName("contenitore-lista-gruppi");
		elencoFascicoliCollegatiPanel.getElement().appendChild(ulNonProt);

		LIElement curLi = LIElement.as(DOM.createElement("li"));
		curLi.setClassName("gruppo clearfix");

		ulNonProt.appendChild(curLi);

		SpanElement pgSpan = SpanElement.as(DOM.createSpan());
		pgSpan.setClassName("label nessun-protocollo");
		pgSpan.setInnerHTML("Non protocollati");
		curLi.appendChild(pgSpan);
		/* div corpo */
		HTMLPanel corpoDIV = new HTMLPanel("");
		corpoDIV.setStylePrimaryName("corpo");
		elencoFascicoliCollegatiPanel.add(corpoDIV, curLi);

		HTMLPanel boxDIV = new HTMLPanel("");
		boxDIV.setStyleName("box-mail last");
		corpoDIV.add(boxDIV);

		return boxDIV;
	}

	private void mostraCollegamentoProtocollato(LIElement curLi, final CollegamentoDto tmp) {

		/* span PG */
		SpanElement pgSpan = SpanElement.as(DOM.createSpan());
		pgSpan.setClassName("label protocollo");
		pgSpan.setInnerText(tmp.getNumeroPg() + "/" + tmp.getAnnoPg());
		curLi.appendChild(pgSpan);

		/* div corpo */
		HTMLPanel corpoDIV = new HTMLPanel("");// DivElement.as(DOM.createElement("div"));
		corpoDIV.setStylePrimaryName("corpo");
		elencoFascicoliCollegatiPanel.add(corpoDIV, curLi);

		HTMLPanel boxDIV = new HTMLPanel("");
		boxDIV.setStyleName("box-mail last");
		corpoDIV.add(boxDIV);

		mostraCollegamentiNonProtocollati(boxDIV, tmp);

	}

	private void mostraPraticheCollegamenti(final HTMLPanel initPraticaProcedi, List<PraticaProcedi> p) {

		if (initPraticaProcedi != null) {

			for (final PraticaProcedi pratica : p) {

				ClickHandler checkBoxClickHandler = new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {

						boolean checked = ((CheckBox) event.getSource()).getValue();
						if (checked) {
							praticheProcediSelezionate.add(pratica);
						} else {
							praticheProcediSelezionate.remove(pratica);
						}

						impostaAbilitazioniPulsantiera();

					}
				};

				initPraticaProcedi.add(new ElementoPraticaProcediCollegato(pratica, goToPraticaProcediCollegatoCommand, dettaglioPraticaProcediCollegatoCommand, checkBoxClickHandler));

			}
		}
	}

	private void mostraCollegamentiNonProtocollati(final HTMLPanel initNonProtocollati, final CollegamentoDto c) {

		ClickHandler checkBoxClickHandler = new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				boolean checked = ((CheckBox) event.getSource()).getValue();
				if (checked) {
					collegamentiSelezionati.add(c);
				} else {
					collegamentiSelezionati.remove(c);
				}

				impostaAbilitazioniPulsantiera();

			}
		};

		initNonProtocollati.add(new ElementoFascicoloCollegato(c, goToFascicoloCollegatoCommand, dettaglioFascicoloCollegatoCommand, checkBoxClickHandler));

	}

	@Override
	public void setNascondiGruppiCommand(final Command mostraNascondiGruppiCommand) {
		this.riassegnaButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				mostraNascondiGruppiCommand.execute();

			}
		});

	}

	@Override
	public void setEliminaFascicoloCommand(final Command eliminaFascicoloCommand) {
		Event.sinkEvents(newElimina, Event.ONCLICK);
		Event.setEventListener(newElimina, new EventListener() {

			@Override
			public void onBrowserEvent(Event event) {
				if (Event.ONCLICK == event.getTypeInt()) {
					eliminaFascicoloCommand.execute();
				}
			}
		});
	}

	private void impostaDettagli() {
		String pg = null;
		if (pratica.getAnnoPG() == null || pratica.getNumeroPG() == null) {
			pg = sanitizeNull(null);
		} else {
			pg = pratica.getNumeroPG() + "/" + pratica.getAnnoPG();
		}

		if (pratica.getNumeroFascicolo() != null) {
			pg = pg + " (" + pratica.getNumeroFascicolo() + ")";
		}

		pgLabel.setText(pg);

		String tipoDocumentoName = pratica.getTipologiaPratica().getEtichettaTipologia();
		stato.setText(sanitizeNull(pratica.getStatoLabel()));
		tipoDocumento.setText(sanitizeNull(tipoDocumentoName));
		titoloLabel.setText(sanitizeNull(pratica.getTitolo()));

		if (Strings.isNullOrEmpty(pratica.getTitoloOriginale())) {
			titoloOriginalePanel.setVisible(false);
		} else {
			titoloOriginalePanel.setVisible(true);
			titoloOriginaleLabel.setText(pratica.getTitoloOriginale());
		}

		numeroRepertorioLabel.setText(sanitizeNull(pratica.getNumeroRepertorio()));
		utenteCreatoreLabel.setText(pratica.getUtenteCreazione() != null ? sanitizeNull(pratica.getUtenteCreazione()) : sanitizeNull(pratica.getUsernameCreazione()));
		dataCreazioneLabel.setText(sanitizeNull(pratica.getDataOraCreazione()));
		assegnatarioLabel.setText(sanitizeNull(pratica.getAssegnatario()));
		noteTextArea.setText(sanitizeNull(pratica.getNote()));
		if (pratica.getTipoPresaInCarico().equals(TipoPresaInCarico.NESSUNO)) {
			inCaricoALabel.setText(sanitizeNull(null));
		} else {
			inCaricoALabel.setText(pratica.getInCaricoALabel());
		}

		if (pratica.getStepIter() != null) {
			stepIter.setText(sanitizeNull(pratica.getStepIter().getNome()));
			dataAggiornamentoIter.setText(pratica.getStepIter().getDataAggiornamento() != null ? dateTimeFormat.format(pratica.getStepIter().getDataAggiornamento()) : "");
		} else {
			stepIter.setText(sanitizeNull(null));
			dataAggiornamentoIter.setText(sanitizeNull(null));
		}

		String operatoreValue = pratica.getOperatore();
		if (operatoreValue != null && !operatoreValue.trim().equals("")) {
			operatorePanel.setVisible(true);
			operatore.setText(sanitizeNull(operatoreValue));
		} else {
			operatore.setText(sanitizeNull(null));
			operatorePanel.setVisible(false);
		}

	}

	public void impostaAbilitazioniPulsantiera() {
		/* configurazione pulsantiera */

		if (pratica.isSganciaPecIn() && pecNonProtSelezionate.size() == 1 && allegatiNonProtSelezionati.isEmpty() && allegatiProtSelezionati.isEmpty() && pecProtSelezionate.isEmpty()
				&& modulisticheNonProtSelezionate.isEmpty() && modulisticheProtSelezionate.isEmpty() && procedimentiSelezionati.isEmpty() && collegamentiSelezionati.isEmpty()) {

			ElementoElenco pecElem = pecNonProtSelezionate.iterator().next();
			if (pecElem instanceof ElementoPECRiferimento) {
				ElementoPECRiferimento pec = (ElementoPECRiferimento) pecElem;
				if (pec.getTipo().equals(TipoRiferimentoPEC.IN) || pec.getTipo().equals(TipoRiferimentoPEC.EPROTO)) {
					showHideMenuItem(newSeparaDaFascicolo, true);
				} else {
					showHideMenuItem(newSeparaDaFascicolo, false);
				}
			} else {
				showHideMenuItem(newSeparaDaFascicolo, false);
			}
		} else {
			showHideMenuItem(newSeparaDaFascicolo, false);
		}

		showHideMenuItem(newMail, pratica.isCreaBozzaAbilitato());
		showHideMenuItem(newArchivia, pratica.isConcludi());
		showHideMenuItem(newMailInteroperabile, pratica.isRispondi());

		boolean checkProtocollazione = abilitazioneProtocollazione() && !isLocked(getAllegatiSelezionati());
		protocollaButton.setEnabled(checkProtocollazione);

		if (checkProtocollazione && !getPecNonProtSelezionate().isEmpty()) {
			pecApiClient.isProtocollazioneAbilitata(getPecNonProtSelezionate(), new BooleanCallback() {

				@Override
				public void onError(String error) {
					protocollaButton.setEnabled(false);
				}

				@Override
				public void onComplete(boolean result) {
					protocollaButton.setEnabled(result);
				}
			});
		}

		caricaAllegatoButton.setEnabled(pratica.isCaricaAllegato());
		caricaZipButton.setEnabled(pratica.isCaricaAllegato());
		showHideMenuItem(newInAffissione, pratica.isAffissioneAbilitato());

		boolean tipolabile = false;
		try {
			AnagraficaFascicolo af = configurazioniHandler.getAnagraficaFascicolo(pratica.getTipologiaPratica().getNomeTipologia());
			tipolabile = !getAllegatiSelezionati().isEmpty() && getPecSelezionate().isEmpty() && !af.getTipologieAllegato().isEmpty() && pratica.isModificaTagAbilitata();

			for (AllegatoDTO a : getAllegatiSelezionati()) {
				tipolabile &= !a.isProtocollato() && !a.isInTaskFirma() && !a.isLock().equals(Boolean.TRUE);
			}

		} catch (@SuppressWarnings("unused") Exception e) {
			// ~ sicuramente ci sono problemi piu' grossi se si spacca qua
		}

		tipologieAllegatoButton.setEnabled(tipolabile);

		/* firma abilitato sse il task è on e sono selezionati solo dei file */
		firmaButton.setEnabled(
				pratica.isFirmaAllegato() && allegatiNonProtSelezionati.size() > 0 && allegatiProtSelezionati.size() == 0 && getPecSelezionate().size() == 0 && !isLocked(getAllegatiSelezionati()));

		salvaButton.setEnabled(pratica.isSalvaNote());
		annullaModificheButton.setEnabled(salvaButton.isEnabled());

		/* cancella abilitato sse il task è on e sono selezionati solo dei file */
		boolean cancella = true;
		if (allegatiNonProtSelezionati.size() > 0) {
			for (AllegatoDTO allegato : allegatiNonProtSelezionati) {
				if (allegato.isPubblicato()) {
					cancella = false;
				}
			}
		}

		showHideMenuItem(newEliminaAllegato, pratica.isCancellaAllegato() && cancella && allegatiNonProtSelezionati.size() > 0 && allegatiProtSelezionati.size() == 0 && getPecSelezionate().size() == 0
				&& !isLocked(getAllegatiSelezionati()));

		showHideMenuItem(newElimina, pratica.isEliminaFascicoloAbilitato());
		showHideMenuItem(newRispondiMail, pratica.isRispondi() && abilitaRispondi(getPecSelezionate()));

		showHideMenuItem(newVisibilitaAllegato, pratica.isModificaVisibilitaAllegatoAbilitato() && allegatiNonProtSelezionati.size() == 1 && allegatiProtSelezionati.size() == 0
				&& getPecSelezionate().size() == 0 && !isLocked(allegatiNonProtSelezionati));

		showHideMenuItem(newRiportaInGestione, pratica.isRiportaInGestioneAbilitato());

		showHideMenuItem(newEliminaCollegamento, pratica.isCollegamentoAbilitato() && (collegamentiSelezionati.size() > 0 || praticheProcediSelezionate.size() > 0));
		showHideMenuItem(newModificaCollegamento, pratica.isCollegamentoAbilitato() && collegamentiSelezionati.size() > 0);

		showHideMenuItem(newAvvioProcedimento, pratica.isAvviaProcedimento() && isPulsanteAvviaProcedimentoAvviato() && !isLocked(getAllegatiSelezionati()));
		showHideMenuItem(newChiudiProcedimento, pratica.isChiudiProcedimento() && isPulsanteChiudiProcedimentoAvviato() && !isLocked(getAllegatiSelezionati()));

		showHideMenuItem(newPrendiInCarico, pratica.getGestionePresaInCarico());
		if (pratica.getTipoPresaInCarico().equals(TipoPresaInCarico.UTENTE_CORRENTE)) {
			newPrendiInCarico.setInnerText("Rilascia in carico");
		} else {
			newPrendiInCarico.setInnerText("Prendi in carico");
		}

		showHideMenuItem(newScaricaAllegati, getAllegatiSelezionati().size() > 1 && (getPraticheNonProtSelezionate().size() + getPraticheProtSelezionate().size() == 0));
		riassegnaButton.setEnabled(pratica.isRiassegna());
		if (getAllegatiSelezionati().size() == 1 && (getPraticheNonProtSelezionate().size() + getPraticheProtSelezionate().size() == 0)) {

			showHideMenuItem(newInviaDaCSV, inviaDaCsvAbilitato(pratica, getAllegatiSelezionati().iterator().next()));

			if (isAllegatoSelezionatoPubblicato(getAllegatiSelezionati().iterator().next().getNome())) {
				newPubblicaAllegato.setInnerText(ConsolePecConstants.MODIFICA_PUBBLICAZIONE);
				showHideMenuItem(newPubblicaAllegato, pratica.isPubblicazioneAbilitata() && !isLocked(getAllegatiSelezionati()));
				newRimuoviPubblicazioneAllegato.setInnerText(ConsolePecConstants.ELIMINA_PUBBLICAZIONE);
				showHideMenuItem(newRimuoviPubblicazioneAllegato, pratica.isRimozionePubblicazioneAbilitata() && !isLocked(getAllegatiSelezionati()));
			} else {
				newPubblicaAllegato.setInnerText(ConsolePecConstants.NUOVA_PUBBLICAZIONE);
				showHideMenuItem(newPubblicaAllegato, pratica.isPubblicazioneAbilitata() && !isLocked(getAllegatiSelezionati()));
				newRimuoviPubblicazioneAllegato.setInnerText(ConsolePecConstants.ELIMINA_PUBBLICAZIONE);
				showHideMenuItem(newRimuoviPubblicazioneAllegato, false);

			}
		} else {
			newPubblicaAllegato.setInnerText(ConsolePecConstants.NUOVA_PUBBLICAZIONE);
			showHideMenuItem(newPubblicaAllegato, false);
			newRimuoviPubblicazioneAllegato.setInnerText(ConsolePecConstants.ELIMINA_PUBBLICAZIONE);
			showHideMenuItem(newRimuoviPubblicazioneAllegato, false);
			showHideMenuItem(newInviaDaCSV, false);
		}

		showHideMenuItem(newCollega, pratica.isCollegamentoAbilitato());
		showHideMenuItem(newRiversamentoCartaceo, (elementiCatenaDocumentaleSelezionati.size() + getPraticheProtSelezionate().size() + allegatiProtSelezionati.size() == 1)
				&& (getPraticheNonProtSelezionate().size() + allegatiNonProtSelezionati.size() == 0));
		showHideMenuItem(newRiportaInLettura, pratica.isRiportaInLettura());
		showHideMenuItem(newAssegnaEsterno, pratica.isAssegnaUtenteEsterno());
		showHideMenuItem(newModificaAbilitazioni, pratica.isModificaAbilitazioniAssegnaUtenteEsterno());
		showHideMenuItem(newConcludi, pratica.isRitornaDaInoltrareEsterno());
		showHideMenuItem(newRicevutaDiConsegna, abilitazioneStampaRicevuteConsegna());
		showHideMenuItem(newMailDaTemplate, pratica.isNuovaEmailDaTemplate());
		showHideMenuItem(newPdfDaTemplate, pratica.isNuovoPdfDaTemplate());
		showHideMenuItem(newModificaOperatore, pratica.isModificaOperatore());
		showHideMenuItem(newRichiediFirma, this.richiediFirmaTask.isTaskInvocabile(pratica, getAllegatiSelezionati()) && (getPecSelezionate().size() == 0));
		showHideMenuItem(newRitiraTaskFirma, this.richiediFirmaTask.isTaskAnnullabile(pratica, getAllegatiSelezionati()) && (getPecSelezionate().size() == 0));
		showHideMenuItem(newEstraiEML, false);

		pecApiClient.isEstrazioneEMLAbilitata(getPecSelezionate(), new BooleanCallback() {

			@Override
			public void onError(String error) {
				showHideMenuItem(newEstraiEML, false);
			}

			@Override
			public void onComplete(boolean result) {
				showHideMenuItem(newEstraiEML, pratica.isEstraiEML() && result && (getAllegatiSelezionati().size() == 0));
			}
		});

		showHideMenuItem(newSpostaAllegati, pratica.isSpostaAllegati() //
				&& !getAllegatiNonProtSelezionati().isEmpty() //
				&& getAllegatiProtSelezionati().isEmpty() //
				&& getPraticheNonProtSelezionate().isEmpty() //
				&& getPraticheProtSelezionate().isEmpty() //
				&& !isLocked(getAllegatiNonProtSelezionati())); //

		showHideMenuItem(newSpostaProtocollazioni, pratica.isSpostaProtocollazioni() && selezioneProtocollazioniValidaPerSpostamento());
		showHideMenuItem(newAggiornaPG, pratica.isAggiornaPG());
		showHideMenuItem(newModificaFascicolo, pratica.isModificaFascicolo());
		showHideMenuItem(collegaPraticaProcedi, pratica.isCollegaPraticaProcedi());
		showHideMenuItem(emissionePermesso, pratica.isEmissionePermesso());

		ImpostaPulsantieraFascicoloGenericoEvent evt = new ImpostaPulsantieraFascicoloGenericoEvent();
		eventBus.fireEvent(evt);

		chiudiButton.setEnabled(pratica.isChiudiAbilitato());

		formDatiAggiuntiviWidget.setSaveEnabled(pratica.isModificaDatiAggiuntivi());

		ricaricaButton.setEnabled(true);

	}

	private static boolean inviaDaCsvAbilitato(FascicoloDTO fascicoloDTO, AllegatoDTO allegatoDTO) {
		return fascicoloDTO.isInviaDaCsvAbilitato() && (allegatoDTO.getNome().toLowerCase().endsWith(".csv") || allegatoDTO.getNome().toLowerCase().endsWith(".xls")
				|| allegatoDTO.getNome().toLowerCase().endsWith(".xlsx") || allegatoDTO.getNome().toLowerCase().endsWith(".ods"));
	}

	private boolean selezioneProtocollazioniValidaPerSpostamento() {

		if (!getAllegatiNonProtSelezionati().isEmpty() || !getPraticheNonProtSelezionate().isEmpty()) {
			return false;
		}

		if (getAllegatiProtSelezionati().isEmpty() && getPraticheProtSelezionate().isEmpty()) {
			return false;
		}

		if (isLocked(getAllegatiProtSelezionati())) {
			return false;
		}

		// valido la selezione del capofila
		Map<ElementoGruppoProtocollato, List<ElementoElenco>> map = new HashMap<ElementoGruppoProtocollato, List<ElementoElenco>>();

		for (ElementoElenco elementoElenco : pratica.getElenco()) {
			if (elementoElenco instanceof ElementoGruppoProtocollatoCapofila) {
				ElementoGruppoProtocollatoCapofila capofila = (ElementoGruppoProtocollatoCapofila) elementoElenco;

				for (ElementoElenco elementoDelCapofila : capofila.getElementi()) {

					if (elementoDelCapofila instanceof ElementoAllegato) {
						ElementoAllegato el = (ElementoAllegato) elementoDelCapofila;

						for (AllegatoDTO allegatoProtSelezionato : getAllegatiProtSelezionati()) {

							if (allegatoProtSelezionato.getNome().equals(el.getNome()) && allegatoProtSelezionato.getClientID().equals(el.getClientID())) {

								for (ProcedimentoDto procedimento : pratica.getProcedimenti()) {
									if (procedimento.getAnnoPG().toString().equals(capofila.getAnnoPG()) && procedimento.getNumeroPG().equals(capofila.getNumeroPG())) {
										return false;
									}
								}

								if (map.containsKey(capofila)) {
									map.get(capofila).add(el);

								} else {
									List<ElementoElenco> list = new ArrayList<ElementoElenco>();
									list.add(el);
									map.put(capofila, list);
								}
							}
						}

					} else if (elementoDelCapofila instanceof ElementoPECRiferimento) {
						ElementoPECRiferimento pecRiferimento = (ElementoPECRiferimento) elementoDelCapofila;

						for (ElementoElenco el : getPecProtSelezionate()) {
							ElementoPECRiferimento pec = (ElementoPECRiferimento) el;

							if (pec.getRiferimento().equals(pecRiferimento.getRiferimento())) {
								for (ProcedimentoDto procedimento : pratica.getProcedimenti()) {
									if (procedimento.getAnnoPG().toString().equals(capofila.getAnnoPG()) && procedimento.getNumeroPG().equals(capofila.getNumeroPG())) {
										return false;
									}
								}

								if (map.containsKey(capofila)) {
									map.get(capofila).add(pecRiferimento);

								} else {
									List<ElementoElenco> list = new ArrayList<ElementoElenco>();
									list.add(pecRiferimento);
									map.put(capofila, list);
								}
							}
						}

					} else if (elementoDelCapofila instanceof ElementoPraticaModulisticaRiferimento) {

						ElementoPraticaModulisticaRiferimento modRiferimento = (ElementoPraticaModulisticaRiferimento) elementoDelCapofila;

						for (ElementoElenco el : getModulisticheProtSelezionate()) {
							ElementoPraticaModulisticaRiferimento pec = (ElementoPraticaModulisticaRiferimento) el;

							if (pec.getRiferimento().equals(modRiferimento.getRiferimento())) {
								for (ProcedimentoDto procedimento : pratica.getProcedimenti()) {
									if (procedimento.getAnnoPG().toString().equals(capofila.getAnnoPG()) && procedimento.getNumeroPG().equals(capofila.getNumeroPG())) {
										return false;
									}
								}

								if (map.containsKey(capofila)) {
									map.get(capofila).add(modRiferimento);

								} else {
									List<ElementoElenco> list = new ArrayList<ElementoElenco>();
									list.add(modRiferimento);
									map.put(capofila, list);
								}
							}
						}
					}

				}
			}
		}

		for (Entry<ElementoGruppoProtocollato, List<ElementoElenco>> entry : map.entrySet()) {

			List<ElementoElenco> list = new ArrayList<ElementoElenco>();

			for (ElementoElenco el : entry.getKey().getElementi()) {
				if (el instanceof ElementoAllegato || el instanceof ElementoPECRiferimento || el instanceof ElementoPraticaModulisticaRiferimento) {
					list.add(el);
				}
			}

			if (list.size() != entry.getValue().size()) {
				return false;
			}
		}

		// Il controllo sull'effettiva presenza della protocollazione capofila sul fascicolo (e non sul suo riferimento) non lo effettuo
		// perchè non è possibile selezionare un capofila se non presente sul fascicolo
		return true;
	}

	private static boolean isLocked(Set<AllegatoDTO> allegati) {
		for (AllegatoDTO allegato : allegati) {
			if (Boolean.TRUE.equals(allegato.isLock())) {
				return true;
			}
		}
		return false;
	}

	private static LIElement showHideMenuItem(LIElement liElement, boolean visible) {
		if (visible) {
			liElement.removeClassName("dropdown-disabilitato");
			liElement.addClassName("dropdown-abilitato");
		} else {
			liElement.removeClassName("dropdown-abilitato");
			liElement.addClassName("dropdown-disabilitato");
		}
		return liElement;
	}

	private boolean abilitazioneStampaRicevuteConsegna() {
		if (allegatiNonProtSelezionati.size() + allegatiProtSelezionati.size() + modulisticheNonProtSelezionate.size() + modulisticheProtSelezionate.size() + procedimentiSelezionati.size()
				+ collegamentiSelezionati.size() != 0) {
			return false;
		}
		if (pecProtSelezionate.size() + pecNonProtSelezionate.size() != 1) {
			return false;
		}
		ElementoElenco selezionato = (pecProtSelezionate.size() == 1) ? pecProtSelezionate.iterator().next() : pecNonProtSelezionate.iterator().next();
		ElementoPECRiferimento pec = (ElementoPECRiferimento) selezionato;
		if (TipoRiferimentoPEC.OUT.equals(pec.getTipo())) {
			for (ElementoPECRiferimento elem : pecOutConRicevute) {
				if (pec.getRiferimento().equalsIgnoreCase(elem.getRiferimento())) {
					return true;
				}
			}
			return false;
		} else {
			return false;
		}
	}

	private boolean isAllegatoSelezionatoPubblicato(String nomeAllegato) {
		boolean pubblicato = false;
		if (getAllegatiSelezionati().size() == 1) {
			for (AllegatoDTO allegato : pratica.getAllegati()) {
				if (allegato.getNome().equals(nomeAllegato) && allegato.isPubblicato()) {
					pubblicato = true;
					break;
				}
			}
		}
		return pubblicato;
	}

	private boolean abilitazioneProtocollazione() {
		boolean abilitata = pratica.isProtocolla() //
				&& (pecProtSelezionate.size() + modulisticheProtSelezionate.size() + allegatiProtSelezionati.size() + procedimentiSelezionati.size() == 0) //
				&& (pecNonProtSelezionate.size() + allegatiNonProtSelezionati.size() + modulisticheNonProtSelezionate.size() > 0); //

		if (abilitata) {
			boolean pecInSelezionata = false;
			boolean pecOutSelezionata = false;
			boolean praticaModulisticaSelezionata = false;

			for (ElementoElenco pratica : getPraticheNonProtSelezionate()) {

				if (pratica instanceof ElementoPECRiferimento) {

					ElementoPECRiferimento pec = (ElementoPECRiferimento) pratica;

					if (pec.getTipo().equals(TipoRiferimentoPEC.IN) || pec.getTipo().equals(TipoRiferimentoPEC.EPROTO)) {
						pecInSelezionata = true;
					}

					if (pec.getTipo().equals(TipoRiferimentoPEC.OUT)) {
						pecOutSelezionata = true;
					}
				}

				if (pratica instanceof ElementoPraticaModulisticaRiferimento) {
					praticaModulisticaSelezionata = true;
				}

			}
			if (pecInSelezionata) {
				return (pecOutSelezionata || praticaModulisticaSelezionata) ? false : true;
			}

			if (pecOutSelezionata) {
				return (pecInSelezionata || praticaModulisticaSelezionata) ? false : true;
			}

			if (praticaModulisticaSelezionata) {
				return (pecInSelezionata || pecOutSelezionata) ? false : true;
			}

			return true;
		}

		return abilitata;
	}

	private static boolean abilitaRispondi(Set<ElementoElenco> pecSelezionate) {
		if (pecSelezionate.size() == 1) {
			ElementoElenco el = pecSelezionate.iterator().hasNext() ? pecSelezionate.iterator().next() : null;
			if (el instanceof ElementoPECRiferimento) {
				return ((ElementoPECRiferimento) el).getTipo().compareTo(TipoRiferimentoPEC.IN) == 0 ? true : false;
			} else {
				return false;
			}
		}
		return false;
	}

	@Override
	public void setSalvaDatiAggiuntiviCommand(final SalvaFascicoloCommand salvaFascicoloCommand) {
		formDatiAggiuntiviWidget.setSalvaFascicoloCommand(salvaFascicoloCommand);
	}

	@Override
	public void setSalvaNoteFascicoloCommand(final Command command) {
		salvaButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				command.execute();
			}
		});

	}

	@Override
	public String getNote() {
		return noteTextArea.getText();
	}

	@Override
	public void setAnnullaSalvaFascicoloCommand(final Command annullaSalvaFascicoloCommand) {
		annullaModificheButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				annullaSalvaFascicoloCommand.execute();
			}
		});
	}

	@Override
	public void setCambiaStepIterCommand(CambiaStepIterCommand cambiaStepIterCommand) {
		this.cambiaStepIterCommand = cambiaStepIterCommand;
	}

	@Override
	public void setModificaTipologieAllegatoCommand(final Command modificaTipologieAllegatoCommand) {
		this.tipologieAllegatoButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				modificaTipologieAllegatoCommand.execute();
			}
		});
	}

	@Override
	public void setChiudiDettaglioCommand(final Command chiudiDettaglioCommand) {
		chiudiButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				chiudiDettaglioCommand.execute();
			}
		});
	}

	@Override
	public void setUploadAllegatoCommand(UploadAllegatoCommand uploadAllegatoCommand) {
		this.uploadAllegatoCommand = uploadAllegatoCommand;
		caricaAllegatoButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				uploadWidget.sfoglia(UriMapping.generaUploadAllegatoServletContextPath());

			}
		});
		uploadWidget.setUploadAllegatoHandler(new UploadAllegatoHandler() {

			@Override
			public void onUploadDone(RispostaFileUploaderDTO dto) {
				DettaglioFascicoloGenericoView.this.uploadAllegatoCommand.onFileUploaded(dto);
			}

			@Override
			public void onFileSelected(String fileName) {
				DettaglioFascicoloGenericoView.this.uploadAllegatoCommand.onFileSelected(fileName);
			}

			@Override
			public boolean onSubmitUpload(Integer fileNumber, String[] fileNames, Long[] fileLength) {
				return true;
			}
		});
	}

	@Override
	public void setUploadZipCommand(UploadZipCommand uploadZipCommand) {
		this.uploadZipCommand = uploadZipCommand;
		caricaZipButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (Window.confirm(ConsolePecConstants.WARN_CARICAMENTO_ZIP)) {
					widgetComposizioneRicerca.reset();
					uploadZipWidget.sfoglia(UriMapping.generaUploadAllegatoServletContextPath());
				}
			}
		});

		uploadZipWidget.setUploadAllegatoHandler(new UploadAllegatoHandler() {
			@Override
			public void onUploadDone(RispostaFileUploaderDTO dto) {
				DettaglioFascicoloGenericoView.this.uploadZipCommand.onFileUploaded(dto);
			}

			@Override
			public void onFileSelected(String fileName) {
				DettaglioFascicoloGenericoView.this.uploadZipCommand.onFileSelected();
			}

			@Override
			public boolean onSubmitUpload(Integer fileNumber, String[] fileNames, Long[] fileLength) {
				return DettaglioFascicoloGenericoView.this.uploadZipCommand.onSubmitUpload(fileNumber, fileNames, fileLength);
			}
		});
	}

	@Override
	public void setProtocollaCommand(final Command protocollaCommand) {
		protocollaButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				protocollaCommand.execute();
			}
		});
	}

	private boolean isPulsanteChiudiProcedimentoAvviato() {
		if (procedimentiSelezionati.size() == 1 && isAvviato(procedimentiSelezionati.iterator().next())
				&& allegatiProtSelezionati.size() + pecProtSelezionate.size() + modulisticheProtSelezionate.size() == 0
				&& allegatiNonProtSelezionati.size() + pecNonProtSelezionate.size() + modulisticheNonProtSelezionate.size() == 0) {
			return true;
		} else if (procedimentiSelezionati.size() == 0 && allegatiProtSelezionati.size() + pecProtSelezionate.size() + modulisticheProtSelezionate.size() == 1
				&& allegatiNonProtSelezionati.size() + pecNonProtSelezionate.size() + modulisticheNonProtSelezionate.size() == 0) {
			ElementoGruppoProtocollato nonCapofila = getNonCapofilaSelezionato();
			if (nonCapofila != null) {
				// for (ProcedimentoDto dto : pratica.getProcedimenti())
				// if( isAvviato(dto) && dto.getAnnoPG().toString().equals(nonCapofila.getAnnoPGCapofila()) && dto.getNumeroPG().equals(nonCapofila.getNumeroPGCapofila()) )
				return true;
			}
		}
		return false;
	}

	private static boolean isAvviato(ProcedimentoDto dto) {
		return dto.getStato().equals(StatoProcedimento.AVVIATO) ? true : false;
	}

	private boolean isPulsanteAvviaProcedimentoAvviato() {
		ElementoGruppoProtocollatoCapofila capofilaSelezionato = getCapofilaSelezionato();
		if (capofilaSelezionato != null) {
			for (ProcedimentoDto procedimentoDto : pratica.getProcedimenti()) {
				if (procedimentoDto.getAnnoPG().toString().equals(capofilaSelezionato.getAnnoPG()) && procedimentoDto.getNumeroPG().equals(capofilaSelezionato.getNumeroPG())) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public ElementoGruppoProtocollato getNonCapofilaSelezionato() {
		if ((allegatiProtSelezionati.size() + pecProtSelezionate.size() + modulisticheProtSelezionate.size() == 1) && procedimentiSelezionati.size() == 0) {
			for (ElementoElenco elementoElenco : pratica.getElenco()) {
				// controllo i capofila
				if (elementoElenco instanceof ElementoGruppoProtocollatoCapofila) {
					ElementoGruppoProtocollatoCapofila capofila = (ElementoGruppoProtocollatoCapofila) elementoElenco;
					for (ElementoElenco elementoDelCapofila : capofila.getElementi()) {
						// controllo le protocollazioni collegate
						if (elementoDelCapofila instanceof ElementoGruppoProtocollato) {
							ElementoGruppoProtocollato nonCapofila = (ElementoGruppoProtocollato) elementoDelCapofila;
							// controllo gli elementi della protocollazione collegata
							for (ElementoElenco elementoDelNonCapofila : nonCapofila.getElementi()) {
								if (allegatiProtSelezionati.size() == 1 && elementoDelNonCapofila instanceof ElementoAllegato) {
									String nome = allegatiProtSelezionati.iterator().next().getNome();
									String clientID = allegatiProtSelezionati.iterator().next().getClientID();
									ElementoAllegato ea = (ElementoAllegato) elementoDelNonCapofila;
									String clientID2 = ea.getClientID();
									String nome2 = ea.getNome();
									if (nome.equals(nome2) && clientID.equals(clientID2)) {
										return nonCapofila;
									}
								} else if (pecProtSelezionate.size() == 1 && elementoDelNonCapofila instanceof ElementoPECRiferimento) {
									ElementoPECRiferimento ep = (ElementoPECRiferimento) elementoDelNonCapofila;
									String riferimento = ep.getRiferimento();
									String riferimento2 = ((ElementoPECRiferimento) pecProtSelezionate.iterator().next()).getRiferimento();
									if (riferimento.equals(riferimento2)) {
										return nonCapofila;
									}
								} else if (modulisticheProtSelezionate.size() == 1 && elementoDelNonCapofila instanceof ElementoPraticaModulisticaRiferimento) {
									ElementoPraticaModulisticaRiferimento ep = (ElementoPraticaModulisticaRiferimento) elementoDelNonCapofila;
									String riferimento = ep.getRiferimento();
									String riferimento2 = modulisticheProtSelezionate.iterator().next().getRiferimento();
									if (riferimento.equals(riferimento2)) {
										return nonCapofila;
									}
								}
							}
						}
					}
				}
			}
		}
		return null;
	}

	@Override
	public ElementoGruppoProtocollatoCapofila getCapofilaSelezionato() {
		if ((allegatiProtSelezionati.size() + pecProtSelezionate.size() + modulisticheProtSelezionate.size() == 1) && procedimentiSelezionati.size() == 0) {

			if (allegatiProtSelezionati.size() == 1) {
				for (ElementoElenco elementoElenco : pratica.getElenco()) {

					if (elementoElenco instanceof ElementoGruppoProtocollatoCapofila) {
						ElementoGruppoProtocollatoCapofila capofila = (ElementoGruppoProtocollatoCapofila) elementoElenco;

						for (ElementoElenco elementoDelCapofila : capofila.getElementi()) {
							if (elementoDelCapofila instanceof ElementoAllegato) {

								String nome = allegatiProtSelezionati.iterator().next().getNome();
								String clientID = allegatiProtSelezionati.iterator().next().getClientID();

								ElementoAllegato ea = (ElementoAllegato) elementoDelCapofila;
								String clientID2 = ea.getClientID();
								String nome2 = ea.getNome();

								if (nome.equals(nome2) && clientID.equals(clientID2)) {
									return capofila;
								}
							}
						}
					}

				}
			}

			if (pecProtSelezionate.size() == 1) {

				for (ElementoElenco elementoElenco : pratica.getElenco()) {

					if (elementoElenco instanceof ElementoGruppoProtocollatoCapofila) {
						ElementoGruppoProtocollatoCapofila capofila = (ElementoGruppoProtocollatoCapofila) elementoElenco;

						for (ElementoElenco elementoDelCapofila : capofila.getElementi()) {

							if (elementoDelCapofila instanceof ElementoPECRiferimento) {
								ElementoPECRiferimento ep = (ElementoPECRiferimento) elementoDelCapofila;
								String riferimento = ep.getRiferimento();
								String riferimento2 = ((ElementoPECRiferimento) pecProtSelezionate.iterator().next()).getRiferimento();

								if (riferimento.equals(riferimento2)) {
									return capofila;
								}
							}
						}
					}
				}
			}

			if (modulisticheProtSelezionate.size() == 1) {

				for (ElementoElenco elementoElenco : pratica.getElenco()) {

					if (elementoElenco instanceof ElementoGruppoProtocollatoCapofila) {
						ElementoGruppoProtocollatoCapofila capofila = (ElementoGruppoProtocollatoCapofila) elementoElenco;

						for (ElementoElenco elementoDelCapofila : capofila.getElementi()) {

							if (elementoDelCapofila instanceof ElementoPraticaModulisticaRiferimento) {
								ElementoPraticaModulisticaRiferimento ep = (ElementoPraticaModulisticaRiferimento) elementoDelCapofila;
								String riferimento = ep.getRiferimento();
								String riferimento2 = modulisticheProtSelezionate.iterator().next().getRiferimento();

								if (riferimento.equals(riferimento2)) {
									return capofila;
								}
							}
						}
					}
				}
			}

		}
		return null;
	}

	@Override
	public void startUpload() {
		uploadWidget.startUpload();
	}

	@Override
	public void startZipUpload() {
		uploadZipWidget.startUpload();
	}

	@Override
	public Set<AllegatoDTO> getAllegatiSelezionati() {
		Set<AllegatoDTO> allegatiSelezionati = new HashSet<AllegatoDTO>();
		allegatiSelezionati.addAll(allegatiNonProtSelezionati);
		allegatiSelezionati.addAll(allegatiProtSelezionati);
		return allegatiSelezionati;
	}

	@Override
	public Set<ElementoElenco> getPraticheNonProtSelezionate() {
		Set<ElementoElenco> praticheNonProtocollateSelezionate = new HashSet<ElementoElenco>();
		praticheNonProtocollateSelezionate.addAll(pecNonProtSelezionate);
		praticheNonProtocollateSelezionate.addAll(modulisticheNonProtSelezionate);
		return praticheNonProtocollateSelezionate;
	}

	@Override
	public void setEliminaAllegatoCommand(final Command eliminaAllegatoCommand) {
		Event.sinkEvents(newEliminaAllegato, Event.ONCLICK);
		Event.setEventListener(newEliminaAllegato, new EventListener() {

			@Override
			public void onBrowserEvent(Event event) {
				if (Event.ONCLICK == event.getTypeInt()) {
					widgetComposizioneFascicolo.getElementiSelezionati().clear();
					eliminaAllegatoCommand.execute();
				}
			}
		});
	}

	@Override
	public void setScaricaAllegatiCommand(final Command scaricaAllegatiCommand) {
		Event.sinkEvents(newScaricaAllegati, Event.ONCLICK);
		Event.setEventListener(newScaricaAllegati, new EventListener() {

			@Override
			public void onBrowserEvent(Event event) {
				if (Event.ONCLICK == event.getTypeInt()) {
					scaricaAllegatiCommand.execute();
				}
			}
		});
	}

	@Override
	public void setCambioStatoArchiviaCommand(final it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, CambiaStatoFascicoloEnum> cambioStatoCommand) {
		Event.sinkEvents(newArchivia, Event.ONCLICK);
		Event.setEventListener(newArchivia, new EventListener() {

			@Override
			public void onBrowserEvent(Event event) {
				if (Event.ONCLICK == event.getTypeInt()) {
					cambioStatoCommand.exe(CambiaStatoFascicoloEnum.ARCHIVIA);
				}
			}
		});
	}

	@Override
	public void setCambioStatoInAffissioneCommand(final it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, CambiaStatoFascicoloEnum> cambioStatoCommand) {
		Event.sinkEvents(newInAffissione, Event.ONCLICK);
		Event.setEventListener(newInAffissione, new EventListener() {

			@Override
			public void onBrowserEvent(Event event) {
				if (Event.ONCLICK == event.getTypeInt()) {
					cambioStatoCommand.exe(CambiaStatoFascicoloEnum.IN_AFFISSIONE);
				}
			}
		});
	}

	@Override
	public void setCambioStatoInGestioneCommand(final it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, CambiaStatoFascicoloEnum> cambioStatoCommand) {
		Event.sinkEvents(newRiportaInGestione, Event.ONCLICK);
		Event.setEventListener(newRiportaInGestione, new EventListener() {

			@Override
			public void onBrowserEvent(Event event) {
				if (Event.ONCLICK == event.getTypeInt()) {
					cambioStatoCommand.exe(CambiaStatoFascicoloEnum.RIPORTAINGESTIONE);
				}
			}
		});
	}

	@Override
	public void setFirmaAllegatoCommand(final Command firmaAllegatoCommand) {
		firmaButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				firmaAllegatoCommand.execute();
			}
		});
	}

	@Override
	public void setNuovaMailDaTemplateCommand(final Command nuovaMailCommand) {
		Event.sinkEvents(newMailDaTemplate, Event.ONCLICK);
		Event.setEventListener(newMailDaTemplate, new EventListener() {

			@Override
			public void onBrowserEvent(Event event) {
				if (Event.ONCLICK == event.getTypeInt()) {
					setReloadDettaglio(true);
					nuovaMailCommand.execute();
				}
			}
		});
	}

	@Override
	public void setNuovPdfDaTemplateCommand(final Command nuovoPdfCommand) {
		Event.sinkEvents(newPdfDaTemplate, Event.ONCLICK);
		Event.setEventListener(newPdfDaTemplate, new EventListener() {

			@Override
			public void onBrowserEvent(Event event) {
				if (Event.ONCLICK == event.getTypeInt()) {
					nuovoPdfCommand.execute();
				}
			}
		});
	}

	@Override
	public void setRispondiMailCommand(final Command rispondiMailCommand) {
		Event.sinkEvents(newRispondiMail, Event.ONCLICK);
		Event.setEventListener(newRispondiMail, new EventListener() {

			@Override
			public void onBrowserEvent(Event event) {
				if (Event.ONCLICK == event.getTypeInt()) {
					rispondiMailCommand.execute();
				}
			}
		});
	}

	@Override
	public void setAggiornaPGCommand(final Command aggiornaPGCommand) {
		Event.sinkEvents(newAggiornaPG, Event.ONCLICK);
		Event.setEventListener(newAggiornaPG, new EventListener() {

			@Override
			public void onBrowserEvent(Event event) {
				if (Event.ONCLICK == event.getTypeInt()) {
					aggiornaPGCommand.execute();
				}
			}
		});
	}

	@Override
	public void setModificaFascicoloCommand(final Command modificaFascicoloCommand) {
		Event.sinkEvents(newModificaFascicolo, Event.ONCLICK);
		Event.setEventListener(newModificaFascicolo, new EventListener() {
			@Override
			public void onBrowserEvent(Event event) {
				if (Event.ONCLICK == event.getTypeInt()) {
					modificaFascicoloCommand.execute();
				}
			}
		});
	}

	@Override
	public void setCollegaPraticaProcediCommand(final Command collegaPraticaProcediCommand) {
		Event.sinkEvents(collegaPraticaProcedi, Event.ONCLICK);
		Event.setEventListener(collegaPraticaProcedi, new EventListener() {
			@Override
			public void onBrowserEvent(Event event) {
				if (Event.ONCLICK == event.getTypeInt()) {
					setReloadDettaglio(true);
					collegaPraticaProcediCommand.execute();
				}
			}
		});
	}

	@Override
	public String getIdMailSelezionata() {
		Set<ElementoElenco> pec = getPecSelezionate();
		ElementoElenco e = pec.iterator().hasNext() ? pec.iterator().next() : null;
		return (e != null && (e instanceof ElementoPECRiferimento)) ? ((ElementoPECRiferimento) e).getRiferimento() : null;
	}

	@Override
	public Set<ElementoElenco> getPecSelezionate() {
		Set<ElementoElenco> pecSelezionate = new HashSet<ElementoElenco>();
		pecSelezionate.addAll(pecProtSelezionate);
		pecSelezionate.addAll(pecNonProtSelezionate);
		return pecSelezionate;
	}

	@Override
	public void sendDownload(SafeUri uri) {
		downloadWidget.sendDownload(uri);
	}

	@Override
	public Set<ElementoElenco> getPraticheProtSelezionate() {
		Set<ElementoElenco> _praticheProtSelezionate = new HashSet<FascicoloDTO.ElementoElenco>();
		_praticheProtSelezionate.addAll(pecProtSelezionate);
		_praticheProtSelezionate.addAll(modulisticheProtSelezionate);
		return _praticheProtSelezionate;

	}

	@Override
	public void setGestioneInCaricoCommand(final Command gestioneInCaricoCommand) {
		Event.sinkEvents(newPrendiInCarico, Event.ONCLICK);
		Event.setEventListener(newPrendiInCarico, new EventListener() {

			@Override
			public void onBrowserEvent(Event event) {
				if (Event.ONCLICK == event.getTypeInt()) {
					gestioneInCaricoCommand.execute();
				}
			}
		});
	}

	@Override
	public void setNuovaMailCommand(final Command nuovaMailCommand) {
		Event.sinkEvents(newMail, Event.ONCLICK);
		Event.setEventListener(newMail, new EventListener() {

			@Override
			public void onBrowserEvent(Event event) {
				if (Event.ONCLICK == event.getTypeInt()) {
					setReloadDettaglio(true);
					nuovaMailCommand.execute();
				}
			}
		});

	}

	@Override
	public void setNuovaMailInteroperabileCommand(final Command nuovaMailCommand) {
		Event.sinkEvents(newMailInteroperabile, Event.ONCLICK);
		Event.setEventListener(newMailInteroperabile, new EventListener() {

			@Override
			public void onBrowserEvent(Event event) {
				if (Event.ONCLICK == event.getTypeInt()) {
					setReloadDettaglio(true);
					nuovaMailCommand.execute();
				}
			}
		});

	}

	@Override
	public void reset() {
		allegatiNonProtSelezionati = new HashSet<AllegatoDTO>();
		allegatiProtSelezionati = new HashSet<AllegatoDTO>();
		pecNonProtSelezionate = new HashSet<FascicoloDTO.ElementoElenco>();
		pecProtSelezionate = new HashSet<FascicoloDTO.ElementoElenco>();
		modulisticheProtSelezionate = new HashSet<FascicoloDTO.ElementoPraticaModulisticaRiferimento>();
		modulisticheNonProtSelezionate = new HashSet<FascicoloDTO.ElementoPraticaModulisticaRiferimento>();
		procedimentiSelezionati = new HashSet<ProcedimentoDto>();
		pecOutConRicevute = new ArrayList<ElementoPECRiferimento>();
		composizionePraticheCollegateCaricate = new ArrayList<String>();
		elementiCatenaDocumentaleSelezionati = new ArrayList<ElementoCatenaDocumentaleWidget>();
	}

	private void popolaIter(List<EventoIterDTO> eventiIterDTO) {
		eventiIter.initWidget(eventiIterDTO);
	}

	@Override
	public void resetDisclosurePanels(boolean showActions) {
		if (showActions) {
			this.datiAggiuntiviFascicoloPanel.setOpen(false);
			this.dettaglioFascicoloPanel.setOpen(false);
			this.composizioneFascicoloPanel.setOpen(false);
			this.fascicoliCollegatiPanel.setOpen(false);
			this.iterFascicoloPanel.setOpen(true);
			this.procedimentiPanel.setOpen(false);
			this.catenaDocumentalePanel.setOpen(false);
			this.autorizzazioniFascicoloPanel.setOpen(false);
		} else {
			this.dettaglioFascicoloPanel.setOpen(true);
			this.composizioneFascicoloPanel.setOpen(false);
			this.iterFascicoloPanel.setOpen(false);
			this.datiAggiuntiviFascicoloPanel.setOpen(false);
			this.fascicoliCollegatiPanel.setOpen(false);
			this.procedimentiPanel.setOpen(false);
			this.catenaDocumentalePanel.setOpen(false);
			this.autorizzazioniFascicoloPanel.setOpen(false);
		}
	}

	@Override
	public List<PecInDTO> getEmailConAllegati() {
		return emailConAllegati;
	}

	@Override
	public void setImportaAllegatiCommand(final Command importaAllegatiCommand) {
		Event.sinkEvents(newImportaAllegati, Event.ONCLICK);
		Event.setEventListener(newImportaAllegati, new EventListener() {

			@Override
			public void onBrowserEvent(Event event) {
				if (Event.ONCLICK == event.getTypeInt()) {
					importaAllegatiCommand.execute();
				}
			}
		});
	}

	@Override
	public void mostraPulsantiera(boolean mostra) {
		fildsetPulsantiera.getStyle().setDisplay(mostra ? Display.BLOCK : Display.NONE);
	}

	@Override
	public void mostraTitolo(boolean mostra) {
		titoloFascicolo.setInnerText(mostra ? "Dettaglio " + pratica.getTipologiaPratica().getEtichettaTipologia() : "");
	}

	@Override
	public void setPubblicaAllegatiCommand(final Command pubblicaAllegatiCommand) {
		Event.sinkEvents(newPubblicaAllegato, Event.ONCLICK);
		Event.setEventListener(newPubblicaAllegato, new EventListener() {

			@Override
			public void onBrowserEvent(Event event) {
				if (Event.ONCLICK == event.getTypeInt()) {
					pubblicaAllegatiCommand.execute();
				}
			}
		});
	}

	@Override
	public void setModificaVisibilitaAllegatiCommand(final Command pubblicaAllegatiCommand) {
		Event.sinkEvents(newVisibilitaAllegato, Event.ONCLICK);
		Event.setEventListener(newVisibilitaAllegato, new EventListener() {

			@Override
			public void onBrowserEvent(Event event) {
				if (Event.ONCLICK == event.getTypeInt()) {
					pubblicaAllegatiCommand.execute();
				}
			}
		});
	}

	@Override
	public void setRimuoviPubblicazioneAllegatiCommand(final Command rimuoviPubblicazioneAllegatiCommand) {
		Event.sinkEvents(newRimuoviPubblicazioneAllegato, Event.ONCLICK);
		Event.setEventListener(newRimuoviPubblicazioneAllegato, new EventListener() {

			@Override
			public void onBrowserEvent(Event event) {
				if (Event.ONCLICK == event.getTypeInt()) {
					rimuoviPubblicazioneAllegatiCommand.execute();
				}
			}
		});
	}

	@Override
	public void setCollegaFascicoloCommand(final Command collegaFascicoloCommand) {
		Event.sinkEvents(newCollega, Event.ONCLICK);
		Event.setEventListener(newCollega, new EventListener() {

			@Override
			public void onBrowserEvent(Event event) {
				if (Event.ONCLICK == event.getTypeInt()) {
					collegaFascicoloCommand.execute();
				}
			}
		});
	}

	@Override
	public void setGoToFascicoloCollegatoCommand(GoToFascicoloCollegatoCommand goToFascicoloCollegatoCommand) {
		this.goToFascicoloCollegatoCommand = goToFascicoloCollegatoCommand;
	}

	@Override
	public void setGoToPraticaProcediCollegatoCommand(GoToPraticaProcediCollegatoCommand goToPraticaProcediCollegatoCommand) {
		this.goToPraticaProcediCollegatoCommand = goToPraticaProcediCollegatoCommand;
	}

	@Override
	public Set<CollegamentoDto> getCollegamentiSelezionati() {
		return collegamentiSelezionati;
	}

	@Override
	public void setEliminaCollegamentiSelezionatiCommand(final Command eliminaCollegamentiSelezionatiCommand) {
		Event.sinkEvents(newEliminaCollegamento, Event.ONCLICK);
		Event.setEventListener(newEliminaCollegamento, new EventListener() {

			@Override
			public void onBrowserEvent(Event event) {
				if (Event.ONCLICK == event.getTypeInt()) {
					eliminaCollegamentiSelezionatiCommand.execute();
				}
			}
		});
	}

	@Override
	public void setModificaCollegamentiSelezionatiCommand(final Command modificaCollegamentiSelezionatiCommand) {
		Event.sinkEvents(newModificaCollegamento, Event.ONCLICK);
		Event.setEventListener(newModificaCollegamento, new EventListener() {

			@Override
			public void onBrowserEvent(Event event) {
				if (Event.ONCLICK == event.getTypeInt()) {
					modificaCollegamentiSelezionatiCommand.execute();
				}
			}
		});
	}

	@Override
	public void setMostraDettaglioComunicazioneCommand(MostraDettaglioComunicazioneCommand mostraDettaglioComunicazioneCommand) {
		// this.mostraDettaglioComunicazioneCommand = mostraDettaglioComunicazioneCommand;
	}

	@Override
	public void setAvviaProcedimentoCommand(final GoToAvviaProcedimentoCommand goToAvviaProcedimentoCommand) {
		Event.sinkEvents(newAvvioProcedimento, Event.ONCLICK);
		Event.setEventListener(newAvvioProcedimento, new EventListener() {

			@Override
			public void onBrowserEvent(Event event) {
				if (Event.ONCLICK == event.getTypeInt()) {
					goToAvviaProcedimentoCommand.execute();
				}
			}
		});
	}

	@Override
	public void setChiudiProcedimentoCommand(final GoToChiudiProcedimentoCommand goToChiudiProcedimentoCommand) {
		Event.sinkEvents(newChiudiProcedimento, Event.ONCLICK);
		Event.setEventListener(newChiudiProcedimento, new EventListener() {

			@Override
			public void onBrowserEvent(Event event) {
				if (Event.ONCLICK == event.getTypeInt()) {
					goToChiudiProcedimentoCommand.execute();
				}
			}
		});

	}

	public void popolaProcedimenti(List<ProcedimentoDto> procedimenti) {
		procedimentiPanel.clear();
		procedimentiPanel.setVisible(false);
		HTMLPanel innerPanel = new HTMLPanel("");
		for (ProcedimentoDto procedimento : procedimenti) {
			addProcedimentiSection(procedimento, innerPanel);
		}
		procedimentiPanel.setVisible(true);
		procedimentiPanel.add(innerPanel);
	}

	private void addProcedimentiSection(final ProcedimentoDto procedimento, HTMLPanel innerPanel) {
		Label mailId = new Label(procedimento.getNumeroPG() + "/" + procedimento.getAnnoPG());
		UListElement ul = Document.get().createULElement();
		ul.addClassName("contenitore-lista-gruppi");
		LIElement li = Document.get().createLIElement();
		li.addClassName("gruppo last clearfix");
		ul.appendChild(li);
		SpanElement span = Document.get().createSpanElement();
		span.addClassName("label nessun-protocollo");
		li.appendChild(span);
		DivElement div = Document.get().createDivElement();
		div.setClassName("corpo");
		li.appendChild(div);
		HTMLPanel panel = new HTMLPanel("");
		panel.setStyleName("box-mail");
		ElementoProcedimentoElencoWidget dettaglioWiget = new ElementoProcedimentoElencoWidget();
		dettaglioWiget.setCheckBoxVisible(true);
		dettaglioWiget.setCheckBoxEnabled(!procedimento.getStato().equals(StatoProcedimento.CHIUSO));
		dettaglioWiget.setSelezionaProcedimentoCommand(new it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, ElementoProcedimentoElencoWidget.SelezioneProcedimento>() {

			@Override
			public Void exe(SelezioneProcedimento t) {
				if (t.isChecked()) {
					procedimentiSelezionati.add(procedimento);
				} else {
					procedimentiSelezionati.remove(procedimento);
				}
				impostaAbilitazioniPulsantiera();
				return null;
			}
		});
		panel.add(dettaglioWiget);
		dettaglioWiget.mostraDettaglio(procedimento);
		innerPanel.getElement().appendChild(ul);
		innerPanel.add(mailId, span);
		innerPanel.add(panel, div);
	}

	@Override
	public Set<ProcedimentoDto> getProcedimentiSelezionati() {
		return procedimentiSelezionati;
	}

	@Override
	public void setRiversamentoCartaceoCommand(final Command command) {
		Event.sinkEvents(newRiversamentoCartaceo, Event.ONCLICK);
		Event.setEventListener(newRiversamentoCartaceo, new EventListener() {

			@Override
			public void onBrowserEvent(Event event) {
				if (Event.ONCLICK == event.getTypeInt()) {
					command.execute();
				}
			}
		});
	}

	@Override
	public void setSganciaPecInCommand(final Command sganciaPecInDaFascicoloCommand) {
		Event.sinkEvents(newSeparaDaFascicolo, Event.ONCLICK);
		Event.setEventListener(newSeparaDaFascicolo, new EventListener() {

			@Override
			public void onBrowserEvent(Event event) {
				if (Event.ONCLICK == event.getTypeInt()) {
					sganciaPecInDaFascicoloCommand.execute();
				}
			}
		});
	}

	@Override
	public void setRiportaInLetturaCommand(final Command command) {
		Event.sinkEvents(newRiportaInLettura, Event.ONCLICK);
		Event.setEventListener(newRiportaInLettura, new EventListener() {

			@Override
			public void onBrowserEvent(Event event) {
				if (Event.ONCLICK == event.getTypeInt()) {
					command.execute();
				}
			}
		});
	}

	@Override
	public void setAssegnaEsternoCommand(final Command command) {
		Event.sinkEvents(newAssegnaEsterno, Event.ONCLICK);
		Event.setEventListener(newAssegnaEsterno, new EventListener() {

			@Override
			public void onBrowserEvent(Event event) {
				if (Event.ONCLICK == event.getTypeInt()) {
					command.execute();
				}
			}
		});
	}

	@Override
	public void setModificaAbilitazioniAssegnaEsternoCommand(final Command command) {
		Event.sinkEvents(newModificaAbilitazioni, Event.ONCLICK);
		Event.setEventListener(newModificaAbilitazioni, new EventListener() {

			@Override
			public void onBrowserEvent(Event event) {
				if (Event.ONCLICK == event.getTypeInt()) {
					command.execute();
				}
			}
		});
	}

	@Override
	public void setRitornaDaInoltraEsterno(final Command command) {
		Event.sinkEvents(newConcludi, Event.ONCLICK);
		Event.setEventListener(newConcludi, new EventListener() {

			@Override
			public void onBrowserEvent(Event event) {
				if (Event.ONCLICK == event.getTypeInt()) {
					command.execute();
				}
			}
		});
	}

	@Override
	public void setStampaRicevuteConsegnaCommand(final Command command) {
		Event.sinkEvents(newRicevutaDiConsegna, Event.ONCLICK);
		Event.setEventListener(newRicevutaDiConsegna, new EventListener() {

			@Override
			public void onBrowserEvent(Event event) {
				if (Event.ONCLICK == event.getTypeInt()) {
					command.execute();
				}
			}
		});
	}

	@Override
	public void setRichiediFirmaFascicoloCommand(final RichiediFirmaCommand command) {
		Event.sinkEvents(newRichiediFirma, Event.ONCLICK);
		Event.setEventListener(newRichiediFirma, new EventListener() {

			@Override
			public void onBrowserEvent(Event event) {
				if (Event.ONCLICK == event.getTypeInt()) {
					command.execute();
				}
			}
		});
	}

	@Override
	public void setRitiraTaskFirmaCommand(final RitiraTaskFirmaCommand command) {
		Event.sinkEvents(newRitiraTaskFirma, Event.ONCLICK);
		Event.setEventListener(newRitiraTaskFirma, new EventListener() {

			@Override
			public void onBrowserEvent(Event event) {
				if (Event.ONCLICK == event.getTypeInt()) {
					command.execute();
				}
			}
		});
	}

	@Override
	public void setInviaCSV(final InviaDaCsvCommand command) {
		Event.sinkEvents(newInviaDaCSV, Event.ONCLICK);
		Event.setEventListener(newInviaDaCSV, new EventListener() {

			@Override
			public void onBrowserEvent(Event event) {
				if (Event.ONCLICK == event.getTypeInt()) {
					widgetComposizioneFascicolo.getElementiSelezionati().clear();
					command.execute();
				}
			}
		});
	}

	@Override
	public void setEstraiEMLCommand(final EstraiEMLCommand command) {
		Event.sinkEvents(newEstraiEML, Event.ONCLICK);
		Event.setEventListener(newEstraiEML, new EventListener() {

			@Override
			public void onBrowserEvent(Event event) {
				if (Event.ONCLICK == event.getTypeInt()) {
					widgetComposizioneFascicolo.getElementiSelezionati().clear();
					command.execute();
				}
			}
		});
	}

	@Override
	public void setModificaOperatoreCommand(final ModificaOperatoreCommand command) {
		Event.sinkEvents(newModificaOperatore, Event.ONCLICK);
		Event.setEventListener(newModificaOperatore, new EventListener() {

			@Override
			public void onBrowserEvent(Event event) {
				if (Event.ONCLICK == event.getTypeInt()) {
					command.setPratica(pratica);
					command.execute();
				}
			}
		});
	}

	public Set<AllegatoDTO> getAllegatiNonProtSelezionati() {
		return allegatiNonProtSelezionati;
	}

	@Override
	public Set<AllegatoDTO> getAllegatiProtSelezionati() {
		return allegatiProtSelezionati;
	}

	public Set<ElementoElenco> getPecNonProtSelezionate() {
		return pecNonProtSelezionate;
	}

	public Set<ElementoElenco> getPecProtSelezionate() {
		return pecProtSelezionate;
	}

	public Set<ElementoPraticaModulisticaRiferimento> getModulisticheNonProtSelezionate() {
		return modulisticheNonProtSelezionate;
	}

	public Set<ElementoPraticaModulisticaRiferimento> getModulisticheProtSelezionate() {
		return modulisticheProtSelezionate;
	}

	public List<ElementoPECRiferimento> getPecOutConRicevute() {
		return pecOutConRicevute;
	}

	public void showHideImportaAllegatiMenuItem(boolean b) {
		showHideMenuItem(newImportaAllegati, b);
	}

	@Override
	public List<DatoAggiuntivo> getDatiAggiuntivi() {
		return formDatiAggiuntiviWidget.getDatiAggiuntivi();
	}

	@Override
	public boolean controlloServerDatiAggiuntivi(List<ValidazioneDatoAggiuntivoDTO> validazioneDatiAggiuntivi) {
		return formDatiAggiuntiviWidget.validazioneServer(validazioneDatiAggiuntivi);
	}

	@Override
	public void setDettaglioFascicoloCollegatoCommand(DettaglioFascicoloCollegatoCommand dettaglioFascicoloCollegatoCommand) {
		this.dettaglioFascicoloCollegatoCommand = dettaglioFascicoloCollegatoCommand;

	}

	@Override
	public void setDettaglioPraticaProcediCollegatoCommand(DettaglioPraticaProcediCollegatoCommand dettaglioPraticaProcediCollegatoCommand) {
		this.dettaglioPraticaProcediCollegatoCommand = dettaglioPraticaProcediCollegatoCommand;

	}

	@Override
	public List<String> getComposizionePraticheCollegateCaricate() {
		return composizionePraticheCollegateCaricate;
	}

	private void initCatenaDocumenatalePanel() {
		catenaDocumentaleWidget = new CatenaDocumentaleWidget();
		catenaDocumentalePanel.add(catenaDocumentaleWidget);
		catenaDocumentalePanel.setOpen(false);
	}

	@Override
	public void setCercaPGCommand(CercaPgCommand cercaPgCommand) {
		catenaDocumentaleWidget.setSearchPGCommand(cercaPgCommand);
	}

	@Override
	public SearchPGParams getSearchPGParams() {
		return catenaDocumentaleWidget.getSearchPGParams();
	}

	@Override
	public void showCatenaDocumentale(CatenaDocumentaleDTO catenaDocumentaleDTO) {

		this.catenaDocumentaleDTO = catenaDocumentaleDTO;

		catenaDocumentaleWidget.clearRisultati();
		for (PgDTO pg : catenaDocumentaleDTO.getProtocollazioni()) {
			addPgDTO(pg);
		}
		catenaDocumentaleWidget.mostraRisultati();
	}

	private void addPgDTO(PgDTO pg) {
		ElementoCatenaDocumentaleWidget dettaglioWiget = new ElementoCatenaDocumentaleWidget(pg);
		dettaglioWiget.setSelezionaPGCommand(new it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, ElementoCatenaDocumentaleWidget>() {

			@Override
			public Void exe(ElementoCatenaDocumentaleWidget w) {
				if (w.isChecked()) {
					elementiCatenaDocumentaleSelezionati.add(w);
				} else {
					elementiCatenaDocumentaleSelezionati.remove(w);
				}
				impostaAbilitazioniPulsantiera();
				return null;
			}
		});
		catenaDocumentaleWidget.addElemento(dettaglioWiget);
	}

	@Override
	public ElementoCatenaDocumentaleWidget getElementoCatenaDocumentaleSelezionato() {
		return (elementiCatenaDocumentaleSelezionati.size() > 0) ? elementiCatenaDocumentaleSelezionati.get(0) : null;
	}

	@Override
	public void loadFormDatiAggiuntivi(EventBus eventBus, Object openingRequestor, DispatchAsync dispatcher) {
		formDatiAggiuntiviWidget = new FormDatiAggiuntiviWidget(eventBus, openingRequestor, dispatcher);
		datiAggiuntiviPanel.add(formDatiAggiuntiviWidget);

	}

	@Override
	public void loadAutorizzazioni(EventBus eventBus, DispatchAsync dispatcher, PecInPraticheDB praticheDB) {
		autorizzazioniFascicoloWidget = new AutorizzazioniFascicoloWidget(profilazioneUtenteHandler, configurazioniHandler, eventBus, dispatcher, praticheDB);
		autorizzazioniSubPanel.add(autorizzazioniFascicoloWidget);
	}

	@Override
	public FormDatiAggiuntiviWidget getFormDatiAggiuntivi() {
		return formDatiAggiuntiviWidget;
	}

	@Override
	public void setEmissionePermessoCommand(final Command emissionePermessoCommand) {
		Event.sinkEvents(emissionePermesso, Event.ONCLICK);
		Event.setEventListener(emissionePermesso, new EventListener() {
			@Override
			public void onBrowserEvent(Event event) {
				if (Event.ONCLICK == event.getTypeInt()) {
					emissionePermessoCommand.execute();
				}
			}
		});
	}

	@Override
	public void setSpostaAllegatiCommand(final Command command) {
		Event.sinkEvents(newSpostaAllegati, Event.ONCLICK);
		Event.setEventListener(newSpostaAllegati, new EventListener() {
			@Override
			public void onBrowserEvent(Event event) {
				if (Event.ONCLICK == event.getTypeInt()) {
					command.execute();
				}
			}
		});

	}

	@Override
	public void setSpostaProtocollazioniCommand(final Command command) {
		Event.sinkEvents(newSpostaProtocollazioni, Event.ONCLICK);
		Event.setEventListener(newSpostaProtocollazioni, new EventListener() {
			@Override
			public void onBrowserEvent(Event event) {
				if (Event.ONCLICK == event.getTypeInt()) {
					command.execute();
				}
			}
		});

	}

	@Override
	public boolean getReloadDettaglio() {
		return reloadDettaglio;
	}

	@Override
	public void setReloadDettaglio(boolean reload) {
		reloadDettaglio = reload;
	}

	@Override
	public List<PraticaProcedi> getPraticheProcedi() {
		return praticaProcedi;
	}

	@Override
	public Set<PraticaProcedi> getPraticheProcediSelezionate() {
		return praticheProcediSelezionate;
	}

	@Override
	public void rimuoviPraticheProcedi(List<PraticaProcedi> praticheProcedi) {

		for (PraticaProcedi praticaProcedi : praticheProcedi) {
			if (this.praticaProcedi != null && this.praticaProcedi.contains(praticaProcedi)) {
				this.praticaProcedi.remove(praticaProcedi);
			}
		}
	}

	public void updateViewPostSelezione(final FascicoloDTO dto) {
		getAllegatiProtSelezionati().clear();
		getAllegatiNonProtSelezionati().clear();

		getPecProtSelezionate().clear();
		getPecNonProtSelezionate().clear();
		getPecOutConRicevute().clear();
		getEmailConAllegati().clear();

		getModulisticheProtSelezionate().clear();
		getModulisticheNonProtSelezionate().clear();

		Set<EmailComposizione> conversazioni = spianaConversazioni(dto.getComposizioneEmail());
		selezionati: //
		for (ElementoComposizione e : this.widgetComposizioneFascicolo.getElementiSelezionati()) {
			for (AllegatoDTO allegatoDTO : dto.getAllegati()) {
				if (allegatoDTO.getNome().equals(e.getNome()) && allegatoDTO.getVersioneCorrente().equals(e.getVersione())) {
					if (e.isProtocollato()) {
						getAllegatiProtSelezionati().add(allegatoDTO);
					} else {
						getAllegatiNonProtSelezionati().add(allegatoDTO);
					}
					continue selezionati;
				}
			}

			for (EmailComposizione email : conversazioni) {
				if (e.equals(email)) {
					TipoRiferimentoPEC tipo = TipoRiferimentoPEC.valueOf(email.getTipo());
					PecOutDTO.StatoDTO stato = PecOutDTO.StatoDTO.valueOf(email.getStato());
					ElementoPECRiferimento pec = new FascicoloDTO.ElementoPECRiferimento(email.getClientID(), tipo, email.getDataCaricamento());
					if (PecOutDTO.StatoDTO.PARZIALMENTECONSEGNATA.equals(stato) || PecOutDTO.StatoDTO.CONSEGNATA.equals(stato)) {
						getPecOutConRicevute().add(pec);
					}
					if (TipoRiferimentoPEC.IN.equals(tipo) || TipoRiferimentoPEC.EPROTO.equals(tipo)) {
						pecInDb.getPecInByPath(email.getClientID(), false, new PraticaEmaiInlLoaded() {
							@Override
							public void onPraticaLoaded(PecInDTO pec) {
								if (pec.getAllegati().size() > 0) {
									getEmailConAllegati().add(pec);
								}
								if (getEmailConAllegati().size() > 0 && dto.isCaricaAllegato()) {
									showHideImportaAllegatiMenuItem(true);
								} else {
									showHideImportaAllegatiMenuItem(false);
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
					if (email.isProtocollato()) {
						getPecProtSelezionate().add(pec);
					} else {
						getPecNonProtSelezionate().add(pec);
					}
					continue selezionati;
				}
			}

			if (ConsolePecConstants.StatiElementiComposizioneFascicolo.STATO_PRATICA_MODULISTICA.equals(e.getStato())) {
				ElementoPraticaModulisticaRiferimento prMod = new FascicoloDTO.ElementoPraticaModulisticaRiferimento(e.getClientID(), e.getDataCaricamento());
				if (e.isProtocollato()) {
					getModulisticheProtSelezionate().add(prMod);
				} else {
					getModulisticheNonProtSelezionate().add(prMod);
				}
				continue selezionati;
			}

			ShowMessageEvent event = new ShowMessageEvent();
			event.setWarningMessage("Un allegato non e' stato caricato correttamente: " + e.getNome());
			eventBus.fireEvent(event);
		}

		impostaAbilitazioniPulsantiera();
	}

	private Set<EmailComposizione> spianaConversazioni(List<EmailComposizione> composizione) {
		Set<EmailComposizione> tmp = new HashSet<>();
		for (EmailComposizione e : composizione) {
			tmp.add(e);
			tmp.addAll(spianaConversazioni(e.getConversazione()));
		}
		return tmp;
	}

	@Override
	public void resetComposizioneFascicolo() {
		widgetComposizioneRicerca.reset();
	}

	@Override
	public void setRicaricaCommand(final Command command) {
		ricaricaButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				command.execute();
			}
		});
	}
}
