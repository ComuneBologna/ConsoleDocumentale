package it.eng.portlet.consolepec.gwt.client.presenter.modulistica;

import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaFascicoloLoaded;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaModulisticaLoaded;
import it.eng.portlet.consolepec.gwt.client.event.ApertoDettaglioEvent;
import it.eng.portlet.consolepec.gwt.client.event.BackFromPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.event.ChiudiDettaglioAllegatoEvent;
import it.eng.portlet.consolepec.gwt.client.event.ChiudiDettaglioAllegatoEvent.ChiudiDettaglioAllegatoHandler;
import it.eng.portlet.consolepec.gwt.client.event.GoToPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.event.MostraDettaglioAllegatoEvent;
import it.eng.portlet.consolepec.gwt.client.event.RichiestaConfermaAnnullaEvent;
import it.eng.portlet.consolepec.gwt.client.event.SceltaConfermaAnnullaEvent;
import it.eng.portlet.consolepec.gwt.client.event.SceltaConfermaAnnullaEvent.SceltaConfermaAnnullaHandler;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.place.NameTokens;
import it.eng.portlet.consolepec.gwt.client.place.NameTokensParams;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter.Place;
import it.eng.portlet.consolepec.gwt.client.presenter.event.GoToAssegnaFromDettaglioPraticaModulisticaEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.modulistica.event.RilasciaInCaricoPraticaModulisticaEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.modulistica.event.RilasciaInCaricoPraticaModulisticaEvent.RilasciaInCaricoPraticaModulisticaHandler;
import it.eng.portlet.consolepec.gwt.client.widget.SitemapMenu;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.UriMapping;
import it.eng.portlet.consolepec.gwt.shared.action.GestionePresaInCaricoFascicoloAction;
import it.eng.portlet.consolepec.gwt.shared.action.GestionePresaInCaricoFascicoloActionResult;
import it.eng.portlet.consolepec.gwt.shared.action.modulistica.CambiaStatoPraticaModulistica;
import it.eng.portlet.consolepec.gwt.shared.action.modulistica.CambiaStatoPraticaModulisticaEnum;
import it.eng.portlet.consolepec.gwt.shared.action.modulistica.CambiaStatoPraticaModulisticaResult;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO.TipoPresaInCarico;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaModulisticaDTO;

import java.util.HashSet;
import java.util.Set;

import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

public class DettaglioPraticaModulisticaPresenter extends Presenter<DettaglioPraticaModulisticaPresenter.MyView, DettaglioPraticaModulisticaPresenter.MyProxy> implements SceltaConfermaAnnullaHandler, RilasciaInCaricoPraticaModulisticaHandler, ChiudiDettaglioAllegatoHandler {

	private final PecInPraticheDB praticheDb;

	private String praticaPath;
	private PraticaModulisticaDTO pratica;
	private final DispatchAsync dispatcher;

	private String eventId;
	private CambiaStatoPraticaModulistica cambiaStatoModulisticaEliminazione;
	private final SitemapMenu sitemapMenu;
	private final PlaceManager placeManager;

	@ProxyCodeSplit
	@NameToken(NameTokens.dettagliopraticamodulistica)
	public interface MyProxy extends ProxyPlace<DettaglioPraticaModulisticaPresenter> {
	}

	public interface MyView extends View {

		void mostraModulo(PraticaModulisticaDTO pratica);

		void setChiudiDettaglioCommand(Command chiudiDettaglioCommand);

		void setEliminaCommand(Command eliminaCommand);

		void setArchiviaCommand(Command archiviaCommand);

		void setRiportaInGestioneCommand(Command command);

		void setCreaFascicoloCommand(Command mostraCreaFascicoloFormCommand);

		void setRiassegnaCommand(Command riassegna);

		void setAggiungiPraticaAFascicoloCommand(Command riassegna);

		void setDettaglioFascicoloCommand(Command dettaglioFascicoloCommand);

		void setMostraDettaglioAllegatoCommand(it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, AllegatoDTO> mostraDettaglioAllegatoCommand);

		void setDownloadAllegatoCommand(it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, AllegatoDTO> downloadAllegatoCommand);

		void sendDownload(SafeUri uri);
	}

	@Inject
	public DettaglioPraticaModulisticaPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, final PecInPraticheDB pecInDb, final DispatchAsync dispatcher, final SitemapMenu sitemapMenu, final PlaceManager placeManager) {
		super(eventBus, view, proxy);
		this.praticheDb = pecInDb;
		this.dispatcher = dispatcher;
		this.sitemapMenu = sitemapMenu;
		this.placeManager = placeManager;

	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPresenter.TYPE_SetMainContent, this);
	}

	@Override
	protected void onHide() {
		super.onHide();

	}

	@Override
	protected void onBind() {
		super.onBind();
		getView().setEliminaCommand(new EliminaPraticaModulisticaCommand());
		getView().setArchiviaCommand(new CambioStatoCommand(CambiaStatoPraticaModulisticaEnum.ARCHIVIA));
		getView().setRiportaInGestioneCommand(new CambioStatoCommand(CambiaStatoPraticaModulisticaEnum.RIPORTAINGESTIONE));
		getView().setCreaFascicoloCommand(new CreaFascicoloCommand());
		getView().setRiassegnaCommand(new RiassegnaCommand());
		getView().setAggiungiPraticaAFascicoloCommand(new AggiungiPraticaAFascicoloEsistenteCommand());
		getView().setDettaglioFascicoloCommand(new DettaglioFascicoloCommand());
		getView().setMostraDettaglioAllegatoCommand(new MostraDettaglioAllegatoCommand());
		getView().setDownloadAllegatoCommand(new DownloadAllegatoCommand());

	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
		praticaPath = request.getParameter(NameTokensParams.idPratica, null);
		getView().setChiudiDettaglioCommand(new ChiudiDettaglioCommand());
		caricaPratica(praticaPath);
	}

	@Override
	protected void onReveal() {
		super.onReveal();
	}

	private void caricaPratica(String praticaPath) {
		ShowAppLoadingEvent.fire(DettaglioPraticaModulisticaPresenter.this, true);
		praticheDb.getPraticaModulisticaByPath(praticaPath, true, new PraticaModulisticaLoaded() {

			@Override
			public void onPraticaModulisticaLoaded(PraticaModulisticaDTO pratica) {
				ShowAppLoadingEvent.fire(DettaglioPraticaModulisticaPresenter.this, false);
				DettaglioPraticaModulisticaPresenter.this.pratica = pratica;
				getView().mostraModulo(pratica);
				ApertoDettaglioEvent.fire(DettaglioPraticaModulisticaPresenter.this, pratica);

			}

			@Override
			public void onPraticaModulisticaError(String error) {
				ShowAppLoadingEvent.fire(DettaglioPraticaModulisticaPresenter.this, false);
				ShowMessageEvent event = new ShowMessageEvent();
				event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
				getEventBus().fireEvent(event);
			}

		});
	}

	public class ChiudiDettaglioCommand implements com.google.gwt.user.client.Command {

		@Override
		public void execute() {
			praticheDb.remove(praticaPath);
			getEventBus().fireEvent(new BackFromPlaceEvent(praticaPath));
		}

	}
	
	public class EliminaPraticaModulisticaCommand implements com.google.gwt.user.client.Command {

		@Override
		public void execute() {

			ShowMessageEvent event = new ShowMessageEvent();
			event.setMessageDropped(true);
			getEventBus().fireEvent(event);

			Set<String> listId = new HashSet<String>();
			listId.add(praticaPath);
			praticheDb.remove(praticaPath); // aggiunto per ricaricare il fascicolo lato server dopo l'importazione allegati dalle email
			cambiaStatoModulisticaEliminazione = new CambiaStatoPraticaModulistica(listId, CambiaStatoPraticaModulisticaEnum.ELIMINATA);
			eventId = DOM.createUniqueId();
			RichiestaConfermaAnnullaEvent.fire(DettaglioPraticaModulisticaPresenter.this, "<h4>Procedere con l'eliminazione della pratica?<h4>", eventId);

		}

	}

	@Override
	@ProxyEvent
	public void onSceltaConfermaAnnulla(SceltaConfermaAnnullaEvent sceltaConfermaCancellazioneFascicoloEvent) {
		if (sceltaConfermaCancellazioneFascicoloEvent.isConfermato() && sceltaConfermaCancellazioneFascicoloEvent.getEventId().equals(eventId)) {
			ShowAppLoadingEvent.fire(DettaglioPraticaModulisticaPresenter.this, true);
			/* reset dei messaggi di errore */
			ShowMessageEvent showEventMessage = new ShowMessageEvent();
			showEventMessage.setMessageDropped(true);
			getEventBus().fireEvent(showEventMessage);
			dispatcher.execute(cambiaStatoModulisticaEliminazione, new AsyncCallback<CambiaStatoPraticaModulisticaResult>() {

				@Override
				public void onFailure(Throwable caught) {
					ShowAppLoadingEvent.fire(DettaglioPraticaModulisticaPresenter.this, false);
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
					getEventBus().fireEvent(event);
				}

				@Override
				public void onSuccess(CambiaStatoPraticaModulisticaResult result) {
					if (result.isError()) {
						ShowAppLoadingEvent.fire(DettaglioPraticaModulisticaPresenter.this, false);
						ShowMessageEvent event = new ShowMessageEvent();
						event.setErrorMessage(result.getErrorMsg());
						getEventBus().fireEvent(event);
					} else {
						ShowAppLoadingEvent.fire(DettaglioPraticaModulisticaPresenter.this, false);
						ShowMessageEvent event = new ShowMessageEvent();
						event.setMessageDropped(true);
						for (String id : result.getClientIdEliminati()) {
							praticheDb.remove(id);
						}
						ShowAppLoadingEvent.fire(DettaglioPraticaModulisticaPresenter.this, false);
						getEventBus().fireEvent(new BackFromPlaceEvent(praticaPath));
					}
				}
			});

		}
	}

	public class CambioStatoCommand implements com.google.gwt.user.client.Command {

		private CambiaStatoPraticaModulisticaEnum stato;

		public CambioStatoCommand(CambiaStatoPraticaModulisticaEnum stato) {
			super();
			this.stato = stato;
		}

		@Override
		public void execute() {
			ShowAppLoadingEvent.fire(DettaglioPraticaModulisticaPresenter.this, true);
			Set<String> set = new HashSet<String>();
			set.add(DettaglioPraticaModulisticaPresenter.this.praticaPath);

			/* reset dei messaggi di errore */
			ShowMessageEvent event = new ShowMessageEvent();
			event.setMessageDropped(true);
			getEventBus().fireEvent(event);

			DettaglioPraticaModulisticaPresenter.this.dispatcher.execute(new CambiaStatoPraticaModulistica(set, stato), new AsyncCallback<CambiaStatoPraticaModulisticaResult>() {

				@Override
				public void onFailure(Throwable caught) {
					ShowAppLoadingEvent.fire(DettaglioPraticaModulisticaPresenter.this, false);
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
					getEventBus().fireEvent(event);
				}

				@Override
				public void onSuccess(CambiaStatoPraticaModulisticaResult result) {
					ShowAppLoadingEvent.fire(DettaglioPraticaModulisticaPresenter.this, false);
					if (result.isError()) {
						ShowMessageEvent event = new ShowMessageEvent();
						event.setErrorMessage(result.getErrorMsg());
						getEventBus().fireEvent(event);
					} else {
						PraticaModulisticaDTO praticaModulistica = result.getPraticaModulistica();
						DettaglioPraticaModulisticaPresenter.this.praticheDb.update(praticaModulistica.getClientID(), praticaModulistica, sitemapMenu.containsLink(praticaModulistica.getClientID()));
						DettaglioPraticaModulisticaPresenter.this.getView().mostraModulo(praticaModulistica);
					}
				}
			});
		}

	}

	private class CreaFascicoloCommand extends PrendiInCaricoPraticaCommand {

		@Override
		public void handlePraticaInCarico(PraticaDTO pratica) {
			Place place = new Place();
			place.setToken(NameTokens.creafascicolo);
			place.addParam(NameTokensParams.idPratica, pratica.getClientID());
			place.addParam(NameTokensParams.creazioneDaTipoPratica, pratica.getTipologiaPratica().getNomeTipologia());
			place.addParam(NameTokensParams.svuotaCampi, Boolean.toString(true));
			getEventBus().fireEvent(new GoToPlaceEvent(place));
		}

	}

	private class AggiungiPraticaAFascicoloEsistenteCommand extends PrendiInCaricoPraticaCommand {

		@Override
		public void handlePraticaInCarico(PraticaDTO pratica) {
			Place place = new Place();
			place.setToken(NameTokens.sceltafascicolo);
			place.addParam(NameTokensParams.idPratica, pratica.getClientID());
			place.addParam(NameTokensParams.praticaModulistica, "true");
			getEventBus().fireEvent(new GoToPlaceEvent(place));
		}
	}

	private class RiassegnaCommand implements com.google.gwt.user.client.Command {

		@Override
		public void execute() {
			praticheDb.getPraticaModulisticaByPath(praticaPath, sitemapMenu.containsLink(praticaPath), new PraticaModulisticaLoaded() {

				@Override
				public void onPraticaModulisticaLoaded(PraticaModulisticaDTO pratica) {
					GoToAssegnaFromDettaglioPraticaModulisticaEvent event = new GoToAssegnaFromDettaglioPraticaModulisticaEvent(praticaPath);
					DettaglioPraticaModulisticaPresenter.this.getEventBus().fireEvent(event);
				}

				@Override
				public void onPraticaModulisticaError(String error) {
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
					DettaglioPraticaModulisticaPresenter.this.getEventBus().fireEvent(event);
				}
			});
		}

	}

	private abstract class PrendiInCaricoPraticaCommand implements Command {

		@Override
		public void execute() {

			// se arrivo a questo punto se la mail è già in carico a qualcuno è sempre in carico a me
			if (pratica.getTipoPresaInCarico().equals(TipoPresaInCarico.NESSUNO)) {

				GestionePresaInCaricoFascicoloAction action = new GestionePresaInCaricoFascicoloAction();
				action.setClientID(praticaPath);
				ShowAppLoadingEvent.fire(DettaglioPraticaModulisticaPresenter.this, true);
				/* reset dei messaggi di errore */
				ShowMessageEvent event = new ShowMessageEvent();
				event.setMessageDropped(true);
				getEventBus().fireEvent(event);
				dispatcher.execute(action, new AsyncCallback<GestionePresaInCaricoFascicoloActionResult>() {

					@Override
					public void onFailure(Throwable caught) {
						ShowAppLoadingEvent.fire(DettaglioPraticaModulisticaPresenter.this, false);
						ShowMessageEvent event = new ShowMessageEvent();
						event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
						getEventBus().fireEvent(event);
					}

					@Override
					public void onSuccess(GestionePresaInCaricoFascicoloActionResult result) {
						ShowAppLoadingEvent.fire(DettaglioPraticaModulisticaPresenter.this, false);
						if (!result.isError()) {
							ShowMessageEvent event = new ShowMessageEvent();
							event.setMessageDropped(true);
							getEventBus().fireEvent(event);
							praticheDb.update(praticaPath, result.getPraticaDTO(), true);
							handlePraticaInCarico(result.getPraticaDTO());
							ShowAppLoadingEvent.fire(DettaglioPraticaModulisticaPresenter.this, false);
						} else {
							ShowAppLoadingEvent.fire(DettaglioPraticaModulisticaPresenter.this, false);
							ShowMessageEvent event = new ShowMessageEvent();
							event.setErrorMessage(result.getErrorMsg());
							getEventBus().fireEvent(event);
						}
					}
				});
			} else
				handlePraticaInCarico(pratica);

		}

		public abstract void handlePraticaInCarico(PraticaDTO pratica);
	}

	@Override
	@ProxyEvent
	public void onChiudiDettaglioAllegato(ChiudiDettaglioAllegatoEvent event) {
		if (event.getClientID().equals(praticaPath) && placeManager.getCurrentPlaceRequest().getNameToken().equals(NameTokens.dettagliopraticamodulistica)) {
			ShowAppLoadingEvent.fire(this, false);
			placeManager.revealCurrentPlace();
		}
	}

	@Override
	@ProxyEvent
	public void onRilasciaInCaricoPraticaModulistica(RilasciaInCaricoPraticaModulisticaEvent event) {
		ShowAppLoadingEvent.fire(DettaglioPraticaModulisticaPresenter.this, true);
		final String idPratica = event.getIdPraticaModulistica();
		Set<String> listId = new HashSet<String>();
		listId.add(idPratica);
		CambiaStatoPraticaModulistica action = new CambiaStatoPraticaModulistica(listId, CambiaStatoPraticaModulisticaEnum.RILASCIA_IN_CARICO);
		dispatcher.execute(action, new AsyncCallback<CambiaStatoPraticaModulisticaResult>() {

			@Override
			public void onFailure(Throwable caught) {
				ShowAppLoadingEvent.fire(DettaglioPraticaModulisticaPresenter.this, false);
				ShowMessageEvent event = new ShowMessageEvent();
				event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
				getEventBus().fireEvent(event);
			}

			@Override
			public void onSuccess(CambiaStatoPraticaModulisticaResult result) {
				ShowAppLoadingEvent.fire(DettaglioPraticaModulisticaPresenter.this, false);
				if (!result.isError()) {
					praticheDb.remove(idPratica);
					getEventBus().fireEvent(new BackFromPlaceEvent());
				} else {
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(result.getErrorMsg());
					getEventBus().fireEvent(event);
				}
			}

		});
	}

	private class DettaglioFascicoloCommand implements Command {

		@Override
		public void execute() {
			String idFascicolo = pratica.getIdClientFascicolo();
			praticheDb.getFascicoloByPath(idFascicolo, false, new PraticaFascicoloLoaded() {

				@Override
				public void onPraticaLoaded(FascicoloDTO fascicolo) {
					if (fascicolo != null) {
						Place place = new Place();
						place.setToken(NameTokens.dettagliofascicolo);
						place.addParam(NameTokensParams.idPratica, fascicolo.getClientID());
						place.addParam(NameTokensParams.showActions, Boolean.FALSE.toString());
						getEventBus().fireEvent(new GoToPlaceEvent(place));
					}
				}

				@Override
				public void onPraticaError(String error) {
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
					getEventBus().fireEvent(event);
				}
			});

		}

	}

	private class MostraDettaglioAllegatoCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, AllegatoDTO> {

		@Override
		public Void exe(AllegatoDTO allegato) {
			MostraDettaglioAllegatoEvent.fire(DettaglioPraticaModulisticaPresenter.this, praticaPath, allegato);
			return null;
		}
	}

	private class DownloadAllegatoCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, AllegatoDTO> {

		@Override
		public Void exe(final AllegatoDTO allegato) {
			SafeUri uri = UriMapping.generaDownloadAllegatoServletURL(praticaPath, allegato);
			getView().sendDownload(uri);
			return null;
		}
	}
	
}
