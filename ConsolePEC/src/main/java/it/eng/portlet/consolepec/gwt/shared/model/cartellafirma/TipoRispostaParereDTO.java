package it.eng.portlet.consolepec.gwt.shared.model.cartellafirma;

/**
 *
 * @author biagiot
 *
 */
public enum TipoRispostaParereDTO {
	RISPOSTA_POSITIVA("Risposta positiva"),

	RISPOSTA_NEGATIVA("Risposta negativa"),

	RISPOSTA_POSITIVA_CON_PRESCRIZIONI("Risposta positiva con prescrizioni"),

	RISPOSTA_SOSPESA("Risposta sospesa"),

	RISPOSTA_RIFIUTATA("Risposta rifiutata");

	private final String label;

	TipoRispostaParereDTO(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public static TipoRispostaParereDTO fromLabel(String label) {
		for(TipoRispostaParereDTO t : TipoRispostaParereDTO.values())
			if (t.getLabel().equalsIgnoreCase(label))
				return t;

		return null;
	}
}
