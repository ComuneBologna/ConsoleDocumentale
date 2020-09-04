package it.eng.portlet.consolepec.gwt.shared.model;

import it.eng.cobo.consolepec.commons.pratica.fascicolo.DatiProcedimento;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class PraticaModulisticaDTO extends PraticaDTO {
	protected String nome;
	protected List<NodoModulisticaDTO> valori = new ArrayList<NodoModulisticaDTO>();
	protected StatoDTO stato;
	
	@Getter 
	@Setter 
	protected DatiProcedimento datiProcedimento;

	private String idClientFascicolo;

	protected boolean eliminaAbilitato, archiviaAbilitato, creaFascicoloAbilitato, riportaInGestioneAbilitato, riassegnaAbilitato,
			aggiungiFascicoloAbilitato;

	public PraticaModulisticaDTO() {

	}

	public PraticaModulisticaDTO(String alfrescoPath) {
		super(alfrescoPath);
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	public List<NodoModulisticaDTO> getValori() {
		return valori;
	}

	public StatoDTO getStato() {
		return stato;
	}

	public void setStato(StatoDTO stato) {
		this.stato = stato;
	}

	public boolean isEliminaAbilitato() {
		return eliminaAbilitato;
	}

	public void setEliminaAbilitato(boolean eliminaAbilitato) {
		this.eliminaAbilitato = eliminaAbilitato;
	}

	public boolean isArchiviaAbilitato() {
		return archiviaAbilitato;
	}

	public void setArchiviaAbilitato(boolean archiviaAbilitato) {
		this.archiviaAbilitato = archiviaAbilitato;
	}

	public boolean isCreaFascicoloAbilitato() {
		return creaFascicoloAbilitato;
	}

	public void setCreaFascicoloAbilitato(boolean creaFascicoloAbilitato) {
		this.creaFascicoloAbilitato = creaFascicoloAbilitato;
	}

	public boolean isRiportaInGestioneAbilitato() {
		return riportaInGestioneAbilitato;
	}

	public void setRiportaInGestioneAbilitato(boolean riportaInGestioneAbilitato) {
		this.riportaInGestioneAbilitato = riportaInGestioneAbilitato;
	}

	public boolean isRiassegnaAbilitato() {
		return riassegnaAbilitato;
	}

	public void setRiassegnaAbilitato(boolean riassegnaAbilitato) {
		this.riassegnaAbilitato = riassegnaAbilitato;
	}

	public boolean isAggiungiFascicoloAbilitato() {
		return aggiungiFascicoloAbilitato;
	}

	public void setAggiungiFascicoloAbilitato(boolean aggiungiFascicoloAbilitato) {
		this.aggiungiFascicoloAbilitato = aggiungiFascicoloAbilitato;
	}

	public String getIdClientFascicolo() {
		return idClientFascicolo;
	}

	public void setIdClientFascicolo(String idClientFascicolo) {
		this.idClientFascicolo = idClientFascicolo;
	}

	public String getCodiceFiscaleProvenienza() {
		return null;
	}

	@Override
	public String getProvenienza() {
		return null;
	}

	public String getTitoloProtocollazione() {
		return null;
	}

	public Date getDataInizioBando() {
		return null;
	}

	public Date getDataFineBando() {
		return null;
	}

	public enum StatoDTO {
		IN_GESTIONE("In gestione"), ARCHIVIATA("Archiviata"), ELIMINATA("Eliminata");

		private String label;

		StatoDTO(String label) {
			this.label = label;
		}

		public String getLabel() {
			return label;
		}

		public static StatoDTO fromLabel(String label) {
			for (StatoDTO dto : values()) {
				if (dto.getLabel().equals(label)) return dto;
			}
			return null;
		}
	}
}
