package it.eng.portlet.consolepec.spring.bean.session.user;

import java.util.HashMap;

import com.liferay.portal.model.User;

import it.eng.portlet.consolepec.spring.bean.session.TipoLogin;

public class ConsolePecUser {

	private final User user;
	private final long companyId;
	private final HashMap<String, Object> customAttribute = new HashMap<String, Object>();
	private final TipoLogin tipoLogin;

	public ConsolePecUser(long companyId, User user, TipoLogin tipoLogin) {
		this.companyId = companyId;
		this.user = user;
		this.tipoLogin = tipoLogin;
	}

	public TipoLogin getTipoLogin() {
		return tipoLogin;
	}

	public User getUser() {
		return user;
	}

	public Object getCustomAttribute(String attributeName) {
		return customAttribute.get(attributeName);
	}

	public void addCustomAttribute(String attributeName, Object attributeValue) {
		this.customAttribute.put(attributeName, attributeValue);
	}

	public void removeCustomAttribute(String attributeName) {
		this.customAttribute.remove(attributeName);
	}

	@Override
	public String toString() {
		return user.toString();
	}

	public long getCompanyId() {
		return companyId;
	}

}
