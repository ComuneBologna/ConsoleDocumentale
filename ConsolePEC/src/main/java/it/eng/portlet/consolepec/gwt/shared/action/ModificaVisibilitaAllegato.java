package it.eng.portlet.consolepec.gwt.shared.action;

import it.eng.portlet.consolepec.gwt.shared.model.GruppoVisibilita;

import java.util.TreeSet;

public class ModificaVisibilitaAllegato extends LiferayPortletUnsecureActionImpl<ModificaVisibilitaAllegatoResult> {

	private TreeSet<GruppoVisibilita> gruppiVisibilita = new TreeSet<GruppoVisibilita>();
	private String clientID;
	private String nomeAllegato;

	public ModificaVisibilitaAllegato() {
	}

	public TreeSet<GruppoVisibilita> getGruppiVisibilita() {
		return gruppiVisibilita;
	}

	public void setGruppiVisibilita(TreeSet<GruppoVisibilita> gruppiVisibilita) {
		this.gruppiVisibilita = gruppiVisibilita;
	}

	public String getClientID() {
		return clientID;
	}

	public void setClientID(String clientID) {
		this.clientID = clientID;
	}

	public String getNomeAllegato() {
		return nomeAllegato;
	}

	public void setNomeAllegato(String nomeAllegato) {
		this.nomeAllegato = nomeAllegato;
	}

}
