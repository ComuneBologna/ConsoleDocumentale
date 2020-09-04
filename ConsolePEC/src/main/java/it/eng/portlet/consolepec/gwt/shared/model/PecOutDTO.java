package it.eng.portlet.consolepec.gwt.shared.model;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Bean di una pec in uscita
 *
 * @author pluttero
 *
 */
public class PecOutDTO extends PecDTO implements IsSerializable {

	public enum StatoDTO {

		// comuni
		IN_GESTIONE("In gestione"), ARCHIVIATA("Archiviata"), NOTIFICATA("Notificata"), ELIMINATA("Eliminata"),
		// specifici delle pecout
		BOZZA("Bozza"), DA_INVIARE("In invio"), NON_INVIATA("Non inviata"), INVIATA("In invio"), // non più utilizzato, si può rimuovere
		INATTESADIPRESAINCARICO("Inviata"), PRESAINCARICO("Inviata"), MANCATA_ACCETTAZIONE("Mancata accettazione"), MANCATA_CONSEGNA("Mancata consegna"),
		MANCATA_CONSEGNA_IN_REINOLTRO("Mancata consegna in reinoltro"), PREAVVISO_MANCATA_CONSEGNA("Preavviso mancata consegna"), PARZIALMENTECONSEGNATA("Parzialmente consegnata"),
		CONSEGNATA("Consegnata");

		private String label;

		StatoDTO(String label) {
			this.label = label;
		}

		public String getLabel() {
			return label;
		}

		public static List<StatoDTO> fromLabel(String label) {
			List<StatoDTO> lista = new ArrayList<StatoDTO>();
			for (StatoDTO dto : values()) {
				if (dto.getLabel().equals(label))
					lista.add(dto);
			}
			return lista;
		}
	}

	public enum TipologiaInteroperabileDTO {
		ALLEGATI, EMAIL
	}

	private String dataInvio;
	private boolean inviata;
	private boolean azioniDisabilitate;
	private boolean reinoltro;
	private String messageIdReinoltro;
	private boolean reinoltrabile, hasRicevuteConsegna;
	private StatoDTO stato;
	private TipologiaInteroperabileDTO tipologiaInteroperabile;
	private String identificativoAllegatoPrincipale;
	private List<RicevuteErroreDTO> ricevuteErrore = new ArrayList<RicevuteErroreDTO>();
	private String firma;
	private boolean eliminaAllegatoAbilitato, firmaAllegatoAbilitato, eliminaAbilitato, inviaAbilitato, chiudiAbilitato, caricaAllegatoAbilitato, caricaAllegatoDaPraticaAbilitato;

	protected PecOutDTO() {
		super();
	}

	public PecOutDTO(String alfrescoPath) {
		super(alfrescoPath);
	}

	public String getDataInvio() {
		return dataInvio;
	}

	public void setDataInvio(String dataInvio) {
		this.dataInvio = dataInvio;
	}

	public boolean isInviata() {
		return inviata;
	}

	public void setInviata(boolean inviata) {
		this.inviata = inviata;
	}

	public StatoDTO getStato() {
		return stato;
	}

	public void setStato(StatoDTO stato) {
		this.stato = stato;
	}

	public boolean isAzioniDisabilitate() {
		return azioniDisabilitate;
	}

	public void setAzioniDisabilitate(boolean azioniDisabilitate) {
		this.azioniDisabilitate = azioniDisabilitate;
	}

	public boolean isReinoltro() {
		return reinoltro;
	}

	public void setReinoltro(boolean reinoltro) {
		this.reinoltro = reinoltro;
	}

	public String getMessageIdReinoltro() {
		return messageIdReinoltro;
	}

	public void setMessageIdReinoltro(String messageIdReinoltro) {
		this.messageIdReinoltro = messageIdReinoltro;
	}

	public boolean isReinoltrabile() {
		return reinoltrabile;
	}

	public void setReinoltrabile(boolean reinoltrabile) {
		this.reinoltrabile = reinoltrabile;
	}

	public boolean hasRicevuteConsegna() {
		return hasRicevuteConsegna;
	}

	public void setRicevuteConsegna(boolean hasRicevuteConsegna) {
		this.hasRicevuteConsegna = hasRicevuteConsegna;
	}

	public TipologiaInteroperabileDTO getTipologiaInteroperabile() {
		return tipologiaInteroperabile;
	}

	public void setTipologiaInteroperabile(TipologiaInteroperabileDTO tipologiaInteroperabile) {
		this.tipologiaInteroperabile = tipologiaInteroperabile;
	}

	public String getIdentificativoAllegatoPrincipale() {
		return identificativoAllegatoPrincipale;
	}

	public void setIdentificativoAllegatoPrincipale(String identificativoAllegatoPrincipale) {
		this.identificativoAllegatoPrincipale = identificativoAllegatoPrincipale;
	}

	public boolean isEliminaAllegatoAbilitato() {
		return eliminaAllegatoAbilitato;
	}

	public boolean isFirmaAllegatoAbilitato() {
		return firmaAllegatoAbilitato;
	}

	public boolean isEliminaAbilitato() {
		return eliminaAbilitato;
	}

	public boolean isInviaAbilitato() {
		return inviaAbilitato;
	}

	public boolean isChiudiAbilitato() {
		return chiudiAbilitato;
	}

	public boolean isCaricaAllegatoAbilitato() {
		return caricaAllegatoAbilitato;
	}

	public boolean isCaricaAllegatoDaPraticaAbilitato() {
		return caricaAllegatoDaPraticaAbilitato;
	}

	public void setEliminaAllegatoAbilitato(boolean eliminaAllegatoAbilitato) {
		this.eliminaAllegatoAbilitato = eliminaAllegatoAbilitato;
	}

	public void setFirmaAllegatoAbilitato(boolean firmaAllegatoAbilitato) {
		this.firmaAllegatoAbilitato = firmaAllegatoAbilitato;
	}

	public void setEliminaAbilitato(boolean eliminaAbilitato) {
		this.eliminaAbilitato = eliminaAbilitato;
	}

	public void setInviaAbilitato(boolean inviaAbilitato) {
		this.inviaAbilitato = inviaAbilitato;
	}

	public void setChiudiAbilitato(boolean chiudiAbilitato) {
		this.chiudiAbilitato = chiudiAbilitato;
	}

	public void setCaricaAllegatoAbilitato(boolean caricaAllegatoAbilitato) {
		this.caricaAllegatoAbilitato = caricaAllegatoAbilitato;
	}

	public void setCaricaAllegatoDaPraticaAbilitato(boolean caricaAllegatoDaPraticaAbilitato) {
		this.caricaAllegatoDaPraticaAbilitato = caricaAllegatoDaPraticaAbilitato;
	}

	public List<RicevuteErroreDTO> getRicevuteErrore() {
		return ricevuteErrore;
	}

	public String getFirma() {
		return firma;
	}

	public void setFirma(String firma) {
		this.firma = firma;
	}

}
