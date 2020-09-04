package it.eng.portlet.consolepec.gwt.client.handler.configurazioni;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;

import it.eng.cobo.consolepec.commons.configurazioni.AbilitazioniRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler.AbilitazioniRuoloCallback;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler.ConfigurazioniCallback;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.configurazioni.CaricaAnagraficheRuoliAction;
import it.eng.portlet.consolepec.gwt.shared.action.configurazioni.CaricaAnagraficheRuoliResult;
import it.eng.portlet.consolepec.gwt.shared.action.configurazioni.amministrazione.AmministrazioneAbilitazioniRuoloAction;
import it.eng.portlet.consolepec.gwt.shared.action.configurazioni.amministrazione.AmministrazioneAbilitazioniRuoloResult;

/**
 * 
 * @author biagiot
 *
 */
public class AnagraficheRuoliHandler {

	private List<AnagraficaRuolo> anagraficheRuoli = new ArrayList<AnagraficaRuolo>();
	private List<AnagraficaRuolo> ruoliPersonali = new ArrayList<AnagraficaRuolo>();
	private List<AbilitazioniRuolo> abilitazioniRuolo = new ArrayList<AbilitazioniRuolo>();

	protected List<AnagraficaRuolo> getAnagraficheRuoli() {
		return new ArrayList<>(anagraficheRuoli);
	}

	protected List<AnagraficaRuolo> getRuoliPersonali() {
		return new ArrayList<>(ruoliPersonali);
	}

	protected List<AbilitazioniRuolo> getAbilitazioniRuolo() {
		return new ArrayList<>(abilitazioniRuolo);
	}

	private DispatchAsync dispatchAsync;

	@Inject
	public AnagraficheRuoliHandler(DispatchAsync dispatchAsync) {
		super();
		this.dispatchAsync = dispatchAsync;
	}

	protected void caricaAbilitazioniRuoli(final AbilitazioniRuoloCallback callback) {

		dispatchAsync.execute(new AmministrazioneAbilitazioniRuoloAction(), new AsyncCallback<AmministrazioneAbilitazioniRuoloResult>() {

			@Override
			public void onFailure(Throwable t) {
				if (callback != null) {
					callback.onError();

				} else {
					throw new IllegalStateException("Errore durante il recupero delle abilitazioni");
				}
			}

			@Override
			public void onSuccess(AmministrazioneAbilitazioniRuoloResult res) {
				if (!res.isError()) {

					abilitazioniRuolo = res.getAbilitazioniRuoli();

					if (callback != null) {
						callback.onSuccess();
					}

				} else {
					if (callback != null) {
						callback.onError();

					} else {
						throw new IllegalStateException(res.getErrorMessage());
					}
				}
			}
		});

	}

	protected void caricaAnagraficheRuoli(final ConfigurazioniCallback callback, boolean ricarica) {

		CaricaAnagraficheRuoliAction action = new CaricaAnagraficheRuoliAction();
		action.setRicarica(ricarica);

		dispatchAsync.execute(action, new AsyncCallback<CaricaAnagraficheRuoliResult>() {

			@Override
			public void onFailure(Throwable arg0) {
				if (callback != null) {
					callback.onFailure(ConsolePecConstants.ERROR_MESSAGE);

				} else {
					throw new IllegalStateException("Errore durante il recupero dei ruoli");
				}
			}

			@Override
			public void onSuccess(CaricaAnagraficheRuoliResult result) {

				if (result.isError()) {
					if (callback != null) {
						callback.onFailure(result.getErrorMessage());

					} else {
						throw new IllegalStateException(result.getErrorMessage());
					}

				} else {
					anagraficheRuoli = result.getAnagraficheRuoli();
					Collections.sort(anagraficheRuoli, new Comparator<AnagraficaRuolo>() {

						@Override
						public int compare(AnagraficaRuolo o1, AnagraficaRuolo o2) {
							return o1.getEtichetta().compareToIgnoreCase(o2.getEtichetta());
						}
					});

					ruoliPersonali = result.getRuoliPersonali();

					if (callback != null)
						callback.onSuccess();
				}
			}

		});
	}
}
