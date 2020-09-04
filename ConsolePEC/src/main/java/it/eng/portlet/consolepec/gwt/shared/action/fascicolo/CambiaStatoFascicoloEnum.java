package it.eng.portlet.consolepec.gwt.shared.action.fascicolo;

import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.StatoDTO;

import com.google.gwt.user.client.rpc.IsSerializable;

public enum CambiaStatoFascicoloEnum implements IsSerializable {
	ARCHIVIA, RIPORTAINGESTIONE, ELIMINATO, IN_AFFISSIONE, IN_VISIONE, ANNULLA_AMIANTO;

	public static CambiaStatoFascicoloEnum translator(StatoDTO statoDTO) {

		switch (statoDTO) {
		case IN_GESTIONE:
			return RIPORTAINGESTIONE;
		case ARCHIVIATO:
			return ARCHIVIA;
		case ANNULLATO:
			return ANNULLA_AMIANTO;
		case IN_AFFISSIONE:
			return IN_AFFISSIONE;
		case IN_VISIONE:
			return IN_VISIONE;
		default:
			throw new IllegalStateException();
		}

	}
};
