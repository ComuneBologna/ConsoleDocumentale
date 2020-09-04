package it.eng.consolepec.spagicclient.bean.response.procedimenti;

public class GestioneProcedimentoResponse {

	private String codice;
	private String descrizione;
	private int termine;
	private int durata;

	public String getCodice() {
		return codice;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public int getTermine() {
		return termine;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public void setTermine(int termine) {
		this.termine = termine;
	}

	public int getDurata() {
		return durata;
	}

	public void setDurata(int durata) {
		this.durata = durata;
	}
}
