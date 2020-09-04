package it.eng.portlet.consolepec.spring.bean.stampe.impl;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import it.bologna.comune.alfresco.generic.service.Response;
import it.eng.consolepec.spagicclient.SpagicClientDownloadStampe;
import it.eng.consolepec.spagicclient.remoteproxy.ResponseWithAttachementsDto;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;
import it.eng.portlet.consolepec.spring.bean.stampe.GestioneStampe;

public class GestioneStampeImpl implements GestioneStampe {

	private final Logger logger = LoggerFactory.getLogger(GestioneStampeImpl.class);

	@Autowired
	SpagicClientDownloadStampe spagicClientDownloadStampe;
	@Autowired
	UserSessionUtil userSessionUtil;

	@Override
	public StampaBean stampaRicevutaDiConsegna(String praticaPath) throws Exception {
		logger.info("Stampa ricevuta di consegna: invocazione spagic client");
		ResponseWithAttachementsDto<Response> dto = spagicClientDownloadStampe.downloadRicevuteDiConsegna(praticaPath, userSessionUtil.getUtenteSpagic());
		return parseResponse(dto);
	}

	@Override
	public StampaBean stampaRiversamentoCartaceo(String praticaPath, String annoPG, String numPG) throws Exception {
		logger.info("Stampa riversamnto cartaceo: invocazione spagic client");
		ResponseWithAttachementsDto<Response> dto = spagicClientDownloadStampe.downloadRiversamentoCartaceo(praticaPath, numPG, annoPG, userSessionUtil.getUtenteSpagic());
		return parseResponse(dto);
	}

	private static StampaBean parseResponse(ResponseWithAttachementsDto<Response> dto) throws Exception {
		if (dto.getAttachements().size() != 1 || dto.getAttachements().get(dto.getResponse().getResponseparam()) == null) {
			throw new Exception("Stampa non scaricata correttamente");
		}
		String nomefile = dto.getAttachements().keySet().iterator().next();
		InputStream stream = dto.getAttachements().get(nomefile);
		StampaBean bean = new StampaBean(nomefile, stream);
		return bean;
	}
}
