package it.eng.cobo.consolepec.commons.configurazioni.abilitazione;

import it.eng.cobo.consolepec.commons.configurazioni.Operazione;

import java.util.List;


public interface ModificaPraticaAbilitazione extends Abilitazione {
	
	String getTipo();
	List<Operazione> getOperazioni();
}
