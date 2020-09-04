package it.eng.portlet.consolepec.gwt.shared.action.template;

import java.util.Map;

public class CreaPdfDaTemplateAction extends CreaGenericoDaTemplate<CreaPdfDaTemplateActionResult> {
	
	private String nomeFile;

	protected CreaPdfDaTemplateAction() {
		super();
	}

	public CreaPdfDaTemplateAction(String pathFascicolo, String pathTemplate, Map<String, String> valori, String nomeFile) {
		super(pathFascicolo, pathTemplate, valori);
		this.nomeFile = nomeFile;
	}

	public String getNomeFile() {
		return nomeFile;
	}
}
