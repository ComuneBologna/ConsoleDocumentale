package it.eng.portlet.consolepec.gwt.shared.action.template;

import it.eng.portlet.consolepec.gwt.shared.model.BaseTemplateDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.gwtplatform.dispatch.shared.Result;

@NoArgsConstructor
public class CaricaModelloResult implements Result {

	private static final long serialVersionUID = 1L;

	private BaseTemplateDTO modello;
	
	@Getter
	private String errorMessage;
	
	@Getter
	private boolean error = false;
	
	public CaricaModelloResult(String errorMessage) {
		this.errorMessage = errorMessage;
		this.error = true;
	}
	
	public <T extends BaseTemplateDTO> void setModello(T modello) {
		this.modello = modello;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends BaseTemplateDTO> T getModello() {
		return (T) modello;
	}
}
