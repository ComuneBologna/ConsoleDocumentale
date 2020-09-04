package it.eng.portlet.consolepec.gwt.shared.action.template.eliminazione;

import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;

/**
 *
 * @author biagiot
 *
 */
public class EliminaTemplateAction extends LiferayPortletUnsecureActionImpl<EliminaTemplateResult> {

	private String pathTemplate;

	protected EliminaTemplateAction() {

	}

	public EliminaTemplateAction(String pathTemplate) {
		this.pathTemplate = pathTemplate;
	}

	public String getPathTemplate() {
		return pathTemplate;
	}

	public void setPathTemplate(String pathTemplate) {
		this.pathTemplate = pathTemplate;
	}
}

