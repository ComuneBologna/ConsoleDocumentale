package it.eng.portlet.consolepec.gwt.client.widget.suggestBox.oracle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.gwt.user.client.ui.SuggestOracle;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import lombok.Getter;

/**
 *
 * @author biagiot
 *
 */
public class AnagraficheRuoliSuggestOracle extends ConsoleMultiWordSuggestOracle {

	private final ComparatorAngrafiche comparator = new ComparatorAngrafiche();

	@Getter
	List<AnagraficaRuolo> anagraficheRuoli = new ArrayList<AnagraficaRuolo>();

	public AnagraficheRuoliSuggestOracle(List<AnagraficaRuolo> anagraficheRuoli) {
		setAnagraficheRuoli(anagraficheRuoli);
	}

	@Override
	public void requestSuggestions(Request request, Callback callback) {
		String query = request.getQuery().toUpperCase();
		ArrayList<Suggestion> suggestions = new ArrayList<SuggestOracle.Suggestion>();
		final SuggestOracle.Response response = new SuggestOracle.Response();

		for (AnagraficaRuolo r : anagraficheRuoli) {
			final String v = r.getEtichetta();

			if (checkForSuggesLike(query, v)) {
				Suggestion s = new Suggestion() {

					@Override
					public String getReplacementString() {
						return v;
					}

					@Override
					public String getDisplayString() {
						return v;
					}
				};

				suggestions.add(s);
			}
		}

		response.setSuggestions(suggestions);
		callback.onSuggestionsReady(request, response);
	}

	public void setAnagraficheRuoli(List<AnagraficaRuolo> anagraficaRuoli) {
		super.clear();
		anagraficheRuoli = new ArrayList<>(anagraficaRuoli);
		Collections.sort(this.anagraficheRuoli, comparator);
	}

	private static class ComparatorAngrafiche implements Comparator<AnagraficaRuolo> {

		@Override
		public int compare(AnagraficaRuolo o1, AnagraficaRuolo o2) {
			return o1.getEtichetta().compareTo(o2.getEtichetta());
		}

	}

	public AnagraficaRuolo getAnagraficaRuolo(String etichetta) {
		for (AnagraficaRuolo ar : anagraficheRuoli) {
			if (ar.getEtichetta().equals(etichetta)) {
				return ar;
			}
		}

		return null;
	}
}
