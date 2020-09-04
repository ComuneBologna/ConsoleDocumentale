package it.eng.portlet.consolepec.gwt.shared.action.fascicolo;

import it.eng.portlet.consolepec.gwt.shared.action.Stampa;

public class StampaRicevuteConsegna extends Stampa<StampaRicevuteConsegnaResult> {

	private String praticaPath;
	
	public StampaRicevuteConsegna() {
		// se no gwt si incazza per la serializzazione
	}

	public StampaRicevuteConsegna(String praticaPath) {
		super();
		this.praticaPath = praticaPath;
	}

	public String getPraticaPath() {
		return praticaPath;
	}
}
