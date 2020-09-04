package it.eng.portlet.consolepec.gwt.client.presenter.pec;

import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.Command;
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

import it.eng.portlet.consolepec.gwt.client.command.ConsolePecCommandBinder;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaEmaiInlLoaded;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaLoaded;
import it.eng.portlet.consolepec.gwt.client.event.ApertoDettaglioEvent;
import it.eng.portlet.consolepec.gwt.client.event.BackFromPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.event.ChiudiDettaglioAllegatoEvent;
import it.eng.portlet.consolepec.gwt.client.event.ChiudiDettaglioAllegatoEvent.ChiudiDettaglioAllegatoHandler;
import it.eng.portlet.consolepec.gwt.client.event.GoToPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.event.MostraDettaglioAllegatoEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.event.UpdateSiteMapEvent;
import it.eng.portlet.consolepec.gwt.client.place.NameTokens;
import it.eng.portlet.consolepec.gwt.client.place.NameTokensParams;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter.Place;
import it.eng.portlet.consolepec.gwt.client.presenter.event.GoToAssegnaFromDettaglioPecInEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.pec.command.AnnullaElettoraleCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.pec.command.ImportaElettoraleCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.pec.command.ModificaOperatoreCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.pec.event.BackToDettaglioPecInEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.pec.event.BackToDettaglioPecInEvent.BackToDettaglioPecInHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.pec.event.RilasciaInCaricoPecInEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.pec.event.RilasciaInCaricoPecInEvent.RilasciaInCaricoPecInHandler;
import it.eng.portlet.consolepec.gwt.client.util.GestioneLinkSiteMapUtil;
import it.eng.portlet.consolepec.gwt.client.widget.SitemapMenu;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.UriMapping;
import it.eng.portlet.consolepec.gwt.shared.action.GestionePresaInCaricoFascicoloAction;
import it.eng.portlet.consolepec.gwt.shared.action.GestionePresaInCaricoFascicoloActionResult;
import it.eng.portlet.consolepec.gwt.shared.action.RiportaInLettura;
import it.eng.portlet.consolepec.gwt.shared.action.RiportaInLetturaResult;
import it.eng.portlet.consolepec.gwt.shared.action.SalvaNotePecInAction;
import it.eng.portlet.consolepec.gwt.shared.action.SalvaNotePecInResult;
import it.eng.portlet.consolepec.gwt.shared.action.pec.CambiaStatoPecInAction;
import it.eng.portlet.consolepec.gwt.shared.action.pec.CambiaStatoPecInAction.Azione;
import it.eng.portlet.consolepec.gwt.shared.action.pec.CambiaStatoPecInActionResult;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PecInDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO.TipoPresaInCarico;

public class DettaglioPecInPresenter extends Presenter<DettaglioPecInPresenter.MyView, DettaglioPecInPresenter.MyProxy> implements ChiudiDettaglioAllegatoHandler, RilasciaInCaricoPecInHandler, BackToDettaglioPecInHandler, ConsolePecCommandBinder {

	public interface MyView extends View {

		void setChiudiDettaglioCommand(Command chiudiDettaglioCommand);

		void mostraPratica(PecInDTO doc);

		void setDownloadAllegatoCommand(it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, AllegatoDTO> downloadAllegatoCommand);

		void sendDownload(SafeUri uri);

		void setMostraCreaFascicoloFormCommand(Command mostraTipoProtocollazioneCommand);

		void setMostraGruppiCommand(Command mostraGruppiCommand);

		void setEliminaCommand(Command eliminaCommand);

		void setArchiviaCommand(Command archiviaCommand);

		void setRiportaInGestione(Command riportaInGestioneCommand);

		void setMostraDettaglioAllegatoCommand(it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, AllegatoDTO> mostraDettaglioAllegatoCommand);

		void setAggiungiPecInAFascicoloEsistenteCommand(Command command);

		void setGotoPraticaCommand(GoToPraticaCommand gotoPraticaCommand);

		void resetDisclosurePanels(boolean showActions);

		void setSalvaPECCommand(Command salvaPECCommand);

		void setAnnullaSalvaPECCommand(Command annullaSalvaPECCommand);

		PecInDTO getPecIn();

		void setRiportaInLetturaCommand(Command riportaInLettura);

		void setImportaElettoraleCommand(ImportaElettoraleCommand importaElettoraleCommand);

		void addPraticaCollegataSection(PraticaDTO pratica);

		void buildPanelPraticheCollegate();

		void setAnnullaElettoraleCommand(AnnullaElettoraleCommand annullaElettoraleCommand);

		void setModificaOperatoreCommand(Command command);

		String getNote();

	}

	@ProxyCodeSplit
	@NameToken(NameTokens.dettagliopecin)
	public interface MyProxy extends ProxyPlace<DettaglioPecInPresenter> {}

	private final EventBus eventBus;
	private final DispatchAsync dispatcher;
	private String pecInPath;
	private final PecInPraticheDB praticheDB;
	private final SitemapMenu siteMap;
	private PecInDTO pecInDTO;
	private final GestioneLinkSiteMapUtil gestioneLinkSiteMapUtil;
	private final PlaceManager placeManager;

	@Inject
	public DettaglioPecInPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, final DispatchAsync dispatcher, PecInPraticheDB db, SitemapMenu sitemap,
			final GestioneLinkSiteMapUtil gestioneLinkSiteMapUtil, final PlaceManager placeManager) {
		super(eventBus, view, proxy);
		this.eventBus = eventBus;
		this.dispatcher = dispatcher;
		this.praticheDB = db;
		this.siteMap = sitemap;
		this.placeManager = placeManager;
		this.gestioneLinkSiteMapUtil = gestioneLinkSiteMapUtil;
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPresenter.TYPE_SetMainContent, this);
	}

	@Override
	protected void onBind() {
		super.onBind();
		getView().setDownloadAllegatoCommand(new DownloadAllegatoCommand());
		getView().setMostraCreaFascicoloFormCommand(new CreaFascicoloCommand());
		getView().setMostraGruppiCommand(new MostraGruppiCommand());
		getView().setEliminaCommand(new EliminaCommand());
		getView().setArchiviaCommand(new ArchiviaCommand());
		getView().setRiportaInGestione(new RiportaInGestioneCommand());
		getView().setGotoPraticaCommand(new GoToPraticaCommand());
		getView().setMostraDettaglioAllegatoCommand(new MostraDettaglioAllegatoCommand());
		getView().setAggiungiPecInAFascicoloEsistenteCommand(new AggiungiPecInAFascicoloEsistenteCommand());
		getView().setSalvaPECCommand(new SalvaPECCommand());
		getView().setAnnullaSalvaPECCommand(new AnnullaSalvaPECCommand());
		getView().setRiportaInLetturaCommand(new RiportaInLetturaCommand());
		getView().setImportaElettoraleCommand(new ImportaElettoraleCommand(this));
		getView().setAnnullaElettoraleCommand(new AnnullaElettoraleCommand(this));
		getView().setModificaOperatoreCommand(new ModificaOperatoreCommand(this));
	}

	@Override
	protected void onHide() {
		super.onHide();
	}

	@Override
	protected void onReset() {
		super.onReset();
	}

	@Override
	protected void onUnbind() {
		super.onUnbind();
	}

	@Override
	protected void onReveal() {
		super.onReveal();
		revealInParent();
	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);

		pecInPath = request.getParameter(NameTokensParams.idPratica, null);

		boolean showActions = Boolean.parseBoolean(request.getParameter(NameTokensParams.showActions, "false"));
		getView().resetDisclosurePanels(showActions);
		getView().setChiudiDettaglioCommand(new ChiudiDettaglioCommand());

		mostraPratica();
	}

	public void mostraPratica() {
		praticheDB.remove(pecInPath);
		ShowAppLoadingEvent.fire(DettaglioPecInPresenter.this, true);
		praticheDB.getPecInByPath(pecInPath, siteMap.containsLink(pecInPath), new PraticaEmaiInlLoaded() {

			@Override
			public void onPraticaLoaded(final PecInDTO pec) {
				ShowAppLoadingEvent.fire(DettaglioPecInPresenter.this, false);
				pecInDTO = pec;
				gestioneLinkSiteMapUtil.aggiungiLinkInLavorazione(pec);
				getView().mostraPratica(pec);
				ApertoDettaglioEvent.fire(DettaglioPecInPresenter.this, pec);

				if (pec.getIdPraticheCollegate().size() > 0) {
					for (String idPratica : pec.getIdPraticheCollegate()) {
						praticheDB.getPraticaByPath(idPratica, true, new PraticaLoaded() {

							@Override
							public void onPraticaLoaded(PraticaDTO pratica) {
								getView().addPraticaCollegataSection(pratica);
							}

							@Override
							public void onPraticaError(String error) {
								ShowMessageEvent event = new ShowMessageEvent();
								event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
								eventBus.fireEvent(event);
							}

						});
					}
					getView().buildPanelPraticheCollegate();
				}
			}

			@Override
			public void onPraticaError(String error) {
				ShowAppLoadingEvent.fire(DettaglioPecInPresenter.this, false);
				ShowMessageEvent event = new ShowMessageEvent();
				event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
				eventBus.fireEvent(event);
			}
		});
	}

	private void executeCambiaStato(CambiaStatoPecInAction action) {
		/* reset dei messaggi di errore */
		ShowMessageEvent event = new ShowMessageEvent();
		event.setMessageDropped(true);
		eventBus.fireEvent(event);
		this.dispatcher.execute(action, new AsyncCallback<CambiaStatoPecInActionResult>() {

			@Override
			public void onSuccess(CambiaStatoPecInActionResult result) {
				if (!result.getIsError()) {
					PecInDTO pecInDTO = result.getDettagliRighe().get(0);
					praticheDB.insertOrUpdate(pecInPath, pecInDTO, siteMap.containsLink(pecInPath));
					DettaglioPecInPresenter.this.pecInDTO = pecInDTO;
					getView().mostraPratica(pecInDTO);
				} else {
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(result.getMessErr());
					eventBus.fireEvent(event);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				ShowMessageEvent event = new ShowMessageEvent();
				event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
				eventBus.fireEvent(event);
			}
		});

	}

	@Override
	@ProxyEvent
	public void onChiudiDettaglioAllegato(ChiudiDettaglioAllegatoEvent event) {
		if (event.getClientID().equals(pecInPath) && placeManager.getCurrentPlaceRequest().getNameToken().equals(NameTokens.dettagliopecin)) {
			ShowAppLoadingEvent.fire(this, false);
			placeManager.revealCurrentPlace();
		}
	}

	private class ChiudiDettaglioCommand implements com.google.gwt.user.client.Command {

		@Override
		public void execute() {
			// praticheDB.remove(pecInPath); // crea problemi al dettaglio dei fascicoli
			eventBus.fireEvent(new BackFromPlaceEvent(pecInPath));
		}

	}

	private class MostraGruppiCommand implements com.google.gwt.user.client.Command {

		@Override
		public void execute() {
			praticheDB.getPecInByPath(pecInPath, siteMap.containsLink(pecInPath), new PraticaEmaiInlLoaded() {

				@Override
				public void onPraticaLoaded(PecInDTO pec) {
					GoToAssegnaFromDettaglioPecInEvent event = new GoToAssegnaFromDettaglioPecInEvent(pecInPath);
					eventBus.fireEvent(event);
				}

				@Override
				public void onPraticaError(String error) {
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
					eventBus.fireEvent(event);
				}
			});
		}

	}

	private class EliminaCommand implements Command {

		@Override
		public void execute() {
			CambiaStatoPecInAction action = new CambiaStatoPecInAction(DettaglioPecInPresenter.this.pecInPath, CambiaStatoPecInAction.Azione.ELIMINA);
			executeCambiaStato(action);
		}

	}

	private class ArchiviaCommand implements Command {

		@Override
		public void execute() {
			CambiaStatoPecInAction action = new CambiaStatoPecInAction(DettaglioPecInPresenter.this.pecInPath, CambiaStatoPecInAction.Azione.ARCHIVIA);
			executeCambiaStato(action);
		}

	}

	private class RiportaInGestioneCommand implements Command {

		@Override
		public void execute() {
			CambiaStatoPecInAction action = new CambiaStatoPecInAction(DettaglioPecInPresenter.this.pecInPath, CambiaStatoPecInAction.Azione.RIPORTAINGESTIONE);
			executeCambiaStato(action);
		}

	}

	private class DownloadAllegatoCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, AllegatoDTO> {

		@Override
		public Void exe(final AllegatoDTO allegato) {
			SafeUri uri = UriMapping.generaDownloadAllegatoServletURL(pecInPath, allegato);
			getView().sendDownload(uri);
			return null;
		}
	}

	private class MostraDettaglioAllegatoCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, AllegatoDTO> {

		@Override
		public Void exe(AllegatoDTO allegato) {
			MostraDettaglioAllegatoEvent.fire(DettaglioPecInPresenter.this, pecInPath, allegato);
			return null;
		}
	}

	private abstract class PrendiInCaricoPecCommand implements Command {

		@Override
		public void execute() {

			// se arrivo a questo punto se la mail è già in carico a qualcuno è sempre in carico a me
			if (pecInDTO.getTipoPresaInCarico().equals(TipoPresaInCarico.NESSUNO)) {

				GestionePresaInCaricoFascicoloAction action = new GestionePresaInCaricoFascicoloAction();
				action.setClientID(pecInPath);
				ShowAppLoadingEvent.fire(DettaglioPecInPresenter.this, true);
				/* reset dei messaggi di errore */
				ShowMessageEvent event = new ShowMessageEvent();
				event.setMessageDropped(true);
				eventBus.fireEvent(event);
				dispatcher.execute(action, new AsyncCallback<GestionePresaInCaricoFascicoloActionResult>() {

					@Override
					public void onFailure(Throwable caught) {
						ShowAppLoadingEvent.fire(DettaglioPecInPresenter.this, false);
						ShowMessageEvent event = new ShowMessageEvent();
						event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
						eventBus.fireEvent(event);
					}

					@Override
					public void onSuccess(GestionePresaInCaricoFascicoloActionResult result) {
						ShowAppLoadingEvent.fire(DettaglioPecInPresenter.this, false);
						if (!result.isError()) {
							ShowMessageEvent event = new ShowMessageEvent();
							event.setMessageDropped(true);
							eventBus.fireEvent(event);
							praticheDB.insertOrUpdate(pecInPath, result.getPraticaDTO(), true);
							handlePraticaInCarico(result.getPraticaDTO());
							ShowAppLoadingEvent.fire(DettaglioPecInPresenter.this, false);
						} else {
							ShowAppLoadingEvent.fire(DettaglioPecInPresenter.this, false);
							ShowMessageEvent event = new ShowMessageEvent();
							event.setErrorMessage(result.getErrorMsg());
							eventBus.fireEvent(event);
						}
					}
				});
			} else {
				handlePraticaInCarico(pecInDTO);
			}

		}

		public abstract void handlePraticaInCarico(PraticaDTO pratica);
	}

	private class CreaFascicoloCommand extends PrendiInCaricoPecCommand {

		@Override
		public void handlePraticaInCarico(PraticaDTO pratica) {
			Place place = new Place();
			place.setToken(NameTokens.creafascicolo);
			place.addParam(NameTokensParams.idPratica, pratica.getClientID());
			place.addParam(NameTokensParams.svuotaCampi, Boolean.toString(true));
			getEventBus().fireEvent(new GoToPlaceEvent(place));
		}

	}

	private class AggiungiPecInAFascicoloEsistenteCommand extends PrendiInCaricoPecCommand {

		@Override
		public void handlePraticaInCarico(PraticaDTO pratica) {
			Place place = new Place();
			place.setToken(NameTokens.sceltafascicolo);
			place.addParam(NameTokensParams.idPratica, pratica.getClientID());
			place.addParam(NameTokensParams.mittente, ((PecInDTO) pratica).getMittente());
			getEventBus().fireEvent(new GoToPlaceEvent(place));
		}
	}

	public class SalvaPECCommand implements Command {

		@Override
		public void execute() {

			ShowAppLoadingEvent.fire(DettaglioPecInPresenter.this, true);
			SalvaNotePecInAction action = new SalvaNotePecInAction(getView().getPecIn().getClientID(), getView().getNote());

			dispatcher.execute(action, new AsyncCallback<SalvaNotePecInResult>() {

				@Override
				public void onFailure(Throwable caught) {
					ShowAppLoadingEvent.fire(DettaglioPecInPresenter.this, false);
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
					eventBus.fireEvent(event);
				}

				@Override
				public void onSuccess(SalvaNotePecInResult result) {
					ShowAppLoadingEvent.fire(DettaglioPecInPresenter.this, false);

					if (result.isError()) {
						ShowMessageEvent event = new ShowMessageEvent();
						event.setErrorMessage(result.getErrorMessage());
						eventBus.fireEvent(event);

					} else {
						PecInDTO dto = result.getPecInDTO();
						praticheDB.insertOrUpdate(dto.getClientID(), dto, siteMap.containsLink(dto.getClientID()));
						getView().mostraPratica(dto);
					}

				}
			});
		}

	}

	public class AnnullaSalvaPECCommand implements Command {

		@Override
		public void execute() {
			praticheDB.getPecInByPath(pecInPath, siteMap.containsLink(pecInPath), new PraticaEmaiInlLoaded() {

				@Override
				public void onPraticaLoaded(PecInDTO pec) {
					getView().mostraPratica(pec);

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

	public class RiportaInLetturaCommand implements Command {

		@Override
		public void execute() {
			ShowAppLoadingEvent.fire(DettaglioPecInPresenter.this, true);

			ShowMessageEvent event = new ShowMessageEvent();
			event.setMessageDropped(true);
			DettaglioPecInPresenter.this.eventBus.fireEvent(event);

			RiportaInLettura action = new RiportaInLettura(DettaglioPecInPresenter.this.pecInPath);

			DettaglioPecInPresenter.this.dispatcher.execute(action, new AsyncCallback<RiportaInLetturaResult>() {

				@Override
				public void onFailure(Throwable caught) {
					ShowAppLoadingEvent.fire(DettaglioPecInPresenter.this, false);
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
					DettaglioPecInPresenter.this.eventBus.fireEvent(event);
				}

				@Override
				public void onSuccess(RiportaInLetturaResult result) {
					ShowAppLoadingEvent.fire(DettaglioPecInPresenter.this, false);
					if (result.getError()) {
						ShowMessageEvent event = new ShowMessageEvent();
						event.setErrorMessage(result.getMessageError());
						DettaglioPecInPresenter.this.eventBus.fireEvent(event);
					} else {
						PecInDTO pecRes = result.getPecIn();
						DettaglioPecInPresenter.this.praticheDB.remove(pecRes.getClientID());
						getView().mostraPratica(pecRes);
						DettaglioPecInPresenter.this.eventBus.fireEvent(new UpdateSiteMapEvent());
					}
				}
			});

		}
	}

	@Override
	@ProxyEvent
	public void onRilasciaInCaricoPecIn(RilasciaInCaricoPecInEvent event) {
		ShowAppLoadingEvent.fire(DettaglioPecInPresenter.this, true);
		final String idPecIn = event.getIdPecIn();
		CambiaStatoPecInAction action = new CambiaStatoPecInAction(idPecIn, Azione.RILASCIA_IN_CARICO);
		dispatcher.execute(action, new AsyncCallback<CambiaStatoPecInActionResult>() {

			@Override
			public void onFailure(Throwable caught) {
				ShowAppLoadingEvent.fire(DettaglioPecInPresenter.this, false);
				ShowMessageEvent event = new ShowMessageEvent();
				event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
				eventBus.fireEvent(event);
			}

			@Override
			public void onSuccess(CambiaStatoPecInActionResult result) {
				ShowAppLoadingEvent.fire(DettaglioPecInPresenter.this, false);
				if (!result.getIsError()) {
					praticheDB.remove(idPecIn);
					getEventBus().fireEvent(new BackFromPlaceEvent());
				} else {
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(result.getMessErr());
					eventBus.fireEvent(event);
				}
			}

		});
	}

	@Override
	public EventBus _getEventBus() {
		return getEventBus();
	}

	@Override
	public DispatchAsync getDispatchAsync() {
		return dispatcher;
	}

	@Override
	public PlaceManager getPlaceManager() {
		return placeManager;
	}

	@Override
	public PecInPraticheDB getPecInPraticheDB() {
		return praticheDB;
	}

	public String getPecInPath() {
		return pecInPath;
	}

	public PecInDTO getPecInDTO() {
		return pecInDTO;
	}

	public class GoToPraticaCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, PraticaDTO> {

		@Override
		public Void exe(PraticaDTO pratica) {
			praticheDB.remove(pratica.getClientID());
			Place place = new Place();
			place.forceHistory();
			place.setToken(pratica.getTipologiaPratica().getDettaglioNameToken());
			place.addParam(NameTokensParams.idPratica, pratica.getClientID());
			place.addParam(NameTokensParams.showActions, Boolean.FALSE.toString());
			getEventBus().fireEvent(new GoToPlaceEvent(place));
			return null;
		}

	}

	@Override
	@ProxyEvent
	public void onBackToDettaglioPecIn(BackToDettaglioPecInEvent event) {
		this.pecInPath = event.getPecPath();
		mostraPratica();
		revealInParent();
	}

}
