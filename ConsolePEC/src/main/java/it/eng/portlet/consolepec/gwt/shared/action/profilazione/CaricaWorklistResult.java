package it.eng.portlet.consolepec.gwt.shared.action.profilazione;

import java.util.HashMap;
import java.util.Map;

import com.gwtplatform.dispatch.shared.Result;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaWorklist;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaWorklist.Counter;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public class CaricaWorklistResult implements Result {
	
	private static final long serialVersionUID = 1L;
	
	@Getter
	private Map<AnagraficaWorklist, Counter> worklist = new HashMap<AnagraficaWorklist, Counter>();
	
	@Getter
	private String errorMessage;
	
	@Getter
	private boolean error;
	
	public CaricaWorklistResult(Map<AnagraficaWorklist, Counter> worklist) {
		this.worklist = worklist;
		this.error = false;
		this.errorMessage = null;
	}
	
	public CaricaWorklistResult(String errorMessage) {
		this.error = true;
		this.errorMessage = errorMessage;
	}
}
