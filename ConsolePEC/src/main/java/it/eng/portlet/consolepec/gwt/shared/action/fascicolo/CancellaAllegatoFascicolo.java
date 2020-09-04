package it.eng.portlet.consolepec.gwt.shared.action.fascicolo;

import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;

import java.util.Set;

public class CancellaAllegatoFascicolo extends LiferayPortletUnsecureActionImpl<CancellaAllegatoFascicoloResult> {

	private String clientID;
	private Set<AllegatoDTO> allegati;
	@SuppressWarnings("unused")
	private CancellaAllegatoFascicolo() {
		// For serialization only
	}

	public CancellaAllegatoFascicolo(String clientID) {
		this.clientID = clientID;
	}

	public String getClientID() {
		return clientID;
	}

	public Set<AllegatoDTO> getAllegati() {
		return allegati;
	}

	public void setAllegati(Set<AllegatoDTO> allegati) {
		this.allegati = allegati;
	}


}
