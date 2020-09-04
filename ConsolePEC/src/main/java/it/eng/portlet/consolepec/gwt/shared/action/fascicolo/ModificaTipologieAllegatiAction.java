package it.eng.portlet.consolepec.gwt.shared.action.fascicolo;

import java.util.List;

import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ModificaTipologieAllegatiAction extends LiferayPortletUnsecureActionImpl<ModificaTipologieAllegatiResult> {

	@Getter private FascicoloDTO fascicolo;
	@Getter private List<AllegatoDTO> allegati;

}
