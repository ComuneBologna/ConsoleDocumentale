package it.eng.portlet.consolepec.gwt.shared.action;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class SalvaNotePecInAction extends LiferayPortletUnsecureActionImpl<SalvaNotePecInResult> {

	private String clientIdPecIn;
	private String note;

}
