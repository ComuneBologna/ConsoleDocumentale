package it.eng.portlet.consolepec.spring.bean.gestionepratiche.impl;

import it.eng.consolepec.spagicclient.SpagicClientGestioneMetadatiPratica;
import it.eng.portlet.consolepec.spring.bean.gestionepratiche.GestioneMetadatiPratica;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author biagiot
 *
 */
public class GestioneMetadatiPraticaImpl implements GestioneMetadatiPratica {

	Logger logger = LoggerFactory.getLogger(GestioneMetadatiPraticaImpl.class);

	@Autowired
	SpagicClientGestioneMetadatiPratica spagicClientGestioneMetadatiPratica;

	@Autowired
	UserSessionUtil userSessionUtil;

	@Override
	public Map<String, String> getEtichetteMetadatiMap(String tipoPratica) {
		logger.info("Recupero etichette metadati");
		return spagicClientGestioneMetadatiPratica.getEtichetteMetadatiMap(tipoPratica, userSessionUtil.getUtenteSpagic());
	}
}
