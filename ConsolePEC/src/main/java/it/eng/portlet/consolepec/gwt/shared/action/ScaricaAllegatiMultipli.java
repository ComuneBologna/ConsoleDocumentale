package it.eng.portlet.consolepec.gwt.shared.action;

import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;

import java.util.Set;

public class ScaricaAllegatiMultipli extends LiferayPortletUnsecureActionImpl<ScaricaAllegatiMultipliResult> {

	private String clientID;
	private Set<AllegatoDTO> selezionati;

	@SuppressWarnings("unused")
	private ScaricaAllegatiMultipli() {

		// For serialization only
	}

	public ScaricaAllegatiMultipli(String clientID, Set<AllegatoDTO> selezionati) {
		this.clientID = clientID;
		this.selezionati = selezionati;
	}

	public Set<AllegatoDTO> getSelezionati() {
		return selezionati;
	}

	public String getClientID() {
		return clientID;
	}

}
