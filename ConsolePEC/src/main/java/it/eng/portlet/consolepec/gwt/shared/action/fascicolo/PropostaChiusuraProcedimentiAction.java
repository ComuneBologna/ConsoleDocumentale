package it.eng.portlet.consolepec.gwt.shared.action.fascicolo;

import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PropostaChiusuraProcedimentiAction extends LiferayPortletUnsecureActionImpl<PropostaChiusuraProcedimentiResult> {

	private String annoPgNonCapofila;
	private String numPgNonCapofila;

}
