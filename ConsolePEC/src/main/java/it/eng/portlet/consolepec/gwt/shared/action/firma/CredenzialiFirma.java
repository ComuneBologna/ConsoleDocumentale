package it.eng.portlet.consolepec.gwt.shared.action.firma;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Informazioni riguradanti le credenziali di firma raccolte sulla Form GWT size
 * 
 * @author pluttero
 * 
 */
public class CredenzialiFirma implements IsSerializable{
	private String username, password, otp;
	private boolean salvaCredenziali, credenzialeUsernameModificata, credenzialePasswordModificata;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getOtp() {
		return otp;
	}
	public void setOtp(String otp) {
		this.otp = otp;
	}
	public boolean isSalvaCredenziali() {
		return salvaCredenziali;
	}
	public void setSalvaCredenziali(boolean salvaCredenziali) {
		this.salvaCredenziali = salvaCredenziali;
	}
	public boolean isCredenzialeUsernameModificata() {
		return credenzialeUsernameModificata;
	}
	public void setCredenzialeUsernameModificata(boolean credenzialeUsernameModificata) {
		this.credenzialeUsernameModificata = credenzialeUsernameModificata;
	}
	public boolean isCredenzialePasswordModificata() {
		return credenzialePasswordModificata;
	}
	public void setCredenzialePasswordModificata(boolean credenzialePasswordModificata) {
		this.credenzialePasswordModificata = credenzialePasswordModificata;
	}

}
