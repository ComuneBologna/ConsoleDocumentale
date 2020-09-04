package it.eng.portlet.consolepec.gwt.shared.action.fascicolo;

import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;

public class SganciaPecIn extends LiferayPortletUnsecureActionImpl<SganciaPecInResult> {

	private String fascicoloPath;
	private String pecInPath;
	
	public SganciaPecIn() {
	}
	
	public SganciaPecIn(String fascicoloPath, String pecInPath) {
		super();
		this.fascicoloPath = fascicoloPath;
		this.pecInPath = pecInPath;
	}
	
	public String getFascicoloPath() {
		return fascicoloPath;
	}

	public void setFascicoloPath(String fascicoloPath) {
		this.fascicoloPath = fascicoloPath;
	}

	public String getPecInPath() {
		return pecInPath;
	}

	public void setPecInPath(String pecInPath) {
		this.pecInPath = pecInPath;
	}

	
}
