package it.eng.cobo.consolepec.commons.datigenerici;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 *
 * @author biagiot
 *
 */
@Data
public class AnagraficaAmministrazione {

	private String codiceAmministrativo;
	private String denominazioneEnte;
	private String comune;
	private String nome;
	private String cognome;
	private String cap;
	private String provincia;
	private String regione;
	private String indirizzo;
	private String codiceFiscale;
	private String fonte;
	private List<Email> indirizziEmail = new ArrayList<>();

	@Data
	public static class Email {

		private String indirizzoEmail;
		private String tipologiaEmail;
	}

}
