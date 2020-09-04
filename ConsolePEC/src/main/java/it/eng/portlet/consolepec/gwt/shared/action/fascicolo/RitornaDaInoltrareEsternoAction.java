package it.eng.portlet.consolepec.gwt.shared.action.fascicolo;

import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;


public class RitornaDaInoltrareEsternoAction extends LiferayPortletUnsecureActionImpl<RitornaDaInoltrareEsternoResult> {

	private String clientID;

	@SuppressWarnings("unused")
	private RitornaDaInoltrareEsternoAction() {
		// For serialization only
	}

	public RitornaDaInoltrareEsternoAction(String clientID) {
		super();
		this.clientID = clientID;
	}

	public String getClientID() {
		return clientID;
	}

}
