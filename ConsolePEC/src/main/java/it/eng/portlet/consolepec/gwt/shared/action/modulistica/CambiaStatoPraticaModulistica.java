package it.eng.portlet.consolepec.gwt.shared.action.modulistica;

import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;

import java.util.Set;

public class CambiaStatoPraticaModulistica extends LiferayPortletUnsecureActionImpl<CambiaStatoPraticaModulisticaResult> {

	private CambiaStatoPraticaModulisticaEnum stato;
	private Set<String> ids;
	
	@SuppressWarnings("unused")
	private CambiaStatoPraticaModulistica() {
		// For serialization only
	}

	public CambiaStatoPraticaModulistica(Set<String> ids,CambiaStatoPraticaModulisticaEnum nuovoStato){
		this.ids = ids;
		this.setStato(nuovoStato);
	}
	
	public CambiaStatoPraticaModulisticaEnum getStato() {
		return stato;
	}

	public void setStato(CambiaStatoPraticaModulisticaEnum stato) {
		this.stato = stato;
	}

	
	public Set<String> getIds() {
	
		return ids;
	}

	
	public void setIds(Set<String> ids) {
	
		this.ids = ids;
	}
	
}
