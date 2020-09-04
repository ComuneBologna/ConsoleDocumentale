package it.eng.portlet.consolepec.gwt.client.presenter.template.dettaglio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
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
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.CreazioneEmailOutAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.CreazioneModelloAbilitazione;
import it.eng.cobo.consolepec.security.handler.AutorizzazioneHandler.CondizioneAbilitazione;
import it.eng.cobo.consolepec.security.handler.AutorizzazioneHandler.QueryAbilitazione;
import it.eng.cobo.consolepec.util.pratica.PraticaUtil;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaLoaded;
import it.eng.portlet.consolepec.gwt.client.event.ApertoDettaglioEvent;
import it.eng.portlet.consolepec.gwt.client.event.BackFromPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.event.ChiudiDettaglioAllegatoEvent;
import it.eng.portlet.consolepec.gwt.client.event.ChiudiDettaglioAllegatoEvent.ChiudiDettaglioAllegatoHandler;
import it.eng.portlet.consolepec.gwt.client.event.GoToPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.event.MostraDettaglioAllegatoEvent;
import it.eng.portlet.consolepec.gwt.client.event.RichiestaConfermaAnnullaEvent;
import it.eng.portlet.consolepec.gwt.client.event.SceltaConfermaAnnullaEvent;
import it.eng.portlet.consolepec.gwt.client.event.SceltaConfermaAnnullaEvent.SceltaConfermaAnnullaHandler;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.operazioni.template.AggiungiCampoCommand;
import it.eng.portlet.consolepec.gwt.client.operazioni.template.EliminaCampoCommand;
import it.eng.portlet.consolepec.gwt.client.operazioni.template.TemplateCreazioneApiClient;
import it.eng.portlet.consolepec.gwt.client.operazioni.template.TemplateCreazioneApiClient.CallbackTemplate;
import it.eng.portlet.consolepec.gwt.client.place.NameTokens;
import it.eng.portlet.consolepec.gwt.client.place.NameTokensParams;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter.Place;
import it.eng.portlet.consolepec.gwt.client.presenter.event.UploadEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.event.UploadEvent.UploadStatus;
import it.eng.portlet.consolepec.gwt.client.presenter.inviomassivo.event.MostraCreaComunicazioneDaDettaglioTemplateEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.template.event.BackToDettaglioTemplateEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.template.event.BackToDettaglioTemplateEvent.BackToDettaglioTemplateHanlder;
import it.eng.portlet.consolepec.gwt.client.util.GestioneLinkSiteMapUtil;
import it.eng.portlet.consolepec.gwt.client.widget.SitemapMenu;
import it.eng.portlet.consolepec.gwt.client.widget.suggestBox.oracle.AnagraficheRuoliSuggestOracle;
import it.eng.portlet.consolepec.gwt.client.widget.template.AbstractCorpoTemplateWidget;
import it.eng.portlet.consolepec.gwt.client.widget.template.CorpoTemplateMailWidget;
import it.eng.portlet.consolepec.gwt.client.widget.template.CorpoTemplatePdfWidget;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.UriMapping;
import it.eng.portlet.consolepec.gwt.shared.action.CancellaAllegatoPratica;
import it.eng.portlet.consolepec.gwt.shared.action.CancellaAllegatoPraticaResult;
import it.eng.portlet.consolepec.gwt.shared.action.pec.UploadAllegatoPraticaAction;
import it.eng.portlet.consolepec.gwt.shared.action.pec.UploadAllegatoPraticaResult;
import it.eng.portlet.consolepec.gwt.shared.action.template.eliminazione.EliminaTemplateAction;
import it.eng.portlet.consolepec.gwt.shared.action.template.eliminazione.EliminaTemplateResult;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.BaseTemplateDTO;
import it.eng.portlet.consolepec.gwt.shared.model.BaseTemplateDTO.ModelloVisitor;
import it.eng.portlet.consolepec.gwt.shared.model.CampoTemplateDTO;
import it.eng.portlet.consolepec.gwt.shared.model.EventoIterDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.RispostaFileUploaderDTO;
import it.eng.portlet.consolepec.gwt.shared.model.TemplateDTO;
import it.eng.portlet.consolepec.gwt.shared.model.TemplatePdfDTO;

/**
 *
 * @author biagiot
 *
 */
public class DettaglioTemplatePresenter extends Presenter<DettaglioTemplatePresenter.MyView, DettaglioTemplatePresenter.MyProxy> implements BackToDettaglioTemplateHanlder, ChiudiDettaglioAllegatoHandler, SceltaConfermaAnnullaHandler {

	public interface MyView extends View {
		void setCorpoTemplateWidget(AbstractCorpoTemplateWidget<?> corpoTemplateWidget);

		void mostraTitolo(boolean enabled);

		void mostraPulsantiera(boolean enabled);

		void resetDisclosurePanels(boolean showActions);

		void initWidgetEventiIter(List<EventoIterDTO> eventiIter);

		void abilitaEliminaButton(boolean enable);

		void abilitaCreaComunicazioneButton(boolean enable);

		void abilitaCreaPerCopiaButton(boolean enable);

		void abilitaSalvaButton(boolean enable);

		void abilitaCaricaAllegatiButton(boolean enable);

		void abilitaEliminaAllegatoButton(boolean enable);

		void mostraAllegati(List<AllegatoDTO> allegati, String panelName);

		boolean controlloCampi(List<String> errori);

		<T extends BaseTemplateDTO> T getTemplate();

		void addCampo(CampoTemplateDTO o);

		void removeCampo(CampoTemplateDTO o);

		void setCorpoTemplateWidgetReadOnly(boolean b);

		void sendDownload(SafeUri uri);

		void startUpload();

		void setDownloadAllegatoButtonName(String name);

		Set<AllegatoDTO> getAllegatiSelezionati();

		void showEliminaAllegatoButton(boolean show);

		void showCreaComunicazioneButton(boolean show);

		void setChiudiDettaglioCommand(Command chiudiDettaglioCommand);

		void setSalvaTemplateCommand(Command salvaTemplateCommand);

		void setCreaTemplatePerCopiaCommand(Command creaTemplatePerCopiacommand);

		void setEliminaTemplateCommand(Command eliminaTemplateCommand);

		void setEliminaCampoCommand(it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, CampoTemplateDTO> eliminaCampoCommand);

		void setAggiungiCampoCommand(it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, CampoTemplateDTO> aggiungiCampoCommand);

		void setCreaComunicazioneCommand(Command creaComunicazioneCommand);

		void cancellaListaAllegatiSelezionati();

		void setUploadAllegatoCommand(UploadAllegatoCommand uploadAllegatoCommand);

		void setDownloadAllegatoCommand(it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, AllegatoDTO> downloadAllegatoCommand);

		void setCancellaAllegatoCommand(it.eng.portlet.consolepec.gwt.client.presenter.Command<Object, Set<AllegatoDTO>> command);

		void setMostraDettaglioAllegatoCommand(it.eng.portlet.consolepec.gwt.client.presenter.Command<Object, AllegatoDTO> mostraDettaglioAllegatoCommand);

		void clearCorpoTemplatePanel();
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.dettagliotemplate)
	public interface MyProxy extends ProxyPlace<DettaglioTemplatePresenter> {}

	private String templatePath;
	private String eventID;
	private String idDocumentale;

	private SuggestBox gruppiSuggestBox;
	private AnagraficheRuoliSuggestOracle suggestOracleRuoli;

	private GestioneLinkSiteMapUtil gestioneLinkSiteMapUtil;
	private PecInPraticheDB praticheDB;
	private DispatchAsync dispatcher;
	private SitemapMenu siteMapMenu;
	private PlaceManager placeManager;
	private TemplateCreazioneApiClient templateCreazioneApiClient;
	private ProfilazioneUtenteHandler profilazioneUtenteHandler;
	private ConfigurazioniHandler configurazioniHandler;

	private final String CARICA_ODT = "Carica Modello ODT";
	private final String RICARICA_ODT = "Ricarica Modello ODT";
	private final String CARICA_ALLEGATO = "Carica Allegato";
	private final String PANNELLO_ALLEGATI = "Allegati";
	private final String PANNELLO_ODT = "Modello ODT";

	@Inject
	public DettaglioTemplatePresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, PecInPraticheDB pecInDb, GestioneLinkSiteMapUtil gestioneLinkSiteMapUtil,
			ConfigurazioniHandler configurazioniHandler, TemplateCreazioneApiClient templateCreazioneApiClient, DispatchAsync dispatcher, SitemapMenu siteMapMenu, PlaceManager placeManager,
			ProfilazioneUtenteHandler profilazioneUtenteHandler) {
		super(eventBus, view, proxy);
		this.praticheDB = pecInDb;
		this.gestioneLinkSiteMapUtil = gestioneLinkSiteMapUtil;
		this.configurazioniHandler = configurazioniHandler;
		this.templateCreazioneApiClient = templateCreazioneApiClient;
		this.dispatcher = dispatcher;
		this.siteMapMenu = siteMapMenu;
		this.placeManager = placeManager;
		this.profilazioneUtenteHandler = profilazioneUtenteHandler;
	}

	@Override
	protected void onBind() {
		super.onBind();

		getView().setChiudiDettaglioCommand(new ChiudiDettaglioCommand());
		getView().setSalvaTemplateCommand(new SalvaTemplateCommand());
		getView().setEliminaTemplateCommand(new EliminaTemplateCommand());
		getView().setCreaComunicazioneCommand(new CreaComunicazioneCommand());
		getView().setCreaTemplatePerCopiaCommand(new CreaTemplatePerCopia());

		getView().setUploadAllegatoCommand(new UploadAllegatoCommand());
		getView().setDownloadAllegatoCommand(new DownloadAllegatoCommand());
		getView().setCancellaAllegatoCommand(new CancellaAllegatoCommand());
		getView().setMostraDettaglioAllegatoCommand(new MostraDettaglioAllegatoCommand());
	}

	@Override
	protected void onReveal() {
		super.onReveal();
		getView().mostraTitolo(false);
		getView().mostraPulsantiera(false);
		loadTemplate();
		dropErrors();
	}

	@Override
	protected void onHide() {
		dropErrors();
		super.onHide();
		getView().clearCorpoTemplatePanel();
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPresenter.TYPE_SetMainContent, this);
	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);

		this.templatePath = request.getParameter(NameTokensParams.idPratica, null);
		getView().resetDisclosurePanels(Boolean.parseBoolean(request.getParameter(NameTokensParams.showActions, null)));

		if (super.isVisible()) {
			getView().mostraTitolo(false);
			getView().mostraPulsantiera(false);
			loadTemplate();
		}
	}

	private void loadTemplate() {
		ShowAppLoadingEvent.fire(DettaglioTemplatePresenter.this, true);

		praticheDB.getPraticaByPath(templatePath, true, new PraticaLoaded() {

			@Override
			public void onPraticaLoaded(PraticaDTO pratica) {
				idDocumentale = pratica.getNumeroRepertorio();
				BaseTemplateDTO template = (BaseTemplateDTO) pratica;
				mostraTemplate(template);
				gestioneLinkSiteMapUtil.aggiungiLinkInLavorazione(template);
				getView().mostraTitolo(true);
				getView().mostraPulsantiera(true);
				ApertoDettaglioEvent.fire(DettaglioTemplatePresenter.this, pratica);
			}

			@Override
			public void onPraticaError(String error) {
				showErrors(ConsolePecConstants.ERROR_MESSAGE, true);
			}
		});
	}

	private void mostraTemplate(BaseTemplateDTO template) {
		getView().clearCorpoTemplatePanel();

		template.accept(new ModelloVisitor() {

			@Override
			public void visit(TemplateDTO modelloMail) {
				showDettaglioTemplateMail(modelloMail);
				ShowAppLoadingEvent.fire(DettaglioTemplatePresenter.this, false);
			}

			@Override
			public void visit(TemplatePdfDTO modelloPdf) {
				showDettaglioTemplatePDF(modelloPdf);
				ShowAppLoadingEvent.fire(DettaglioTemplatePresenter.this, false);
			}
		});
	}

	private void showDettaglioTemplateMail(final TemplateDTO templateMail) {
		final CorpoTemplateMailWidget corpoTemplateMailWidget = new CorpoTemplateMailWidget(configurazioniHandler, templateCreazioneApiClient);
		corpoTemplateMailWidget.clear();

		QueryAbilitazione<CreazioneModelloAbilitazione> qb = new QueryAbilitazione<CreazioneModelloAbilitazione>();
		qb.addCondition(new CondizioneAbilitazione<CreazioneModelloAbilitazione>() {

			@Override
			protected boolean valutaCondizione(CreazioneModelloAbilitazione abilitazione) {
				return abilitazione.getTipo().equals(TipologiaPratica.MODELLO_MAIL.getNomeTipologia());
			}
		});

		popolaRuoli(corpoTemplateMailWidget, profilazioneUtenteHandler.getAnagraficheRuoliAbilitati(CreazioneModelloAbilitazione.class, qb));

		List<String> mittentiAbilitati = profilazioneUtenteHandler.getIndirizziEmailInUscitaAbilitati(CreazioneEmailOutAbilitazione.class);
		Map<String, String> mittMap = new HashMap<String, String>();
		for (String mitt : mittentiAbilitati) {
			mittMap.put(mitt, mitt);
		}
		popolaMittenti(corpoTemplateMailWidget, mittMap);
		caricaTipologieFascicoli(corpoTemplateMailWidget);
		corpoTemplateMailWidget.setTemplate(templateMail);
		corpoTemplateMailWidget.headingInformazioniVisibile(false);
		corpoTemplateMailWidget.headingInformazioniModelloMailVisibile(false);
		corpoTemplateMailWidget.enableStato(templateMail.getStatoTemplate());
		getView().setAggiungiCampoCommand(new AggiungiCampoCommand(corpoTemplateMailWidget));
		getView().setEliminaCampoCommand(new EliminaCampoCommand(corpoTemplateMailWidget));
		getView().setCorpoTemplateWidget(corpoTemplateMailWidget);
		impostaAbilitazioniPulsantieraTemplateMail(templateMail);
		getView().initWidgetEventiIter(templateMail.getEventiIterDTO());
		getView().mostraAllegati(templateMail.getAllegati(), PANNELLO_ALLEGATI);

	}

	private void showDettaglioTemplatePDF(final TemplatePdfDTO templatePDF) {
		final CorpoTemplatePdfWidget corpoTemplatePDFWidget = new CorpoTemplatePdfWidget(configurazioniHandler, templateCreazioneApiClient);
		corpoTemplatePDFWidget.clear();
		QueryAbilitazione<CreazioneModelloAbilitazione> qb = new QueryAbilitazione<CreazioneModelloAbilitazione>();
		qb.addCondition(new CondizioneAbilitazione<CreazioneModelloAbilitazione>() {

			@Override
			protected boolean valutaCondizione(CreazioneModelloAbilitazione abilitazione) {
				return abilitazione.getTipo().equals(TipologiaPratica.MODELLO_PDF.getNomeTipologia());
			}
		});

		popolaRuoli(corpoTemplatePDFWidget, profilazioneUtenteHandler.getAnagraficheRuoliAbilitati(CreazioneModelloAbilitazione.class, qb));
		caricaTipologieFascicoli(corpoTemplatePDFWidget);
		corpoTemplatePDFWidget.setTemplate(templatePDF);
		corpoTemplatePDFWidget.headingInformazioniVisibile(false);
		corpoTemplatePDFWidget.enableStato(templatePDF.getStatoTemplate());
		getView().setAggiungiCampoCommand(new AggiungiCampoCommand(corpoTemplatePDFWidget));
		getView().setEliminaCampoCommand(new EliminaCampoCommand(corpoTemplatePDFWidget));
		getView().setCorpoTemplateWidget(corpoTemplatePDFWidget);
		impostaAbilitazioniPulsantieraTemplatePDF(templatePDF);
		getView().initWidgetEventiIter(templatePDF.getEventiIterDTO());

		List<AllegatoDTO> allegati = new ArrayList<AllegatoDTO>();
		if (templatePDF.getModelloOdt() != null)
			allegati.add(templatePDF.getModelloOdt());

		getView().mostraAllegati(allegati, PANNELLO_ODT);
	}

	private void impostaAbilitazioniPulsantieraTemplateMail(TemplateDTO templateMail) {
		getView().abilitaEliminaButton(templateMail.isEliminaButtonAbilitato());
		getView().abilitaCreaComunicazioneButton(templateMail.isCreaComunicazioneButtonAbilitato());
		getView().abilitaCreaPerCopiaButton(templateMail.isCreaTemplatePerCopiaAbilitato());
		getView().abilitaSalvaButton(templateMail.isSalvaButtonAbilitato());
		getView().abilitaCaricaAllegatiButton(templateMail.isCaricaAllegatoAbilitato());
		getView().abilitaEliminaAllegatoButton(templateMail.isEliminaAllegatoAbilitato());
		getView().setCorpoTemplateWidgetReadOnly(!templateMail.isSalvaButtonAbilitato());
		getView().setDownloadAllegatoButtonName(CARICA_ALLEGATO);
	}

	private void impostaAbilitazioniPulsantieraTemplatePDF(TemplatePdfDTO templatePDF) {
		getView().abilitaEliminaButton(templatePDF.isEliminaButtonAbilitato());
		getView().abilitaCreaComunicazioneButton(false);
		getView().abilitaCreaPerCopiaButton(templatePDF.isCreaTemplatePerCopiaAbilitato());
		getView().abilitaSalvaButton(templatePDF.isSalvaButtonAbilitato());
		getView().abilitaCaricaAllegatiButton(templatePDF.isCaricaModelloOdtAbilitato());
		getView().setCorpoTemplateWidgetReadOnly(!templatePDF.isSalvaButtonAbilitato());

		if (templatePDF.getModelloOdt() != null)
			getView().setDownloadAllegatoButtonName(RICARICA_ODT);
		else
			getView().setDownloadAllegatoButtonName(CARICA_ODT);

		getView().showEliminaAllegatoButton(false);
		getView().showCreaComunicazioneButton(false);
	}

	/*
	 * COMMANDS
	 */

	public class ChiudiDettaglioCommand implements Command {

		@Override
		public void execute() {
			praticheDB.remove(templatePath);
			getEventBus().fireEvent(new BackFromPlaceEvent(templatePath));
		}
	}

	public class SalvaTemplateCommand implements Command {

		@Override
		public void execute() {
			dropErrors();

			List<String> errori = new ArrayList<String>();

			if (getView().controlloCampi(errori) == false) {
				StringBuilder messaggio = new StringBuilder();
				messaggio.append("<br/>");
				for (String errore : errori) {
					messaggio.append(errore + "<br/>");
				}

				showErrors(messaggio.toString(), true);

			} else {
				salvaTemplate(getView().getTemplate());
			}
		}
	}

	public class CreaTemplatePerCopia implements com.google.gwt.user.client.Command {

		@Override
		public void execute() {
			List<String> errori = new ArrayList<String>();
			if (getView().controlloCampi(errori) == false) {
				StringBuilder messaggio = new StringBuilder();
				messaggio.append("<br/>");
				for (String errore : errori) {
					messaggio.append(errore + "<br/>");
				}

				showErrors(messaggio.toString(), false);

			} else {
				creaTemplatePerCopia(getView().getTemplate());
			}
		}
	}

	private <T extends BaseTemplateDTO> void creaTemplatePerCopia(T template) {
		ShowAppLoadingEvent.fire(DettaglioTemplatePresenter.this, true);

		templateCreazioneApiClient.creaModelloPerCopia(idDocumentale, new CallbackTemplate<T>() {
			@Override
			public void onError(String errorMessage) {
				showErrors(errorMessage, true);
			}

			@Override
			public void onComplete(T template) {
				ShowAppLoadingEvent.fire(DettaglioTemplatePresenter.this, false);
				goToDettaglioTemplate(template.getClientID());
			}
		});
	}

	private <T extends BaseTemplateDTO> void salvaTemplate(T template) {
		salvaTemplate(template, null);
	}

	private <T extends BaseTemplateDTO> void salvaTemplate(T template, final AsyncCallback<String> asyncCallback) {
		ShowAppLoadingEvent.fire(DettaglioTemplatePresenter.this, true);

		templateCreazioneApiClient.salvaModello(template, templatePath, new CallbackTemplate<T>() {

			@Override
			public void onComplete(T template) {
				if (asyncCallback != null) {
					ShowAppLoadingEvent.fire(DettaglioTemplatePresenter.this, false);
					asyncCallback.onSuccess(template.getClientID());

				} else {
					mostraTemplate(template);
				}
			}

			@Override
			public void onError(String errorMessage) {
				showErrors(errorMessage, true);
			}

		});
	}

	public class EliminaTemplateCommand implements Command {

		@Override
		public void execute() {
			eliminaTemplate();
		}
	}

	public class CreaComunicazioneCommand implements com.google.gwt.user.client.Command {

		@Override
		public void execute() {
			getEventBus().fireEvent(new MostraCreaComunicazioneDaDettaglioTemplateEvent((TemplateDTO) getView().getTemplate()));
		}
	}

	public class UploadAllegatoCommand {

		public void onFileSelected(String fileName) {

			dropErrors();

			salvaTemplate(getView().getTemplate(), new AsyncCallback<String>() {

				@Override
				public void onFailure(Throwable caught) {
					showErrors(ConsolePecConstants.ERROR_MESSAGE, false);
				}

				@Override
				public void onSuccess(String result) {
					getEventBus().fireEvent(new UploadEvent(templatePath, UploadStatus.START));
					DettaglioTemplatePresenter.this.getView().startUpload();
				}
			});
		}

		public void onFileUploaded(RispostaFileUploaderDTO dto) {
			dropErrors();

			if (!dto.isError()) {

				UploadAllegatoPraticaAction action = new UploadAllegatoPraticaAction(dto.getTmpFiles(), DettaglioTemplatePresenter.this.templatePath);
				dispatcher.execute(action, new AsyncCallback<UploadAllegatoPraticaResult>() {

					@Override
					public void onFailure(Throwable caught) {
						getEventBus().fireEvent(new UploadEvent(templatePath, UploadStatus.ERROR));
						showErrors(ConsolePecConstants.ERROR_MESSAGE, false);
					}

					@Override
					public void onSuccess(UploadAllegatoPraticaResult result) {
						getEventBus().fireEvent(new UploadEvent(templatePath, UploadStatus.DONE));

						if (!result.getError()) {
							dropErrors();
							praticheDB.insertOrUpdate(result.getPratica().getClientID(), result.getPratica(), siteMapMenu.containsLink(result.getPratica().getClientID()));
							mostraTemplate((BaseTemplateDTO) result.getPratica());

						} else {
							getEventBus().fireEvent(new UploadEvent(templatePath, UploadStatus.ERROR));
							showErrors(ConsolePecConstants.ERROR_MESSAGE, false);
						}

					}
				});

			} else {
				getEventBus().fireEvent(new UploadEvent(templatePath, UploadStatus.ERROR));
				showErrors(ConsolePecConstants.ERROR_MESSAGE, false);
			}
		}
	}

	private class DownloadAllegatoCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, AllegatoDTO> {

		@Override
		public Void exe(final AllegatoDTO allegato) {
			SafeUri uri = UriMapping.generaDownloadAllegatoServletURL(allegato.getClientID(), allegato);
			getView().sendDownload(uri);
			return null;
		}
	}

	private class CancellaAllegatoCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Object, Set<AllegatoDTO>> {

		@Override
		public Object exe(final Set<AllegatoDTO> allegati) {

			dropErrors();
			ShowAppLoadingEvent.fire(DettaglioTemplatePresenter.this, true);

			salvaTemplate(getView().getTemplate(), new AsyncCallback<String>() {

				@Override
				public void onFailure(Throwable caught) {
					showErrors(ConsolePecConstants.ERROR_MESSAGE, true);

				}

				@Override
				public void onSuccess(String result) {
					CancellaAllegatoPratica action = new CancellaAllegatoPratica(templatePath, allegati);
					DettaglioTemplatePresenter.this.dispatcher.execute(action, new AsyncCallback<CancellaAllegatoPraticaResult>() {

						@Override
						public void onSuccess(final CancellaAllegatoPraticaResult result) {

							if (result.getError()) {
								showErrors(result.getMessError(), true);

							} else {
								dropErrors();
								getView().cancellaListaAllegatiSelezionati();
								mostraTemplate((BaseTemplateDTO) result.getPraticaDTO());
							}
						}

						@Override
						public void onFailure(Throwable caught) {
							showErrors(ConsolePecConstants.ERROR_MESSAGE, true);
						}
					});
				}
			});

			return null;
		}
	}

	private class MostraDettaglioAllegatoCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Object, AllegatoDTO> {

		@Override
		public Object exe(AllegatoDTO allegato) {
			mostraDettaglioAllegato(allegato, templatePath);
			return null;
		}
	}

	private void showErrors(String errorMessage, boolean stopLoadingEvent) {
		if (stopLoadingEvent)
			ShowAppLoadingEvent.fire(DettaglioTemplatePresenter.this, false);

		ShowMessageEvent event = new ShowMessageEvent();
		event.setErrorMessage(errorMessage);
		getEventBus().fireEvent(event);
	}

	private void dropErrors() {
		ShowMessageEvent event = new ShowMessageEvent();
		event.setMessageDropped(true);
		getEventBus().fireEvent(event);
	}

	private void popolaRuoli(AbstractCorpoTemplateWidget<?> corpoTemplateWidget, List<AnagraficaRuolo> ruoli) {
		suggestOracleRuoli = new AnagraficheRuoliSuggestOracle(ruoli);
		gruppiSuggestBox = new SuggestBox(suggestOracleRuoli);

		if (ruoli.size() == 1) {
			gruppiSuggestBox.setValue(ruoli.iterator().next().getEtichetta());
			gruppiSuggestBox.setEnabled(false);
			gruppiSuggestBox.setStyleName("testo disabilitato");

		} else {
			gruppiSuggestBox.removeStyleName("disabilitato");
		}

		corpoTemplateWidget.setGruppiSuggestBox(gruppiSuggestBox);
	}

	private void popolaMittenti(CorpoTemplateMailWidget corpoTemplateWidget, Map<String, String> mittenti) {
		corpoTemplateWidget.clearMittenti();
		for (Entry<String, String> e : mittenti.entrySet()) {
			String k = e.getKey();
			String v = e.getValue();
			corpoTemplateWidget.addMittente(k, v);
		}
	}

	private void caricaTipologieFascicoli(AbstractCorpoTemplateWidget<?> corpoTemplateWidget) {
		corpoTemplateWidget.setTipiFascicoloAbilitati(PraticaUtil.fascicoliToTipologiePratiche(configurazioniHandler.getAnagraficheFascicoli(true)));
	}

	private void mostraDettaglioAllegato(AllegatoDTO allegato, String templatePath) {
		MostraDettaglioAllegatoEvent.fire(DettaglioTemplatePresenter.this, templatePath, allegato);
	}

	@Override
	@ProxyEvent
	public void onChiudiDettaglioAllegato(ChiudiDettaglioAllegatoEvent event) {
		if (event.getClientID().equals(templatePath) && placeManager.getCurrentPlaceRequest().getNameToken().equals(NameTokens.dettagliotemplate)) {
			ShowAppLoadingEvent.fire(DettaglioTemplatePresenter.this, false);
			placeManager.revealCurrentPlace();
		}
	}

	private void eliminaTemplate() {
		eventID = DOM.createUniqueId();
		RichiestaConfermaAnnullaEvent.fire(DettaglioTemplatePresenter.this, "<h4>Procedere con la cancellazione del modello?<h4>", eventID);
	}

	@Override
	@ProxyEvent
	public void onSceltaConfermaAnnulla(SceltaConfermaAnnullaEvent event) {
		if (event.isConfermato() && event.getEventId().equals(eventID)) {

			ShowAppLoadingEvent.fire(DettaglioTemplatePresenter.this, true);
			dropErrors();

			dispatcher.execute(new EliminaTemplateAction(templatePath), new AsyncCallback<EliminaTemplateResult>() {

				@Override
				public void onFailure(Throwable caught) {
					showErrors(ConsolePecConstants.ERROR_MESSAGE, true);
				}

				@Override
				public void onSuccess(EliminaTemplateResult result) {
					if (!result.isEsito()) {
						showErrors(result.getMessage(), true);

					} else {
						ShowAppLoadingEvent.fire(DettaglioTemplatePresenter.this, false);
						dropErrors();

						praticheDB.remove(templatePath);

						ShowAppLoadingEvent.fire(DettaglioTemplatePresenter.this, false);
						getEventBus().fireEvent(new BackFromPlaceEvent(templatePath));
					}
				}
			});
		}
	}

	private void goToDettaglioTemplate(String clientId) {
		Place place = new Place();
		place.setToken(NameTokens.dettagliotemplate);
		place.addParam(NameTokensParams.idPratica, clientId);
		getEventBus().fireEvent(new GoToPlaceEvent(place));
	}

	@Override
	@ProxyEvent
	public void onBackToDettaglioTemplate(BackToDettaglioTemplateEvent event) {
		revealInParent();
	}
}
