package it.eng.portlet.consolepec.gwt.server.ricercautenti;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.exception.ConsoleDocumentaleException;
import it.eng.cobo.consolepec.commons.ldap.LdapQueryFilter;
import it.eng.cobo.consolepec.commons.ldap.LdapUser;
import it.eng.cobo.consolepec.commons.profilazione.Utente;
import it.eng.cobo.consolepec.util.json.JsonFactory;
import it.eng.cobo.consolepec.util.utente.UtenteUtils;
import it.eng.portlet.consolepec.gwt.server.rest.RestClientInvoker;
import it.eng.portlet.consolepec.gwt.server.rest.RestResponse;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.ricercautenti.RecuperaUtentiLdapAction;
import it.eng.portlet.consolepec.gwt.shared.action.ricercautenti.RecuperaUtentiLdapActionResult;
import it.eng.portlet.consolepec.spring.bean.configurazioni.GestioneConfigurazioni;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

/**
 *
 * @author biagiot
 *
 */
public class RecuperaUtentiLdapActionHandler implements ActionHandler<RecuperaUtentiLdapAction, RecuperaUtentiLdapActionResult> {

	private static final Logger logger = LoggerFactory.getLogger(RecuperaUtentiLdapActionHandler.class);

	@Autowired
	UserSessionUtil userSessionUtil;

	@Autowired
	RestClientInvoker restClientInvoker;

	@Autowired
	GestioneConfigurazioni gestioneConfigurazioni;

	@Override
	public RecuperaUtentiLdapActionResult execute(RecuperaUtentiLdapAction action, ExecutionContext context) throws ActionException {

		logger.info("Start RecuperaUtentiActionHandler - action: {}", action);
		RecuperaUtentiLdapActionResult result;

		try {
			Set<Utente> utenti = ricercaRest(action);
			result = new RecuperaUtentiLdapActionResult(utenti);

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			result = new RecuperaUtentiLdapActionResult(ConsolePecConstants.ERROR_MESSAGE);

		}

		logger.info("End RecuperaUtentiActionHandler");
		return result;
	}

	public RecuperaUtentiLdapActionHandler() {

	}

	@Override
	public Class<RecuperaUtentiLdapAction> getActionType() {
		return RecuperaUtentiLdapAction.class;
	}

	@Override
	public void undo(RecuperaUtentiLdapAction arg0, RecuperaUtentiLdapActionResult arg1, ExecutionContext arg2) throws ActionException {}

	protected Set<Utente> ricercaRest(RecuperaUtentiLdapAction action) throws ConsoleDocumentaleException {
		Set<Utente> utenti = new HashSet<Utente>();

		LdapQueryFilter ricerca = null;

		String endpoint = null;
		switch (action.getTipoRicerca()) {

		case USERNAME:
			endpoint = "/service/ldap/utenti";
			ricerca = new LdapQueryFilter();
			ricerca.setUtente("*" + action.getInputQuery() + "*");
			break;

		case NOME_COGNOME_PATTERN:
			endpoint = "/service/ldap/utenti";
			ricerca = new LdapQueryFilter();
			ricerca.setNomeOCognome("*" + action.getInputQuery() + "*");
			ricerca.setLimit(action.getLimit());

			break;
		case GRUPPO:
			endpoint = "/service/ldap/gruppi";
			ricerca = new LdapQueryFilter();
			ricerca.getGruppi().add(action.getInputQuery());
			break;
		}

		HttpEntity entity = new StringEntity(JsonFactory.defaultFactory().serialize(ricerca), ContentType.APPLICATION_JSON);

		RestResponse output = restClientInvoker.customPost(endpoint, null, entity);
		if (!output.isOk()) {
			throw new RuntimeException("Errore nella chiamata al servizio: " + output.getJson());
		}

		String json = output.getJson();

		List<LdapUser> utentiLdap = JsonFactory.defaultFactory().deserializeList(json, LdapUser.class);

		for (LdapUser ldapUser : utentiLdap) {
			Utente utente = new Utente();
			utente.setUsername(ldapUser.getUsername());
			utente.setNome(ldapUser.getName());
			utente.setCognome(ldapUser.getSurname());
			utente.setMail(ldapUser.getMail());
			utente.setNomeCompleto(ldapUser.getName() + " " + ldapUser.getSurname());
			utente.setMatricola(ldapUser.getMatricola());
			utente.setDipartimento(ldapUser.getDipartimento());
			utente.setNomeCompleto(ldapUser.getName() + " " + ldapUser.getSurname());
			utente.setCodicefiscale(ldapUser.getCodiceFiscale());
			utente.setRuoloPersonale(UtenteUtils.getAnagraficaRuoloPersonale(ldapUser.getName(), ldapUser.getSurname(), ldapUser.getCodiceFiscale(), ldapUser.getMatricola()));

			for (String ruolo : ldapUser.getRoles()) {
				AnagraficaRuolo ar = gestioneConfigurazioni.getAnagraficaRuolo(ruolo);
				utente.getAnagraficheRuoli().add(ar);
			}

			utenti.add(utente);

		}
		return utenti;
	}

}
