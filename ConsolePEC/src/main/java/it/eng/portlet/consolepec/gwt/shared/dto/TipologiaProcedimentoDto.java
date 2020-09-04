package it.eng.portlet.consolepec.gwt.shared.dto;

import java.io.Serializable;
import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

public class TipologiaProcedimentoDto implements IsSerializable,Serializable,Comparable<TipologiaProcedimentoDto>{

	private static final long serialVersionUID = -5971106228474931183L;
	private String descrizioneSettore, flagTerritorialita, descrizione, modalitaAvvio;
	private Integer codiceProcedimento, codiceQuartiere, termineNormato;
	private Date dataInizio;

	public String getDescrizioneSettore() {
		return descrizioneSettore;
	}

	public void setDescrizioneSettore(String descrizioneSettore) {
		this.descrizioneSettore = descrizioneSettore;
	}

	public String getFlagTerritorialita() {
		return flagTerritorialita;
	}

	public void setFlagTerritorialita(String flagTerritorialita) {
		this.flagTerritorialita = flagTerritorialita;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getModalitaAvvio() {
		return modalitaAvvio;
	}

	public void setModalitaAvvio(String modalitaAvvio) {
		this.modalitaAvvio = modalitaAvvio;
	}

	public Integer getCodiceProcedimento() {
		return codiceProcedimento;
	}

	public void setCodiceProcedimento(Integer codiceProcedimento) {
		this.codiceProcedimento = codiceProcedimento;
	}

	public Integer getCodiceQuartiere() {
		return codiceQuartiere;
	}

	public void setCodiceQuartiere(Integer codiceQuartiere) {
		this.codiceQuartiere = codiceQuartiere;
	}

	public Integer getTermineNormato() {
		return termineNormato;
	}

	public void setTermineNormato(Integer termineNormato) {
		this.termineNormato = termineNormato;
	}

	public Date getDataInizio() {
		return dataInizio;
	}

	public void setDataInizio(Date dataInizio) {
		this.dataInizio = dataInizio;
	}

	@Override
	public String toString() {
		return "TipologiaProcedimentoDto [descrizioneSettore=" + descrizioneSettore + ", flagTerritorialita=" + flagTerritorialita + ", descrizione=" + descrizione + ", modalitaAvvio=" + modalitaAvvio + ", codiceProcedimento=" + codiceProcedimento + ", codiceQuartiere=" + codiceQuartiere
				+ ", termineNormato=" + termineNormato + ", dataInizio=" + dataInizio + "]";
	}

	@Override
	public int compareTo(TipologiaProcedimentoDto o) {
		return codiceProcedimento.compareTo(o.codiceProcedimento);
	}

}
