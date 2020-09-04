package it.eng.portlet.consolepec.gwt.client.presenter.urbanistica;

import it.eng.cobo.consolepec.commons.urbanistica.PraticaProcedi;
import it.eng.portlet.consolepec.gwt.client.command.ConsolePecCommandBinder;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB;
import it.eng.portlet.consolepec.gwt.client.event.BackFromPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.place.NameTokens;
import it.eng.portlet.consolepec.gwt.client.place.NameTokensParams;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.event.urbanistica.MostraListaPraticaProcediEvent;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.urbanistica.DettaglioPraticaProcediAction;
import it.eng.portlet.consolepec.gwt.shared.action.urbanistica.DettaglioPraticaProcediResult;

import java.util.Arrays;

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
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

/**
 * @author GiacomoFM
 * @since 06/nov/2017
 */
public class DettaglioPraticaProcediPresenter extends Presenter<DettaglioPraticaProcediPresenter.MyView, DettaglioPraticaProcediPresenter.MyProxy> implements ConsolePecCommandBinder {

	@ProxyCodeSplit
	@NameToken(NameTokens.praticaprocedi)
	public interface MyProxy extends ProxyPlace<DettaglioPraticaProcediPresenter> {
		//
	}

	public interface MyView extends View {
		void setIndietroCommand(final Command command);

		void mostraPraticaProcedi(final PraticaProcedi praticaProcedi);

		void setPresenter(DettaglioPraticaProcediPresenter dettaglioPraticaProcediPresenter);

		void sendDownload(SafeUri uri);
	}

	private String idPraticaProcedi;

	private final DispatchAsync dispatcher;

	private MostraListaPraticaProcediEvent mostraListaPraticaProcediEvent = new MostraListaPraticaProcediEvent();

	@Inject
	public DettaglioPraticaProcediPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, final DispatchAsync dispatcher) {
		super(eventBus, view, proxy);
		this.dispatcher = dispatcher;
	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
		idPraticaProcedi = request.getParameter(NameTokensParams.idPratica, "0");
		mostraListaPraticaProcediEvent.setNameClass(request.getParameter(NameTokensParams.nomeClasseDiRitorno, "0"));
		mostraListaPraticaProcediEvent.setIdFascicolo(request.getParameter(NameTokensParams.idFascicolo, "0"));
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPresenter.TYPE_SetMainContent, this);
	}

	@Override
	protected void onBind() {
		super.onBind();
		getView().setPresenter(this);
		getView().setIndietroCommand(new Command() {
			@Override
			public void execute() {
				
				getEventBus().fireEvent(new BackFromPlaceEvent(mostraListaPraticaProcediEvent.getPathFascicolo()));
			}
		});
	}

	@Override
	protected void onReveal() {
		super.onReveal();
		ShowAppLoadingEvent.fire(DettaglioPraticaProcediPresenter.this, true);
		dispatcher.execute(new DettaglioPraticaProcediAction(Arrays.asList(idPraticaProcedi)), new AsyncCallback<DettaglioPraticaProcediResult>() {
			@Override
			public void onFailure(Throwable caught) {
				ShowAppLoadingEvent.fire(DettaglioPraticaProcediPresenter.this, false);
				fireErrorMessageEvent(ConsolePecConstants.ERROR_MESSAGE);
			}

			@Override
			public void onSuccess(DettaglioPraticaProcediResult result) {
				ShowAppLoadingEvent.fire(DettaglioPraticaProcediPresenter.this, false);
				if (result.isError() || result.getPraticheProcedi() == null || result.getPraticheProcedi().isEmpty()) {
					fireErrorMessageEvent(result.getMsgError());
				} else {
					getView().mostraPraticaProcedi(result.getPraticheProcedi().get(0));
				}
			}
		});
	}

	private void fireErrorMessageEvent(String msg) {
		ShowMessageEvent event = new ShowMessageEvent();
		event.setErrorMessage(msg);
		getEventBus().fireEvent(event);
	}

	@Override
	public EventBus _getEventBus() {
		// TODO Auto-generated method stub
		return getEventBus();
	}

	@Override
	public DispatchAsync getDispatchAsync() {
		// TODO Auto-generated method stub
		return dispatcher;
	}

	@Override
	public PlaceManager getPlaceManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PecInPraticheDB getPecInPraticheDB() {
		// TODO Auto-generated method stub
		return null;
	}

}
