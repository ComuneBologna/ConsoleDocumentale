package it.eng.portlet.consolepec.gwt.client.view.fascicolo.composizione;

import java.util.ArrayList;
import java.util.Set;

import com.google.gwt.user.client.ui.SuggestOracle;

import it.eng.portlet.consolepec.gwt.client.composizione.RicercaComposizioneFascicoloHandler;

public class RicercaComposizioneFascicoloSuggestOracle extends SuggestOracle {
	private RicercaComposizioneFascicoloHandler ricercaHandler;

	public RicercaComposizioneFascicoloSuggestOracle(RicercaComposizioneFascicoloHandler ricercaHandler) {
		super();
		this.ricercaHandler = ricercaHandler;
	}

	@Override
	public void requestSuggestions(Request request, Callback callback) {
		String query = request.getQuery();
		ArrayList<Suggestion> suggestions = new ArrayList<SuggestOracle.Suggestion>();
		SuggestOracle.Response response = new SuggestOracle.Response();

		Set<String> values = ricercaHandler.autocomplete(query);

		for (final String value : values) {

			Suggestion s = new Suggestion() {

				@Override
				public String getReplacementString() {
					return value;
				}

				@Override
				public String getDisplayString() {
					return value;
				}
			};

			suggestions.add(s);

		}

		response.setSuggestions(suggestions);
		callback.onSuggestionsReady(request, response);
	}

}
