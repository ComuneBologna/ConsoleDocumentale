package it.eng.portlet.consolepec.gwt.server.fascicolo;

import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Stato;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.StatoDTO;

/**
 * Fornisce metodi per la traduzione da {@link StatoDTO} a {@link Stato} e viceversa
 * 
 * @author pluttero
 * 
 */
public class StatiFascicoloTranslator {
	/**
	 * Tradue un {@link Stato} in {@link StatoDTO}
	 * 
	 * @param stato
	 * @return
	 */
	public static StatoDTO getStatoDTOFromStato(Stato stato) {
		switch (stato) {
		case ARCHIVIATO:
			return StatoDTO.ARCHIVIATO;
		case IN_GESTIONE:
			return StatoDTO.IN_GESTIONE;
		case IN_AFFISSIONE:
			return StatoDTO.IN_AFFISSIONE;
		case IN_VISIONE:
			return StatoDTO.IN_VISIONE;
		case ANNULLATO:
			return StatoDTO.ANNULLATO;
		case DA_INOLTRARE_ESTERNO:
			return StatoDTO.DA_INOLTRARE_ESTERNO;
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
		case ARCHIVIATO:
			return Stato.ARCHIVIATO;
		case IN_GESTIONE:
			return Stato.IN_GESTIONE;
		default:
			throw new IllegalArgumentException();
		}
	}

}
