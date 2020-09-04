package it.eng.portlet.consolepec.gwt.client.handler.configurazioni;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaComunicazione;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaEmailOut;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaFascicolo;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaIngresso;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaModello;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaPraticaModulistica;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler.ConfigurazioniCallback;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.configurazioni.CaricaAnagrafichePraticheAction;
import it.eng.portlet.consolepec.gwt.shared.action.configurazioni.CaricaAnagrafichePraticheResult;

/**
 * 
 * @author biagiot
 *
 */
public class AnagrafichePraticheHandler {

	private List<AnagraficaFascicolo> anagraficheFascicoli = new ArrayList<>();
	private List<AnagraficaIngresso> anagraficheIngressi = new ArrayList<>();
	private List<AnagraficaModello> anagraficheModelli = new ArrayList<>();
	private List<AnagraficaEmailOut> anagraficheMailInUscita = new ArrayList<>();
	private List<AnagraficaComunicazione> anagraficheComunicazioni = new ArrayList<>();
	private List<AnagraficaPraticaModulistica> anagrafichePraticaModulistica = new ArrayList<>();

	protected List<AnagraficaFascicolo> getAnagraficheFascicoli() {
		return new ArrayList<>(anagraficheFascicoli);
	}

	protected List<AnagraficaIngresso> getAnagraficheIngressi() {
		return new ArrayList<>(anagraficheIngressi);
	}

	protected List<AnagraficaModello> getAnagraficheModelli() {
		return new ArrayList<>(anagraficheModelli);
	}

	protected List<AnagraficaEmailOut> getAnagraficheMailInUscita() {
		return new ArrayList<>(anagraficheMailInUscita);
	}

	protected List<AnagraficaComunicazione> getAnagraficheComunicazioni() {
		return new ArrayList<>(anagraficheComunicazioni);
	}

	protected List<AnagraficaPraticaModulistica> getAnagrafichePraticaModulistica() {
		return new ArrayList<>(anagrafichePraticaModulistica);
	}

	private DispatchAsync dispatchAsync;

	@Inject
	public AnagrafichePraticheHandler(DispatchAsync dispatchAsync) {
		super();
		this.dispatchAsync = dispatchAsync;
	}

	protected void caricaAnagrafichePratiche(final ConfigurazioniCallback callback, boolean ricarica) {

		CaricaAnagrafichePraticheAction action = new CaricaAnagrafichePraticheAction();
		action.setRicarica(ricarica);

		dispatchAsync.execute(action, new AsyncCallback<CaricaAnagrafichePraticheResult>() {

			@Override
			public void onFailure(Throwable arg0) {
				if (callback != null) {
					callback.onFailure(ConsolePecConstants.ERROR_MESSAGE);

				} else {
					throw new IllegalStateException("Errore durante il recupero delle pratiche");
				}
			}

			@Override
			public void onSuccess(CaricaAnagrafichePraticheResult result) {

				if (result.isError()) {
					if (callback != null) {
						callback.onFailure(result.getErrorMessage());

					} else {
						throw new IllegalStateException(result.getErrorMessage());
					}

				} else {
					if (result.getAnagraficheFascicoli() != null) {
						anagraficheFascicoli = result.getAnagraficheFascicoli();
						Collections.sort(anagraficheFascicoli, new Comparator<AnagraficaFascicolo>() {

							@Override
							public int compare(AnagraficaFascicolo o1, AnagraficaFascicolo o2) {
								int i2 = o1.getNomeTipologia().compareToIgnoreCase(o2.getNomeTipologia());
								int i1 = o1.getEtichettaTipologia().compareToIgnoreCase(o2.getEtichettaTipologia());

								if (i1 == 0)
									return i2;

								return i1;
							}

						});
					}

					if (result.getAnagraficheIngressi() != null) {
						anagraficheIngressi = result.getAnagraficheIngressi();
						Collections.sort(anagraficheIngressi, new Comparator<AnagraficaIngresso>() {

							@Override
							public int compare(AnagraficaIngresso o1, AnagraficaIngresso o2) {
								int i2 = o1.getNomeTipologia().compareToIgnoreCase(o2.getNomeTipologia());
								int i1 = o1.getEtichettaTipologia().compareToIgnoreCase(o2.getEtichettaTipologia());
								int i3 = o1.getIndirizzo().compareToIgnoreCase(o2.getIndirizzo());

								if (i1 != 0)
									return i1;

								if (i2 != 0)
									return i2;

								return i3;
							}
						});
					}

					if (result.getAnagraficheModelli() != null) {
						anagraficheModelli = result.getAnagraficheModelli();
						Collections.sort(anagraficheModelli, new Comparator<AnagraficaModello>() {

							@Override
							public int compare(AnagraficaModello o1, AnagraficaModello o2) {
								int i2 = o1.getNomeTipologia().compareToIgnoreCase(o2.getNomeTipologia());
								int i1 = o1.getEtichettaTipologia().compareToIgnoreCase(o2.getEtichettaTipologia());

								if (i1 == 0)
									return i2;

								return i1;
							}

						});
					}

					if (result.getAnagraficheMailInUscita() != null) {
						anagraficheMailInUscita = result.getAnagraficheMailInUscita();
						Collections.sort(anagraficheMailInUscita, new Comparator<AnagraficaEmailOut>() {

							@Override
							public int compare(AnagraficaEmailOut o1, AnagraficaEmailOut o2) {
								int i2 = o1.getNomeTipologia().compareToIgnoreCase(o2.getNomeTipologia());
								int i1 = o1.getEtichettaTipologia().compareToIgnoreCase(o2.getEtichettaTipologia());
								int i3 = o1.getIndirizzo().compareToIgnoreCase(o2.getIndirizzo());

								if (i1 != 0)
									return i1;

								if (i2 != 0)
									return i2;

								return i3;
							}
						});
					}

					if (result.getAnagraficheComunicazioni() != null) {
						anagraficheComunicazioni = result.getAnagraficheComunicazioni();
						Collections.sort(anagraficheComunicazioni, new Comparator<AnagraficaComunicazione>() {

							@Override
							public int compare(AnagraficaComunicazione o1, AnagraficaComunicazione o2) {
								int i2 = o1.getNomeTipologia().compareToIgnoreCase(o2.getNomeTipologia());
								int i1 = o1.getEtichettaTipologia().compareToIgnoreCase(o2.getEtichettaTipologia());

								if (i1 == 0)
									return i2;

								return i1;
							}
						});
					}

					if (result.getAnagrafichePraticaModulistica() != null) {
						anagrafichePraticaModulistica = result.getAnagrafichePraticaModulistica();
						Collections.sort(anagrafichePraticaModulistica, new Comparator<AnagraficaPraticaModulistica>() {

							@Override
							public int compare(AnagraficaPraticaModulistica o1, AnagraficaPraticaModulistica o2) {
								int i2 = o1.getNomeTipologia().compareToIgnoreCase(o2.getNomeTipologia());
								int i1 = o1.getEtichettaTipologia().compareToIgnoreCase(o2.getEtichettaTipologia());

								if (i1 == 0)
									return i2;

								return i1;
							}
						});
					}

					if (callback != null)
						callback.onSuccess();
				}
			}

		});
	}
}
