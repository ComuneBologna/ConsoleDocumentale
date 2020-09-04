package it.eng.portlet.consolepec.gwt.client.handler.drive;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;

import it.eng.cobo.consolepec.commons.drive.Nomenclatura;
import it.eng.portlet.consolepec.gwt.client.handler.drive.DriveHandler.DriveCallback;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.configurazioni.CaricaNomenclatureAction;
import it.eng.portlet.consolepec.gwt.shared.action.configurazioni.CaricaNomenclatureResult;

/**
 * @author Giacomo F.M.
 * @since 2019-07-08
 */
public class NomenclatureHandler {

	private Map<String, Nomenclatura> map = new HashMap<>();

	protected Map<String, Nomenclatura> getNomenclature() {
		return new HashMap<>(map);
	}

	private DispatchAsync dispatchAsync;

	@Inject
	public NomenclatureHandler(DispatchAsync dispatchAsync) {
		super();
		this.dispatchAsync = dispatchAsync;
	}

	protected void caricaNomenclature(final DriveCallback callback) {
		dispatchAsync.execute(new CaricaNomenclatureAction(), new AsyncCallback<CaricaNomenclatureResult>() {
			@Override
			public void onFailure(Throwable caught) {
				if (callback == null) {
					throw new IllegalStateException("Errore durante il recupero delle nomenclature");
				} else {
					callback.onFailure(ConsolePecConstants.ERROR_MESSAGE);
				}
			}

			@Override
			public void onSuccess(CaricaNomenclatureResult result) {
				if (result.isError()) {
					if (callback == null) {
						throw new IllegalStateException(result.getErrorMessage());
					} else {
						callback.onFailure(result.getErrorMessage());
					}
				} else {
					for (Nomenclatura n : result.getNomenclature()) {
						map.put(n.getNome(), n);
					}
					if (callback != null)
						callback.onSuccess();
				}
			}
		});
	}

}
