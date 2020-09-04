package it.eng.portlet.consolepec.gwt.shared.action.fascicolo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.gwtplatform.dispatch.shared.Result;

@NoArgsConstructor
public class GeneraTitoloFascicoloResult implements Result {
	
	private static final long serialVersionUID = 1L;
	
	@Getter
	@Setter
	private String errorMessage;
	
	@Getter
	@Setter
	private boolean error = false;
	
	@Getter
	@Setter
	private String titolo;
}
