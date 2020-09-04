package it.eng.portlet.consolepec.gwt.shared.model.cartellafirma;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
*
*
* @author biagiot
*
*/
public enum StatoDestinatarioTaskFirmaDTO {

	IN_APPROVAZIONE(
			StatoTaskFirmaDTO.IN_APPROVAZIONE.getLabel(),
			Arrays.asList(TipoPropostaTaskFirmaDTO.values())),

	APPROVATO(
			StatoTaskFirmaDTO.APPROVATO.getLabel(),
			Arrays.asList(TipoPropostaTaskFirmaDTO.FIRMA, TipoPropostaTaskFirmaDTO.VISTO)),

	DINIEGATO(
			StatoTaskFirmaDTO.DINIEGATO.getLabel(),
			Arrays.asList(TipoPropostaTaskFirmaDTO.FIRMA, TipoPropostaTaskFirmaDTO.VISTO)),

	RISPOSTA_POSITIVA(
			TipoRispostaParereDTO.RISPOSTA_POSITIVA.getLabel(),
			Arrays.asList(TipoPropostaTaskFirmaDTO.PARERE)),

	RISPOSTA_NEGATIVA(
			TipoRispostaParereDTO.RISPOSTA_NEGATIVA.getLabel(),
			Arrays.asList(TipoPropostaTaskFirmaDTO.PARERE)),

	RISPOSTA_POSITIVA_CON_PRESCRIZIONI(
			TipoRispostaParereDTO.RISPOSTA_POSITIVA_CON_PRESCRIZIONI.getLabel(),
			Arrays.asList(TipoPropostaTaskFirmaDTO.PARERE)),

	RISPOSTA_SOSPESA(
			TipoRispostaParereDTO.RISPOSTA_SOSPESA.getLabel(),
			Arrays.asList(TipoPropostaTaskFirmaDTO.PARERE)),

	RISPOSTA_RIFIUTATA(
			TipoRispostaParereDTO.RISPOSTA_RIFIUTATA.getLabel(),
			Arrays.asList(TipoPropostaTaskFirmaDTO.PARERE));


	private final String label;
	private final List<TipoPropostaTaskFirmaDTO> tipiProposta;

	StatoDestinatarioTaskFirmaDTO(String label, List<TipoPropostaTaskFirmaDTO> tipiProposta) {
		this.label = label;
		this.tipiProposta = tipiProposta;
	}

	public String getLabel() {
		return label;
	}

	public List<TipoPropostaTaskFirmaDTO> getTipiProposta() {
		return tipiProposta;
	}

	public static List<String> getLabels() {
		List<String> labels = new ArrayList<String>();

		for (StatoDestinatarioTaskFirmaDTO s : StatoDestinatarioTaskFirmaDTO.values())
			labels.add(s.getLabel());

		return labels;
	}

	public static List<StatoDestinatarioTaskFirmaDTO> fromTipoProposta(TipoPropostaTaskFirmaDTO tipoProposta) {
		List<StatoDestinatarioTaskFirmaDTO> result = new ArrayList<StatoDestinatarioTaskFirmaDTO>();

		for (StatoDestinatarioTaskFirmaDTO to : StatoDestinatarioTaskFirmaDTO.values())
			if (to.getTipiProposta().contains(tipoProposta))
				result.add(to);

		return result;
	}

	public static StatoDestinatarioTaskFirmaDTO fromLabel(String label) {
		for (StatoDestinatarioTaskFirmaDTO to : StatoDestinatarioTaskFirmaDTO.values())
			if (to.getLabel().equalsIgnoreCase(label))
				return to;

		return null;
	}
}
