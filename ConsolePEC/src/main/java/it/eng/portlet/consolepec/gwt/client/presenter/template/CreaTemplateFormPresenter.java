package it.eng.portlet.consolepec.gwt.client.presenter.template;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.CreazioneEmailOutAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.CreazioneModelloAbilitazione;
import it.eng.cobo.consolepec.security.handler.AutorizzazioneHandler.CondizioneAbilitazione;
import it.eng.cobo.consolepec.security.handler.AutorizzazioneHandler.QueryAbilitazione;
import it.eng.cobo.consolepec.util.pratica.PraticaUtil;
import it.eng.portlet.consolepec.gwt.client.event.BackFromPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.event.GoToPlaceEvent;
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
import it.eng.portlet.consolepec.gwt.client.presenter.Command;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter.Place;
import it.eng.portlet.consolepec.gwt.client.widget.SitemapMenu;
import it.eng.portlet.consolepec.gwt.client.widget.suggestBox.oracle.AnagraficheRuoliSuggestOracle;
import it.eng.portlet.consolepec.gwt.client.widget.template.AbstractCorpoTemplateWidget;
import it.eng.portlet.consolepec.gwt.client.widget.template.CorpoTemplateMailWidget;
import it.eng.portlet.consolepec.gwt.client.widget.template.CorpoTemplatePdfWidget;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants.VociRootSiteMap;
import it.eng.portlet.consolepec.gwt.shared.model.BaseTemplateDTO;
import it.eng.portlet.consolepec.gwt.shared.model.CampoTemplateDTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.user.client.ui.SuggestBox;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

/**
 *
 * @author biagiot
 *
 */
public class CreaTemplateFormPresenter extends Presenter<CreaTemplateFormPresenter.MyView, CreaTemplateFormPresenter.MyProxy>{

	private final EventBus eventBus;
	private final SitemapMenu siteMapMenu;
	private TemplateCreazioneApiClient templateCreazioneApiClient;
	private ProfilazioneUtenteHandler profilazioneUtenteHandler;
	private SuggestBox gruppiSuggestBox;
	private AnagraficheRuoliSuggestOracle suggestOracleRuoli;
	private String utente;
	private ConfigurazioniHandler configurazioniHandler;

	public interface MyView extends View {
		void setEliminaCampoCommand(Command<Void, CampoTemplateDTO> eliminaCampoCommand);
		void setAggiungiCampoCommand(Command<Void, CampoTemplateDTO> aggiungiCampoCommand);
		void removeCampo(CampoTemplateDTO obj);
		void addCampo(CampoTemplateDTO obj);
		boolean controlloCampi(List<String> errori);
		<T extends BaseTemplateDTO> T getTemplate();
		void setCreaTemplateCommand(com.google.gwt.user.client.Command avantiCreaTemplateCommand);
		void setAnnullaCreazioneCommand(com.google.gwt.user.client.Command annullaCommand);
		void clearFormCampi();
		void resetCorpoTemplateWidget();
		void setCorpoTemplateWidget(AbstractCorpoTemplateWidget<?> corpoTemplateWidget);
		AbstractCorpoTemplateWidget<?> getCorpoTemplateWidget();
		void resetPannelloWidget();
		TipologiaPratica getTipoTemplateSelezionato();
		void initListBox(com.google.gwt.user.client.Command changeStateCommand);
		void addItemListBox(TipologiaPratica tipoTemplate);
		void clearListBox();
		void abilitaSceltaTemplate(boolean abiltiazione);
		void abilitaCorpoTemplate(boolean abiltiazione);
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.createmplate)
	public interface MyProxy extends ProxyPlace<CreaTemplateFormPresenter> {
	}

	@Inject
	public CreaTemplateFormPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, final SitemapMenu siteMapMenu, 
			TemplateCreazioneApiClient templateCreazioneApiClient, ProfilazioneUtenteHandler profilazioneUtenteHandler, ConfigurazioniHandler configurazioniHandler) {
		super(eventBus, view, proxy);
		this.eventBus = eventBus;
		this.siteMapMenu = siteMapMenu;
		this.templateCreazioneApiClient = templateCreazioneApiClient;
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
		getView().setCreaTemplateCommand(new CreaTemplateCommand());
		getView().setAnnullaCreazioneCommand(new AnnullaCreazioneCommand());
	}

	@Override
	protected void onHide() {
		dropErrors();
		getView().resetCorpoTemplateWidget();
		super.onHide();
	}

	@Override
	protected void onReveal() {
		super.onReveal();
		siteMapMenu.setActiveVoce(VociRootSiteMap.CREA_TEMPLATE.getId());
	}


	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
	
		if (profilazioneUtenteHandler.isAbilitato(CreazioneModelloAbilitazione.class)) {
			ShowAppLoadingEvent.fire(CreaTemplateFormPresenter.this, true);
			utente = profilazioneUtenteHandler.getDatiUtente().getNomeCompleto();
			initView(utente);
			dropErrors();
			
		} else {
			throw new IllegalArgumentException("Utente non abilitato alla creazione di modelli");
		}

	}

	/*
	 * INIT VIEW
	 */

	private void initView(String nomeUtente) {		
		List<TipologiaPratica> tipiTemplateAbilitati = 
				PraticaUtil.modelliToTipologiePratiche(profilazioneUtenteHandler.getAnagraficheModelliAbilitati(CreazioneModelloAbilitazione.class));

		if (tipiTemplateAbilitati.size() == 1)
			initPannelloWidgetCreazioneTemplate(tipiTemplateAbilitati.iterator().next(), nomeUtente);
		else
			initPannelloSceltaTipologiaTemplate(tipiTemplateAbilitati);
	}

	private void initPannelloSceltaTipologiaTemplate(List<TipologiaPratica> tipiTemplateAbilitati) {
		getView().clearListBox();

		getView().initListBox(new CambiaTipologiaTemplateCommand());
		for (TipologiaPratica tipoTemplateAbilitato : tipiTemplateAbilitati) {
			getView().addItemListBox(tipoTemplateAbilitato);
		}

		getView().abilitaSceltaTemplate(true);
		cambiaTipologiaTemplate();
	}

	private void initPannelloWidgetCreazioneTemplate(TipologiaPratica tipoTemplate, String nomeUtente) {
		getView().resetPannelloWidget();
		
		if (tipoTemplate.equals(TipologiaPratica.MODELLO_MAIL)) {
			initTemplateMailWidget(nomeUtente);

		} else if (tipoTemplate.equals(TipologiaPratica.MODELLO_PDF)) {
			initTemplatePdfWidget(nomeUtente);

		}

		ShowAppLoadingEvent.fire(CreaTemplateFormPresenter.this, false);
		getView().abilitaCorpoTemplate(true);
	}

	private void initTemplateMailWidget(final String nomeUtente) {
		final CorpoTemplateMailWidget corpoTemplateMailWidget = new CorpoTemplateMailWidget(configurazioniHandler, templateCreazioneApiClient);
		corpoTemplateMailWidget.clear();
		corpoTemplateMailWidget.initListaCampiTemplate();
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
		corpoTemplateMailWidget.setDataCreazionePratica(new Date());
		corpoTemplateMailWidget.setUtente(nomeUtente);
		corpoTemplateMailWidget.headingInformazioniVisibile(true);
		corpoTemplateMailWidget.headingInformazioniModelloMailVisibile(true);
		getView().setAggiungiCampoCommand(new AggiungiCampoCommand(corpoTemplateMailWidget));
		getView().setEliminaCampoCommand(new EliminaCampoCommand(corpoTemplateMailWidget));
		getView().setCorpoTemplateWidget(corpoTemplateMailWidget);

	}

	private void initTemplatePdfWidget(final String nomeUtente) {
		final CorpoTemplatePdfWidget corpoTemplatePdfWidget = new CorpoTemplatePdfWidget(configurazioniHandler, templateCreazioneApiClient);
		corpoTemplatePdfWidget.clear();
		corpoTemplatePdfWidget.initListaCampiTemplate();
		QueryAbilitazione<CreazioneModelloAbilitazione> qb = new QueryAbilitazione<CreazioneModelloAbilitazione>();
		qb.addCondition(new CondizioneAbilitazione<CreazioneModelloAbilitazione>() {
			
			@Override
			protected boolean valutaCondizione(CreazioneModelloAbilitazione abilitazione) {
				return abilitazione.getTipo().equals(TipologiaPratica.MODELLO_PDF.getNomeTipologia());
			}
		});
		popolaRuoli(corpoTemplatePdfWidget, profilazioneUtenteHandler.getAnagraficheRuoliAbilitati(CreazioneModelloAbilitazione.class, qb));
		caricaTipologieFascicoli(corpoTemplatePdfWidget);
		corpoTemplatePdfWidget.setDataCreazionePratica(new Date());
		corpoTemplatePdfWidget.setUtente(nomeUtente);
		corpoTemplatePdfWidget.headingInformazioniVisibile(true);
		getView().setAggiungiCampoCommand(new AggiungiCampoCommand(corpoTemplatePdfWidget));
		getView().setEliminaCampoCommand(new EliminaCampoCommand(corpoTemplatePdfWidget));
		getView().setCorpoTemplateWidget(corpoTemplatePdfWidget);
	}


	/*
	 * COMMANDS
	 */

	public class CreaTemplateCommand implements com.google.gwt.user.client.Command {

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
				creaTemplate(getView().getTemplate());
			}
		}
	}

	public class AnnullaCreazioneCommand implements com.google.gwt.user.client.Command {

		@Override
		public void execute() {
			eventBus.fireEvent(new BackFromPlaceEvent());
		}
	}

	public class CambiaTipologiaTemplateCommand implements com.google.gwt.user.client.Command {

		@Override
		public void execute() {
			cambiaTipologiaTemplate();
		}
	}

	private <T extends BaseTemplateDTO> void creaTemplate(T template) {
		
		ShowAppLoadingEvent.fire(CreaTemplateFormPresenter.this, true);

		templateCreazioneApiClient.creaModello(template, new CallbackTemplate<T>() {

			@Override
			public void onComplete(T template) {
				ShowAppLoadingEvent.fire(CreaTemplateFormPresenter.this, false);
				goToDettaglioTemplate(template.getClientID());
			}

			@Override
			public void onError(String errorMessage) {
				showErrors(errorMessage, true);
			}
			
		});
	}

	private void cambiaTipologiaTemplate() {
		TipologiaPratica tipoTemplateSelezionato = getView().getTipoTemplateSelezionato();
		initPannelloWidgetCreazioneTemplate(tipoTemplateSelezionato, utente);
	}

	private void showErrors(String errorMessage, boolean stopLoadingEvent) {
		if(stopLoadingEvent)
			ShowAppLoadingEvent.fire(CreaTemplateFormPresenter.this, false);

		ShowMessageEvent event = new ShowMessageEvent();
		event.setErrorMessage(errorMessage);
		getEventBus().fireEvent(event);
	}

	private void dropErrors() {
		ShowMessageEvent event = new ShowMessageEvent();
		event.setMessageDropped(true);
		getEventBus().fireEvent(event);
	}

	private void caricaTipologieFascicoli(AbstractCorpoTemplateWidget<?> corpoTemplateWidget) {
		corpoTemplateWidget.setTipiFascicoloAbilitati(PraticaUtil.fascicoliToTipologiePratiche(configurazioniHandler.getAnagraficheFascicoli(true)));
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

	private void goToDettaglioTemplate(String clientId) {
		Place place = new Place();
		place.setToken(NameTokens.dettagliotemplate);
		place.addParam(NameTokensParams.idPratica, clientId);
		getEventBus().fireEvent(new GoToPlaceEvent(place));
	}
}
