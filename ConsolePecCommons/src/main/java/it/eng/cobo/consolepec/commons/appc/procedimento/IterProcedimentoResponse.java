package it.eng.cobo.consolepec.commons.appc.procedimento;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * @since 2019-11-21
 */
@Data
public class IterProcedimentoResponse implements Serializable {

	private static final long serialVersionUID = 91824459133324486L;

	private Integer annoProtocollazione;
	private String codiceComune;
	private String codiceEvento;
	private String codiceMessaggio;
	private Integer codiceProcedimento;
	private Date dataEvento;
	private List<ProcedimentoDatiBase> datiProcedimento = new ArrayList<>();
	private String descrizioneMessaggio;
	private List<ProcedimentoIterLogico> iterEventi = new ArrayList<>();
	private Integer numItemCoda;
	private String numeroProtocollazione;
	private Integer numeroTotaleProcedimenti;
	private String progrEvento;
	private String tipoProtocollo;
	private String tipoRecord;

}
