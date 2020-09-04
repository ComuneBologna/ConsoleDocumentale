package it.eng.portlet.consolepec.gwt.shared.action.fascicolo;

import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;

import java.util.Set;
import java.util.TreeSet;

public class CercaProcedimentiCollegati extends LiferayPortletUnsecureActionImpl<CercaProcedimentiCollegatiResult> {

	private Set<String> ids = new TreeSet<String>();

	@SuppressWarnings("unused")
	private CercaProcedimentiCollegati() {
		// For serialization only
	}

	public CercaProcedimentiCollegati(Set<String> ids) {
		this.ids.clear();
		this.ids.addAll(ids);
	}

	public Set<String> getIds() {
		return ids;
	}
}
