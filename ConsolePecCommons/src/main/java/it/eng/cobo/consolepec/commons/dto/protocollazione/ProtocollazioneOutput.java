package it.eng.cobo.consolepec.commons.dto.protocollazione;

import java.util.Date;

import lombok.Data;

@Data
public class ProtocollazioneOutput {

	private String codiceMessaggio;
	private String descrizioneMesssaggio;
	private String annoProtocollo;
	private String numeroProtocollo;
	private Date dataProtocollo;
	private String annoFascicolo;
	private Integer numeroFascicolo;
	private String codiceTitolo;
	private String codiceRubrica;
	private String codiceSezione;
	private String annoCapofila;
	private String numeroCapofila;
	private String tipoRegistro;
	private String annoRegistro;
	private String numeroRegistro;
}
