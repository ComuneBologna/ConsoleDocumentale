package it.eng.cobo.consolepec.commons.appc.procedimento;

import java.io.Serializable;

import lombok.Data;

/**
 * @author Giacomo F.M.
 * @since 2019-12-13
 */
@Data
public class PropostaChiusuraProcedimentoRequest implements Serializable {

	private static final long serialVersionUID = 5739197799932330785L;

	private String codiceComune;
	private String tipoProtocollo;
	private String annoProtocollazione;
	private String numeroProtocollazione;

}
