package it.eng.portlet.consolepec.gwt.shared.action.fascicolo;

import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;

public class IterProcedimento extends LiferayPortletUnsecureActionImpl<IterProcedimentoResult> {

	private String fascicoloPath;
	
	private String annoPgCapofila;
	private String numPgCapofila;

	public IterProcedimento() {
	}

	public String getFascicoloPath() {
		return fascicoloPath;
	}

	public void setFascicoloPath(String fascicoloPath) {
		this.fascicoloPath = fascicoloPath;
	}

	public String getAnnoPgCapofila() {
		return annoPgCapofila;
	}

	public void setAnnoPgCapofila(String annoPgCapofila) {
		this.annoPgCapofila = annoPgCapofila;
	}

	public String getNumPgCapofila() {
		return numPgCapofila;
	}

	public void setNumPgCapofila(String numPgCapofila) {
		this.numPgCapofila = numPgCapofila;
	}

}
