package it.eng.cobo.consolepec.commons.ldap;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "username")
public class LdapUser {
	private String name;
	private String surname;
	private String matricola;
	private String codiceFiscale;
	private String username;
	private String mail;
	private String dipartimento;
	private List<String> roles = new ArrayList<>();

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		if (username != null && !username.isEmpty())
			sb.append(username);

		if (name != null && !name.isEmpty())
			sb.append(" - ").append(name);

		if (surname != null && !surname.isEmpty())
			sb.append(" - ").append(surname);

		if (matricola != null && !matricola.isEmpty())
			sb.append(" - ").append(matricola);

		if (codiceFiscale != null && !codiceFiscale.isEmpty())
			sb.append(" - ").append(codiceFiscale);

		if (roles != null && !roles.isEmpty()) {
			sb.append(" - ").append(roles.toString());
		}

		return sb.toString();
	}
}
