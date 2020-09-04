package it.eng.portlet.consolepec.gwt.shared.action;


public class ComposizioneFascicolo extends LiferayPortletUnsecureActionImpl<ComposizioneFascicoloResult> {

	private String numPG;
	private Integer annoPG;

	@SuppressWarnings("unused")
	private ComposizioneFascicolo() {
		// For serialization only
	}

	public ComposizioneFascicolo(String numPG, Integer annoPG) {
		this.numPG = numPG;
		this.annoPG = annoPG;
	}

	public String getNumPG() {
		return numPG;
	}

	public Integer getAnnoPG() {
		return annoPG;
	}
}
