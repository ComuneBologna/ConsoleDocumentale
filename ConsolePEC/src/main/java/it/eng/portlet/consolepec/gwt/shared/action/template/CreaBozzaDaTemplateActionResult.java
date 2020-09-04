package it.eng.portlet.consolepec.gwt.shared.action.template;

import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PecOutDTO;

public class CreaBozzaDaTemplateActionResult extends CreaGenericoDaTemplateResult {

	private static final long serialVersionUID = 1L;

	private PecOutDTO bozza;

	protected CreaBozzaDaTemplateActionResult() {
		super();
	}

	public CreaBozzaDaTemplateActionResult(boolean isError, String errorMsg) {
		super(isError, errorMsg);
	}

	public CreaBozzaDaTemplateActionResult(FascicoloDTO fascicolo, PecOutDTO bozza) {
		super(fascicolo);
		this.bozza = bozza;
	}

	public PecOutDTO getBozza() {
		return bozza;
	}

}
