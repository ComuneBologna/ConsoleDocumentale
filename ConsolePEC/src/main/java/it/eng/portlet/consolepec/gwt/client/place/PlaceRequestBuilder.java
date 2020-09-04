package it.eng.portlet.consolepec.gwt.client.place;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;

/**
 * Interfaccia ad uso delle view, per ottenere la generazione di un token per hyperlink
 * 
 * @author pluttero
 * 
 */
public interface PlaceRequestBuilder {
	/**
	 * Dato il token, il metodo costruisce la stringa da passare ad un hyperlink, completa di eventuali parametri
	 * 
	 * @param token
	 * @return
	 */
	public String getRequestString(String token);

	/**
	 * Dato il token e il tipo della pratica il metodo costruisce la stringa da passare ad un hyperlink, completa di eventuali parametri
	 * 
	 * @param token
	 * @return
	 */
	public String getRequestString(TipologiaPratica tipoPratica, String token);
}
