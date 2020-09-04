package it.eng.portlet.consolepec.gwt.shared.model;

import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoGruppoProtocollato;

import java.util.Date;
import java.util.TreeSet;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CatenaDocumentaleDTO implements IsSerializable {

	private TreeSet<PgDTO> protocollazioni = new TreeSet<PgDTO>();

	public TreeSet<PgDTO> getProtocollazioni() {
		return protocollazioni;
	}	
	
	public static class PgDTO extends ElementoGruppoProtocollato implements Comparable<ElementoGruppoProtocollato> {

		
		private String provenienza;
		private String annoFascicolo;
		private Integer numFascicolo;
		private String descrTipologiaDoc;
		private String codCellaProtocollazione;
		private String descrCellaProtocollazione;
		private String codCellaAssegnazione;
		private String descrCellaAssegnazione;
		private Date dataAnnullamentoPratica;
		private Date dataAnnullamentoDoc;

		public String getProvenienza() {
			return provenienza;
		}

		public void setProvenienza(String provenienza) {
			this.provenienza = provenienza;
		}

		public String getAnnoFascicolo() {
			return annoFascicolo;
		}

		public void setAnnoFascicolo(String annoFascicolo) {
			this.annoFascicolo = annoFascicolo;
		}

		public Integer getNumFascicolo() {
			return numFascicolo;
		}

		public void setNumFascicolo(Integer numFascicolo) {
			this.numFascicolo = numFascicolo;
		}

		public String getDescrTipologiaDoc() {
			return descrTipologiaDoc;
		}

		public void setDescrTipologiaDoc(String descrTipologiaDoc) {
			this.descrTipologiaDoc = descrTipologiaDoc;
		}

		public String getCodCellaProtocollazione() {
			return codCellaProtocollazione;
		}

		public void setCodCellaProtocollazione(String codCellaProtocollazione) {
			this.codCellaProtocollazione = codCellaProtocollazione;
		}

		public String getDescrCellaProtocollazione() {
			return descrCellaProtocollazione;
		}

		public void setDescrCellaProtocollazione(String descrCellaProtocollazione) {
			this.descrCellaProtocollazione = descrCellaProtocollazione;
		}

		public String getCodCellaAssegnazione() {
			return codCellaAssegnazione;
		}

		public void setCodCellaAssegnazione(String codCellaAssegnazione) {
			this.codCellaAssegnazione = codCellaAssegnazione;
		}

		public String getDescrCellaAssegnazione() {
			return descrCellaAssegnazione;
		}

		public void setDescrCellaAssegnazione(String descrCellaAssegnazione) {
			this.descrCellaAssegnazione = descrCellaAssegnazione;
		}

		public Date getDataAnnullamentoPratica() {
			return dataAnnullamentoPratica;
		}

		public void setDataAnnullamentoPratica(Date dataAnnullamentoPratica) {
			this.dataAnnullamentoPratica = dataAnnullamentoPratica;
		}

		public Date getDataAnnullamentoDoc() {
			return dataAnnullamentoDoc;
		}

		public void setDataAnnullamentoDoc(Date dataAnnullamentoDoc) {
			this.dataAnnullamentoDoc = dataAnnullamentoDoc;
		}
		
		public boolean isCapofila(){
			return (getAnnoPG().equalsIgnoreCase(getAnnoPGCapofila()) && getNumeroPG().equalsIgnoreCase(getNumeroPGCapofila()));
		}
		
		@Override
		public int compareTo(ElementoGruppoProtocollato o) {
			
			int dataCfr = getDataProtocollazione().compareTo(o.getDataProtocollazione());
			if(dataCfr == 0)
				return getNumeroPG().compareTo(o.getNumeroPG());
			else
				return dataCfr;
		}
	}
}
