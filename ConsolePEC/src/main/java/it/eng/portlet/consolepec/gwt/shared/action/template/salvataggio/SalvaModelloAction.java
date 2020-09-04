package it.eng.portlet.consolepec.gwt.shared.action.template.salvataggio;

import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;
import it.eng.portlet.consolepec.gwt.shared.model.BaseTemplateDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class SalvaModelloAction extends LiferayPortletUnsecureActionImpl<SalvaModelloResult>{
	
	private BaseTemplateDTO modello;
	
	@Getter
	@Setter
	private String pathModello;
	
	public <T extends BaseTemplateDTO> void setModello(T modello) {
		this.modello = modello;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends BaseTemplateDTO> T getModello() {
		return (T) modello;
	}
	
}
