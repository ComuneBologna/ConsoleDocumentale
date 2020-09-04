package it.eng.cobo.consolepec.util.utente;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo.Stato;

public class UtenteUtils {
	
	public static final String MATRICOLA_UTENTI_NON_COMUNALI = "XXXXXXX";
	public static final String MATRICOLA_UTENTI_NON_COMUNALI_2 = "000000X";

	public static String calcolaEtichettaRuoloPersonale(String nome, String cognome, String codiceFiscale, String matricola) {
		return calcolaEtichettaRuoloPersonale(nome + " " + cognome, codiceFiscale, matricola);
	}
	
	public static String calcolaEtichettaRuoloPersonale(String nomeCompleto, String codiceFiscale, String matricola) {
		StringBuilder sb = new StringBuilder();
		if (MATRICOLA_UTENTI_NON_COMUNALI.equalsIgnoreCase(matricola) || MATRICOLA_UTENTI_NON_COMUNALI_2.equalsIgnoreCase(matricola))
			sb.append(nomeCompleto).append(" - ").append(codiceFiscale);
		else
			sb.append(nomeCompleto).append(" - ").append(matricola);
		return sb.toString();
	}
	
	public static String calcolaRuoloPersonale(String nome, String cognome, String codiceFiscale, String matricola) {
		StringBuilder sb = new StringBuilder();
		sb.append(nome).append(" ").append(cognome).append(" - ").append(matricola).append(" - ").append(codiceFiscale);
		return sb.toString();
	}
	
	public static String calcolaEtichettaRuoloPersonale(String ruoloPersonale) {
		if (ruoloPersonale == null || !ruoloPersonale.contains(" - ") || ruoloPersonale.split(" - ").length != 3) {
			throw new IllegalArgumentException("Ruolo personale " + ruoloPersonale + " non valido");
		}
		
		String[] splitted = ruoloPersonale.split(" - ");
		return calcolaEtichettaRuoloPersonale(splitted[0], splitted[2], splitted[1]);
	}
	
	public static AnagraficaRuolo getAnagraficaRuoloPersonale(String nome, String cognome, String codiceFiscale, String matricola) {
		AnagraficaRuolo ar = new AnagraficaRuolo();
		ar.setStato(Stato.ATTIVA);
		ar.setRuolo(calcolaRuoloPersonale(nome, cognome, codiceFiscale, matricola));
		ar.setEtichetta(calcolaEtichettaRuoloPersonale(nome, cognome, codiceFiscale, matricola));
		return ar;
	}
}
