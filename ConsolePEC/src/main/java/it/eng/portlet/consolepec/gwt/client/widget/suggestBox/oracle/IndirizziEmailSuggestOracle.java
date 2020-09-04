package it.eng.portlet.consolepec.gwt.client.widget.suggestBox.oracle;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.SuggestOracle;

import it.eng.portlet.consolepec.gwt.client.gin.ClientGinjector;
import it.eng.portlet.consolepec.gwt.client.presenter.IndirizziEmailHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.IndirizziEmailHandler.OnIndirizziEmailLoaded;

/**
 *
 * SuggestOracle custom per l'autocomplete degli indirizzi email
 *
 * @author biagiot
 *
 */
public class IndirizziEmailSuggestOracle extends ConsoleMultiWordSuggestOracle {
	private final ClientGinjector ginjector = GWT.create(ClientGinjector.class);
	private final int MIN_CHAR_NUMBER = 4;

	private List<String> elenco = new ArrayList<>();

	public IndirizziEmailSuggestOracle(List<String> elenco) {
		super();
		this.elenco = elenco;
	}

	public void setElenco(List<String> elenco) {
		super.clear();
		elenco = new ArrayList<String>(elenco);
	}

	@Override
	public void requestSuggestions(final Request request, final Callback callback) {

		final ArrayList<Suggestion> suggestions = new ArrayList<SuggestOracle.Suggestion>();
		final SuggestOracle.Response response = new SuggestOracle.Response();

		if (elenco != null) {
			String query = request.getQuery().toLowerCase();

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

						@Override
						public boolean equals(Object obj) {
							if (this == obj)
								return true;

							if (obj == null || obj.getClass() != this.getClass())
								return false;

							Suggestion geek = (Suggestion) obj;
							return (geek.getDisplayString() == this.getDisplayString());
						}

						@Override
						public int hashCode() {
							return this.getDisplayString().hashCode();
						}
					};

					if (!suggestions.contains(s))
						suggestions.add(s);
				}
			}

		}

		IndirizziEmailHandler indirizziEmailHandler = ginjector.getIndirizziEmailHandler();
		String autocompleteQuery = request.getQuery().toLowerCase();

		if (autocompleteQuery.length() >= MIN_CHAR_NUMBER) {
			indirizziEmailHandler.getIndirizziEmail(autocompleteQuery, new OnIndirizziEmailLoaded() {

				@Override
				public void onIndirizziEmailLoaded(List<String> indirizziEmail) {

					if (indirizziEmail != null) {
						for (final String email : indirizziEmail) {
							Suggestion s = new Suggestion() {

								@Override
								public String getReplacementString() {
									return email;
								}

								@Override
								public String getDisplayString() {
									return email;
								}

								@Override
								public boolean equals(Object obj) {
									if (this == obj)
										return true;

									if (obj == null || obj.getClass() != this.getClass())
										return false;

									Suggestion geek = (Suggestion) obj;
									return (geek.getDisplayString() == this.getDisplayString());
								}

								@Override
								public int hashCode() {
									return this.getDisplayString().hashCode();
								}
							};

							if (!suggestions.contains(s))
								suggestions.add(s);
						}
					}

					response.setSuggestions(suggestions);
					callback.onSuggestionsReady(request, response);

				}
			});

		} else {
			response.setSuggestions(suggestions);
			callback.onSuggestionsReady(request, response);
		}

	}
}
