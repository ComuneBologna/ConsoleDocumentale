package it.eng.cobo.consolepec.commons.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class ProtocollazioneCapofila {

	private String numeroPG;
	private String numeroFascicolo;
	private String titolo;
	private String rubrica;
	private String sezione;
	private String tipologiaDocumento;
	private String oggetto;
	private String provenienza;
	private String utenteProtocollazione;
	private String numeroRegistro;
	private String note;
	private String dataArrivo;
	private String oraArrivo;
	private Integer annoPG;
	private Integer annoRegistro;
	private Integer annoFascicolo;
	private Date dataProtocollazione;
	private TipoProtocollazione tipoProtocollazione;
	private List<PraticaCollegata> praticheCollegateProtocollate = new ArrayList<>();
	private List<Allegato> allegatiProtocollati = new ArrayList<>();
	private List<ProtocollazioneCollegata> protocollazioniCollegate = new ArrayList<>();
	private Boolean fromBa01;

	public enum TipoProtocollazione {
		ENTRATA, USCITA, INTERNA;

		public static TipoProtocollazione tipoProtocollazione(String tipo) {
			for (TipoProtocollazione tp : TipoProtocollazione.values()) {
				if (tp.name().equalsIgnoreCase(tipo))
					return tp;
			}

			return null;
		}
	}

	@Data
	public static class ProtocollazioneCollegata {
		private String numeroPG;
		private String numeroFascicolo;
		private String titolo;
		private String rubrica;
		private String sezione;
		private String tipologiaDocumento;
		private String oggetto;
		private String provenienza;
		private String utenteProtocollazione;
		private String numeroRegistro;
		private String note;
		private String dataArrivo;
		private String oraArrivo;
		private Integer annoPG;
		private Integer annoRegistro;
		private Integer annoFascicolo;
		private Date dataProtocollazione;
		private TipoProtocollazione tipoProtocollazione;
		private List<PraticaCollegata> praticheCollegateProtocollate = new ArrayList<>();
		private List<Allegato> allegatiProtocollati = new ArrayList<>();
	}
}
