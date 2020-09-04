package it.eng.cobo.consolepec.commons.ldap;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class LdapQueryFilter {

	private List<String> gruppi = new ArrayList<>();
	private String utente;
	private String nome;
	private String cognome;
	private String nomeOCognome;

	private Integer limit;

}
