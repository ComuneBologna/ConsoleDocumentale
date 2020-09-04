package it.eng.portlet.consolepec.spring.bean.search.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import it.eng.cobo.consolepec.commons.urbanistica.FiltriRicercaUrbanistica;
import it.eng.portlet.consolepec.gwt.shared.action.CercaPratiche;
import it.eng.portlet.consolepec.spring.bean.search.PraticaProcediSearchUtil;

public class PraticaProcediSearchUtilImpl implements PraticaProcediSearchUtil {

	@Override
	public Map<String, Object> createQueryFilters(CercaPratiche cercaPraticheAction) {
		Map<String, Object> filtri = new HashMap<String, Object>();

		if (cercaPraticheAction == null) {
			return filtri;
		}

		if (StringUtils.isNotBlank(cercaPraticheAction.getAnno())) {
			filtri.put(FiltriRicercaUrbanistica.ANNO_PROTOCOLLO, cercaPraticheAction.getAnno());
		}

		if (StringUtils.isNotBlank(cercaPraticheAction.getNumero())) {
			filtri.put(FiltriRicercaUrbanistica.NUMERO_PROTOCOLLO, cercaPraticheAction.getNumero());
		}

		if (StringUtils.isNotBlank(cercaPraticheAction.getTitolo())) {
			filtri.put(FiltriRicercaUrbanistica.OGGETTO, cercaPraticheAction.getTitolo());
		}

		if (StringUtils.isNotBlank(cercaPraticheAction.getIndirizzoVia())) {
			filtri.put(FiltriRicercaUrbanistica.INDIRIZZO_VIA, cercaPraticheAction.getIndirizzoVia());
		}

		if (StringUtils.isNotBlank(cercaPraticheAction.getIndirizzoCivico())) {
			filtri.put(FiltriRicercaUrbanistica.INDIRIZZO_CIVICO, cercaPraticheAction.getIndirizzoCivico());
		}

		if (StringUtils.isNotBlank(cercaPraticheAction.getAmbito())) {
			filtri.put(FiltriRicercaUrbanistica.AMBITO, cercaPraticheAction.getAmbito());
		}

		if (StringUtils.isNotBlank(cercaPraticheAction.getTipoPraticaProcedi())) {
			filtri.put(FiltriRicercaUrbanistica.TIPO_PRATICA, cercaPraticheAction.getTipoPraticaProcedi());
		}

		if (StringUtils.isNotBlank(cercaPraticheAction.getDataCreazioneFrom())) {
			filtri.put(FiltriRicercaUrbanistica.DATA_CREAZIONE_DA, cercaPraticheAction.getDataCreazioneFrom());
		}

		if (StringUtils.isNotBlank(cercaPraticheAction.getDataCreazioneTo())) {
			filtri.put(FiltriRicercaUrbanistica.DATA_CREAZIONE_A, cercaPraticheAction.getDataCreazioneTo());
		}

		if (StringUtils.isNotBlank(cercaPraticheAction.getCognomeNome())) {
			filtri.put(FiltriRicercaUrbanistica.COGNOME_NOME, cercaPraticheAction.getCognomeNome());
		}

		return filtri;

	}

	@Override
	public Map<String, Boolean> createSortFilters(CercaPratiche cercaPraticheAction) {
		Map<String, Boolean> filtri = new HashMap<String, Boolean>();

		if (cercaPraticheAction == null) {
			return filtri;
		}

		switch (cercaPraticheAction.getCampoOrdinamento()) {
		case DESTINATARIO:
			break;

		case DATA:
			filtri.put(FiltriRicercaUrbanistica.DATA_CREAZIONE, cercaPraticheAction.isOrdinamentoAsc() ? true : false);
			break;

		case DATA_RIC:
			break;

		case TITOLO:
			filtri.put(FiltriRicercaUrbanistica.OGGETTO, cercaPraticheAction.isOrdinamentoAsc() ? true : false);
			break;

		case STATO:
			break;

		case TIPO_PRATICA:
			break;

		case PG:
			filtri.put(FiltriRicercaUrbanistica.ANNO_PROTOCOLLO, cercaPraticheAction.isOrdinamentoAsc() ? true : false);
			filtri.put(FiltriRicercaUrbanistica.NUMERO_PROTOCOLLO, cercaPraticheAction.isOrdinamentoAsc() ? true : false);
			break;

		default:
			filtri.put(FiltriRicercaUrbanistica.DATA_CREAZIONE, cercaPraticheAction.isOrdinamentoAsc() ? true : false);
			break;
		}

		return filtri;
	}

}
