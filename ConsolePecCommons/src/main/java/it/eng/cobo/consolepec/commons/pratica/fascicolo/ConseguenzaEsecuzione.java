package it.eng.cobo.consolepec.commons.pratica.fascicolo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 *
 * @author biagiot
 *
 */
public interface ConseguenzaEsecuzione extends Serializable {

	@Data
	@AllArgsConstructor
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class CambiaStepIterConseguenzaEsecuzione implements ConseguenzaEsecuzione {
		private static final long serialVersionUID = -3592142427988892371L;

		public String valore;
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public abstract static class ModificaDatoAggiuntivoConseguenzaEsecuzione implements ConseguenzaEsecuzione {
		private static final long serialVersionUID = -1493074925978759699L;

		private String nome;
	}

	@Data
	@EqualsAndHashCode(callSuper = false)
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class ModificaDatoAggiuntivoValoreConseguenzaEsecuzione extends ModificaDatoAggiuntivoConseguenzaEsecuzione {
		private static final long serialVersionUID = 2278770219945536244L;

		private String valore;

		@Builder
		public ModificaDatoAggiuntivoValoreConseguenzaEsecuzione(String nome, String valore) {
			super(nome);
			this.valore = valore;
		}
	}

	@Data
	@EqualsAndHashCode(callSuper = false)
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class ModificaDatoAggiuntivoValoriConseguenzaEsecuzione extends ModificaDatoAggiuntivoConseguenzaEsecuzione {
		private static final long serialVersionUID = 4175342884757869538L;

		private List<String> valori = new ArrayList<String>();

		@Builder
		public ModificaDatoAggiuntivoValoriConseguenzaEsecuzione(String nome, List<String> valori) {
			super(nome);
			this.valori = valori;
		}
	}

	/**
	 * I valori di questi parametri vengono assegnati dalla configurazione e rappresentano i dati aggiuntivi in cui vengono ribaltati.<br>
	 * <br>
	 * Per esempio:<br>
	 * <code>codiceFiscale = "indirizzo"</code><br>
	 * dall'anagrafica prendo il valore del codice fiscale (la chiave che sto usando) e lo ribalto sul dato aggiuntivo che avr&agrave; come nome il valore "indirizzo"
	 */
	@Data
	@EqualsAndHashCode(callSuper = false)
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	@AllArgsConstructor
	public static class RibaltaDatiAggiuntiviDaAnagraficaConseguenzaEsecuzione implements ConseguenzaEsecuzione {
		private static final long serialVersionUID = -7773471677991446266L;

		private String nome;
		private String cognome;
		private String codiceFiscale;
		private String luogoNascita;
		private String dataNascita;
		private String tipologiaIndirizzo;
		private String indirizzo;
		private String via;
		private String civico;
		private String esponente;
		private String piano;
		private String interno;
		private String cap;
		private String comune;
		private String nazione;
		private String tipologiaTelefono;
		private String telefono;

	}

}
