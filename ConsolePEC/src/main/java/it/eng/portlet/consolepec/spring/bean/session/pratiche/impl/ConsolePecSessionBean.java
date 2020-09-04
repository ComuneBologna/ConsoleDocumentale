package it.eng.portlet.consolepec.spring.bean.session.pratiche.impl;

import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;

import java.util.HashMap;

public class ConsolePecSessionBean {

	private final HashMap<String, PraticaSessionBean> _pratiche = new HashMap<String, PraticaSessionBean>();

	/**
	 * 
	 * @param encPath
	 * @param lockedPratica
	 * @return
	 */
	public PraticaSessionBean putPratica(String encPath, LockedPratica lockedPratica) {
		PraticaSessionBean sessionBean = PraticaSessionUtilImpl.getSessionBean(lockedPratica);
		return putPratica(encPath, sessionBean);
	}

	/**
	 * @param encPath
	 * @param sessionBean
	 * @return
	 */
	public PraticaSessionBean putPratica(String encPath, PraticaSessionBean sessionBean) {
		return _pratiche.put(encPath, sessionBean);
	}

	/**
	 * 
	 * @param encPath
	 * @return
	 */
	public PraticaSessionBean getPratica(String encPath) {
		return _pratiche.get(encPath);
	}

	/**
	 * 
	 * @param encPath
	 * @return
	 */
	public PraticaSessionBean removePratica(String encPath) {
		return _pratiche.remove(encPath);
	}
	
	
	public void resetSessionData(){
		_pratiche.clear();
	}

}
