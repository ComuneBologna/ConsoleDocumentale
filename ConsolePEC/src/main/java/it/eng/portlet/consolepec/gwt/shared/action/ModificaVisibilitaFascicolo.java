package it.eng.portlet.consolepec.gwt.shared.action;

import it.eng.portlet.consolepec.gwt.shared.model.GruppoVisibilita;

import java.util.TreeSet;

public class ModificaVisibilitaFascicolo extends LiferayPortletUnsecureActionImpl<ModificaVisibilitaFascicoloResult> {

	private String clientID;
	private TreeSet<GruppoVisibilita> gruppiVisibilita = new TreeSet<GruppoVisibilita>();
	
	public ModificaVisibilitaFascicolo() {
	}

	public String getClientID() {
		return clientID;
	}

	public void setClientID(String clientID) {
		this.clientID = clientID;
	}

	public TreeSet<GruppoVisibilita> getGruppiVisibilita() {
		return gruppiVisibilita;
	}

	public void setGruppiVisibilita(TreeSet<GruppoVisibilita> gruppiVisibilita) {
		this.gruppiVisibilita = gruppiVisibilita;
	}

}
