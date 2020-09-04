package it.eng.portlet.consolepec.gwt.shared.action.fascicolo;

import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;

public class EliminaFascicolo extends LiferayPortletUnsecureActionImpl<EliminaFascicoloResult> {

	private String pathFascicolo;
	
	public EliminaFascicolo(String pathFascicolo){
		this.pathFascicolo = pathFascicolo;
	}

	@SuppressWarnings("unused")
	private EliminaFascicolo() {
		// For serialization only
	}

	public String getPathFascicolo() {
		return pathFascicolo;
	}

	public void setPathFascicolo(String pathFascicolo) {
		this.pathFascicolo = pathFascicolo;
	}

}
