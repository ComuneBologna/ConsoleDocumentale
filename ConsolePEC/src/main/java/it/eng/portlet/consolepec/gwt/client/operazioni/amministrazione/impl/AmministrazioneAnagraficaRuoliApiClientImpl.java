package it.eng.portlet.consolepec.gwt.client.operazioni.amministrazione.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;

import it.eng.cobo.consolepec.commons.configurazioni.AbilitazioniRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaWorklist.TipoWorklistAttivita;
import it.eng.cobo.consolepec.commons.configurazioni.Azione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.Abilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.GestioneRubricaAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.RicercaLiberaAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.WorklistAbilitazione;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.operazioni.amministrazione.AmministrazioneAnagraficaRuoliApiClient;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.configurazioni.amministrazione.AmministrazioneAnagraficaRuoloAction;
import it.eng.portlet.consolepec.gwt.shared.action.configurazioni.amministrazione.AmministrazioneAnagraficaRuoloResult;

/**
 * 
 * @author biagiot
 *
 */
public class AmministrazioneAnagraficaRuoliApiClientImpl implements AmministrazioneAnagraficaRuoliApiClient {

	private DispatchAsync dispatcher;
	private ConfigurazioniHandler configurazioniHandler;

	@Inject
	public AmministrazioneAnagraficaRuoliApiClientImpl(DispatchAsync dispatcher, ConfigurazioniHandler configurazioniHandler) {
		this.dispatcher = dispatcher;
		this.configurazioniHandler = configurazioniHandler;
	}

	@Override
	public void cerca(Map<String, Object> filter, Integer limit, Integer offset, String orderBy, Boolean sort, final AnagraficaRuoloCallback callback) {

		dispatcher.execute(new AmministrazioneAnagraficaRuoloAction(filter, limit, offset, orderBy, sort), new AsyncCallback<AmministrazioneAnagraficaRuoloResult>() {

			@Override
			public void onFailure(Throwable arg0) {
				callback.onError(ConsolePecConstants.ERROR_MESSAGE);
			}

			@Override
			public void onSuccess(AmministrazioneAnagraficaRuoloResult result) {

				if (result.isError()) {
					callback.onError(result.getErrorMessage());

				} else {
					callback.onSuccess(result.getAnagraficheRuolo(), (int) result.getCount());
				}
			}
		});

	}

	@Override
	public void modifica(final AnagraficaRuolo anagraficaRuolo, final String settore, final List<Abilitazione> abilitazioniDaAggiungere, final List<Abilitazione> abilitazioniDaRimuovere,
			final List<Azione> azioniRuolo, final List<Azione> azioniAbilitazioni, final AnagraficaRuoloCallback callback) {

		AbilitazioniRuolo ar = new AbilitazioniRuolo();
		ar.setRuolo(anagraficaRuolo.getRuolo());
		ar.getAbilitazioni().addAll(abilitazioniDaAggiungere);
		
		List<String> erroriAbilitazioni = null;
		if (!(erroriAbilitazioni = configurazioniHandler.controlloCongruenzaAbilitazioni(ar)).isEmpty()) {
			callback.onError(erroriAbilitazioni.toString());

		} else {
			dispatcher.execute(new AmministrazioneAnagraficaRuoloAction(anagraficaRuolo, settore, abilitazioniDaAggiungere, abilitazioniDaRimuovere, azioniRuolo, azioniAbilitazioni),
					new AsyncCallback<AmministrazioneAnagraficaRuoloResult>() {

						@Override
						public void onFailure(Throwable arg0) {
							callback.onError(ConsolePecConstants.ERROR_MESSAGE);
						}

						@Override
						public void onSuccess(AmministrazioneAnagraficaRuoloResult result) {

							if (result.isError()) {
								callback.onError(result.getErrorMessage());

							} else {
								configurazioniHandler.reloadRuoli();
								callback.onSuccess(result.getAnagraficaRuoloResponse());
							}
						}
					});
		}
	}

	@Override
	public void inserisci(final AnagraficaRuolo anagraficaRuolo, String settore, List<Abilitazione> abilitazioni, List<Azione> azioniRuolo, List<Azione> azioniAbilitazioni,
			final AnagraficaRuoloCallback callback) {

		if (abilitazioni == null) {
			abilitazioni = new ArrayList<>();
		}

		abilitazioni.addAll(getAbilitazioniComuni());

		AbilitazioniRuolo ar = new AbilitazioniRuolo();
		ar.setRuolo(anagraficaRuolo.getRuolo());
		ar.getAbilitazioni().addAll(abilitazioni);
		
		List<String> erroriAbilitazioni = null;
		if (!(erroriAbilitazioni = configurazioniHandler.controlloCongruenzaAbilitazioni(ar)).isEmpty()) {
			callback.onError(erroriAbilitazioni.toString());

		} else {
			dispatcher.execute(new AmministrazioneAnagraficaRuoloAction(anagraficaRuolo, settore, abilitazioni, azioniRuolo, azioniAbilitazioni),
					new AsyncCallback<AmministrazioneAnagraficaRuoloResult>() {

						@Override
						public void onFailure(Throwable arg0) {
							callback.onError(ConsolePecConstants.ERROR_MESSAGE);
						}

						@Override
						public void onSuccess(AmministrazioneAnagraficaRuoloResult result) {

							if (result.isError()) {
								callback.onError(result.getErrorMessage());

							} else {
								configurazioniHandler.reloadRuoli();
								callback.onSuccess(result.getAnagraficaRuoloResponse());
							}
						}
					});
		}

	}

	private List<Abilitazione> getAbilitazioniComuni() {
		List<Abilitazione> abilitazioniComuni = new ArrayList<>();

		abilitazioniComuni.add(new GestioneRubricaAbilitazione());
		abilitazioniComuni.add(new RicercaLiberaAbilitazione());
		abilitazioniComuni.add(new WorklistAbilitazione(TipoWorklistAttivita.WORKLIST_CARTELLA_FIRMA.name(), 0));

		return abilitazioniComuni;
	}

}
