package it.eng.portlet.consolepec.gwt.client.widget.suggestBox.oracle;

import it.eng.portlet.consolepec.gwt.shared.dto.TipologiaProcedimentoDto;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import com.google.gwt.user.client.ui.SuggestOracle;

public class ProcedimentiMultiWordSuggestOracle extends ConsoleMultiWordSuggestOracle {

	private TreeSet<TipologiaProcedimentoDto> tipologiaProcedimentoDtoList = new TreeSet<TipologiaProcedimentoDto>();

	public void setTipologiaProcedimentoDtoList(List<TipologiaProcedimentoDto> tipologiaProcedimentoDtoList) {
		this.tipologiaProcedimentoDtoList = new TreeSet<TipologiaProcedimentoDto>(tipologiaProcedimentoDtoList);
	}

	@Override
	public void requestSuggestions(Request request, Callback callback) {
		String query = request.getQuery().toUpperCase();
		ArrayList<Suggestion> suggestions = new ArrayList<SuggestOracle.Suggestion>();
		final SuggestOracle.Response response = new SuggestOracle.Response();
		for (final TipologiaProcedimentoDto r : tipologiaProcedimentoDtoList) {
			if (checkForSuggesLike(query, r.getCodiceProcedimento() + " - " + r.getDescrizione())) {
				Suggestion s = new Suggestion() {

					@Override
					public String getReplacementString() {
						return r.getCodiceProcedimento() + " - " + r.getDescrizione();
					}

					@Override
					public String getDisplayString() {
						return r.getCodiceProcedimento() + " - " + getDescrizioneLabel(r.getDescrizione());
					}

				};
				suggestions.add(s);
			}
		}
		response.setSuggestions(suggestions);
		callback.onSuggestionsReady(request, response);

	}

	private String getDescrizioneLabel(String descrizione) {
		if (descrizione == null)
			return "";
		if (descrizione.length() > 100) {
			return descrizione.subSequence(0, 97) + "...";
		}
		return descrizione;
	}

}
