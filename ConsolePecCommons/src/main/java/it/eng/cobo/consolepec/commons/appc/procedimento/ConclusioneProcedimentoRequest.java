package it.eng.cobo.consolepec.commons.appc.procedimento;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @since 2019-11-21
 */
@Data
public class ConclusioneProcedimentoRequest implements Serializable {

	private static final long serialVersionUID = -4191732877669675817L;

	private Integer annoProtocollazione;
	private Integer annoProtocolloDocChiusura;
	private String codiceComune;
	private Integer codiceEventoChiusura;
	private Integer codiceTipologiaProcedimento;
	private String codiceUtente;
	private Date dataChiusura;
	private String modalitaChiusura;
	private String numeroProtocollazione;
	private String numeroProtocolloDocChiusura;
	private String tipoProtocollo;

}
