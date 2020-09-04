package it.eng.portlet.consolepec.gwt.shared.action.fascicolo;

import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;
import it.eng.portlet.consolepec.gwt.shared.action.firma.CredenzialiFirma;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO.TipologiaFirma;

public class FirmaAllegatoFascicoloAction extends LiferayPortletUnsecureActionImpl<FirmaAllegatoFascicoloResult> {

	private String clientID;
	private CredenzialiFirma credenzialiFirma;
	private AllegatoDTO[] allegati;
	private TipologiaFirma tipologiaFirma;

	@SuppressWarnings("unused")
	private FirmaAllegatoFascicoloAction() {
		// For serialization only
	}

	public FirmaAllegatoFascicoloAction(String clientID, AllegatoDTO[] allegati) {
		this.clientID = clientID;
		this.allegati = allegati;
	}

	public String getClientID() {
		return clientID;
	}

	public AllegatoDTO[] getAllegati() {
		return allegati;
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
