package it.eng.cobo.consolepec.commons.authentication;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class UtenzaTecnica {

	private String username;
	private String password;
	private String nome;
	private String cognome;
	private String matricola;
	private String codiceFiscale;
	private List<String> ruoli = new ArrayList<>();
}
