package it.eng.portlet.consolepec.gwt.shared.action.assegna.modulo;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.portlet.consolepec.gwt.shared.action.assegna.RiassegnaAction;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaModulisticaDTO.StatoDTO;

import java.util.HashSet;
import java.util.Set;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RiassegnaModulo extends RiassegnaAction<RiassegnaModuloResult> {

	@Getter
	@Setter
	private AnagraficaRuolo anagraficaRuolo;
	
	@Getter
	private Set<String> ids = new HashSet<String>();
	
	@Getter
	@Setter
	private StatoDTO stato;
	
	@Getter
	@Setter
	private String note;
	
	@Getter
	@Setter
	private String operatore;

	public RiassegnaModulo(Set<String> ids, AnagraficaRuolo anagraficaRuolo) {
		this.ids = ids;
		this.anagraficaRuolo = anagraficaRuolo;
	}
}
