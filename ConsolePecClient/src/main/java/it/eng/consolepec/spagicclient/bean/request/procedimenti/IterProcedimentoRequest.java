package it.eng.consolepec.spagicclient.bean.request.procedimenti;

public class IterProcedimentoRequest {

	private String tipoProtocollo;
	private int codComune;
	private String annoProtocollazione;
	private String numProtocollazione;

	public String getTipoProtocollo() {
		return tipoProtocollo;
	}

	public int getCodComune() {
		return codComune;
	}

	public String getAnnoProtocollazione() {
		return annoProtocollazione;
	}

	public String getNumProtocollazione() {
		return numProtocollazione;
	}

	public void setTipoProtocollo(String tipoProtocollo) {
		this.tipoProtocollo = tipoProtocollo;
	}

	public void setCodComune(int codComune) {
		this.codComune = codComune;
	}

	public void setAnnoProtocollazione(String annoProtocollazione) {
		this.annoProtocollazione = annoProtocollazione;
	}

	public void setNumProtocollazione(String numProtocollazione) {
		this.numProtocollazione = numProtocollazione;
	}

}
