package it.eng.portlet.consolepec.gwt.shared.action.pec;

import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;

import java.util.Set;

public class CancellaAllegatoPecOut extends LiferayPortletUnsecureActionImpl<CancellaAllegatoPecOutResult> {


	private Set<AllegatoDTO> allegati;
	private String clientID;

	public String getClientID() {
		return clientID;
	}

	@SuppressWarnings("unused")
	private CancellaAllegatoPecOut() {

		// For serialization only
	}

	public CancellaAllegatoPecOut(String clientID, Set<AllegatoDTO> allegati) {

		this.clientID = clientID;
		this.allegati = allegati;
	}


	public Set<AllegatoDTO> getAllegati() {
		return allegati;
	}

	public void setAllegati(Set<AllegatoDTO> allegati) {
		this.allegati = allegati;
	}
}
