package it.eng.portlet.consolepec.gwt.shared.action.fascicolo;

import java.util.Set;
import java.util.TreeSet;

import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoElenco;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@AllArgsConstructor
public class SpostaProtocollazioniAction extends LiferayPortletUnsecureActionImpl<SpostaProtocollazioniResult> {

	private String fascicoloSorgenteClientID;
	private String fascicoloDestinatarioClientID;
	private Set<AllegatoDTO> allegatiProtocollati = new TreeSet<AllegatoDTO>();
	private Set<ElementoElenco> elementiProtocollati = new TreeSet<ElementoElenco>();

}
