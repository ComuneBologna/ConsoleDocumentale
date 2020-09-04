package it.eng.portlet.consolepec.gwt.shared.drive.action;

import java.util.Set;

import it.eng.cobo.consolepec.commons.drive.permessi.PermessoDrive;
import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author Giacomo F.M.
 * @since 2019-07-05
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AggiornaPermessiAction extends LiferayPortletUnsecureActionImpl<AggiornaPermessiResult> {

	private String id;
	private boolean recursive;
	private Set<PermessoDrive> aggiunta;
	private Set<PermessoDrive> rimozione;

}
