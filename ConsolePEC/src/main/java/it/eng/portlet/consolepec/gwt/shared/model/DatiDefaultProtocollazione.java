package it.eng.portlet.consolepec.gwt.shared.model;

import it.eng.cobo.consolepec.commons.pratica.fascicolo.DatiProcedimento;
import it.eng.cobo.consolepec.commons.pratica.fascicolo.Titolazione;
import it.eng.portlet.consolepec.gwt.shared.dto.DatiPg;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import lombok.Getter;
import lombok.Setter;

public class DatiDefaultProtocollazione implements Serializable {

	private static final long serialVersionUID = 1L;

	private boolean isFascicoloNuovo;
	private int numeroAllegati;
	private Date dataDiProtocollazione = new Date();
	private String utente;
	private String note;
	private String titolo;
	private String assegnatario;
	private String oggettoProtocollazione;
	private String codiceFiscaleProvenienza;
	private String provenienza;
	private DatiPg datiPg;
	@Getter @Setter private Titolazione titolazione;
	@Getter @Setter private DatiProcedimento datiProcedimento;
	private Set<String> allegati = new TreeSet<String>();
	private Set<String> pratiche = new TreeSet<String>();
	private boolean protocollazioneRiservata;

	public DatiDefaultProtocollazione() {}

	public String getAssegnatario() {
		return assegnatario;
	}

	public void setAssegnatario(String assegnatario) {
		this.assegnatario = assegnatario;
	}

	public String getUtente() {
		return utente;
	}

	public Date getDataDiProtocollazione() {
		return dataDiProtocollazione;
	}

	public void setDataDiProtocollazione(Date dataDiProtocollazione) {
		this.dataDiProtocollazione = dataDiProtocollazione;
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

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public Set<String> getAllegati() {
		return allegati;
	}

	public void setAllegati(Set<String> allegati) {
		this.allegati = allegati;
	}

	public Set<String> getPratiche() {
		return pratiche;
	}

	public boolean isFascicoloNuovo() {
		return isFascicoloNuovo;
	}

	public void setFascicoloNuovo(boolean isFascicoloNuovo) {
		this.isFascicoloNuovo = isFascicoloNuovo;
	}

	public DatiPg getDatiPg() {
		return datiPg;
	}

	public void setDatiPg(DatiPg datiPg) {
		this.datiPg = datiPg;
	}

	public String getOggettoProtocollazione() {
		return oggettoProtocollazione;
	}

	public void setOggettoProtocollazione(String oggettoProtocollazione) {
		this.oggettoProtocollazione = oggettoProtocollazione;
	}

	public int getNumeroAllegati() {
		return numeroAllegati;
	}

	public void initNumeroAllegati(Set<PraticaDTO> pratiche, Set<AllegatoDTO> allegati) {
		int numeroAllegati = 0;
		for (PraticaDTO p : pratiche)
			numeroAllegati = numeroAllegati + p.getAllegati().size();
		numeroAllegati = numeroAllegati + allegati.size();
		this.numeroAllegati = numeroAllegati;
	}

	public String getCodiceFiscaleProvenienza() {
		return codiceFiscaleProvenienza;
	}

	public void setCodiceFiscaleProvenienza(String codiceFiscaleProvenienza) {
		this.codiceFiscaleProvenienza = codiceFiscaleProvenienza;
	}

	public String getProvenienza() {
		return provenienza;
	}

	public void setProvenienza(String provenienza) {
		this.provenienza = provenienza;
	}

	public boolean isProtocollazioneRiservata() {
		return protocollazioneRiservata;
	}

	public void setProtocollazioneRiservata(boolean protocollazioneRiservata) {
		this.protocollazioneRiservata = protocollazioneRiservata;
	}

}
