package it.eng.portlet.consolepec.gwt.shared.model.cartellafirma;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * Rappresenta lo stato di un task di firma.
 *
 * @author biagiot
 *
 */
public enum StatoTaskFirmaDTO {
	EVASO(
			"Evaso",
			TipoStatoTaskFirmaDTO.EVASO,
			new ArrayList<TipoPropostaTaskFirmaDTO>()),

	IN_APPROVAZIONE(
			"In approvazione",
			TipoStatoTaskFirmaDTO.IN_APPROVAZIONE,
			Arrays.asList(TipoPropostaTaskFirmaDTO.values())),

	RITIRATO(
			"Ritirato",
			TipoStatoTaskFirmaDTO.CONCLUSO,
			Arrays.asList(TipoPropostaTaskFirmaDTO.values())),

	APPROVATO(
			"Approvato",
			TipoStatoTaskFirmaDTO.CONCLUSO,
			Arrays.asList(TipoPropostaTaskFirmaDTO.FIRMA, TipoPropostaTaskFirmaDTO.VISTO)),

	DINIEGATO(
			"Diniegato",
			TipoStatoTaskFirmaDTO.CONCLUSO,
			Arrays.asList(TipoPropostaTaskFirmaDTO.FIRMA, TipoPropostaTaskFirmaDTO.VISTO)),

	PARERE_RICEVUTO (
			"Parere ricevuto",
			TipoStatoTaskFirmaDTO.CONCLUSO,
			Arrays.asList(TipoPropostaTaskFirmaDTO.PARERE));


	private final String label;
	private final TipoStatoTaskFirmaDTO tipologiaStato;
	private final List<TipoPropostaTaskFirmaDTO> tipiProposta;

	StatoTaskFirmaDTO(String label, TipoStatoTaskFirmaDTO tipologiaStato, List<TipoPropostaTaskFirmaDTO> tipiProposta) {
		this.label = label;
		this.tipologiaStato = tipologiaStato;
		this.tipiProposta = tipiProposta;
	}

	public String getLabel() {
		return label;
	}

	public TipoStatoTaskFirmaDTO getTipologiaStato() {
		return tipologiaStato;
	}

	public List<TipoPropostaTaskFirmaDTO> getTipiProposta() {
		return tipiProposta;
	}

	public static StatoTaskFirmaDTO fromLabel(String label) {
		for (StatoTaskFirmaDTO stf : StatoTaskFirmaDTO.values())
			if (stf.getLabel().equals(label))
				return stf;

		return null;
	}

	public static List<StatoTaskFirmaDTO> fromTipologiaStato(TipoStatoTaskFirmaDTO tipologiaStato) {
		List<StatoTaskFirmaDTO> result = new ArrayList<StatoTaskFirmaDTO>();

		for (StatoTaskFirmaDTO stf : StatoTaskFirmaDTO.values())
			if (stf.getTipologiaStato().equals(tipologiaStato))
				result.add(stf);

		return result;
	}

	public static List<StatoTaskFirmaDTO> fromTipoProposta(TipoPropostaTaskFirmaDTO tipoProposta) {
		List<StatoTaskFirmaDTO> result = new ArrayList<StatoTaskFirmaDTO>();

		for (StatoTaskFirmaDTO stf : StatoTaskFirmaDTO.values())
			if (stf.getTipiProposta().contains(tipoProposta))
				result.add(stf);

		return result;
	}

	public static List<StatoTaskFirmaDTO> fromTipoStatoETipoProposta(TipoStatoTaskFirmaDTO tipologiaStato, TipoPropostaTaskFirmaDTO tipoProposta) {
		List<StatoTaskFirmaDTO> stati = new ArrayList<StatoTaskFirmaDTO>();

		for (StatoTaskFirmaDTO st : StatoTaskFirmaDTO.values()) {
			if(st.getTipologiaStato().equals(tipologiaStato) && st.getTipiProposta().contains(tipoProposta))
				stati.add(st);
		}

		return stati;
	}

	public static List<StatoTaskFirmaDTO> intersect(TipoStatoTaskFirmaDTO tipologiaStato, TipoPropostaTaskFirmaDTO tipoProposta) {
		List<StatoTaskFirmaDTO> statiByTipoStato = null;
		List<StatoTaskFirmaDTO> statiByTipoProposta = null;

		if (tipoProposta == null) {
			statiByTipoProposta = new ArrayList<StatoTaskFirmaDTO>(Arrays.asList(StatoTaskFirmaDTO.values()));

		} else {
			statiByTipoProposta = fromTipoProposta(tipoProposta);
		}

		if (tipologiaStato == null) {
			statiByTipoStato = new ArrayList<StatoTaskFirmaDTO>(Arrays.asList(StatoTaskFirmaDTO.values()));

		} else {
			statiByTipoStato = fromTipologiaStato(tipologiaStato);
		}

		statiByTipoProposta.retainAll(statiByTipoStato);
		return statiByTipoProposta;
	}
}
