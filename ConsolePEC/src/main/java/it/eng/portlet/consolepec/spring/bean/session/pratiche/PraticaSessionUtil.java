package it.eng.portlet.consolepec.spring.bean.session.pratiche;

import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.xmlplugin.factory.Pratica;
import it.eng.consolepec.xmlplugin.pratica.email.PraticaEmail;
import it.eng.consolepec.xmlplugin.pratica.email.PraticaEmailIn;
import it.eng.consolepec.xmlplugin.pratica.email.PraticaEmailOut;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.consolepec.xmlplugin.pratica.modulistica.PraticaModulistica;
import it.eng.consolepec.xmlplugin.pratica.template.AbstractTemplate;
import it.eng.consolepec.xmlplugin.pratica.template.TemplateEmail;
import it.eng.portlet.consolepec.gwt.shared.model.TipologiaCaricamento;

import java.io.IOException;

public interface PraticaSessionUtil {

	/**
	 * 
	 * Carica la pratica in sessione
	 * 
	 * @param lockedPratica
	 * @return
	 */
	public Pratica<?> loadPraticaInSessione(LockedPratica lockedPratica);

	/**
	 * Carica la PraticaEmailIn passando un PATH encoded dal client
	 * 
	 * @param alfrescoPath
	 * @return
	 * @throws Exception
	 */
	public PraticaEmailIn loadPraticaEmailInFromEncodedPath(String alfrescoPath, TipologiaCaricamento tipologiaCaricamento) throws Exception;

	/**
	 * Carica la PraticaEmailOut passando un PATH encoded dal client
	 * 
	 * @param alfrescoPath
	 * @return
	 * @throws Exception
	 */
	public PraticaEmailOut loadPraticaEmailOutFromEncodedPath(String alfrescoPath, TipologiaCaricamento tipologiaCaricamento) throws Exception;

	/**
	 * carica il fascicolo passando un PATH encoded dal client
	 * 
	 * @param alfrescoPath
	 * @return
	 * @throws Exception
	 */
	public Fascicolo loadFascicoloFromEncodedPath(String alfrescoPath, TipologiaCaricamento tipologiaCaricamento) throws Exception;

	/**
	 * carica una pratica generica passando un PATH encoded dal client
	 * 
	 * @param encPath
	 * @return
	 * @throws Exception
	 */
	public Pratica<?> loadPraticaFromEncodedPath(String encPath) throws IOException;

	/**
	 * carica una pratica modulistica passando un PATH encoded dal client
	 * 
	 * @param encPath
	 * @return
	 * @throws Exception
	 */
	public PraticaModulistica loadPraticaModulisticaFromEncodedPath(String clientID, TipologiaCaricamento tipologiaCaricamento) throws Exception;
	
	
	/**
	 * carica un modello passando un PATH encoded dal client
	 * 
	 * @param encPath
	 * @return
	 * @throws Exception
	 */
	public <T extends AbstractTemplate<?>> T loadModelloFromEncodedPath(String clientID, TipologiaCaricamento tipologiaCaricamento);

	
	/**
	 * rimuove la pratica dalla sessione
	 * 
	 * @param encPath
	 */
	public void removePraticaFromEncodedPath(String encPath);

	/**
	 * carica una pratica generica passando un PATH encoded dal client
	 * 
	 * @param encPath
	 * @return
	 * @throws Exception
	 */
	public Pratica<?> loadPratica(String alfrescoPath);
	
	/**
	 * RIcarica una pratica generica passando un PATH encoded dal client
	 * 
	 * @param alfrescoPath
	 * @param tipologiaCaricamento
	 * @return
	 * @throws Exception
	 */
	Pratica<?> loadPraticaFromEncodedPath(String alfrescoPath, TipologiaCaricamento tipologiaCaricamento) throws Exception;

	/**
	 * Salva in Alfresco una pratica Email
	 * 
	 * @param praticaEmail
	 */
	public PraticaEmail updatePraticaEmail(PraticaEmail praticaEmail);

	/**
	 * Salva in Alfresco una pratica Fascicolo
	 * 
	 * @param fascicolo
	 */
	public Fascicolo updateFascicolo(Fascicolo fascicolo);

	/**
	 * Salva in Alfresco una pratica template
	 * 
	 * @param fascicolo
	 */
	public TemplateEmail updateTemplate(TemplateEmail template);

	/**
	 * Salva in Alfresco una pratica generica
	 * 
	 * @param fascicolo
	 */
	public Pratica<?> updatePratica(Pratica<?> pratica);

	/**
	 * Salva in Alfresco una pratica Modulistica
	 * 
	 * @param praticaModulistica
	 */
	public PraticaModulistica updatePraticaModulistica(PraticaModulistica praticaModulistica);
	
	/**
	 * 
	 * Controlla se la pratica è già in sessione
	 * 
	 * @param alfrescoPath
	 * @return
	 */
	public boolean checkIfPraticaIsInSession(String alfrescoPath);

}