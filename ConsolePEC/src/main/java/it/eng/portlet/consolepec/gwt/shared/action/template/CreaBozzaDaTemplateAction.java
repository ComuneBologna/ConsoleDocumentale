package it.eng.portlet.consolepec.gwt.shared.action.template;

import java.util.Map;

public class CreaBozzaDaTemplateAction extends CreaGenericoDaTemplate<CreaBozzaDaTemplateActionResult> {
	
	protected CreaBozzaDaTemplateAction() {
		super();
	}

	public CreaBozzaDaTemplateAction(String pathFascicolo, String pathTemplate, Map<String, String> valori) {
		super(pathFascicolo, pathTemplate, valori);
	}

}
