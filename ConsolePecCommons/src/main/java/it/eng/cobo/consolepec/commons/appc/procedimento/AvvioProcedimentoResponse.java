package it.eng.cobo.consolepec.commons.appc.procedimento;

import java.io.Serializable;

import lombok.Data;

/**
 * @since 2019-11-21
 */
@Data
public class AvvioProcedimentoResponse implements Serializable {

	private static final long serialVersionUID = -4972185149189302978L;

	private String codiceMessaggio;
	private String descrizioneMessaggio;
	private Integer termine;

}
