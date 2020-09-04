package it.eng.portlet.consolepec.gwt.client.presenter;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;

import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.util.pratica.PraticaUtil;
import it.eng.portlet.consolepec.gwt.client.command.RicercaPraticheServerAdapter;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaLoaded;
import it.eng.portlet.consolepec.gwt.client.event.ApertoDettaglioEvent;
import it.eng.portlet.consolepec.gwt.client.event.ChiudiDettaglioAllegatoEvent;
import it.eng.portlet.consolepec.gwt.client.event.ChiudiDettaglioAllegatoEvent.ChiudiDettaglioAllegatoHandler;
import it.eng.portlet.consolepec.gwt.client.event.GoToPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.event.MostraDettaglioAllegatoEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.place.NameTokens;
import it.eng.portlet.consolepec.gwt.client.place.NameTokensParams;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter.Place;
import it.eng.portlet.consolepec.gwt.client.presenter.event.urbanistica.MostraListaPraticaProcediEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.event.urbanistica.MostraListaPraticaProcediEvent.MostraListaPraticaProcedHandler;
import it.eng.portlet.consolepec.gwt.client.widget.ElementoComunicazioneElencoWidget.MostraComunicazione;
import it.eng.portlet.consolepec.gwt.client.widget.ElementoPECElencoWidget.MostraPEC;
import it.eng.portlet.consolepec.gwt.client.widget.ElementoPraticaModulisticaElencoWidget.MostraPraticaModulistica;
import it.eng.portlet.consolepec.gwt.client.widget.SitemapMenu;
import it.eng.portlet.consolepec.gwt.client.worklist.RicercaLiberaStrategy;
import it.eng.portlet.consolepec.gwt.client.worklist.RigaEspansaComunicazioneStrategy;
import it.eng.portlet.consolepec.gwt.client.worklist.RigaEspansaFascicoloGenericoStrategy;
import it.eng.portlet.consolepec.gwt.client.worklist.RigaEspansaPecInStrategy;
import it.eng.portlet.consolepec.gwt.client.worklist.RigaEspansaPecOutStrategy;
import it.eng.portlet.consolepec.gwt.client.worklist.RigaEspansaPerTipoStrategy;
import it.eng.portlet.consolepec.gwt.client.worklist.RigaEspansaPraticaModulisticaStrategy;
import it.eng.portlet.consolepec.gwt.client.worklist.RigaEspansaTemplateStrategy;
import it.eng.portlet.consolepec.gwt.client.worklist.WorklistStrategy;
import it.eng.portlet.consolepec.gwt.client.worklist.WorklistStrategy.EspandiRigaEventListener;
import it.eng.portlet.consolepec.gwt.client.worklist.WorklistStrategy.RicercaCallback;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants.VociRootSiteMap;
import it.eng.portlet.consolepec.gwt.shared.UriMapping;
import it.eng.portlet.consolepec.gwt.shared.action.CercaPratiche;
import it.eng.portlet.consolepec.gwt.shared.action.pec.TipoRiferimentoPEC;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.ColonnaWorklist;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;

public class RicercaLiberaPresenter extends Presenter<RicercaLiberaPresenter.MyView, RicercaLiberaPresenter.MyProxy> implements ChiudiDettaglioAllegatoHandler, MostraListaPraticaProcedHandler {

	public interface MyView extends View {

		public void init(WorklistStrategy worklistStrategy, Command cercaPraticheCommand);

		public Set<ConstraintViolation<CercaPratiche>> formRicercaToCercaPratiche(CercaPratiche action);

		public List<PraticaDTO> getRigheEspanse();

		public void espandiRiga(String rowAlfrescoPath, PraticaDTO pratica);

		public void nascondiRiga(String rowAlfrescoPath);

		public void aggiornaRiga(PraticaDTO pratica);

		public void sendDownload(SafeUri uri);

		public void updateRigheEspanse();

		public void resetSelezioni();

		public void resetRicerca();

		public void updateRigheSelezionate();

		public void initDatiRicercaProtocollazione();

		public void setRicercaLiberaBloccata(boolean bloccata);

		public void setTipoPratiche(List<TipologiaPratica> tipi);

		public void setGruppiAbilitati(List<AnagraficaRuolo> set);

		public void setTipiPraticaGestite(List<TipologiaPratica> tipi);

	}

	@ProxyCodeSplit
	@NameToken(NameTokens.ricercalibera)
	public interface MyProxy extends ProxyPlace<RicercaLiberaPresenter> {/**/}

	private RicercaLiberaStrategy strategy;
	private final PecInPraticheDB pecInDb;
	private final PlaceManager placeManager;
	private final RicercaPraticheServerAdapter ricercaAdapter;
	private final SitemapMenu sitemapMenu;
	private boolean firstReveal = true;
	private RicercaCommand ricercaCommand;
	private ProfilazioneUtenteHandler profilazioneUtenteHandler;

	private List<TipologiaPratica> all;

	@Inject
	public RicercaLiberaPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, final PecInPraticheDB pecInDb, final RicercaPraticheServerAdapter ricercaAdapter,
			final SitemapMenu sitemapMenu, final PlaceManager placeManager, ConfigurazioniHandler configurazioniHandler, ProfilazioneUtenteHandler profilazioneUtenteHandler) {
		super(eventBus, view, proxy);

		all = PraticaUtil.toTipologiePratiche(configurazioniHandler.getAnagraficheFascicoli(true), configurazioniHandler.getAnagraficheIngressi(true),
				configurazioniHandler.getAnagraficheMailInUscita(true), configurazioniHandler.getAnagraficheComunicazioni(true), configurazioniHandler.getAnagrafichePraticaModulistica(true),
				configurazioniHandler.getAnagraficheModelli(true));

		this.pecInDb = pecInDb;
		this.placeManager = placeManager;
		this.ricercaAdapter = ricercaAdapter;
		this.sitemapMenu = sitemapMenu;
		this.profilazioneUtenteHandler = profilazioneUtenteHandler;
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPresenter.TYPE_SetMainContent, this);
	}

	@Override
	protected void onBind() {
		super.onBind();

	}

	@Override
	protected void onReveal() {
		super.onReveal();
		if (firstReveal) {

			firstReveal = false;

			RigaEspansaPerTipoStrategy perTipoStrategy = new RigaEspansaPerTipoStrategy();

			for (TipologiaPratica tp : all) {

				if (PraticaUtil.isFascicolo(tp)) {
					perTipoStrategy.addStrategy(tp, new RigaEspansaFascicoloGenericoStrategy(pecInDb, new DownloadAllegatoCommand(), new MostraDettaglioAllegatoCommand(),
							new MostraDettaglioPECDaFascicoloCommand(), new MostraDettaglioPraticaModulisticaDaFascicoloCommand(), new MostraDettaglioComunicazioneDaFascicoloCommand(), sitemapMenu));

				} else if (PraticaUtil.isEmailOut(tp)) {
					perTipoStrategy.addStrategy(tp, new RigaEspansaPecOutStrategy(new MostraDettaglioPecOutCommand(), new DownloadAllegatoCommand(), new MostraDettaglioAllegatoCommand()));

				} else if (PraticaUtil.isIngresso(tp)) {
					perTipoStrategy.addStrategy(tp,
							new RigaEspansaPecInStrategy(new MostraDettaglioPecInCommand(), null, null, null, new DownloadAllegatoCommand(), new MostraDettaglioAllegatoCommand()));

				} else if (PraticaUtil.isPraticaModulistica(tp)) {
					perTipoStrategy.addStrategy(tp, new RigaEspansaPraticaModulisticaStrategy());
				} else if (PraticaUtil.isModello(tp)) {
					perTipoStrategy.addStrategy(tp, new RigaEspansaTemplateStrategy());
				} else if (PraticaUtil.isComunicazione(tp)) {
					perTipoStrategy.addStrategy(tp, new RigaEspansaComunicazioneStrategy());
				}
			}

			strategy = new RicercaLiberaStrategy();
			strategy.setRigaEspansaStrategy(perTipoStrategy);
			strategy.addEspandiRigaEventListener(new EspandiRigaEvent());
			strategy.setRicercaEventListener(new RicercaEventListener());
			strategy.setPraticheDB(pecInDb);
			strategy.setSitemapMenu(sitemapMenu);
			strategy.setEventBus(getEventBus());

			ricercaCommand = new RicercaCommand(getEventBus(), strategy, getView().formRicercaToCercaPratiche(new CercaPratiche()));
			getView().init(strategy, ricercaCommand);
		}

		getView().updateRigheEspanse();
		getView().updateRigheSelezionate();
		getView().resetSelezioni();
		sitemapMenu.setActiveVoce(VociRootSiteMap.RICERCA_LIBERA.getId());
		getView().setTipiPraticaGestite(all);
		getView().setGruppiAbilitati(profilazioneUtenteHandler.getAllAnagraficheRuoliSubordinati());

		if (!firstReveal && ricercaCommand != null) {
			ricercaCommand.refreshDatiGrid();
		}
	}

	private void mostraDettaglio(String clientID) {
		Place place = new Place();
		place.setToken(NameTokens.dettagliofascicolo);
		place.addParam(NameTokensParams.idPratica, clientID);
		place.addParam(NameTokensParams.resetComposizioneFascicolo, Boolean.toString(true));
		getEventBus().fireEvent(new GoToPlaceEvent(place));
	}

	@Override
	@ProxyEvent
	public void onChiudiDettaglioAllegato(ChiudiDettaglioAllegatoEvent event) {
		if (placeManager.getCurrentPlaceRequest().getNameToken().equals(NameTokens.ricercalibera)) {
			ShowAppLoadingEvent.fire(this, false);
			placeManager.revealCurrentPlace();
		}
	}

	/* Classi interne */

	private class RicercaEventListener implements WorklistStrategy.RicercaEventListener {

		@Override
		public void onStartRicerca(int start, int length, ColonnaWorklist campoOrdinamento, boolean asc, RicercaCallback callback) {
			CercaPratiche action = new CercaPratiche();
			action.setFine(start + length);
			action.setInizio(start);
			action.setCampoOrdinamento(campoOrdinamento);
			action.setOrdinamentoAsc(asc);
			action.setSoloWorklist(false);
			action.setFiltroGruppiVisibilita(true);
			action.setFiltroAbilitazioni(true);
			action.setSuperutente(true);
			Set<ConstraintViolation<CercaPratiche>> violations = getView().formRicercaToCercaPratiche(action);
			if (violations.size() == 0) {
				ricercaAdapter.startRicerca(action, callback);
			}
		}

	}

	public class DownloadAllegatoCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, AllegatoDTO> {

		@Override
		public Void exe(final AllegatoDTO allegato) {
			SafeUri uri = UriMapping.generaDownloadAllegatoServletURL(allegato.getClientID(), allegato);
			getView().sendDownload(uri);
			return null;
		}
	}

	private class MostraDettaglioAllegatoCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, AllegatoDTO> {

		@Override
		public Void exe(AllegatoDTO allegato) {
			MostraDettaglioAllegatoEvent.fire(RicercaLiberaPresenter.this, allegato.getClientID(), allegato);
			return null;
		}

	}

	private class MostraDettaglioPECDaFascicoloCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, MostraPEC> {

		@Override
		public Void exe(MostraPEC m) {
			String nameToken = null;
			if (m.getTipo().equals(TipoRiferimentoPEC.IN)) {
				nameToken = NameTokens.dettagliopecin;
			} else {
				nameToken = NameTokens.dettagliopecout;
			}
			Place place = new Place();
			place.setToken(nameToken);
			place.addParam(NameTokensParams.idPratica, m.getClientID());
			getEventBus().fireEvent(new GoToPlaceEvent(place));
			return null;
		}

	}

	private class MostraDettaglioPraticaModulisticaDaFascicoloCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, MostraPraticaModulistica> {

		@Override
		public Void exe(MostraPraticaModulistica m) {
			Place place = new Place();
			place.setToken(NameTokens.dettagliopraticamodulistica);
			place.addParam(NameTokensParams.idPratica, m.getClientID());
			getEventBus().fireEvent(new GoToPlaceEvent(place));
			return null;
		}

	}

	private class MostraDettaglioComunicazioneDaFascicoloCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, MostraComunicazione> {

		@Override
		public Void exe(MostraComunicazione c) {
			Place place = new Place();
			place.setToken(NameTokens.dettagliocomunicazione);
			place.addParam(NameTokensParams.idPratica, c.getClientID());
			getEventBus().fireEvent(new GoToPlaceEvent(place));
			return null;
		}

	}

	private class MostraDettaglioPecInCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, String> {

		@Override
		public Void exe(String idPratica) {
			MainPresenter.Place place = new MainPresenter.Place();
			place.setToken(NameTokens.dettagliopecin);
			place.addParam(NameTokensParams.idPratica, idPratica);
			GoToPlaceEvent goToPlaceEvent = new GoToPlaceEvent(place);
			getEventBus().fireEvent(goToPlaceEvent);
			return null;
		}
	}

	private class MostraDettaglioPecOutCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, String> {

		@Override
		public Void exe(String idPratica) {
			MainPresenter.Place place = new MainPresenter.Place();
			place.setToken(NameTokens.dettagliopecout);
			place.addParam(NameTokensParams.idPratica, idPratica);
			GoToPlaceEvent goToPlaceEvent = new GoToPlaceEvent(place);
			getEventBus().fireEvent(goToPlaceEvent);
			return null;
		}
	}

	private class EspandiRigaEvent implements EspandiRigaEventListener {

		@Override
		public void onEspandiRiga(final String clientID, TipologiaPratica tipologiaPratica, final boolean isEspansa) {
			int clientWidth = Window.getClientWidth();
			if (clientWidth > ConsolePecConstants.MIN_DESKTOP_WIDTH_PIXELS) {
				if (!isEspansa) {
					pecInDb.getPraticaByPathETipo(clientID, tipologiaPratica, isEspansa, new PraticaLoaded() {

						@Override
						public void onPraticaLoaded(PraticaDTO pratica) {
							pecInDb.insertOrUpdate(clientID, pratica, isEspansa);
							ApertoDettaglioEvent.fire(RicercaLiberaPresenter.this, pratica);
							getView().espandiRiga(clientID, pratica);

						}

						@Override
						public void onPraticaError(String error) {/**/}
					});

				} else {
					getView().nascondiRiga(clientID);
				}
			} else {
				// mobile
				RicercaLiberaPresenter.this.mostraDettaglio(clientID);
			}

		}

	}

	@Override
	@ProxyEvent
	public void onMostraListaPraticaProced(MostraListaPraticaProcediEvent event) {
		if (event.getNameClass().equals(RicercaLiberaPresenter.class.getName())) {
			revealInParent();
		}
	}

}
