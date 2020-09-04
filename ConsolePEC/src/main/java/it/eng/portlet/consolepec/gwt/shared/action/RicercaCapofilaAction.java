package it.eng.portlet.consolepec.gwt.shared.action;

public class RicercaCapofilaAction extends LiferayPortletUnsecureActionImpl<RicercaCapofilaResult> {

	private String numeroPg;
	private int annoPg;

	public String getNumeroPg() {
		return numeroPg;
	}

	public void setNumeroPg(String numeroPg) {
		this.numeroPg = numeroPg;
	}

	public int getAnnoPg() {
		return annoPg;
	}

	public void setAnnoPg(int annoPg) {
		this.annoPg = annoPg;
	}

	public RicercaCapofilaAction() {
	}

}
