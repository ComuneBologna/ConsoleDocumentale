package it.eng.portlet.consolepec.gwt.shared.action.fascicolo;

import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;

import java.util.Set;

public class CambiaStatoFascicolo extends LiferayPortletUnsecureActionImpl<CambiaStatoFascicoloResult> {

	private String clientID;
	private CambiaStatoFascicoloEnum stato;
	private Set<String> ids;
	
	@SuppressWarnings("unused")
	private CambiaStatoFascicolo() {
		// For serialization only
	}

	public CambiaStatoFascicolo(String clientID, CambiaStatoFascicoloEnum nuovoStato) {
		this.clientID = clientID;
		this.setStato(nuovoStato);
	}
	public CambiaStatoFascicolo(Set<String> ids,CambiaStatoFascicoloEnum nuovoStato){
		this.ids = ids;
		this.setStato(nuovoStato);
	}
	public String getClientID() {
		return clientID;
	}

	public CambiaStatoFascicoloEnum getStato() {
		return stato;
	}

	public void setStato(CambiaStatoFascicoloEnum stato) {
		this.stato = stato;
	}

	
	public Set<String> getIds() {
	
		return ids;
	}

	
	public void setIds(Set<String> ids) {
	
		this.ids = ids;
	}
	public void setClientID(String clientID) {
		this.clientID = clientID;
	}
}
