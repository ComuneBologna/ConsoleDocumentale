package it.eng.cobo.consolepec.commons.configurazioni;

import java.util.Date;
import java.util.List;

public interface Configurabile {
	
	List<Azione> getAzioni();
	Date getDataCreazione();
	String getUsernameCreazione();
	
}
