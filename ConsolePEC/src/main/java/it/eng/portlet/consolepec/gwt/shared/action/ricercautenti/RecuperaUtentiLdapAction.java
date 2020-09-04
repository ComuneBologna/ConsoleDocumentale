package it.eng.portlet.consolepec.gwt.shared.action.ricercautenti;

import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;
import it.eng.portlet.consolepec.gwt.shared.model.ricercautenti.TipoRicercaLdapUtenti;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author biagiot
 *
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RecuperaUtentiLdapAction extends LiferayPortletUnsecureActionImpl<RecuperaUtentiLdapActionResult> {

	@Getter
	String inputQuery;

	@Getter
	@Setter
	Integer limit;

	@Getter
	TipoRicercaLdapUtenti tipoRicerca;

	public RecuperaUtentiLdapAction(TipoRicercaLdapUtenti tipoRicerca, String inputQuery) {
		this.inputQuery = inputQuery;
		this.tipoRicerca = tipoRicerca;
	}
}
