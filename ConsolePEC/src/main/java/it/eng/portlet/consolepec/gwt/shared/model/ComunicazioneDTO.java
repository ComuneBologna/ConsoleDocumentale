package it.eng.portlet.consolepec.gwt.shared.model;

import java.util.ArrayList;
import java.util.List;

import it.eng.portlet.consolepec.gwt.shared.dto.CollegamentoDto;

public class ComunicazioneDTO extends PraticaDTO {
	
	private String codice;
	private String descrizione;
	private String idDocumentaleTemplate;
	private List<InvioComunicazioneDTO> inviiComunicazione = new ArrayList<InvioComunicazioneDTO>();
	private StatoDTO stato;
	private List<CollegamentoDto> collegamenti = new ArrayList<CollegamentoDto>();
	private boolean caricaAllegatoAbilitato, eliminaAllegatoAbilitato, creaComunicazionePerCopiaAbilitato;
	
	public ComunicazioneDTO(String clientID) {
		super(clientID);
	}
	
	public ComunicazioneDTO() {
		super();
	}

	public String getCodice() {
		return codice;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public String getIdDocumentaleTemplate() {
		return idDocumentaleTemplate;
	}
	public void setCodice(String codice) {
		this.codice = codice;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public void setIdDocumentaleTemplate(String idDocumentaleTemplate) {
		this.idDocumentaleTemplate = idDocumentaleTemplate;
	}
	
	public List<InvioComunicazioneDTO> getInviiComunicazione() {
		return inviiComunicazione;
	}
	
	public StatoDTO getStato() {
		return stato;
	}

	public void setStato(StatoDTO stato) {
		this.stato = stato;
	}
	public boolean isCaricaAllegatoAbilitato() {
		return caricaAllegatoAbilitato;
	}
	public boolean isEliminaAllegatoAbilitato() {
		return eliminaAllegatoAbilitato;
	}
	public void setCaricaAllegatoAbilitato(boolean caricaAllegatoAbilitato) {
		this.caricaAllegatoAbilitato = caricaAllegatoAbilitato;
	}
	public void setEliminaAllegatoAbilitato(boolean eliminaAllegatoAbilitato) {
		this.eliminaAllegatoAbilitato = eliminaAllegatoAbilitato;
	}
	public boolean isCreaComunicazionePerCopiaAbilitato() {
		return creaComunicazionePerCopiaAbilitato;
	}
	public void setCreaComunicazionePerCopiaAbilitato(boolean creaComunicazionePerCopiaAbilitato) {
		this.creaComunicazionePerCopiaAbilitato = creaComunicazionePerCopiaAbilitato;
	}
	public List<CollegamentoDto> getCollegamenti() {
		return collegamenti;
	}

	public enum StatoDTO {
		IN_GESTIONE("In gestione"), TERMINATA("Terminata");

		private String label;

		StatoDTO(String label) {
			this.label = label;
		}

		public String getLabel() {
			return label;
		}

		public static StatoDTO fromLabel(String label) {
			for (StatoDTO dto : values()) {
				if (dto.getLabel().equals(label))
					return dto;
			}
			return null;
		}
	}

	public static class InvioComunicazioneDTO {
		private String flgTestProd;
		private Integer numRecordTest;
		private String pecDestinazioneTest;
		private String codEsito;
		public String getFlgTestProd() {
			return flgTestProd;
		}
		public Integer getNumRecordTest() {
			return numRecordTest;
		}
		public String getPecDestinazioneTest() {
			return pecDestinazioneTest;
		}
		public String getCodEsito() {
			return codEsito;
		}
		public void setFlgTestProd(String flgTestProd) {
			this.flgTestProd = flgTestProd;
		}
		public void setNumRecordTest(Integer numRecordTest) {
			this.numRecordTest = numRecordTest;
		}
		public void setPecDestinazioneTest(String pecDestinazioneTest) {
			this.pecDestinazioneTest = pecDestinazioneTest;
		}
		public void setCodEsito(String codEsito) {
			this.codEsito = codEsito;
		}
		
		
		
	} 
	
}
