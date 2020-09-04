package it.eng.portlet.consolepec.gwt.shared.action.pec;

import java.util.Set;

import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;

public class CambiaStatoPecInAction extends LiferayPortletUnsecureActionImpl<CambiaStatoPecInActionResult> {
	public enum Azione {
		ARCHIVIA, ARCHIVIAMASSIVA, ELIMINA, ELIMINAMASSIVO, RIPORTAINGESTIONE, RIPORTAINGESTIONEMASSIVO, RILASCIA_IN_CARICO;
	}

	private Azione tipoAzione;
	private Set<String> ids;
	private String id;

	@SuppressWarnings("unused")
	private CambiaStatoPecInAction() {

	}

	public CambiaStatoPecInAction(Set<String> ids, Azione azione) {
		this.ids = ids;
		this.tipoAzione = azione;
	}

	public CambiaStatoPecInAction(String id, Azione azione) {
		this.id = id;
		this.tipoAzione = azione;
	}

	public CambiaStatoPecInAction(Azione azione) {
		this.tipoAzione = azione;
	}

	public Azione getTipoAzione() {
		return tipoAzione;
	}

	public Set<String> getIds() {
		return ids;
	}

	public void setIds(Set<String> ids) {
		this.ids = ids;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
