package it.eng.cobo.consolepec.commons.appc.procedimento;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @since 2019-11-21
 */
@Data
public class ProcedimentoIterLogico implements Serializable {

	private static final long serialVersionUID = 7476442356930344941L;

	private String annoProtocollazione;
	private String codiceComune;
	private String codiceEvento;
	private Integer codiceProcedimento;
	private Date dataEvento;
	private Date dataInizioEvento;
	private String descrizioneEvento;
	private Integer numItemCoda;
	private String numeroProtocollazione;
	private String progrEvento;
	private String tipoProtocollo;
	private String tipoRecord;

}
