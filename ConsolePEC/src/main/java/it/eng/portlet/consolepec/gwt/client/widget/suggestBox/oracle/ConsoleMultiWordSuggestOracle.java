package it.eng.portlet.consolepec.gwt.client.widget.suggestBox.oracle;

import com.google.gwt.user.client.ui.MultiWordSuggestOracle;

public class ConsoleMultiWordSuggestOracle extends MultiWordSuggestOracle {

	protected boolean checkForSuggesLike(String query, String value) {
		return " ".equalsIgnoreCase(query) || value.toLowerCase().startsWith(query.toLowerCase().trim()) || value.toLowerCase().contains(query.toLowerCase().trim());
	}
}
