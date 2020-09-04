package it.eng.portlet.consolepec.gwt.server.pec;

import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Stato;
import it.eng.portlet.consolepec.gwt.shared.model.PecOutDTO.StatoDTO;

/**
 * Fornisce metodi per la traduzione da {@link StatoDTO} a {@link Stato} e viceversa
 *
 * @author pluttero
 *
 */
public class StatiPecTranslator {
	/**
	 * Tradue un {@link Stato} in {@link StatoDTO}
	 *
	 * @param stato
	 * @return
	 */
	public static it.eng.portlet.consolepec.gwt.shared.model.PecOutDTO.StatoDTO getStatoPecOutDTOFromStato(Stato stato) {
		switch (stato) {
		case IN_GESTIONE:
			return StatoDTO.IN_GESTIONE;
		case ARCHIVIATA:
			return StatoDTO.ARCHIVIATA;
		case ELIMINATA:
			return StatoDTO.ELIMINATA;
		case BOZZA:
			return StatoDTO.BOZZA;
		case DA_INVIARE:
			return StatoDTO.DA_INVIARE;
		case NON_INVIATA:
			return StatoDTO.NON_INVIATA;
		case INATTESADIPRESAINCARICO:
			return StatoDTO.INATTESADIPRESAINCARICO;
		case PRESAINCARICO:
			return StatoDTO.PRESAINCARICO;
		case MANCATA_ACCETTAZIONE:
			return StatoDTO.MANCATA_ACCETTAZIONE;
		case MANCATA_CONSEGNA:
			return StatoDTO.MANCATA_CONSEGNA;
		case MANCATA_CONSEGNA_IN_REINOLTRO:
			return StatoDTO.MANCATA_CONSEGNA_IN_REINOLTRO;
		case PREAVVISO_MANCATA_CONSEGNA:
			return StatoDTO.PREAVVISO_MANCATA_CONSEGNA;
		case PARZIALMENTECONSEGNATA:
			return StatoDTO.PARZIALMENTECONSEGNATA;
		case CONSEGNA_SENZA_ACCETTAZIONE:
			return StatoDTO.CONSEGNATA;
		case CONSEGNATA:
			return StatoDTO.CONSEGNATA;

		default:
			throw new IllegalArgumentException("Valore di stato non supportato da stato dto");

		}

	}

	/**
	 * Tradue un {@link Stato} in {@link it.eng.portlet.consolepec.gwt.shared.model.PecInDTO.StatoDTO}
	 *
	 * @param stato
	 * @return
	 */
	public static it.eng.portlet.consolepec.gwt.shared.model.PecInDTO.StatoDTO getStatoPecInDTOFromStato(Stato stato) {

		switch (stato) {
		case ARCHIVIATA:
			return it.eng.portlet.consolepec.gwt.shared.model.PecInDTO.StatoDTO.ARCHIVIATA;
		case NOTIFICATA:
			return it.eng.portlet.consolepec.gwt.shared.model.PecInDTO.StatoDTO.NOTIFICATA;
		case ELIMINATA:
			return it.eng.portlet.consolepec.gwt.shared.model.PecInDTO.StatoDTO.ELIMINATA;
		case IN_GESTIONE:
			return it.eng.portlet.consolepec.gwt.shared.model.PecInDTO.StatoDTO.IN_GESTIONE;
		case RESPINTA:
			return it.eng.portlet.consolepec.gwt.shared.model.PecInDTO.StatoDTO.RESPINTA;
		case RICONSEGNATA:
			return it.eng.portlet.consolepec.gwt.shared.model.PecInDTO.StatoDTO.RICONSEGNATA;
		case SCARTATA:
			return it.eng.portlet.consolepec.gwt.shared.model.PecInDTO.StatoDTO.SCARTATA;
		default:
			throw new IllegalArgumentException("Valore di stato non supportato da statodto");
		}

	}
}
