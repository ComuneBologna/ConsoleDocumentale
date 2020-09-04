package it.eng.portlet.consolepec.gwt.client.operazioni.pec;

import java.util.Set;

import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoElenco;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;

/**
 *
 * @author biagiot
 *
 */
public interface PecApiClient {

	void estraiEML(String pecPath, String praticaPath, PraticaCallback praticaCallback);

	void isEstrazioneEMLAbilitata(Set<ElementoElenco> pecSelezionate, BooleanCallback booleanCallback);

	void isProtocollazioneAbilitata(Set<ElementoElenco> pecSelezionate, BooleanCallback booleanCallback);

	public interface BooleanCallback {
		void onComplete(boolean result);

		void onError(String error);
	}

	public interface PraticaCallback {
		void onComplete(PraticaDTO praticaCallback);

		void onError(String error);
	}
}
