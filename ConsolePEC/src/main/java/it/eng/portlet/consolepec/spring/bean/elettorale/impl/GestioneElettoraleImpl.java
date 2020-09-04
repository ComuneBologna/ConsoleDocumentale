package it.eng.portlet.consolepec.spring.bean.elettorale.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import it.eng.consolepec.spagicclient.SpagicClientGestioneElettorale;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.portlet.consolepec.spring.bean.elettorale.GestioneElettorale;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

public class GestioneElettoraleImpl implements GestioneElettorale {

	private static final Logger logger = LoggerFactory.getLogger(GestioneElettoraleImpl.class);

	@Autowired
	SpagicClientGestioneElettorale spagicClientGestioneElettorale;

	@Autowired
	UserSessionUtil userSessionUtil;

	@Override
	public void importa(String praticaPath) throws SpagicClientException {
		logger.info("Gestione elettorale - importazione: invocazione spagic client");
		spagicClientGestioneElettorale.importa(praticaPath, userSessionUtil.getUtenteSpagic());
	}

	@Override
	public void annulla(String praticaPath) throws SpagicClientException {
		logger.info("Gestione elettorale - annullamento: invocazione spagic client");
		spagicClientGestioneElettorale.annulla(praticaPath, userSessionUtil.getUtenteSpagic());
	}

}
