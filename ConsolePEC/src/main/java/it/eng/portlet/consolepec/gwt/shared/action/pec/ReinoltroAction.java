package it.eng.portlet.consolepec.gwt.shared.action.pec;

import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;

import java.util.ArrayList;
import java.util.List;

public class ReinoltroAction extends LiferayPortletUnsecureActionImpl<ReinoltroResult> {

	public ReinoltroAction() {
	}

	private String idMailOriginale;
	private List<String> destinatari = new ArrayList<String>();
	private List<String> copia = new ArrayList<String>();

	public String getIdMailOriginale() {
		return idMailOriginale;
	}

	public void setIdMailOriginale(String idMailOriginale) {
		this.idMailOriginale = idMailOriginale;
	}

	public List<String> getDestinatari() {
		return destinatari;
	}

	public void setDestinatari(List<String> destinatari) {
		this.destinatari = destinatari;
	}

	public List<String> getCopia() {
		return copia;
	}

	public void setCopia(List<String> copia) {
		this.copia = copia;
	}

}
