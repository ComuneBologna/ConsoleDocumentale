package it.eng.portlet.consolepec.gwt.shared.action.urbanistica;

import it.eng.cobo.consolepec.commons.urbanistica.PraticaProcedi;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;

import com.gwtplatform.dispatch.shared.Result;

/**
 * @author GiacomoFM
 * @since 06/nov/2017
 */
@NoArgsConstructor
public class RicercaPraticaProcediResult implements Result {

	private static final long serialVersionUID = 728211829827758010L;

	@Getter private List<PraticaProcedi> listaPraticaProcedi;
	@Getter private long maxResult;

	@Getter private String msgError;
	@Getter private boolean error = false;

	public RicercaPraticaProcediResult(List<PraticaProcedi> listaPraticaProcedi, long maxResult) {
		super();
		this.listaPraticaProcedi = listaPraticaProcedi;
		this.maxResult = maxResult;
	}

	public RicercaPraticaProcediResult(String msgError) {
		super();
		this.error = true;
		this.msgError = msgError;
	}
}
