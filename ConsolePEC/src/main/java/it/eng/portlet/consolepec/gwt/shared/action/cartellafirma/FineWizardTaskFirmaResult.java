package it.eng.portlet.consolepec.gwt.shared.action.cartellafirma;

import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import com.gwtplatform.dispatch.shared.Result;

/**
 *
 * @author biagiot
 *
 */

@Getter
@Setter
public class FineWizardTaskFirmaResult implements Result {

	private static final long serialVersionUID = 1L;
	private List<FascicoloDTO> fascicoli = new ArrayList<FascicoloDTO>();
	private String errorMessage;
	private boolean error;

	protected FineWizardTaskFirmaResult() {
		//
	}

	public FineWizardTaskFirmaResult(List<FascicoloDTO> fasicoli) {
		this.fascicoli = fasicoli;
		this.error = false;
		this.errorMessage = null;
	}

	public FineWizardTaskFirmaResult(String errorMessage) {
		this.error = true;
		this.errorMessage = errorMessage;
	}
}
