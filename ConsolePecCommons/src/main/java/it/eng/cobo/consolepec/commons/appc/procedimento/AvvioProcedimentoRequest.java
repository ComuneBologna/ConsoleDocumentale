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
public class AvvioProcedimentoRequest implements Serializable {

	private static final long serialVersionUID = -1844312172432236921L;

	private Integer annoProtocollazione;
	private String codiceComune;
	private Integer codiceQuartiere;
	private Integer codiceTipologiaProcedimento;
	private Integer codiceUnitaOrgCompetente;
	private Integer codiceUnitaOrgResponsabile;
	private String codiceUtente;
	private Date dataInizioDecorrenzaProcedimento;
	private String flagInterruzione;
	private String modalitaAvvioProcedimento;
	private String numeroProtocollazione;
	private String tipoProtocollo;

	private List<String> indirizziDestinatari = new ArrayList<>();

}
