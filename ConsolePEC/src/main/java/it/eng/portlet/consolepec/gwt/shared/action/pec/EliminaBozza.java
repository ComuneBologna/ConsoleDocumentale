package it.eng.portlet.consolepec.gwt.shared.action.pec;

import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;

public class EliminaBozza extends LiferayPortletUnsecureActionImpl<EliminaBozzaResult> {

	private String clientIDFascicolo;
	private String clientID;

	@SuppressWarnings("unused")
	private EliminaBozza() {

		// For serialization only
	}

	public EliminaBozza(String clientID, String clientIDFascicolo) {

		this.clientID = clientID;
		this.clientIDFascicolo = clientIDFascicolo;
	}

	public String getClientID() {
		return clientID;
	}

	public String getClientIDFascicolo() {
		return clientIDFascicolo;
	}

}
