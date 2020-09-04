package it.eng.cobo.consolepec.commons.appc.procedimento;

import java.io.Serializable;

import lombok.Data;

/**
 * @since 2019-11-21
 */
@Data
public class IterProcedimentoRequest implements Serializable {

	private static final long serialVersionUID = -4161735481891972268L;

	private Integer annoProtocollazione;
	private String codiceComune;
	private String numeroProtocollazione;
	private String tipoProtocollo;

}
