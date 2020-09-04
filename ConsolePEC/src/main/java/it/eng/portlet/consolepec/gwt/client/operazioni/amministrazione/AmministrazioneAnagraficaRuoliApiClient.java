package it.eng.portlet.consolepec.gwt.client.operazioni.amministrazione;

import java.util.List;
import java.util.Map;

import it.eng.cobo.consolepec.commons.configurazioni.AbilitazioniRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.Azione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.Abilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.services.AnagraficaRuoloResponse;

/**
 * 
 * @author biagiot
 *
 */
public interface AmministrazioneAnagraficaRuoliApiClient {

	void cerca(Map<String, Object> filter, Integer limit, Integer offset, String orderBy, Boolean sort, AnagraficaRuoloCallback callback);

	void modifica(AnagraficaRuolo anagraficaRuolo, String settore, List<Abilitazione> abilitazioniDaAggiungere, List<Abilitazione> abilitazioniDaRimuovere, List<Azione> azioniRuolo,
			List<Azione> azioniAbilitazioni, AnagraficaRuoloCallback callback);

	void inserisci(AnagraficaRuolo anagraficaRuolo, String settore, List<Abilitazione> abilitazioni, List<Azione> azioniRuolo, List<Azione> azioniAbilitazioni, AnagraficaRuoloCallback callback);

	public static interface AnagraficaRuoloCallback {
		public void onSuccess(List<AnagraficaRuolo> a, int count);

		public void onSuccess(AnagraficaRuoloResponse a);

		public void onError(String error);
	}

	public static interface AbilitazioniCallback {
		public void onSuccess(AbilitazioniRuolo abilitazioniRuolo);

		public void onError(String error);
	}
}
