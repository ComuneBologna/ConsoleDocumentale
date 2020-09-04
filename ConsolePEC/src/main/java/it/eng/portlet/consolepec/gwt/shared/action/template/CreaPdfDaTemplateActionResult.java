package it.eng.portlet.consolepec.gwt.shared.action.template;

import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;

public class CreaPdfDaTemplateActionResult extends CreaGenericoDaTemplateResult {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected CreaPdfDaTemplateActionResult() {
		super();
	}

	public CreaPdfDaTemplateActionResult(FascicoloDTO fascicolo) {
		super(fascicolo);
	}

	public CreaPdfDaTemplateActionResult(boolean isError, String errorMsg) {
		super(isError, errorMsg);
	}
}
