package it.eng.portlet.consolepec.gwt.shared.action.pec;

import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;

public class InviaMailAction
	extends LiferayPortletUnsecureActionImpl<InviaMailActionResult> {

	private String clientID;
	
	@SuppressWarnings("unused")
	private InviaMailAction() {
	
		// For serialization only
	}

	public InviaMailAction(String clientID) {
	
		this.clientID = clientID;
	}

	public String getClientID() {
	
		return clientID;
	}

	
}
