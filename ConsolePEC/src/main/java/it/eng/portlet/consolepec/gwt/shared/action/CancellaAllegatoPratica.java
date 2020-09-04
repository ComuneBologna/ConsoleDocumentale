package it.eng.portlet.consolepec.gwt.shared.action;

import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;

import java.util.Set;

public class CancellaAllegatoPratica extends LiferayPortletUnsecureActionImpl<CancellaAllegatoPraticaResult> {


	private Set<AllegatoDTO> allegati;
	private String clientID;

	public String getClientID() {
		return clientID;
	}

	@SuppressWarnings("unused")
	private CancellaAllegatoPratica() {

		// For serialization only
	}

	public CancellaAllegatoPratica(String clientID, Set<AllegatoDTO> allegati) {

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
