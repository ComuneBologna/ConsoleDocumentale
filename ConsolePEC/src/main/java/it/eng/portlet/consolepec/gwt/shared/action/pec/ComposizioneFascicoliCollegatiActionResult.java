package it.eng.portlet.consolepec.gwt.shared.action.pec;

import it.eng.portlet.consolepec.gwt.shared.model.PecDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaModulisticaDTO;

import java.util.ArrayList;
import java.util.List;

import com.gwtplatform.dispatch.shared.Result;

/**
 * 
 * @author biagiot
 *
 */
public class ComposizioneFascicoliCollegatiActionResult implements Result {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7012825635725269627L;
	
	private List<PecDTO> pec;
	private List<PraticaModulisticaDTO> praticheModulistica;
	private int errorsPec;
	private int errorPraticheModulistica;
	
	public ComposizioneFascicoliCollegatiActionResult() {
		this.pec = new ArrayList<PecDTO>();
		this.praticheModulistica = new ArrayList<PraticaModulisticaDTO>();
	}
	
	public List<PecDTO> getPec() {
		return pec;
	}

	public List<PraticaModulisticaDTO> getPraticheModulistica() {
		return praticheModulistica;
	}

	public int getErrorsPec() {
		return errorsPec;
	}
	
	public int getErrorsPraticheModulistica() {
		return errorPraticheModulistica;
	}

	public void addErrorPec() {
		this.errorsPec++;
	}
	
	public void addErrorPraticheModulistica() {
		this.errorPraticheModulistica++;
	}
}
