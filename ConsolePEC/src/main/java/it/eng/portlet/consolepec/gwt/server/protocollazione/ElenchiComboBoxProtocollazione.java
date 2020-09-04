package it.eng.portlet.consolepec.gwt.server.protocollazione;

import it.eng.portlet.consolepec.gwt.shared.dto.ComboProtocollazioneDto;

import java.util.Set;

public interface ElenchiComboBoxProtocollazione {
	
	public Set<ComboProtocollazioneDto> getComboTitoli();

	public Set<ComboProtocollazioneDto> getComboRubriche(String titolo);

	public Set<ComboProtocollazioneDto> getComboSezioni(String titolo, String rubrica);

	public Set<ComboProtocollazioneDto> getComboTipologiaDocumenti();

	public Set<ComboProtocollazioneDto> getComboCodiciInterni();

	public Set<ComboProtocollazioneDto> getComboCodiciEsterni();

}
