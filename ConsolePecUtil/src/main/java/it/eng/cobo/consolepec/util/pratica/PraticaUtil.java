package it.eng.cobo.consolepec.util.pratica;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaComunicazione;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaEmailOut;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaFascicolo;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaIngresso;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaModello;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaPraticaModulistica;
import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;

public class PraticaUtil {

	public static final String FASCICOLO_PREFIX = "FASCICOLO_";
	public static final String FASCICOLO_ETICHETTA_PREFIX = "FASCICOLO";

	public static boolean isFascicolo(String tipologiaPratica) {
		return tipologiaPratica != null && (tipologiaPratica.startsWith(FASCICOLO_PREFIX) //
				|| tipologiaPratica.equals(TipologiaPratica.FASCICOLO.getNomeTipologia()) //
				|| tipologiaPratica.equals(TipologiaPratica.PRATICA_PROCEDI.getNomeTipologia() //
				));
	}

	public static boolean isIngresso(String tipologiaPratica) {
		return (TipologiaPratica.EMAIL_IN.getNomeTipologia().equals(tipologiaPratica) || TipologiaPratica.EMAIL_EPROTOCOLLO.getNomeTipologia().equals(tipologiaPratica));
	}

	public static boolean isEmailOut(String tipologiaPratica) {
		return TipologiaPratica.EMAIL_OUT.getNomeTipologia().equals(tipologiaPratica);
	}

	public static boolean isComunicazione(String tipologiaPratica) {
		return TipologiaPratica.COMUNICAZIONE.getNomeTipologia().equals(tipologiaPratica);
	}

	public static boolean isModello(String tipologiaPratica) {
		return TipologiaPratica.MODELLO_MAIL.getNomeTipologia().equals(tipologiaPratica) || TipologiaPratica.MODELLO_PDF.getNomeTipologia().equals(tipologiaPratica);
	}

	public static boolean isPraticaModulistica(String tipologiaPratica) {
		return TipologiaPratica.PRATICA_MODULISTICA.getNomeTipologia().equals(tipologiaPratica);
	}

	public static boolean isFascicolo(TipologiaPratica tipologiaPratica) {
		return isFascicolo(tipologiaPratica.getNomeTipologia());
	}

	public static boolean isIngresso(TipologiaPratica tipologiaPratica) {
		return isIngresso(tipologiaPratica.getNomeTipologia());
	}

	public static boolean isEmailOut(TipologiaPratica tipologiaPratica) {
		return isEmailOut(tipologiaPratica.getNomeTipologia());
	}

	public static boolean isComunicazione(TipologiaPratica tipologiaPratica) {
		return isComunicazione(tipologiaPratica.getNomeTipologia());
	}

	public static boolean isModello(TipologiaPratica tipologiaPratica) {
		return isModello(tipologiaPratica.getNomeTipologia());
	}

	public static boolean isPraticaModulistica(TipologiaPratica tipologiaPratica) {
		return isPraticaModulistica(tipologiaPratica.getNomeTipologia());
	}

	public static boolean isPraticaProcedi(TipologiaPratica tipologiaPratica) {
		return TipologiaPratica.PRATICA_PROCEDI.equals(tipologiaPratica);
	}

	public static TipologiaPratica toTipologiaPratica(AnagraficaFascicolo a) {

		if (a != null) {
			TipologiaPratica tp = new TipologiaPratica(a.getNomeTipologia());
			tp.setEtichettaTipologia(a.getEtichettaTipologia());
			tp.setAzioniDettaglio(a.isAzioniDettaglio());
			tp.setDettaglioNameToken(a.getDettaglioNameToken());
			tp.setEtichettaTipologia(a.getEtichettaTipologia());
			tp.setStato(a.getStato());
			tp.setProtocollabile(a.isProtocollabile());
			if (isFascicolo(tp)) return tp;
		}

		return null;
	}

	public static List<TipologiaPratica> fascicoliToTipologiePratiche(List<AnagraficaFascicolo> a) {

		List<TipologiaPratica> res = new ArrayList<TipologiaPratica>();

		if (a != null) {
			for (AnagraficaFascicolo af : a) {
				TipologiaPratica tp = toTipologiaPratica(af);
				if (tp != null && !res.contains(tp)) res.add(tp);
			}
		}

		return res;
	}

	public static TipologiaPratica toTipologiaPratica(AnagraficaComunicazione a) {

		if (a != null) {
			TipologiaPratica tp = new TipologiaPratica(a.getNomeTipologia());
			tp.setEtichettaTipologia(a.getEtichettaTipologia());
			tp.setAzioniDettaglio(a.isAzioniDettaglio());
			tp.setDettaglioNameToken(a.getDettaglioNameToken());
			tp.setEtichettaTipologia(a.getEtichettaTipologia());
			tp.setStato(a.getStato());
			tp.setProtocollabile(a.isProtocollabile());
			if (isComunicazione(tp)) return tp;
		}

		return null;
	}

	public static List<TipologiaPratica> comunicazioniToTipologiePratiche(List<AnagraficaComunicazione> modelli) {

		List<TipologiaPratica> res = new ArrayList<TipologiaPratica>();

		if (modelli != null) {
			for (AnagraficaComunicazione af : modelli) {
				TipologiaPratica tp = toTipologiaPratica(af);
				if (tp != null && !res.contains(tp)) res.add(tp);
			}
		}

		return res;
	}

	public static TipologiaPratica toTipologiaPratica(AnagraficaEmailOut a) {

		if (a != null) {
			TipologiaPratica tp = new TipologiaPratica(a.getNomeTipologia());
			tp.setEtichettaTipologia(a.getEtichettaTipologia());
			tp.setAzioniDettaglio(a.isAzioniDettaglio());
			tp.setDettaglioNameToken(a.getDettaglioNameToken());
			tp.setEtichettaTipologia(a.getEtichettaTipologia());
			tp.setStato(a.getStato());
			tp.setProtocollabile(a.isProtocollabile());
			if (isEmailOut(tp)) return tp;
		}

		return null;
	}

	public static List<TipologiaPratica> mailInUscitaToTipologiePratiche(List<AnagraficaEmailOut> amu) {

		List<TipologiaPratica> res = new ArrayList<TipologiaPratica>();

		if (amu != null) {
			for (AnagraficaEmailOut af : amu) {
				TipologiaPratica tp = toTipologiaPratica(af);
				if (tp != null && !res.contains(tp)) res.add(tp);
			}
		}

		return res;
	}

	public static TipologiaPratica toTipologiaPratica(AnagraficaIngresso a) {

		if (a != null) {
			TipologiaPratica tp = new TipologiaPratica(a.getNomeTipologia());
			tp.setEtichettaTipologia(a.getEtichettaTipologia());
			tp.setAzioniDettaglio(a.isAzioniDettaglio());
			tp.setDettaglioNameToken(a.getDettaglioNameToken());
			tp.setEtichettaTipologia(a.getEtichettaTipologia());
			tp.setStato(a.getStato());
			tp.setProtocollabile(a.isProtocollabile());
			if (isIngresso(tp)) return tp;
		}

		return null;
	}

	public static List<TipologiaPratica> ingressiToTipologiePratiche(List<AnagraficaIngresso> ingressi) {

		List<TipologiaPratica> res = new ArrayList<TipologiaPratica>();

		if (ingressi != null) {
			for (AnagraficaIngresso af : ingressi) {
				TipologiaPratica tp = toTipologiaPratica(af);
				if (tp != null && !res.contains(tp)) res.add(tp);
			}
		}

		return res;
	}

	public static TipologiaPratica toTipologiaPratica(AnagraficaPraticaModulistica a) {

		if (a != null) {
			TipologiaPratica tp = new TipologiaPratica(a.getNomeTipologia());
			tp.setEtichettaTipologia(a.getEtichettaTipologia());
			tp.setAzioniDettaglio(a.isAzioniDettaglio());
			tp.setDettaglioNameToken(a.getDettaglioNameToken());
			tp.setEtichettaTipologia(a.getEtichettaTipologia());
			tp.setStato(a.getStato());
			tp.setProtocollabile(a.isProtocollabile());
			if (isPraticaModulistica(tp)) return tp;
		}

		return null;
	}

	public static List<TipologiaPratica> praticheModulisticaToTipologiePratiche(List<AnagraficaPraticaModulistica> modelli) {

		List<TipologiaPratica> res = new ArrayList<TipologiaPratica>();

		if (modelli != null) {
			for (AnagraficaPraticaModulistica af : modelli) {
				TipologiaPratica tp = toTipologiaPratica(af);
				if (tp != null && !res.contains(tp)) res.add(tp);
			}
		}

		return res;
	}

	public static TipologiaPratica toTipologiaPratica(AnagraficaModello a) {

		if (a != null) {
			TipologiaPratica tp = new TipologiaPratica(a.getNomeTipologia());
			tp.setEtichettaTipologia(a.getEtichettaTipologia());
			tp.setAzioniDettaglio(a.isAzioniDettaglio());
			tp.setDettaglioNameToken(a.getDettaglioNameToken());
			tp.setEtichettaTipologia(a.getEtichettaTipologia());
			tp.setStato(a.getStato());
			tp.setProtocollabile(a.isProtocollabile());
			if (isModello(tp)) return tp;
		}

		return null;
	}

	public static List<TipologiaPratica> modelliToTipologiePratiche(List<AnagraficaModello> modelli) {

		List<TipologiaPratica> res = new ArrayList<TipologiaPratica>();

		if (modelli != null) {
			for (AnagraficaModello af : modelli) {
				TipologiaPratica tp = toTipologiaPratica(af);
				if (tp != null && !res.contains(tp)) res.add(tp);
			}
		}

		return res;
	}

	public static List<TipologiaPratica> toTipologiePratiche(List<AnagraficaFascicolo> f, List<AnagraficaIngresso> i, List<AnagraficaEmailOut> u, List<AnagraficaComunicazione> c,
			List<AnagraficaPraticaModulistica> p, List<AnagraficaModello> m) {
		List<TipologiaPratica> res = new ArrayList<TipologiaPratica>();

		res.addAll(fascicoliToTipologiePratiche(f));
		res.addAll(ingressiToTipologiePratiche(i));
		res.addAll(mailInUscitaToTipologiePratiche(u));
		res.addAll(comunicazioniToTipologiePratiche(c));
		res.addAll(praticheModulisticaToTipologiePratiche(p));
		res.addAll(modelliToTipologiePratiche(m));

		return res;
	}

	public static boolean isSport(TipologiaPratica tipologiaPratica) {

		return tipologiaPratica != null //
				&& (tipologiaPratica.equals(TipologiaPratica.FASCICOLO_SPORT_BORGOPANIGALE) //
						|| tipologiaPratica.equals(TipologiaPratica.FASCICOLO_SPORT_NAVILE) //
						|| tipologiaPratica.equals(TipologiaPratica.FASCICOLO_SPORT_PORTO) //
						|| tipologiaPratica.equals(TipologiaPratica.FASCICOLO_SPORT_RENO) //
						|| tipologiaPratica.equals(TipologiaPratica.FASCICOLO_SPORT_SANDONATO) //
						|| tipologiaPratica.equals(TipologiaPratica.FASCICOLO_SPORT_SANTOSTEFANO) //
						|| tipologiaPratica.equals(TipologiaPratica.FASCICOLO_SPORT_SANVITALE) //
						|| tipologiaPratica.equals(TipologiaPratica.FASCICOLO_SPORT_SARAGOZZA) //
						|| tipologiaPratica.equals(TipologiaPratica.FASCICOLO_SPORT_SAVENA) //
						|| tipologiaPratica.equals(TipologiaPratica.FASCICOLO_SPORT_PORTO_SARAGOZZA) //
						|| tipologiaPratica.equals(TipologiaPratica.FASCICOLO_SPORT_BORGOPANIGALE_RENO) //
						|| tipologiaPratica.equals(TipologiaPratica.FASCICOLO_SPORT_SANDONATO_SANVITALE) //
						|| tipologiaPratica.equals(TipologiaPratica.FASCICOLO_SALUTE_SPORT));
	}

	public static List<TipologiaPratica> getTipologieElettorale() {
		return Arrays.asList(TipologiaPratica.FASCICOLO_ELETTORALE_ELETTORE, TipologiaPratica.FASCICOLO_ELETTORALE_COMUNICAZIONI, TipologiaPratica.FASCICOLO_ELETTORALE_GENERICO);
	}

	public static boolean isConfigurazioneGenericaFascicolo(String tipologia) {
		return TipologiaPratica.FASCICOLO.getNomeTipologia().equals(tipologia);
	}

	public static boolean isFascicoloPersonale(TipologiaPratica tipologia) {
		return TipologiaPratica.FASCICOLO_PERSONALE.equals(tipologia);
	}

	public static boolean isFascicoloRiservato(TipologiaPratica tipologia) {
		return TipologiaPratica.FASCICOLO_RISERVATO.equals(tipologia);
	}

	public static boolean isFascicoloFatturazioneElettronica(TipologiaPratica tipologia) {
		return TipologiaPratica.FASCICOLO_FATTURAZIONE_ELETTRONICA.equals(tipologia);
	}

	public static List<TipologiaPratica> getTipologieEmailOut() {
		return Arrays.asList(TipologiaPratica.EMAIL_OUT);
	}

	public static List<TipologiaPratica> getTipologieEmailIn() {
		return Arrays.asList(TipologiaPratica.EMAIL_IN, TipologiaPratica.EMAIL_EPROTOCOLLO);
	}

	public static boolean validaEtichettaFascicolo(String etichetta) {
		return etichetta != null && !etichetta.trim().isEmpty() && etichetta.contains(" ") && etichetta.toUpperCase().startsWith(PraticaUtil.FASCICOLO_ETICHETTA_PREFIX) && !etichetta.endsWith(" ")
				&& !etichetta.contains("_");
	}

	public static boolean validaNomeTipologiaFascicolo(String nomeTipologia) {
		return nomeTipologia != null && !nomeTipologia.trim().isEmpty() && nomeTipologia.toUpperCase().startsWith(FASCICOLO_PREFIX) && !nomeTipologia.contains(" ");
	}
}
