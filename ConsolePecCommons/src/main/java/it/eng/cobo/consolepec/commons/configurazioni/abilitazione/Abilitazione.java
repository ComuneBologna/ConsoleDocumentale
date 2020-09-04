package it.eng.cobo.consolepec.commons.configurazioni.abilitazione;

import java.io.Serializable;
import java.util.Date;

/**
 * Abilitazione generica
 * 
 * @author biagiot
 *
 */
public interface Abilitazione extends Serializable {

	void setUsernameCreazione(String usernameCreazione);
	String getUsernameCreazione();
	
	Date getDataCreazione();
	void setDataCreazione(Date dataCreazione);
	
}
