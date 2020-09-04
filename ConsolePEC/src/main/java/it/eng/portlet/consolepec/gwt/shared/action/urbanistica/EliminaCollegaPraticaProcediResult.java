package it.eng.portlet.consolepec.gwt.shared.action.urbanistica;

import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.gwtplatform.dispatch.shared.Result;

/**
 * @author GiacomoFM
 * @since 08/nov/2017
 */
@NoArgsConstructor
public class EliminaCollegaPraticaProcediResult implements Result {

	private static final long serialVersionUID = -6662255738345087840L;

	@Getter private PraticaDTO praticaDTO;

	@Getter private String msgError;
	@Getter private boolean error = false;

	public EliminaCollegaPraticaProcediResult(PraticaDTO praticaDTO) {
		super();
		this.praticaDTO = praticaDTO;
	}

	public EliminaCollegaPraticaProcediResult(String msgError) {
		super();
		this.error = true;
		this.msgError = msgError;
	}

}
