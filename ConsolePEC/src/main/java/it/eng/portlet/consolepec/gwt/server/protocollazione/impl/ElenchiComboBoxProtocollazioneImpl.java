package it.eng.portlet.consolepec.gwt.server.protocollazione.impl;

import it.bologna.comune.spagic.combo.protocollazione.Combo;
import it.bologna.comune.spagic.combo.protocollazione.Combos;
import it.bologna.comune.spagic.combo.protocollazione.Row;
import it.eng.portlet.consolepec.gwt.server.protocollazione.ElenchiComboBoxProtocollazione;
import it.eng.portlet.consolepec.gwt.shared.dto.ComboProtocollazioneDto;
import it.eng.portlet.consolepec.spring.bean.titolazione.ComboBoxesTitolazione;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Strings;

public class ElenchiComboBoxProtocollazioneImpl implements ElenchiComboBoxProtocollazione {

	Logger logger = LoggerFactory.getLogger(getClass());

	private Set<ComboProtocollazioneDto> _titoli = new TreeSet<ComboProtocollazioneDto>();
	private Set<ComboProtocollazioneDto> _rubriche = new TreeSet<ComboProtocollazioneDto>();
	private Set<ComboProtocollazioneDto> _sezioni = new TreeSet<ComboProtocollazioneDto>();
	private Set<ComboProtocollazioneDto> _documenti = new TreeSet<ComboProtocollazioneDto>();
	private Set<ComboProtocollazioneDto> _comboInterni = new TreeSet<ComboProtocollazioneDto>();
	private Set<ComboProtocollazioneDto> _comboEsterni = new TreeSet<ComboProtocollazioneDto>();

	@Autowired
	ComboBoxesTitolazione comboBoxesTitolazione;

	public ElenchiComboBoxProtocollazioneImpl() {
	}

	private void fillDTO() {
		Combos combos = comboBoxesTitolazione.retrieveComboBoxes();
		for (Combo c : combos.getCombos()) {
			fillDtos(c);
		}
	}

	@Override
	public Set<ComboProtocollazioneDto> getComboTitoli() {
		fillDTO();
		return getFilteredDto(null, null, _titoli);
	}

	@Override
	public Set<ComboProtocollazioneDto> getComboRubriche(String titolo) {
		fillDTO();
		return getFilteredDto(titolo, null, _rubriche);
	}

	@Override
	public Set<ComboProtocollazioneDto> getComboSezioni(String titolo, String rubrica) {
		fillDTO();
		return getFilteredDto(rubrica, titolo, _sezioni);
	}

	@Override
	public Set<ComboProtocollazioneDto> getComboTipologiaDocumenti() {
		fillDTO();
		return getFilteredDto(null, null, _documenti);
	}

	@Override
	public Set<ComboProtocollazioneDto> getComboCodiciInterni() {
		fillDTO();
		return getFilteredDto(null, null, _comboInterni);
	}

	@Override
	public Set<ComboProtocollazioneDto> getComboCodiciEsterni() {
		fillDTO();
		return getFilteredDto(null, null, _comboEsterni);
	}
	
	
	/* metodi interni */

	private void fillDtos(Combo c) {
		if (c.getNomeCombo().equalsIgnoreCase("ESTERNI")) {
			_comboEsterni = new TreeSet<ComboProtocollazioneDto>();
			fillListDto(_comboEsterni, c.getElementiCombo());
		} else if (c.getNomeCombo().equalsIgnoreCase("INTERNI")) {
			_comboInterni = new TreeSet<ComboProtocollazioneDto>();
			fillListDto(_comboInterni, c.getElementiCombo());
		} else if (c.getNomeCombo().equalsIgnoreCase("TITOLO")) {
			_titoli = new TreeSet<ComboProtocollazioneDto>();
			fillListDto(_titoli, c.getElementiCombo());
		} else if (c.getNomeCombo().equalsIgnoreCase("RUBRICA")) {
			_rubriche = new TreeSet<ComboProtocollazioneDto>();
			fillListDto(_rubriche, c.getElementiCombo());
		} else if (c.getNomeCombo().equalsIgnoreCase("SEZIONE")) {
			_sezioni = new TreeSet<ComboProtocollazioneDto>();
			fillListDto(_sezioni, c.getElementiCombo());
		} else if (c.getNomeCombo().equalsIgnoreCase("TIPOLOGIADOCUMENTI")) {
			_documenti = new TreeSet<ComboProtocollazioneDto>();
			fillListDto(_documenti, c.getElementiCombo());
		}
	}

	private void fillListDto(Set<ComboProtocollazioneDto> combos, List<Row> rows) {
		for (Row row : rows) {
			combos.add(new ComboProtocollazioneDto(sanitizeNull(row.getId()), sanitizeNull(row.getReflev1()), sanitizeNull(row.getReflev2()), sanitizeNull(row.getDescrizione()), sanitizeNull(row.getFlagentrata()), sanitizeNull(row.getFlaguscita()), sanitizeNull(row.getFlaginterna())));
		}
	}

	private Set<ComboProtocollazioneDto> getFilteredDto(String reflev1, String reflev2, Set<ComboProtocollazioneDto> list) {
		TreeSet<ComboProtocollazioneDto> copyList = null;
		TreeSet<ComboProtocollazioneDto> copyList2 = null;
		if (reflev1 == null || reflev1.equalsIgnoreCase("")) {
			copyList = new TreeSet<ComboProtocollazioneDto>(list);
		} else {
			copyList = new TreeSet<ComboProtocollazioneDto>();
			for (ComboProtocollazioneDto dto : list) {
				if (dto.getRiferimentoliv1().equalsIgnoreCase(reflev1)) {
					logger.info("Riferimento dto {}, {}", dto.getRiferimentoliv1(), reflev1);
					copyList.add(dto);
				}
			}
			if (reflev2 == null || reflev2.equalsIgnoreCase("")) {
				copyList2 = new TreeSet<ComboProtocollazioneDto>();
				for (ComboProtocollazioneDto dto : copyList) {
					if (dto.getRiferimentoliv2().equalsIgnoreCase(reflev2)) {
						logger.info("Riferimento dto {}, {}", dto.getRiferimentoliv2(), reflev2);
						copyList2.add(dto);
					}
				}
				copyList.clear();
				copyList.addAll(copyList2);
			}
		}
		return copyList;
	}

	private String sanitizeNull(String value) {
		if (Strings.isNullOrEmpty(value))
			return null;
		return value.trim();
	}

}
