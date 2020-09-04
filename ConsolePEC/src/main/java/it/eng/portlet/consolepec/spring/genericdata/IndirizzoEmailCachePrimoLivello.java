package it.eng.portlet.consolepec.spring.genericdata;

import java.util.List;

import it.eng.cobo.consolepec.commons.datigenerici.IndirizzoEmail;
import it.eng.consolepec.spagicclient.Utente;

/**
 * 
 * @author biagiot
 *
 */
public interface IndirizzoEmailCachePrimoLivello {

	public List<IndirizzoEmail> getAllIndirizziEmail(Utente utente);

	public void insertIndirizzoEmail(String indirizzoEmail);

}
