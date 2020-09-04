package it.eng.portlet.consolepec.gwt.client.presenter;

import java.util.Set;

import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

import it.eng.portlet.consolepec.gwt.client.event.ChiudiDettaglioAllegatoEvent;
import it.eng.portlet.consolepec.gwt.client.event.MostraDettaglioAllegatoEvent;
import it.eng.portlet.consolepec.gwt.client.event.MostraDettaglioAllegatoEvent.MostraDettaglioAllegatoHandler;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.event.dettaglioallegato.DettaglioAllegatoFineEvent;
import it.eng.portlet.consolepec.gwt.client.event.dettaglioallegato.DettaglioAllegatoInizioEvent;
import it.eng.portlet.consolepec.gwt.client.event.dettaglioallegato.DettaglioAllegatoInizioEvent.DettaglioAllegatoInizioHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.pratica.event.MostraDettaglioAllegatoDaDettaglioRidottoEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.pratica.event.MostraDettaglioAllegatoDaDettaglioRidottoEvent.MostraDettaglioAllegatoDaDettaglioRidottoHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.pratica.event.TornaADettaglioRidottoEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.TornaAFormDiProtocollazioneEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.MostraDettaglioAllegatoDaFormProtocollazioneEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.MostraDettaglioAllegatoDaFormProtocollazioneEvent.MostraDettaglioAllegatoDaFormProtocollazioneHandler;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.UriMapping;
import it.eng.portlet.consolepec.gwt.shared.action.GetDettagliAllegato;
import it.eng.portlet.consolepec.gwt.shared.action.GetDettagliAllegatoResult;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO.StoricoVersioniDTO;
import it.eng.portlet.consolepec.gwt.shared.model.DettagliAllegatoDTO;

public class DettaglioAllegatoPresenter extends Presenter<DettaglioAllegatoPresenter.MyView, DettaglioAllegatoPresenter.MyProxy> implements MostraDettaglioAllegatoHandler, MostraDettaglioAllegatoDaFormProtocollazioneHandler, MostraDettaglioAllegatoDaDettaglioRidottoHandler, DettaglioAllegatoInizioHandler {

	public interface MyView extends View {

		void setChiudiDettaglioCommand(ChiudiCommand chiudiCommand);

		void disegnaDettagliAllegato(DettagliAllegatoDTO dto, Set<StoricoVersioniDTO> storicoVersioni);

		void resetView();

		void setCreaUrlFileSbustato(CreaUrlFileSbustato creaUrl);

		void setCreaUrlFileVersione(CreaUrlFileVersione creaUrl);
	}

	@ProxyCodeSplit
	public interface MyProxy extends Proxy<DettaglioAllegatoPresenter> {/**/}

	private AllegatoDTO allegato;

	private String pathPraticaAllegato;
	private String pathPraticaRitornoMaschera; // può essere differente dalla pratica dell'allegato(dettaglio dell'allegato di una mail dalla maschera
												// del dettagli fascicolo)

	private final DispatchAsync dispatcher;
	private DettagliAllegatoDTO dto;
	private ChiudiCommand chiudiCommand;
	private Object openingRequestor;

	@Inject
	public DettaglioAllegatoPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, final DispatchAsync dispatcher) {
		super(eventBus, view, proxy);
		this.dispatcher = dispatcher;
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPresenter.TYPE_SetMainContent, this);
	}

	@Override
	protected void onBind() {
		super.onBind();
		getView().setCreaUrlFileSbustato(new CreaUrlFileSbustato());
		getView().setCreaUrlFileVersione(new CreaUrlFileVersione());
	}

	@Override
	@ProxyEvent
	public void onMostraDettaglioAllegato(MostraDettaglioAllegatoEvent event) {
		this.pathPraticaAllegato = event.getPathPraticaAllegato();
		this.pathPraticaRitornoMaschera = event.getPathPraticaRitornoMaschera();
		this.allegato = event.getAllegato();
		openDettaglioAllegato();
		chiudiCommand = new ChiudiCommand();
		getView().setChiudiDettaglioCommand(chiudiCommand);
	}

	@Override
	@ProxyEvent
	public void onStartDettaglioAllegato(DettaglioAllegatoInizioEvent event) {
		this.pathPraticaAllegato = event.getPathPraticaAllegato();
		this.allegato = event.getAllegato();
		this.openingRequestor = event.getOpeningRequestor();
		openDettaglioAllegato();
		chiudiCommand = new ChiudiCommand();
		getView().setChiudiDettaglioCommand(chiudiCommand);
	}

	private void openDettaglioAllegato() {
		getView().resetView();
		GetDettagliAllegato action = new GetDettagliAllegato(pathPraticaAllegato, allegato);

		ShowAppLoadingEvent.fire(DettaglioAllegatoPresenter.this, true);
		/* reset dei messaggi di errore */
		ShowMessageEvent event2 = new ShowMessageEvent();
		event2.setMessageDropped(true);
		getEventBus().fireEvent(event2);

		dispatcher.execute(action, new AsyncCallback<GetDettagliAllegatoResult>() {

			@Override
			public void onFailure(Throwable caught) {
				ShowAppLoadingEvent.fire(DettaglioAllegatoPresenter.this, false);
				ShowMessageEvent event = new ShowMessageEvent();
				event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
				getEventBus().fireEvent(event);
			}

			@Override
			public void onSuccess(GetDettagliAllegatoResult result) {
				ShowAppLoadingEvent.fire(DettaglioAllegatoPresenter.this, false);
				if (result.getError()) {
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(result.getErrMsg());
					getEventBus().fireEvent(event);
				} else {
					dto = result.getDto();
					getView().disegnaDettagliAllegato(dto, allegato.getStoricoVersioni());
				}
			}
		});
		revealInParent();
	}

	/* definizione classi command */
	public class ChiudiCommand implements Command {
		@Override
		public void execute() {
			ShowAppLoadingEvent.fire(DettaglioAllegatoPresenter.this, true);
			if (openingRequestor != null) {
				DettaglioAllegatoFineEvent.fire(DettaglioAllegatoPresenter.this, openingRequestor);
			} else {
				ChiudiDettaglioAllegatoEvent.fire(DettaglioAllegatoPresenter.this, pathPraticaRitornoMaschera);
			}
		}
	}

	public class TornaFormProtocollazioneCommand extends ChiudiCommand {
		@Override
		public void execute() {
			TornaAFormDiProtocollazioneEvent.fire(DettaglioAllegatoPresenter.this);
		}
	}

	public class TornaDettaglioRidottoCommand extends ChiudiCommand {
		@Override
		public void execute() {
			TornaADettaglioRidottoEvent.fire(DettaglioAllegatoPresenter.this);
		}
	}

	public class CreaUrlFileSbustato implements it.eng.portlet.consolepec.gwt.client.presenter.Command<SafeUri, String> {
		@Override
		public SafeUri exe(String uuid) {
			SafeUri uri = UriMapping.generaDownloadAllegatoSbustatoServletURL(pathPraticaAllegato, uuid, dto.getNome());
			return uri;
		}
	}

	public class CreaUrlFileVersione implements it.eng.portlet.consolepec.gwt.client.presenter.Command<SafeUri, String> {
		@Override
		public SafeUri exe(String versione) {
			SafeUri uri = UriMapping.generaDownloadAllegatoVersionato(pathPraticaAllegato, dto.getNome(), versione);
			return uri;
		}
	}

	@Override
	@ProxyEvent
	public void onMostraDettaglioAllegatoDaFormProtocollazione(MostraDettaglioAllegatoDaFormProtocollazioneEvent event) {
		this.pathPraticaAllegato = event.getAllegato().getClientID();
		this.pathPraticaRitornoMaschera = event.getAllegato().getClientID();
		this.allegato = event.getAllegato();
		openDettaglioAllegato();
		chiudiCommand = new TornaFormProtocollazioneCommand();
		getView().setChiudiDettaglioCommand(chiudiCommand);
	}

	@Override
	@ProxyEvent
	public void onMostraDettaglioAllegatoDaDettaglioRidotto(MostraDettaglioAllegatoDaDettaglioRidottoEvent event) {
		this.pathPraticaAllegato = event.getAllegato().getClientID();
		this.pathPraticaRitornoMaschera = event.getAllegato().getClientID();
		this.allegato = event.getAllegato();
		openDettaglioAllegato();
		chiudiCommand = new TornaDettaglioRidottoCommand();
		getView().setChiudiDettaglioCommand(chiudiCommand);
	}
}
