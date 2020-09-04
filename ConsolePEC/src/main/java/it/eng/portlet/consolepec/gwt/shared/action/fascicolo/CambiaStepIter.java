package it.eng.portlet.consolepec.gwt.shared.action.fascicolo;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaFascicolo.StepIter;
import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;


public class CambiaStepIter extends LiferayPortletUnsecureActionImpl<CambiaStepIterResult> {

	private String fascicoloPath;
	private StepIter stepIter;

	@SuppressWarnings("unused")
	private CambiaStepIter() {
		// For serialization only
	}

	public CambiaStepIter(String fascicoloPath, StepIter stepIterDto) {
		super();
		this.fascicoloPath = fascicoloPath;
		this.stepIter = stepIterDto;
	}

	public String getFascicoloPath() {
		return fascicoloPath;
	}

	public StepIter getStepIter() {
		return stepIter;
	}

	
}
