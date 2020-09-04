package it.eng.portlet.consolepec.gwt.client.presenter.pecout;

import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaEmailOutLoaded;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaLoaded;
import it.eng.portlet.consolepec.gwt.client.event.BackFromPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.event.GoToPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.place.NameTokens;
import it.eng.portlet.consolepec.gwt.client.place.NameTokensParams;
import it.eng.portlet.consolepec.gwt.client.presenter.Command;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter.Place;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command.IStampaRicevute;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command.StampaRicevuteConsegnaCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.pecout.event.MostraBozzaReinoltroEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.pecout.event.MostraDettaglioPecOutInviataEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.pecout.event.MostraDettaglioPecOutInviataEvent.MostraDettaglioPecOutInviataHandler;
import it.eng.portlet.consolepec.gwt.client.widget.SitemapMenu;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.UriMapping;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PecOutDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.ui.Button;
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

public class DettaglioPecOutInviatoPresenter extends Presenter<DettaglioPecOutInviatoPresenter.MyView, DettaglioPecOutInviatoPresenter.MyProxy> implements MostraDettaglioPecOutInviataHandler, IStampaRicevute {

	public interface MyView extends View {

		void mostraBozza(PecOutDTO pec);

		Button getChiudiButton();

		void setDownloadAllegatoCommand(Command<Void, AllegatoDTO> downloadAllegatoCommand);

		void sendDownload(SafeUri uri);

		public Button getReinoltraButton();

		public void initTitle(boolean isReinoltro);

		public void addPraticaCollegataSection(PraticaDTO pratica);

		public void buildPanelPraticheCollegate();

		public void setGotoPraticaCommand(GoToPraticaCommand gotoPraticaCommand);

		public void resetDisclosurePanels(boolean showActions);

		public Button getRicevutaButton();

	}

	@ProxyCodeSplit
	public interface MyProxy extends Proxy<DettaglioPecOutInviatoPresenter> {
	}

	private String idPratica;
	private String idFascicolo;
	private final PecInPraticheDB pecInDb;
	private final SitemapMenu siteMapMenu;
	private final EventBus eventBus;
	private DispatchAsync dispatcher;
	private PlaceManager placeManager;

	@Inject
	public DettaglioPecOutInviatoPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, final DispatchAsync dispatcher, final PecInPraticheDB pecInDb, final SitemapMenu siteMap, final PlaceManager placeManager) {
		super(eventBus, view, proxy);
		this.pecInDb = pecInDb;
		this.siteMapMenu = siteMap;
		this.eventBus = eventBus;
		this.dispatcher = dispatcher;
		this.placeManager = placeManager;
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, DettaglioPecOutPresenter.TYPE_SetDettaglio, this);
	}

	@Override
	protected void onBind() {
		super.onBind();
		getView().setGotoPraticaCommand(new GoToPraticaCommand());
		getView().setDownloadAllegatoCommand(new DownloadAllegatoCommand());
		getView().getChiudiButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				siteMapMenu.removeVoice(idPratica);
				chiudi();
			}
		});

		getView().getReinoltraButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				new ReinoltraCommand().execute();

			}
		});

		getView().getRicevutaButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				new StampaRicevuteConsegnaCommand<DettaglioPecOutInviatoPresenter>(DettaglioPecOutInviatoPresenter.this).execute();
			}
		});

	}

	protected void chiudi() {
		pecInDb.remove(idPratica);
		DettaglioPecOutInviatoPresenter.this.siteMapMenu.removeVoice(idPratica);
		eventBus.fireEvent(new BackFromPlaceEvent());
	}

	@Override
	protected void onReveal() {
		super.onReveal();
		getView().resetDisclosurePanels(false);
		caricaPecOutInviato();
	}

	private void caricaPecOutInviato() {

		ShowAppLoadingEvent.fire(DettaglioPecOutInviatoPresenter.this, true);
		pecInDb.getPecOutByPath(idPratica, siteMapMenu.containsLink(idPratica), new PraticaEmailOutLoaded() {

			@Override
			public void onPraticaLoaded(final PecOutDTO pecOutDto) {
				getView().mostraBozza(pecOutDto);
				getView().initTitle(pecOutDto.isReinoltro());
				ShowAppLoadingEvent.fire(DettaglioPecOutInviatoPresenter.this, false);
				if (pecOutDto.getIdPraticheCollegate().size() > 0) {
					for (String idPratica : pecOutDto.getIdPraticheCollegate()) {
						pecInDb.getPraticaByPath(idPratica, true, new PraticaLoaded() {

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
				ShowAppLoadingEvent.fire(DettaglioPecOutInviatoPresenter.this, false);
				ShowMessageEvent event = new ShowMessageEvent();
				event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
				eventBus.fireEvent(event);
			}
		});
	}

	private class DownloadAllegatoCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, AllegatoDTO> {

		@Override
		public Void exe(final AllegatoDTO allegato) {
			SafeUri uri = UriMapping.generaDownloadAllegatoServletURL(idPratica, allegato);
			getView().sendDownload(uri);
			return null;
		}
	}

	public class GoToDettaglioFascicoloCommand implements com.google.gwt.user.client.Command {

		@Override
		public void execute() {
			Place place = new Place();
			place.setToken(NameTokens.dettagliofascicolo);
			place.addParam(NameTokensParams.idPratica, idFascicolo);
			getEventBus().fireEvent(new GoToPlaceEvent(place));

		}

	}

	public class ReinoltraCommand implements com.google.gwt.user.client.Command {

		@Override
		public void execute() {
			ShowAppLoadingEvent.fire(DettaglioPecOutInviatoPresenter.this, true);
			pecInDb.getPecOutByPath(idPratica, siteMapMenu.containsLink(idPratica), new PraticaEmailOutLoaded() {

				@Override
				public void onPraticaLoaded(final PecOutDTO pecOutDto) {
					ShowAppLoadingEvent.fire(DettaglioPecOutInviatoPresenter.this, false);
					MostraBozzaReinoltroEvent mostraBozzaReinoltroEvent = new MostraBozzaReinoltroEvent(pecOutDto);
					getEventBus().fireEvent(mostraBozzaReinoltroEvent);
				}

				@Override
				public void onPraticaError(String error) {
					ShowAppLoadingEvent.fire(DettaglioPecOutInviatoPresenter.this, false);
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
					eventBus.fireEvent(event);
				}
			});

		}
	}

	@Override
	@ProxyEvent
	public void onMostraDettaglioPecOutInviata(MostraDettaglioPecOutInviataEvent event) {
		idPratica = event.getIdPratica();
		revealInParent();
		if (isVisible())
			caricaPecOutInviato();
	}

	public class GoToPraticaCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, PraticaDTO> {

		@Override
		public Void exe(PraticaDTO pratica) {
			pecInDb.remove(pratica.getClientID());
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
	public void downloadStampa(SafeUri uri) {
		getView().sendDownload(uri);
		onReveal();
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
		return pecInDb;
	}

	@Override
	public String getPecOutPath() {
		return idPratica;
	}

	

}
