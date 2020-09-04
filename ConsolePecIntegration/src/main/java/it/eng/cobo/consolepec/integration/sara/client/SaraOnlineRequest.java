package it.eng.cobo.consolepec.integration.sara.client;

import it.eng.cobo.consolepec.integration.sara.client.SaraOnlineResponse.FlagEcoNeco;
import it.eng.cobo.consolepec.integration.sara.client.SaraOnlineResponse.Titolare;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

public class SaraOnlineRequest {

	public static class DatiResidenti {

		@Getter @Setter private boolean flagPatente;
		@Getter @Setter private String enteEmissionePatente;
		@Getter @Setter private boolean gratuito;
		@Getter @Setter private String settore;
		@Getter @Setter private String secondoSettore;
	}

	public static enum CategoriaContrassegno {

		RESIDENTI, VETROFANIE;

		public String value() {
			return name();
		}

		public static CategoriaContrassegno fromValue(String v) {
			return valueOf(v);
		}

	}

	public static class TitoloPossesso {
		
		@Getter @Setter private ProprietaPersonale proprietaPersonale;
		@Getter @Setter private ProprietaAltroMembro proprietaAltroMembro;
		@Getter @Setter private Contratto contratto;
		@Getter @Setter private TrascrizioneLibretto trascrizioneLibretto;
	}

	public static class ProprietaPersonale {

		@Getter @Setter private String codiceFiscale;
	}

	public static class ProprietaAltroMembro {

		@Getter @Setter private String codiceFiscale;
	}

	public static class Contratto {

		@Getter @Setter private Leasing leasing;
		@Getter @Setter private Noleggio noleggio;
		@Getter @Setter private Comodato comodato;
	}

	public static class Leasing {

		@Getter @Setter private String codiceFiscaleAzienda;
		@Getter @Setter private String denominazioneAzienda;
	}

	public static class Noleggio {

		@Getter @Setter private String codiceFiscaleAzienda;
		@Getter @Setter private String denominazioneAzienda;
	}

	public static class Comodato {

		@Getter @Setter private String luogoRegistrazione;
		@Getter @Setter private String numeroRegistrazione;
	}

    public static class TrascrizioneLibretto {

    	@Getter @Setter private Date dataRegistrazione;
    	@Getter @Setter private String luogoRegistrazione;
    	@Getter @Setter private String protocollo;
    }
	
	public static class TitolareGenerico {
		
		@Getter @Setter private Titolare titolare;
		@Getter @Setter private TitolareTestoLibero titolareTestoLibero;
	}
	
	public static class TitolareTestoLibero {

		@Getter @Setter private String comune;
		@Getter @Setter private String nomeCognome;
		@Getter @Setter private String provincia;
		@Getter @Setter  private String stato;
	}
	
	public static enum TipoPermesso {

	    XH;

	    public String value() {
	        return name();
	    }

	    public static TipoPermesso fromValue(String v) {
	        return valueOf(v);
	    }

	}

	public static class VeicoloGenerico {
		
		@Getter @Setter Veicolo veicolo;
		@Getter @Setter VeicoloDettaglio veicoloDettaglio;
	}
	
	public static class VeicoloDettaglio {
		
		@Getter @Setter private String alimentazioneVeicolo;
		@Getter @Setter private String compatibilitaAmbientale;
		@Getter @Setter private Date dataFineValidita;
		@Getter @Setter private Date dataInizioValidita;
		@Getter @Setter private Integer direttiva;
		@Getter @Setter private String direttivaDesc;
		@Getter @Setter private String direttivaMctc;
		@Getter @Setter private FlagEcoNeco ecoNeco;
		@Getter @Setter private FlagCessato flagCessato;
		@Getter @Setter private FlagItalianoEstero flagItalianoEstero;
		@Getter @Setter private FlagPermanenteAggiuntivo flagPermanenteAggiuntivo;
		@Getter @Setter private String idPermessoVeicolo;
		@Getter @Setter private String indEuro;
		@Getter @Setter private String targa;
		@Getter @Setter private String tipoVeicolo;
		@Getter @Setter private String tipoVeicoloDesc;
	}
	
	public static class Veicolo {
		
		@Getter @Setter private FlagItalianoEstero flagItalianoEstero;
		@Getter @Setter private FlagPermanenteAggiuntivo flagPermanenteAggiuntivo;
		@Getter @Setter private String idPermessoVeicolo;
		@Getter @Setter private String targa;
		@Getter @Setter private String tipoVeicolo;
	}

	public static enum FlagItalianoEstero {

		I, E;

		public String value() {
			return name();
		}

		public static FlagItalianoEstero fromValue(String v) {
			return valueOf(v);
		}

	}

	public static enum FlagPermanenteAggiuntivo {

		P, T;

		public String value() {
			return name();
		}

		public static FlagPermanenteAggiuntivo fromValue(String v) {
			return valueOf(v);
		}
	}
	
	public static enum MotivoRilascio {
		E_21;
		
		public String value() {
			return name();
		}

		public static MotivoRilascio fromValue(String v) {
			return valueOf(v);
		}
	}
	
	public enum FlagCessato {

	    A,
	    C;

	    public String value() {
	        return name();
	    }

	    public static FlagCessato fromValue(String v) {
	        return valueOf(v);
	    }

	}
}
