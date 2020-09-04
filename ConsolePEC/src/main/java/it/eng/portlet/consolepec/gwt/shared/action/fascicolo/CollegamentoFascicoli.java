package it.eng.portlet.consolepec.gwt.shared.action.fascicolo;

import it.eng.cobo.consolepec.commons.pratica.fascicolo.collegamenti.Permessi;
import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CollegamentoFascicoli extends LiferayPortletUnsecureActionImpl<CollegamentoFascicoliResult> {

	public static final String LINK_MERGE = "merge";
	public static final String LINK_DELETE = "delete";

	private String link;
	private String pathFascicoloRemoto;
	private String pathFascicoloLocale;
	private Permessi permessi;
	private String ruolo;

	public CollegamentoFascicoli(String link, String pathFascicoloRemoto, String pathFascicoloLocale, Permessi permessi) {
		this.link = link;
		this.pathFascicoloRemoto = pathFascicoloRemoto;
		this.pathFascicoloLocale = pathFascicoloLocale;
		this.permessi = permessi;
	}

	public CollegamentoFascicoli(String link, String pathFascicoloRemoto, String pathFascicoloLocale, Permessi permessi, String ruolo) {
		this.link = link;
		this.pathFascicoloRemoto = pathFascicoloRemoto;
		this.pathFascicoloLocale = pathFascicoloLocale;
		this.permessi = permessi;
		this.ruolo = ruolo;
	}

}
