package it.eng.portlet.consolepec.gwt.shared.action.fascicolo;

import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;

public class RimozionePubblicazioneAllegati extends LiferayPortletUnsecureActionImpl<RimozionePubblicazioneAllegatiResult> {

	private String nomeAllegato;
	private String praticaPath;

	@SuppressWarnings("unused")
	private RimozionePubblicazioneAllegati() {
		// For serialization only
	}

	public RimozionePubblicazioneAllegati(String praticaPath, String nomeAllegato) {
		this.nomeAllegato = nomeAllegato;
		this.praticaPath = praticaPath;
	}

	public String getNomeAllegato() {
		return nomeAllegato;
	}

	public String getPraticaPath() {
		return praticaPath;
	}
}
