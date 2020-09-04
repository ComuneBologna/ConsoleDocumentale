package it.eng.portlet.consolepec.gwt.shared.action.sara;

import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.gwtplatform.dispatch.shared.Result;

@NoArgsConstructor
public class EmissionePermessoResult implements Result {

	private static final long serialVersionUID = 1L;
	@Setter @Getter private String msgError;
	@Setter @Getter private boolean error = false;
	private FascicoloDTO fascicolo;
	
	public EmissionePermessoResult(FascicoloDTO fascicolo) {
		this.fascicolo = fascicolo;
	}

	public EmissionePermessoResult(String msgError) {
		super();
		this.error = true;
		this.msgError = msgError;
	}

	public FascicoloDTO getFascicolo(){
		return this.fascicolo;
	}

}