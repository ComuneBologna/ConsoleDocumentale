package it.eng.portlet.consolepec.gwt.client.operazioni.amministrazione;

import java.util.List;
import java.util.Map;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaIngresso;
import it.eng.cobo.consolepec.commons.configurazioni.services.AnagraficaIngressoResponse;

/**
 * 
 * @author biagiot
 *
 */
public interface AmministrazioneAnagraficaIngressoApiClient {

	void cerca(Map<String, Object> filter, Integer limit, Integer offset, String orderBy, Boolean sort, AnagraficaIngressoCallback callback);

	void modifica(AnagraficaIngresso anagraficaRuolo, AnagraficaIngressoCallback callback);

	void inserisci(AnagraficaIngresso anagraficaRuolo, boolean creaEmailOut, AnagraficaIngressoCallback callback);

	void aggiornaPrimoAssegnatario(String tipologiaEmail, String indirizzoEmail, String nuovoAssegnatario, AnagraficaIngressoCallback callback);

	public static interface AnagraficaIngressoCallback {
		public void onSuccess(List<AnagraficaIngresso> a, int count);

		public void onSuccess(AnagraficaIngressoResponse a);

		public abstract void onError(String errore);
	}
}
