package it.eng.portlet.consolepec.gwt.shared.action.fascicolo;


import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;

import java.util.List;


public class CambiaTipologiaAllegato extends LiferayPortletUnsecureActionImpl<CambiaTipologiaAllegatoResult> {

	private String fascicoloPath;
	private String tipologiaAllegato;
	private List<String> nomiAllegati;

	@SuppressWarnings("unused")
	private CambiaTipologiaAllegato() {
		// For serialization only
	}

	public CambiaTipologiaAllegato(String fascicoloPath, String tipologiaAllegato , List<String> nomiAllegati) {
		super();
		this.fascicoloPath = fascicoloPath;
		this.tipologiaAllegato = tipologiaAllegato;
		this.nomiAllegati = nomiAllegati;
	}

	public String getFascicoloPath() {
		return fascicoloPath;
	}

	public String getTipologiaAllegato() {
		return tipologiaAllegato;
	}

	public List<String> getNomiAllegati() {
		return nomiAllegati;
	}

	public void setNomiAllegati(List<String> nomiAllegati) {
		this.nomiAllegati = nomiAllegati;
	}

	
}
