package it.eng.portlet.consolepec.gwt.client.presenter;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.presenter.slots.NestedSlot;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealRootLayoutContentEvent;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

import it.eng.portlet.consolepec.gwt.client.event.ApertoDettaglioEvent;
import it.eng.portlet.consolepec.gwt.client.event.ApertoDettaglioEvent.DecrementaNumeroNonLettiHandler;
import it.eng.portlet.consolepec.gwt.client.event.AppTerminatedEvent;
import it.eng.portlet.consolepec.gwt.client.event.BackFromPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.event.BackFromPlaceEvent.BackFromPlaceHandler;
import it.eng.portlet.consolepec.gwt.client.event.GoToPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.event.GoToPlaceEvent.GoToPlaceHandler;
import it.eng.portlet.consolepec.gwt.client.event.SessionTimeoutEvent;
import it.eng.portlet.consolepec.gwt.client.event.SessionTimeoutEvent.SessionTimeoutHandler;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.UpdateSiteMapEvent;
import it.eng.portlet.consolepec.gwt.client.event.UpdateSiteMapEvent.UpdateSiteMapHandler;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.place.NameTokens;
import it.eng.portlet.consolepec.gwt.client.presenter.event.UploadEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.event.UploadEvent.UploadHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.newsitemap.SiteMapConfiguraSiteMap;
import it.eng.portlet.consolepec.gwt.client.presenter.newsitemap.SiteMapHandler;
import it.eng.portlet.consolepec.gwt.client.widget.SitemapMenu;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;

public class MainPresenter extends Presenter<MainPresenter.MyView, MainPresenter.MyProxy> implements DecrementaNumeroNonLettiHandler, SessionTimeoutHandler, BackFromPlaceHandler, GoToPlaceHandler, UpdateSiteMapHandler, UploadHandler {

	public interface MyView extends View {
		public void forwardUploadEvent(UploadEvent event);
	}

	@ProxyCodeSplit
	public interface MyProxy extends Proxy<MainPresenter> {/**/}

	private final SitemapMenu siteMap;
	private final PlaceManager placeManager;
	private final Stack<Place> history = new Stack<Place>();
	private SiteMapHandler siteMapHandler;
	private SiteMapConfiguraSiteMap siteMapConfiguraSiteMap;
	private ConfigurazioniHandler configurazioniHandler;
	private ProfilazioneUtenteHandler profilazioneUtenteHandler;

	@Inject
	public MainPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, final SitemapMenu siteMenu, final PlaceManager placeManager, SiteMapHandler siteMapHandler,
			SiteMapConfiguraSiteMap siteMapConfiguraSiteMap, ProfilazioneUtenteHandler profilazioneUtenteHandler, ConfigurazioniHandler configurazioniHandler) {
		super(eventBus, view, proxy);
		this.siteMap = siteMenu;
		this.placeManager = placeManager;
		this.siteMapHandler = siteMapHandler;
		this.siteMapConfiguraSiteMap = siteMapConfiguraSiteMap;
		this.profilazioneUtenteHandler = profilazioneUtenteHandler;
		this.configurazioniHandler = configurazioniHandler;
	}

	@Override
	protected void revealInParent() {
		RevealRootLayoutContentEvent.fire(this, this);
	}

	@Override
	protected void onBind() {
		super.onBind();
		checkConfigurationForSiteMap();
		configuraErroriGlobali();
	}

	private void configuraErroriGlobali() {
		/* gestione degli errori globale */
		GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {

			@Override
			public void onUncaughtException(Throwable e) {
				e.printStackTrace();
				HTMLPanel panel = new HTMLPanel("<h4>Attenzione, si è verificato un ERRORE imprevisto. L'applicazione sarà riavviata</h4>");
				Button bt = new Button("OK");
				bt.setStylePrimaryName("btn black");
				bt.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						/* ricarichiamo verso il default place */
						String newHref = (Location.getHref().split("#").length > 0 ? Location.getHref().split("#")[0] : Location.getHref());
						Location.replace(newHref);
					}
				});
				panel.add(bt);
				AppTerminatedEvent.fire(MainPresenter.this, panel);
			}
		});
	}

	private void checkConfigurationForSiteMap() {
		ShowAppLoadingEvent.fire(MainPresenter.this, true);
		siteMap.setTitle("Gestione Documentale");

		if (!profilazioneUtenteHandler.getDatiUtente().isUtenteEsterno()) {
			siteMapHandler.configuraSiteMap(MainPresenter.this);
			ShowAppLoadingEvent.fire(MainPresenter.this, false);

		} else {
			ShowAppLoadingEvent.fire(MainPresenter.this, false);
		}
	}

	public static final NestedSlot TYPE_SetMainContent = new NestedSlot();

	public static final NestedSlot TYPE_SetSplashScreenContent = new NestedSlot();

	public static final NestedSlot TYPE_SetMessageContent = new NestedSlot();

	private void updateSiteMap(boolean reload) {
		siteMapConfiguraSiteMap.updateSiteMap(reload);
	}

	@ProxyEvent
	@Override
	public void onDettaglioAperto(ApertoDettaglioEvent event) {
		if (event.isReloadWorklist() != null) {
			updateSiteMap(event.isReloadWorklist());

		} else if (configurazioniHandler.getProprietaGenerali().getMaxRisultatiWorklist() != null) {
			updateSiteMap(false);

		} else {
			updateSiteMap(true);

		}
	}

	@ProxyEvent
	@Override
	public void onSessionTimeout(SessionTimeoutEvent event) {
		HTMLPanel panel = new HTMLPanel("<h4>Attenzione, la sessione utente è scaduta. L'applicazione sarà riavviata</h4>");
		Button bt = new Button("OK");
		bt.setStylePrimaryName("btn black");
		bt.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				/* ricarichiamo verso il default place */
				String newHref = (Location.getHref().split("#").length > 0 ? Location.getHref().split("#")[0] : Location.getHref());
				Location.replace(newHref);
			}
		});
		panel.add(bt);
		AppTerminatedEvent.fire(this, panel);
	}

	@ProxyEvent
	@Override
	public void onGoToPlace(GoToPlaceEvent goToPlaceEvent) {
		Place place = goToPlaceEvent.getPlace();

		if (Place.isToHistory(placeManager.getCurrentPlaceRequest().getNameToken())) {
			/* se il place va aggiunto alla history */
			Place currentPlace = new Place();
			String nameToken = placeManager.getCurrentPlaceRequest().getNameToken();
			currentPlace.setToken(nameToken);
			Set<String> parameterNames = placeManager.getCurrentPlaceRequest().getParameterNames();
			for (String paramName : parameterNames) {
				currentPlace.addParam(paramName, placeManager.getCurrentPlaceRequest().getParameter(paramName, null));
			}

			/* se l'ultimo place inserito è diverso dal corrente oppure lo stack è vuoto metto il place corrente nello stack */
			if (!place.equals(currentPlace) || place.isForceHistory())
				history.push(currentPlace);
		}
		// }
		/* mi muovo verso il token indicato */
		navigate(place);
	}

	@ProxyEvent
	@Override
	public void onBackFromPlace(BackFromPlaceEvent backFromTokenEvent) {
		Place place = null;
		int size = history.size();
		if (size > 0) {
			place = history.pop();
		} else {
			place = Place.getDefaultPlace();
		}

		if (backFromTokenEvent.isReload()) {
			place.addParam(ConsolePecConstants.FORCE_RELOAD_PARAM, Boolean.TRUE.toString());
		}

		navigate(place);
		if (backFromTokenEvent.getIdPratica() != null)
			siteMap.removeSiteMapOpenVoice(backFromTokenEvent.getIdPratica());
	}

	private void navigate(Place place) {
		PlaceRequest.Builder builder = new PlaceRequest.Builder();
		builder.nameToken(place.getToken());
		for (Entry<String, String> entry : place.getParams().entrySet()) {
			builder.with(entry.getKey(), entry.getValue());
		}
		placeManager.revealPlace(builder.build());
	}

	/* inner classes */

	public static class Place {

		private static HashMap<String, Boolean> _maps = new HashMap<String, Boolean>();

		static {
			_maps.put(NameTokens.creafascicolo, Boolean.FALSE);
			_maps.put(NameTokens.dettagliofascicolo, Boolean.TRUE);
			_maps.put(NameTokens.dettagliopecin, Boolean.TRUE);
			_maps.put(NameTokens.dettagliopecout, Boolean.TRUE);
			_maps.put(NameTokens.firmaallegato, Boolean.FALSE);
			_maps.put(NameTokens.ricercalibera, Boolean.TRUE);
			_maps.put(NameTokens.worklistfascicolo, Boolean.TRUE);
			_maps.put(NameTokens.worklistpecin, Boolean.TRUE);
			_maps.put(NameTokens.sceltafascicolo, Boolean.FALSE);
			_maps.put(NameTokens.caricaallegati, Boolean.FALSE);
			_maps.put(NameTokens.dettagliopraticamodulistica, Boolean.FALSE);
			_maps.put(NameTokens.dettagliotemplate, Boolean.TRUE);
			_maps.put(NameTokens.createmplate, Boolean.FALSE);
			_maps.put(NameTokens.worklisttemplate, Boolean.TRUE);
			_maps.put(NameTokens.sceltatemplate, Boolean.TRUE);
			_maps.put(NameTokens.worklistpraticamodulistica, Boolean.TRUE);
			_maps.put(NameTokens.worklistcomunicazione, Boolean.TRUE);
			_maps.put(NameTokens.dettagliocomunicazione, Boolean.TRUE);
			_maps.put(NameTokens.creacomunicazione, Boolean.FALSE);
			_maps.put(NameTokens.worklistcartellafirma, Boolean.TRUE);

			/*
			 * Rubrica
			 */
			_maps.put(NameTokens.listaanagrafiche, Boolean.TRUE);
			_maps.put(NameTokens.creaanagrafica, Boolean.TRUE);

			/*
			 * Procedi
			 */
			_maps.put(NameTokens.praticaprocedi, Boolean.TRUE);

			/*
			 * Amministrazione
			 */
			_maps.put(NameTokens.amministrazione, Boolean.TRUE);

			_maps.put(NameTokens.listaabilitazioni, Boolean.TRUE);
			_maps.put(NameTokens.dettaglioabilitazione, Boolean.TRUE);
			_maps.put(NameTokens.creaabilitazione, Boolean.TRUE);

			_maps.put(NameTokens.listaanagraficafascicoli, Boolean.TRUE);
			_maps.put(NameTokens.dettaglioanagraficafascicolo, Boolean.TRUE);
			_maps.put(NameTokens.creaanagraficafascicolo, Boolean.TRUE);

			_maps.put(NameTokens.listaanagraficagruppi, Boolean.TRUE);
			_maps.put(NameTokens.dettaglioanagraficagruppo, Boolean.TRUE);
			_maps.put(NameTokens.creaanagraficagruppo, Boolean.TRUE);

			_maps.put(NameTokens.listaanagraficaingresso, Boolean.TRUE);
			_maps.put(NameTokens.creaanagraficaingresso, Boolean.TRUE);
			_maps.put(NameTokens.dettaglioanagraficaingresso, Boolean.TRUE);

			/*
			 * Amianto
			 */
			_maps.put(NameTokens.estrazioniamianto, Boolean.FALSE);

			/*
			 * Urbanistica
			 */
			_maps.put(NameTokens.angular, Boolean.TRUE);
			_maps.put(NameTokens.drive, Boolean.TRUE);
			_maps.put(NameTokens.drivedetail, Boolean.TRUE);

		}

		private String token;
		private HashMap<String, String> params = new HashMap<String, String>();
		private boolean addToHistory;
		private boolean forceHistory;

		public boolean isForceHistory() {
			return forceHistory;
		}

		public void forceHistory() {
			this.forceHistory = true;
		}

		public Place() {
			this.addToHistory = true;
		}

		public static Place getDefaultPlace() {
			Place p = new Place();
			p.setToken(NameTokens.worklistcartellafirma);
			return p;
		}

		public boolean isAddToHistory() {
			return addToHistory;
		}

		public String getToken() {
			return token;
		}

		public void setToken(String token) {
			Boolean addToHistory = _maps.get(token);
			if (addToHistory == null)
				throw new IllegalStateException("Token non configurato.");
			this.addToHistory = addToHistory;
			this.token = token;
		}

		public void addParam(String name, String value) {
			params.put(name, value);
		}

		public HashMap<String, String> getParams() {
			return params;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj != null && obj instanceof Place) {
				Place place = (Place) obj;

				boolean checkToken = (this.token == null && place.token == null) || this.token.equals(place.token);
				boolean checkAddHistory = this.addToHistory && place.addToHistory;

				return checkToken && checkAddHistory;

			}
			return false;
		}

		@Override
		public String toString() {
			return token + " " + addToHistory;
		}

		public static boolean isToHistory(String token) {
			return _maps.get(token);
		}
	}

	@Override
	@ProxyEvent
	public void onUpdateSiteMap(UpdateSiteMapEvent event) {
		if (event.isReloadWorklist() != null) {
			updateSiteMap(event.isReloadWorklist());

		} else if (configurazioniHandler.getProprietaGenerali().getMaxRisultatiWorklist() != null) {
			updateSiteMap(false);

		} else {
			updateSiteMap(true);

		}
	}

	@Override
	@ProxyEvent
	public void onUpload(UploadEvent event) {
		getView().forwardUploadEvent(event);
	}

}
