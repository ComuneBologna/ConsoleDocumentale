package it.eng.cobo.consolepec.integration.sara.client;


import it.eng.cobo.consolepec.integration.sara.client.SaraOnlineRequest.VeicoloDettaglio;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class SaraOnlineResponse {

	public static class File {
		@Getter @Setter private byte[] content;
		@Getter @Setter private String fileName;
	}
	
	public static class Esito {
		@Getter @Setter private CodEsito codEsito;
		@Getter @Setter private String descrizione;
		@Getter @Setter private ErrorCode errorCode;
	}

	public static enum CodEsito {

		OK, ERROR;

		public String value() {
			return name();
		}

		public static CodEsito fromValue(String v) {
			return valueOf(v);
		}

	}

	public static enum ErrorCode {

		E_01("E01"),E_02("E02"),  E_03("E03"), E_04("E04"),
		E_05("E05"),E_06("E06"),  E_07("E07"), E_08("E08"),
		E_09("E09"),E_10("E10"),  E_11("E11"), E_12("E12"),
		E_13("E13"), E_18("E18"), E_19("E19"), E_21("E21"),
		ER_01("ER01"),E_20("E20"), EI_01("EI01"),
		 EI_02("EI02"),  EI_03("EI03"),  EI_04("EI04"),
		 EI_05("EI05"),  EI_06("EI06"),EI_07("EI07"),
		 EI_08("EI08"),  EI_09("EI09"),  EI_10("EI10"),
		 EI_11("EI11");
		private final String value;

		ErrorCode(String v) {
			value = v;
		}

		public String value() {
			return value;
		}

		public static ErrorCode fromValue(String v) {
			for (ErrorCode c : ErrorCode.values()) {
				if (c.value.equals(v)) {
					return c;
				}
			}
			throw new IllegalArgumentException(v);
		}

	}

	public static class Contrassegno {

		@Getter @Setter private Titolare titolare;
		@Getter @Setter private ArrayOfVeicoloDettaglio veicoli;
		@Getter @Setter private ArrayOfVeicoloDettaglio veicoliAggiuntivi;
		@Getter @Setter private Date dataFineValidita;
		@Getter @Setter private Date dataInizioValidita;
		@Getter @Setter private FlagEcoNeco ecoNeco;
		@Getter @Setter private Boolean flagTargaEstera;
		@Getter @Setter private String indirizzoRiferimento;
		@Getter @Setter private Integer numCambiTargaAggiuntiviResidui;
		@Getter @Setter private Integer numCambiTargaPermanentiResidui;
		@Getter @Setter private Integer numeroContrassegno;
		@Getter @Setter private String secondoSettore;
		@Getter @Setter private String settore;
		@Getter @Setter private String sostaPagamento;
		@Getter @Setter private String tipoContrassegno;
		@Getter @Setter private String zoneSirio;
	}

	public static class Titolare {

		@Getter @Setter private String codiceFiscale;
		@Getter @Setter private String cognome;
		@Getter @Setter private Date dataNascita;
		@Getter @Setter private String idTitolare;
		@Getter @Setter private String nome;
		@Getter @Setter private Sesso sesso;
		@Getter @Setter private String telefonoCellulare;
		@Getter @Setter private String telefonoFisso;
	}

	public static enum Sesso {

		M, F;

		public String value() {
			return name();
		}

		public static Sesso fromValue(String v) {
			return valueOf(v);
		}

	}

	public static class ArrayOfVeicoloDettaglio {

		@Getter @Setter private List<VeicoloDettaglio> item;
	}

	public static enum FlagEcoNeco {

		E, NE;

		public String value() {
			return name();
		}

		public static FlagEcoNeco fromValue(String v) {
			return valueOf(v);
		}

	}

	public static class ArrayOfXsdString {

		@Getter @Setter private List<String> item;
	}
}
