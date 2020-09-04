package it.eng.portlet.consolepec.gwt.shared.action.fascicolo;

import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;
import it.eng.portlet.consolepec.gwt.shared.model.TipologiaCaricamento;

public class CaricaPraticaFascicoloAction extends LiferayPortletUnsecureActionImpl<CaricaPraticaFascicoloActionResult> {

	private String clientID;
	private TipologiaCaricamento tipologiaCaricamento;

	@SuppressWarnings("unused")
	private CaricaPraticaFascicoloAction() {
		// For serialization only
	}

//	public CaricaPraticaFascicoloAction(String clientID) {
//		this.setClientID(clientID);
//	}
	
	public CaricaPraticaFascicoloAction(String clientID,TipologiaCaricamento tipologiaCaricamento) {
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
