package it.eng.portlet.consolepec.gwt.shared.action.configurazioni;

import java.util.List;

import com.gwtplatform.dispatch.shared.Result;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author biagiot
 * 
 */
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CaricaMatriceVisibilitaPraticaResult implements Result {

	private static final long serialVersionUID = 1L;

	private List<AnagraficaRuolo> ruoli;
	private boolean error;
	private String errorMessage;

	public CaricaMatriceVisibilitaPraticaResult(List<AnagraficaRuolo> ruoli) {
		this.ruoli = ruoli;
		this.error = false;
		this.errorMessage = null;
	}

	public CaricaMatriceVisibilitaPraticaResult(String errorMessage) {
		this.error = true;
		this.errorMessage = errorMessage;
	}
}
