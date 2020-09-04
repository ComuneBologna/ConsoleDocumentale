package it.eng.portlet.consolepec.gwt.client.actionhandler;

import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.RecuperaTipologieProcedimenti;
import it.eng.portlet.consolepec.gwt.shared.action.RecuperaTipologieProcedimentiResult;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.client.CompletedDispatchRequest;
import com.gwtplatform.dispatch.client.actionhandler.AbstractClientActionHandler;
import com.gwtplatform.dispatch.client.actionhandler.ExecuteCommand;
import com.gwtplatform.dispatch.client.actionhandler.UndoCommand;
import com.gwtplatform.dispatch.client.actionhandler.caching.Cache;
import com.gwtplatform.dispatch.shared.DispatchRequest;

public class TipologiaProcedimentoClientActionHandler extends AbstractClientActionHandler<RecuperaTipologieProcedimenti, RecuperaTipologieProcedimentiResult> {

	private Cache cache;

	@Inject
	protected TipologiaProcedimentoClientActionHandler(Cache cache) {
		super(RecuperaTipologieProcedimenti.class);
		this.cache = cache;
		this.cache.setAutoExpireTimeInMs(ConsolePecConstants.AUTO_EXPIRE_TIME_IN_MS);
	}

	@Override
	public DispatchRequest execute(final RecuperaTipologieProcedimenti action, final AsyncCallback<RecuperaTipologieProcedimentiResult> resultCallback, ExecuteCommand<RecuperaTipologieProcedimenti, RecuperaTipologieProcedimentiResult> executeCommand) {
		RecuperaTipologieProcedimentiResult result = (RecuperaTipologieProcedimentiResult) this.cache.get(action);

		if (result != null && result.getTipologiaProcedimenti().size() != 0) {
			resultCallback.onSuccess(result);
			return new CompletedDispatchRequest();
		} else {
			return executeCommand.execute(action, new AsyncCallback<RecuperaTipologieProcedimentiResult>() {
				@Override
				public void onSuccess(RecuperaTipologieProcedimentiResult result) {
					if (!result.getError()) {
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
	public DispatchRequest undo(RecuperaTipologieProcedimenti action, RecuperaTipologieProcedimentiResult result, AsyncCallback<Void> callback, UndoCommand<RecuperaTipologieProcedimenti, RecuperaTipologieProcedimentiResult> undoCommand) {
		// TODO Auto-generated method stub
		return null;
	}

}
