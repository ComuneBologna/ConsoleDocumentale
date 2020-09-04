package it.eng.portlet.consolepec.gwt.shared.action.fascicolo;

import java.util.Date;

public class AvvioProcedimentoResult implements GestioneProcedimentoResult {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6153976179803687829L;
	private String fascicoloPath;
	private String errorMsg;
	private boolean error;
	private String descrizioneProcedimento;
	private int codiceProcedimento;
	private Date dataInizioProcedimento;
	private Date dataChiusuraProcedimento;
	private Integer tempoNormatoInGiorni;
	private Integer durataProcedimento;

	@SuppressWarnings("unused")
	private AvvioProcedimentoResult() {
		this.error = false;
		this.errorMsg = null;
	}

	public AvvioProcedimentoResult(String fascicoloPath, String errorMsg, Boolean error) {
		this.fascicoloPath = fascicoloPath;
		this.error = error;
		this.errorMsg = errorMsg;
	}

	public AvvioProcedimentoResult(String fascicoloPath) {
		this.fascicoloPath = fascicoloPath;
		this.error = false;
		this.errorMsg = null;
	}

	public String getFascicoloPath() {
		return fascicoloPath;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public boolean isError() {
		return error;
	}

	public String getDescrizioneProcedimento() {
		return descrizioneProcedimento;
	}

	public void setDescrizioneProcedimento(String descrizioneProcedimento) {
		this.descrizioneProcedimento = descrizioneProcedimento;
	}

	public int getCodiceProcedimento() {
		return codiceProcedimento;
	}

	public void setCodiceProcedimento(int codiceProcedimento) {
		this.codiceProcedimento = codiceProcedimento;
	}

	public Date getDataInizioProcedimento() {
		return dataInizioProcedimento;
	}

	public void setDataInizioProcedimento(Date dataInizioProcedimento) {
		this.dataInizioProcedimento = dataInizioProcedimento;
	}

	public Date getDataChiusuraProcedimento() {
		return dataChiusuraProcedimento;
	}

	public void setDataChiusuraProcedimento(Date dataChiusuraProcedimento) {
		this.dataChiusuraProcedimento = dataChiusuraProcedimento;
	}

	public Integer getTempoNormatoInGiorni() {
		return tempoNormatoInGiorni;
	}

	public void setTempoNormatoInGiorni(Integer tempoNormatoInGiorni) {
		this.tempoNormatoInGiorni = tempoNormatoInGiorni;
	}

	public Integer getDurataProcedimento() {
		return durataProcedimento;
	}

	public void setDurataProcedimento(Integer durataProcedimento) {
		this.durataProcedimento = durataProcedimento;
	}

	public void setFascicoloPath(String fascicoloPath) {
		this.fascicoloPath = fascicoloPath;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public void setError(boolean error) {
		this.error = error;
	}

}
