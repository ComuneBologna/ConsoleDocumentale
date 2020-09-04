package it.eng.portlet.consolepec.gwt.server.fascicolo;

import it.eng.consolepec.xmlplugin.pratica.modulistica.DatiModulistica.Stato;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaModulisticaDTO.StatoDTO;

/**
 * Fornisce metodi per la traduzione da {@link StatoDTO} a {@link Stato} e viceversa
 * 
 * @author pluttero
 * 
 */
public class StatiPraticaModulisticaTranslator {
	/**
	 * Tradue un {@link Stato} in {@link StatoDTO}
	 * 
	 * @param stato
	 * @return
	 */
	public static StatoDTO getStatoDTOFromStato(Stato stato) {
		switch (stato) {
		case ARCHIVIATA:
			return StatoDTO.ARCHIVIATA;
		case IN_GESTIONE:
			return StatoDTO.IN_GESTIONE;
		default:
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Tradue un {@link StatoDTO} in {@link Stato}
	 * 
	 * @param stato
	 * @return
	 */
	public static Stato getStatoFromStatoDTO(StatoDTO dto) {
		switch (dto) {
		case ARCHIVIATA:
			return Stato.ARCHIVIATA;
		case IN_GESTIONE:
			return Stato.IN_GESTIONE;
		default:
			throw new IllegalArgumentException();
		}
	}

}
