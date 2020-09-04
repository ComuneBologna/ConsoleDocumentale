package it.eng.portlet.consolepec.gwt.client.operazioni.template;

import it.eng.portlet.consolepec.gwt.client.widget.template.AbstractCorpoTemplateWidget;
import it.eng.portlet.consolepec.gwt.shared.model.CampoTemplateDTO;

/**
 *
 * @author biagiot
 *
 */
public class EliminaCampoCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, CampoTemplateDTO> {

	private AbstractCorpoTemplateWidget<?> corpoTemplateWidget;

	public EliminaCampoCommand(AbstractCorpoTemplateWidget<?> corpoTemplateWidget) {
		this.corpoTemplateWidget = corpoTemplateWidget;
	}

	@Override
	public Void exe(CampoTemplateDTO o) {
		corpoTemplateWidget.removeCampo(o);
		return null;
	}
}
