package it.eng.portlet.consolepec.spring.bean.procedimenti.impl;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import it.eng.cobo.consolepec.commons.procedimento.ChiusuraProcedimentoInput;
import it.eng.cobo.consolepec.commons.procedimento.ChiusuraProcedimentoOutput;
import it.eng.cobo.consolepec.commons.procedimento.PropostaChiusuraProcedimentoInput;
import it.eng.cobo.consolepec.commons.procedimento.PropostaChiusuraProcedimentoOutput;
import it.eng.consolepec.client.GestioneProcedimentiClient;
import it.eng.consolepec.spagicclient.SpagicClientGestioneProcedimenti;
import it.eng.consolepec.spagicclient.bean.request.procedimenti.GestioneProcedimentoRequest;
import it.eng.consolepec.spagicclient.bean.request.procedimenti.IterProcedimentoRequest;
import it.eng.consolepec.spagicclient.bean.response.procedimenti.GestioneProcedimentoResponse;
import it.eng.consolepec.spagicclient.bean.response.procedimenti.IterProcedimentoResponse;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.portlet.consolepec.spring.bean.procedimenti.GestioneProcedimenti;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

public class GestioneProcedimentiImpl implements GestioneProcedimenti {

	@Autowired
	SpagicClientGestioneProcedimenti spagicClientGestioneProcedimenti;

	@Autowired
	GestioneProcedimentiClient gestioneProcedimentiClient;

	@Autowired
	UserSessionUtil userSessionUtil;

	private Logger logger = LoggerFactory.getLogger(GestioneProcedimentiImpl.class);

	@Override
	public GestioneProcedimentoResponse avviaProcedimento(GestioneProcedimentoRequest bean, String praticaPath, java.util.List<String> indirizziMail) throws SpagicClientException {
		logger.info("Avvio procedimento: invocazione spagic client");
		GestioneProcedimentoResponse esitoAvvioProcedimento = spagicClientGestioneProcedimenti.avvioProcedimento(bean, praticaPath, indirizziMail, userSessionUtil.getUtenteSpagic());
		if (esitoAvvioProcedimento != null)
			logger.info("Avvio procedimento: esito {}, {}", esitoAvvioProcedimento.getCodice(), esitoAvvioProcedimento.getDescrizione());
		else
			logger.error("Errore avvio procedimento con codice {} sul capofila {}/{}", bean.getCodTipologiaProcedimento(), bean.getNumProtocollazione(), bean.getAnnoProtocollazione());
		return esitoAvvioProcedimento;
	}

	@Override
	public GestioneProcedimentoResponse avviaProcedimento(GestioneProcedimentoRequest bean, String praticaPath) throws SpagicClientException {
		return avviaProcedimento(bean, praticaPath, new ArrayList<String>());
	}

	@Override
	public ChiusuraProcedimentoOutput chiudiProcedimento(ChiusuraProcedimentoInput request, String idDocumentaleFascicolo) {
		return gestioneProcedimentiClient.chiudiProcedimento(request, idDocumentaleFascicolo, userSessionUtil.getUtenteSpagic());
	}

	@Deprecated
	@Override
	public IterProcedimentoResponse getIterProcedimento(IterProcedimentoRequest bean) throws SpagicClientException {
		logger.info("Richiesta iter procedimento: invocazione spagic client");
		IterProcedimentoResponse iterProcedimento = spagicClientGestioneProcedimenti.getIterProcedimento(bean, userSessionUtil.getUtenteSpagic());
		if (iterProcedimento != null)
			logger.info("Trovati {} procedimenti sul capofila {}/{}", iterProcedimento.getNumTotProcedimenti(), iterProcedimento.getNumProtocollazione(), iterProcedimento.getAnnoProtocollazione());
		else
			logger.error("Errore recupero iter procedimento sul capofila {}/{}", bean.getNumProtocollazione(), bean.getAnnoProtocollazione());
		return iterProcedimento;
	}

	@Override
	public PropostaChiusuraProcedimentoOutput propostaChiusuraProcedimento(PropostaChiusuraProcedimentoInput input) {
		return gestioneProcedimentiClient.propostaChiusuraProcedimento(input, userSessionUtil.getUtenteSpagic());
	}
	
}
