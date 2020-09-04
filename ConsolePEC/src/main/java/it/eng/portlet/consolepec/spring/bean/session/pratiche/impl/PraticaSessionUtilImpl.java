package it.eng.portlet.consolepec.spring.bean.session.pratiche.impl;

import it.eng.consolepec.spagicclient.SpagicClientHandleLockedMetadata;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.xmlplugin.factory.Pratica;
import it.eng.consolepec.xmlplugin.factory.PraticaFactory;
import it.eng.consolepec.xmlplugin.factory.XMLPraticaFactory;
import it.eng.consolepec.xmlplugin.pratica.email.PraticaEmail;
import it.eng.consolepec.xmlplugin.pratica.email.PraticaEmailDaEprotocollo;
import it.eng.consolepec.xmlplugin.pratica.email.PraticaEmailIn;
import it.eng.consolepec.xmlplugin.pratica.email.PraticaEmailOut;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.consolepec.xmlplugin.pratica.modulistica.PraticaModulistica;
import it.eng.consolepec.xmlplugin.pratica.template.AbstractTemplate;
import it.eng.consolepec.xmlplugin.pratica.template.TemplateEmail;
import it.eng.portlet.consolepec.gwt.shared.Base64Utils;
import it.eng.portlet.consolepec.gwt.shared.SessionUtils;
import it.eng.portlet.consolepec.gwt.shared.model.TipologiaCaricamento;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class PraticaSessionUtilImpl implements PraticaSessionUtil {

	private static Logger logger = LoggerFactory.getLogger(PraticaSessionUtilImpl.class);

	@Autowired
	SpagicClientHandleLockedMetadata spagicClientHandleLockedMetadata;

	@Autowired
	UserSessionUtil userSessionUtil;

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eng.portlet.consolepec.gwt.server.session.pratica.IPraticaSessionUtil #loadPraticaEmailInFromEncodedPath(java.lang.String)
	 */
	@Override
	public PraticaEmailIn loadPraticaEmailInFromEncodedPath(String alfrescoPath, TipologiaCaricamento tipologiaCaricamento) throws Exception {
		if (tipologiaCaricamento.equals(TipologiaCaricamento.RICARICA)) {
			removePraticaFromEncodedPath(alfrescoPath);
			logger.debug("Remove encoded path: {}", alfrescoPath);
		}
		logger.debug("Encoded path: {}", alfrescoPath);
		alfrescoPath = Base64Utils.URLdecodeAlfrescoPath(alfrescoPath);
		logger.debug("Decoded path: {}", alfrescoPath);
		Pratica<?> loadPratica = loadPratica(alfrescoPath);
		if (loadPratica instanceof PraticaEmailDaEprotocollo)
			return (PraticaEmailDaEprotocollo) loadPratica;
		else
			return (PraticaEmailIn) loadPratica;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eng.portlet.consolepec.gwt.server.session.pratica.IPraticaSessionUtil #loadPraticaEmailOutFromEncodedPath(java.lang.String)
	 */
	@Override
	public PraticaEmailOut loadPraticaEmailOutFromEncodedPath(String alfrescoPath, TipologiaCaricamento tipologiaCaricamento) throws Exception {
		if (tipologiaCaricamento.equals(TipologiaCaricamento.RICARICA)) {
			removePraticaFromEncodedPath(alfrescoPath);
			logger.debug("Remove encoded path: {}", alfrescoPath);
		}
		logger.debug("Encoded path: {}", alfrescoPath);
		alfrescoPath = Base64Utils.URLdecodeAlfrescoPath(alfrescoPath);
		logger.debug("Decoded path: {}", alfrescoPath);
		return (PraticaEmailOut) loadPratica(alfrescoPath);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eng.portlet.consolepec.gwt.server.session.pratica.IPraticaSessionUtil #loadFascicoloFromEncodedPath(java.lang.String)
	 */
	@Override
	public Fascicolo loadFascicoloFromEncodedPath(String alfrescoPath, TipologiaCaricamento tipologiaCaricamento) throws Exception {
		if (tipologiaCaricamento.equals(TipologiaCaricamento.RICARICA)) {
			removePraticaFromEncodedPath(alfrescoPath);
			logger.debug("Remove encoded path: {}", alfrescoPath);
		}
		logger.debug("Encoded path: {}", alfrescoPath);
		alfrescoPath = Base64Utils.URLdecodeAlfrescoPath(alfrescoPath);
		logger.debug("Decoded path: {}", alfrescoPath);
		return (Fascicolo) loadPratica(alfrescoPath);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil#loadPraticaFromEncodedPath(java.lang.String, it.eng.portlet.consolepec.gwt.shared.model.TipologiaCaricamento)
	 */
	@Override
	public Pratica<?> loadPraticaFromEncodedPath(String alfrescoPath, TipologiaCaricamento tipologiaCaricamento) throws Exception {
		if (tipologiaCaricamento.equals(TipologiaCaricamento.RICARICA)) {
			removePraticaFromEncodedPath(alfrescoPath);
			logger.debug("Remove encoded path: {}", alfrescoPath);
		}
		logger.debug("Encoded path: {}", alfrescoPath);
		alfrescoPath = Base64Utils.URLdecodeAlfrescoPath(alfrescoPath);
		logger.debug("Decoded path: {}", alfrescoPath);
		return loadPratica(alfrescoPath);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eng.portlet.consolepec.gwt.server.session.pratica.IPraticaSessionUtil #loadPraticaFromEncodedPath(java.lang.String)
	 */
	@Override
	public PraticaModulistica loadPraticaModulisticaFromEncodedPath(String alfrescoPath, TipologiaCaricamento tipologiaCaricamento) throws Exception {
		if (tipologiaCaricamento.equals(TipologiaCaricamento.RICARICA)) {
			removePraticaFromEncodedPath(alfrescoPath);
			logger.debug("Remove encoded path: {}", alfrescoPath);
		}
		logger.debug("Encoded path: {}", alfrescoPath);
		alfrescoPath = Base64Utils.URLdecodeAlfrescoPath(alfrescoPath);
		logger.debug("Decoded path: {}", alfrescoPath);
		return (PraticaModulistica) loadPratica(alfrescoPath);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends AbstractTemplate<?>> T loadModelloFromEncodedPath(String alfrescoPath, TipologiaCaricamento tipologiaCaricamento) {
		if (tipologiaCaricamento.equals(TipologiaCaricamento.RICARICA)) {
			removePraticaFromEncodedPath(alfrescoPath);
			logger.debug("Remove encoded path: {}", alfrescoPath);
		}
		logger.debug("Encoded path: {}", alfrescoPath);
		alfrescoPath = Base64Utils.URLdecodeAlfrescoPath(alfrescoPath);
		logger.debug("Decoded path: {}", alfrescoPath);
		return (T) loadPratica(alfrescoPath);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eng.portlet.consolepec.gwt.server.session.pratica.IPraticaSessionUtil #loadPraticaFromEncodedPath(java.lang.String)
	 */
	@Override
	public Pratica<?> loadPraticaFromEncodedPath(String encPath) throws IOException {
		logger.debug("Encoded path: {}", encPath);
		String path = Base64Utils.URLdecodeAlfrescoPath(encPath);
		logger.debug("Decoded path: {}", path);
		return loadPratica(path);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eng.portlet.consolepec.gwt.server.session.pratica.IPraticaSessionUtil #loadPratica(java.lang.String)
	 */
	@Override
	public Pratica<?> loadPratica(String alfrescoPath) {
		if (alfrescoPath == null) {
			logger.error("alfresco path è null");
			throw new IllegalArgumentException("alfresco path è null");
		}
		ConsolePecSessionBean consolePecSessionBean = getConsolePecSessionBean();
		// Recupero la pratica dalla sessione
		String encPath = Base64Utils.URLencodeAlfrescoPath(alfrescoPath);
		PraticaSessionBean praticaSessionBean = consolePecSessionBean.getPratica(encPath);
		// TODO riabilitare quando gestiamo la chiamata di svuotamento dal
		// client
		if (praticaSessionBean == null) {
			// Recupero la pratica da alfresco
			LockedPratica loadMetadatiXml = spagicClientHandleLockedMetadata.loadMetadatiXml(alfrescoPath, userSessionUtil.getUtenteSpagic());
			praticaSessionBean = getSessionBean(loadMetadatiXml);
			// Inserisco la pratica nella sessione
			consolePecSessionBean.putPratica(encPath, praticaSessionBean);
		}
		return praticaSessionBean.getPratica();

	}

	private ConsolePecSessionBean getConsolePecSessionBean() {
		ConsolePecSessionBean consolePecSessionBean = null;
		// Recupero l'oggetto ConsolePecSessionBean dalla sessione
		Object sessionBean = userSessionUtil.getHttpSession().getAttribute(SessionUtils.HTTP_SESSION_KEY);
		if (sessionBean != null) {
			consolePecSessionBean = (ConsolePecSessionBean) sessionBean;
		} else {
			consolePecSessionBean = new ConsolePecSessionBean();
			userSessionUtil.getHttpSession().setAttribute(SessionUtils.HTTP_SESSION_KEY, consolePecSessionBean);
		}
		return consolePecSessionBean;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eng.portlet.consolepec.gwt.server.session.pratica.IPraticaSessionUtil #updatePraticaEmail (it.eng.consolepec.xmlplugin.pratica.email.PraticaEmail)
	 */
	@Override
	public PraticaEmail updatePraticaEmail(PraticaEmail praticaEmail) {
		return (PraticaEmail) updatePratica(praticaEmail);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eng.portlet.consolepec.gwt.server.session.pratica.IPraticaSessionUtil #updateFascicolo(it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo)
	 */
	@Override
	public Fascicolo updateFascicolo(Fascicolo fascicolo) {
		return (Fascicolo)updatePratica(fascicolo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eng.portlet.consolepec.gwt.server.session.pratica.IPraticaSessionUtil #updateFascicolo(it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo)
	 */
	@Override
	public PraticaModulistica updatePraticaModulistica(PraticaModulistica praticaModulistica) {
		return (PraticaModulistica)updatePratica(praticaModulistica);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eng.portlet.consolepec.gwt.server.session.pratica.IPraticaSessionUtil #updateTemplate(it.eng.consolepec.xmlplugin.pratica.template.Template)
	 */
	@Override
	public TemplateEmail updateTemplate(TemplateEmail template) {
		return (TemplateEmail) updatePratica(template);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eng.portlet.consolepec.gwt.server.session.pratica.IPraticaSessionUtil #updatePratica(it.eng.consolepec.xmlplugin.factory.Pratica)
	 */
	@Override
	public Pratica<?> updatePratica(Pratica<?> pratica) {

		ConsolePecSessionBean sessionBean = (ConsolePecSessionBean) userSessionUtil.getHttpSession().getAttribute(SessionUtils.HTTP_SESSION_KEY);
		String encPath = Base64Utils.URLencodeAlfrescoPath(pratica.getAlfrescoPath());
		PraticaSessionBean praticaSessionBean = sessionBean.getPratica(encPath);
		logger.debug("Pratica in sessione. Alfresco path: {}, Version (precedente alla modifica): {}", pratica.getAlfrescoPath(), praticaSessionBean.getVersion());

		// aggiorno la pratica in alfresco
		XMLPraticaFactory pf = new XMLPraticaFactory();
		StringWriter stringWriter = new StringWriter();
		pf.serializePraticaInstance(stringWriter, pratica);
		// viene mantenuta la firma per la retrocompatibilità con le pprecedenti versioni
		LockedPratica lockedPratica = spagicClientHandleLockedMetadata.updateMetadatiXml(stringWriter.toString(), "" + praticaSessionBean.getVersion(), userSessionUtil.getUtenteSpagic());
		logger.debug("Pratica aggiornata in Alfresco");

		// pratica con versione aggiornata
		pratica = pf.loadPratica(new StringReader(lockedPratica.getMetadatiXml()));

		praticaSessionBean.setPratica(pratica);
		praticaSessionBean.setVersion(lockedPratica.getHash());
		sessionBean.putPratica(encPath, praticaSessionBean);
		logger.debug("Pratica in sessione. Alfresco path: {}, Version (successivo alla modifica): {}", pratica.getAlfrescoPath(), praticaSessionBean.getVersion());

		return pratica;
	}

	@Override
	public Pratica<?> loadPraticaInSessione(LockedPratica lockedPratica) {
		PraticaSessionBean sessionBean = getSessionBean(lockedPratica);
		ConsolePecSessionBean consolePecSessionBean = (ConsolePecSessionBean) userSessionUtil.getHttpSession().getAttribute(SessionUtils.HTTP_SESSION_KEY);

		if (consolePecSessionBean == null) {
			consolePecSessionBean = new ConsolePecSessionBean();
			userSessionUtil.getHttpSession().setAttribute(SessionUtils.HTTP_SESSION_KEY, consolePecSessionBean);
		}

		String encPath = Base64Utils.URLencodeAlfrescoPath(sessionBean.getPratica().getAlfrescoPath());
		consolePecSessionBean.putPratica(encPath, sessionBean);
		return sessionBean.getPratica();
	}

	public static PraticaSessionBean getSessionBean(LockedPratica lockedPratica) {
		PraticaFactory praticaFactory = new XMLPraticaFactory();
		Pratica<?> pratica = praticaFactory.loadPratica(new StringReader(lockedPratica.getMetadatiXml()));
		return new PraticaSessionBean(lockedPratica.getHash(), pratica);
	}

	@Override
	public void removePraticaFromEncodedPath(String encPath) {
		if (getConsolePecSessionBean().removePratica(encPath) == null)
			logger.warn("Richiesta rimozione di pratica con path {} non trovata in sessione", encPath);

	}

	@Override
	public boolean checkIfPraticaIsInSession(String alfrescoPath) {
		ConsolePecSessionBean consolePecSessionBean = getConsolePecSessionBean();
		String encPath = Base64Utils.URLencodeAlfrescoPath(alfrescoPath);
		PraticaSessionBean praticaSessionBean = consolePecSessionBean.getPratica(encPath);
		return praticaSessionBean != null;
	}

}
