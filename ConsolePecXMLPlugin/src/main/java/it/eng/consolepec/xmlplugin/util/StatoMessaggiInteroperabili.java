package it.eng.consolepec.xmlplugin.util;

public enum StatoMessaggiInteroperabili {
	
	ERRORE ("Errore"),
	VALIDO ("Valido"),
	NON_VALIDO ("Non valido");

	String label = null;

	StatoMessaggiInteroperabili(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

}
