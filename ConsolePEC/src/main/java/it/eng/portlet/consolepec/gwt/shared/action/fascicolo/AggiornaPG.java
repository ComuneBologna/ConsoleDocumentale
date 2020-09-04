package it.eng.portlet.consolepec.gwt.shared.action.fascicolo;

import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoGruppoProtocollato;

import java.util.List;


public class AggiornaPG extends LiferayPortletUnsecureActionImpl<AggiornaPGResult> {

	private String fascicoloPath;
	private List<ElementoGruppoProtocollato> protocollazioni;
	
	@SuppressWarnings("unused")
	private AggiornaPG() {
		// For serialization only
	}
	
	public AggiornaPG(String fascicoloPath, List<ElementoGruppoProtocollato> protocollazioni) {
		super();
		this.fascicoloPath = fascicoloPath;
		this.protocollazioni = protocollazioni;
	}

	public String getFascicoloPath() {
		return fascicoloPath;
	}

	public List<ElementoGruppoProtocollato> getProtocollazioni() {
		return protocollazioni;
	}

	
}
