package it.eng.portlet.consolepec.gwt.shared.action.urbanistica;

import it.eng.cobo.consolepec.commons.urbanistica.PraticaProcedi;
import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author GiacomoFM
 * @since 08/nov/2017
 */
@NoArgsConstructor
@AllArgsConstructor
public class EliminaCollegaPraticaProcediAction extends LiferayPortletUnsecureActionImpl<EliminaCollegaPraticaProcediResult> {

	@Getter @Setter private String encodedPath;
	@Getter @Setter private List<PraticaProcedi> praticheProcedi;

}
