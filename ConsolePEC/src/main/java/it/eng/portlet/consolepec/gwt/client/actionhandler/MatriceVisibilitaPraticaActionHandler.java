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
import it.eng.portlet.consolepec.gwt.shared.action.configurazioni.CaricaMatriceVisibilitaPraticaAction;
import it.eng.portlet.consolepec.gwt.shared.action.configurazioni.CaricaMatriceVisibilitaPraticaResult;

public class MatriceVisibilitaPraticaActionHandler extends AbstractClientActionHandler<CaricaMatriceVisibilitaPraticaAction, CaricaMatriceVisibilitaPraticaResult> {

	private Cache cache;
	
	@Inject
	protected MatriceVisibilitaPraticaActionHandler(Cache cache) {
		super(CaricaMatriceVisibilitaPraticaAction.class);
		this.cache = cache;
		this.cache.setAutoExpireTimeInMs(ConsolePecConstants.AUTO_EXPIRE_TIME_IN_MS);
	}

	@Override
	public DispatchRequest execute(final CaricaMatriceVisibilitaPraticaAction action, final AsyncCallback<CaricaMatriceVisibilitaPraticaResult> resultCallback, ExecuteCommand<CaricaMatriceVisibilitaPraticaAction, CaricaMatriceVisibilitaPraticaResult> executeCommand) {
		CaricaMatriceVisibilitaPraticaResult result = (CaricaMatriceVisibilitaPraticaResult) this.cache.get(action);
		
		if (result != null && !result.isError() && result.getRuoli() != null && !result.getRuoli().isEmpty()) {
			resultCallback.onSuccess(result);
			return new CompletedDispatchRequest();
			
		} else {
			return executeCommand.execute(action, new AsyncCallback<CaricaMatriceVisibilitaPraticaResult>() {
				@Override
				public void onSuccess(CaricaMatriceVisibilitaPraticaResult result) {
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
	public DispatchRequest undo(CaricaMatriceVisibilitaPraticaAction action, CaricaMatriceVisibilitaPraticaResult result, AsyncCallback<Void> callback, UndoCommand<CaricaMatriceVisibilitaPraticaAction, CaricaMatriceVisibilitaPraticaResult> undoCommand) {
		return null;
	}

}
