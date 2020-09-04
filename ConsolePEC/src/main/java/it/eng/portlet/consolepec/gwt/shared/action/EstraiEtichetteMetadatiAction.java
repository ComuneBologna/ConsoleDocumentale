package it.eng.portlet.consolepec.gwt.shared.action;

/**
 *
 * @author biagiot
 *
 */
public class EstraiEtichetteMetadatiAction extends LiferayPortletUnsecureActionImpl<EstraiEtichetteMetadatiResult> {

	private String tipoPratica;

	public EstraiEtichetteMetadatiAction() {

	}

	public EstraiEtichetteMetadatiAction(String tipoPratica) {
		this.tipoPratica = tipoPratica;
	}

	public String getTipoPratica() {
		return tipoPratica;
	}

	public void setTipoPratica(String tipoPratica) {
		this.tipoPratica = tipoPratica;
	}
}
