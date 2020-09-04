package it.eng.portlet.consolepec.gwt.shared.action;


public class RiportaInLettura extends LiferayPortletUnsecureActionImpl<RiportaInLetturaResult> {

	private String clientID;

	@SuppressWarnings("unused")
	private RiportaInLettura() {
		// For serialization only
	}

	public RiportaInLettura(String clientID) {
		super();
		this.clientID = clientID;
	}

	public String getClientID() {
		return clientID;
	}

}
