package it.eng.portlet.consolepec.gwt.client.handler.profilazione;

import it.eng.cobo.consolepec.commons.profilazione.Utente;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler.ProfilazioneUtenteCallback;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.profilazione.CaricaDatiUtenteAction;
import it.eng.portlet.consolepec.gwt.shared.action.profilazione.CaricaDatiUtenteResult;
import lombok.AccessLevel;
import lombok.Getter;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;

/**
 * 
 * @author biagiot
 *
 */
public class DatiUtenteHandler {

	private DispatchAsync dispatchAsync;

	@Getter(AccessLevel.PROTECTED)
	private Utente utente;
		
	@Inject
	public DatiUtenteHandler(DispatchAsync dispatchAsync) {
		super();
		this.dispatchAsync = dispatchAsync;
	}

	protected void caricaDatiUtente(final ProfilazioneUtenteCallback callback) {
		dispatchAsync.execute(new CaricaDatiUtenteAction(), new AsyncCallback<CaricaDatiUtenteResult>() {
			
			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(ConsolePecConstants.ERROR_MESSAGE);
			}

			@Override
			public void onSuccess(CaricaDatiUtenteResult result) {
				
				if (result.isError()) {
					callback.onFailure(result.getErrorMessage());
					
				} else {
					utente = result.getUtente();
					callback.onSuccess();
				}
			}
		});
	}
}
