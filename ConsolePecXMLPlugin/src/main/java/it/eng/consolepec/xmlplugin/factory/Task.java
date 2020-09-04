package it.eng.consolepec.xmlplugin.factory;

import java.util.Map;
import java.util.Set;

import it.eng.consolepec.xmlplugin.factory.DatiPratica.Utente;
import it.eng.consolepec.xmlplugin.tasks.esecuzioni.ContestoEsecuzione;

/**
 * Le implementazioni dovranno ridefinire opportunamente i metodi equals() e hashCode()
 *
 * @author pluttero
 */
public interface Task<T extends DatiTask> {

	public boolean controllaAbilitazione(ITipoApiTask tipoApiTask);

	public void setOperazioniAbilitate(Set<String> operazioni);

	public Set<String> getOperazioniAbilitate();

	public T getDati();

	public Map<String, Object> getMetadata();

	public boolean isAttivo();

	public Pratica<?> getEnclosingPratica();

	// public void termina();

	public DatiTask.TipoTask getTipo();

	public void riattivaConCheck();

	public void prendiInCarico(Utente user);

	public void rilasciaInCarico(Utente user);

	public void setCurrentUser(String currentUser);

	public String getCurrentUser();

	public boolean isUtenteEsterno();

	public void setUtenteEsterno(boolean utenteEsterno);

	public <TS extends Task<?>> ContestoEsecuzione<TS> getContestoEsecuzione();
}
