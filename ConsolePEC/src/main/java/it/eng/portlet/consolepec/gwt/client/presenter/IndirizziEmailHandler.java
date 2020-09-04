package it.eng.portlet.consolepec.gwt.client.presenter;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;

import it.eng.portlet.consolepec.gwt.shared.action.genericdata.CaricaIndirizziEmailAction;
import it.eng.portlet.consolepec.gwt.shared.action.genericdata.CaricaIndirizziEmailActionResult;

/**
 * 
 * @author biagiot
 *
 */
public class IndirizziEmailHandler {

	private DispatchAsync dispatchAsync;

	@Inject
	public IndirizziEmailHandler(DispatchAsync dispatchAsync) {
		super();
		this.dispatchAsync = dispatchAsync;
	}

	public void getIndirizziEmail(String chiave, final OnIndirizziEmailLoaded callback) {
		dispatchAsync.execute(new CaricaIndirizziEmailAction(chiave), new AsyncCallback<CaricaIndirizziEmailActionResult>() {

			@Override
			public void onFailure(Throwable caught) {
				// NOP
			}

			@Override
			public void onSuccess(CaricaIndirizziEmailActionResult result) {
				callback.onIndirizziEmailLoaded(result.getIndirizziEmail());
			}

		});
	}

	public static interface OnIndirizziEmailLoaded {
		void onIndirizziEmailLoaded(List<String> indirizziEmail);
	}

	public void caricaCache() {
		dispatchAsync.execute(new CaricaIndirizziEmailAction(true), new AsyncCallback<CaricaIndirizziEmailActionResult>() {

			@Override
			public void onFailure(Throwable caught) {
				// NOP
			}

			@Override
			public void onSuccess(CaricaIndirizziEmailActionResult result) {

			}
		});
	}
}
