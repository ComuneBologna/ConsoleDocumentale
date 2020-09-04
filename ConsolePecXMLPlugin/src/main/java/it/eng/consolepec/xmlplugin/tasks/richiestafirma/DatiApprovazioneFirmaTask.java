package it.eng.consolepec.xmlplugin.tasks.richiestafirma;

import it.eng.consolepec.xmlplugin.factory.DatiTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@EqualsAndHashCode(callSuper = true)
public class DatiApprovazioneFirmaTask extends DatiTask {

	@Setter(value = AccessLevel.PRIVATE)
	@Getter
	private Date dataCreazione;
	@Setter(value = AccessLevel.PRIVATE)
	@Getter
	private TreeSet<DestinatarioRichiestaApprovazioneFirmaTask> destinatari = new TreeSet<DestinatarioRichiestaApprovazioneFirmaTask>();
	@Setter(value = AccessLevel.PRIVATE)
	@Getter
	private RiferimentoAllegatoApprovazioneFirmaTask riferimentoAllegato;
	@Setter(value = AccessLevel.PRIVATE)
	@Getter
	TipoPropostaApprovazioneFirmaTask tipo;
	@Getter
	@Setter(value = AccessLevel.PRIVATE)
	private String mittenteOriginale;
	@Getter
	@Setter(value = AccessLevel.PRIVATE)
	private Date dataScadenza;
	@Getter
	@Setter
	private Boolean valido = true;


	public DatiApprovazioneFirmaTask(Assegnatario assegnatario, String stato, Date dataCreazione, TipoPropostaApprovazioneFirmaTask tipo,
			RiferimentoAllegatoApprovazioneFirmaTask riferimentoAllegato, TreeSet<DestinatarioRichiestaApprovazioneFirmaTask> destinatari,
			String mittenteOriginale, Date dataScadenza, Boolean valido) {
		setAssegnatario(assegnatario);
		setTipo(tipo);
		setRiferimentoAllegato(riferimentoAllegato);
		setDestinatari(new TreeSet<DestinatarioRichiestaApprovazioneFirmaTask>(destinatari));
		setStato(stato);
		setDataCreazione(dataCreazione);
		setMittenteOriginale(mittenteOriginale);
		setDataScadenza(dataScadenza);
		setValido(valido);
	}

	@Override
	protected void setId(Integer id){
		super.setId(id);
	}

	@Override
	public Boolean getAttivo() {
		return super.getAttivo();
	}

	@Override
	public void setAttivo(Boolean attivo) {
		super.setAttivo(attivo);
	}

	@Override
	public void setStato(String stato){
		super.setStato(stato);
	}

	@Override
	public String getStato() {
		return super.getStato();
	}

	@ToString
	@AllArgsConstructor
	@EqualsAndHashCode
	public static class RiferimentoAllegatoApprovazioneFirmaTask  implements Comparable<RiferimentoAllegatoApprovazioneFirmaTask>{

		@Getter
		private String nome;
		@Getter
		@Setter
		private String currentVersion;


		@Override
		public int compareTo(RiferimentoAllegatoApprovazioneFirmaTask o) {
			return (nome.compareTo(o.nome) != 0) ? nome.compareTo(o.getNome()) : o.currentVersion.compareTo(o.getCurrentVersion());
		}

	}

	public enum TipoStatoApprovazioneFirmaTask {
		IN_APPROVAZIONE,
		CONCLUSO,
		EVASO;
	}

	public enum TipoPropostaApprovazioneFirmaTask {
	    VISTO,
	    FIRMA,
	    PARERE;

	    public String value() {
	        return name();
	    }

	    public static TipoPropostaApprovazioneFirmaTask fromValue(String v) {
	        return valueOf(v);
	    }
	}

	public enum TipoUtentApprovazioneFirmaTask {
		DESTINATARIO, PROPONENTE;
	}

	public enum StatoRichiestaApprovazioneFirmaTask {
		EVASO(
				TipoStatoApprovazioneFirmaTask.EVASO,
				new ArrayList<TipoPropostaApprovazioneFirmaTask>()),

		IN_APPROVAZIONE(
				TipoStatoApprovazioneFirmaTask.IN_APPROVAZIONE,
				Arrays.asList(TipoPropostaApprovazioneFirmaTask.values())),

		DINIEGATO(
				TipoStatoApprovazioneFirmaTask.CONCLUSO,
				Arrays.asList(TipoPropostaApprovazioneFirmaTask.FIRMA, TipoPropostaApprovazioneFirmaTask.VISTO)),

		RITIRATO(
				TipoStatoApprovazioneFirmaTask.CONCLUSO,
				Arrays.asList(TipoPropostaApprovazioneFirmaTask.values())),

		APPROVATO(
				TipoStatoApprovazioneFirmaTask.CONCLUSO,
				Arrays.asList(TipoPropostaApprovazioneFirmaTask.FIRMA, TipoPropostaApprovazioneFirmaTask.VISTO)),

		PARERE_RICEVUTO(
				TipoStatoApprovazioneFirmaTask.CONCLUSO,
				Arrays.asList(TipoPropostaApprovazioneFirmaTask.PARERE));

		private List<TipoPropostaApprovazioneFirmaTask> tipiProposta;
		private TipoStatoApprovazioneFirmaTask statoFinaleRichiesta;

		private StatoRichiestaApprovazioneFirmaTask(TipoStatoApprovazioneFirmaTask statoFinaleRichiesta, List<TipoPropostaApprovazioneFirmaTask> tipiProposta) {
			this.statoFinaleRichiesta = statoFinaleRichiesta;
			this.tipiProposta = tipiProposta;
		}

		public List<TipoPropostaApprovazioneFirmaTask> getTipiProposta() {
			return tipiProposta;
		}

		public String value() {
	        return name();
	    }

		public TipoStatoApprovazioneFirmaTask getStatoFinaleRichiesta() {
			return statoFinaleRichiesta;
		}

		public static StatoRichiestaApprovazioneFirmaTask fromValue(String v) {
	        return valueOf(v);
	    }

		public static List<StatoRichiestaApprovazioneFirmaTask> fromTipoStato (TipoStatoApprovazioneFirmaTask tipoStato) {
			List<StatoRichiestaApprovazioneFirmaTask> result = new ArrayList<StatoRichiestaApprovazioneFirmaTask>();

			for (StatoRichiestaApprovazioneFirmaTask stato : StatoRichiestaApprovazioneFirmaTask.values()) {
				if(stato.getStatoFinaleRichiesta().equals(tipoStato))
					result.add(stato);
			}

			return result;
		}

		public static List<StatoRichiestaApprovazioneFirmaTask> fromTipoProposta(TipoPropostaApprovazioneFirmaTask tp) {
			List<StatoRichiestaApprovazioneFirmaTask> result = new ArrayList<StatoRichiestaApprovazioneFirmaTask>();

			for (StatoRichiestaApprovazioneFirmaTask stato : StatoRichiestaApprovazioneFirmaTask.values()) {
				if(stato.getTipiProposta().contains(tp))
					result.add(stato);
			}

			return result;
		}

		public static List<StatoRichiestaApprovazioneFirmaTask> fromTipoStatoETipoProposta(TipoStatoApprovazioneFirmaTask tipoStato, TipoPropostaApprovazioneFirmaTask tp) {
			List<StatoRichiestaApprovazioneFirmaTask> result = new ArrayList<StatoRichiestaApprovazioneFirmaTask>();

			for (StatoRichiestaApprovazioneFirmaTask stato : StatoRichiestaApprovazioneFirmaTask.values()) {
				if(stato.getTipiProposta().contains(tp) && stato.getStatoFinaleRichiesta().equals(tipoStato))
					result.add(stato);
			}

			return result;
		}

	}

	public enum StatoDestinatarioRichiestaApprovazioneFirmaTask {
	    IN_APPROVAZIONE(Arrays.asList(TipoPropostaApprovazioneFirmaTask.values())),
	    DINIEGATO(Arrays.asList(TipoPropostaApprovazioneFirmaTask.FIRMA, TipoPropostaApprovazioneFirmaTask.VISTO)),
	    APPROVATO(Arrays.asList(TipoPropostaApprovazioneFirmaTask.FIRMA, TipoPropostaApprovazioneFirmaTask.VISTO)),
	    RISPOSTA_POSITIVA(Arrays.asList(TipoPropostaApprovazioneFirmaTask.PARERE)),
		RISPOSTA_NEGATIVA(Arrays.asList(TipoPropostaApprovazioneFirmaTask.PARERE)),
		RISPOSTA_POSITIVA_CON_PRESCRIZIONI(Arrays.asList(TipoPropostaApprovazioneFirmaTask.PARERE)),
		RISPOSTA_SOSPESA(Arrays.asList(TipoPropostaApprovazioneFirmaTask.PARERE)),
		RISPOSTA_RIFIUTATA(Arrays.asList(TipoPropostaApprovazioneFirmaTask.PARERE));

	    private List<TipoPropostaApprovazioneFirmaTask> tipiProposta;

	    StatoDestinatarioRichiestaApprovazioneFirmaTask(List<TipoPropostaApprovazioneFirmaTask> tipiProposta) {
	    	this.tipiProposta = tipiProposta;
	    }

	    public List<TipoPropostaApprovazioneFirmaTask> getTipiProposta(){
	    	return tipiProposta;
	    }

	    public String value() {
	        return name();
	    }

	    public static StatoDestinatarioRichiestaApprovazioneFirmaTask fromValue(String v) {
	        return valueOf(v);
	    }

	    public static List<StatoDestinatarioRichiestaApprovazioneFirmaTask> fromTipoProposta(TipoPropostaApprovazioneFirmaTask tp) {
	    	List<StatoDestinatarioRichiestaApprovazioneFirmaTask> result = new ArrayList<DatiApprovazioneFirmaTask.StatoDestinatarioRichiestaApprovazioneFirmaTask>();

	    	for (StatoDestinatarioRichiestaApprovazioneFirmaTask sd : StatoDestinatarioRichiestaApprovazioneFirmaTask.values())
	    		if (sd.getTipiProposta().contains(tp))
	    			result.add(sd);

	    	return result;
	    }
	}

	public enum TipoRispostaApprovazioneFirmaTask {
		RISPOSTA_POSITIVA,
		RISPOSTA_NEGATIVA,
		RISPOSTA_POSITIVA_CON_PRESCRIZIONI,
		RISPOSTA_SOSPESA,
		RISPOSTA_RIFIUTATA;

		public String value() {
	        return name();
	    }

		public static TipoRispostaApprovazioneFirmaTask fromValue(String v) {
		    return valueOf(v);
		}
	}

	@ToString
	@EqualsAndHashCode
	public static abstract class DestinatarioRichiestaApprovazioneFirmaTask {

		@Setter
		@Getter
		protected StatoDestinatarioRichiestaApprovazioneFirmaTask stato;
	}

	@ToString
	@EqualsAndHashCode(callSuper = true)
	public static class DestinatarioUtenteRichiestaApprovazioneFirmaTask extends DestinatarioRichiestaApprovazioneFirmaTask implements Comparable<DestinatarioUtenteRichiestaApprovazioneFirmaTask>{

		public DestinatarioUtenteRichiestaApprovazioneFirmaTask(String nomeUtente, String nome, String cognome, String matricola, String settore, StatoDestinatarioRichiestaApprovazioneFirmaTask stato){
			this.nomeUtente = nomeUtente;
			this.nome = nome;
			this.cognome = cognome;
			this.matricola = matricola;
			this.settore = settore;
			this.stato = stato;
		}

		@Setter
		@Getter
		private String nomeUtente;
		@Setter
		@Getter
		private String nome;
		@Setter
		@Getter
		private String cognome;
		@Setter
		@Getter
		private String matricola;
		@Setter
		@Getter
		private String settore;

		@Override
		public int compareTo(DestinatarioUtenteRichiestaApprovazioneFirmaTask o) {
			return nomeUtente.compareTo(o.getNomeUtente());
		}
	}

	@ToString
	@EqualsAndHashCode(callSuper = true)
	public static class DestinatarioGruppoRichiestaApprovazioneFirmaTask extends DestinatarioRichiestaApprovazioneFirmaTask implements Comparable<DestinatarioGruppoRichiestaApprovazioneFirmaTask>{

		public DestinatarioGruppoRichiestaApprovazioneFirmaTask(String nomeGruppo, StatoDestinatarioRichiestaApprovazioneFirmaTask stato) {
			this.nomeGruppo = nomeGruppo;
			this.stato = stato;
		}

		@Setter
		@Getter
		private String nomeGruppo;

		@Override
		public int compareTo(DestinatarioGruppoRichiestaApprovazioneFirmaTask o) {
			return nomeGruppo.compareTo(o.getNomeGruppo());
		}
	}

	public static class Builder {

		@Setter
		private Assegnatario assegnatario;
		@Setter
		private Date dataCreazione;
		@Setter
		private TreeSet<DestinatarioRichiestaApprovazioneFirmaTask> destinatari = new TreeSet<DestinatarioRichiestaApprovazioneFirmaTask>();
		@Setter
		private RiferimentoAllegatoApprovazioneFirmaTask riferimentoAllegato;
		@Setter
		private TipoPropostaApprovazioneFirmaTask tipo;
		@Setter
		private String stato;
		@Getter
		@Setter
		private String mittenteOriginale;
		@Getter
		@Setter
		private Date dataScadenza;
		@Getter
		@Setter
		private Boolean valido = true;

		@Setter
		Integer id;
		@Setter
		Boolean attivo;

		public Builder() {

		}

		public DatiApprovazioneFirmaTask construct() {
			DatiApprovazioneFirmaTask dati = new DatiApprovazioneFirmaTask(assegnatario, stato, dataCreazione, tipo, riferimentoAllegato, destinatari, mittenteOriginale, dataScadenza, valido);
			dati.setAttivo(attivo);
			dati.setId(id);
			return dati;

		}
	}
}