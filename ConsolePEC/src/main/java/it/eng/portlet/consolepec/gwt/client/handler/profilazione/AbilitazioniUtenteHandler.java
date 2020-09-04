package it.eng.portlet.consolepec.gwt.client.handler.profilazione;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.security.handler.AutorizzazioneHandler;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler.ProfilazioneUtenteCallback;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.profilazione.CaricaAbilitazioniUtenteAction;
import it.eng.portlet.consolepec.gwt.shared.action.profilazione.CaricaAbilitazioniUtenteResult;
import it.eng.portlet.consolepec.gwt.shared.action.profilazione.CaricaSupervisoriUtenteAction;
import it.eng.portlet.consolepec.gwt.shared.action.profilazione.CaricaSupervisoriUtenteResult;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * 
 * @author biagiot
 *
 */
public class AbilitazioniUtenteHandler {

	private DispatchAsync dispatchAsync;

	@Getter(AccessLevel.PROTECTED)
	private AutorizzazioneHandler autorizzazioneHandler;

	@Getter(AccessLevel.PROTECTED)
	private Map<String, List<AnagraficaRuolo>> anagraficheRuoliSuperutenti = new HashMap<String, List<AnagraficaRuolo>>();

	@Getter(AccessLevel.PROTECTED)
	private Map<String, List<AnagraficaRuolo>> anagraficheRuoliSuperutentiMatriceVisibilita = new HashMap<String, List<AnagraficaRuolo>>();

	@Inject
	public AbilitazioniUtenteHandler(DispatchAsync dispatchAsync) {
		super();
		this.dispatchAsync = dispatchAsync;
	}

	protected void caricaAnagraficheUtentiSupervisori(final ProfilazioneUtenteCallback callback) {

		dispatchAsync.execute(new CaricaSupervisoriUtenteAction(), new AsyncCallback<CaricaSupervisoriUtenteResult>() {

			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(ConsolePecConstants.ERROR_MESSAGE);
			}

			@Override
			public void onSuccess(CaricaSupervisoriUtenteResult result) {
				if (result.isError()) {
					callback.onFailure(result.getErrorMessage());

				} else {
					anagraficheRuoliSuperutenti = result.getRuoliSuperutenti();
					anagraficheRuoliSuperutentiMatriceVisibilita = result.getRuoliSupervisoriMatriceVisibilita();
					callback.onSuccess();
				}
			}
		});
	}

	protected void caricaAutorizzazioniUtente(final ProfilazioneUtenteCallback callback) {

		dispatchAsync.execute(new CaricaAbilitazioniUtenteAction(), new AsyncCallback<CaricaAbilitazioniUtenteResult>() {

			@Override
			public void onFailure(Throwable arg0) {
				callback.onFailure(ConsolePecConstants.ERROR_MESSAGE);
			}

			@Override
			public void onSuccess(CaricaAbilitazioniUtenteResult result) {

				if (result.isError()) {
					callback.onFailure(result.getErrorMessage());

				} else {
					autorizzazioneHandler = result.getAutorizzazioneHandler();
					callback.onSuccess();
				}
			}
		});
	}

}
