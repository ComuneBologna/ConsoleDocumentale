package it.eng.consolepec.spagicclient.bean.response.procedimenti;

import it.bologna.comune.base.Error;
import it.bologna.comune.spagic.procedimenti.iter.Record1;
import it.bologna.comune.spagic.procedimenti.iter.Record2;

import java.util.ArrayList;
import java.util.List;


public class IterProcedimentoResponse {
	
	private int codComune;
	private String tipoProtocollo;
	private String annoProtocollazione;
	private String numProtocollazione;

	private String tipoRecord;
	private Integer codProcedimento;
	private String dataEvento;
	private String codEvento;
    private String progrEvento;
    private Integer numItemCoda;
	
    private String codMessaggio;
    private String descMessaggio;
    private Integer numTotProcedimenti;
	
    private Error error;
    private List<Record1> datiProcedimento = new ArrayList<Record1>();
    private List<Record2> iterEventi = new ArrayList<Record2>();
    
	public int getCodComune() {
		return codComune;
	}
	public void setCodComune(int codComune) {
		this.codComune = codComune;
	}
	public String getTipoProtocollo() {
		return tipoProtocollo;
	}
	public void setTipoProtocollo(String tipoProtocollo) {
		this.tipoProtocollo = tipoProtocollo;
	}
	public String getAnnoProtocollazione() {
		return annoProtocollazione;
	}
	public void setAnnoProtocollazione(String annoProtocollazione) {
		this.annoProtocollazione = annoProtocollazione;
	}
	public String getNumProtocollazione() {
		return numProtocollazione;
	}
	public void setNumProtocollazione(String numProtocollazione) {
		this.numProtocollazione = numProtocollazione;
	}
	public String getTipoRecord() {
		return tipoRecord;
	}
	public void setTipoRecord(String tipoRecord) {
		this.tipoRecord = tipoRecord;
	}
	public Integer getCodProcedimento() {
		return codProcedimento;
	}
	public void setCodProcedimento(Integer codProcedimento) {
		this.codProcedimento = codProcedimento;
	}
	public String getDataEvento() {
		return dataEvento;
	}
	public void setDataEvento(String dataEvento) {
		this.dataEvento = dataEvento;
	}
	public String getCodEvento() {
		return codEvento;
	}
	public void setCodEvento(String codEvento) {
		this.codEvento = codEvento;
	}
	public String getProgrEvento() {
		return progrEvento;
	}
	public void setProgrEvento(String progrEvento) {
		this.progrEvento = progrEvento;
	}
	public Integer getNumItemCoda() {
		return numItemCoda;
	}
	public void setNumItemCoda(Integer numItemCoda) {
		this.numItemCoda = numItemCoda;
	}
	public String getCodMessaggio() {
		return codMessaggio;
	}
	public void setCodMessaggio(String codMessaggio) {
		this.codMessaggio = codMessaggio;
	}
	public String getDescMessaggio() {
		return descMessaggio;
	}
	public void setDescMessaggio(String descMessaggio) {
		this.descMessaggio = descMessaggio;
	}
	public Integer getNumTotProcedimenti() {
		return numTotProcedimenti;
	}
	public void setNumTotProcedimenti(Integer numTotProcedimenti) {
		this.numTotProcedimenti = numTotProcedimenti;
	}
	public Error getError() {
		return error;
	}
	public void setError(Error error) {
		this.error = error;
	}
	public List<Record1> getDatiProcedimento() {
		return datiProcedimento;
	}
	public List<Record2> getIterEventi() {
		return iterEventi;
	}

}
