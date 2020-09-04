package it.eng.portlet.consolepec.spring.firma.impl;


/**
 * Bean di credenziali di firma
 * @author pluttero
 *
 */
public class CredenzialiFirma {
	String username, password, otp;
	boolean salvaCredenziali;

	public CredenzialiFirma(String username, String password, String otp, boolean salvaCredenziali) {
		this.username = username;
		this.password = password;
		this.salvaCredenziali = salvaCredenziali;
		this.otp = otp;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public boolean isSalvaCredenziali() {
		return salvaCredenziali;
	}

	public String getOtp() {
		return otp;
	}
}
