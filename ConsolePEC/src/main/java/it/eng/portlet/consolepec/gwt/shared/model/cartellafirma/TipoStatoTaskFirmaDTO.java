package it.eng.portlet.consolepec.gwt.shared.model.cartellafirma;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Stato finale task di firma, serve per racchiudere i vari stati di dettaglio in fase di ricerca
 *
 * @author biagiot
 *
 */
public enum TipoStatoTaskFirmaDTO {
	IN_APPROVAZIONE("In approvazione"),
	CONCLUSO("Concluso"),
	EVASO("Evaso");

	private final String label;

	TipoStatoTaskFirmaDTO(String value) {
		this.label = value;
	}

	public String getLabel() {
		return label;
	}

	public static TipoStatoTaskFirmaDTO fromValue(String value) {
		for (TipoStatoTaskFirmaDTO stato : TipoStatoTaskFirmaDTO.values()) {
			if (stato.getLabel().equalsIgnoreCase(value))
				return stato;
		}

		return null;
	}

	public static List<String> getLabels() {
		List<String> labels = new ArrayList<String>();

		for (TipoStatoTaskFirmaDTO stato : TipoStatoTaskFirmaDTO.values()) {
			labels.add(stato.getLabel());
		}

		return labels;
	}
}
