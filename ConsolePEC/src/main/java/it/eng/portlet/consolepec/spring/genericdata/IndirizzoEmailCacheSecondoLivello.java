package it.eng.portlet.consolepec.spring.genericdata;

import it.eng.cobo.consolepec.commons.profilazione.Utente;

import java.util.List;

/**
 * 
 * @author biagiot
 *
 */
public interface IndirizzoEmailCacheSecondoLivello {
	
	public List<String> getIndirizziEmail(String chiave, Utente utente);
	
	public void checkIndirizzoEmail(String indirizzoEmail, Utente utente);
}
