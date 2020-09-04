package it.eng.portlet.consolepec.gwt.shared.action.pec;

import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;

public class AggiungiPraticaAFascicolo
	extends LiferayPortletUnsecureActionImpl<AggiungiPraticaAFascicoloResult> {

	private String idFascicolo;
	private String idPratica;

	@SuppressWarnings("unused")
	private AggiungiPraticaAFascicolo() {
	
		// For serialization only
	}

	public AggiungiPraticaAFascicolo(String idFascicolo, String idPratica) {
	
		this.idFascicolo = idFascicolo;
		this.idPratica = idPratica;
	}

	public String getIdFascicolo() {
	
		return idFascicolo;
	}

	public String getIdPratica() {
	
		return idPratica;
	}
}
