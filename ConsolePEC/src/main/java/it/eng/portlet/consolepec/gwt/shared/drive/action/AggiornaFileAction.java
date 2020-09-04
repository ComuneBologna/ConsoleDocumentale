package it.eng.portlet.consolepec.gwt.shared.drive.action;

import it.eng.cobo.consolepec.commons.drive.File;
import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author Giacomo F.M.
 * @since 2019-06-12
 */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AggiornaFileAction extends LiferayPortletUnsecureActionImpl<AggiornaFileResult> {

	@Getter
	private File file;

}
