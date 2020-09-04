package it.eng.portlet.consolepec.gwt.shared.action.rubrica;

import it.eng.cobo.consolepec.commons.rubrica.Anagrafica;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;

import com.gwtplatform.dispatch.shared.Result;

/**
 * @author GiacomoFM
 * @since 18/set/2017
 */
@NoArgsConstructor
public class RicercaAnagraficheResult implements Result {

	private static final long serialVersionUID = -3232478105651950904L;

	@Getter private List<Anagrafica> anagrafiche = new ArrayList<>();

	@Getter private String msgError;
	@Getter private boolean error = false;
	
	public RicercaAnagraficheResult(List<Anagrafica> anagrafiche) {
		this.anagrafiche = anagrafiche;
	}
	
	public RicercaAnagraficheResult(String msgError) {
		super();
		this.error = true;
		this.msgError = msgError;
	}
}
