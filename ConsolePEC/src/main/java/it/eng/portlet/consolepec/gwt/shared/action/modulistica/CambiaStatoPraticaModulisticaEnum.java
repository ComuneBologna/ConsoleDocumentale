package it.eng.portlet.consolepec.gwt.shared.action.modulistica;

import it.eng.portlet.consolepec.gwt.shared.model.PraticaModulisticaDTO.StatoDTO;

import com.google.gwt.user.client.rpc.IsSerializable;

public enum CambiaStatoPraticaModulisticaEnum implements IsSerializable {
	ARCHIVIA, RIPORTAINGESTIONE, ELIMINATA, RILASCIA_IN_CARICO;

	public static CambiaStatoPraticaModulisticaEnum translator(StatoDTO statoDTO) {

		switch (statoDTO) {
		case IN_GESTIONE:
			return RIPORTAINGESTIONE;
		case ARCHIVIATA:
			return ARCHIVIA;
		case ELIMINATA:
			return ELIMINATA;
		default:
			throw new IllegalStateException();
		}

	}
};
