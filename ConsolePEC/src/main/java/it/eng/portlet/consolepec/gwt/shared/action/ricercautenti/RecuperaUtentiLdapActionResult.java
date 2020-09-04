package it.eng.portlet.consolepec.gwt.shared.action.ricercautenti;

import it.eng.cobo.consolepec.commons.profilazione.Utente;

import java.util.HashSet;
import java.util.Set;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.gwtplatform.dispatch.shared.Result;

/**
 *
 * @author biagiot
 *
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RecuperaUtentiLdapActionResult implements Result{

	private static final long serialVersionUID = 1L;

	@Getter
	private Set<Utente> utenti = new HashSet<Utente>();

	@Getter
	@Setter
	private String errorMessage;
	
	@Getter
	@Setter
	private boolean error;
	
	public RecuperaUtentiLdapActionResult(String errorMessage) {
		this.error = true;
		this.errorMessage = errorMessage;
	}
	
	public RecuperaUtentiLdapActionResult(Set<Utente> utenti) {
		this.utenti = utenti;
		this.error = false;
		this.errorMessage = null;
	}
}
