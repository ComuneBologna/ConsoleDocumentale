package it.eng.portlet.consolepec.gwt.shared.action.pec;

import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;
import it.eng.portlet.consolepec.gwt.shared.model.TipologiaCaricamento;

public class CaricaPraticaEmailInAction extends LiferayPortletUnsecureActionImpl<CaricaPraticaEmailInActionResult> {

	private String clientID;
	private TipologiaCaricamento tipologiaCaricamento;

	@SuppressWarnings("unused")
	private CaricaPraticaEmailInAction() {
		// For serialization only
	}

	public CaricaPraticaEmailInAction(String clientID, TipologiaCaricamento tipologiaCaricamento) {
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
