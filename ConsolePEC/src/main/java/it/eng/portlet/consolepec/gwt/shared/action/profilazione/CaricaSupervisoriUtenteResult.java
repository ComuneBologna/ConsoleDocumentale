package it.eng.portlet.consolepec.gwt.shared.action.profilazione;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class CaricaSupervisoriUtenteResult implements Result {

	private static final long serialVersionUID = 1L;

	private Map<String, List<AnagraficaRuolo>> ruoliSuperutenti = new HashMap<String, List<AnagraficaRuolo>>();
	private Map<String, List<AnagraficaRuolo>> ruoliSupervisoriMatriceVisibilita = new HashMap<String, List<AnagraficaRuolo>>();
	private boolean error;
	private String errorMessage;

	public CaricaSupervisoriUtenteResult(Map<String, List<AnagraficaRuolo>> ruoliSuperutenti, Map<String, List<AnagraficaRuolo>> ruoliSupervisoriMatriceVisibilita) {
		this.ruoliSuperutenti = ruoliSuperutenti;
		this.ruoliSupervisoriMatriceVisibilita = ruoliSupervisoriMatriceVisibilita;
		this.error = false;
		this.errorMessage = null;
	}

	public CaricaSupervisoriUtenteResult(String errorMessage) {
		this.error = true;
		this.errorMessage = errorMessage;
	}
}
