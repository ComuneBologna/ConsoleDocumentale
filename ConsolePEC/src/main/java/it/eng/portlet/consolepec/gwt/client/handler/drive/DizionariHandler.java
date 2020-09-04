package it.eng.portlet.consolepec.gwt.client.handler.drive;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;

import it.eng.cobo.consolepec.commons.drive.Dizionario;
import it.eng.portlet.consolepec.gwt.client.handler.drive.DriveHandler.DriveCallback;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.configurazioni.CaricaDizionariAction;
import it.eng.portlet.consolepec.gwt.shared.action.configurazioni.CaricaDizionariResult;

/**
 * @author Giacomo F.M.
 * @since 2019-06-11
 */
public class DizionariHandler {

	private List<Dizionario> dizionari = new ArrayList<Dizionario>();

	protected List<Dizionario> getDizionari() {
		return new ArrayList<>(dizionari);
	}

	private DispatchAsync dispatchAsync;

	@Inject
	public DizionariHandler(DispatchAsync dispatchAsync) {
		super();
		this.dispatchAsync = dispatchAsync;
	}

	protected void caricaDizionari(final DriveCallback callback) {
		dispatchAsync.execute(new CaricaDizionariAction(), new AsyncCallback<CaricaDizionariResult>() {

			@Override
			public void onFailure(Throwable caught) {
				if (callback == null) {
					throw new IllegalStateException("Errore durante il recupero dei dizionari");
				} else {
					callback.onFailure(ConsolePecConstants.ERROR_MESSAGE);
				}
			}

			@Override
			public void onSuccess(CaricaDizionariResult result) {
				if (result.isError()) {
					if (callback == null) {
						throw new IllegalStateException(result.getErrorMessage());
					} else {
						callback.onFailure(result.getErrorMessage());
					}
				} else {
					dizionari = result.getDizionari();
					if (callback != null)
						callback.onSuccess();
				}
			}
		});
	}

}
