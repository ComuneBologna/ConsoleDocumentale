package it.eng.portlet.consolepec.gwt.shared.action.pec;

import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoPECRiferimento;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoPraticaModulisticaRiferimento;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author biagiot
 *
 */
public class ComposizioneFascicoliCollegatiAction extends LiferayPortletUnsecureActionImpl<ComposizioneFascicoliCollegatiActionResult> {

	List<ElementoPECRiferimento> pecInComposizione;
	List<ElementoPraticaModulisticaRiferimento> praticheModulisticaInComposizione;
	
	public ComposizioneFascicoliCollegatiAction() {
		this.pecInComposizione = new ArrayList<ElementoPECRiferimento>();
		this.praticheModulisticaInComposizione = new ArrayList<ElementoPraticaModulisticaRiferimento>();
	}
	
	public List<ElementoPECRiferimento> getPecInComposizione() {
		return pecInComposizione;
	}

	public List<ElementoPraticaModulisticaRiferimento> getPraticheModulisticaInComposizione() {
		return praticheModulisticaInComposizione;
	}

}
