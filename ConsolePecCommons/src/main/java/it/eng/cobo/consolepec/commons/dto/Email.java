package it.eng.cobo.consolepec.commons.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Giacomo F.M.
 * @since 2019-08-06
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class Email extends Pratica {

	private static final long serialVersionUID = -669639522360236816L;

	private String oggetto;
	private String messageID;
	private String body;
	private String firma;
	private Date dataInvio;
	private Date dataRicezione;

	private String mittente;
	@Setter(AccessLevel.NONE)
	private List<Destinatario> destinatari = new ArrayList<>();
	@Setter(AccessLevel.NONE)
	private List<Destinatario> destinatariCC = new ArrayList<>();

	@Setter(AccessLevel.NONE)
	private List<Ricevuta> ricevute = new ArrayList<>();

	private ProtocollazioneCapofila protocollazione;

	private TipoEmail tipoEmail;

	private String replyTo;

	public Email() {
		super(Tipo.EMAIL);
	}

	public enum TipoEmail {
		PEC, NORMALE;
	}

	@Data
	public static class Destinatario {
		private String destinatario;
		private String errore;
		private boolean accettazione;
		private boolean consegna;
		private StatoEmail stato;
		private TipoDestinatario tipo;

		public enum StatoEmail {
			INVIATO, ACCETTATO, NON_ACCETTATO, CONSEGNATO, NON_CONSEGNATO;
		}

		public enum TipoDestinatario {
			ESTERNO, CERTIFICATO;
		}
	}

	@Data
	@AllArgsConstructor
	public static class DestinatarioRicevuta {
		private String destinatario;
		private TipoDestinatario tipo;

		@Getter
		@AllArgsConstructor
		public enum TipoDestinatario {
			ESTERNO("esterno"), CERTIFICATO("certificato");

			private String descrizione;

			public static TipoDestinatario fromDescrizione(String label) {

				for (TipoDestinatario t : TipoDestinatario.values()) {
					if (t.getDescrizione().equals(label)) {
						return t;
					}
				}

				return null;

			}
		}

	}

	@Data
	public static class Ricevuta {
		private String mittente;
		@Setter(AccessLevel.NONE)
		private List<DestinatarioRicevuta> destinatari = new ArrayList<>();
		private String risposte;
		private String oggetto;

		private DataPec data;
		private TipoRicevuta tipo;
		private String gestoreEmittente;
		private String identificativo;
		private String msgid;

		private String consegna;
		private String tipoConsegna;

		private String errore;
		private String erroreEsteso;

		public static enum TipoRicevuta {
			ACCETTAZIONE, NON_ACCETTAZIONE, CONSEGNA, PREAVVISO_MANCATA_CONSEGNA, MANCATA_CONSEGNA, PRESA_IN_CARICO, RILEVAZIONE_VIRUS;
		}

	}

	@Data
	@AllArgsConstructor
	public static class DataPec {
		private String zona;
		private String giorno;
		private String ora;
	}

}
