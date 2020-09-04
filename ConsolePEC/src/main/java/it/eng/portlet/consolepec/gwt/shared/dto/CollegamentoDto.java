package it.eng.portlet.consolepec.gwt.shared.dto;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoPECRiferimento;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoPraticaModulisticaRiferimento;

public class CollegamentoDto implements Comparable<CollegamentoDto>, IsSerializable {

	private String numeroPg, annoPg, oggetto, clientId;
	private List<String> operazioni = new ArrayList<String>();
	private boolean accessibileInLettura = true;
	private String displayNameGruppo;

	private List<ElementoPECRiferimento> elencoPEC;
	private List<ElementoPraticaModulisticaRiferimento> elencoPraticheModulistica;
	private List<AllegatoDTO> allegati;

	public CollegamentoDto() {
		this.elencoPEC = new ArrayList<ElementoPECRiferimento>();
		this.elencoPraticheModulistica = new ArrayList<ElementoPraticaModulisticaRiferimento>();
		this.allegati = new ArrayList<AllegatoDTO>();
	}

	public String getNumeroPg() {
		return numeroPg;
	}

	public void setNumeroPg(String numeroPg) {
		this.numeroPg = numeroPg;
	}

	public String getAnnoPg() {
		return annoPg;
	}

	public void setAnnoPg(String annoPg) {
		this.annoPg = annoPg;
	}

	public String getOggetto() {
		return oggetto;
	}

	public void setOggetto(String oggetto) {
		this.oggetto = oggetto;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public List<String> getOperazioni() {
		return operazioni;
	}

	public List<ElementoPECRiferimento> getElencoPEC() {
		return elencoPEC;
	}

	public List<ElementoPraticaModulisticaRiferimento> getElencoPraticheModulistica() {
		return elencoPraticheModulistica;
	}

	public List<AllegatoDTO> getAllegati() {
		return allegati;
	}

	public boolean isAccessibileInLettura() {
		return accessibileInLettura;
	}

	public void setAccessibileInLettura(boolean accessibileInLettura) {
		this.accessibileInLettura = accessibileInLettura;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((annoPg == null) ? 0 : annoPg.hashCode());
		result = prime * result + ((clientId == null) ? 0 : clientId.hashCode());
		result = prime * result + ((numeroPg == null) ? 0 : numeroPg.hashCode());
		result = prime * result + ((oggetto == null) ? 0 : oggetto.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		CollegamentoDto other = (CollegamentoDto) obj;
		if (annoPg == null) {
			if (other.annoPg != null) return false;
		} else if (!annoPg.equals(other.annoPg)) return false;
		if (clientId == null) {
			if (other.clientId != null) return false;
		} else if (!clientId.equals(other.clientId)) return false;
		if (numeroPg == null) {
			if (other.numeroPg != null) return false;
		} else if (!numeroPg.equals(other.numeroPg)) return false;
		if (oggetto == null) {
			if (other.oggetto != null) return false;
		} else if (!oggetto.equals(other.oggetto)) return false;
		return true;
	}

	@Override
	public int compareTo(CollegamentoDto o) {
		if (o.numeroPg != null && numeroPg != null) {
			int compareAnnoPg = annoPg.compareTo(o.annoPg);
			if (compareAnnoPg == 0) return numeroPg.compareTo(o.numeroPg);
			return compareAnnoPg;
		}
		return clientId.compareTo(o.clientId);
	}

	@Override
	public String toString() {
		return "CollegamentoDto [numeroPg=" + numeroPg + ", annoPg=" + annoPg + ", oggetto=" + oggetto + ", clientId=" + clientId + ", operazioni=" + operazioni + "]";
	}

	public String getDisplayNameGruppo() {
		return displayNameGruppo;
	}

	public void setDisplayNameGruppo(String displayNameGruppo) {
		this.displayNameGruppo = displayNameGruppo;
	}
}
