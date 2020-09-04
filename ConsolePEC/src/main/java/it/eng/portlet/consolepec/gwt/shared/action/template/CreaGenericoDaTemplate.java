package it.eng.portlet.consolepec.gwt.shared.action.template;

import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;

import java.util.Map;

public abstract class CreaGenericoDaTemplate<T extends CreaGenericoDaTemplateResult> extends LiferayPortletUnsecureActionImpl<T> {

	private String pathFascicolo;
	private String pathTemplate;
	private Map<String, String> valori;

	protected CreaGenericoDaTemplate() {
		// For serialization only
	}

	public CreaGenericoDaTemplate(String pathFascicolo, String pathTemplate, Map<String, String> valori) {
		super();
		this.pathFascicolo = pathFascicolo;
		this.pathTemplate = pathTemplate;
		this.valori = valori;
	}

	public String getPathFascicolo() {
		return pathFascicolo;
	}

	public String getPathTemplate() {
		return pathTemplate;
	}

	public Map<String, String> getValori() {
		return valori;
	}
}
