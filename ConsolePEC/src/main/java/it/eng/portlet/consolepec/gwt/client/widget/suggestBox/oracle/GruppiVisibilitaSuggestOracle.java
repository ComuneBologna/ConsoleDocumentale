package it.eng.portlet.consolepec.gwt.client.widget.suggestBox.oracle;

import it.eng.portlet.consolepec.gwt.shared.model.GruppoVisibilita;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.google.gwt.user.client.ui.SuggestOracle;

public class GruppiVisibilitaSuggestOracle extends ConsoleMultiWordSuggestOracle {
	protected Set<GruppoVisibilita> gruppi = new HashSet<GruppoVisibilita>();

	public GruppiVisibilitaSuggestOracle(Set<GruppoVisibilita> gruppi) {
		this.gruppi = gruppi;
	}

	@Override
	public void requestSuggestions(Request request, Callback callback) {
		String query = request.getQuery().toUpperCase();
		ArrayList<Suggestion> suggestions = new ArrayList<SuggestOracle.Suggestion>();
		final SuggestOracle.Response response = new SuggestOracle.Response();
		for (GruppoVisibilita r : gruppi) {
			final String v = r.getLabel();
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

	public Set<GruppoVisibilita> getGruppi() {
		return gruppi;
	}

	public void setGruppi(Set<GruppoVisibilita> gruppi) {
		this.gruppi = gruppi;
	}

}
