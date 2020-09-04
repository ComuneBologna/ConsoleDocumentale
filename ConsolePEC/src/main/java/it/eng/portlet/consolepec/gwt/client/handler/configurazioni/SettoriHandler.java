package it.eng.portlet.consolepec.gwt.client.handler.configurazioni;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;

import it.eng.cobo.consolepec.commons.profilazione.Settore;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler.ConfigurazioniCallback;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.configurazioni.CaricaSettoriAction;
import it.eng.portlet.consolepec.gwt.shared.action.configurazioni.CaricaSettoriResult;

/**
 * 
 * @author biagiot
 *
 */
public class SettoriHandler {

	private List<Settore> settori = new ArrayList<Settore>();

	protected List<Settore> getSettori() {
		return new ArrayList<>(settori);
	}

	private DispatchAsync dispatchAsync;

	@Inject
	public SettoriHandler(DispatchAsync dispatchAsync) {
		super();
		this.dispatchAsync = dispatchAsync;
	}

	protected void caricaSettori(final ConfigurazioniCallback callback, boolean ricarica) {

		CaricaSettoriAction action = new CaricaSettoriAction();
		action.setRicarica(ricarica);

		dispatchAsync.execute(action, new AsyncCallback<CaricaSettoriResult>() {

			@Override
			public void onFailure(Throwable arg0) {
				if (callback != null) {
					callback.onFailure(ConsolePecConstants.ERROR_MESSAGE);

				} else {
					throw new IllegalStateException("Errore durante il recupero dei settori");
				}
			}

			@Override
			public void onSuccess(CaricaSettoriResult result) {

				if (result.isError()) {
					if (callback != null) {
						callback.onFailure(result.getErrorMessage());

					} else {
						throw new IllegalStateException(result.getErrorMessage());
					}

				} else {
					settori = result.getSettori();
					Collections.sort(settori, new Comparator<Settore>() {

						@Override
						public int compare(Settore o1, Settore o2) {
							return o1.getNome().compareToIgnoreCase(o2.getNome());
						}
					});

					if (callback != null)
						callback.onSuccess();
				}
			}
		});
	}

}
