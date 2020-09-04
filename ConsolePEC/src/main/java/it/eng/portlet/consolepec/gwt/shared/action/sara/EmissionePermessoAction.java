package it.eng.portlet.consolepec.gwt.shared.action.sara;

import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class EmissionePermessoAction extends LiferayPortletUnsecureActionImpl<EmissionePermessoResult>{

	@Getter private String idDocumentale;
	
}