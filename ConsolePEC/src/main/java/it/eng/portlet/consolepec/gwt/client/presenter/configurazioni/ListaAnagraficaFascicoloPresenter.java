package it.eng.portlet.consolepec.gwt.client.presenter.configurazioni;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaFascicolo;
import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica.Stato;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.AmministrazioneFascicoliAbilitazione;
import it.eng.cobo.consolepec.util.configurazioni.ConfigurazioniUtil;
import it.eng.cobo.consolepec.util.console.ConsoleConstants;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.operazioni.amministrazione.AmministrazioneAnagraficaFascicoloApiClient;
import it.eng.portlet.consolepec.gwt.client.operazioni.amministrazione.AmministrazioneAnagraficaFascicoloApiClient.AnagraficaFascicoloCallback;
import it.eng.portlet.consolepec.gwt.client.place.NameTokens;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter;
import it.eng.portlet.consolepec.gwt.client.widget.SitemapMenu;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants.VociRootSiteMap;

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
public class ListaAnagraficaFascicoloPresenter extends Presenter<ListaAnagraficaFascicoloPresenter.MyView, ListaAnagraficaFascicoloPresenter.MyProxy> {

	private static final DateTimeFormat dtf = DateTimeFormat.getFormat(ConsoleConstants.FORMATO_ISO8601);

	@ProxyCodeSplit
	@NameToken(NameTokens.listaanagraficafascicoli)
	public interface MyProxy extends ProxyPlace<ListaAnagraficaFascicoloPresenter> {
		//
	}

	public interface MyView extends View {
		String getFiltroTipologiaFascicolo();

		Stato getFiltroStato();

		boolean getFiltroProtocollabile();

		Date getFiltroDataA();

		Date getFiltroDataDa();

		void mostraAnagraficheFascicoli(List<AnagraficaFascicolo> anagraficheFascicoli, int start, int count);

		void setDataProvider(ListaAnagraficheFascicoliProvider provider);

		void setColumnSortEventHandler(ColumnSortEvent.Handler handler);

		String getCampoOrdinamento();

		Boolean getTipoOrdinamento();

		void setCercaCommand(Command command);

		void clearView();
	}

	private final SitemapMenu sitemapMenu;
	private ProfilazioneUtenteHandler profilazioneUtenteHandler;
	private AmministrazioneAnagraficaFascicoloApiClient amministrazioneAnagraficaFascicoloApiClient;

	@Inject
	public ListaAnagraficaFascicoloPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, final SitemapMenu sitemapMenu, ProfilazioneUtenteHandler profilazioneUtenteHandler,
			AmministrazioneAnagraficaFascicoloApiClient amministrazioneAnagraficaFascicoloApiClient) {
		super(eventBus, view, proxy);

		this.profilazioneUtenteHandler = profilazioneUtenteHandler;
		this.amministrazioneAnagraficaFascicoloApiClient = amministrazioneAnagraficaFascicoloApiClient;
		this.sitemapMenu = sitemapMenu;
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
		getView().setDataProvider(new ListaAnagraficheFascicoliProvider());
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

		if (profilazioneUtenteHandler.isAbilitato(AmministrazioneFascicoliAbilitazione.class)) {
			revealInParent();
			sitemapMenu.setActiveVoce(VociRootSiteMap.ANAGRAFICA_FASCICOLI.getId());
			ricerca(5, 0);

		} else {
			throw new IllegalArgumentException("Utente non amministratore");
		}
	}

	private void ricerca(Integer limit, final Integer offset) {
		ShowAppLoadingEvent.fire(ListaAnagraficaFascicoloPresenter.this, true);

		String campoOrdinamento = getView().getCampoOrdinamento() != null ? getView().getCampoOrdinamento() : ConfigurazioniUtil.FILTRO_RICERCA_CONFIGURAZIONE_ETICHETTA_PRATICA;
		Boolean tipoOrdinamento = getView().getTipoOrdinamento() != null ? getView().getTipoOrdinamento() : Boolean.TRUE;

		amministrazioneAnagraficaFascicoloApiClient.cerca(getFiltriDiRicerca(), limit, offset, campoOrdinamento, tipoOrdinamento, new AnagraficaFascicoloCallback() {

			@Override
			public void onSuccess(AnagraficaFascicolo a) {/**/}

			@Override
			public void onSuccess(List<AnagraficaFascicolo> a, int count) {
				ShowAppLoadingEvent.fire(ListaAnagraficaFascicoloPresenter.this, false);
				getView().mostraAnagraficheFascicoli(a, offset, count);
			}

			@Override
			public void onError(String error) {
				ShowAppLoadingEvent.fire(ListaAnagraficaFascicoloPresenter.this, false);
				fireErrorMessageEvent(error);
			}
		});
	}

	private Map<String, Object> getFiltriDiRicerca() {
		Map<String, Object> filtri = new HashMap<String, Object>();

		if (getView().getFiltroTipologiaFascicolo() != null && !getView().getFiltroTipologiaFascicolo().trim().isEmpty()) {
			filtri.put(ConfigurazioniUtil.FILTRO_RICERCA_CONFIGURAZIONE_TIPO_PRATICA, getView().getFiltroTipologiaFascicolo().toUpperCase());
		}

		if (getView().getFiltroStato() != null) {
			filtri.put(ConfigurazioniUtil.FILTRO_RICERCA_CONFIGURAZIONE_STATO, getView().getFiltroStato().toString());
		}

		if (getView().getFiltroDataDa() != null) {
			String date = dtf.format(getView().getFiltroDataDa());
			filtri.put(ConfigurazioniUtil.FILTRO_RICERCA_CONFIGURAZIONE_DATA_CREAZIONE_DA, date);
		}

		if (getView().getFiltroDataA() != null) {
			String date = dtf.format(getView().getFiltroDataA());
			filtri.put(ConfigurazioniUtil.FILTRO_RICERCA_CONFIGURAZIONE_DATA_CREAZIONE_A, date);
		}

		if (getView().getFiltroStato() != null) {
			filtri.put(ConfigurazioniUtil.FILTRO_RICERCA_CONFIGURAZIONE_STATO, getView().getFiltroStato().toString());
		}

		filtri.put(ConfigurazioniUtil.FILTRO_RICERCA_FASCICOLI_PROTOCOLLABILE, getView().getFiltroProtocollabile());

		return filtri;
	}

	private void fireErrorMessageEvent(String msg) {
		ShowMessageEvent event = new ShowMessageEvent();
		event.setErrorMessage(msg);
		getEventBus().fireEvent(event);
	}

	public class ListaAnagraficheFascicoliProvider extends AsyncDataProvider<AnagraficaFascicolo> {

		@Override
		protected void onRangeChanged(HasData<AnagraficaFascicolo> display) {
			int start = display.getVisibleRange().getStart();
			int length = display.getVisibleRange().getLength();
			ricerca(length, start);
		}
	}
}
