package it.eng.portlet.consolepec.gwt.shared.action;

import it.eng.portlet.consolepec.gwt.shared.action.firma.CredenzialiFirma;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO.TipologiaFirma;

public class FirmaAllegatoPecOutBozzaAction extends LiferayPortletUnsecureActionImpl<FirmaAllegatoPecOutBozzaActionResult> {

	private String clientID;
	private CredenzialiFirma credenzialiFirma;
	private AllegatoDTO[] allegati;
	private TipologiaFirma tipologiaFirma;

	public FirmaAllegatoPecOutBozzaAction() {

	}

	public FirmaAllegatoPecOutBozzaAction(String clientID, AllegatoDTO[] allegati) {
		this.clientID = clientID;
		this.allegati = allegati;
	}

	public String getClientID() {
		return clientID;
	}

	public void setClientID(String clientID) {
		this.clientID = clientID;
	}

	public AllegatoDTO[] getAllegati() {
		return allegati;
	}

	public void setAllegati(AllegatoDTO[] allegati) {
		this.allegati = allegati;
	}

	public CredenzialiFirma getCredenzialiFirma() {
		return credenzialiFirma;
	}

	public void setCredenzialiFirma(CredenzialiFirma credenzialiFirma) {
		this.credenzialiFirma = credenzialiFirma;
	}
	
	public TipologiaFirma getTipologiaFirma() {
		return tipologiaFirma;
	}

	public void setTipologiaFirma(TipologiaFirma tipologiaFirma) {
		this.tipologiaFirma = tipologiaFirma;
	}

}
