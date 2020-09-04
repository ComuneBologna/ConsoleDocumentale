package it.eng.portlet.consolepec.gwt.shared.action.firma;

import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;

public class SendOTPRequest extends LiferayPortletUnsecureActionImpl<SendOTPRequestResult> {

	private String username, password, credentialType;
	
	public SendOTPRequest() {
	}
	
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

	public String getCredentialType() {
		return credentialType;
	}

	public void setCredentialType(String credentialType) {
		this.credentialType = credentialType;
	}

}
