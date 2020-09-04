package it.eng.portlet.consolepec.gwt.client.operazioni.amministrazione;

import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.util.AbilitazioneRuoloSingola;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author biagiot
 *
 */
public interface AmministrazioneAbilitazioniVisibilitaApiClient {
	
	void cerca(Map<String, Object> filter, Integer limit, Integer offset, String orderBy, Boolean sort, AbilitazioneCallback callback);
	
	public static interface AbilitazioneCallback {
		public void onSuccess(List<AbilitazioneRuoloSingola> a, int count);
		public abstract void onError(String error);
	}
	
	public static enum TipoAbilitazione {
		Fascicolo, Comunicazione, Modello, PModulistica, Gruppo;
		
		public static List<String> labels() {
			List<String> res = new ArrayList<String>();
			for (TipoAbilitazione tipo : TipoAbilitazione.values()) {
				res.add(tipo.toString());
			}
			return res;
		}
		
		public static TipoAbilitazione from(String value) {
			
			try {
				return TipoAbilitazione.valueOf(value);
				
			} catch (Exception e) {
				return null;
			}
			
		}
	}
	
	public static enum TipoAccessoAbilitazione {
		Lettura;
		
		public static TipoAccessoAbilitazione from(String value) {
			
			try {
				return TipoAccessoAbilitazione.valueOf(value);
				
			} catch (Exception e) {
				return null;
			}
			
		}
	}
}
