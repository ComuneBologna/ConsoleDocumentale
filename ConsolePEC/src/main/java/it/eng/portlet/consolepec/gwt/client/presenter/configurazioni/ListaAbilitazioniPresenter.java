package it.eng.portlet.consolepec.gwt.client.presenter.configurazioni;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.AmministrazioneRuoliAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.VisibilitaComunicazioneAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.VisibilitaFascicoloAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.VisibilitaModelloAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.VisibilitaPraticaModulisticaAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.VisibilitaRuoloAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.util.AbilitazioneRuoloSingola;
import it.eng.cobo.consolepec.util.configurazioni.ConfigurazioniUtil;
import it.eng.cobo.consolepec.util.console.ConsoleConstants;
import it.eng.cobo.consolepec.util.pratica.PraticaUtil;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.operazioni.amministrazione.AmministrazioneAbilitazioniVisibilitaApiClient;
import it.eng.portlet.consolepec.gwt.client.operazioni.amministrazione.AmministrazioneAbilitazioniVisibilitaApiClient.AbilitazioneCallback;
import it.eng.portlet.consolepec.gwt.client.operazioni.amministrazione.AmministrazioneAbilitazioniVisibilitaApiClient.TipoAbilitazione;
import it.eng.portlet.consolepec.gwt.client.operazioni.amministrazione.AmministrazioneAbilitazioniVisibilitaApiClient.TipoAccessoAbilitazione;
import it.eng.portlet.consolepec.gwt.client.place.NameTokens;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter;
import it.eng.portlet.consolepec.gwt.client.widget.SitemapMenu;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants.VociRootSiteMap;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
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
public class ListaAbilitazioniPresenter extends Presenter<ListaAbilitazioniPresenter.MyView, ListaAbilitazioniPresenter.MyProxy> {

	private static final DateTimeFormat dtf = DateTimeFormat.getFormat(ConsoleConstants.FORMATO_ISO8601);

	@ProxyCodeSplit
	@NameToken(NameTokens.listaabilitazioni)
	public interface MyProxy extends ProxyPlace<ListaAbilitazioniPresenter> {
		//
	}

	public interface MyView extends View {
		String getFiltroGruppoAbilitato();

		String getFiltroTipologiaPraticaAssociata();

		String getFiltroTipoAbilitazione();

		Date getFiltroDataA();

		Date getFiltroDataDa();

		String getFiltroGruppoAssociato();

		String getFiltroAbilitazione();

		void mostraAbilitazioni(List<AbilitazioneRuoloSingola> abilitazioni, int start, int count);

		String getCampoOrdinamento();

		Boolean getTipoOrdinamento();

		void setDataProvider(ListaAbilitazioniProvider provider);

		void setColumnSortEventHandler(ColumnSortEvent.Handler handler);

		void setCercaCommand(Command command);

		void clearView();
	}

	private final SitemapMenu sitemapMenu;
	private ProfilazioneUtenteHandler profilazioneUtenteHandler;
	private ConfigurazioniHandler configurazioniHandler;
	private AmministrazioneAbilitazioniVisibilitaApiClient amministrazioneAbilitazioniApiClient;

	@Inject
	public ListaAbilitazioniPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, final SitemapMenu sitemapMenu, ProfilazioneUtenteHandler profilazioneUtenteHandler,
			ConfigurazioniHandler configurazioniHandler, AmministrazioneAbilitazioniVisibilitaApiClient amministrazioneAbilitazioniApiClient) {
		super(eventBus, view, proxy);
		this.sitemapMenu = sitemapMenu;
		this.profilazioneUtenteHandler = profilazioneUtenteHandler;
		this.configurazioniHandler = configurazioniHandler;
		this.amministrazioneAbilitazioniApiClient = amministrazioneAbilitazioniApiClient;
	}

	@Override
	protected void onBind() {
		super.onBind();
		getView().setCercaCommand(new Command() {

			@Override
			public void execute() {
				ricerca(5, 0);
			}
		});

		getView().setColumnSortEventHandler(new ColumnSortEvent.Handler() {

			@Override
			public void onColumnSort(ColumnSortEvent arg0) {
				ricerca(5, 0);
			}
		});

		getView().setDataProvider(new ListaAbilitazioniProvider());

	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPresenter.TYPE_SetMainContent, this);
	}

	@Override
	protected void onReveal() {
		super.onReveal();
		revealInParent();
		Window.scrollTo(0, 0);
	}

	@Override
	protected void onHide() {
		super.onHide();
		getView().clearView();
	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);

		if (profilazioneUtenteHandler.isAbilitato(AmministrazioneRuoliAbilitazione.class)) {
			revealInParent();
			sitemapMenu.setActiveVoce(VociRootSiteMap.ABILITAZIONI.getId());
			ricerca(5, 0);

		} else {
			throw new IllegalArgumentException("Utente non amministratore");
		}
	}

	private void ricerca(Integer limit, final Integer offset) {
		ShowAppLoadingEvent.fire(ListaAbilitazioniPresenter.this, true);

		String campoOrdinamento = getView().getCampoOrdinamento() != null ? getView().getCampoOrdinamento() : ConfigurazioniUtil.FILTRO_RICERCA_CONFIGURAZIONE_ETICHETTA_PRATICA;
		Boolean tipoOrdinamento = getView().getTipoOrdinamento() != null ? getView().getTipoOrdinamento() : Boolean.TRUE;

		amministrazioneAbilitazioniApiClient.cerca(getFiltriDiRicerca(), limit, offset, campoOrdinamento, tipoOrdinamento, new AbilitazioneCallback() {

			@Override
			public void onSuccess(List<AbilitazioneRuoloSingola> a, int count) {
				ShowAppLoadingEvent.fire(ListaAbilitazioniPresenter.this, false);
				getView().mostraAbilitazioni(a, offset, count);
			}

			@Override
			public void onError(String error) {
				ShowAppLoadingEvent.fire(ListaAbilitazioniPresenter.this, false);
				fireErrorMessageEvent(error);
			}
		});
	}

	private Map<String, Object> getFiltriDiRicerca() {
		Map<String, Object> filtri = new HashMap<String, Object>();

		if (getView().getFiltroTipoAbilitazione() != null && !getView().getFiltroTipoAbilitazione().trim().isEmpty()) {

			TipoAbilitazione tipo = TipoAbilitazione.valueOf(getView().getFiltroTipoAbilitazione());

			if (TipoAbilitazione.Fascicolo.equals(tipo)) {
				filtri.put(ConfigurazioniUtil.FILTRO_RICERCA_ABILITAZIONI_RUOLO_TIPO_ABILITAZIONE, VisibilitaFascicoloAbilitazione.class.getSimpleName());

			} else if (TipoAbilitazione.Comunicazione.equals(tipo)) {
				filtri.put(ConfigurazioniUtil.FILTRO_RICERCA_ABILITAZIONI_RUOLO_TIPO_ABILITAZIONE, VisibilitaComunicazioneAbilitazione.class.getSimpleName());

			} else if (TipoAbilitazione.Modello.equals(tipo)) {
				filtri.put(ConfigurazioniUtil.FILTRO_RICERCA_ABILITAZIONI_RUOLO_TIPO_ABILITAZIONE, VisibilitaModelloAbilitazione.class.getSimpleName());

			} else if (TipoAbilitazione.PModulistica.equals(tipo)) {
				filtri.put(ConfigurazioniUtil.FILTRO_RICERCA_ABILITAZIONI_RUOLO_TIPO_ABILITAZIONE, VisibilitaPraticaModulisticaAbilitazione.class.getSimpleName());

			} else if (TipoAbilitazione.Gruppo.equals(tipo)) {
				filtri.put(ConfigurazioniUtil.FILTRO_RICERCA_ABILITAZIONI_RUOLO_TIPO_ABILITAZIONE, VisibilitaRuoloAbilitazione.class.getSimpleName());
			}
		}

		if (getView().getFiltroAbilitazione() != null && !getView().getFiltroAbilitazione().trim().isEmpty()
				&& !filtri.containsKey(ConfigurazioniUtil.FILTRO_RICERCA_ABILITAZIONI_RUOLO_TIPO_ABILITAZIONE)) {
			TipoAccessoAbilitazione t = TipoAccessoAbilitazione.from(getView().getFiltroAbilitazione());
			if (t != null) {

				switch (t) {
				case Lettura:
					filtri.put(
							ConfigurazioniUtil.FILTRO_RICERCA_ABILITAZIONI_RUOLO_TIPO_ABILITAZIONE,
							Arrays.asList(VisibilitaFascicoloAbilitazione.class.getSimpleName(), VisibilitaComunicazioneAbilitazione.class.getSimpleName(),
									VisibilitaModelloAbilitazione.class.getSimpleName(), VisibilitaPraticaModulisticaAbilitazione.class.getSimpleName(),
									VisibilitaRuoloAbilitazione.class.getSimpleName()));
					break;
				}

			}
		}

		if (getView().getFiltroGruppoAbilitato() != null && !getView().getFiltroGruppoAbilitato().trim().isEmpty()) {
			AnagraficaRuolo ar = configurazioniHandler.getAnagraficaRuoloByEtichetta(getView().getFiltroGruppoAbilitato());
			if (ar != null) {
				filtri.put(ConfigurazioniUtil.FILTRO_RICERCA_ABILITAZIONI_RUOLO_RUOLO, ar.getRuolo());
			}
		}

		if (getView().getFiltroGruppoAssociato() != null && !getView().getFiltroGruppoAssociato().trim().isEmpty()) {
			AnagraficaRuolo ar = configurazioniHandler.getAnagraficaRuoloByEtichetta(getView().getFiltroGruppoAssociato());
			if (ar != null) {
				filtri.put(ConfigurazioniUtil.FILTRO_RICERCA_ABILITAZIONI_RUOLO_ABILITAZIONE_RUOLO, ar.getRuolo());

				if (filtri.get(ConfigurazioniUtil.FILTRO_RICERCA_ABILITAZIONI_RUOLO_TIPO_ABILITAZIONE) == null) {
					filtri.put(ConfigurazioniUtil.FILTRO_RICERCA_ABILITAZIONI_RUOLO_TIPO_ABILITAZIONE, VisibilitaRuoloAbilitazione.class.getSimpleName());
				}
			}
		}

		if (getView().getFiltroTipologiaPraticaAssociata() != null && !getView().getFiltroTipologiaPraticaAssociata().trim().isEmpty()) {
			filtri.put(ConfigurazioniUtil.FILTRO_RICERCA_ABILITAZIONI_RUOLO_ABILITAZIONE_PRATICA_TIPO, getView().getFiltroTipologiaPraticaAssociata());

			if (filtri.get(ConfigurazioniUtil.FILTRO_RICERCA_ABILITAZIONI_RUOLO_TIPO_ABILITAZIONE) == null) {

				if (PraticaUtil.isFascicolo(getView().getFiltroTipologiaPraticaAssociata())) {
					filtri.put(ConfigurazioniUtil.FILTRO_RICERCA_ABILITAZIONI_RUOLO_TIPO_ABILITAZIONE, VisibilitaFascicoloAbilitazione.class.getSimpleName());

				} else if (PraticaUtil.isComunicazione(getView().getFiltroTipologiaPraticaAssociata())) {
					filtri.put(ConfigurazioniUtil.FILTRO_RICERCA_ABILITAZIONI_RUOLO_TIPO_ABILITAZIONE, VisibilitaComunicazioneAbilitazione.class.getSimpleName());

				} else if (PraticaUtil.isModello(getView().getFiltroTipologiaPraticaAssociata())) {
					filtri.put(ConfigurazioniUtil.FILTRO_RICERCA_ABILITAZIONI_RUOLO_TIPO_ABILITAZIONE, VisibilitaModelloAbilitazione.class.getSimpleName());

				} else if (PraticaUtil.isPraticaModulistica(getView().getFiltroTipologiaPraticaAssociata())) {
					filtri.put(ConfigurazioniUtil.FILTRO_RICERCA_ABILITAZIONI_RUOLO_TIPO_ABILITAZIONE, VisibilitaPraticaModulisticaAbilitazione.class.getSimpleName());
				}
			}
		}

		if (getView().getFiltroDataDa() != null) {
			String date = dtf.format(getView().getFiltroDataDa());
			filtri.put(ConfigurazioniUtil.FILTRO_RICERCA_ABILITAZIONI_DATA_CREAZIONE_DA, date);
		}

		if (getView().getFiltroDataA() != null) {
			String date = dtf.format(getView().getFiltroDataA());
			filtri.put(ConfigurazioniUtil.FILTRO_RICERCA_ABILITAZIONI_DATA_CREAZIONE_A, date);
		}

		return filtri;
	}

	private void fireErrorMessageEvent(String msg) {
		ShowMessageEvent event = new ShowMessageEvent();
		event.setErrorMessage(msg);
		getEventBus().fireEvent(event);
	}

	public class ListaAbilitazioniProvider extends AsyncDataProvider<AbilitazioneRuoloSingola> {

		@Override
		protected void onRangeChanged(HasData<AbilitazioneRuoloSingola> display) {
			int start = display.getVisibleRange().getStart();
			int length = display.getVisibleRange().getLength();
			ricerca(length, start);
		}
	}

}