package it.eng.portlet.consolepec.gwt.shared.model.cartellafirma;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Rappresenta il tipo di proposta che il proponente può effettuare
 *
 * @author biagiot
 *
 */
public enum TipoPropostaTaskFirmaDTO {
	FIRMA("Firma", false),
	VISTO("Visto", true),
	PARERE("Risposta/Parere", true);

	private final String label;
	private boolean allegatoProtocollatoEnabled;

	TipoPropostaTaskFirmaDTO (String label, boolean allegatoProtocollatoEnabled) {
		this.label = label;
		this.allegatoProtocollatoEnabled = allegatoProtocollatoEnabled;
	}

	public String getLabel() {
		return label;
	}

	public boolean isAllegatoProtocollatoEnabled() {
		return allegatoProtocollatoEnabled;
	}

	public static TipoPropostaTaskFirmaDTO fromLabel(String label) {
		for (TipoPropostaTaskFirmaDTO t : TipoPropostaTaskFirmaDTO.values()) {
			if (t.getLabel().equalsIgnoreCase(label))
				return t;
		}

		return null;
	}

	public static List<String> getLabels() {
		List<String> labels = new ArrayList<String>();

		for (TipoPropostaTaskFirmaDTO tipo : TipoPropostaTaskFirmaDTO.values()) {
			labels.add(tipo.getLabel());
		}

		return labels;
	}
}
