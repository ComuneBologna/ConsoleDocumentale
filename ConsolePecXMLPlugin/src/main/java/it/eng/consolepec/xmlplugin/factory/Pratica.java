package it.eng.consolepec.xmlplugin.factory;

import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.PraticaCollegata;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Utente;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * T sarà il tipo di DatiPratica gestito. L'interfaccia espone tutti i metodi per la gestione di una pratica. Le classi implementatrici dovranno, tra l'altro, gestire la coerenza/validazione delle
 * rispettive specializazioni di DatiPratica.
 * 
 * @author pluttero
 * 
 * @param <T>
 */
public interface Pratica<T extends DatiPratica> {
	/**
	 * 
	 */
	public static final String CONSOLE_XML_FILE_NAME = "metadati.xml";

	public static final String CONSOLE_RICEVUTE_FOLDER = "RICEVUTE";

	public Map<String, Object> getMetadataString();

	public T getDati();

	public void putTask(Task<?> task);

	public Set<Task<?>> getTasks();

	public String getAlfrescoPath();

	public void cambiaPath(String newFolderPath);

	public String getSubFolderPath();

	public boolean isRiattivabile();

	public boolean isAttiva();

	public boolean isProtocollaAbilitato();

	public Utente getInCaricoA();
	
	/*
	 * gestione pratiche collegate
	 */
	
	public void addPraticaCollegata(PraticaCollegata pc);
	
	public void removePraticaCollegata(PraticaCollegata pc);
	
	public void removePraticaCollegata(Pratica<? extends DatiPratica> pratica);
	
	public boolean hasPraticaCollegata(PraticaCollegata pc);
	
	public boolean hasPraticaCollegata(Pratica<? extends DatiPratica> pratica);
	
	public boolean hasPraticheCollegate();
	
	public List<PraticaCollegata> getFascicoliCollegati();
	
	public List<PraticaCollegata> getEmailInCollegate();
	
	public List<PraticaCollegata> getEmailOutCollegate();
	
	public List<PraticaCollegata> getModuliCollegati();
	
	public List<PraticaCollegata> getTemplateCollegati();
	
	public List<PraticaCollegata> getAllPraticheCollegate();

	/**
	 * L'implementazione deve garantire che l'handler sia chiamato sincronamente
	 * 
	 * @param allegato
	 * @param handler
	 */
	public void downloadAllegato(Allegato allegato, PraticaObserver.FileDownload handler);

	/**
	 * L'implementazione deve garantire che l'handler sia chiamato sincronamente
	 * 
	 * @param allegato
	 * @param handler
	 */
	public void loadVersioni(Allegato allegato, PraticaObserver.VersionLoad handler);

	/**
	 * L'implementazione deve garantire che l'handler sia chiamato sincronamente
	 * 
	 * @param versionid
	 * @param handler
	 */
	public void downloadVersion(String versionid, PraticaObserver.VersionDownload handler);

	/**
	 * Metodo di richiesta di verifica firma.
	 * 
	 * @param allegato
	 */
	public void verificaFirma(Allegato allegato, PraticaObserver.VerificaFirma handler);

}
