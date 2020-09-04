package it.eng.cobo.consolepec.util.validation;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Strings;

import it.eng.cobo.consolepec.commons.rubrica.Anagrafica;
import it.eng.cobo.consolepec.commons.rubrica.Indirizzo;
import it.eng.cobo.consolepec.commons.rubrica.PersonaFisica;
import it.eng.cobo.consolepec.commons.rubrica.PersonaGiuridica;

public class ValidationUtilities {

	public static final String FAKE_CODICE_FISCALE_VALIDO = "XXXXXX00A41A944C";
	public static final String FAKE_PARTITA_IVA_VALIDA = "00000000000";
	public final static String RE_CODICE_FISCALE = "[a-zA-Z]{6}[0-9]{2}[a-zA-Z]{1}[0-9]{2}([a-zA-Z]{1}[0-9]{3})[a-zA-Z]{1}";
	public final static String RE_NUMERIC = "-?\\d+(\\.\\d+)?";
	public final static String RE_BA01_CHAR_COMPATIBILITY = "[a-zA-Z0-9\\(\\)\\s\\r\\n\\?\\*\\+\\.\\-\\_,;:!\"'=%&@/]+";
	public final static String INT_NUMERIC = "^0$|^[1-9][\\d]*$";
	public final static String TELEPHONE_NUMBER = "^\\+[\\d]+$|^[\\d]*$";

	public static final String RFC_EMAIL_REGEX = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

	public static List<String> valida(Anagrafica anagrafica) {
		List<String> errors = new ArrayList<String>();

		if (anagrafica != null) {
			validaIndirizzi(anagrafica.getIndirizzi(), errors);

			if (anagrafica instanceof PersonaFisica) {
				validaPersonaFisica((PersonaFisica) anagrafica, errors);
			} else if (anagrafica instanceof PersonaGiuridica) {
				validaPersonaGiuridica((PersonaGiuridica) anagrafica, errors);
			} else {
				errors.add("L'oggetto anagrafica non &egrave; istanziato correttamente.");
			}

		} else {
			errors.add("L'oggetto anagrafica non &egrave; istanziato correttamente.");
		}

		return errors;
	}

	private static void validaPersonaFisica(PersonaFisica pf, List<String> errors) {
		String cf = controllaCF(pf.getCodiceFiscale());
		if (!Strings.isNullOrEmpty(cf)) {
			errors.add(cf);
		}
	}

	private static void validaPersonaGiuridica(PersonaGiuridica pg, List<String> errors) {
		String piva = controllaPIVA(pg.getPartitaIva());
		if (!Strings.isNullOrEmpty(piva)) {
			errors.add(piva);
		}
	}

	private static void validaIndirizzi(List<Indirizzo> indirizzi, List<String> errors) {

		if (indirizzi != null) {

			int tipologieResidenza = 0;

			for (Indirizzo indirizzo : indirizzi) {

				if (!Strings.isNullOrEmpty(indirizzo.getTipologia()) && Indirizzo.TIPOLOGIA_INDIRIZZO_LAG.equalsIgnoreCase(indirizzo.getTipologia())) {
					tipologieResidenza++;
				}

				if (!Strings.isNullOrEmpty(indirizzo.getVia())) {

					if (Strings.isNullOrEmpty(indirizzo.getCivico())) {

						if (!Strings.isNullOrEmpty(indirizzo.getEsponente())) {
							errors.add("Non &egrave; possibile valorizzare l'esponente senza civico");
							return;
						}

						if (!Strings.isNullOrEmpty(indirizzo.getInterno())) {
							errors.add("Non &egrave; possibile valorizzare l'interno senza civico");
							return;
						}

						if (!Strings.isNullOrEmpty(indirizzo.getPiano())) {
							errors.add("Non &egrave; possibile valorizzare il piano senza civico");
							return;
						}

					}

				} else {
					if (!Strings.isNullOrEmpty(indirizzo.getCivico())) {
						errors.add("Non &egrave; possibile valorizzare il civico senza la via");
						return;
					}
				}
			}

			if (tipologieResidenza > 1) {
				errors.add("Non &egrave; possibile inserire pi&uacute; indirizzi con tipologia " + Indirizzo.TIPOLOGIA_INDIRIZZO_LAG);
				return;
			}
		}
	}

	public static String controllaCF(String cf) {
		int i, s, c;
		String cf2;
		int setdisp[] = { 1, 0, 5, 7, 9, 13, 15, 17, 19, 21, 2, 4, 18, 20, 11, 3, 6, 8, 12, 14, 16, 10, 22, 25, 24, 23 };
		if (cf.length() == 0) {
			return "Nessun codice fiscale indicato";
		}
		if (cf.length() != 16) {
			return "La lunghezza del codice fiscale non &egrave; corretta: il codice fiscale dovrebbe essere lungo esattamente 16 caratteri.";
		}
		cf2 = cf.toUpperCase();
		for (i = 0; i < 16; i++) {
			c = cf2.charAt(i);
			if (!(c >= '0' && c <= '9' || c >= 'A' && c <= 'Z')) {
				return "Il codice fiscale contiene dei caratteri non validi: i soli caratteri validi sono le lettere e le cifre.";
			}
		}
		s = 0;
		for (i = 1; i <= 13; i += 2) {
			c = cf2.charAt(i);
			if (c >= '0' && c <= '9')
				s = s + c - '0';
			else
				s = s + c - 'A';
		}
		for (i = 0; i <= 14; i += 2) {
			c = cf2.charAt(i);
			if (c >= '0' && c <= '9')
				c = c - '0' + 'A';
			s = s + setdisp[c - 'A'];
		}
		if (s % 26 + 'A' != cf2.charAt(15)) {
			return "Il codice fiscale non &egrave; corretto: il codice di controllo non corrisponde.";
		}
		return "";
	}

	public static String controllaPIVA(String pi) {
		int i, c, s;
		if (pi.length() == 0) {
			return "Nessuna partita IVA indicata.";
		}
		if (pi.length() != 11) {
			return "La lunghezza della partita IVA non &egrave; corretta: la partita IVA dovrebbe essere lunga esattamente 11 caratteri.";
		}
		for (i = 0; i < 11; i++) {
			if (pi.charAt(i) < '0' || pi.charAt(i) > '9') {
				return "La partita IVA contiene dei caratteri non ammessi: la partita IVA dovrebbe contenere solo cifre.";
			}
		}
		s = 0;
		for (i = 0; i <= 9; i += 2) {
			s += pi.charAt(i) - '0';
		}
		for (i = 1; i <= 9; i += 2) {
			c = 2 * (pi.charAt(i) - '0');
			if (c > 9)
				c = c - 9;
			s += c;
		}
		if ((10 - s % 10) % 10 != pi.charAt(10) - '0') {
			return "La partita IVA non &egrave; valida: il codice di controllo non corrisponde.";
		}
		return "";
	}

	/**
	 * Controlla la correttezza di un indirizzo email secondo una RegEx che cerca di attenersi con la maggior precisione possibile allo standard RFC
	 * 
	 * @param emailAddress indirizzo email da validare
	 * @return <b>true</b> solo quando l'indirizzo email si attiene allo standard RFC
	 * @see <a href="https://emailregex.com/">https://emailregex.com/</a>
	 */
	public static boolean validateEmailAddress(String emailAddress) {
		// return emailAddress.matches("^([a-zA-Z0-9_.\\-+])+@(([a-zA-Z0-9\\-])+\\.)+[a-zA-Z0-9]{2,4}$");
		if (emailAddress == null || emailAddress.isEmpty())
			return false;
		return emailAddress.toLowerCase().matches(RFC_EMAIL_REGEX);
	}

	/**
	 * Controlla se una stringa è un numerico, può essere anche negativo.
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		return str.matches(RE_NUMERIC);
	}

	/**
	 * Controlla se una stringa è un intero.
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isIntNumber(String str) {
		return str.matches(INT_NUMERIC);
	}

	/**
	 * Controlla se una stringa è un numero di telefono.
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isTelephoneNumber(String str) {
		return str.matches(TELEPHONE_NUMBER);
	}

	/**
	 * controlla che una stringa non contenga caratteri speciali non supportati da BA01
	 * 
	 * @param valore
	 * @return
	 */
	public static boolean isBA01CharsCompatibility(String valore) {
		if ((valore == null) || (valore.isEmpty()))
			return true;
		return valore.matches(RE_BA01_CHAR_COMPATIBILITY);
	}

	/**
	 * Controlla la correttezza formale del CF
	 * 
	 * @param codiceFiscale
	 * @return
	 */
	public static boolean isCodiceFiscaleValid(String codiceFiscale) {
		return codiceFiscale.matches(RE_CODICE_FISCALE);
	}

	/**
	 * Controlla la correttezza formale della Partita Iva
	 * 
	 * @param pIva
	 * @return
	 */
	public static boolean isPartitaIvaValid(String pIva) {
		return controllaPIVA(pIva).isEmpty();
	}

}
