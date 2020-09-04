package it.eng.consolepec.xmlplugin.pratica.modulistica;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.consolepec.xmlplugin.factory.DatiPratica;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString(callSuper = true)
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DatiModulistica extends DatiPratica {

	@Getter
	@Setter
	String nome;
	@Getter
	@Setter
	List<NodoModulistica> valori = new ArrayList<NodoModulistica>();
	@Getter
	@Setter
	List<ProtocollazioneCapofila> protocollazioniCapofila = new ArrayList<ProtocollazioneCapofila>();
	@Getter
	@Setter
	Stato stato;
	@Getter
	@Setter(value = AccessLevel.PROTECTED)
	private ProtocollazionePraticaModulistica protocollazionePraticaModulistica;

	public DatiModulistica(String folderPath, String consoleFileName, String provenienza, String titolo, Date dataCreazione, String idDocumentale, String utenteCreazione, String usernameCreazione, Utente inCaricoA, List<GruppoVisibilita> gruppiVisibilita, String nomeModulo,
			List<NodoModulistica> valori) {
		setFolderPath(folderPath);
		setConsoleFileName(consoleFileName);
		setProvenienza(provenienza);
		setTitolo(titolo);
		setDataCreazione(dataCreazione);
		setTipo(TipologiaPratica.PRATICA_MODULISTICA);
		setIdDocumentale(idDocumentale);
		setUtenteCreazione(utenteCreazione);
		setInCaricoA(inCaricoA);
		setUsernameCreazione(usernameCreazione);
		setGruppiVisibilita(new TreeSet<DatiPratica.GruppoVisibilita>(gruppiVisibilita));
		setNome(nomeModulo);
		setValori(valori);
		stato = Stato.IN_GESTIONE;

	}

	public enum Stato {
		IN_GESTIONE("In gestione"), ARCHIVIATA("Archiviato"), IN_AFFISSIONE("In affissione");

		private String label;

		Stato(String label) {
			this.label = label;
		}

		public String getLabel() {
			return label;
		}
	}

	public interface NodoModulistica {
		TipoNodoModulistica getTipoNodo();

		public static enum TipoNodoModulistica {
			VALORE_MODULO, SEZIONE;
		}
	}

	@AllArgsConstructor
	@NoArgsConstructor
	@EqualsAndHashCode
	@ToString
	public static class ValoreModulo implements NodoModulistica {
		@Getter
		@Setter
		String nome;
		@Getter
		@Setter
		String etichetta;
		@Getter
		@Setter
		String valore;
		@Getter
		@Setter
		String descrizione;
		@Getter
		@Setter
		TipoValoreModulo tipo;
		@Getter
		@Setter
		TabellaModulo tabella;
		@Getter
		@Setter
		boolean visibile;
		@Getter
		final TipoNodoModulistica tipoNodo = TipoNodoModulistica.VALORE_MODULO;

		public ValoreModulo(String nome, String etichetta, String valore, String descrizione, boolean visibilita) {
			super();
			this.nome = nome;
			this.valore = valore;
			this.etichetta = etichetta;
			this.descrizione = descrizione;
			this.visibile = visibilita;
			tipo = TipoValoreModulo.Valore;
		}

		public ValoreModulo(String nome, String etichetta, TabellaModulo tabella, boolean visibilita) {
			super();
			this.nome = nome;
			this.etichetta = etichetta;
			this.tabella = tabella;
			this.visibile = visibilita;
			tipo = TipoValoreModulo.Tabella;
		}

		public static enum TipoValoreModulo {
			Valore, Tabella;
		}
	}

	@AllArgsConstructor
	@NoArgsConstructor
	@EqualsAndHashCode
	public static class TabellaModulo {
		@Getter
		List<Riga> righe = new ArrayList<Riga>();

		public TabellaModulo(Riga... righe) {
			super();
			this.righe = java.util.Arrays.asList(righe);
		}

		@AllArgsConstructor
		@NoArgsConstructor
		@EqualsAndHashCode
		public static class Riga {

			@Getter
			List<ValoreModulo> colonne = new ArrayList<ValoreModulo>();

			public Riga(ValoreModulo... colonne) {
				super();
				this.colonne = java.util.Arrays.asList(colonne);
			}

		}
	}

	@EqualsAndHashCode
	@NoArgsConstructor
	public static class Sezione implements NodoModulistica {
		@Getter
		@Setter
		String titolo;
		@Getter
		final TipoNodoModulistica tipoNodo = TipoNodoModulistica.SEZIONE;
		@Getter
		List<NodoModulistica> nodi = new ArrayList<NodoModulistica>();

	}

	public static class Builder {

		@Setter
		String folderPath, consoleFileName, provenienza, titolo, note, idDocumentale, utenteCreazione, usernameCreazione;
		@Setter
		Utente inCaricoA;
		@Setter
		Date dataCreazione;
		@Setter
		List<GruppoVisibilita> gruppiVisibilita = new ArrayList<DatiPratica.GruppoVisibilita>();
		@Setter
		String nome;
		@Setter
		List<NodoModulistica> valori = new ArrayList<NodoModulistica>();

		public Builder() {
		}

		public DatiModulistica construct() {
			return new DatiModulistica(folderPath, consoleFileName, provenienza, titolo, dataCreazione, idDocumentale, utenteCreazione, usernameCreazione, inCaricoA, gruppiVisibilita, nome, valori);
		}

	}

	public static class ProtocollazioneBuilder {

		private final DatiModulistica datiModulistica;

		@Setter
		String numeroPG, numeroPraticaModulistica, titiolo, rubrica, sezione, tipologiadocumento, oggetto, provenienza, utenteprotocollazione, numeroRegistro;
		@Setter
		Integer annoPG, annoRegistro, annoFascicolo;
		@Setter
		Date dataprotocollazione;
		@Setter
		TipoProtocollazione tipoProtocollazione;
		@Setter
		List<PraticaCollegata> praticheCollegateProtocollate = new ArrayList<PraticaCollegata>();
		@Setter
		List<Allegato> allegatiProtocollati = new ArrayList<Allegato>();

		public ProtocollazioneBuilder(DatiModulistica datiModulistica) {
			this.datiModulistica = datiModulistica;
		}

		public Protocollazione construct() {
			return datiModulistica.new Protocollazione(numeroPG, numeroPraticaModulistica, titiolo, rubrica, sezione, tipologiadocumento, oggetto, provenienza, utenteprotocollazione, numeroRegistro, annoPG, annoRegistro, annoFascicolo, dataprotocollazione, tipoProtocollazione, praticheCollegateProtocollate,
					allegatiProtocollati);
		}
	}

	public static class ProtocollazioneCapofilaBuilder {

		private final DatiModulistica datiModulistica;

		@Setter
		String numeroPG, numeroPraticaModulistica, titiolo, rubrica, sezione, tipologiadocumento, oggetto, provenienza, utenteprotocollazione, numeroRegistro;
		@Setter
		Integer annoPG, annoRegistro, annoFascicolo;
		@Setter
		Date dataprotocollazione;
		@Setter
		TipoProtocollazione tipoProtocollazione;
		@Setter
		List<PraticaCollegata> praticheCollegateProtocollate = new ArrayList<PraticaCollegata>();
		@Setter
		List<Allegato> allegatiProtocollati = new ArrayList<Allegato>();
		@Setter
		List<Protocollazione> protocollazioniCollegate = new ArrayList<Protocollazione>();
		@Setter
		boolean fromBa01 = false;

		public ProtocollazioneCapofilaBuilder(DatiModulistica datiModulistica) {
			this.datiModulistica = datiModulistica;
		}

		public ProtocollazioneCapofila construct() {
			return datiModulistica.new ProtocollazioneCapofila(numeroPG, numeroPraticaModulistica, titiolo, rubrica, sezione, tipologiadocumento, oggetto, provenienza, utenteprotocollazione, numeroRegistro, annoPG, annoRegistro, annoFascicolo, dataprotocollazione, tipoProtocollazione,
					praticheCollegateProtocollate, allegatiProtocollati, protocollazioniCollegate, fromBa01);
		}
	}

	@ToString
	@AllArgsConstructor
	@EqualsAndHashCode
	public class Protocollazione implements Comparable<Protocollazione> {
		@Getter
		@Setter
		String numeroPG, numeroPratica, titolo, rubrica, sezione, tipologiadocumento, oggetto, provenienza, utenteprotocollazione, numeroRegistro;
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
		List<PraticaCollegata> praticheCollegateProtocollate = new ArrayList<PraticaCollegata>();
		@Getter
		@Setter
		List<Allegato> allegatiProtocollati = new ArrayList<Allegato>();

		@Override
		public int compareTo(Protocollazione protocollazione) {
			if (annoPG.compareTo(protocollazione.getAnnoPG()) == 0) {
				return new Integer(numeroPG).compareTo(new Integer(protocollazione.getNumeroPG()));
			}
			return annoPG.compareTo(protocollazione.getAnnoPG());
		}
	}

	@ToString
	@AllArgsConstructor
	@EqualsAndHashCode
	public class ProtocollazioneCapofila implements Comparable<ProtocollazioneCapofila> {
		@Getter
		@Setter(value = AccessLevel.PROTECTED)
		String numeroPG, numeroPraticaModulistica, titolo, rubrica, sezione, tipologiadocumento, oggetto, provenienza, utenteprotocollazione, numeroRegistro;
		@Getter
		@Setter(value = AccessLevel.PROTECTED)
		Integer annoPG, annoRegistro, annoFascicolo;
		@Getter
		@Setter(value = AccessLevel.PROTECTED)
		Date dataprotocollazione;
		@Getter
		@Setter(value = AccessLevel.PROTECTED)
		TipoProtocollazione tipoProtocollazione;
		@Getter
		@Setter(value = AccessLevel.PROTECTED)
		List<PraticaCollegata> praticheCollegateProtocollate = new ArrayList<PraticaCollegata>();
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
			if (annoPG.compareTo(capofila.getAnnoPG()) == 0) {
				return new Integer(numeroPG).compareTo(new Integer(capofila.getNumeroPG()));
			}
			return annoPG.compareTo(capofila.getAnnoPG());
		}
	}

	@AllArgsConstructor
	public static class ProtocollazionePraticaModulistica {
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

	public static class ProtocollazionePraticaModulisticaBuilder {
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

		public ProtocollazionePraticaModulistica construct() {
			return new ProtocollazionePraticaModulistica(numeroPG, numeroFascicolo, titolo, rubrica, sezione, tipologiadocumento, oggetto, provenienza, utenteprotocollazione, numeroRegistro, annoPG, annoRegistro, annoFascicolo, dataprotocollazione, tipoProtocollazione, capofila);
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

}
