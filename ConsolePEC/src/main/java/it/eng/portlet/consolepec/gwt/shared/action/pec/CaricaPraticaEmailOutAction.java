package it.eng.portlet.consolepec.gwt.shared.action.pec;

import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;
import it.eng.portlet.consolepec.gwt.shared.model.TipologiaCaricamento;

public class CaricaPraticaEmailOutAction extends LiferayPortletUnsecureActionImpl<CaricaPraticaEmailOutActionResult> {

	private String clientId;
	private TipologiaCaricamento tipologiaCaricamento;

	protected CaricaPraticaEmailOutAction() {
		// For serialization only
	}

	public CaricaPraticaEmailOutAction(String clientID, TipologiaCaricamento tipologiaCaricamento) {
		this.clientId = clientID;
		this.tipologiaCaricamento = tipologiaCaricamento;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public TipologiaCaricamento getTipologiaCaricamento() {
		return tipologiaCaricamento;
	}

	public void setTipopologiaCaricamento(TipologiaCaricamento tipologiaCaricamento) {
		this.tipologiaCaricamento = tipologiaCaricamento;
	}

}
