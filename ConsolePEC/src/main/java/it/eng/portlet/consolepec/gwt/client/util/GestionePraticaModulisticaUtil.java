package it.eng.portlet.consolepec.gwt.client.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.i18n.shared.DateTimeFormat;

import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoValoreSingolo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.TipoDato;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CreaFascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;

public class GestionePraticaModulisticaUtil {

	public static final String FASCICOLO_DA_PRATICA_MODULISTICA_SCARICATO = "fascicolodapraticamodulisticascaricato";
	private static final String FASCICOLO_DA_PRATICA_MODULISTICA_SCARICATO_DES = "Flag per gestione scarico modulistica";

	public static final String FASCICOLO_DA_PRATICA_DATA_INIZIO_BANDO = "fascicolodapraticamodulisticadatainiziobando";
	public static final String FASCICOLO_DA_PRATICA_DATA_FINE_BANDO = "fascicolodapraticamodulisticadatafinebando";

	private static final String FASCICOLO_DA_PRATICA_DATA_INIZIO_BANDO_DES = "Data inzio bando";
	private static final String FASCICOLO_DA_PRATICA_DATA_FINE_BANDO_DES = "Data fine bando";

	public static final String FASCICOLO_DA_PRATICA_EMAIL_PROVENIENZA_MODULO = "fascicolodapraticamodulisticaemailprovenienzamodulo";
	private static final String FASCICOLO_DA_PRATICA_EMAIL_PROVENIENZA_MODULO_DES = "Email provenienza modulo";

	public static final String MODULO_DENOMINAZIONE_SOCIETA = "DENOMINAZIONE_SOC";
	public static final String MODULO_DATA_INIZIO_BANDO = "CONSOLE_DATA_INIZIO_BANDO";
	public static final String MODULO_DATA_FINE_BANDO = "CONSOLE_DATA_FINE_BANDO";
	public static final String MODULO_EMAIL_PROVENIENZA_MODULO = "CONSOLE_EMAILRICHIED";
	public static final String MODULO_CODICE_FISCALE = "CONSOLE_CODFISCALERICHIED";
	public static final String MODULO_NOME_RICHIEDENTE = "CONSOLE_NOMERICHIED";
	public static final String MODULO_COGNOME_RICHIEDENTE = "CONSOLE_COGNOMERICHIED";

	public static final String MODULO_ANNO_SPORT_DA = "ANNO_SPORT_DA";
	public static final String MODULO_ANNO_SPORT_A = "ANNO_SPORT_A";

	public static void aggiungiValoreAggiuntivo(FascicoloDTO fascicolo, String moduloPath) {
		// fascicolo.getValoriDatiAggiuntivi().add(new ValoreDatoAggiuntivoDTO__(moduloPath + "_" + FASCICOLO_DA_PRATICA_MODULISTICA_SCARICATO, "false", FASCICOLO_DA_PRATICA_MODULISTICA_SCARICATO_DES,
		// TipoDatoDTO.Testo, false, false, false, 100));
		fascicolo.getValoriDatiAggiuntivi().add(new DatoAggiuntivoValoreSingolo(moduloPath + "_" + FASCICOLO_DA_PRATICA_MODULISTICA_SCARICATO, FASCICOLO_DA_PRATICA_MODULISTICA_SCARICATO_DES,
				TipoDato.Testo, 100, false, false, false, new ArrayList<String>(), "false"));
	}

	public static void aggiungiValoreAggiuntivo(CreaFascicoloDTO dto, String moduloPath) {
		// dto.getDatiAggiuntivi().add(new ValoreDatoAggiuntivoDTO__(moduloPath + "_" + FASCICOLO_DA_PRATICA_MODULISTICA_SCARICATO , "false", FASCICOLO_DA_PRATICA_MODULISTICA_SCARICATO_DES,
		// TipoDatoDTO.Testo, false, false, false, 100));
		dto.getDatiAggiuntivi().add(new DatoAggiuntivoValoreSingolo(moduloPath + "_" + FASCICOLO_DA_PRATICA_MODULISTICA_SCARICATO, FASCICOLO_DA_PRATICA_MODULISTICA_SCARICATO_DES, TipoDato.Testo, 100,
				false, false, false, new ArrayList<String>(), "false"));

	}

	public static void aggiungiDataInizioBando(CreaFascicoloDTO dto, String value, String moduloPath) {
		// dto.getDatiAggiuntivi().add(new ValoreDatoAggiuntivoDTO__(moduloPath + "_" + FASCICOLO_DA_PRATICA_DATA_INIZIO_BANDO, value, FASCICOLO_DA_PRATICA_DATA_INIZIO_BANDO_DES, TipoDatoDTO.Data,
		// false, false, false, 101));
		dto.getDatiAggiuntivi().add(new DatoAggiuntivoValoreSingolo(moduloPath + "_" + FASCICOLO_DA_PRATICA_DATA_INIZIO_BANDO, FASCICOLO_DA_PRATICA_DATA_INIZIO_BANDO_DES, TipoDato.Data, 101, false,
				false, false, new ArrayList<String>(), convertDateDatiAggiuntivi(value)));
	}

	public static void aggiungiDataFineBando(CreaFascicoloDTO dto, String value, String moduloPath) {
		// dto.getDatiAggiuntivi().add(new ValoreDatoAggiuntivoDTO__(moduloPath + "_" + FASCICOLO_DA_PRATICA_DATA_FINE_BANDO, value, FASCICOLO_DA_PRATICA_DATA_FINE_BANDO_DES, TipoDatoDTO.Data, false,
		// false, false, 102));
		dto.getDatiAggiuntivi().add(new DatoAggiuntivoValoreSingolo(moduloPath + "_" + FASCICOLO_DA_PRATICA_DATA_FINE_BANDO, FASCICOLO_DA_PRATICA_DATA_FINE_BANDO_DES, TipoDato.Data, 102, false, false,
				false, new ArrayList<String>(), convertDateDatiAggiuntivi(value)));
	}

	public static void aggiungiEmailProvenienzaModulo(CreaFascicoloDTO dto, String value, String moduloPath) {
		// dto.getDatiAggiuntivi().add(new ValoreDatoAggiuntivoDTO__(moduloPath + "_" + FASCICOLO_DA_PRATICA_EMAIL_PROVENIENZA_MODULO, value, FASCICOLO_DA_PRATICA_EMAIL_PROVENIENZA_MODULO_DES,
		// TipoDatoDTO.Testo, false, false, false, 103));
		dto.getDatiAggiuntivi().add(new DatoAggiuntivoValoreSingolo(moduloPath + "_" + FASCICOLO_DA_PRATICA_EMAIL_PROVENIENZA_MODULO, FASCICOLO_DA_PRATICA_EMAIL_PROVENIENZA_MODULO_DES, TipoDato.Testo,
				103, false, false, false, new ArrayList<String>(), value));
	}

	public static void aggiungiDataInizioBando(FascicoloDTO fascicolo, String value, String moduloPath) {
		// fascicolo.getValoriDatiAggiuntivi().add(new ValoreDatoAggiuntivoDTO__(moduloPath + "_" + FASCICOLO_DA_PRATICA_DATA_INIZIO_BANDO, value, FASCICOLO_DA_PRATICA_DATA_INIZIO_BANDO_DES,
		// TipoDatoDTO.Data, false, false, false, 101));
		fascicolo.getValoriDatiAggiuntivi().add(new DatoAggiuntivoValoreSingolo(moduloPath + "_" + FASCICOLO_DA_PRATICA_DATA_INIZIO_BANDO, FASCICOLO_DA_PRATICA_DATA_INIZIO_BANDO_DES, TipoDato.Data,
				101, false, false, false, new ArrayList<String>(), convertDateDatiAggiuntivi(value)));
	}

	public static void aggiungiDataFineBando(FascicoloDTO fascicolo, String value, String moduloPath) {
		// fascicolo.getValoriDatiAggiuntivi().add(new ValoreDatoAggiuntivoDTO__(moduloPath + "_" + FASCICOLO_DA_PRATICA_DATA_FINE_BANDO, value, FASCICOLO_DA_PRATICA_DATA_FINE_BANDO_DES,
		// TipoDatoDTO.Data, false, false, false, 102));
		fascicolo.getValoriDatiAggiuntivi().add(new DatoAggiuntivoValoreSingolo(moduloPath + "_" + FASCICOLO_DA_PRATICA_DATA_FINE_BANDO, FASCICOLO_DA_PRATICA_DATA_FINE_BANDO, TipoDato.Data, 102,
				false, false, false, new ArrayList<String>(), convertDateDatiAggiuntivi(value)));
	}

	public static void aggiungiEmailProvenienzaModulo(FascicoloDTO fascicolo, String value, String moduloPath) {
		// fascicolo.getValoriDatiAggiuntivi().add(new ValoreDatoAggiuntivoDTO__(moduloPath + "_" + FASCICOLO_DA_PRATICA_EMAIL_PROVENIENZA_MODULO, value,
		// FASCICOLO_DA_PRATICA_EMAIL_PROVENIENZA_MODULO_DES, TipoDatoDTO.Testo, false, false, false, 103));
		fascicolo.getValoriDatiAggiuntivi().add(new DatoAggiuntivoValoreSingolo(moduloPath + "_" + FASCICOLO_DA_PRATICA_EMAIL_PROVENIENZA_MODULO, FASCICOLO_DA_PRATICA_EMAIL_PROVENIENZA_MODULO_DES,
				TipoDato.Testo, 103, false, false, false, new ArrayList<String>(), value));
	}

	public static Date getDataInizioBando(List<DatoAggiuntivo> valori, String moduloPath) {
		Date inizioBando = null;
		DateTimeFormat fmt = DateTimeFormat.getFormat("yyyyMMdd'T'HH:mm:ss.SSSZ");
		DateTimeFormat fmtDAG = DateTimeFormat.getFormat("dd/MM/yyyy");

		for (DatoAggiuntivo v : valori) {
			if (v.getNome().equals(moduloPath + "_" + FASCICOLO_DA_PRATICA_DATA_INIZIO_BANDO)) {
				try {
					inizioBando = fmtDAG.parse(((DatoAggiuntivoValoreSingolo) v).getValore());
				} catch (Exception e) {
					inizioBando = fmt.parse(((DatoAggiuntivoValoreSingolo) v).getValore());
				}
			}
		}
		return inizioBando;
	}

	public static Date getDataFineBando(List<DatoAggiuntivo> valori, String moduloPath) {
		Date fineBando = null;
		DateTimeFormat fmt = DateTimeFormat.getFormat("yyyyMMdd'T'HH:mm:ss.SSSZ");
		DateTimeFormat fmtDAG = DateTimeFormat.getFormat("dd/MM/yyyy");

		for (DatoAggiuntivo v : valori) {
			if (v.getNome().equals(moduloPath + "_" + FASCICOLO_DA_PRATICA_DATA_FINE_BANDO)) {

				try {
					fineBando = fmtDAG.parse(((DatoAggiuntivoValoreSingolo) v).getValore());

				} catch (Exception e) {
					fineBando = fmt.parse(((DatoAggiuntivoValoreSingolo) v).getValore());

				}

			}
		}
		return fineBando;
	}

	private static String convertDateDatiAggiuntivi(String date) {

		if (date == null)
			return null;

		DateTimeFormat fmt = DateTimeFormat.getFormat("yyyyMMdd'T'HH:mm:ss.SSSZ");
		DateTimeFormat fmtDAG = DateTimeFormat.getFormat("dd/MM/yyyy");

		try {
			Date dateMod = fmt.parse(date);
			return fmtDAG.format(dateMod);

		} catch (Exception e) {
			return date;
		}

	}

	public static List<String> getDestinatarioEmail(List<DatoAggiuntivo> valori, final String moduloPath) {
		final ArrayList<String> values = new ArrayList<String>();

		for (DatoAggiuntivo v : valori)
			v.accept(new DatoAggiuntivo.DatoAggiuntivoVisitorAdapter() {
				@Override
				public void visit(DatoAggiuntivoValoreSingolo v) {
					if (v.getNome().equals(moduloPath + "_" + FASCICOLO_DA_PRATICA_EMAIL_PROVENIENZA_MODULO)) {
						values.add(v.getValore());
					}
				}
			});

		return values;
	}
}
