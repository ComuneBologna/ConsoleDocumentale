package it.eng.consolepec.spagicclient.bean.request.procedimenti;

import java.util.Date;

public class GestioneProcedimentoRequest {

	private String operazione;
	private String tipoProtocollo;
	private int codComune;
	private String codUtente;
	private int annoProtocollazione;
	private String numProtocollazione;
	private Date dataProtocollazione;
	private Integer codTipologiaProcedimento;
	private Date dataInizioDecorrenzaProcedimento;
	private String modAvvioProcedimento;
	private Integer codUnitaOrgResponsabile;
	private Integer codUnitaOrgCompetenza;
	private String modalitaChiusura;
	private Date dataChiusura;
	private String numProtocolloDocChiusura;
	private Integer annoProtocolloDocChiusura;
	private Integer codiceEventoChiusura;
	private Date dataInizioInterruzione;
	private String flagInterruzione;
	private Integer giorniInterruzione;
	private Date dataFineInterruzione;
	private Integer nuovoCodTipologiaProcedimento;
	private Integer codQuartiere;
	
	public String getOperazione() {
		return operazione;
	}
	public void setOperazione(String operazione) {
		this.operazione = operazione;
	}
	public String getTipoProtocollo() {
		return tipoProtocollo;
	}
	public void setTipoProtocollo(String tipoProtocollo) {
		this.tipoProtocollo = tipoProtocollo;
	}
	public int getCodComune() {
		return codComune;
	}
	public void setCodComune(int codComune) {
		this.codComune = codComune;
	}
	public String getCodUtente() {
		return codUtente;
	}
	public void setCodUtente(String codUtente) {
		this.codUtente = codUtente;
	}
	public int getAnnoProtocollazione() {
		return annoProtocollazione;
	}
	public void setAnnoProtocollazione(int annoProtocollazione) {
		this.annoProtocollazione = annoProtocollazione;
	}
	public String getNumProtocollazione() {
		return numProtocollazione;
	}
	public void setNumProtocollazione(String numProtocollazione) {
		this.numProtocollazione = numProtocollazione;
	}
	public Integer getCodTipologiaProcedimento() {
		return codTipologiaProcedimento;
	}
	public void setCodTipologiaProcedimento(Integer codTipologiaProcedimento) {
		this.codTipologiaProcedimento = codTipologiaProcedimento;
	}
	public Date getDataInizioDecorrenzaProcedimento() {
		return dataInizioDecorrenzaProcedimento;
	}
	public void setDataInizioDecorrenzaProcedimento(Date dataInizioDecorrenzaProcedimento) {
		this.dataInizioDecorrenzaProcedimento = dataInizioDecorrenzaProcedimento;
	}
	public String getModAvvioProcedimento() {
		return modAvvioProcedimento;
	}
	public void setModAvvioProcedimento(String modAvvioProcedimento) {
		this.modAvvioProcedimento = modAvvioProcedimento;
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
	public String getModalitaChiusura() {
		return modalitaChiusura;
	}
	public void setModalitaChiusura(String modalitaChiusura) {
		this.modalitaChiusura = modalitaChiusura;
	}
	public Date getDataChiusura() {
		return dataChiusura;
	}
	public void setDataChiusura(Date dataChiusura) {
		this.dataChiusura = dataChiusura;
	}
	public String getNumProtocolloDocChiusura() {
		return numProtocolloDocChiusura;
	}
	public void setNumProtocolloDocChiusura(String numProtocolloDocChiusura) {
		this.numProtocolloDocChiusura = numProtocolloDocChiusura;
	}
	public Integer getAnnoProtocolloDocChiusura() {
		return annoProtocolloDocChiusura;
	}
	public void setAnnoProtocolloDocChiusura(Integer annoProtocolloDocChiusura) {
		this.annoProtocolloDocChiusura = annoProtocolloDocChiusura;
	}
	public Integer getCodiceEventoChiusura() {
		return codiceEventoChiusura;
	}
	public void setCodiceEventoChiusura(Integer codiceEventoChiusura) {
		this.codiceEventoChiusura = codiceEventoChiusura;
	}
	public Date getDataInizioInterruzione() {
		return dataInizioInterruzione;
	}
	public void setDataInizioInterruzione(Date dataInizioInterruzione) {
		this.dataInizioInterruzione = dataInizioInterruzione;
	}
	public String getFlagInterruzione() {
		return flagInterruzione;
	}
	public void setFlagInterruzione(String flagInterruzione) {
		this.flagInterruzione = flagInterruzione;
	}
	public Integer getGiorniInterruzione() {
		return giorniInterruzione;
	}
	public void setGiorniInterruzione(Integer giorniInterruzione) {
		this.giorniInterruzione = giorniInterruzione;
	}
	public Date getDataFineInterruzione() {
		return dataFineInterruzione;
	}
	public void setDataFineInterruzione(Date dataFineInterruzione) {
		this.dataFineInterruzione = dataFineInterruzione;
	}
	public Integer getNuovoCodTipologiaProcedimento() {
		return nuovoCodTipologiaProcedimento;
	}
	public void setNuovoCodTipologiaProcedimento(Integer nuovoCodTipologiaProcedimento) {
		this.nuovoCodTipologiaProcedimento = nuovoCodTipologiaProcedimento;
	}
	public Integer getCodQuartiere() {
		return codQuartiere;
	}
	public void setCodQuartiere(Integer codQuartiere) {
		this.codQuartiere = codQuartiere;
	}
	public Date getDataProtocollazione() {
		return dataProtocollazione;
	}
	public void setDataProtocollazione(Date dataProtocollazione) {
		this.dataProtocollazione = dataProtocollazione;
	}

}
