package it.eng.portlet.consolepec.gwt.shared.action.assegna;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.profilazione.Settore;
import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import com.gwtplatform.dispatch.shared.Result;

public abstract class RiassegnaAction<R extends Result> extends LiferayPortletUnsecureActionImpl<R>{

	@Getter
	@Setter
	private Settore settore;
	
	@Getter
	@Setter
	private boolean ricordaScelta;
	
	@Getter
	private List<String> indirizziNotifica = new ArrayList<String>();
	
	@Getter
	@Setter
	private AnagraficaRuolo anagraficaRuolo;
}
