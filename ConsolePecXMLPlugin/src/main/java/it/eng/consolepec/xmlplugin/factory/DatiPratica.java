package it.eng.consolepec.xmlplugin.factory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.commons.pratica.OperativitaRidotta;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.DatiApprovazioneFirmaTask.DestinatarioRichiestaApprovazioneFirmaTask;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.DatiApprovazioneFirmaTask.StatoRichiestaApprovazioneFirmaTask;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.DatiApprovazioneFirmaTask.TipoPropostaApprovazioneFirmaTask;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class DatiPratica {
	@Getter @Setter(value = AccessLevel.PROTECTED) String folderPath, consoleFileName, provenienza, titolo, utenteCreazione, usernameCreazione;
	@Getter @Setter(value = AccessLevel.PROTECTED) Date dataCreazione;
	@Getter @NonNull @Setter(value = AccessLevel.PROTECTED) TipologiaPratica tipo;
	@Getter @Setter(value = AccessLevel.PROTECTED) String idDocumentale;
	@Getter @Setter(value = AccessLevel.PROTECTED) int _version;
	@Getter @Setter(value = AccessLevel.PROTECTED) boolean _sync;
	@Getter @Setter private boolean letto;
	@Getter @Setter Utente inCaricoA;
	@Getter private final TreeSet<Allegato> allegati = new TreeSet<DatiEmail.Allegato>();
	@Getter @Setter(value = AccessLevel.PROTECTED) private TreeSet<GruppoVisibilita> gruppiVisibilita = new TreeSet<GruppoVisibilita>();
	@Getter @Setter Set<EventoIterPratica> iter = new TreeSet<EventoIterPratica>();
	@Getter @Setter String note;
	@Getter TreeSet<Operazione> operazioniSuperUtente = new TreeSet<DatiFascicolo.Operazione>();
	@Getter TreeSet<Operazione> operazioniAssegnaUtenteEsterno = new TreeSet<Operazione>();
	@Getter List<PraticaCollegata> praticheCollegate = new ArrayList<DatiPratica.PraticaCollegata>();
	@Getter @Setter AssegnazioneEsterna assegnazioneEsterna;
	@Getter List<Notifica> notifiche = new ArrayList<Notifica>();
	@Getter @Setter List<DatoAggiuntivo> datiAggiuntivi = new ArrayList<>();
	@Getter Operatore operatore = new Operatore();
	@Getter @Setter OperativitaRidotta operativitaRidotta;
	@Getter @Setter String assegnatarioCorrente;

	public enum TipoProtocollazione {
		ENTRATA, USCITA, INTERNA;
	}

	public enum TipoFirma {
		PADES, CADES, XADES;

		public static TipoFirma translate(String value) {
			if (value == null) return null;
			return valueOf(value);
		}
	}

	@ToString
	@EqualsAndHashCode
	@AllArgsConstructor
	public static class Utente {
		@Getter @NonNull String username;
		@Getter String nome, cognome, matricola, codicefiscale;
		@Getter @Setter Date dataPresaInCarico;
	}

	@ToString
	@EqualsAndHashCode
	public static class Operatore {
		@Getter @Setter String nome;
	}

	@ToString
	@EqualsAndHashCode
	public class Allegato implements Comparable<Allegato> {
		@Getter @NonNull String nome, label, folderRelativePath;
		@Getter String folderOriginPath, folderOriginName;
		@Getter @NonNull Long dimensioneByte;
		@Getter @Setter String currentVersion;
		@Getter List<Versione> versioni = new ArrayList<Versione>();
		@Getter TipoFirma tipoFirma;
		@NonNull @Getter @Setter Boolean firmato;
		@NonNull @Getter @Setter Boolean firmatoHash;
		@Getter @Setter Date dataInizioPubblicazione;
		@Getter @Setter Date dataFinePubblicazione;
		@Getter @Setter private TreeSet<GruppoVisibilita> gruppiVisibilita = new TreeSet<GruppoVisibilita>();
		@Getter @Setter Date dataCaricamento;
		@Getter List<String> tipologiaDocumento = new ArrayList<>();
		@Getter @Setter Boolean lock;
		@Getter @Setter Integer lockedBy;
		@Getter @Setter String oggettoDocumento;
		@Getter TreeSet<StoricoVersioni> storicoVersioni = new TreeSet<StoricoVersioni>();
		@Getter @Setter TreeSet<CampoModificabile> campiModificabili = new TreeSet<CampoModificabile>();
		@Getter @Setter Boolean versionable = true;
		@Getter List<DatoAggiuntivo> datiAggiuntivi = new ArrayList<>();
		@Getter @Setter String hash;

		public Allegato(String nome, String label, String folderRelativePath) {
			this(nome, label, folderRelativePath, null, null, 0L, null, false, null, false, null, null, null, null, null, null);
		}

		public Allegato(String nome, String label, String folderRelativePath, String version) {
			this(nome, label, folderRelativePath, null, null, 0L, version, false, null, false, null, null, null, null, null, null);
		}

		public Allegato(String nome, String label, String folderRelativePath, Long dimensioneByte, String currentVersion, boolean firmato, TipoFirma tipoFirma, boolean firmatoHash,
				Date dataInizioPubblicazione, Date dataFinePubblicazione, Date dataCaricamento) {
			this(nome, label, folderRelativePath, null, null, dimensioneByte, currentVersion, firmato, tipoFirma, firmatoHash, dataInizioPubblicazione, dataFinePubblicazione, dataCaricamento, null,
					null, null);
		}

		public Allegato(String nome, String label, String folderRelativePath, Long dimensioneByte, String currentVersion, boolean firmato, TipoFirma tipoFirma, boolean firmatoHash,
				Date dataInizioPubblicazione, Date dataFinePubblicazione, Date dataCaricamento, String oggettoDocumento, Boolean lock, Integer lockedBy) {
			this(nome, label, folderRelativePath, null, null, dimensioneByte, currentVersion, firmato, tipoFirma, firmatoHash, dataInizioPubblicazione, dataFinePubblicazione, dataCaricamento,
					oggettoDocumento, lock, lockedBy);
		}

		public Allegato(String nome, String label, String folderRelativePath, String folderOriginPath, String folderOriginName, Long dimensioneByte, String currentVersion, boolean firmato,
				TipoFirma tipoFirma, boolean firmatoHash, Date dataInizioPubblicazione, Date dataFinePubblicazione, Date dataCaricamento, String oggettoDocumento, Boolean lock, Integer lockedBy) {
			super();
			this.nome = nome;
			this.label = label;
			this.folderRelativePath = folderRelativePath;
			this.folderOriginPath = folderOriginPath;
			this.folderOriginName = folderOriginName;
			this.dimensioneByte = dimensioneByte;
			this.currentVersion = currentVersion;
			this.firmato = firmato;
			this.tipoFirma = tipoFirma;
			this.dataInizioPubblicazione = dataInizioPubblicazione;
			this.dataFinePubblicazione = dataFinePubblicazione;
			this.firmatoHash = firmatoHash;
			this.dataCaricamento = dataCaricamento;
			this.oggettoDocumento = oggettoDocumento;
			this.lock = lock;
			this.lockedBy = lockedBy;
		}

		@Override
		public int compareTo(Allegato o) {
			return toString().compareTo(o.toString());
		}

		public Allegato clona() {
			Allegato allegato = new Allegato(this.nome, this.label, this.folderRelativePath, this.folderOriginPath, this.folderOriginName, this.dimensioneByte, this.currentVersion, this.firmato,
					this.tipoFirma, this.firmatoHash, this.dataInizioPubblicazione, this.dataFinePubblicazione, this.dataCaricamento, this.oggettoDocumento, this.lock, this.lockedBy);
			allegato.setHash(this.hash);
			allegato.getDatiAggiuntivi().addAll(this.datiAggiuntivi);
			allegato.getCampiModificabili().addAll(this.campiModificabili);
			allegato.getStoricoVersioni().addAll(this.storicoVersioni);
			allegato.getTipologiaDocumento().addAll(this.tipologiaDocumento);
			allegato.getGruppiVisibilita().addAll(this.gruppiVisibilita);
			return allegato;
		}
	}

	@ToString
	@AllArgsConstructor
	@EqualsAndHashCode
	public static class Operazione implements Comparable<Operazione> {

		@Getter @Setter(AccessLevel.PROTECTED) private String nomeOperazione;
		@Getter @Setter(AccessLevel.PROTECTED) private boolean abilitata;

		@Override
		public int compareTo(Operazione o) {
			return nomeOperazione.compareTo(o.nomeOperazione);
		}

		public Operazione(String nomeOperazione) {
			super();
			this.nomeOperazione = nomeOperazione;
		}

	}

	@ToString
	@AllArgsConstructor
	@EqualsAndHashCode(of = "nomeGruppo")
	public static class GruppoVisibilita implements Comparable<GruppoVisibilita> {
		@Setter @Getter String nomeGruppo;

		@Override
		public int compareTo(GruppoVisibilita ext) {
			return this.nomeGruppo.compareTo(ext.nomeGruppo);
		}
	}

	@ToString
	@EqualsAndHashCode
	public class EventoIterPratica implements Comparable<EventoIterPratica> {
		@Getter @Setter private Integer annpPG;
		@Getter @Setter private String numeroPG;
		@Getter @Setter private String tipoEvento;
		@Getter @Setter private String testoEvento;
		@Getter @Setter private String user;
		@Getter @Setter private boolean isSerialized;
		@Getter @Setter private boolean isSerializationEnabled;
		@Getter @Setter private Date dataEvento = new Date();

		@Override
		public int compareTo(EventoIterPratica eventoIterPratica) {
			int compareDataEvento = dataEvento.compareTo(eventoIterPratica.getDataEvento());
			return compareDataEvento == 0 ? testoEvento.compareTo(eventoIterPratica.testoEvento) : compareDataEvento;
		}
	}

	@ToString
	@AllArgsConstructor
	@EqualsAndHashCode(exclude = { "dataCaricamento" })
	public class PraticaCollegata implements Comparable<PraticaCollegata> {
		@Getter @Setter @NonNull String alfrescoPath;
		@Getter @Setter @NonNull String tipo;
		@Getter @Setter Date dataCaricamento;

		@Override
		public int compareTo(PraticaCollegata o) {
			return tipo.toString().compareTo(o.getTipo().toString()) + alfrescoPath.compareTo(o.getAlfrescoPath());
		}

		public PraticaCollegata clona() {
			return new PraticaCollegata(alfrescoPath, tipo, dataCaricamento);
		}

	}

	@ToString
	@AllArgsConstructor
	@EqualsAndHashCode
	public static class AssegnazioneEsterna {

		@Getter @Setter(AccessLevel.PROTECTED) private TreeSet<Operazione> operazioniConsentite = new TreeSet<DatiFascicolo.Operazione>();

		@Getter @Setter(AccessLevel.PROTECTED) private TreeSet<String> destinatari = new TreeSet<String>();

		@Getter @Setter(AccessLevel.PROTECTED) private String testoMail;

		@Getter @Setter(AccessLevel.PROTECTED) private Date dataNotifica;

	}

	@ToString
	@AllArgsConstructor
	@EqualsAndHashCode
	public static class Notifica {

		@Getter @Setter(AccessLevel.PROTECTED) private String operazione;

		@Getter @Setter(AccessLevel.PROTECTED) private TipologiaNotifica tipologia;

		@Getter @Setter(AccessLevel.PROTECTED) private ParametriExtra parametriExtra;

	}

	/*
	 * parametri extra per la notifica(non obbligatori), specifici per ciascuna implementazione
	 */
	@ToString
	@EqualsAndHashCode
	public static class ParametriExtra {
		@Getter List<String> indirizziEmail = new ArrayList<String>();
		@Getter @Setter boolean salvaEMLNotificaEmail = false;
		@Getter List<Allegato> allegatiDaCaricare = new ArrayList<Allegato>();
		@Getter @Setter boolean cancellaAllegatiCaricati = false;
		@Getter @Setter String mittenteEmail;
		@Getter @Setter Integer idTask;
		@Getter @Setter String operazioneTask;
		@Getter @Setter String noteTask;

	}

	public static enum TipologiaNotifica {
		EMAIL;

		public static TipologiaNotifica fromName(String name) {
			for (TipologiaNotifica t : values()) {
				if (t.name().equals(name)) return t;
			}
			return null;
		}
	}

	@ToString
	@EqualsAndHashCode
	public static class StoricoVersioni implements Comparable<StoricoVersioni> {
		@Getter @Setter private String versione;
		@Getter @Setter private InformazioniTaskFirma informazioniTaskFirma;
		@Getter @Setter private String utente;
		@Getter @Setter private InformazioniCopia informazioniCopia;

		public StoricoVersioni(String versione) {
			this.versione = versione;
		}

		@Data
		@NoArgsConstructor(access = AccessLevel.PRIVATE)
		public static class InformazioniCopia {

			public InformazioniCopia(String idDocumentaleSorgente) {
				this.idDocumentaleSorgente = idDocumentaleSorgente;
			}

			private String idDocumentaleSorgente;
			private Map<String, InformazioniTaskFirma> informazioniTaskFirma = new TreeMap<String, InformazioniTaskFirma>();
		}

		@ToString
		public static class InformazioniTaskFirma {
			@Getter @Setter private String oggetto;
			@Getter @Setter private String proponente;
			@Getter private List<DestinatarioRichiestaApprovazioneFirmaTask> destinatari = new ArrayList<DestinatarioRichiestaApprovazioneFirmaTask>();
			@Getter @Setter private TipoPropostaApprovazioneFirmaTask tipoRichiesta;
			@Getter @Setter private StatoRichiestaApprovazioneFirmaTask operazioneEffettuata;
			@Getter @Setter private StatoRichiestaApprovazioneFirmaTask statoRichiesta;
			@Getter @Setter private Date dataScadenza;
			@Getter @Setter private String mittenteOriginale;
			@Getter @Setter private String motivazione;

			public InformazioniTaskFirma(String oggetto, String proponente, TipoPropostaApprovazioneFirmaTask tipoRichiesta, List<DestinatarioRichiestaApprovazioneFirmaTask> destinatari,
					StatoRichiestaApprovazioneFirmaTask operazioneEffettuata, String mittenteOriginale, Date dataScadenza, StatoRichiestaApprovazioneFirmaTask statoRichiesta, String motivazione) {
				this.oggetto = oggetto;
				this.proponente = proponente;
				this.tipoRichiesta = tipoRichiesta;
				this.destinatari = destinatari;
				this.operazioneEffettuata = operazioneEffettuata;
				this.mittenteOriginale = mittenteOriginale;
				this.dataScadenza = dataScadenza;
				this.statoRichiesta = statoRichiesta;
				this.motivazione = motivazione;
			}
		}

		@Override
		public int compareTo(StoricoVersioni o) {
			return versione.compareTo(o.getVersione());
		}
	}

	@ToString
	@AllArgsConstructor
	@EqualsAndHashCode
	public static class CampoModificabile implements Comparable<CampoModificabile> {
		@Getter @Setter String nome;
		@Getter @Setter String valore;
		@Getter @Setter boolean abilitato;

		@Override
		public int compareTo(CampoModificabile o) {
			return this.nome.compareTo(o.nome);
		}
	}
}
