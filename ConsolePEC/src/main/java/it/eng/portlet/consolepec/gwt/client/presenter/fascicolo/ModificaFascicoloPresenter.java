package it.eng.portlet.consolepec.gwt.client.presenter.fascicolo;

import java.util.Collections;
import java.util.List;

import com.google.common.base.Strings;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaFascicolo;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaFascicolo.ConfigurazioneEsecuzione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.CreazioneFascicoloAbilitazione;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo;
import it.eng.cobo.consolepec.util.generics.GenericsUtil;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaFascicoloLoaded;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.event.BackToFascicoloEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.event.MostraModificaFascicoloEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.event.MostraModificaFascicoloEvent.MostraModificaFascicoloHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.event.rubrica.MostraListaAnagraficheEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.event.rubrica.MostraListaAnagraficheEvent.MostraListaAnagraficheHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.event.rubrica.SelezionaAnagraficaFineEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.event.rubrica.SelezionaAnagraficaFineEvent.SelezionaAnagraficaFineHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command.BackToFascicoloCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command.IBackToFascicolo;
import it.eng.portlet.consolepec.gwt.client.widget.SitemapMenu;
import it.eng.portlet.consolepec.gwt.client.widget.TitoloLink;
import it.eng.portlet.consolepec.gwt.client.widget.datiaggiuntivi.FormDatiAggiuntiviWidget;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.ModificaFascicoloAction;
import it.eng.portlet.consolepec.gwt.shared.action.ModificaFascicoloResult;
import it.eng.portlet.consolepec.gwt.shared.action.ValidazioneDatiAggiuntivi;
import it.eng.portlet.consolepec.gwt.shared.action.ValidazioneDatiAggiuntiviResult;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.ValidazioneDatoAggiuntivoDTO;

/**
 * @author GiacomoFM
 * @since 17/lug/2017
 */
public final class ModificaFascicoloPresenter extends Presenter<ModificaFascicoloPresenter.MyView, ModificaFascicoloPresenter.MyProxy> implements IBackToFascicolo, MostraModificaFascicoloHandler, SelezionaAnagraficaFineHandler, MostraListaAnagraficheHandler {

	private static final String WARN_MESSAGE = "I campi in rosso devono essere valorizzati correttamente";
	private static final String NO_CHANGE_MESSAGE = "Nessun campo &egrave; stato modificato";

	private PecInPraticheDB praticheDB;
	private DispatchAsync dispatchAsync;
	private PlaceManager placeManager;
	private String pathFascicolo;
	private SitemapMenu sitemapMenu;
	private ProfilazioneUtenteHandler profilazioneUtenteHandler;
	private ConfigurazioniHandler configurazioniHandler;
	private WaitRecuperaGruppiMostraFascicoloAction waitAction = new WaitRecuperaGruppiMostraFascicoloAction();

	@Inject
	public ModificaFascicoloPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, final PecInPraticheDB praticheDB, final DispatchAsync dispatchAsync,
			final PlaceManager placeManager, final SitemapMenu sitemapMenu, final ProfilazioneUtenteHandler profilazioneUtenteHandler, ConfigurazioniHandler configurazioniHandler) {
		super(eventBus, view, proxy);
		this.praticheDB = praticheDB;
		this.dispatchAsync = dispatchAsync;
		this.placeManager = placeManager;
		this.sitemapMenu = sitemapMenu;
		this.profilazioneUtenteHandler = profilazioneUtenteHandler;
		this.configurazioniHandler = configurazioniHandler;

	}

	public interface MyView extends View {
		FascicoloDTO getFascicolo();

		String getTitolo();

		String getTipoFascicolo();

		List<DatoAggiuntivo> getValoriDatiAggiuntivi();

		boolean isModificaEseguita();

		boolean controlloClientDatiAggiuntivi();

		boolean controlloServerDatiAggiuntivi(List<ValidazioneDatoAggiuntivoDTO> validazione);

		void setRequiredField();

		void setTipologieFascicolo(List<AnagraficaFascicolo> tipologie);

		void mostraFascicolo(FascicoloDTO fascicolo);

		void setAnnullaCommand(Command annullaCommand);

		void setConfermaCommand(Command confermaCommand);

		void loadFormDatiAggiuntivi(EventBus eventBus, Object openingRequestor, DispatchAsync dispatcher);

		FormDatiAggiuntiviWidget getFormDatiAggiuntivi();
	}

	@ProxyCodeSplit
	public interface MyProxy extends Proxy<ModificaFascicoloPresenter> {
		//
	}

	@Override
	public EventBus _getEventBus() {
		return getEventBus();
	}

	@Override
	public DispatchAsync getDispatchAsync() {
		return dispatchAsync;
	}

	@Override
	public PlaceManager getPlaceManager() {
		return placeManager;
	}

	@Override
	public PecInPraticheDB getPecInPraticheDB() {
		return praticheDB;
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPresenter.TYPE_SetMainContent, this);
	}

	@Override
	@ProxyEvent
	public void onMostraModificaFascicolo(MostraModificaFascicoloEvent event) {
		pathFascicolo = event.getPathFascicolo();
		praticheDB.getFascicoloByPath(pathFascicolo, false, new PraticaFascicoloLoaded() {

			@Override
			public void onPraticaLoaded(FascicoloDTO fascicolo) {
				waitAction.mostraFascicolo(fascicolo);
				revealInParent();
			}

			@Override
			public void onPraticaError(String error) {
				ShowAppLoadingEvent.fire(ModificaFascicoloPresenter.this, false);
				fireEvent(ConsolePecConstants.ERROR_MESSAGE);
			}
		});
	}

	@Override
	public String getFascicoloPath() {
		return pathFascicolo;
	}

	@Override
	protected void onBind() {
		super.onBind();
		getView().loadFormDatiAggiuntivi(getEventBus(), this, dispatchAsync);
		getView().setAnnullaCommand(new BackToFascicoloCommand<ModificaFascicoloPresenter>(this));
		getView().setConfermaCommand(new ConfermaCommand());
		waitAction.setTipologieFascicolo(configurazioniHandler.filtraFascicoloPersonale(profilazioneUtenteHandler.getAnagraficheFascicoliAbilitati(CreazioneFascicoloAbilitazione.class)));
	}

	@Override
	protected void onReveal() {
		super.onReveal();
	}

	private class ConfermaCommand implements Command {
		@Override
		public void execute() {
			if (!getView().isModificaEseguita()) {
				fireEvent(NO_CHANGE_MESSAGE);
				return;
			}
			if (Strings.isNullOrEmpty(getView().getTitolo()) || !getView().controlloClientDatiAggiuntivi()) {
				getView().setRequiredField();
				fireEvent(WARN_MESSAGE);
				return;
			}

			ValidazioneDatiAggiuntivi validazioneDatiAggiuntivi = new ValidazioneDatiAggiuntivi(getView().getValoriDatiAggiuntivi());
			dispatchAsync.execute(validazioneDatiAggiuntivi, new AsyncCallback<ValidazioneDatiAggiuntiviResult>() {
				@Override
				public void onFailure(Throwable caught) {
					ShowAppLoadingEvent.fire(ModificaFascicoloPresenter.this, false);
					fireEvent(ConsolePecConstants.ERROR_MESSAGE);
				}

				@Override
				public void onSuccess(ValidazioneDatiAggiuntiviResult result) {
					ShowAppLoadingEvent.fire(ModificaFascicoloPresenter.this, false);

					if (result.getError()) {
						fireEvent(result.getMessError());

					} else if (!result.getErroriDaVisualizzare().isEmpty()) {
						getView().controlloServerDatiAggiuntivi(result.getValidazioneDatiAggiuntivi());
						fireEvent(GenericsUtil.format(result.getErroriDaVisualizzare()));

					} else if (getView().controlloServerDatiAggiuntivi(result.getValidazioneDatiAggiuntivi())) {
						ModificaFascicoloAction action = new ModificaFascicoloAction(getView().getFascicolo(), getView().getTitolo(), getView().getTipoFascicolo(),
								getView().getValoriDatiAggiuntivi());
						ShowAppLoadingEvent.fire(ModificaFascicoloPresenter.this, true);
						dispatchAsync.execute(action, new ModificaFascicoloExecution());
					}
				}
			});

		}
	}

	private class ModificaFascicoloExecution implements AsyncCallback<ModificaFascicoloResult> {
		@Override
		public void onFailure(Throwable t) {
			fireEvent(ConsolePecConstants.ERROR_MESSAGE);
			ShowAppLoadingEvent.fire(ModificaFascicoloPresenter.this, false);
		}

		@Override
		public void onSuccess(ModificaFascicoloResult result) {
			ShowAppLoadingEvent.fire(ModificaFascicoloPresenter.this, false);
			if (result.isError()) {
				fireEvent(result.getMsgError());
			} else {
				praticheDB.remove(pathFascicolo);
				sitemapMenu.setTitoloPratica(pathFascicolo, new TitoloLink(result.getPratica().getTitolo()));
				getEventBus().fireEvent(new BackToFascicoloEvent(pathFascicolo));
			}
		}
	}

	private void fireEvent(String msg) {
		ShowMessageEvent event = new ShowMessageEvent();
		event.setErrorMessage(msg);
		getEventBus().fireEvent(event);
	}

	private class WaitRecuperaGruppiMostraFascicoloAction {
		private boolean gruppiCaricati = false;
		private FascicoloDTO fascicoloCaricatoInDifferita = null;

		public void mostraFascicolo(FascicoloDTO fascicolo) {
			if (gruppiCaricati) {
				getView().mostraFascicolo(fascicolo);
			} else {
				this.fascicoloCaricatoInDifferita = fascicolo;
			}
		}

		public void setTipologieFascicolo(List<AnagraficaFascicolo> tipologieFascicolo) {
			getView().setTipologieFascicolo(tipologieFascicolo);
			gruppiCaricati = true;
			if (fascicoloCaricatoInDifferita != null) {
				getView().mostraFascicolo(fascicoloCaricatoInDifferita);
				fascicoloCaricatoInDifferita = null;
			}
		}
	}

	@Override
	@ProxyEvent
	public void onAnagraficaSelezionata(SelezionaAnagraficaFineEvent event) {
		if (this.equals(event.getOpeningRequestor())) {
			if (!event.isAnnulla()) {
				List<ConfigurazioneEsecuzione> esecuzioni = Collections.emptyList();
				AnagraficaFascicolo af = configurazioniHandler.getAnagraficaFascicolo(getView().getTipoFascicolo());
				if (af != null) {
					esecuzioni = af.getConfigurazioneEsecuzioni();
				}
				getView().getFormDatiAggiuntivi().setAnagrafica(event.getNomeDatoAggiuntivo(), event.getAnagrafica(), esecuzioni);
			}
			revealInParent();
		}
	}

	@Override
	@ProxyEvent
	public void onMostraListaAnagrafiche(MostraListaAnagraficheEvent event) {
		if (this.equals(event.getOpeningRequestor())) {
			revealInParent();
		}
	}

}
