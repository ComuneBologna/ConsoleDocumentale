package it.eng.portlet.consolepec.gwt.shared.model;

public enum TipologiaCaricamento {

	CARICA, RICARICA;

	public static TipologiaCaricamento getTipologiaCaricamento(boolean isOpen) {
		if (isOpen)
			return CARICA;
		return RICARICA;
	}

}
