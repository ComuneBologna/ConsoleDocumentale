package it.eng.consolepec.xmlplugin.pratica.email;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.TipoApiTaskPEC;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
/**
 * Il bean contiene tutti i dati caratterizzanti una pratica di tipo Email.
 *
 * @author pluttero
 *
 */
public class DatiEmail extends DatiPratica {
	@Getter
	@Setter(value = AccessLevel.PROTECTED)
	private String mittente, oggetto, body, messageID, firma;
	@Getter
	@Setter(value = AccessLevel.PROTECTED)
	private Destinatario destinatarioPrincipale;
	@Getter
	private Date dataRicezione;
	@Getter
	@Setter
	private Date dataInvio;
	@Getter
	@Setter
	private boolean ricevutaConsegna;
	@Getter
	@Setter
	private boolean ricevutaAccettazione;
	// questo ha il getter furbo
	@Setter(value = AccessLevel.PROTECTED)
	private List<Ricevuta> ricevute = new ArrayList<DatiEmail.Ricevuta>();
	@Getter
	@Setter
	private TipoEmail tipoEmail;
	@Getter
	@Setter
	private DatiEmail.Stato stato;
	@Getter
	@Setter(value = AccessLevel.PROTECTED)
	private String messageIDReinoltro;
	@Getter
	@Setter(value = AccessLevel.PROTECTED)
	private ProtocollazionePEC protocollazionePec;
	@Getter
	private final List<Destinatario> destinatariCC = new ArrayList<Destinatario>();
	@Getter
	private final List<Destinatario> destinatari = new ArrayList<Destinatario>();
	@Getter
	private final List<String> destinatariInoltro = new ArrayList<String>();
	@Getter
	@Setter
	private BigInteger progressivoInoltro;
	@Getter
	@Setter
	private boolean notificaRifiutoInoltro;
	@Getter
	@Setter
	private Interoperabile interoperabile;
	@Getter
	@Setter
	private String replyTo;

	@Getter
	@Setter
	private Integer idEmailServer;

	public DatiEmail() {

	}

	protected DatiEmail(String messageID, String folderPath, String consoleFileName, Date dataRicezione, String mittente, String oggetto, Destinatario destinatarioPrincipale, String body,
			String firma, TipoEmail tipoEmail, Date dataCreazione, Destinatario[] destinatari, Destinatario[] destinatariCC, String[] destinatariInoltro, BigInteger progressivoInoltro,
			ProtocollazionePEC protocollazionePEC, String titolo, String idDocumentale, String utenteCreazione, String usernameCreazione, Utente inCarico, List<GruppoVisibilita> gruppi,
			String messageIDReinoltro, Interoperabile interoperabile, String note, String replyTo, Integer idEmailServer) {

		this.setFolderPath(folderPath);
		this.setConsoleFileName(consoleFileName);
		this.setDataCreazione(dataCreazione == null ? new Date() : dataCreazione);
		this.setProvenienza(mittente);
		this.setTitolo(titolo);
		this.dataRicezione = dataRicezione;
		this.mittente = mittente;
		this.oggetto = oggetto;
		this.destinatarioPrincipale = destinatarioPrincipale;
		this.body = body;
		this.firma = firma;
		this.stato = Stato.IN_GESTIONE;
		this.tipoEmail = tipoEmail;
		this.messageID = messageID;
		this.setUsernameCreazione(usernameCreazione);
		this.setUtenteCreazione(utenteCreazione);
		if (destinatari != null) {
			for (Destinatario destinatario : destinatari) {
				this.destinatari.add(destinatario);
			}
		}
		if (destinatariCC != null) {
			for (Destinatario cc : destinatariCC) {
				this.destinatariCC.add(cc);
			}
		}
		if (destinatariInoltro != null) {
			for (String inoltro : destinatariInoltro) {
				this.destinatariInoltro.add(inoltro);
			}
		}
		this.progressivoInoltro = progressivoInoltro;
		this.protocollazionePec = protocollazionePEC;
		this.setIdDocumentale(idDocumentale);
		this.setGruppiVisibilita(new TreeSet<DatiPratica.GruppoVisibilita>(gruppi));
		this.setInCaricoA(inCarico);
		this.setMessageIDReinoltro(messageIDReinoltro);
		this.interoperabile = interoperabile;
		this.setNote(note);
		this.replyTo = replyTo;
		this.idEmailServer = idEmailServer;
		initOperazioniSuperUtente();
		initOperazioniAssegnaEsterno();
	}

	protected void initOperazioniSuperUtente() {
		getOperazioniSuperUtente().add(new Operazione(TipoApiTaskPEC.RIASSEGNA.name(), false));
	}

	protected void initOperazioniAssegnaEsterno() {
		getOperazioniAssegnaUtenteEsterno().add(new Operazione(TipoApiTaskPEC.ELIMINA_BOZZA.name()));
		getOperazioniAssegnaUtenteEsterno().add(new Operazione(TipoApiTaskPEC.MODIFICA_BOZZA.name()));
		getOperazioniAssegnaUtenteEsterno().add(new Operazione(TipoApiTaskPEC.INVIA_PEC.name()));
		getOperazioniAssegnaUtenteEsterno().add(new Operazione(TipoApiTaskPEC.AGGIUNGI_ALLEGATO.name()));

	}

	public List<Ricevuta> getRicevute() {
		return new ArrayList<Ricevuta>(ricevute);
	}

	/**
	 * Stati di una pratica Email
	 *
	 * @author pluttero
	 *
	 */
	public enum Stato {
		IN_GESTIONE("In gestione", 0), ARCHIVIATA("Archiviata", 10), NOTIFICATA("Notificata", 11), ELIMINATA("Eliminata", 20), RESPINTA("Respinta", 21), RICONSEGNATA("Riconsegnata", 22),
		SCARTATA("Scartata", 23),

		// stati email out
		BOZZA("Bozza", 30), DA_INVIARE("Da inviare", 40), NON_INVIATA("Non inviata", 41), // serve per gestire manualemente alcuni casi di smaialamento
		INATTESADIPRESAINCARICO("Da inviare", 50), PRESAINCARICO("Presa in carico", 60), // con ricevuta di accettazione
		MANCATA_ACCETTAZIONE("Mancata accettazione", 61), MANCATA_CONSEGNA("Mancata consegna", 62), MANCATA_CONSEGNA_IN_REINOLTRO("Mancata consegna in reinoltro", 64),
		PREAVVISO_MANCATA_CONSEGNA("Preavviso mancata consegna", 65), PARZIALMENTECONSEGNATA("Parzialmente Consegnata", 70), CONSEGNA_SENZA_ACCETTAZIONE("Consegnata", 75),
		CONSEGNATA("Consegnata", 80);

		private String label;
		private int idStato;

		Stato(String label, int idStato) {
			this.label = label;
			this.idStato = idStato;
		}

		public String getLabel() {
			return label;
		}

		public int getIdStato() {
			return idStato;
		}
	}

	public enum TipoEmail {
		PEC("Pec"), NORMALE("Normale");

		private String label;

		TipoEmail(String label) {
			this.label = label;
		}

		public String getLabel() {
			return label;
		}
	}

	@ToString
	public static class Destinatario {

		public enum TipoEmail {
			esterno, certificato;
		}

		public enum StatoDestinatario {
			INVIATO, ACCETTATO, NON_ACCETTATO, CONSEGNATO, NON_CONSEGNATO;
		}

		@Setter
		@Getter
		String destinatario, errore;
		@Setter
		@Getter
		TipoEmail tipo;
		@Getter
		@Setter
		boolean accettazione, consegna;
		@Getter
		@Setter
		StatoDestinatario statoDestinatario;

		public Destinatario(String destinatario, TipoEmail tipo) {
			this.destinatario = destinatario;
			this.tipo = tipo;
		}

		public Destinatario(String destinatario, String errore, TipoEmail tipo, boolean accettazione, boolean consegna) {
			this(destinatario, tipo);
			this.errore = errore;
			this.accettazione = accettazione;
			this.consegna = consegna;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			if (!(obj instanceof Destinatario)) {
				return false;
			}
			return destinatario.equalsIgnoreCase(((Destinatario) obj).getDestinatario());
		}
	}

	@AllArgsConstructor
	public static class ProtocollazionePEC {
		@Getter
		@Setter
		String numeroPG, numeroFascicolo, titolo, rubrica, sezione, tipologiadocumento, oggetto, provenienza, utenteprotocollazione, numeroRegistro;
		@Setter
		@Getter
		Integer annoPG, annoRegistro, annoFascicolo;
		@Getter
		@Setter
		Date dataprotocollazione;
		@Setter
		@Getter
		TipoProtocollazione tipoProtocollazione;
		@Setter
		@Getter
		PG capofila;
	}

	public static class ProtocollazionePECBuilder {
		@Setter
		String numeroPG, numeroFascicolo, titolo, rubrica, sezione, tipologiadocumento, oggetto, provenienza, utenteprotocollazione, numeroRegistro;
		@Setter
		Integer annoPG, annoRegistro, annoFascicolo;
		@Setter
		PG capofila;
		@Setter
		Date dataprotocollazione;
		@Setter
		TipoProtocollazione tipoProtocollazione;

		public ProtocollazionePEC construct() {
			return new ProtocollazionePEC(numeroPG, numeroFascicolo, titolo, rubrica, sezione, tipologiadocumento, oggetto, provenienza, utenteprotocollazione, numeroRegistro, annoPG, annoRegistro,
					annoFascicolo, dataprotocollazione, tipoProtocollazione, capofila);
		}
	}

	public static class PG {
		@Setter
		@Getter
		String numeroPG;
		@Setter
		@Getter
		Integer annoPG;
	}

	public static class Interoperabile {

		@Getter
		@Setter
		String stato;
		@Getter
		@Setter
		String oggettoMessaggio;
		@Getter
		@Setter
		String provenienzaMessaggio;
		@Getter
		@Setter
		String codiceAmministrazione;
		@Getter
		@Setter
		String codiceAOO;
		@Getter
		@Setter
		String codiceRegistro;
		@Getter
		@Setter
		String numeroRegistrazione;
		@Getter
		@Setter
		Date dataRegistrazione;
		@Getter
		@Setter
		String nomeDocumento;
		@Getter
		@Setter
		String oggettoDocumento;
		@Getter
		@Setter
		String titoloDocumento;
		@Getter
		@Setter
		TipologiaSegnatura tipologiaSegnatura;
		@Getter
		@Setter
		String nomeAllegatoPrincipale;
		@Getter
		@Setter
		DestinatarioInteroperabile risposta;
		@Getter
		List<DestinatarioInteroperabile> destinatari = new ArrayList<DestinatarioInteroperabile>();
		@Getter
		List<DestinatarioInteroperabile> copiaConoscenza = new ArrayList<DestinatarioInteroperabile>();
	}

	@AllArgsConstructor
	@ToString
	public static class DestinatarioInteroperabile {

		@Getter
		@Setter
		String email;
		@Getter
		@Setter
		boolean confermaRicezione = false;
		@Getter
		@Setter
		boolean confermata = false;
		@Getter
		@Setter
		boolean aggiornata = false;
		@Getter
		@Setter
		boolean annullata = false;

		public DestinatarioInteroperabile(String email) {
			this.email = email;
		}
	}

	public static enum TipologiaSegnatura {
		EMAIL, ALLEGATI
	}

	@ToString
	@EqualsAndHashCode
	public static class Ricevuta {

		@Getter
		@Setter
		TipoRicevuta tipo;
		@Getter
		@Setter
		ErroreRicevuta errore;
		// intestazione
		@Getter
		@Setter
		String mittente;
		@Getter
		List<DestinatarioRicevuta> destinatari = new ArrayList<DestinatarioRicevuta>(); // uno o più
		@Getter
		@Setter
		String risposte;
		@Getter
		@Setter
		String oggetto; // zero o uno
		// dati
		@Getter
		@Setter
		String gestoreEmittente;
		@Getter
		@Setter
		DataPec data;
		@Getter
		@Setter
		String identificativo;
		@Getter
		@Setter
		String msgid; // zero o uno
		@Getter
		@Setter
		TipoConsegna tipoConsegna; // zero o uno
		@Getter
		@Setter
		String consegna; // zero o uno
		@Getter
		@Setter
		String erroreEsteso; // zero o uno
	}

	@AllArgsConstructor
	@ToString
	public static class DestinatarioRicevuta {

		@Getter
		@Setter
		String email, tipo;

	}

	/**
	 * Corrisponde a quei valori dell'header X-Ricevuta che possono essere contenuti nelle ricevute destinate al mittente (sono esclusi i valori riservati alla comunicazione fra i gestori pec)
	 *
	 * @author fmattiazzo
	 *
	 */
	public enum TipoRicevuta {

		ACCETTAZIONE("accettazione"), //
		NON_ACCETTAZIONE("non-accettazione"), //
		PREAVVISO_ERRORE_CONSEGNA("preavviso-errore-consegna"), //
		AVVENUTA_CONSEGNA("avvenuta-consegna"), //
		ERRORE_CONSEGNA("errore-consegna");

		private final String label;

		private TipoRicevuta(String label) {
			this.label = label;
		}

		public String getLabel() {
			return label;
		}

		public static TipoRicevuta fromLabel(String value) throws PraticaException {
			for (TipoRicevuta r : TipoRicevuta.values()) {
				if (r.getLabel().equalsIgnoreCase(value)) {
					return r;
				}
			}
			if ("presa-in-carico".equalsIgnoreCase(value) || "rilevazione-virus".equalsIgnoreCase(value)) {
				throw new PraticaException("Tipo di ricevuta non supportata: " + value);
			}
			throw new IllegalArgumentException(value);
		}
	}

	public enum ErroreRicevuta {

		NESSUNO("nessuno"), //
		NO_DEST("no-dest"), //
		NO_DOMINIO("no-dominio"), //
		VIRUS("virus"), //
		ALTRO("altro");

		private final String label;

		private ErroreRicevuta(String label) {
			this.label = label;
		}

		public String getLabel() {
			return label;
		}

		public static ErroreRicevuta fromLabel(String value) {
			for (ErroreRicevuta r : ErroreRicevuta.values()) {
				if (r.getLabel().equalsIgnoreCase(value)) {
					return r;
				}
			}

			return null;
		}
	}

	@ToString
	@EqualsAndHashCode
	public static class DataPec {

		@Getter
		@Setter
		String zona;
		@Getter
		@Setter
		String giorno;
		@Getter
		@Setter
		String ora;
	}

	public enum TipoConsegna {

		COMPLETA("completa"), BREVE("breve"), SINTETICA("sintetica");

		private final String label;

		private TipoConsegna(String label) {
			this.label = label;
		}

		public String getLabel() {
			return label;
		}

		public static TipoConsegna fromLabel(String value) {
			for (TipoConsegna r : TipoConsegna.values()) {
				if (r.getLabel().equalsIgnoreCase(value)) {
					return r;
				}
			}
			return null;
		}
	}

	public static class Builder {
		@Setter
		String messageID, folderPath, consoleFileName, mittente, oggetto, body, firma, titolo, idDocumentale, utenteCreazione, usernameCreazione;
		@Setter
		Integer idEmailServer;
		@Setter
		Date dataRicezione, dataCreazione;
		@Setter
		TipoEmail tipoEmail;
		@Setter
		Destinatario destinatarioPrincipale;
		@Setter
		Destinatario[] destinatari;
		@Setter
		Destinatario[] destinatariCC;
		@Setter
		String[] destinatariInoltro;
		@Setter
		BigInteger progressivoInoltro;
		@Setter
		ProtocollazionePEC protocollazionePEC;
		@Setter
		Utente inCaricoA;
		@Setter
		private String messageIDReinoltro;
		@Setter
		private List<GruppoVisibilita> gruppiVisibilita = new ArrayList<GruppoVisibilita>();
		@Setter
		Interoperabile interoperabile;
		@Setter
		String note;
		@Setter
		String replyTo;
		@Setter
		TipologiaPratica tipologiaPratica;

		public Builder() {

		}

		public DatiEmail construct() {
			return new DatiEmail(messageID, folderPath, consoleFileName, dataRicezione, mittente, oggetto, destinatarioPrincipale, body, firma, tipoEmail, dataCreazione, destinatari, destinatariCC,
					destinatariInoltro, progressivoInoltro, protocollazionePEC, titolo, idDocumentale, utenteCreazione, usernameCreazione, inCaricoA, gruppiVisibilita, messageIDReinoltro,
					interoperabile, note, replyTo, idEmailServer);
		}
	}
}
