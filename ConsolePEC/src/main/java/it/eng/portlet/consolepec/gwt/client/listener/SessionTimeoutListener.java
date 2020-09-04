package it.eng.portlet.consolepec.gwt.client.listener;

import it.eng.portlet.consolepec.gwt.client.event.SessionTimeoutEvent;
import it.eng.portlet.consolepec.gwt.client.rpc.IONOIAsyncCallbackListener;
import it.eng.portlet.consolepec.gwt.client.rpc.IONOIAsyncCallbackProxy;
import it.eng.portlet.consolepec.gwt.shared.exception.SessionExpiredException;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

public class SessionTimeoutListener implements IONOIAsyncCallbackListener, HasHandlers {

	private final EventBus eventbus;

	@Inject
	public SessionTimeoutListener(EventBus eventbus) {
		this.eventbus = eventbus;
		/* registrazione del listener */
		IONOIAsyncCallbackProxy.addListener(this);
	}

	@Override
	public void onSuccess(SuccessEvent event) {
		// nop
	}

	@Override
	public void onFailure(FailureEvent event) {
		if (event.getCaught() instanceof SessionExpiredException) {
			event.preventPropagation();
			// Window.alert("sessione scaduta, si prega di riaggiornare la pagina");
			SessionTimeoutEvent.fire(this);
		}
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		eventbus.fireEvent(event);
	}

}
