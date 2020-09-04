package it.eng.consolepec.spagicclient.model.taskfirma;

/**
 *
 * Bean destinatario per la proposta
 *
 * @author biagiot
 *
 */
public class DestinatarioUtenteTaskFirmaClient extends DestinatarioTaskFirmaClient {
	private String nome;
	private String cognome;
	private String username;
	private String matricola;
	private String settore;

	public DestinatarioUtenteTaskFirmaClient(String username, String nome, String cognome, String matricola, String settore) {
		this.username = username;
		this.nome = nome;
		this.cognome = cognome;
		this.settore = settore;
		this.matricola = matricola;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMatricola() {
		return matricola;
	}

	public void setMatricola(String matricola) {
		this.matricola = matricola;
	}

	public String getSettore() {
		return settore;
	}

	public void setSettore(String settore) {
		this.settore = settore;
	}
}
