package it.eng.portlet.consolepec.gwt.client.widget.suggestBox.oracle;

import java.util.Set;

import com.google.gwt.user.client.ui.MultiWordSuggestOracle;

import it.eng.cobo.consolepec.commons.profilazione.Utente;

public abstract class UtentiSuggestOracle extends MultiWordSuggestOracle {

	public abstract Set<Utente> getUtenti();

	public Utente getUtenteFromSuggest(String suggest) {
		for (Utente u : getUtenti()) {

			if (suggest != null && (suggest.equals(u.getNomeCompleto()) || suggest.equals(u.getNomeCompleto() + " - " + u.getDipartimento()))) {
				return u;
			}
		}

		return null;
	}
}
