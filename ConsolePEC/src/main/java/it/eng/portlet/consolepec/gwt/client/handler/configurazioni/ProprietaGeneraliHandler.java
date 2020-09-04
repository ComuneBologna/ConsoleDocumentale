package it.eng.portlet.consolepec.gwt.client.handler.configurazioni;

import it.eng.cobo.consolepec.commons.configurazioni.properties.ProprietaGenerali;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler.ConfigurazioniCallback;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.configurazioni.CaricaProprietaGeneraliAction;
import it.eng.portlet.consolepec.gwt.shared.action.configurazioni.CaricaProprietaGeneraliResult;
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
public class ProprietaGeneraliHandler {

	private DispatchAsync dispatchAsync;
	
	@Getter(AccessLevel.PROTECTED)
	private ProprietaGenerali proprietaGenerali;
	
	@Getter(AccessLevel.PROTECTED)
	private String baseUrlPubblicazioneAllegato;

	@Inject
	public ProprietaGeneraliHandler(DispatchAsync dispatchAsync) {
		super();
		this.dispatchAsync = dispatchAsync;
	}
	
	protected void caricaProprietaGenerali(final ConfigurazioniCallback callback) {

		dispatchAsync.execute(new CaricaProprietaGeneraliAction(), new AsyncCallback<CaricaProprietaGeneraliResult>() {

			@Override
			public void onFailure(Throwable t) {
				callback.onFailure(ConsolePecConstants.ERROR_MESSAGE);
			}

			@Override
			public void onSuccess(CaricaProprietaGeneraliResult result) {
				
				if (result.isError()) {
					callback.onFailure(result.getErrorMessage());
					
				} else {
					proprietaGenerali = result.getGeneralProperties();
					baseUrlPubblicazioneAllegato = result.getBaseUrlPubblicazioneAllegato();
					callback.onSuccess();
				}
			}
		});
	}
}
