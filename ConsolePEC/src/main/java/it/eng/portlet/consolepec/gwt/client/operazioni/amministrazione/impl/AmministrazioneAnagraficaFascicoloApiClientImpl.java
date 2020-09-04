package it.eng.portlet.consolepec.gwt.client.operazioni.amministrazione.impl;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaFascicolo;
import it.eng.cobo.consolepec.commons.configurazioni.Azione;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.operazioni.amministrazione.AmministrazioneAnagraficaFascicoloApiClient;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.configurazioni.amministrazione.AmministrazioneAnagraficaFascicoloAction;
import it.eng.portlet.consolepec.gwt.shared.action.configurazioni.amministrazione.AmministrazioneAnagraficaFascicoloAction.Operazione;
import it.eng.portlet.consolepec.gwt.shared.action.configurazioni.amministrazione.AmministrazioneAnagraficaFascicoloResult;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;

public class AmministrazioneAnagraficaFascicoloApiClientImpl implements AmministrazioneAnagraficaFascicoloApiClient {

	private DispatchAsync dispatcher;
	private ConfigurazioniHandler configurazioniHandler;
	
	@Inject
	public AmministrazioneAnagraficaFascicoloApiClientImpl(DispatchAsync dispatcher, ConfigurazioniHandler configurazioniHandler) {
		this.dispatcher = dispatcher;
		this.configurazioniHandler = configurazioniHandler;
	}
	
	@Override
	public void cerca(Map<String, Object> filter, Integer limit, Integer offset, String orderBy, Boolean sort, final AnagraficaFascicoloCallback callback) {

		dispatcher.execute(new AmministrazioneAnagraficaFascicoloAction(filter, limit, offset, orderBy, sort), new AsyncCallback<AmministrazioneAnagraficaFascicoloResult>() {

			@Override
			public void onFailure(Throwable arg0) {
				callback.onError(ConsolePecConstants.ERROR_MESSAGE);
			}

			@Override
			public void onSuccess(AmministrazioneAnagraficaFascicoloResult result) {
				
				if (result.isError()) {
					callback.onError(result.getMessageError());
					
				} else {
					callback.onSuccess(result.getAnagraficheFascicoli(), (int)result.getCount());
				}
			}
		});
	}

	@Override
	public void modifica(AnagraficaFascicolo anagraficaFascicolo, List<Azione> azioni, final AnagraficaFascicoloCallback callback) {
		dispatcher.execute(new AmministrazioneAnagraficaFascicoloAction(Operazione.MODIFICA, anagraficaFascicolo, azioni), new AsyncCallback<AmministrazioneAnagraficaFascicoloResult>() {

			@Override
			public void onFailure(Throwable arg0) {
				callback.onError(ConsolePecConstants.ERROR_MESSAGE);
			}

			@Override
			public void onSuccess(AmministrazioneAnagraficaFascicoloResult result) {
				
				if (result.isError()) {
					callback.onError(result.getMessageError());
					
				} else {
					configurazioniHandler.reloadAnagrafichePratiche();
					callback.onSuccess(result.getAnagraficaFascicolo());
				}
			}
		});
	}

	@Override
	public void inserisci(AnagraficaFascicolo anagraficaFascicolo, List<Azione> azioni, final AnagraficaFascicoloCallback callback) {
		dispatcher.execute(new AmministrazioneAnagraficaFascicoloAction(Operazione.INSERIMENTO, anagraficaFascicolo, azioni), new AsyncCallback<AmministrazioneAnagraficaFascicoloResult>() {

			@Override
			public void onFailure(Throwable arg0) {
				callback.onError(ConsolePecConstants.ERROR_MESSAGE);
			}

			@Override
			public void onSuccess(AmministrazioneAnagraficaFascicoloResult result) {
				
				if (result.isError()) {
					callback.onError(result.getMessageError());
					
				} else {
					configurazioniHandler.reloadAnagrafichePratiche();
					callback.onSuccess(result.getAnagraficaFascicolo());
				}
			}
		});
	}

}
