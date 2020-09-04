package it.eng.portlet.consolepec.gwt.shared.action.fascicolo;

import java.util.Set;
import java.util.TreeSet;

import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@AllArgsConstructor
public class SpostaAllegatiAction extends LiferayPortletUnsecureActionImpl<SpostaAllegatiResult> {

	private String fascicoloSorgenteClientID;
	private String fascicoloDestinatarioClientID;
	private Set<AllegatoDTO> allegatiDaSpostare = new TreeSet<AllegatoDTO>();

}
