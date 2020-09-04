package it.eng.portlet.consolepec.gwt.shared.action.fascicolo;

import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;

import java.util.ArrayList;
import java.util.List;

public class CondivisioneFascicoloMultipla extends LiferayPortletUnsecureActionImpl<CondivisioneFascicoloMultiplaResult> {
	
	private List<CondivisioneFascicolo> condivisioni = new ArrayList<CondivisioneFascicolo>();

	public CondivisioneFascicoloMultipla() {
	}

	public List<CondivisioneFascicolo> getCondivisioni() {
		return condivisioni;
	}

}
