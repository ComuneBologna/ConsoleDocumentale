package it.eng.portlet.consolepec.gwt.shared.action;

import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import lombok.Getter;

public class GetDettagliAllegato extends LiferayPortletUnsecureActionImpl<GetDettagliAllegatoResult> {

	@Getter
	private String clientID;

	@Getter
	private AllegatoDTO allegato;

	@SuppressWarnings("unused")
	private GetDettagliAllegato() {
		// For serialization only
	}

	public GetDettagliAllegato(String clientID, AllegatoDTO allegato) {
		this.clientID = clientID;
		this.allegato = allegato;
	}
}
