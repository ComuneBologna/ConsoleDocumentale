package it.eng.portlet.consolepec.gwt.shared.model;

import com.google.gwt.user.client.rpc.IsSerializable;

public class PecInDTO extends PecDTO implements IsSerializable {

	/* dettagli PEC */
	private String dataOraArrivo;
	private StatoDTO stato;
	/* Elenco campi per la gestione dei tasti attivi */
	private boolean archiviabile, eliminabile, isCreaFascicoloAbilitato, isRiassegnaAbilitato, isAgganciaFascicoloAbilitato, isRiportaInGestioneAbilitato, riportaInLetturaAbilitato,
			importaElettoraleAbilitato, annullaElettoraleAbilitato, modificaOperatoreAbilitato;
	private boolean inoltratoDaEProtocollo;

	public enum StatoDTO {

		// comuni
		IN_GESTIONE("In gestione"), ARCHIVIATA("Archiviata"), NOTIFICATA("Notificata"), ELIMINATA("Eliminata"),
		// specifici delle pecin
		RESPINTA("Respinta"), RICONSEGNATA("Riconsegnata"), SCARTATA("Scartata");

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

	public boolean isAgganciaFascicoloAbilitato() {

		return isAgganciaFascicoloAbilitato;
	}

	public void setAgganciaFascicoloAbilitato(boolean isAgganciaFascicoloAbilitato) {

		this.isAgganciaFascicoloAbilitato = isAgganciaFascicoloAbilitato;
	}

	public boolean isRiassegnaAbilitato() {

		return isRiassegnaAbilitato;
	}

	public void setRiassegnaAbilitato(boolean isRiassegnaAbilitato) {

		this.isRiassegnaAbilitato = isRiassegnaAbilitato;
	}

	public boolean isCreaFascicoloAbilitato() {

		return isCreaFascicoloAbilitato;
	}

	public void setCreaFascicoloAbilitato(boolean isCreaFascicoloAbilitato) {

		this.isCreaFascicoloAbilitato = isCreaFascicoloAbilitato;
	}

	protected PecInDTO() {
		// serialization
		super();
	}

	public PecInDTO(String alfrescoPath) {
		super(alfrescoPath);
	}

	public boolean isArchiviabile() {
		return archiviabile;
	}

	public void setArchiviabile(boolean archiviabile) {
		this.archiviabile = archiviabile;
	}

	public boolean isEliminabile() {
		return eliminabile;
	}

	public void setEliminabile(boolean eliminabile) {
		this.eliminabile = eliminabile;
	}

	public boolean isRiportaInGestioneAbilitato() {
		return isRiportaInGestioneAbilitato;
	}

	public void setRiportaInGestioneAbilitato(boolean isRiportaInGestioneAbilitato) {
		this.isRiportaInGestioneAbilitato = isRiportaInGestioneAbilitato;
	}

	public String getDataOraArrivo() {
		return dataOraArrivo;
	}

	public void setDataOraArrivo(String dataOraArrivo) {
		this.dataOraArrivo = dataOraArrivo;
	}

	public StatoDTO getStato() {
		return stato;
	}

	public void setStato(StatoDTO stato) {
		this.stato = stato;
	}

	public boolean isInoltratoDaEProtocollo() {
		return inoltratoDaEProtocollo;
	}

	public void setInoltratoDaEProtocollo(boolean inoltratoDaEProtocollo) {
		this.inoltratoDaEProtocollo = inoltratoDaEProtocollo;
	}

	public boolean isRiportaInLetturaAbilitato() {
		return riportaInLetturaAbilitato;
	}

	public void setRiportaInLetturaAbilitato(boolean riportaInLetturaAbilitato) {
		this.riportaInLetturaAbilitato = riportaInLetturaAbilitato;
	}

	public boolean isImportaElettoraleAbilitato() {
		return importaElettoraleAbilitato;
	}

	public void setImportaElettoraleAbilitato(boolean importaElettoraleAbilitato) {
		this.importaElettoraleAbilitato = importaElettoraleAbilitato;
	}

	public boolean isAnnullaElettoraleAbilitato() {
		return annullaElettoraleAbilitato;
	}

	public void setAnnullaElettoraleAbilitato(boolean annullaElettoraleAbilitato) {
		this.annullaElettoraleAbilitato = annullaElettoraleAbilitato;
	}

	public boolean isModificaOperatoreAbilitato() {
		return modificaOperatoreAbilitato;
	}

	public void setModificaOperatoreAbilitato(boolean modificaOperatoreAbilitato) {
		this.modificaOperatoreAbilitato = modificaOperatoreAbilitato;
	}

}
