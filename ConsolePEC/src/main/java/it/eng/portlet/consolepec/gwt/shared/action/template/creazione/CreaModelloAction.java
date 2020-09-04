package it.eng.portlet.consolepec.gwt.shared.action.template.creazione;

import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;
import it.eng.portlet.consolepec.gwt.shared.model.BaseTemplateDTO;
import lombok.Getter;
import lombok.Setter;

public class CreaModelloAction extends LiferayPortletUnsecureActionImpl<CreaModelloResult>{
	
	private BaseTemplateDTO modello;
	
	@Getter
	@Setter
	private String idDocumentale;
	
	public <T extends BaseTemplateDTO> void setModello(T modello) {
		this.modello = modello;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends BaseTemplateDTO> T getModello() {
		return (T) modello;
	}
}
