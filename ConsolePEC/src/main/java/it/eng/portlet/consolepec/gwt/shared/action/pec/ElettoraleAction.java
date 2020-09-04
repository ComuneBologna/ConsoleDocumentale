package it.eng.portlet.consolepec.gwt.shared.action.pec;

import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;

public class ElettoraleAction extends LiferayPortletUnsecureActionImpl<ElettoraleResult> {
	
	private String clientID;
	private OperazioneElettorale operazioneElettorale;
	
	public enum OperazioneElettorale {
		IMPORTA, ANNULLA
	}
	
	@SuppressWarnings("unused")
	private ElettoraleAction() {
		// For serialization only
	}
	
	public ElettoraleAction(String clientID, OperazioneElettorale operazioneElettorale) {
		this.clientID = clientID;
		this.operazioneElettorale =  operazioneElettorale;
	}
	
	public String getClientID() {
		return clientID;
	}
	
	public OperazioneElettorale getOperazioneElettorale() {
		return operazioneElettorale;
	}
}
