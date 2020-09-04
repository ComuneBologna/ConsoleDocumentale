package it.eng.cobo.consolepec.commons.dto.protocollazione;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public abstract class ProtocollazioneInput {

	private Integer codiceComune;
	private String codiceUtente;
	private String utenteApplicazione;
	private String fascicoloRiservato;
	private Date dataArrivo;
	private Date oraArrivo;

	private String cellaProtocollazione;
	private String cellaAssegnazione;
	private String tipoProtocollazione;
	private String codiceProvenienza;
	private String provenienza;
	private String cfProvenienza;
	private String riferimentoProvenienza;
	private String codiceDestinatario;
	private String destinatario;
	private String cfDestinatario;
	private String codiceTitolo;
	private String codiceRubrica;
	private String codiceSezione;
	private String documentazioneCompleta;
	private String tipologiaDocumento;
	private String oggetto;
	private Indirizzo viaPrincipale;
	private String sistemaImmissione;
	private String tipoApplicazione;
	private String mezzoSpedizione;
	private String tipoRegistro;

	private Integer numeroProtocolloCapofila;
	private Integer annoProtocolloCapofila;
	private Integer numeroProtocolloCapofilaBA01;
	private Integer annoProtocolloCapofilaBA01;

	private List<Indirizzo> indirizzi = new ArrayList<>();

	public abstract void accept(ProtocollazioneInputVisitor v);

	public static interface ProtocollazioneInputVisitor {

		void visit(ProtocollazioneNuovoFascicoloInput protocollazioneInput);

		void visit(ProtocollazioneFascicoloEsistenteInput protocollazioneInput);

	}

}
