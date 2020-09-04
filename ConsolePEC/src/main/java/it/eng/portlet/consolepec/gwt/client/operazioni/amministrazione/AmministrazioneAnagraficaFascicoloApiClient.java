package it.eng.portlet.consolepec.gwt.client.operazioni.amministrazione;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaFascicolo;
import it.eng.cobo.consolepec.commons.configurazioni.Azione;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author biagiot
 *
 */
public interface AmministrazioneAnagraficaFascicoloApiClient {
	
	void cerca(Map<String, Object> filter, Integer limit, Integer offset, String orderBy, Boolean sort, AnagraficaFascicoloCallback callback);
	void modifica(AnagraficaFascicolo anagraficaRuolo, List<Azione> azioni, AnagraficaFascicoloCallback callback);
	void inserisci(AnagraficaFascicolo anagraficaRuolo, List<Azione> azioni, AnagraficaFascicoloCallback callback);

	public static interface AnagraficaFascicoloCallback {
		public void onSuccess(List<AnagraficaFascicolo> a, int count);
		public void onSuccess(AnagraficaFascicolo a);
		public abstract void onError(String error);
	}
}
