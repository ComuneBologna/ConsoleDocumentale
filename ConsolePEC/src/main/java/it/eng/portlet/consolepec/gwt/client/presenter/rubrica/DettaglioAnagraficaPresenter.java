package it.eng.portlet.consolepec.gwt.client.presenter.rubrica;

import java.util.List;

import com.google.gwt.user.client.Window;
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

import it.eng.cobo.consolepec.commons.rubrica.Anagrafica;
import it.eng.cobo.consolepec.util.validation.ValidationUtilities;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.Command;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.event.rubrica.MostraDettaglioAnagraficaEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.event.rubrica.MostraDettaglioAnagraficaEvent.MostraDettaglioAnagraficaHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.event.rubrica.MostraListaAnagraficheEvent;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.rubrica.EliminaAnagraficaAction;
import it.eng.portlet.consolepec.gwt.shared.action.rubrica.EliminaAnagraficaResult;
import it.eng.portlet.consolepec.gwt.shared.action.rubrica.ModificaAnagraficaAction;
import it.eng.portlet.consolepec.gwt.shared.action.rubrica.ModificaAnagraficaResult;

/**
 * @author GiacomoFM
 * @since 15/set/2017
 */
public class DettaglioAnagraficaPresenter extends Presenter<DettaglioAnagraficaPresenter.MyView, DettaglioAnagraficaPresenter.MyProxy> implements MostraDettaglioAnagraficaHandler {

	@ProxyCodeSplit
	public interface MyProxy extends Proxy<DettaglioAnagraficaPresenter> {
		//
	}

	public interface MyView extends View {
		void mostraDettaglioAnagrafica(Anagrafica anagrafica);

		void setIndietroCommand(com.google.gwt.user.client.Command command);

		void setModificaCommand(Command<Void, Anagrafica> command);

		void setEliminaCommand(Command<Void, Anagrafica> command);
	}

	private final DispatchAsync dispatcher;

	private Object openingRequestor;

	@Inject
	public DettaglioAnagraficaPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, final DispatchAsync dispatcher) {
		super(eventBus, view, proxy);
		this.dispatcher = dispatcher;
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
	@ProxyEvent
	public void onMostraDettaglioAnagrafica(MostraDettaglioAnagraficaEvent event) {
		openingRequestor = event.getOpeningRequestor();
		revealInParent();
		getView().mostraDettaglioAnagrafica(event.getAnagrafica());
	}

	@Override
	protected void onBind() {
		super.onBind();
		getView().setIndietroCommand(new com.google.gwt.user.client.Command() {
			@Override
			public void execute() {
				getEventBus().fireEvent(new MostraListaAnagraficheEvent(openingRequestor));
			}
		});

		getView().setModificaCommand(new Command<Void, Anagrafica>() {
			@Override
			public Void exe(Anagrafica t) {
				modificaAnagrafica(t);
				return null;
			}
		});

		getView().setEliminaCommand(new Command<Void, Anagrafica>() {
			@Override
			public Void exe(Anagrafica t) {
				eliminaAnagrafica(t);
				return null;
			}
		});
	}

	private void modificaAnagrafica(Anagrafica anagrafica) {
		List<String> errors = ValidationUtilities.valida(anagrafica);
		if (errors.size() != 0) {
			StringBuilder sb = new StringBuilder();
			for (String error : errors) {
				sb.append(error).append("\n");
			}
			fireErrorMessageEvent(sb.toString());
			return;
		}

		ShowAppLoadingEvent.fire(DettaglioAnagraficaPresenter.this, true);
		dispatcher.execute(new ModificaAnagraficaAction(anagrafica), new AsyncCallback<ModificaAnagraficaResult>() {
			@Override
			public void onFailure(Throwable caught) {
				ShowAppLoadingEvent.fire(DettaglioAnagraficaPresenter.this, false);
				fireErrorMessageEvent(ConsolePecConstants.ERROR_MESSAGE);
			}

			@Override
			public void onSuccess(ModificaAnagraficaResult result) {
				ShowAppLoadingEvent.fire(DettaglioAnagraficaPresenter.this, false);
				if (result.isError()) {
					fireErrorMessageEvent(result.getMsgError());
				} else {
					ShowMessageEvent event = new ShowMessageEvent();
					if (result.getAnagrafica() != null) {
						event.setInfoMessage("Anagrafica modificata correttamente");
						getEventBus().fireEvent(event);
						getView().mostraDettaglioAnagrafica(result.getAnagrafica());
					}
				}
			}
		});
	}

	private void eliminaAnagrafica(Anagrafica anagrafica) {
		ShowAppLoadingEvent.fire(DettaglioAnagraficaPresenter.this, true);
		dispatcher.execute(new EliminaAnagraficaAction(anagrafica), new AsyncCallback<EliminaAnagraficaResult>() {
			@Override
			public void onFailure(Throwable caught) {
				ShowAppLoadingEvent.fire(DettaglioAnagraficaPresenter.this, false);
				fireErrorMessageEvent(ConsolePecConstants.ERROR_MESSAGE);
			}

			@Override
			public void onSuccess(EliminaAnagraficaResult result) {
				ShowAppLoadingEvent.fire(DettaglioAnagraficaPresenter.this, false);
				if (result.isError()) {
					fireErrorMessageEvent(result.getMsgError());
				} else {
					getEventBus().fireEvent(new MostraListaAnagraficheEvent(null));
				}
			}
		});
	}

	private void fireErrorMessageEvent(String msg) {
		ShowMessageEvent event = new ShowMessageEvent();
		event.setErrorMessage(msg);
		getEventBus().fireEvent(event);
	}

}
