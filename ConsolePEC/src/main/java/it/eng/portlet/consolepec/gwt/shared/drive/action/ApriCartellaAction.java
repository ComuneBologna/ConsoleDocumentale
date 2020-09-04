package it.eng.portlet.consolepec.gwt.shared.drive.action;

import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author Giacomo F.M.
 * @since 2019-05-31
 */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApriCartellaAction extends LiferayPortletUnsecureActionImpl<ApriCartellaResult> {

	@Getter
	private String idCartella;
	@Getter
	private Integer page, limit;

}
