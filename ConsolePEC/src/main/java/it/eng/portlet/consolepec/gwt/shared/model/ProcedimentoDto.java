package it.eng.portlet.consolepec.gwt.shared.model;

import java.util.Date;

public class ProcedimentoDto extends ProcedimentoMiniDto {
	
	private String codUtente, modAvvioProcedimento, flagInterruzione, responsabile, provenienza, modalitaChiusura, numeroPGChiusura;
	private Integer codUnitaOrgResponsabile, codUnitaOrgCompetenza, codQuartiere, annoPGChiusura, durata;
	private Date dataChiusura; 
	
	public ProcedimentoDto(){
	}
	
	public String getCodUtente() {
		return codUtente;
	}

	public void setCodUtente(String codUtente) {
		this.codUtente = codUtente;
	}

	public String getModAvvioProcedimento() {
		return modAvvioProcedimento;
	}

	public void setModAvvioProcedimento(String modAvvioProcedimento) {
		this.modAvvioProcedimento = modAvvioProcedimento;
	}

	public String getFlagInterruzione() {
		return flagInterruzione;
	}

	public void setFlagInterruzione(String flagInterruzione) {
		this.flagInterruzione = flagInterruzione;
	}

	public Integer getCodUnitaOrgResponsabile() {
		return codUnitaOrgResponsabile;
	}

	public void setCodUnitaOrgResponsabile(Integer codUnitaOrgResponsabile) {
		this.codUnitaOrgResponsabile = codUnitaOrgResponsabile;
	}

	public Integer getCodUnitaOrgCompetenza() {
		return codUnitaOrgCompetenza;
	}

	public void setCodUnitaOrgCompetenza(Integer codUnitaOrgCompetenza) {
		this.codUnitaOrgCompetenza = codUnitaOrgCompetenza;
	}

	public Integer getCodQuartiere() {
		return codQuartiere;
	}

	public void setCodQuartiere(Integer codQuartiere) {
		this.codQuartiere = codQuartiere;
	}

	public String getResponsabile() {
		return responsabile;
	}

	public void setResponsabile(String responsabile) {
		this.responsabile = responsabile;
	}

	public String getProvenienza() {
		return provenienza;
	}

	public void setProvenienza(String provenienza) {
		this.provenienza = provenienza;
	}

//	public Date getDataDecorrenza() {
//		return dataDecorrenza;
//	}
//
//	public void setDataDecorrenza(Date dataDecorrenza) {
//		this.dataDecorrenza = dataDecorrenza;
//	}

	public String getModalitaChiusura() {
		return modalitaChiusura;
	}

	public void setModalitaChiusura(String modalitaChiusura) {
		this.modalitaChiusura = modalitaChiusura;
	}

	public String getNumeroPGChiusura() {
		return numeroPGChiusura;
	}

	public void setNumeroPGChiusura(String numeroPGChiusura) {
		this.numeroPGChiusura = numeroPGChiusura;
	}

	public Integer getAnnoPGChiusura() {
		return annoPGChiusura;
	}

	public void setAnnoPGChiusura(Integer annoPGChiusura) {
		this.annoPGChiusura = annoPGChiusura;
	}
	
	public Integer getDurata() {
		return durata;
	}

	public void setDurata(Integer durata) {
		this.durata = durata;
	}
	
	public Date getDataChiusura() {
		return dataChiusura;
	}

	public void setDataChiusura(Date dataChiusura) {
		this.dataChiusura = dataChiusura;
	}

}
