package it.eng.portlet.consolepec.gwt.client.rpc;

import it.eng.portlet.consolepec.gwt.client.rpc.IONOIAsyncCallbackListener.FailureEvent;
import it.eng.portlet.consolepec.gwt.client.rpc.IONOIAsyncCallbackListener.SuccessEvent;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * La classe funziona, client side, come un interceptor delle response alle async call al server
 * 
 * @author pluttero
 * 
 * @param <T>
 */
public class IONOIAsyncCallbackProxy<T> implements AsyncCallback<T> {

	private final AsyncCallback<T> realCallback;
	private static List<IONOIAsyncCallbackListener> listeners = new ArrayList<IONOIAsyncCallbackListener>();

	public IONOIAsyncCallbackProxy(AsyncCallback<T> realCallback) {
		this.realCallback = realCallback;
	}

	@Override
	public void onFailure(Throwable caught) {
		boolean prop = true;
		for (IONOIAsyncCallbackListener listener : listeners) {
			FailureEvent event = new FailureEvent(caught);
			listener.onFailure(event);
			prop &= event.isProp();
		}
		if (prop)
			realCallback.onFailure(caught);
	}

	@Override
	public void onSuccess(T result) {
		boolean prop = true;
		for (IONOIAsyncCallbackListener listener : listeners) {
			SuccessEvent event = new SuccessEvent(result);
			listener.onSuccess(event);
			prop &= event.isProp();
		}
		if (prop)
			realCallback.onSuccess(result);
	}

	public static void addListener(IONOIAsyncCallbackListener listener) {
		listeners.add(listener);

	}
}
