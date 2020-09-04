package it.eng.portlet.consolepec.gwt.shared.action.modulistica;

import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;
import it.eng.portlet.consolepec.gwt.shared.model.TipologiaCaricamento;

public class CaricaPraticaModulisticaAction extends LiferayPortletUnsecureActionImpl<CaricaPraticaModulisticaActionResult> {

	private String clientID;
	private TipologiaCaricamento tipologiaCaricamento;

	@SuppressWarnings("unused")
	private CaricaPraticaModulisticaAction() {
		// For serialization only
	}

	public CaricaPraticaModulisticaAction(String clientID,TipologiaCaricamento tipologiaCaricamento) {
		this.setClientID(clientID);
		this.setTipologiaCaricamento(tipologiaCaricamento);
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

	public void setTipologiaCaricamento(TipologiaCaricamento tipoCaricamento) {
		this.tipologiaCaricamento = tipoCaricamento;
	}

}
