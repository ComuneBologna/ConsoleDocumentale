package it.eng.portlet.consolepec.spring.bean.session.pratiche.impl;

import it.eng.consolepec.xmlplugin.factory.Pratica;

public class PraticaSessionBean {

	public PraticaSessionBean(String version, Pratica<?> pratica) {
		super();
		this.version = version;
		this.pratica = pratica;
	}

	private String version;
	private Pratica<?> pratica;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Pratica<?> getPratica() {
		return pratica;
	}

	public void setPratica(Pratica<?> pratica) {
		this.pratica = pratica;
	}

}
