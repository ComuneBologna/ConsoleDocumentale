package it.eng.cobo.consolepec.commons.dto;

import java.util.Date;

import lombok.Data;

@Data
public class Procedimento {

	private String codidceUtente;
	private String modalitaAvvioProcedimento;
	private String flagInterruzione;
	private String numeroPG;
	private String responsabile;
	private String provenienza;
	private String modalitaChiusura;
	private String numeroPGChiusura;
	private Integer codiceTipologiaProcedimento;
	private Integer codiceUnitaOrgResponsabile;
	private Integer codiceUnitaOrgCompetenza;
	private Integer durata;
	private Integer codiceQuartiere;
	private Integer annoPG;
	private Integer termine;
	private Integer annoPGChiusura;
	private Date dataInizioDecorrenzaProcedimento;
	private Date dataChiusura;
}
