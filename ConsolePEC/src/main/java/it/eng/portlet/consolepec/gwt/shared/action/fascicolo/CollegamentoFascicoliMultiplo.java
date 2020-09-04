package it.eng.portlet.consolepec.gwt.shared.action.fascicolo;

import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;

import java.util.ArrayList;
import java.util.List;

public class CollegamentoFascicoliMultiplo extends LiferayPortletUnsecureActionImpl<CollegamentoFascicoliMultiploResult> {

	private List<CollegamentoFascicoli> collegamentoFascicoliList = new ArrayList<CollegamentoFascicoli>();

	public CollegamentoFascicoliMultiplo() {
	}

	public List<CollegamentoFascicoli> getCollegamentoFascicoliList() {
		return collegamentoFascicoliList;
	}

	public void setCollegamentoFascicoliList(List<CollegamentoFascicoli> collegamentoFascicoliList) {
		this.collegamentoFascicoliList = collegamentoFascicoliList;
	}

}
