package it.eng.portlet.consolepec.spring.bean.tipologiaprocedimenti.impl;

import it.bologna.comune.spagic.procedimenti.tipologie.Response;
import it.bologna.comune.spagic.procedimenti.tipologie.Tipologia;
import it.eng.consolepec.spagicclient.SpagicClientRecuperaTipologiaProcedimenti;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.portlet.consolepec.gwt.shared.dto.TipologiaProcedimentoDto;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;
import it.eng.portlet.consolepec.spring.bean.tipologiaprocedimenti.RecuperoTipologiaProcedimenti;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;

public class RecuperoTipologiaProcedimentiImpl implements RecuperoTipologiaProcedimenti {

	@Autowired
	SpagicClientRecuperaTipologiaProcedimenti spagicClientRecuperaTipologiaProcedimenti;
	@Autowired
	UserSessionUtil userSessionUtil;

	@Override
	@Cacheable(value = "tipologiaProcedimenti", key = "#root.methodName")
	public List<TipologiaProcedimentoDto> getElencoTipologieProcedimenti() throws SpagicClientException {
		Response tipologieProcedimenti = spagicClientRecuperaTipologiaProcedimenti.getTipologieProcedimenti(new Date(), userSessionUtil.getUtenteSpagic());

		ArrayList<TipologiaProcedimentoDto> listaTipologiaProcedimenti = new ArrayList<TipologiaProcedimentoDto>();

		for (Tipologia tipologia : tipologieProcedimenti.getTipologie()) {

			TipologiaProcedimentoDto tipologiaProcedimentoDto = new TipologiaProcedimentoDto();

			tipologiaProcedimentoDto.setCodiceProcedimento(tipologia.getCodiceProcedimento().intValue());
			tipologiaProcedimentoDto.setCodiceQuartiere(tipologia.getQrt().intValue());
			tipologiaProcedimentoDto.setTermineNormato(tipologia.getTermineNormato().intValue());

			tipologiaProcedimentoDto.setDescrizione(tipologia.getDescrizione());
			tipologiaProcedimentoDto.setDescrizioneSettore(tipologia.getDescrizioneSettore());
			tipologiaProcedimentoDto.setFlagTerritorialita(tipologia.getFlagTerritorialita());
			tipologiaProcedimentoDto.setModalitaAvvio(tipologia.getModalitaAvvio());

			tipologiaProcedimentoDto.setDataInizio(tipologia.getDataInizio().toGregorianCalendar().getTime());

			listaTipologiaProcedimenti.add(tipologiaProcedimentoDto);

		}

		return listaTipologiaProcedimenti;
	}
	
	@Override
	public String getDescrizioneProcedimento(int codiceProcedimento){
		for (TipologiaProcedimentoDto tipologiaProcedimentoDto : getElencoTipologieProcedimenti()) {
			if (codiceProcedimento == tipologiaProcedimentoDto.getCodiceProcedimento())
				return tipologiaProcedimentoDto.getDescrizione();
		}
		throw new IllegalStateException("descrizione procedimento non trovata");
	}

}
