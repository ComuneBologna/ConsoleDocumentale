package it.eng.cobo.consolepec.util.firmadigitale;

import it.eng.cobo.consolepec.commons.firmadigitale.FirmaDigitale;
import it.eng.cobo.consolepec.commons.firmadigitale.Firmatario;

/**
 * XXX: Sottolineo che viene fatta l'assunzione (potenzialmente errata) che il primo elemento nella lista dei firmatari sia anche l'ultimo ad aver firmato il file.<br/>
 * Da notare anche che il primo elemento e' anche l'unico mostrato per ogni versione del file
 * 
 * @author GiacomoFM
 * @since 16/feb/2018
 */
public class FirmaDigitaleUtil {

	public static final String SUCCESSFUL_OPERATION_CODE = "000";

	/**
	 * Controllo se la firma digitale &egrave; valida in base al codice della firma e al numero di firmatari
	 */
	public static boolean isFirmaDigitaleValida(final FirmaDigitale firmaDigitale) {
		return firmaDigitale != null && SUCCESSFUL_OPERATION_CODE.equals(firmaDigitale.getCodice()) && !firmaDigitale.getFirmatari().isEmpty();
	}

	/**
	 * Controlla se l'oggetto firma digitale rappresenta un file firmato
	 * 
	 * @see {@link FirmaDigitaleUtil#isFirmatoCorretto(FirmaDigitale)}
	 */
	public static boolean isFirmato(final FirmaDigitale firmaDigitale) {
		if (!isFirmaDigitaleValida(firmaDigitale))
			return false;
		return firmaDigitale.getFirmatari().get(0).getStato().equals(Firmatario.Stato.OK) || firmaDigitale.getFirmatari().get(0).getStato().equals(Firmatario.Stato.KO);
	}

	/**
	 * Controllo se l'oggetto firma digitale rappresenta un file firmato correttamente.
	 */
	public static boolean isFirmatoCorretto(final FirmaDigitale firmaDigitale) {
		if (!isFirmato(firmaDigitale))
			return false;
		return firmaDigitale.getFirmatari().get(0).getStato().equals(Firmatario.Stato.OK);
	}

	/**
	 * Seleziono il tipo firma dell'ultimo firmatario solo in caso la firma sia valida, null altrimenti
	 */
	public static Firmatario.TipoFirma getTipoFirma(final FirmaDigitale firmaDigitale) {
		if (isFirmato(firmaDigitale)) {
			return firmaDigitale.getFirmatari().get(0).getTipoFirma();
		}
		return null;
	}

	public static String getStringTipoFirma(final FirmaDigitale firmaDigitale) {
		Firmatario.TipoFirma tipoFirma = getTipoFirma(firmaDigitale);
		if (tipoFirma == null)
			return null;
		return tipoFirma.toString();
	}

	public static Firmatario.Stato getStato(final FirmaDigitale firmaDigitale) {
		Firmatario.Stato stato = null;
		for (Firmatario firmatario : firmaDigitale.getFirmatari()) {
			stato = firmatario.getStato();
			if (!Firmatario.Stato.OK.equals(stato)) {
				break;
			}
		}
		return stato;
	}

	public static String getStringStato(final FirmaDigitale firmaDigitale) {
		Firmatario.Stato stato = getStato(firmaDigitale);
		if (stato == null)
			return null;
		return stato.toString();
	}

	public static boolean getStatoFirmato(final FirmaDigitale firmaDigitale) {
		boolean firmato = false;
		for (Firmatario firmatario : firmaDigitale.getFirmatari()) {
			switch (firmatario.getStato()) {
			case NF:
				return false;
			case KO:
				return true;
			case OK:
				firmato = true;
			}
		}
		return firmato;
	}

}
