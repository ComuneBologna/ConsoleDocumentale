package it.eng.consolepec.xmlplugin.pratica.fascicolo;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaFascicolo;
import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo;
import it.eng.consolepec.xmlplugin.factory.DatiPratica;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DatiFascicolo extends DatiPratica {
	@Getter
	@Setter(value = AccessLevel.PROTECTED)
	DatiFascicolo.Stato stato;
	@Getter
	@Setter
	String note;
	@Getter
	@Setter
	List<ProtocollazioneCapofila> protocollazioniCapofila = new ArrayList<ProtocollazioneCapofila>();
	@Getter
	TreeSet<Collegamento> collegamenti = new TreeSet<DatiFascicolo.Collegamento>();
	@Getter
	TreeSet<Operazione> operazioni = new TreeSet<DatiFascicolo.Operazione>();
	@Getter
	TreeSet<Procedimento> procedimenti = new TreeSet<DatiFascicolo.Procedimento>();
	@Getter
	@Setter
	StepIter stepIter;
	@Getter
	@Setter
	String titoloOriginale;
	@Getter
	List<String> idPraticheProcedi = new ArrayList<String>();
	@Getter
	@Setter
	AnagraficaFascicolo anagraficaFascicolo;

	public DatiFascicolo(TipologiaPratica tipologiaPratica, String folderPath, String consoleFileName, String provenienza, String titolo, String numeroPG, Date dataCreazione, Integer annoPG,
			String note, String idDocumentale, String utenteCreazione, String usernameCreazione, Utente inCaricoA, List<GruppoVisibilita> gruppiVisibilita, List<DatoAggiuntivo> datiAggiuntivi) {
		setTipo(tipologiaPratica);
		setFolderPath(folderPath);
		setConsoleFileName(consoleFileName);
		setProvenienza(provenienza);
		setTitolo(titolo);
		setDataCreazione(dataCreazione);
		setNote(note);
		setIdDocumentale(idDocumentale);
		setUtenteCreazione(utenteCreazione);
		setUsernameCreazione(usernameCreazione);
		stato = DatiFascicolo.Stato.IN_GESTIONE;
		setGruppiVisibilita(new TreeSet<DatiPratica.GruppoVisibilita>(gruppiVisibilita));
		setInCaricoA(inCaricoA);
		setDatiAggiuntivi(datiAggiuntivi);
		initOperazioni();
		initOperazioniSuperUtente();
		initOperazioniAssegnaUtenteEsterno();
	}

	protected void initOperazioni() {
		getOperazioni().add(new Operazione(TipoApiTask.AGGIUNGI_ALLEGATO.name(), true));
		getOperazioni().add(new Operazione(TipoApiTask.AGGIUNGI_PROTOCOLLAZIONE_BA01.name(), true));
		getOperazioni().add(new Operazione(TipoApiTask.AVVIA_PROCEDIMENTO.name(), true));
		getOperazioni().add(new Operazione(TipoApiTask.CHIUDI_PROCEDIMENTO.name(), true));
		getOperazioni().add(new Operazione(TipoApiTask.COLLEGA_FASCICOLO.name(), true));
		getOperazioni().add(new Operazione(TipoApiTask.CONDIVIDI_FASCICOLO.name(), true));
		getOperazioni().add(new Operazione(TipoApiTask.ELIMINA_COLLEGAMENTO_FASCICOLO.name(), true));
		getOperazioni().add(new Operazione(TipoApiTask.ELIMINA_CONDIVISIONE_FASCICOLO.name(), true));
		getOperazioni().add(new Operazione(TipoApiTask.FIRMA.name(), true));
		getOperazioni().add(new Operazione(TipoApiTask.METTI_IN_AFFISSIONE.name(), true));
		getOperazioni().add(new Operazione(TipoApiTask.PROTOCOLLAZIONE.name(), true));
		getOperazioni().add(new Operazione(TipoApiTask.PUBBLICA.name(), true));
		getOperazioni().add(new Operazione(TipoApiTask.RIMUOVI_ALLEGATO.name(), true));
		getOperazioni().add(new Operazione(TipoApiTask.RIMUOVI_PUBBLICAZIONE.name(), true));
		getOperazioni().add(new Operazione(TipoApiTask.RISPONDI_MAIL.name(), true));
		getOperazioni().add(new Operazione(TipoApiTask.ASSEGNA_UTENTE_ESTERNO.name(), true));
		getOperazioni().add(new Operazione(TipoApiTask.CONCLUDI_FASCICOLO.name(), true));
	}

	protected void initOperazioniSuperUtente() {
		getOperazioniSuperUtente().add(new Operazione(TipoApiTask.RIASSEGNA.name(), false));
		getOperazioniSuperUtente().add(new Operazione(TipoApiTask.SGANCIA_PEC_IN.name(), false));
		getOperazioniSuperUtente().add(new Operazione(TipoApiTask.CAMBIA_TIPO_FASCICOLO.name(), true)); // true = solo per superutenti
		getOperazioniSuperUtente().add(new Operazione(TipoApiTask.AGGANCIA_PRATICA_A_FASCICOLO.name(), false));
		getOperazioniSuperUtente().add(new Operazione(TipoApiTask.PROTOCOLLAZIONE.name(), false));
		getOperazioniSuperUtente().add(new Operazione(TipoApiTask.AGGIUNGI_PROTOCOLLAZIONE_BA01.name(), false));
		getOperazioniSuperUtente().add(new Operazione(TipoApiTask.RIPORTA_IN_LETTURA.name(), false));
		getOperazioniSuperUtente().add(new Operazione(TipoApiTask.RIPORTA_IN_GESTIONE.name(), false));
	}

	protected void initOperazioniAssegnaUtenteEsterno() {
		getOperazioniAssegnaUtenteEsterno().add(new Operazione(TipoApiTask.AGGIUNGI_ALLEGATO.name()));
		getOperazioniAssegnaUtenteEsterno().add(new Operazione(TipoApiTask.FIRMA.name()));
		getOperazioniAssegnaUtenteEsterno().add(new Operazione(TipoApiTask.RISPONDI_MAIL.name()));
		getOperazioniAssegnaUtenteEsterno().add(new Operazione(TipoApiTask.AVVIA_PROCEDIMENTO.name()));
		getOperazioniAssegnaUtenteEsterno().add(new Operazione(TipoApiTask.CHIUDI_PROCEDIMENTO.name()));
		getOperazioniAssegnaUtenteEsterno().add(new Operazione(TipoApiTask.CONCLUDI_FASCICOLO.name()));
		getOperazioniAssegnaUtenteEsterno().add(new Operazione(TipoApiTask.ASSEGNA_UTENTE_ESTERNO.name()));
	}

	@Override
	public void setTitolo(String titolo) {
		super.setTitolo(titolo);
	}

	@ToString
	@AllArgsConstructor
	@EqualsAndHashCode
	public class Protocollazione implements Comparable<Protocollazione> {
		@Getter
		@Setter
		String numeroPG, numeroFascicolo, titolo, rubrica, sezione, tipologiadocumento, oggetto, provenienza, utenteprotocollazione, numeroRegistro, note, dataArrivo, oraArrivo;
		@Getter
		@Setter
		Integer annoPG, annoRegistro, annoFascicolo;
		@Getter
		@Setter
		Date dataprotocollazione;
		@Getter
		@Setter
		TipoProtocollazione tipoProtocollazione;
		@Getter
		@Setter
		List<PraticaCollegata> praticheCollegateProtocollate = new ArrayList<DatiPratica.PraticaCollegata>();
		@Getter
		@Setter
		List<Allegato> allegatiProtocollati = new ArrayList<Allegato>();

		@Override
		public int compareTo(Protocollazione protocollazione) {
			if (protocollazione.getAnnoPG().compareTo(annoPG) == 0) {
				return new Integer(protocollazione.getNumeroPG()).compareTo(new Integer(numeroPG));
			}
			return protocollazione.getAnnoPG().compareTo(annoPG);
		}

		public Protocollazione clona() {

			List<Allegato> allegati = new ArrayList<Allegato>();
			for (Allegato a : this.allegatiProtocollati) {
				allegati.add(a.clona());
			}

			List<PraticaCollegata> praticheCollProto = new ArrayList<DatiPratica.PraticaCollegata>();
			for (PraticaCollegata pc : this.praticheCollegateProtocollate) {
				praticheCollProto.add(pc.clona());
			}

			return new Protocollazione(numeroPG, numeroFascicolo, titolo, rubrica, sezione, tipologiadocumento, oggetto, provenienza, utenteprotocollazione, numeroRegistro, note, dataArrivo,
					oraArrivo, annoPG, annoRegistro, annoFascicolo, dataprotocollazione, tipoProtocollazione, praticheCollProto, allegati);
		}
	}

	@ToString
	@AllArgsConstructor
	@EqualsAndHashCode
	public static class Collegamento implements Comparable<Collegamento> {
		@Getter
		@Setter(AccessLevel.PROTECTED)
		private String nomeGruppo, path;
		@Getter
		@Setter(AccessLevel.PROTECTED)
		private Date dataCollegamento;
		@Getter
		@Setter
		private Boolean accessibileInLettura;
		@Getter
		@Setter(AccessLevel.PROTECTED)
		private TreeSet<Operazione> operazioniConsentite = new TreeSet<DatiFascicolo.Operazione>();

		public Collegamento(String nomeGruppo, String path, Date dataCollegamento, TreeSet<Operazione> operazioniConsentite) {
			this.path = path;
			this.nomeGruppo = nomeGruppo;
			this.dataCollegamento = dataCollegamento;
			this.operazioniConsentite = operazioniConsentite;
			this.accessibileInLettura = true;
		}

		@Override
		public int compareTo(Collegamento o) {
			int result = path.compareTo(o.path);
			if (result == 0) {
				result = operazioniConsentite.size() - o.operazioniConsentite.size();
			}
			if (result == 0) {
				Iterator<Operazione> i1 = operazioniConsentite.iterator();
				Iterator<Operazione> i2 = operazioniConsentite.iterator();
				while (i1.hasNext()) {
					result = i1.next().compareTo(i2.next());
					if (result != 0) {
						break;
					}
				}
			}
			return result;
		}

	}

	@ToString
	@AllArgsConstructor
	@EqualsAndHashCode
	public class ProtocollazioneCapofila implements Comparable<ProtocollazioneCapofila> {
		@Getter
		@Setter
		String numeroPG, numeroFascicolo, titolo, rubrica, sezione, tipologiadocumento, oggetto, provenienza, utenteprotocollazione, numeroRegistro, note, dataArrivo, oraArrivo;
		@Getter
		@Setter(value = AccessLevel.PROTECTED)
		Integer annoPG, annoRegistro, annoFascicolo;
		@Getter
		@Setter
		Date dataprotocollazione;
		@Getter
		@Setter(value = AccessLevel.PROTECTED)
		TipoProtocollazione tipoProtocollazione;
		@Getter
		@Setter(value = AccessLevel.PROTECTED)
		List<PraticaCollegata> praticheCollegateProtocollate = new ArrayList<DatiPratica.PraticaCollegata>();
		@Getter
		@Setter(value = AccessLevel.PROTECTED)
		List<Allegato> allegatiProtocollati = new ArrayList<Allegato>();
		@Getter
		@Setter(value = AccessLevel.PROTECTED)
		List<Protocollazione> protocollazioniCollegate = new ArrayList<Protocollazione>();
		@Getter
		@Setter(value = AccessLevel.PROTECTED)
		boolean fromBa01;

		@Override
		public int compareTo(ProtocollazioneCapofila capofila) {
			if (capofila.getAnnoPG().compareTo(annoPG) == 0) {
				return new Integer(capofila.getNumeroPG()).compareTo(new Integer(numeroPG));
			}
			return capofila.getAnnoPG().compareTo(annoPG);
		}

		public ProtocollazioneCapofila clona() {

			List<Allegato> allegati = new ArrayList<Allegato>();
			for (Allegato a : this.allegatiProtocollati) {
				allegati.add(a.clona());
			}

			List<Protocollazione> protoCollegate = new ArrayList<Protocollazione>();
			for (Protocollazione proto : this.protocollazioniCollegate) {
				protoCollegate.add(proto.clona());
			}

			List<PraticaCollegata> praticheCollProto = new ArrayList<DatiPratica.PraticaCollegata>();
			for (PraticaCollegata pc : this.praticheCollegateProtocollate) {
				praticheCollProto.add(pc.clona());
			}

			return new ProtocollazioneCapofila(numeroPG, numeroFascicolo, titolo, rubrica, sezione, tipologiadocumento, oggetto, provenienza, utenteprotocollazione, numeroRegistro, note, dataArrivo,
					oraArrivo, annoPG, annoRegistro, annoFascicolo, dataprotocollazione, tipoProtocollazione, praticheCollProto, allegati, protoCollegate, fromBa01);
		}
	}

	public enum Stato {
		IN_GESTIONE("In gestione"), ARCHIVIATO("Archiviato"), IN_AFFISSIONE("In affissione"), IN_VISIONE("In visione"), ANNULLATO("Annullato"), DA_INOLTRARE_ESTERNO("Assegnato via mail");

		private String label;

		Stato(String label) {
			this.label = label;
		}

		public String getLabel() {
			return label;
		}
	}

	public static class ProtocollazioneBuilder {

		private final DatiFascicolo datiFascicolo;

		@Setter
		String numeroPG, numeroFascicolo, titolo, rubrica, sezione, tipologiadocumento, oggetto, provenienza, utenteprotocollazione, numeroRegistro, note, dataArrivo, oraArrivo;
		@Setter
		Integer annoPG, annoRegistro, annoFascicolo;
		@Setter
		Date dataprotocollazione;
		@Setter
		TipoProtocollazione tipoProtocollazione;
		@Setter
		List<PraticaCollegata> praticheCollegateProtocollate = new ArrayList<DatiPratica.PraticaCollegata>();
		@Setter
		List<Allegato> allegatiProtocollati = new ArrayList<Allegato>();

		public ProtocollazioneBuilder(DatiFascicolo datiFascicolo) {
			this.datiFascicolo = datiFascicolo;
		}

		public Protocollazione construct() {
			return datiFascicolo.new Protocollazione(numeroPG, numeroFascicolo, titolo, rubrica, sezione, tipologiadocumento, oggetto, provenienza, utenteprotocollazione, numeroRegistro, note,
					dataArrivo, oraArrivo, annoPG, annoRegistro, annoFascicolo, dataprotocollazione, tipoProtocollazione, praticheCollegateProtocollate, allegatiProtocollati);
		}
	}

	public static class ProtocollazioneCapofilaBuilder {

		private final DatiFascicolo datiFascicolo;

		@Setter
		String numeroPG, numeroFascicolo, titolo, rubrica, sezione, tipologiadocumento, oggetto, provenienza, utenteprotocollazione, numeroRegistro, note, dataArrivo, oraArrivo;
		@Setter
		Integer annoPG, annoRegistro, annoFascicolo;
		@Setter
		Date dataprotocollazione;
		@Setter
		TipoProtocollazione tipoProtocollazione;
		@Setter
		List<PraticaCollegata> praticheCollegateProtocollate = new ArrayList<DatiPratica.PraticaCollegata>();
		@Setter
		List<Allegato> allegatiProtocollati = new ArrayList<Allegato>();
		@Setter
		List<Protocollazione> protocollazioniCollegate = new ArrayList<Protocollazione>();
		@Setter
		boolean fromBa01 = false;

		public ProtocollazioneCapofilaBuilder(DatiFascicolo datiFascicolo) {
			this.datiFascicolo = datiFascicolo;
		}

		public ProtocollazioneCapofila construct() {
			return datiFascicolo.new ProtocollazioneCapofila(numeroPG, numeroFascicolo, titolo, rubrica, sezione, tipologiadocumento, oggetto, provenienza, utenteprotocollazione, numeroRegistro, note,
					dataArrivo, oraArrivo, annoPG, annoRegistro, annoFascicolo, dataprotocollazione, tipoProtocollazione, praticheCollegateProtocollate, allegatiProtocollati, protocollazioniCollegate,
					fromBa01);
		}
	}

	public static class Builder {

		@Setter
		TipologiaPratica tipologiaPratica;
		@Setter
		String folderPath, consoleFileName, provenienza, titolo, numeroPG, note, idDocumentale, utenteCreazione, usernameCreazione;
		@Setter
		Utente inCaricoA;
		@Setter
		Date dataCreazione;
		@Setter
		Integer annoPG;
		@Setter
		List<GruppoVisibilita> gruppiVisibilita = new ArrayList<DatiPratica.GruppoVisibilita>();
		@Setter
		List<DatoAggiuntivo> datiAggiuntivi = new ArrayList<>();

		public Builder() {}

		public DatiFascicolo construct() {
			return new DatiFascicolo(tipologiaPratica, folderPath, consoleFileName, provenienza, titolo, numeroPG, dataCreazione, annoPG, note, idDocumentale, utenteCreazione, usernameCreazione,
					inCaricoA, gruppiVisibilita, datiAggiuntivi);
		}

	}

	@ToString
	@AllArgsConstructor
	@EqualsAndHashCode
	public static class Procedimento implements Comparable<Procedimento> {

		@Getter
		@Setter
		String codUtente, modAvvioProcedimento, flagInterruzione, numeroPG, responsabile, provenienza, modalitaChiusura, numeroPGChiusura;
		@Getter
		@Setter
		Integer codTipologiaProcedimento, codUnitaOrgResponsabile, codUnitaOrgCompetenza, durata, codQuartiere, annoPG, termine, annoPGChiusura;
		@Getter
		@Setter
		Date dataInizioDecorrenzaProcedimento, dataChiusura;

		@Override
		public int compareTo(Procedimento o) {
			int res = (getAnnoPG() != null) ? getAnnoPG().compareTo(o.getAnnoPG()) : 0;
			if (res == 0 && getNumeroPG() != null) {
				res += getNumeroPG().compareTo(o.getNumeroPG());
			}
			if (res == 0 && codTipologiaProcedimento != null) {
				res += (codTipologiaProcedimento - o.getCodTipologiaProcedimento());
			}
			return res;
		}

		public Procedimento() {
			// TODO Auto-generated constructor stub
		}
	}

	@ToString
	@AllArgsConstructor
	@EqualsAndHashCode
	public static class StepIter implements Comparable<StepIter> {

		@Getter
		@Setter
		String nome;
		@Getter
		@Setter
		Boolean finale;
		@Getter
		@Setter
		Boolean iniziale;
		@Getter
		@Setter
		Boolean creaBozza = false;
		List<String> destinatariNotifica = new ArrayList<String>();
		@Getter
		@Setter
		Date dataAggiornamento;

		@Override
		public int compareTo(StepIter o) {
			return nome.compareTo(o.nome);
		}

		public List<String> getDestinatariNotifica() {
			if (destinatariNotifica == null) {
				destinatariNotifica = new ArrayList<String>();
			}
			return destinatariNotifica;
		}
	}

}
