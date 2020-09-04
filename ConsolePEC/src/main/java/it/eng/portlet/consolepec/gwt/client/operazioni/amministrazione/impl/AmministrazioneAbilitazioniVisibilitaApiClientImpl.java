package it.eng.portlet.consolepec.gwt.client.operazioni.amministrazione.impl;

import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.VisibilitaComunicazioneAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.VisibilitaFascicoloAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.VisibilitaModelloAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.VisibilitaPraticaModulisticaAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.VisibilitaRuoloAbilitazione;
import it.eng.cobo.consolepec.util.configurazioni.ConfigurazioniUtil;
import it.eng.portlet.consolepec.gwt.client.operazioni.amministrazione.AmministrazioneAbilitazioniVisibilitaApiClient;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.configurazioni.amministrazione.AmministrazioneAbilitazioniRuoloAction;
import it.eng.portlet.consolepec.gwt.shared.action.configurazioni.amministrazione.AmministrazioneAbilitazioniRuoloResult;

import java.util.Arrays;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;

/**
 * 
 * @author biagiot
 *
 */
public class AmministrazioneAbilitazioniVisibilitaApiClientImpl implements AmministrazioneAbilitazioniVisibilitaApiClient {

	private DispatchAsync dispatcher;
	
	@Inject
	public AmministrazioneAbilitazioniVisibilitaApiClientImpl(DispatchAsync dispatcher) {
		this.dispatcher = dispatcher;
	}
	
	@Override
	public void cerca(final Map<String, Object> filter, final Integer limit, Integer offset, String orderBy, Boolean sort, final AbilitazioneCallback callback) {
		
		if (filter.get(ConfigurazioniUtil.FILTRO_RICERCA_ABILITAZIONI_RUOLO_TIPO_ABILITAZIONE) == null) {
			filter.put(ConfigurazioniUtil.FILTRO_RICERCA_ABILITAZIONI_RUOLO_TIPO_ABILITAZIONE, Arrays.asList(VisibilitaFascicoloAbilitazione.class.getSimpleName(), 
					VisibilitaComunicazioneAbilitazione.class.getSimpleName(), VisibilitaModelloAbilitazione.class.getSimpleName(), 
					VisibilitaPraticaModulisticaAbilitazione.class.getSimpleName(), VisibilitaRuoloAbilitazione.class.getSimpleName()));
		}
		
		dispatcher.execute(new AmministrazioneAbilitazioniRuoloAction(filter, limit, offset, orderBy, sort), new AsyncCallback<AmministrazioneAbilitazioniRuoloResult>() {

			@Override
			public void onFailure(Throwable arg0) {
				callback.onError(ConsolePecConstants.ERROR_MESSAGE);
			}

			@Override
			public void onSuccess(AmministrazioneAbilitazioniRuoloResult res) {
				
				if (res.isError()) {
					callback.onError(res.getErrorMessage());
					
				} else {
					callback.onSuccess(res.getAbilitazioniRuoloSingole(), res.getCount());
				}
			}
		});
	}
}
