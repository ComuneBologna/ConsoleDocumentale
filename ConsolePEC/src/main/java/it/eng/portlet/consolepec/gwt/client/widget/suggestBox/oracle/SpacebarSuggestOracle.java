package it.eng.portlet.consolepec.gwt.client.widget.suggestBox.oracle;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.SuggestOracle;

public class SpacebarSuggestOracle extends ConsoleMultiWordSuggestOracle {
	List<String> elenco;

	public SpacebarSuggestOracle(List<String> elenco) {
		super();
		this.elenco = elenco;
	}
	
	public void setSuggestions(List<String> elenco) {
		super.clear();
		super.addAll(elenco);
		this.elenco = new ArrayList<String>(elenco);
	}

	@Override
	public void requestSuggestions(Request request, Callback callback) {
		String query = request.getQuery().toUpperCase();
		ArrayList<Suggestion> suggestions = new ArrayList<SuggestOracle.Suggestion>();
		final SuggestOracle.Response response = new SuggestOracle.Response();
		
		for (final String v : elenco) {
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
}