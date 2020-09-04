package it.eng.portlet.consolepec.gwt.client.presenter.inviomassivo;

import java.util.Set;

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
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaComunicazioneLoaded;
import it.eng.portlet.consolepec.gwt.client.event.ApertoDettaglioEvent;
import it.eng.portlet.consolepec.gwt.client.event.BackFromPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.event.ChiudiDettaglioAllegatoEvent;
import it.eng.portlet.consolepec.gwt.client.event.ChiudiDettaglioAllegatoEvent.ChiudiDettaglioAllegatoHandler;
import it.eng.portlet.consolepec.gwt.client.event.GoToPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.event.MostraDettaglioAllegatoEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.place.NameTokens;
import it.eng.portlet.consolepec.gwt.client.place.NameTokensParams;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter.Place;
import it.eng.portlet.consolepec.gwt.client.presenter.event.UploadEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.event.UploadEvent.UploadStatus;
import it.eng.portlet.consolepec.gwt.client.presenter.inviomassivo.command.InviaCsvCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.inviomassivo.event.BackToDettaglioComunicazioneEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.inviomassivo.event.BackToDettaglioComunicazioneEvent.BackToDettaglioComunicazioneHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.inviomassivo.event.MostraCreaComunicazioneDaDettaglioComunicazioneEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.inviomassivo.event.MostraInviaCsvTestComunicazioneEvent;
import it.eng.portlet.consolepec.gwt.client.util.GestioneLinkSiteMapUtil;
import it.eng.portlet.consolepec.gwt.client.widget.SitemapMenu;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.UriMapping;
import it.eng.portlet.consolepec.gwt.shared.action.CancellaAllegatoPratica;
import it.eng.portlet.consolepec.gwt.shared.action.CancellaAllegatoPraticaResult;
import it.eng.portlet.consolepec.gwt.shared.action.pec.UploadAllegatoPraticaAction;
import it.eng.portlet.consolepec.gwt.shared.action.pec.UploadAllegatoPraticaResult;
import it.eng.portlet.consolepec.gwt.shared.dto.CollegamentoDto;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.ComunicazioneDTO;
import it.eng.portlet.consolepec.gwt.shared.model.RispostaFileUploaderDTO;

public class DettaglioComunicazionePresenter extends Presenter<DettaglioComunicazionePresenter.MyView, DettaglioComunicazionePresenter.MyProxy> implements ChiudiDettaglioAllegatoHandler, BackToDettaglioComunicazioneHandler, ConsolePecCommandBinder {

	public interface MyView extends View {

		void resetDisclosurePanels(boolean parseBoolean);

		void mostraTitolo(boolean b);

		void mostraPulsantiera(boolean b);

		void mostraPratica(ComunicazioneDTO comunicazione);

		void clear();

		void setChiudiDettaglioCommand(Command chiudiDettaglioCommand);

		void startUpload();

		void setUploadAllegatoCommand(UploadAllegatoCommand uploadAllegatoCommand);

		void setCancellaAllegatoCommand(it.eng.portlet.consolepec.gwt.client.presenter.Command<Object, Set<AllegatoDTO>> command);

		void setDownloadAllegatoCommand(it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, AllegatoDTO> downloadAllegatoCommand);

		void setMostraDettaglioAllegatoCommand(it.eng.portlet.consolepec.gwt.client.presenter.Command<Object, AllegatoDTO> mostraDettaglioAllegatoCommand);

		void setInviaCsvTestCommand(it.eng.portlet.consolepec.gwt.client.presenter.Command<Object, AllegatoDTO> inviaCsvTestCommand);

		void setInviaCsvCommand(InviaCsvCommand inviaCsvCommand);

		void sendDownload(SafeUri uri);

		void setCreaComunicazionePerCopiaCommand(Command creaComunicazionePerCopiaCommand);

		void setGoToFascicoloCollegatoCommand(GoToFascicoloCollegatoCommand goToFascicoloCollegatoCommand);

	}

	@ProxyCodeSplit
	@NameToken(NameTokens.dettagliocomunicazione)
	public interface MyProxy extends ProxyPlace<DettaglioComunicazionePresenter> {}

	private String comunicazionePath;
	private ComunicazioneDTO comunicazione;
	private final DispatchAsync dispatcher;
	private final PecInPraticheDB praticheDB;
	private final SitemapMenu sitemapMenu;
	private GestioneLinkSiteMapUtil gestioneLinkSiteMapUtil;
	private final PlaceManager placeManager;

	private final EventBus eventBus;

	@Inject
	public DettaglioComunicazionePresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, final PecInPraticheDB pecInDb, final DispatchAsync dispatcher, final SitemapMenu sitemap,
			final GestioneLinkSiteMapUtil gestioneLinkSiteMapUtil, final PlaceManager placeManager) {
		super(eventBus, view, proxy);
		this.eventBus = eventBus;
		this.dispatcher = dispatcher;
		this.praticheDB = pecInDb;
		this.placeManager = placeManager;
		this.sitemapMenu = sitemap;
		this.gestioneLinkSiteMapUtil = gestioneLinkSiteMapUtil;
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPresenter.TYPE_SetMainContent, this);
	}

	@Override
	protected void onBind() {
		super.onBind();

		getView().setChiudiDettaglioCommand(new ChiudiDettaglioCommand());

		getView().setUploadAllegatoCommand(new UploadAllegatoCommand());
		getView().setDownloadAllegatoCommand(new DownloadAllegatoCommand());
		getView().setCancellaAllegatoCommand(new CancellaAllegatoCommand());
		getView().setMostraDettaglioAllegatoCommand(new MostraDettaglioAllegatoCommand());
		getView().setInviaCsvTestCommand(new InviaCsvTestCommand());
		getView().setInviaCsvCommand(new InviaCsvCommand(this));
		getView().setCreaComunicazionePerCopiaCommand(new CreaComunicazionePerCopiaCommand());

		getView().setGoToFascicoloCollegatoCommand(new GoToFascicoloCollegatoCommand());
	}

	@Override
	protected void onReveal() {
		super.onReveal();

		getView().clear();

		getView().mostraTitolo(false);
		getView().mostraPulsantiera(false);
		caricaPratica();

		ShowMessageEvent event = new ShowMessageEvent();
		event.setMessageDropped(true);
		getEventBus().fireEvent(event);

	}

	@Override
	protected void onHide() {
		dropMessage();
		super.onHide();

	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);

		this.comunicazionePath = request.getParameter(NameTokensParams.idPratica, null);
		getView().resetDisclosurePanels(Boolean.parseBoolean(request.getParameter(NameTokensParams.showActions, null)));
		if (super.isVisible()) {
			getView().mostraTitolo(false);
			getView().mostraPulsantiera(false);
			caricaPratica();
		}

	}

	private void dropMessage() {
		ShowMessageEvent event = new ShowMessageEvent();
		event.setMessageDropped(true);
		eventBus.fireEvent(event);
	}

	private void caricaPratica() {
		ShowAppLoadingEvent.fire(DettaglioComunicazionePresenter.this, true);
		praticheDB.getComunicazioneByPath(comunicazionePath, true, new PraticaComunicazioneLoaded() {

			@Override
			public void onPraticaLoaded(ComunicazioneDTO comunicazione) {
				ShowAppLoadingEvent.fire(DettaglioComunicazionePresenter.this, false);

				DettaglioComunicazionePresenter.this.comunicazione = comunicazione;

				mostraPratica(comunicazione);
				gestioneLinkSiteMapUtil.aggiungiLinkInLavorazione(comunicazione);
				getView().mostraTitolo(true);
				getView().mostraPulsantiera(true);
				ApertoDettaglioEvent.fire(DettaglioComunicazionePresenter.this, comunicazione);
			}

			@Override
			public void onPraticaError(String error) {
				ShowAppLoadingEvent.fire(DettaglioComunicazionePresenter.this, false);
				ShowMessageEvent event = new ShowMessageEvent();
				event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
				getEventBus().fireEvent(event);
			}
		});
	}

	private void mostraPratica(ComunicazioneDTO comunicazione) {
		getView().mostraPratica(comunicazione);
	}

	public class ChiudiDettaglioCommand implements com.google.gwt.user.client.Command {

		@Override
		public void execute() {
			praticheDB.remove(comunicazionePath);
			getEventBus().fireEvent(new BackFromPlaceEvent(comunicazionePath));
		}

	}

	public class UploadAllegatoCommand {

		public void onFileSelected(String fileName) {
			/* reset dei messaggi di errore */
			ShowMessageEvent event2 = new ShowMessageEvent();
			event2.setMessageDropped(true);
			getEventBus().fireEvent(event2);
			// prima di avviare l'upload eseguo il salvataggio
			getEventBus().fireEvent(new UploadEvent(comunicazionePath, UploadStatus.START));
			DettaglioComunicazionePresenter.this.getView().startUpload();
		}

		public void onFileUploaded(RispostaFileUploaderDTO dto) {
			/* reset dei messaggi di errore */
			ShowMessageEvent event = new ShowMessageEvent();
			event.setMessageDropped(true);
			getEventBus().fireEvent(event);
			if (!dto.isError()) {
				UploadAllegatoPraticaAction action = new UploadAllegatoPraticaAction(dto.getTmpFiles(), DettaglioComunicazionePresenter.this.comunicazionePath);
				dispatcher.execute(action, new AsyncCallback<UploadAllegatoPraticaResult>() {

					@Override
					public void onFailure(Throwable caught) {
						getEventBus().fireEvent(new UploadEvent(comunicazionePath, UploadStatus.ERROR));
						ShowMessageEvent event = new ShowMessageEvent();
						event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
						getEventBus().fireEvent(event);
					}

					@Override
					public void onSuccess(UploadAllegatoPraticaResult result) {
						getEventBus().fireEvent(new UploadEvent(comunicazionePath, UploadStatus.DONE));
						if (!result.getError()) {
							dropMessage();
							ComunicazioneDTO output = (ComunicazioneDTO) result.getPratica();

							praticheDB.insertOrUpdate(output.getClientID(), output, sitemapMenu.containsLink(output.getClientID()));

							getView().mostraPratica(output);

						} else {
							getEventBus().fireEvent(new UploadEvent(comunicazionePath, UploadStatus.ERROR));
							writeErrorMessage(result.getMessError());
						}

					}
				});
			} else {
				getEventBus().fireEvent(new UploadEvent(comunicazionePath, UploadStatus.ERROR));
				writeErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
			}
		}

	}

	private class DownloadAllegatoCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, AllegatoDTO> {

		@Override
		public Void exe(final AllegatoDTO allegato) {
			SafeUri uri = UriMapping.generaDownloadAllegatoServletURL(allegato.getClientID(), allegato);
			getView().sendDownload(uri);
			return null;
		}
	}

	private class MostraDettaglioAllegatoCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Object, AllegatoDTO> {

		@Override
		public Object exe(AllegatoDTO allegato) {
			MostraDettaglioAllegatoEvent.fire(DettaglioComunicazionePresenter.this, comunicazionePath, comunicazionePath, allegato);
			return null;
		}

	}

	private class InviaCsvTestCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Object, AllegatoDTO> {

		@Override
		public Object exe(AllegatoDTO allegato) {
			MostraInviaCsvTestComunicazioneEvent.fire(DettaglioComunicazionePresenter.this, comunicazione, allegato);
			return null;
		}

	}

	private class CreaComunicazionePerCopiaCommand implements Command {

		@Override
		public void execute() {
			MostraCreaComunicazioneDaDettaglioComunicazioneEvent.fire(DettaglioComunicazionePresenter.this, comunicazione);
		}
	}

	private class CancellaAllegatoCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Object, Set<AllegatoDTO>> {

		@Override
		public Object exe(final Set<AllegatoDTO> allegati) {

			ShowAppLoadingEvent.fire(DettaglioComunicazionePresenter.this, true);
			/* reset dei messaggi di errore */
			ShowMessageEvent event = new ShowMessageEvent();
			event.setMessageDropped(true);
			getEventBus().fireEvent(event);

			CancellaAllegatoPratica action = new CancellaAllegatoPratica(comunicazionePath, allegati);
			DettaglioComunicazionePresenter.this.dispatcher.execute(action, new AsyncCallback<CancellaAllegatoPraticaResult>() {

				@Override
				public void onSuccess(final CancellaAllegatoPraticaResult result) {
					ShowAppLoadingEvent.fire(DettaglioComunicazionePresenter.this, false);

					if (result.getError()) {
						writeErrorMessage(result.getMessError());
					} else {
						dropMessage();
						getView().mostraPratica((ComunicazioneDTO) result.getPraticaDTO());
					}

				}

				@Override
				public void onFailure(Throwable caught) {
					ShowAppLoadingEvent.fire(DettaglioComunicazionePresenter.this, false);
					writeErrorMessage(ConsolePecConstants.ERROR_MESSAGE);

				}
			});
			return null;
		}

	}

	@Override
	@ProxyEvent
	public void onChiudiDettaglioAllegato(ChiudiDettaglioAllegatoEvent event) {
		if (event.getClientID().equals(comunicazionePath) && placeManager.getCurrentPlaceRequest().getNameToken().equals(NameTokens.dettagliocomunicazione)) {
			ShowAppLoadingEvent.fire(this, false);
			placeManager.revealCurrentPlace();
		}
	}

	@Override
	@ProxyEvent
	public void onBackToDettaglioComunicazione(BackToDettaglioComunicazioneEvent event) {
		revealInParent();
	}

	private void writeErrorMessage(String errorMessage) {
		ShowMessageEvent event = new ShowMessageEvent();
		event.setErrorMessage(errorMessage);
		eventBus.fireEvent(event);
	}

	@Override
	public EventBus _getEventBus() {
		return eventBus;
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

	public ComunicazioneDTO getComunicazione() {
		return comunicazione;
	}

	public SitemapMenu getSitemapMenu() {
		return sitemapMenu;
	}

	public class GoToFascicoloCollegatoCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, CollegamentoDto> {

		@Override
		public Void exe(CollegamentoDto collegamentoDto) {
			praticheDB.remove(collegamentoDto.getClientId());
			Place place = new Place();
			place.forceHistory();
			place.setToken(NameTokens.dettagliofascicolo);
			place.addParam(NameTokensParams.idPratica, collegamentoDto.getClientId());
			getEventBus().fireEvent(new GoToPlaceEvent(place));
			return null;
		}
	}

}
