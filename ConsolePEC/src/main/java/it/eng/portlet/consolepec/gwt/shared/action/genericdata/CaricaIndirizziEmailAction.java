package it.eng.portlet.consolepec.gwt.shared.action.genericdata;

import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;

/**
 *
 * @author biagiot
 *
 */
public class CaricaIndirizziEmailAction extends LiferayPortletUnsecureActionImpl<CaricaIndirizziEmailActionResult> {

	private String chiave;
	private boolean caricaCache;

	protected CaricaIndirizziEmailAction() {
		// for Serialization
	}

	public CaricaIndirizziEmailAction(String chiave) {
		this.chiave = chiave;
		this.caricaCache = false;
	}

	public CaricaIndirizziEmailAction(boolean caricaCache) {
		this.caricaCache = caricaCache;
	}

	public String getChiave() {
		return chiave;
	}

	public void setChiave(String chiave) {
		this.chiave = chiave;
	}

	public boolean isCaricaCache() {
		return caricaCache;
	}
}
