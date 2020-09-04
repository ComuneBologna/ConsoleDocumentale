package it.eng.portlet.consolepec.gwt.shared.action.assegna.pec;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.portlet.consolepec.gwt.shared.action.assegna.RiassegnaAction;

import java.util.HashSet;
import java.util.Set;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RiassegnaPecIn extends RiassegnaAction<RiassegnaPecInResult> {

	@Getter
	@Setter
	private String clientID;
	
	@Getter
	@Setter
	private String note;
	
	@Getter
	private Set<String> ids = new HashSet<String>();
	
	@Getter
	@Setter
	private String operatore;
	
	@Getter
	@Setter
	private AnagraficaRuolo anagraficaRuolo;

	public RiassegnaPecIn(Set<String> ids, AnagraficaRuolo anagraficaRuolo) {
		this.ids = ids;
		this.anagraficaRuolo = anagraficaRuolo;
	}
}
