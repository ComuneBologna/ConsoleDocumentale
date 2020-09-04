package it.eng.portlet.consolepec.gwt.shared.drive.action;

import it.eng.cobo.consolepec.commons.drive.DriveElement;
import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author Giacomo F.M.
 * @since 2019-07-09
 */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EliminaElementoAction extends LiferayPortletUnsecureActionImpl<EliminaElementoResult> {

	@Getter
	private DriveElement elemento;

}
