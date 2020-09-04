package it.eng.portlet.consolepec.gwt.shared.action;

public class GestionePresaInCaricoFascicoloAction extends LiferayPortletUnsecureActionImpl<GestionePresaInCaricoFascicoloActionResult> {

	private String clientID;

	public GestionePresaInCaricoFascicoloAction() {
	}

	public String getClientID() {
		return clientID;
	}

	public void setClientID(String clientID) {
		this.clientID = clientID;
	}
}
