package it.eng.portlet.consolepec.gwt.client.presenter.newsitemap;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.proxy.PlaceManager;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaWorklist;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaWorklist.Counter;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.CreazioneComunicazioneAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.CreazioneFascicoloAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.CreazioneModelloAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.EstrazioneAmiantoAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.GestioneDriveAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.GestioneRubricaAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.WorklistAbilitazione;
import it.eng.portlet.consolepec.gwt.client.event.GoToPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.WorklistHandler.WorklistHandlerCallback;
import it.eng.portlet.consolepec.gwt.client.place.NameTokens;
import it.eng.portlet.consolepec.gwt.client.place.NameTokensParams;
import it.eng.portlet.consolepec.gwt.client.presenter.GestioneLinkDaLavorare;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter.Place;
import it.eng.portlet.consolepec.gwt.client.widget.SitemapMenu;
import it.eng.portlet.consolepec.gwt.client.widget.images.ConsolePECIcons;
import it.eng.portlet.consolepec.gwt.shared.Base64Utils;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants.VociRootSiteMap;

public class SiteMapConfiguraSiteMap {

	private final SitemapMenu siteMap;
	private String idRoot;
	private final PlaceManager placeManager;
	private final EventBus eventBus;
	private HashMap<String, String> aliasEmailMap = new HashMap<String, String>();
	private ConfigurazioniHandler configurazioniHandler;
	private ProfilazioneUtenteHandler profilazioneUtenteHandler;

	@Inject
	public SiteMapConfiguraSiteMap(SitemapMenu siteMap, PlaceManager placeManager, EventBus eventBus, ConfigurazioniHandler configurazioniHandler,
			ProfilazioneUtenteHandler profilazioneUtenteHandler) {
		super();
		this.siteMap = siteMap;
		this.placeManager = placeManager;
		this.eventBus = eventBus;
		this.configurazioniHandler = configurazioniHandler;
		this.profilazioneUtenteHandler = profilazioneUtenteHandler;
	}

	public void configureSiteMap(final MainPresenter mainPresenter) {
		if (profilazioneUtenteHandler.isAbilitato(EstrazioneAmiantoAbilitazione.class)) {

			ClickHandler clickHandlerEstrazioniAmianto = new ClickHandler() {

				@Override
				public void onClick(ClickEvent e) {
					Place place = new Place();
					place.setToken(NameTokens.estrazioniamianto);
					eventBus.fireEvent(new GoToPlaceEvent(place));
				}
			};
			siteMap.addRootVoce(VociRootSiteMap.ESTRAZIONI_AMIANTO.getLabel(), NameTokens.estrazioniamianto, false, VociRootSiteMap.ESTRAZIONI_AMIANTO.name(), clickHandlerEstrazioniAmianto, null);
		}

		if (profilazioneUtenteHandler.isAbilitato(CreazioneModelloAbilitazione.class)) {
			ClickHandler creaTemplateClickHandler = new ClickHandler() {

				@Override
				public void onClick(ClickEvent e) {
					Place place = new Place();
					place.setToken(NameTokens.createmplate);
					place.addParam(NameTokensParams.svuotaCampi, Boolean.TRUE.toString());
					eventBus.fireEvent(new GoToPlaceEvent(place));
				}
			};
			siteMap.addRootVoce(VociRootSiteMap.CREA_TEMPLATE.getLabel(), NameTokens.createmplate, false, VociRootSiteMap.CREA_TEMPLATE.name(), creaTemplateClickHandler, null);
		}

		if (profilazioneUtenteHandler.isAbilitato(CreazioneComunicazioneAbilitazione.class)) {
			ClickHandler creaComunicazioneClickHandler = new ClickHandler() {

				@Override
				public void onClick(ClickEvent e) {
					Place place = new Place();
					place.setToken(NameTokens.creacomunicazione);
					place.addParam(NameTokensParams.svuotaCampi, Boolean.TRUE.toString());
					eventBus.fireEvent(new GoToPlaceEvent(place));
				}
			};
			siteMap.addRootVoce(VociRootSiteMap.CREA_COMUNICAZIONE.getLabel(), NameTokens.creacomunicazione, false, VociRootSiteMap.CREA_COMUNICAZIONE.name(), creaComunicazioneClickHandler, null);
		}

		if (profilazioneUtenteHandler.isAbilitato(CreazioneFascicoloAbilitazione.class)) {
			ClickHandler clickHandlerCreaNuovoFascicolo = new ClickHandler() {

				@Override
				public void onClick(ClickEvent e) {
					Place place = new Place();
					place.setToken(NameTokens.creafascicolo);
					place.addParam(NameTokensParams.svuotaCampi, Boolean.TRUE.toString());
					eventBus.fireEvent(new GoToPlaceEvent(place));
				}
			};
			siteMap.addRootVoce(VociRootSiteMap.CREA_FASCICOLO.getLabel(), NameTokens.creafascicolo, false, VociRootSiteMap.CREA_FASCICOLO.name(), clickHandlerCreaNuovoFascicolo, null);
		}

		if (profilazioneUtenteHandler.isAbilitato(GestioneRubricaAbilitazione.class)) {
			siteMap.addRootVoce(VociRootSiteMap.LISTA_ANAGRAFICHE.getLabel(), NameTokens.listaanagrafiche, false, VociRootSiteMap.LISTA_ANAGRAFICHE.name(), new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					Place place = new Place();
					place.setToken(NameTokens.listaanagrafiche);
					eventBus.fireEvent(new GoToPlaceEvent(place));
				}
			}, null);
		}

		if (profilazioneUtenteHandler.isAbilitato(GestioneRubricaAbilitazione.class)) {
			siteMap.addRootVoce(VociRootSiteMap.CREA_ANAGRAFICA.getLabel(), NameTokens.creaanagrafica, false, VociRootSiteMap.CREA_ANAGRAFICA.name(), new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					Place place = new Place();
					place.setToken(NameTokens.creaanagrafica);
					eventBus.fireEvent(new GoToPlaceEvent(place));
				}
			}, null);
		}

		if (profilazioneUtenteHandler.isAbilitato(GestioneDriveAbilitazione.class)) {
			siteMap.addRootVoce(VociRootSiteMap.DRIVE.getLabel(), NameTokens.drive, false, VociRootSiteMap.DRIVE.name(), new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					Place place = new Place();
					place.setToken(NameTokens.drive);
					eventBus.fireEvent(new GoToPlaceEvent(place));
				}
			}, null);
		}

		/* VOCI DI GESTIONE */
		idRoot = siteMap.addRootVoce(VociRootSiteMap.DA_LAVORARE.getLabel(), "#", true, VociRootSiteMap.DA_LAVORARE.name());
		siteMap.addRootVoce(VociRootSiteMap.APERTE_ORA.getLabel(), "#", false, VociRootSiteMap.APERTE_ORA.name(), "break-word");
		siteMap.setEmptyStackToken(NameTokens.worklistcartellafirma);

		loadMainMenu();
	}

	public void loadMainMenu() {
		if (profilazioneUtenteHandler.isAbilitato(WorklistAbilitazione.class)) {

			profilazioneUtenteHandler.getWorklist(false, new WorklistHandlerCallback() {

				@Override
				public void onSuccess(Map<AnagraficaWorklist, Counter> worklists) {

					for (Entry<AnagraficaWorklist, Counter> entry : worklists.entrySet()) {

						AnagraficaWorklist wc = entry.getKey();
						Counter counter = entry.getValue();

						GestioneLinkDaLavorare gestioneLinkDaLavorare = new GestioneLinkDaLavorare(wc.getTitoloMenu(), wc.getNome(), counter.getDaLeggere(), counter.getTotale(),
								configurazioniHandler.getProprietaGenerali().getMaxRisultatiWorklist(), wc.getNome(), wc.getTitoloMenu(), aliasEmailMap);

						StringBuilder linkWorklist = new StringBuilder(wc.getNameTokenWorklist());

						linkWorklist.append(";").append(NameTokensParams.identificativoWorklist).append("=").append(Base64Utils.URLencodeAlfrescoPath(wc.getNome()));
						gestioneLinkDaLavorare.setCustomTitoloLink(wc.getTitoloMenu());

						siteMap.addChildVoce(gestioneLinkDaLavorare.getTitoloLink(), linkWorklist.toString(), idRoot, false, true, gestioneLinkDaLavorare.getIdLink(), null);
						siteMap.updateTitoloVoce(gestioneLinkDaLavorare.getIdLink(), gestioneLinkDaLavorare.getTitoloLink(), ConsolePECIcons._instance.praticamodulistica());
						siteMap.addLinkGestione(gestioneLinkDaLavorare);
					}

					AnagraficaWorklist aw = profilazioneUtenteHandler.getWorklist(placeManager.getCurrentPlaceRequest().getParameter(NameTokensParams.identificativoWorklist, null));
					if (aw != null) {
						siteMap.setActiveVoce(aw.getTitoloWorklist());
					}
					siteMap.caricamentoChildVoiceDifferite();

				}

				@Override
				public void onFailure(String error) {
					throw new IllegalStateException(error);
				}
			});
		}
	}

	public void updateSiteMap(final boolean reload) {
		profilazioneUtenteHandler.getWorklist(reload, new WorklistHandlerCallback() {

			@Override
			public void onSuccess(Map<AnagraficaWorklist, Counter> worklists) {

				for (Entry<AnagraficaWorklist, Counter> entry : worklists.entrySet()) {

					AnagraficaWorklist wc = entry.getKey();
					Counter counter = entry.getValue();

					GestioneLinkDaLavorare gestioneLinkDaLavorare = new GestioneLinkDaLavorare(wc.getTitoloMenu(), wc.getNome(), counter.getDaLeggere(), counter.getTotale(),
							configurazioniHandler.getProprietaGenerali().getMaxRisultatiWorklist(), wc.getNome(), wc.getTitoloMenu(), aliasEmailMap);

					StringBuilder linkWorklist = new StringBuilder(wc.getNameTokenWorklist());
					linkWorklist.append(";").append(NameTokensParams.identificativoWorklist).append("=").append(Base64Utils.URLencodeAlfrescoPath(wc.getNome()));
					gestioneLinkDaLavorare.setCustomTitoloLink(wc.getTitoloMenu());

					siteMap.updateTitoloVoce(gestioneLinkDaLavorare.getIdLink(), gestioneLinkDaLavorare.getTitoloLink(), ConsolePECIcons._instance.praticamodulistica());
				}
			}

			@Override
			public void onFailure(String error) {
				throw new IllegalStateException(error);
			}
		});
	}
}
