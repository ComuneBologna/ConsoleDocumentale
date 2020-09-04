package it.eng.portlet.consolepec.spring.bean.session;

public enum TipoLogin {

	LDAP, FEDERA;

	public static TipoLogin from(String name) {

		for (TipoLogin t : TipoLogin.values()) {
			if (t.name().equals(name)) {
				return t;
			}
		}

		return null;

	}

}
