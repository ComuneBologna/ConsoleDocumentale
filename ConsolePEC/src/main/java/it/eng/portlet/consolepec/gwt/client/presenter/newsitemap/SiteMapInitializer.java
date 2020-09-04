package it.eng.portlet.consolepec.gwt.client.presenter.newsitemap;

import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.AmministrazioneFascicoliAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.AmministrazioneIngressiAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.AmministrazioneRuoliAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.RicercaLiberaAbilitazione;
import it.eng.portlet.consolepec.gwt.client.event.GoToPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.place.NameTokens;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter.Place;
import it.eng.portlet.consolepec.gwt.client.widget.SitemapMenu;
import it.eng.portlet.consolepec.gwt.client.widget.TitoloLink;
import it.eng.portlet.consolepec.gwt.client.widget.images.ConsolePECIcons;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants.VociRootSiteMap;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

public class SiteMapInitializer {

	private final SitemapMenu siteMap;
	private final EventBus eventBus;
	private ProfilazioneUtenteHandler profilazioneUtenteHandler;

	@Inject
	public SiteMapInitializer(SitemapMenu siteMap, EventBus eventBus, ProfilazioneUtenteHandler profilazioneUtenteHandler) {
		super();
		this.siteMap = siteMap;
		this.eventBus = eventBus;
		this.profilazioneUtenteHandler = profilazioneUtenteHandler;
	}

	public void init() {
		
		if (profilazioneUtenteHandler.isAmministratore()) {
			String idRootAmministrazione = siteMap.addRootVoce(VociRootSiteMap.AMMINISTRAZIONE.getLabel(), "#", true, VociRootSiteMap.AMMINISTRAZIONE.name());
			siteMap.setEmptyStackToken(NameTokens.amministrazione);

			if (profilazioneUtenteHandler.isAbilitato(AmministrazioneFascicoliAbilitazione.class)) {
				siteMap.addChildVoce(getTitoloLinkAmministrazione(VociRootSiteMap.ANAGRAFICA_FASCICOLI.getLabel()), NameTokens.listaanagraficafascicoli, idRootAmministrazione, false, true, NameTokens.listaanagraficafascicoli, getClickHandlerAmministrazione(NameTokens.listaanagraficafascicoli));
			}
			
			if (profilazioneUtenteHandler.isAbilitato(AmministrazioneRuoliAbilitazione.class)) {
				siteMap.addChildVoce(getTitoloLinkAmministrazione(VociRootSiteMap.ANAGRAFICA_GRUPPI.getLabel()), NameTokens.listaanagraficagruppi, idRootAmministrazione, false, true, NameTokens.listaanagraficagruppi, getClickHandlerAmministrazione(NameTokens.listaanagraficagruppi));
			}
			
			if (profilazioneUtenteHandler.isAbilitato(AmministrazioneIngressiAbilitazione.class)) {
				siteMap.addChildVoce(getTitoloLinkAmministrazione(VociRootSiteMap.ANAGRAFICA_INGRESSI.getLabel()), NameTokens.listaanagraficaingresso, idRootAmministrazione, false, true, NameTokens.listaanagraficaingresso, getClickHandlerAmministrazione(NameTokens.listaanagraficaingresso));
			}
			
			if (profilazioneUtenteHandler.isAbilitato(AmministrazioneRuoliAbilitazione.class)) {
				siteMap.addChildVoce(getTitoloLinkAmministrazione(VociRootSiteMap.ABILITAZIONI.getLabel()), NameTokens.listaabilitazioni, idRootAmministrazione, false, true, NameTokens.listaabilitazioni, getClickHandlerAmministrazione(NameTokens.listaabilitazioni));
			}
			
		}
		
		if (profilazioneUtenteHandler.isAbilitato(RicercaLiberaAbilitazione.class)) {
			ClickHandler clickHandlerRicercaLibera = new ClickHandler() {

				@Override
				public void onClick(ClickEvent e) {
					Place place = new Place();
					place.setToken(NameTokens.ricercalibera);
					eventBus.fireEvent(new GoToPlaceEvent(place));
				}

			};
			
			siteMap.addRootVoce(VociRootSiteMap.RICERCA_LIBERA.getLabel(), NameTokens.ricercalibera, false, VociRootSiteMap.RICERCA_LIBERA.name(), clickHandlerRicercaLibera, ConsolePECIcons._instance.ricerca());
		}
	}

	private TitoloLink getTitoloLinkAmministrazione(String label){
		return new TitoloLink(label);
	}
	
	private ClickHandler getClickHandlerAmministrazione(final String nameTokens){
		
		 ClickHandler clickHandler = new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					Place place = new Place();
					place.setToken(nameTokens);
					eventBus.fireEvent(new GoToPlaceEvent(place));
				}
			};
			
		return clickHandler;
	}
	
}
