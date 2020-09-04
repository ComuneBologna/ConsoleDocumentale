package it.eng.portlet.consolepec.spring.bean.search.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoValoreMultiplo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.TipoDato;
import it.eng.cobo.consolepec.util.console.ConsoleConstants;
import it.eng.cobo.consolepec.util.date.DateUtils;
import it.eng.cobo.consolepec.util.validation.ValidationUtilities;
import it.eng.consolepec.spagicclient.search.SearchPraticheFilter;
import it.eng.consolepec.spagicclient.search.SearchPraticheSort;
import it.eng.portlet.consolepec.gwt.shared.TextUtils;
import it.eng.portlet.consolepec.gwt.shared.action.CercaPratiche;
import it.eng.portlet.consolepec.gwt.shared.action.cartellafirma.CercaDocumentoFirmaVistoAction;
import it.eng.portlet.consolepec.spring.bean.configurazioni.GestioneConfigurazioni;
import it.eng.portlet.consolepec.spring.bean.gestionepratiche.GestioneTaskPratiche;
import it.eng.portlet.consolepec.spring.bean.profilazione.GestioneProfilazioneUtente;
import it.eng.portlet.consolepec.spring.bean.search.NewMongoDbSearchDocumentRequestGenerator;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;

public class NewMongoDbSearchDocumentRequestGeneratorImpl implements NewMongoDbSearchDocumentRequestGenerator {

	private static HashMap<String, String> regexSpecialCharacter = new HashMap<String, String>();

	static {
		regexSpecialCharacter.put("\\\\", "\\\\\\\\");
		regexSpecialCharacter.put("\\^", "\\\\^");
		regexSpecialCharacter.put("\\$", "\\\\$");
		regexSpecialCharacter.put("\\.", "\\\\.");
		regexSpecialCharacter.put("\\|", "\\\\|");
		regexSpecialCharacter.put("\\?", "\\\\?");
		regexSpecialCharacter.put("\\*", "\\\\*");
		regexSpecialCharacter.put("\\+", "\\\\+");
		regexSpecialCharacter.put("\\(", "\\\\(");
		regexSpecialCharacter.put("\\)", "\\\\)");
		regexSpecialCharacter.put("\\[", "\\\\[");
		regexSpecialCharacter.put("\\{", "\\\\{");
	}

	@Autowired
	PraticaSessionUtil praticaSessionUtil;
	@Autowired
	GestioneTaskPratiche gestioneTaskPratiche;
	@Autowired
	GestioneProfilazioneUtente gestioneProfilazioneUtente;
	@Autowired
	GestioneConfigurazioni gestioneConfigurazioni;

	@SuppressWarnings("unchecked")
	@Override
	public SearchPraticheFilter generaFilter(CercaPratiche action) {
		SearchPraticheFilter filter = new SearchPraticheFilter();

		// Parametri fissi worklist: vengono sovrascritti nel caso sia stato specificato un filtro nella form di ricerca
		if (action.getParametriFissiWorklist() != null) {

			for (Entry<String, Object> entry : action.getParametriFissiWorklist().entrySet()) {
				String campo = entry.getKey();
				Object valore = entry.getValue();

				if (valore instanceof List) {
					filter.addFilter(campo, (List<String>) valore);

				} else if (valore instanceof Map) {
					Map<String, String> map = new LinkedHashMap<String, String>();
					for (Entry<String, Object> e : ((Map<String, Object>) valore).entrySet()) {
						if (e.getValue() != null) {
							map.put(e.getKey(), e.getValue().toString());
						}
					}

					filter.addFilter(campo, map);

				} else if (valore != null) {
					filter.addFilter(campo, valore.toString());
				}

			}

		}

		if (action.getTipologiePratiche() != null && !action.getTipologiePratiche().isEmpty()) {
			filter.addFilter(ConsoleConstants.FiltriRicerca.TIPO_PRATICA.getFiltro(), Lists.transform(action.getTipologiePratiche(), new Function<TipologiaPratica, String>() {

				@Override
				public String apply(TipologiaPratica input) {
					return input.getNomeTipologia();
				}

			}));
		}

		// cc
		if (action.getCc() != null && !action.getCc().trim().isEmpty()) {
			filter.addFilter(ConsoleConstants.FiltriRicerca.DESTINATARIO_CC.getFiltro(), action.getCc().toLowerCase());
		}

		// dataCreazione
		if (action.getDataCreazioneFrom() != null && !action.getDataCreazioneFrom().trim().isEmpty()) {
			filter.addFilter(ConsoleConstants.FiltriRicerca.DATA_CREAZIONE_FROM.getFiltro(),
					fromDate(action.getDataCreazioneFrom(), action.getDataCreazioneFromHour(), action.getDataCreazioneFromMinute()));
		}

		if (action.getDataCreazioneTo() != null && !action.getDataCreazioneTo().trim().isEmpty()) {
			filter.addFilter(ConsoleConstants.FiltriRicerca.DATA_CREAZIONE_TO.getFiltro(), toDate(action.getDataCreazioneTo(), action.getDataCreazioneToHour(), action.getDataCreazioneToMinute()));
		}

		// dataricezionepec
		if (action.getDataRicezioneFrom() != null && !action.getDataRicezioneFrom().trim().isEmpty()) {
			filter.addFilter(ConsoleConstants.FiltriRicerca.DATA_RICEZIONE_PEC_FROM.getFiltro(),
					fromDate(action.getDataRicezioneFrom(), action.getDataRicezioneFromHour(), action.getDataRicezioneFromMinute()));
		}

		if (action.getDataRicezioneTo() != null && !action.getDataRicezioneTo().trim().isEmpty()) {
			filter.addFilter(ConsoleConstants.FiltriRicerca.DATA_RICEZIONE_PEC_TO.getFiltro(), toDate(action.getDataRicezioneTo(), action.getDataRicezioneToHour(), action.getDataRicezioneToMinute()));
		}

		// destinatario
		if (action.getDestinatario() != null && !action.getDestinatario().isEmpty()) {
			String dest = null;
			if (ValidationUtilities.validateEmailAddress(action.getDestinatario())) {
				dest = action.getDestinatario().toLowerCase();
			} else {
				dest = action.getDestinatario();
			}

			filter.addFilter(ConsoleConstants.FiltriRicerca.DESTINATARIO.getFiltro(), dest);
		}

		// idDocumentale
		if (action.getIdDocumentale() != null && !action.getIdDocumentale().isEmpty()) {
			filter.addFilter(ConsoleConstants.FiltriRicerca.ID_DOCUMENTALE.getFiltro(), action.getIdDocumentale().toUpperCase());
		}
		// idEmail
		if (action.getIdEmail() != null && !action.getIdEmail().isEmpty()) {
			filter.addFilter(ConsoleConstants.FiltriRicerca.PEC_MESSAGE_ID.getFiltro(), action.getIdEmail());
		}

		// letto
		if (action.getDaLeggere() != null && action.getDaLeggere()) {
			filter.addFilter(ConsoleConstants.FiltriRicerca.LETTO.getFiltro(), !action.getDaLeggere());
		}

		if (action.getEscludiProprieAssegnazioni() != null && action.getEscludiProprieAssegnazioni()) {
			filter.addFilter(ConsoleConstants.FiltriRicerca.ESCLUDI_ASSEGNAZIONI.getFiltro(), action.getEscludiProprieAssegnazioni());
		}

		if (action.getHasRicevutaAccettazione()) {
			filter.addFilter(ConsoleConstants.FiltriRicerca.ACCETTAZIONE.getFiltro(), action.getHasRicevutaAccettazione());
		}
		// Filtro ricevuta consegna
		if (action.getHasRicevutaConsegna()) {
			filter.addFilter(ConsoleConstants.FiltriRicerca.CONSEGNA.getFiltro(), action.getHasRicevutaConsegna());
		}

		// Stato dei destinatari
		if (action.getStatoDestinatario() != null) {
			filter.addFilter(ConsoleConstants.FiltriRicerca.STATO_DESTINATARIO.getFiltro(), action.getStatoDestinatario().name());
		}

		// Provenienza
		if (action.getProvenienza() != null && !action.getProvenienza().trim().isEmpty()) {
			filter.addFilter(ConsoleConstants.FiltriRicerca.PROVENIENZA.getFiltro(), action.getProvenienza().toLowerCase());
		}

		// stato
		if (action.getStato() != null && action.getStato().length > 0) {
			filter.addFilter(ConsoleConstants.FiltriRicerca.STATO_PRATICA.getFiltro(), toList(action.getStato()));
		}

		// step iter
		if (action.getStepIter() != null && !action.getStepIter().trim().equals("")) {
			filter.addFilter(ConsoleConstants.FiltriRicerca.STEP_ITER.getFiltro(), action.getStepIter());
		}

		// operatore
		if (action.getOperatore() != null && !action.getOperatore().trim().isEmpty()) {
			filter.addFilter(ConsoleConstants.FiltriRicerca.OPERATORE.getFiltro(), regex(action.getOperatore()));
		}

		// tipologia PEC
		if (action.getTipoEmail() != null) {
			filter.addFilter(ConsoleConstants.FiltriRicerca.TIPO_EMAIL.getFiltro(), action.getTipoEmail().name());
		}

		// titolo
		if (action.getTitolo() != null && !action.getTitolo().trim().equals("")) {
			filter.addFilter(ConsoleConstants.FiltriRicerca.TITOLO.getFiltro(), action.getTitolo());
		}
		// utente creazione
		if (action.getUtenteCreazione() != null && !action.getUtenteCreazione().trim().isEmpty()) {
			filter.addFilter(ConsoleConstants.FiltriRicerca.UTENTE_CREAZIONE.getFiltro(), regex(action.getUtenteCreazione()));
		}

		// filtraggio worklist assegnatoA
		if (action.getSoloWorklist() && (action.getAssegnatoA() == null || action.getAssegnatoA().isEmpty())) {
			filter.addFilter(ConsoleConstants.FiltriRicerca.FILTRO_ASSEGNATARIO.getFiltro(), true);
		}
		// filtraggio assegnatario
		if (action.getAssegnatoA() != null && !action.getAssegnatoA().isEmpty()) {
			filter.addFilter(ConsoleConstants.FiltriRicerca.ASSEGNATARIO_SINGOLO.getFiltro(), action.getAssegnatoA());
		}

		// filtraggio abilitazione
		if (action.isFiltroAbilitazioni()) {
			filter.addFilter(ConsoleConstants.FiltriRicerca.FILTRO_MATRICE_VISIBILITA.getFiltro(), true);
		}

		if (action.getSuperutente()) {
			filter.addFilter(ConsoleConstants.FiltriRicerca.FILTRO_SUPERVISORE.getFiltro(), true);
		}

		addProtocollazioneFilter(filter, action);

		// dati aggiuntivi
		if (action.getDatiAggiuntivi() != null && !action.getDatiAggiuntivi().isEmpty()) {

			Map<String, Object> datiAggiuntivi = new LinkedHashMap<String, Object>();
			Map<String, Object> datiAggiuntiviOr = new LinkedHashMap<String, Object>();

			for (Entry<DatoAggiuntivo, Object> entry : action.getDatiAggiuntivi().entrySet()) {

				if (entry.getKey().getTipo().equals(TipoDato.MultiploRicerca)) {

					DatoAggiuntivoValoreMultiplo dag = (DatoAggiuntivoValoreMultiplo) entry.getKey();

					for (String idDato : dag.getValoriPredefiniti()) {

						if (entry.getValue() instanceof String) { // campo singolo
							datiAggiuntiviOr.put(idDato, regex((String) entry.getValue()));

						} else if (entry.getValue() instanceof List) { // campo multiplo
							List<String> escaped = new ArrayList<String>();
							for (String dato : (List<String>) entry.getValue()) {
								escaped.add(regex(dato));
							}

							datiAggiuntiviOr.put(idDato, escaped);
						}
					}

				} else if (entry.getValue() instanceof String) { // campo singolo
					datiAggiuntivi.put(entry.getKey().getNome(), regex((String) entry.getValue()));

				} else if (entry.getValue() instanceof List) { // campo multiplo
					List<String> escaped = new ArrayList<String>();
					for (String dato : (List<String>) entry.getValue()) {
						escaped.add(regex(dato));
					}

					datiAggiuntivi.put(entry.getKey().getNome(), escaped);
				}

			}

			if (!datiAggiuntivi.isEmpty()) {
				filter.addFilter(ConsoleConstants.FiltriRicerca.DATI_AGGIUNTIVI_MAP.getFiltro(), datiAggiuntivi);
			}

			if (!datiAggiuntiviOr.isEmpty()) {
				filter.addFilter(ConsoleConstants.FiltriRicerca.DATI_AGGIUNTIVI_OR_MAP.getFiltro(), datiAggiuntiviOr);
			}
		}

		if (action.getNomeModulo() != null && !action.getNomeModulo().trim().isEmpty()) {
			filter.addFilter(ConsoleConstants.FiltriRicerca.NOME_MODULO.getFiltro(), action.getNomeModulo());
		}

		if (action.getValoriModulo() != null && !action.getValoriModulo().isEmpty()) {
			Map<String, String> map = new LinkedHashMap<String, String>();

			for (String coppiaNomeValore : action.getValoriModulo()) {
				if (coppiaNomeValore != null && !coppiaNomeValore.equals("%")) {
					String[] valoreModulo = coppiaNomeValore.split("=");
					String nome = regex(valoreModulo[0]);
					String valore = regex(valoreModulo[1]);
					map.put(nome, valore);
				}
			}
			if (!map.isEmpty()) {
				filter.addFilter(ConsoleConstants.FiltriRicerca.VALORI_MODULO.getFiltro(), map);
			}
		}

		if (!Strings.isNullOrEmpty(action.getRicercaPerCollegamento())) {
			// filtraggio condivisione
			filter.addFilter(ConsoleConstants.FiltriRicerca.GRUPPI_CONDIVISIONI.getFiltro(), gestioneProfilazioneUtente.getDatiUtente().getRuoli());
			filter.addFilter(ConsoleConstants.FiltriRicerca.CONDIVISIONI_ID_DOCUMENTALE.getFiltro(), TextUtils.getIdDocumentaleFromClientID(action.getRicercaPerCollegamento()));

		} else {
			// filtraggio visibilità
			if ((!action.getSoloWorklist() && !action.getIgnoraGruppi()) || action.isFiltroGruppiVisibilita()) {
				filter.addFilter(ConsoleConstants.FiltriRicerca.FILTRO_VISIBILITA.getFiltro(), true);
			}
		}

		if (!Strings.isNullOrEmpty(action.getNomeTemplate())) {
			filter.addFilter(ConsoleConstants.FiltriRicerca.NOME_TEMPLATE.getFiltro(), action.getNomeTemplate());
		}
		if (!Strings.isNullOrEmpty(action.getTipoFascicoloAbilitatoSceltaTemplate())) {
			filter.addFilter(ConsoleConstants.FiltriRicerca.TEMPLATE_FASCICOLI_ABILITATI.getFiltro(), action.getTipoFascicoloAbilitatoSceltaTemplate());
		}

		if (action.getDestinatarioAssegnaUtenteEsterno() != null && !action.getDestinatarioAssegnaUtenteEsterno().trim().isEmpty()) {
			filter.addFilter(ConsoleConstants.FiltriRicerca.DESTINATARI_ASSEGNAZIONE_ESTERNA.getFiltro(), regex(action.getDestinatarioAssegnaUtenteEsterno()));
		}

		if (action.getCodiceComunicazione() != null && !action.getCodiceComunicazione().trim().isEmpty()) {
			filter.addFilter(ConsoleConstants.FiltriRicerca.CODICE_COMUNICAZIONE.getFiltro(), regex(action.getCodiceComunicazione()));
		}

		if (action.getIdTemplateComunicazione() != null && !action.getIdTemplateComunicazione().trim().isEmpty()) {
			filter.addFilter(ConsoleConstants.FiltriRicerca.ID_TEMPLATE_COMUNICAZIONE.getFiltro(), action.getIdTemplateComunicazione());
		}

		if (action.getIdDocumentaleDaEscludere() != null && !action.getIdDocumentaleDaEscludere().trim().isEmpty()) {
			filter.addFilter(ConsoleConstants.FiltriRicerca.CONDIVISIONI_ID_DOCUMENTALE.getFiltro(), TextUtils.getIdDocumentaleFromClientID(action.getIdDocumentaleDaEscludere()));
		}

		return filter;
	}

	@Override
	public SearchPraticheSort generaSort(CercaPratiche action) {
		if (action.getCampoOrdinamento() == null) {
			return null;
		}

		SearchPraticheSort searchPraticheSort = new SearchPraticheSort();
		switch (action.getCampoOrdinamento()) {

		case DESTINATARIO:
			searchPraticheSort.add("pec.destinatari.destinatarioPrincipale.email", action.isOrdinamentoAsc() ? SearchPraticheSort.ASC : SearchPraticheSort.DESC);
			searchPraticheSort.add("pec.destinatari.destinatario", action.isOrdinamentoAsc() ? SearchPraticheSort.ASC : SearchPraticheSort.DESC);
			break;

		case DATA:
			searchPraticheSort.add("dataCreazione", action.isOrdinamentoAsc() ? SearchPraticheSort.ASC : SearchPraticheSort.DESC);

			break;

		case DATA_RIC:
			searchPraticheSort.add("pec.dataRicezione", action.isOrdinamentoAsc() ? SearchPraticheSort.ASC : SearchPraticheSort.DESC);
			break;

		case TITOLO:
			searchPraticheSort.add("titolo", action.isOrdinamentoAsc() ? SearchPraticheSort.ASC : SearchPraticheSort.DESC);
			searchPraticheSort.add("pec.oggetto", action.isOrdinamentoAsc() ? SearchPraticheSort.ASC : SearchPraticheSort.DESC);
			searchPraticheSort.add("template.nome", action.isOrdinamentoAsc() ? SearchPraticheSort.ASC : SearchPraticheSort.DESC);
			break;

		case STATO:
			searchPraticheSort.add("stato", action.isOrdinamentoAsc() ? SearchPraticheSort.ASC : SearchPraticheSort.DESC);
			break;

		case TIPO_PRATICA:
			searchPraticheSort.add("tipo", action.isOrdinamentoAsc() ? SearchPraticheSort.ASC : SearchPraticheSort.DESC);
			break;

		case PG:
			searchPraticheSort.add("fascicolo.protocollazione.$.annoPg", action.isOrdinamentoAsc() ? SearchPraticheSort.ASC : SearchPraticheSort.DESC);
			searchPraticheSort.add("fascicolo.protocollazione.$.numeroPg", action.isOrdinamentoAsc() ? SearchPraticheSort.ASC : SearchPraticheSort.DESC);
			break;

		default:
			searchPraticheSort.add(action.getCampoOrdinamento().getId(), action.isOrdinamentoAsc() ? SearchPraticheSort.ASC : SearchPraticheSort.DESC);
			break;
		}

		return searchPraticheSort;
	}

	private static void addProtocollazioneFilter(SearchPraticheFilter filter, CercaPratiche action) {
		Map<String, String> protocollazione = new HashMap<String, String>();
		if (!Strings.isNullOrEmpty(action.getAnno())) {
			protocollazione.put("annoPG", action.getAnno());
		}

		if (!Strings.isNullOrEmpty(action.getDataProtocollazione())) {
			protocollazione.put("dataprotocollazioneFrom", fromDate(action.getDataProtocollazione(), null, null));
			protocollazione.put("dataprotocollazioneTo", toDate(action.getDataProtocollazione(), null, null));
		}

		if (!Strings.isNullOrEmpty(action.getNumero())) {
			protocollazione.put("numeroPG", action.getNumero());
		}

		if (!Strings.isNullOrEmpty(action.getOggettoProtocollazione())) {
			protocollazione.put("oggetto", regex(action.getOggettoProtocollazione()));
		}

		if (!Strings.isNullOrEmpty(action.getTipologiadelDocumentoProtocollazione())) {
			protocollazione.put("tipologiadocumento", action.getTipologiadelDocumentoProtocollazione());
		}

		if (!Strings.isNullOrEmpty(action.getProvenienzaProtocollazione())) {
			protocollazione.put("provenienza", regex(action.getProvenienzaProtocollazione()));
		}

		if (!Strings.isNullOrEmpty(action.getRubricaProtocollazione())) {
			protocollazione.put("rubrica", action.getRubricaProtocollazione());
		}

		if (!Strings.isNullOrEmpty(action.getSezioneProtocollazione())) {
			protocollazione.put("sezione", action.getSezioneProtocollazione());
		}

		if (!Strings.isNullOrEmpty(action.getTitoloDocumentoProtocollazione())) {
			protocollazione.put("titolo", action.getTitoloDocumentoProtocollazione());
		}

		if (!Strings.isNullOrEmpty(action.getAnnoRegistro())) {
			protocollazione.put("annoregistro", action.getAnnoRegistro());
		}

		if (!Strings.isNullOrEmpty(action.getNumeroFascicolo())) {
			protocollazione.put("numerofascicolo", action.getNumeroFascicolo());
		}

		if (!Strings.isNullOrEmpty(action.getNumeroRegistro())) {
			protocollazione.put("numeroregistro", action.getNumeroRegistro());
		}

		if (!Strings.isNullOrEmpty((action.getTipoProtocollazione() != null ? action.getTipoProtocollazione().getCodice() : null))) {
			protocollazione.put("tipoprotocollazione", (action.getTipoProtocollazione() != null ? action.getTipoProtocollazione().getCodice() : null));
		}

		if (!protocollazione.isEmpty()) {
			filter.addFilter(ConsoleConstants.FiltriRicerca.PROTOCOLLAZIONE.getFiltro(), protocollazione);
		}

	}

	private static String fromDate(String date, Integer hour, Integer minute) {
		DateFormat ISO8601Format = (DateFormat) DateUtils.DATEFORMAT_ISO8601.clone();
		ISO8601Format.setTimeZone(TimeZone.getDefault());

		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(DateUtils.DATEFORMAT_DATE.parse(date));

			if (hour != null) {
				calendar.add(Calendar.HOUR, hour);
			}
			if (minute != null) {
				calendar.add(Calendar.MINUTE, minute);

			}

			calendar.add(Calendar.SECOND, 0);
			calendar.add(Calendar.MILLISECOND, 000);

			return ISO8601Format.format(calendar.getTime());

		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	private static String toDate(String date, Integer hour, Integer minute) {
		DateFormat ISO8601Format = (DateFormat) DateUtils.DATEFORMAT_ISO8601.clone();
		ISO8601Format.setTimeZone(TimeZone.getDefault());

		try {
			Date data = DateUtils.DATEFORMAT_DATE.parse(date);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(data);

			if (hour != null || minute != null) {
				calendar.add(Calendar.HOUR, hour);
				calendar.add(Calendar.MINUTE, minute);
				calendar.add(Calendar.SECOND, 59);

			} else {
				// 23:59
				calendar.add(Calendar.DATE, 1);
				calendar.add(Calendar.SECOND, -1);
			}

			calendar.add(Calendar.MILLISECOND, 999);
			return ISO8601Format.format(calendar.getTime());

		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	private static List<String> toList(Object[] objects) {
		List<String> strings = new ArrayList<String>();
		for (Object obj : objects) {
			if (obj != null) {
				strings.add(obj.toString());
			}
		}
		return strings;
	}

	private static String regex(String value) {
		for (Entry<String, String> specialChar : regexSpecialCharacter.entrySet()) {
			value = value.replaceAll(specialChar.getKey(), specialChar.getValue());
		}
		return value;
	}

	@Override
	public SearchPraticheFilter generaFilter(CercaDocumentoFirmaVistoAction action) {

		SearchPraticheFilter filter = new SearchPraticheFilter();

		filter.addFilter(ConsoleConstants.FiltriRicerca.RICERCA_DA_DESTINATARIO.getFiltro(), action.isRicercaDaDestinatario());

		if (!action.getProponenti().isEmpty()) {
			filter.addFilter(ConsoleConstants.FiltriRicerca.PROPONENTI.getFiltro(), Lists.transform(action.getProponenti(), new Function<AnagraficaRuolo, String>() {

				@Override
				public String apply(AnagraficaRuolo input) {
					return input.getRuolo();
				}

			}));
		}

		if (action.getTipoStato() != null) {
			filter.addFilter(ConsoleConstants.FiltriRicerca.TIPO_STATO.getFiltro(), action.getTipoStato().toString());
		}

		if (action.getTipoProposta() != null) {
			filter.addFilter("tipoProposta", action.getTipoProposta().toString());
		}

		if (action.getStato() != null) {
			filter.addFilter("stato", action.getStato().name());
		}

		if (action.getStatoDestinatario() != null) {
			filter.addFilter("statoDestinatario", action.getStatoDestinatario().toString());
		}

		if (!Strings.isNullOrEmpty(action.getOggetto())) {
			filter.addFilter("oggettoRichiesta", action.getOggetto());
		}

		if (!Strings.isNullOrEmpty(action.getDataDa())) {
			filter.addFilter("dataRichiestaFrom", fromDate(action.getDataDa(), null, null));
		}

		if (!Strings.isNullOrEmpty(action.getDataA())) {
			filter.addFilter("dataRichiestaTo", toDate(action.getDataA(), null, null));
		}

		if (!Strings.isNullOrEmpty(action.getMittenteOriginale())) {
			filter.addFilter("mittenteOriginale", action.getMittenteOriginale());
		}

		if (!Strings.isNullOrEmpty(action.getDataScadenzaDa())) {
			filter.addFilter("dataScadenzaFrom", fromDate(action.getDataScadenzaDa(), null, null));
		}

		if (!Strings.isNullOrEmpty(action.getDataScadenzaA())) {
			filter.addFilter("dataScadenzaTo", toDate(action.getDataScadenzaA(), null, null));
		}

		if (!Strings.isNullOrEmpty(action.getIdDocumentaleFascicolo())) {
			filter.addFilter("idDocumentale", action.getIdDocumentaleFascicolo().toUpperCase());
		}

		if (!Strings.isNullOrEmpty(action.getTitoloFascicolo())) {
			filter.addFilter("titolo", action.getTitoloFascicolo());
		}

		if (action.getTipologiaPratica() != null) {
			filter.addFilter(ConsoleConstants.FiltriRicerca.TIPO_PRATICA.getFiltro(), action.getTipologiaPratica().getNomeTipologia());
		}

		return filter;
	}

	@Override
	public SearchPraticheSort generaSort(CercaDocumentoFirmaVistoAction action) {

		if (action.getCampoOrdinamento() == null) {
			return null;
		}

		SearchPraticheSort searchPraticheSort = new SearchPraticheSort();
		switch (action.getCampoOrdinamento()) {

		case CARTELLA_FIRMA_OGGETTO:
			searchPraticheSort.add("fascicolo.allegati.documento.oggettoDocumento", action.isOrdinamentoAsc() ? SearchPraticheSort.ASC : SearchPraticheSort.DESC);
			break;

		case CARTELLA_FIRMA_PROPONENTE:
			searchPraticheSort.add("tasks.task.assegnatari.assegnatarioCorrente.nome", action.isOrdinamentoAsc() ? SearchPraticheSort.ASC : SearchPraticheSort.DESC);
			break;

		case CARTELLA_FIRMA_TIPO_RICHIESTA:
			searchPraticheSort.add("tasks.task.richiestaFirma.tipo", action.isOrdinamentoAsc() ? SearchPraticheSort.ASC : SearchPraticheSort.DESC);
			break;

		case CARTELLA_FIRMA_DATA_CREAZIONE:
			searchPraticheSort.add("tasks.task.richiestaFirma.dataCreazione", action.isOrdinamentoAsc() ? SearchPraticheSort.ASC : SearchPraticheSort.DESC);
			break;

		case CARTELLA_FIRMA_DATA_SCADENZA:
			searchPraticheSort.add("tasks.task.richiestaFirma.dataScadenza", action.isOrdinamentoAsc() ? SearchPraticheSort.ASC : SearchPraticheSort.DESC);
			break;

		case CARTELLA_FIRMA_TITOLO_FASCICOLO:
			searchPraticheSort.add("titolo", action.isOrdinamentoAsc() ? SearchPraticheSort.ASC : SearchPraticheSort.DESC);
			break;

		default:
			searchPraticheSort.add("tasks.task.richiestaFirma.dataCreazione", action.isOrdinamentoAsc() ? SearchPraticheSort.ASC : SearchPraticheSort.DESC);
			break;
		}

		return searchPraticheSort;
	}
}
