package it.eng.cobo.consolepec.commons.appc.procedimento;

import java.io.Serializable;

import lombok.Data;

@Data
public class ConclusioneProcedimentoResponse implements Serializable {

	private static final long serialVersionUID = 5785896941821343177L;

	private String codiceMessaggio;
	private String descrizioneMessaggio;
	private Integer durata;

}
