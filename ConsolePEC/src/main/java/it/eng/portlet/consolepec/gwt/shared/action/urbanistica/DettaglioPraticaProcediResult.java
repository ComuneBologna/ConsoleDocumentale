package it.eng.portlet.consolepec.gwt.shared.action.urbanistica;

import it.eng.cobo.consolepec.commons.urbanistica.PraticaProcedi;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;

import com.gwtplatform.dispatch.shared.Result;

/**
 * @author GiacomoFM
 * @since 07/nov/2017
 */
@NoArgsConstructor
public class DettaglioPraticaProcediResult implements Result {

	private static final long serialVersionUID = 6537111223910059858L;

	@Getter private List<PraticaProcedi> praticheProcedi = new ArrayList<PraticaProcedi>();
	@Getter private String msgError;
	@Getter private boolean error = false;

	
	public DettaglioPraticaProcediResult(List<PraticaProcedi> praticheProcedi) {
		super();
		this.praticheProcedi = praticheProcedi;
	}

	public DettaglioPraticaProcediResult(String msgError) {
		super();
		this.error = true;
		this.msgError = msgError;
	}
}
