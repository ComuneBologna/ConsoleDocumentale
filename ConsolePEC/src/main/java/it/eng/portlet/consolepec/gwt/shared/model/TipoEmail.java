package it.eng.portlet.consolepec.gwt.shared.model;

/**
 * Versione GWT di DatiEmail.TipoEmail
 * 
 * @author pluttero
 * 
 */
public enum TipoEmail {
	PEC("Pec"), NORMALE("Normale");
	private String label;

	TipoEmail(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public static TipoEmail fromLabel(String label) {
		for (TipoEmail dto : values()) {
			if (dto.getLabel().equals(label))
				return dto;
		}
		return null;
	}
}
