package it.eng.portlet.consolepec.spring.bean.gestionepratiche;

import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CambiaStatoFascicolo;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CambiaStatoFascicoloResult;

public interface AggiornaStatoFascicolo {

	public CambiaStatoFascicoloResult cambiaStato(CambiaStatoFascicolo cambiaStatoFascicolo);
}
