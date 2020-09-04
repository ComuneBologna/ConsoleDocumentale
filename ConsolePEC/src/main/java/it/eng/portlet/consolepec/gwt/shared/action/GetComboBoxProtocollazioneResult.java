package it.eng.portlet.consolepec.gwt.shared.action;

import it.eng.portlet.consolepec.gwt.shared.dto.ComboProtocollazioneDto;

import java.util.Set;
import java.util.TreeSet;

import com.gwtplatform.dispatch.shared.Result;

public class GetComboBoxProtocollazioneResult implements Result {

	private static final long serialVersionUID = -7767310911177510585L;

	private Set<ComboProtocollazioneDto> _titoli = new TreeSet<ComboProtocollazioneDto>();
	private Set<ComboProtocollazioneDto> _rubriche = new TreeSet<ComboProtocollazioneDto>();
	private Set<ComboProtocollazioneDto> _sezioni = new TreeSet<ComboProtocollazioneDto>();
	private Set<ComboProtocollazioneDto> _documenti = new TreeSet<ComboProtocollazioneDto>();
	private Set<ComboProtocollazioneDto> _interni = new TreeSet<ComboProtocollazioneDto>();
	private Set<ComboProtocollazioneDto> _esterni = new TreeSet<ComboProtocollazioneDto>();

	public GetComboBoxProtocollazioneResult() {
	}

	public GetComboBoxProtocollazioneResult(Set<ComboProtocollazioneDto> _titoli, Set<ComboProtocollazioneDto> _rubriche, Set<ComboProtocollazioneDto> _sezioni, Set<ComboProtocollazioneDto> _documenti, Set<ComboProtocollazioneDto> _interni, Set<ComboProtocollazioneDto> _esterni) {
		super();
		this._titoli = _titoli;
		this._rubriche = _rubriche;
		this._sezioni = _sezioni;
		this._documenti = _documenti;
		this._interni = _interni;
		this._esterni = _esterni;
	}

	public Set<ComboProtocollazioneDto> getTitoli() {
		return _titoli;
	}

	public Set<ComboProtocollazioneDto> getRubriche() {
		return _rubriche;
	}

	public Set<ComboProtocollazioneDto> getSezioni() {
		return _sezioni;
	}

	public Set<ComboProtocollazioneDto> getDocumenti() {
		return _documenti;
	}

	public Set<ComboProtocollazioneDto> getInterni() {
		return _interni;
	}

	public Set<ComboProtocollazioneDto> getEsterni() {
		return _esterni;
	}

}
