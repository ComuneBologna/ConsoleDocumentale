package it.eng.portlet.consolepec.gwt.shared.action;

import it.eng.portlet.consolepec.gwt.shared.model.TipologiaCaricamento;

public class CaricaPraticaAction extends LiferayPortletUnsecureActionImpl<CaricaPraticaActionResult> {

	private String clientID;
	private TipologiaCaricamento tipologiaCaricamento;

	@SuppressWarnings("unused")
	private CaricaPraticaAction() {
		// For serialization only
	}

	public CaricaPraticaAction(String clientID, TipologiaCaricamento tipologiaCaricamento) {
		this.clientID = clientID;
		this.tipologiaCaricamento = tipologiaCaricamento;
	}

	public String getClientID() {
		return clientID;
	}

	public void setClientID(String clientID) {
		this.clientID = clientID;
	}

	public TipologiaCaricamento getTipologiaCaricamento() {
		return tipologiaCaricamento;
	}

	public void setTipologiaCaricamento(TipologiaCaricamento tipologiaCaricamento) {
		this.tipologiaCaricamento = tipologiaCaricamento;
	}
}
