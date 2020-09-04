package it.eng.cobo.consolepec.commons.ldap;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class LdapGroup {
	private String name;
	private List<String> users = new ArrayList<>();

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		if (name != null && !name.isEmpty()) sb.append(" - ").append(name);
		if (users != null && !users.isEmpty()) {
			sb.append(" - ").append(users.toString());
		}

		return sb.toString();
	}
}
