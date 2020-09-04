package it.eng.portlet.consolepec.gwt.shared.action.assegna.fasciolo;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.portlet.consolepec.gwt.shared.action.assegna.RiassegnaAction;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.StatoDTO;

import java.util.Set;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RiassegnaFascicolo extends RiassegnaAction<RiassegnaFascicoloResult> {

	@Getter
	@Setter
	private AnagraficaRuolo anagraficaRuolo;
	
	@Getter
	@Setter
	private Set<String> ids;
	
	@Getter
	@Setter
	private StatoDTO stato;
	
	@Getter
	@Setter
	private String note;
	
	@Getter
	@Setter
	private String operatore;
	
	public RiassegnaFascicolo(Set<String> ids, AnagraficaRuolo anagraficaRuolo) {
		this.ids = ids;
		this.anagraficaRuolo = anagraficaRuolo;
	}
}
