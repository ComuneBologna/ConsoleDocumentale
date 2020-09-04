package it.eng.portlet.consolepec.gwt.client.actionhandler;

import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.GetDatiAssegnaEsterno;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.GetDatiAssegnaEsternoResult;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.client.CompletedDispatchRequest;
import com.gwtplatform.dispatch.client.actionhandler.AbstractClientActionHandler;
import com.gwtplatform.dispatch.client.actionhandler.ExecuteCommand;
import com.gwtplatform.dispatch.client.actionhandler.UndoCommand;
import com.gwtplatform.dispatch.client.actionhandler.caching.Cache;
import com.gwtplatform.dispatch.shared.DispatchRequest;

public class GetDatiAssegnaEsternoClientActionHandler extends AbstractClientActionHandler<GetDatiAssegnaEsterno, GetDatiAssegnaEsternoResult> {

	private Cache cache;

	@Inject
	protected GetDatiAssegnaEsternoClientActionHandler(Cache cache) {
		super(GetDatiAssegnaEsterno.class);
		this.cache = cache;
		this.cache.setAutoExpireTimeInMs(ConsolePecConstants.AUTO_EXPIRE_TIME_IN_MS);
	}

	@Override
	public DispatchRequest execute(final GetDatiAssegnaEsterno action, final AsyncCallback<GetDatiAssegnaEsternoResult> resultCallback, ExecuteCommand<GetDatiAssegnaEsterno, GetDatiAssegnaEsternoResult> executeCommand) {
		GetDatiAssegnaEsternoResult result = (GetDatiAssegnaEsternoResult) this.cache.get(action);

		if (result != null && result.getEmails().size() != 0) {
			resultCallback.onSuccess(result);
			return new CompletedDispatchRequest();
		} else {
			return executeCommand.execute(action, new AsyncCallback<GetDatiAssegnaEsternoResult>() {
				@Override
				public void onSuccess(GetDatiAssegnaEsternoResult result) {
					if (!result.isError()) {
						cache.put(action, result);
						resultCallback.onSuccess(result);
					}
				}
				@Override
				public void onFailure(Throwable caught) {
					resultCallback.onFailure(caught);
				}
			});
		}

	}

	@Override
	public DispatchRequest undo(GetDatiAssegnaEsterno action, GetDatiAssegnaEsternoResult result, AsyncCallback<Void> callback, UndoCommand<GetDatiAssegnaEsterno, GetDatiAssegnaEsternoResult> undoCommand) {
		return null;
	}

}
