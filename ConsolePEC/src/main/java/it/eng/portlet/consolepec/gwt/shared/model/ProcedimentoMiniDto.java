package it.eng.portlet.consolepec.gwt.shared.model;

import it.eng.portlet.consolepec.gwt.shared.procedimenti.StatoProcedimento;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ProcedimentoMiniDto implements IsSerializable {
	
	protected String numeroPG;
	protected String descrTipologiaProcedimento;
	protected Integer codTipologiaProcedimento;
	protected Integer annoPG;
	protected StatoProcedimento stato;
	protected Date dataInizioDecorrenzaProcedimento;
	protected Integer termine;
	
	public ProcedimentoMiniDto(){
	}
	
	public Integer getCodTipologiaProcedimento() {
		return codTipologiaProcedimento;
	}

	public void setCodTipologiaProcedimento(Integer codTipologiaProcedimento) {
		this.codTipologiaProcedimento = codTipologiaProcedimento;
	}
	
	public String getNumeroPG() {
		return numeroPG;
	}

	public void setNumeroPG(String numeroPG) {
		this.numeroPG = numeroPG;
	}
	
	public Integer getAnnoPG() {
		return annoPG;
	}

	public void setAnnoPG(Integer annoPG) {
		this.annoPG = annoPG;
	}
	
	public String getDescrTipologiaProcedimento() {
		return descrTipologiaProcedimento;
	}

	public void setDescrTipologiaProcedimento(String descrTipologiaProcedimento) {
		this.descrTipologiaProcedimento = descrTipologiaProcedimento;
	}

	public StatoProcedimento getStato() {
		return stato;
	}

	public void setStato(StatoProcedimento stato) {
		this.stato = stato;
	}
	
	public Date getDataInizioDecorrenzaProcedimento() {
		return dataInizioDecorrenzaProcedimento;
	}

	public void setDataInizioDecorrenzaProcedimento(Date dataInizioDecorrenzaProcedimento) {
		this.dataInizioDecorrenzaProcedimento = dataInizioDecorrenzaProcedimento;
	}

	public Integer getTermine() {
		return termine;
	}

	public void setTermine(Integer termine) {
		this.termine = termine;
	}
}
