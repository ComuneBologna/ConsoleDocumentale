package it.eng.consolepec.spagicclient.remoteproxy.result;

import it.bologna.comune.alfresco.creazione.fascicolo.Request.DatoAggiuntivo;
import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;

import java.util.ArrayList;
import java.util.List;

public class CreaFascicoloDto {

	private String titolo;
	private String utente;
	private String note;
	private String idEmailIn;
	private String tipoPratica = TipologiaPratica.FASCICOLO.getNomeTipologia();
	private String assegnatario;
	private List<DatoAggiuntivo> datiAggiuntivi = new ArrayList<DatoAggiuntivo>();

	public List<DatoAggiuntivo> getDatiAggiuntivi() {
		return datiAggiuntivi;
	}

	public void setDatiAggiuntivi(List<DatoAggiuntivo> datiAggiuntivi) {
		this.datiAggiuntivi = datiAggiuntivi;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public String getUtente() {
		return utente;
	}

	public void setUtente(String utente) {
		this.utente = utente;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getIdEmailIn() {
		return idEmailIn;
	}

	public void setIdEmailIn(String idEmailIn) {
		this.idEmailIn = idEmailIn;
	}

	public String getTipoPratica() {
		return tipoPratica;
	}

	public void setTipoPratica(String tipoPratica) {
		this.tipoPratica = tipoPratica;
	}

	public String getAssegnatario() {
		return assegnatario;
	}

	public void setAssegnatario(String assegnatario) {
		this.assegnatario = assegnatario;
	}

}
