package it.eng.portlet.consolepec.gwt.client.widget.suggestBox.oracle;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;

import it.eng.cobo.consolepec.commons.profilazione.Utente;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.ricercautenti.RecuperaUtentiLdapAction;
import it.eng.portlet.consolepec.gwt.shared.action.ricercautenti.RecuperaUtentiLdapActionResult;
import it.eng.portlet.consolepec.gwt.shared.model.ricercautenti.TipoRicercaLdapUtenti;

/**
 *
 * @author biagiot
 *
 */
public class UtentiSuggestOracleImpl extends UtentiSuggestOracle {

	private Set<Utente> utenti = new HashSet<Utente>();

	private final DispatchAsync dispatcher;
	private final EventBus eventBus;

	private final int MIN_CHAR_NUMBER = 4;
	private final int MAX_ELEMENT = 10;

	private boolean loading;

	@Inject
	public UtentiSuggestOracleImpl(DispatchAsync dispatcher, EventBus eventBus) {
		this.dispatcher = dispatcher;
		this.eventBus = eventBus;
		this.loading = false;
	}

	@Override
	public void requestSuggestions(final Request request, final Callback callback) {
		utenti.clear();

		String query = request.getQuery().toUpperCase();
		final ArrayList<Suggestion> suggestions = new ArrayList<SuggestOracle.Suggestion>();
		final SuggestOracle.Response response = new SuggestOracle.Response();

		if (query.length() >= MIN_CHAR_NUMBER && !loading) {

			RecuperaUtentiLdapAction action = new RecuperaUtentiLdapAction(TipoRicercaLdapUtenti.NOME_COGNOME_PATTERN, query);
			action.setLimit(MAX_ELEMENT);

			loading = true;
			dispatcher.execute(action, new AsyncCallback<RecuperaUtentiLdapActionResult>() {

				@Override
				public void onFailure(Throwable caught) {
					loading = false;

					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
					eventBus.fireEvent(event);
				}

				@Override
				public void onSuccess(RecuperaUtentiLdapActionResult result) {

					loading = false;

					if (!result.isError()) {
						for (final Utente utente : result.getUtenti()) {

							if (suggestions.size() <= MAX_ELEMENT) {
								utenti.add(utente);
							}
						}

						for (Utente utente : utenti) {
							suggestions.add(new UtenteSuggestion(utente, utenti));

						}

						response.setSuggestions(suggestions);
						callback.onSuggestionsReady(request, response);

					} else {
						ShowMessageEvent event = new ShowMessageEvent();
						event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
						eventBus.fireEvent(event);
					}
				}
			});
		}
	}

	@Override
	public Set<Utente> getUtenti() {
		return utenti;
	}

	public static class UtenteSuggestion implements Suggestion {

		private Utente utente;
		private TipoDescrizione tipoDescrizione;

		public UtenteSuggestion(Utente utente, Set<Utente> utenti) {
			super();
			this.utente = utente;

			int c = 0;
			for (Utente u : utenti) {
				if (u.getNomeCompleto().equals(utente.getNomeCompleto())) {
					c++;
				}
			}

			if (c > 1) {
				this.tipoDescrizione = TipoDescrizione.NOME_COMPLETO_SETTORE;

			} else {
				this.tipoDescrizione = TipoDescrizione.NOME_COMPLETO;
			}
		}

		@Override
		public String getDisplayString() {

			switch (this.tipoDescrizione) {
			case NOME_COMPLETO:
				return utente.getNomeCompleto();

			case NOME_COMPLETO_SETTORE:
				return utente.getNomeCompleto() + " - " + utente.getDipartimento();

			default:
				return utente.getNomeCompleto();
			}

		}

		@Override
		public String getReplacementString() {
			switch (this.tipoDescrizione) {
			case NOME_COMPLETO:
				return utente.getNomeCompleto();

			case NOME_COMPLETO_SETTORE:
				return utente.getNomeCompleto() + " - " + utente.getDipartimento();

			default:
				return utente.getNomeCompleto();
			}
		}

		public Utente getUtente() {
			return utente;
		}

		public static enum TipoDescrizione {
			NOME_COMPLETO, NOME_COMPLETO_SETTORE;
		}
	}
}
