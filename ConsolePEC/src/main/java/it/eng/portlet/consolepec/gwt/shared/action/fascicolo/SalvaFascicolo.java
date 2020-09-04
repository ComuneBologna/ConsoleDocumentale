package it.eng.portlet.consolepec.gwt.shared.action.fascicolo;

import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;

public class SalvaFascicolo extends LiferayPortletUnsecureActionImpl<SalvaFascicoloResult> {

	private FascicoloDTO fascicolo;

	@SuppressWarnings("unused")
	private SalvaFascicolo() {
		// For serialization only
	}

	public SalvaFascicolo(FascicoloDTO fascicolo) {
		this.fascicolo = fascicolo;
	}

	public FascicoloDTO getFascicolo() {
		return fascicolo;
	}
}
