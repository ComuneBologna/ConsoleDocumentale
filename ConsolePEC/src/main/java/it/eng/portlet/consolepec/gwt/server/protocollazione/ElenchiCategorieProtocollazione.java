package it.eng.portlet.consolepec.gwt.server.protocollazione;

import java.util.Map;

public interface ElenchiCategorieProtocollazione {
	// protocollazione
	// E -> entrata
	// U -> uscita
	// I -> interna
	public enum Categoria {
		ENTRATA, USCITA, INTERNA
	};

	/**
	 * Metodo per ottenere una mappa delle categorie di protocollazione.
	 * 
	 * @param categoria
	 * @return - mappa chiave,valore delle categorie di protocollazione, così come definite da spagic
	 */
	public Map<String, String> getCategorieProtocollazione(Categoria categoria);
}
