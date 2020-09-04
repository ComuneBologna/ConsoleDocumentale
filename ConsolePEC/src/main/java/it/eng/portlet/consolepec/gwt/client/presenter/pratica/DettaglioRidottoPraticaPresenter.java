package it.eng.portlet.consolepec.gwt.client.presenter.pratica;

import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaLoaded;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.Command;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.pratica.event.MostraDettaglioAllegatoDaDettaglioRidottoEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.pratica.event.TornaADettaglioRidottoEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.pratica.event.TornaADettaglioRidottoEvent.TornaADettaglioRidottoHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.TornaAFormDiProtocollazioneEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.DettaglioPraticaFromProtocollazioneEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.DettaglioPraticaFromProtocollazioneEvent.DettaglioPraticaFromProtocollazioneHandler;
import it.eng.portlet.consolepec.gwt.shared.UriMapping;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;

import com.google.gwt.safehtml.shared.SafeUri;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

public class DettaglioRidottoPraticaPresenter extends Presenter<DettaglioRidottoPraticaPresenter.MyView, DettaglioRidottoPraticaPresenter.MyProxy> implements DettaglioPraticaFromProtocollazioneHandler, TornaADettaglioRidottoHandler {

	public interface MyView extends View {

		void setDownloadAllegatoCommand(Command<Void, AllegatoDTO> downloadAllegatoCommand);

		void sendDownload(SafeUri uri);

		void setMostraDettaglioAllegatoCommand(Command<Object, AllegatoDTO> mostraDettaglioAllegatoCommand);

		void mostraPratica(PraticaDTO pratica);

		void setChiudiDettaglioCommand(com.google.gwt.user.client.Command e);

	}

	@ProxyCodeSplit
	public interface MyProxy extends Proxy<DettaglioRidottoPraticaPresenter> {
	}

	final DispatchAsync dispatcher;
	private final PecInPraticheDB praticheDB;
	
	private String praticaPath;
	
	private final EventBus eventBus;


	private class ChiudiDettaglioCommand implements com.google.gwt.user.client.Command {

		@Override
		public void execute() {
			eventBus.fireEvent(new TornaAFormDiProtocollazioneEvent());
		}

	}
	
	@Inject
	public DettaglioRidottoPraticaPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, final PecInPraticheDB pecInDb, final DispatchAsync dispatcher) {
		super(eventBus, view, proxy);
		this.eventBus = eventBus;
		this.dispatcher = dispatcher;
		this.praticheDB = pecInDb;

	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPresenter.TYPE_SetMainContent, this);
	}

	@Override
	protected void onBind() {
		super.onBind();
		getView().setChiudiDettaglioCommand(new ChiudiDettaglioCommand());
		getView().setDownloadAllegatoCommand(new DownloadAllegatoCommand());
		getView().setMostraDettaglioAllegatoCommand(new MostraDettaglioAllegatoCommand());
		
	}

	@Override
	protected void onReveal() {
		super.onReveal();

		revealInParent();

	}

	@Override
	protected void onHide() {
		dropMessage();
		super.onHide();

	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);

	}

	private void dropMessage() {
		ShowMessageEvent event = new ShowMessageEvent();
		event.setMessageDropped(true);
		eventBus.fireEvent(event);
	}

	private void writeErrorMessage(String errorMessage) {
		ShowMessageEvent event = new ShowMessageEvent();
		event.setErrorMessage(errorMessage);
		eventBus.fireEvent(event);
	}

	@Override
	@ProxyEvent
	public void onDettaglioPraticaFromProtocollazione(DettaglioPraticaFromProtocollazioneEvent event) {
		ShowAppLoadingEvent.fire(DettaglioRidottoPraticaPresenter.this, true);
		dropMessage();
		praticaPath = event.getIdPratica();
		this.praticheDB.getPraticaByPath(praticaPath, false, new PraticaLoaded() {

			@Override
			public void onPraticaLoaded(PraticaDTO pratica) {
				getView().mostraPratica(pratica);
				ShowAppLoadingEvent.fire(DettaglioRidottoPraticaPresenter.this, false);

			}

			@Override
			public void onPraticaError(String error) {
				ShowAppLoadingEvent.fire(DettaglioRidottoPraticaPresenter.this, false);
				writeErrorMessage(error);
			}
		});
		revealInParent();

	}
	
	private class MostraDettaglioAllegatoCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Object, AllegatoDTO> {

		@Override
		public Object exe(AllegatoDTO allegato) {
			MostraDettaglioAllegatoDaDettaglioRidottoEvent.fire(DettaglioRidottoPraticaPresenter.this, praticaPath, allegato);
			return null;
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

	@Override
	@ProxyEvent
	public void onTornaADettaglioRidotto(TornaADettaglioRidottoEvent event) {
		revealInParent();
	}
}
