package it.eng.portlet.consolepec.gwt.shared.action;

import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;

public class ModificaOperatore extends LiferayPortletUnsecureActionImpl<ModificaOperatoreResult> {
	
	private PraticaDTO pratica;
	private String operatore;
	
	protected ModificaOperatore() {
		super();
	}

	public ModificaOperatore(PraticaDTO pratica, String operatore) {
		super();
		this.pratica = pratica;
		this.operatore = operatore;
	}
	
	public PraticaDTO getPratica() {
		return pratica;
	}
	
	public String getOperatore() {
		return operatore;
	}
	
}
