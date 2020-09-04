package it.eng.portlet.consolepec.gwt.client.presenter.configurazioni;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo.Stato;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.AmministrazioneRuoliAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.services.AnagraficaRuoloResponse;
import it.eng.cobo.consolepec.util.configurazioni.ConfigurazioniUtil;
import it.eng.cobo.consolepec.util.console.ConsoleConstants;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.operazioni.amministrazione.AmministrazioneAnagraficaRuoliApiClient;
import it.eng.portlet.consolepec.gwt.client.operazioni.amministrazione.AmministrazioneAnagraficaRuoliApiClient.AnagraficaRuoloCallback;
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
public class ListaAnagraficaGruppiPresenter extends Presenter<ListaAnagraficaGruppiPresenter.MyView, ListaAnagraficaGruppiPresenter.MyProxy> {

	private static final DateTimeFormat dtf = DateTimeFormat.getFormat(ConsoleConstants.FORMATO_ISO8601);

	@ProxyCodeSplit
	@NameToken(NameTokens.listaanagraficagruppi)
	public interface MyProxy extends ProxyPlace<ListaAnagraficaGruppiPresenter> {
		//
	}

	public interface MyView extends View {
		String getFiltroNome();

		String getFiltroLdap();

		Date getFiltroDataA();

		Date getFiltroDataDa();

		Stato getFiltroStato();

		String getCampoOrdinamento();

		Boolean getTipoOrdinamento();

		void mostraAnagraficheGruppi(List<AnagraficaRuolo> anagraficheRuoli, int start, int count);

		void setDataProvider(ListaAnagraficheGruppiProvider provider);

		void setColumnSortEventHandler(ColumnSortEvent.Handler handler);

		void setCercaCommand(Command command);

		void clearView();
	}

	private final SitemapMenu sitemapMenu;
	private AmministrazioneAnagraficaRuoliApiClient amministrazioneGruppiApiClient;
	private ProfilazioneUtenteHandler profilazioneUtenteHandler;

	@Inject
	public ListaAnagraficaGruppiPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, final SitemapMenu sitemapMenu,
			AmministrazioneAnagraficaRuoliApiClient amministrazioneGruppiApiClient, ProfilazioneUtenteHandler profilazioneUtenteHandler) {
		super(eventBus, view, proxy);
		this.sitemapMenu = sitemapMenu;
		this.amministrazioneGruppiApiClient = amministrazioneGruppiApiClient;
		this.profilazioneUtenteHandler = profilazioneUtenteHandler;
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

		getView().setDataProvider(new ListaAnagraficheGruppiProvider());

	}

	@Override
	protected void onHide() {
		super.onHide();
		getView().clearView();
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
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);

		if (profilazioneUtenteHandler.isAbilitato(AmministrazioneRuoliAbilitazione.class)) {
			revealInParent();
			sitemapMenu.setActiveVoce(VociRootSiteMap.ANAGRAFICA_GRUPPI.getId());
			ricerca(5, 0);

		} else {
			throw new IllegalArgumentException("Utente non amministratore");
		}

	}

	private Map<String, Object> getFiltriDiRicerca() {
		Map<String, Object> filtri = new HashMap<String, Object>();

		if (getView().getFiltroNome() != null && !getView().getFiltroNome().trim().isEmpty()) {
			filtri.put(ConfigurazioniUtil.FILTRO_RICERCA_RUOLI_ETICHETTA, getView().getFiltroNome());
		}

		if (getView().getFiltroLdap() != null && !getView().getFiltroLdap().trim().isEmpty()) {
			filtri.put(ConfigurazioniUtil.FILTRO_RICERCA_RUOLI_NOME, getView().getFiltroLdap());
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

		return filtri;
	}

	private void ricerca(Integer limit, final Integer offset) {
		ShowAppLoadingEvent.fire(ListaAnagraficaGruppiPresenter.this, true);

		String campoOrdinamento = getView().getCampoOrdinamento() != null ? getView().getCampoOrdinamento() : ConfigurazioniUtil.FILTRO_RICERCA_RUOLI_ETICHETTA;
		Boolean tipoOrdinamento = getView().getTipoOrdinamento() != null ? getView().getTipoOrdinamento() : Boolean.TRUE;

		amministrazioneGruppiApiClient.cerca(getFiltriDiRicerca(), limit, offset, campoOrdinamento, tipoOrdinamento, new AnagraficaRuoloCallback() {

			@Override
			public void onSuccess(AnagraficaRuoloResponse a) {/**/}

			@Override
			public void onSuccess(List<AnagraficaRuolo> a, int count) {
				ShowAppLoadingEvent.fire(ListaAnagraficaGruppiPresenter.this, false);
				getView().mostraAnagraficheGruppi(a, offset, count);
			}

			@Override
			public void onError(String error) {
				ShowAppLoadingEvent.fire(ListaAnagraficaGruppiPresenter.this, false);
				fireErrorMessageEvent(error);
			}
		});
	}

	private void fireErrorMessageEvent(String msg) {
		ShowMessageEvent event = new ShowMessageEvent();
		event.setErrorMessage(msg);
		getEventBus().fireEvent(event);
	}

	public class ListaAnagraficheGruppiProvider extends AsyncDataProvider<AnagraficaRuolo> {

		@Override
		protected void onRangeChanged(HasData<AnagraficaRuolo> display) {
			int start = display.getVisibleRange().getStart();
			int length = display.getVisibleRange().getLength();
			ricerca(length, start);
		}
	}
}
