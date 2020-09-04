package it.eng.portlet.consolepec.gwt.client.widget.suggestBox.oracle;

import it.eng.portlet.consolepec.gwt.shared.dto.ComboProtocollazioneDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle;

public class DatiTitolazioneMultiWordSuggestOracle extends MultiWordSuggestOracle {

	Set<ComboProtocollazioneDto> dati = new TreeSet<ComboProtocollazioneDto>();
	Set<ComboProtocollazioneDto> datiCache = new TreeSet<ComboProtocollazioneDto>();

	public DatiTitolazioneMultiWordSuggestOracle() {
		super();
	}

	public DatiTitolazioneMultiWordSuggestOracle(List<ComboProtocollazioneDto> dati) {
		super();
		this.dati = new TreeSet<ComboProtocollazioneDto>(dati);
		this.datiCache = new TreeSet<ComboProtocollazioneDto>(dati);
	}

	public Set<ComboProtocollazioneDto> getDati() {
		return dati;
	}

	public void setDati(Set<ComboProtocollazioneDto> dati) {
		if (datiCache.size() == 0)
			datiCache.addAll(dati);
		this.dati = new TreeSet<ComboProtocollazioneDto>(dati);
	}

	public void refreshLiv1(String identificativoliv1) {
		dati = new TreeSet<ComboProtocollazioneDto>();
		for (ComboProtocollazioneDto dto : datiCache) {
			if (dto.getRiferimentoliv1().equalsIgnoreCase(identificativoliv1))
				dati.add(dto);
		}
	}

	public void refreshLiv2(String identificativoliv2) {
		Set<ComboProtocollazioneDto> datibk = new TreeSet<ComboProtocollazioneDto>();
		for (ComboProtocollazioneDto dto : dati) {
			if (dto.getRiferimentoliv2().equalsIgnoreCase(identificativoliv2))
				datibk.add(dto);
		}
		dati.clear();
		dati.addAll(datibk);
	}

	@Override
	public void requestSuggestions(Request request, Callback callback) {
		String query = request.getQuery().toUpperCase();
		ArrayList<Suggestion> suggestions = new ArrayList<SuggestOracle.Suggestion>();
		final SuggestOracle.Response response = new SuggestOracle.Response();

		for (final ComboProtocollazioneDto dto : dati) {
			if (" ".equalsIgnoreCase(query) || dto.getDescrizione().startsWith(query.trim()) || dto.getDescrizione().contains(query.trim()) || dto.getIdentificativo().startsWith(query.trim())) {
				SuggestionExt suggestion = new SuggestionExt(dto);
				suggestions.add(suggestion);
			}
		}
		response.setSuggestions(suggestions);
		callback.onSuggestionsReady(request, response);
	}

	public class SuggestionExt implements Suggestion {

		private ComboProtocollazioneDto dto;

		public SuggestionExt(ComboProtocollazioneDto dto) {
			this.dto = dto;
		}

		public String getRiferimentoLiv1() {
			return dto.getRiferimentoliv1();
		}

		public String getRiferimentoLiv2() {
			return dto.getRiferimentoliv2();
		}

		public String getIdentificativo() {
			return dto.getIdentificativo();
		}

		public String getDescrizione() {
			return dto.getDescrizione();
		}

		@Override
		public String getDisplayString() {
			return dto.getIdentificativo() + " - " + dto.getDescrizione();
		}

		@Override
		public String getReplacementString() {
			return dto.getIdentificativo() + " - " + dto.getDescrizione();
		}
	}
}
