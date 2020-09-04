package it.eng.portlet.consolepec.gwt.client.actionhandler;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.client.CompletedDispatchRequest;
import com.gwtplatform.dispatch.client.actionhandler.AbstractClientActionHandler;
import com.gwtplatform.dispatch.client.actionhandler.ExecuteCommand;
import com.gwtplatform.dispatch.client.actionhandler.UndoCommand;
import com.gwtplatform.dispatch.client.actionhandler.caching.Cache;
import com.gwtplatform.dispatch.shared.DispatchRequest;

import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.RecuperaGruppiVisibilita;
import it.eng.portlet.consolepec.gwt.shared.action.RecuperaGruppiVisibilitaResult;

public class GruppiVisibilitaClientActionHandler extends AbstractClientActionHandler<RecuperaGruppiVisibilita, RecuperaGruppiVisibilitaResult> {

	private Cache cache;

	@Inject
	protected GruppiVisibilitaClientActionHandler(Cache cache) {
		super(RecuperaGruppiVisibilita.class);
		this.cache = cache;
		this.cache.setAutoExpireTimeInMs(ConsolePecConstants.AUTO_EXPIRE_TIME_IN_MS);
	}

	@Override
	public DispatchRequest execute(final RecuperaGruppiVisibilita action, final AsyncCallback<RecuperaGruppiVisibilitaResult> resultCallback,
			ExecuteCommand<RecuperaGruppiVisibilita, RecuperaGruppiVisibilitaResult> executeCommand) {
		RecuperaGruppiVisibilitaResult result = (RecuperaGruppiVisibilitaResult) this.cache.get(action);

		if (result != null && result.getGruppoVisibilita().size() != 0) {
			resultCallback.onSuccess(result);
			return new CompletedDispatchRequest();
		} else {
			return executeCommand.execute(action, new AsyncCallback<RecuperaGruppiVisibilitaResult>() {
				@Override
				public void onSuccess(RecuperaGruppiVisibilitaResult result) {
					if (!result.isError()) {
						cache.put(action, result);
					}
					resultCallback.onSuccess(result);
				}

				@Override
				public void onFailure(Throwable caught) {
					resultCallback.onFailure(caught);
				}
			});
		}

	}

	@Override
	public DispatchRequest undo(RecuperaGruppiVisibilita action, RecuperaGruppiVisibilitaResult result, AsyncCallback<Void> callback,
			UndoCommand<RecuperaGruppiVisibilita, RecuperaGruppiVisibilitaResult> undoCommand) {
		return null;
	}

}
