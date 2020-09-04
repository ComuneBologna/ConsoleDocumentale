package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni;

import java.util.Set;


public interface AssegnaUtenteEsternoTaskApi extends ITaskApi {
	public void assegnaEsterno(Set<String> destinatari, String testoEmail, Set<String> operazioni);
}
