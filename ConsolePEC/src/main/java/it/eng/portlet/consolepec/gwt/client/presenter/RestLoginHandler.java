package it.eng.portlet.consolepec.gwt.client.presenter;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;

import it.eng.portlet.consolepec.gwt.client.PostLoadingAction;
import it.eng.portlet.consolepec.gwt.shared.rest.RestLoginAction;
import it.eng.portlet.consolepec.gwt.shared.rest.RestLoginResult;

/**
 * @author Giacomo F.M.
 * @since 2019-07-04
 */
public class RestLoginHandler {

	private DispatchAsync dispatchAsync;

	@Inject
	public RestLoginHandler(final DispatchAsync dispatchAsync) {
		super();
		this.dispatchAsync = dispatchAsync;
	}

	public void eseguiRestLogin(final PostLoadingAction action) {
		dispatchAsync.execute(new RestLoginAction(), new AsyncCallback<RestLoginResult>() {

			@Override
			public void onFailure(Throwable caught) {
				throw new IllegalStateException(caught);
			}

			@Override
			public void onSuccess(RestLoginResult result) {
				if (result.isError()) {
					// TODO: in caso di errore nella login dei servizi rest sarebbe da disabilitare le parti che li usano
					if (action != null)
						action.onComplete();
				}

				if (action != null)
					action.onComplete();
			}
		});
	}

}
