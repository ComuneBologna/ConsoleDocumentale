package it.eng.portlet.consolepec.gwt.client.operazioni.amministrazione.impl;

import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaIngresso;
import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.util.configurazioni.ConfigurazioniUtil;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler.AbilitazioniRuoloCallback;
import it.eng.portlet.consolepec.gwt.client.operazioni.amministrazione.AmministrazioneAnagraficaIngressoApiClient;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.configurazioni.amministrazione.AmministrazioneAnagraficaIngressoAction;
import it.eng.portlet.consolepec.gwt.shared.action.configurazioni.amministrazione.AmministrazioneAnagraficaIngressoAction.Operazione;
import it.eng.portlet.consolepec.gwt.shared.action.configurazioni.amministrazione.AmministrazioneAnagraficaIngressoResult;

/**
 * 
 * @author biagiot
 *
 */
public class AmministrazioneAnagraficaIngressoApiClientImpl implements AmministrazioneAnagraficaIngressoApiClient {

	private DispatchAsync dispatcher;
	private ConfigurazioniHandler configurazioniHandler;

	@Inject
	public AmministrazioneAnagraficaIngressoApiClientImpl(DispatchAsync dispatcher, ConfigurazioniHandler configurazioniHandler) {
		this.dispatcher = dispatcher;
		this.configurazioniHandler = configurazioniHandler;
	}

	@Override
	public void cerca(Map<String, Object> filter, Integer limit, Integer offset, String orderBy, Boolean sort, final AnagraficaIngressoCallback callback) {

		if (!filter.containsKey(ConfigurazioniUtil.FILTRO_RICERCA_CONFIGURAZIONE_TIPO_PRATICA)) {
			filter.put(ConfigurazioniUtil.FILTRO_RICERCA_CONFIGURAZIONE_TIPO_PRATICA, TipologiaPratica.EMAIL_IN.getNomeTipologia());
		}

		dispatcher.execute(new AmministrazioneAnagraficaIngressoAction(filter, limit, offset, orderBy, sort), new AsyncCallback<AmministrazioneAnagraficaIngressoResult>() {

			@Override
			public void onFailure(Throwable arg0) {
				callback.onError(ConsolePecConstants.ERROR_MESSAGE);
			}

			@Override
			public void onSuccess(AmministrazioneAnagraficaIngressoResult result) {

				if (result.isError()) {
					callback.onError(result.getErrore());

				} else {
					callback.onSuccess(result.getAnagraficheIngressi(), (int) result.getCount());
				}
			}
		});
	}

	@Override
	public void modifica(AnagraficaIngresso anagraficaIngresso, final AnagraficaIngressoCallback callback) {
		dispatcher.execute(new AmministrazioneAnagraficaIngressoAction(Operazione.MODIFICA, anagraficaIngresso), new AsyncCallback<AmministrazioneAnagraficaIngressoResult>() {

			@Override
			public void onFailure(Throwable arg0) {
				callback.onError(ConsolePecConstants.ERROR_MESSAGE);
			}

			@Override
			public void onSuccess(AmministrazioneAnagraficaIngressoResult result) {

				if (result.isError()) {
					callback.onError(result.getErrore());

				} else {
					configurazioniHandler.reloadAnagrafichePratiche();
					callback.onSuccess(result.getAnagraficaIngressoResponse());
				}
			}
		});
	}

	@Override
	public void inserisci(AnagraficaIngresso anagraficaIngresso, boolean creaEmailOut, final AnagraficaIngressoCallback callback) {
		AmministrazioneAnagraficaIngressoAction action = new AmministrazioneAnagraficaIngressoAction(Operazione.INSERIMENTO, anagraficaIngresso);
		action.setCreaEmailOut(creaEmailOut);

		dispatcher.execute(action, new AsyncCallback<AmministrazioneAnagraficaIngressoResult>() {

			@Override
			public void onFailure(Throwable arg0) {
				callback.onError(ConsolePecConstants.ERROR_MESSAGE);
			}

			@Override
			public void onSuccess(AmministrazioneAnagraficaIngressoResult result) {

				if (result.isError()) {
					callback.onError(result.getErrore());

				} else {
					configurazioniHandler.reloadAnagrafichePratiche();
					callback.onSuccess(result.getAnagraficaIngressoResponse());
				}
			}
		});
	}

	@Override
	public void aggiornaPrimoAssegnatario(String tipologiaEmail, String indirizzoEmail, String nuovoAssegnatario, final AnagraficaIngressoCallback callback) {
		dispatcher.execute(new AmministrazioneAnagraficaIngressoAction(tipologiaEmail, indirizzoEmail, nuovoAssegnatario), new AsyncCallback<AmministrazioneAnagraficaIngressoResult>() {

			@Override
			public void onFailure(Throwable arg0) {
				callback.onError(ConsolePecConstants.ERROR_MESSAGE);
			}

			@Override
			public void onSuccess(final AmministrazioneAnagraficaIngressoResult result) {

				if (result.isError()) {
					callback.onError(result.getErrore());

				} else {
					configurazioniHandler.reloadAnagrafichePratiche();

					configurazioniHandler.caricaAbilitazioniRuoli(new AbilitazioniRuoloCallback() {

						@Override
						public void onSuccess() {
							callback.onSuccess(result.getAnagraficaIngressoResponse());
						}

						@Override
						public void onError() {
							callback.onSuccess(result.getAnagraficaIngressoResponse());
						}
					});
				}
			}
		});
	}

}
