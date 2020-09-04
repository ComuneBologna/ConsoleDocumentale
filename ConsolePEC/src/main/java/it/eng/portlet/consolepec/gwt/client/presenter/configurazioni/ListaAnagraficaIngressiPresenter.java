package it.eng.portlet.consolepec.gwt.client.presenter.configurazioni;

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

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaIngresso;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.AmministrazioneIngressiAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.services.AnagraficaIngressoResponse;
import it.eng.cobo.consolepec.util.configurazioni.ConfigurazioniUtil;
import it.eng.cobo.consolepec.util.console.ConsoleConstants;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.operazioni.amministrazione.AmministrazioneAnagraficaIngressoApiClient;
import it.eng.portlet.consolepec.gwt.client.operazioni.amministrazione.AmministrazioneAnagraficaIngressoApiClient.AnagraficaIngressoCallback;
import it.eng.portlet.consolepec.gwt.client.place.NameTokens;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter;
import it.eng.portlet.consolepec.gwt.client.widget.SitemapMenu;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants.VociRootSiteMap;

/**
 *
 * @author biagiot
 *
 */
public class ListaAnagraficaIngressiPresenter extends Presenter<ListaAnagraficaIngressiPresenter.MyView, ListaAnagraficaIngressiPresenter.MyProxy> {

	private static final DateTimeFormat dtf = DateTimeFormat.getFormat(ConsoleConstants.FORMATO_ISO8601);

	@ProxyCodeSplit
	@NameToken(NameTokens.listaanagraficaingresso)
	public interface MyProxy extends ProxyPlace<ListaAnagraficaIngressiPresenter> {
		//
	}

	public interface MyView extends View {

		List<String> getFiltroServer();

		String getFiltroIndirizzo();

		Date getFiltroDataA();

		Date getFiltroDataDa();

		String getCampoOrdinamento();

		Boolean getTipoOrdinamento();

		void mostraAnagraficheIngressi(List<AnagraficaIngresso> anagraficheIngressi, int start, int count);

		void setDataProvider(ListaAnagraficheIngressiProvider provider);

		void setColumnSortEventHandler(ColumnSortEvent.Handler handler);

		void setCercaCommand(Command command);

		void clearView();
	}

	private final SitemapMenu sitemapMenu;
	private ProfilazioneUtenteHandler profilazioneUtenteHandler;
	private AmministrazioneAnagraficaIngressoApiClient amministrazioneAnagraficaIngressoApiClient;

	@Inject
	public ListaAnagraficaIngressiPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, final SitemapMenu sitemapMenu, ProfilazioneUtenteHandler profilazioneUtenteHandler,
			AmministrazioneAnagraficaIngressoApiClient amministrazioneAnagraficaIngressoApiClient) {
		super(eventBus, view, proxy);

		this.profilazioneUtenteHandler = profilazioneUtenteHandler;
		this.amministrazioneAnagraficaIngressoApiClient = amministrazioneAnagraficaIngressoApiClient;
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

		getView().setDataProvider(new ListaAnagraficheIngressiProvider());

	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPresenter.TYPE_SetMainContent, this);
	}

	@Override
	protected void onHide() {
		super.onHide();
		getView().clearView();
	}

	@Override
	protected void onReveal() {
		super.onReveal();
		revealInParent();
		Window.scrollTo(0, 0);

	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);

		if (profilazioneUtenteHandler.isAbilitato(AmministrazioneIngressiAbilitazione.class)) {
			revealInParent();
			sitemapMenu.setActiveVoce(VociRootSiteMap.ANAGRAFICA_INGRESSI.getId());
			ricerca(5, 0);

		} else {
			throw new IllegalArgumentException("Utente non amministratore");
		}
	}

	private Map<String, Object> getFiltriDiRicerca() {
		Map<String, Object> filtri = new HashMap<String, Object>();

		if (getView().getFiltroIndirizzo() != null && !getView().getFiltroIndirizzo().trim().isEmpty()) {
			filtri.put(ConfigurazioniUtil.FILTRO_RICERCA_INGRESSI_INDIRIZZO, getView().getFiltroIndirizzo().toLowerCase());
		}

		if (getView().getFiltroServer() != null && !getView().getFiltroServer().isEmpty()) {
			filtri.put(ConfigurazioniUtil.FILTRO_RICERCA_INGRESSI_SERVER, getView().getFiltroServer());
		}

		if (getView().getFiltroDataDa() != null) {
			String date = dtf.format(getView().getFiltroDataDa());
			filtri.put(ConfigurazioniUtil.FILTRO_RICERCA_CONFIGURAZIONE_DATA_CREAZIONE_DA, date);
		}

		if (getView().getFiltroDataA() != null) {
			String date = dtf.format(getView().getFiltroDataA());
			filtri.put(ConfigurazioniUtil.FILTRO_RICERCA_CONFIGURAZIONE_DATA_CREAZIONE_A, date);
		}

		return filtri;
	}

	private void ricerca(Integer limit, final Integer offset) {
		ShowAppLoadingEvent.fire(ListaAnagraficaIngressiPresenter.this, true);

		String campoOrdinamento = getView().getCampoOrdinamento() != null ? getView().getCampoOrdinamento() : ConfigurazioniUtil.FILTRO_RICERCA_INGRESSI_INDIRIZZO;
		Boolean tipoOrdinamento = getView().getTipoOrdinamento() != null ? getView().getTipoOrdinamento() : Boolean.TRUE;

		amministrazioneAnagraficaIngressoApiClient.cerca(getFiltriDiRicerca(), limit, offset, campoOrdinamento, tipoOrdinamento, new AnagraficaIngressoCallback() {

			@Override
			public void onSuccess(AnagraficaIngressoResponse a) {/**/}

			@Override
			public void onSuccess(List<AnagraficaIngresso> a, int count) {
				ShowAppLoadingEvent.fire(ListaAnagraficaIngressiPresenter.this, false);
				getView().mostraAnagraficheIngressi(a, offset, count);
			}

			@Override
			public void onError(String error) {
				ShowAppLoadingEvent.fire(ListaAnagraficaIngressiPresenter.this, false);
				fireErrorMessageEvent(error);
			}
		});
	}

	private void fireErrorMessageEvent(String msg) {
		ShowMessageEvent event = new ShowMessageEvent();
		event.setErrorMessage(msg);
		getEventBus().fireEvent(event);
	}

	public class ListaAnagraficheIngressiProvider extends AsyncDataProvider<AnagraficaIngresso> {

		@Override
		protected void onRangeChanged(HasData<AnagraficaIngresso> display) {
			int start = display.getVisibleRange().getStart();
			int length = display.getVisibleRange().getLength();
			ricerca(length, start);
		}
	}
}
