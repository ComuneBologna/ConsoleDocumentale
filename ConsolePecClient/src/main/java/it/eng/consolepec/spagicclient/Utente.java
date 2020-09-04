package it.eng.consolepec.spagicclient;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.util.utente.UtenteUtils;

/**
 *
 * @author Roberto
 *
 *         Dto per il passaggio di informazioni dell'utente all'Api
 *
 */
public class Utente implements Serializable {

	private static final long serialVersionUID = -1184839076214282344L;
	private String nome, cognome, username, matricola, codicefiscale;
	private List<String> ruoli;
	private Date dataPresaInCarico;
	private boolean utenteEsterno;

	public Utente(String nome, String cognome, String username, String matricola, String codicefiscale, List<String> ruoli, Date dataPresaInCarico, boolean utenteEsterno) {
		this(nome, cognome, username, matricola, codicefiscale, ruoli, dataPresaInCarico);
		setUtenteEsterno(utenteEsterno);
	}

	public Utente(String nome, String cognome, String username, String matricola, String codicefiscale, List<String> ruoli, Date dataPresaInCarico) {
		super();
		this.nome = nome;
		this.cognome = cognome;
		this.username = username;
		this.matricola = matricola;
		this.codicefiscale = codicefiscale;
		this.ruoli = ruoli;
		this.dataPresaInCarico = dataPresaInCarico;
		this.utenteEsterno = false;
	}

	public Utente(String nome, String cognome, String username, String matricola, String codicefiscale, List<String> ruoli) {
		this(nome, cognome, username, matricola, codicefiscale, ruoli, new Date());
	}

	public Date getDataPresaInCarico() {
		return dataPresaInCarico;
	}

	public void setDataPresaInCarico(Date dataPresaInCarico) {
		this.dataPresaInCarico = dataPresaInCarico;
	}

	public String getMatricola() {
		return matricola;
	}

	public void setMatricola(String matricola) {
		this.matricola = matricola;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
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

	public List<String> getRuoli() {
		return ruoli;
	}

	public String getFullName() {
		return (sanitizeNull(nome) + " " + sanitizeNull(cognome)).trim();
	}

	public boolean isUtenteEsterno() {
		return utenteEsterno;
	}

	public void setUtenteEsterno(boolean utenteEsterno) {
		this.utenteEsterno = utenteEsterno;
	}

	@Override
	public String toString() {
		return UtenteUtils.calcolaRuoloPersonale(nome, cognome, codicefiscale, matricola);
	}

	private static String sanitizeNull(String s) {
		if (s == null || s.isEmpty())
			return "";
		return s;
	}

	public String getUserRoleName() {
		return UtenteUtils.calcolaEtichettaRuoloPersonale(nome, cognome, codicefiscale, matricola);
	}

	public String getCodicefiscale() {
		return codicefiscale;
	}

	public void setCodicefiscale(String codicefiscale) {
		this.codicefiscale = codicefiscale;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codicefiscale == null) ? 0 : codicefiscale.hashCode());
		result = prime * result + ((cognome == null) ? 0 : cognome.hashCode());
		result = prime * result + ((dataPresaInCarico == null) ? 0 : dataPresaInCarico.hashCode());
		result = prime * result + ((matricola == null) ? 0 : matricola.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((ruoli == null) ? 0 : ruoli.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Utente other = (Utente) obj;
		if (codicefiscale == null) {
			if (other.codicefiscale != null)
				return false;
		} else if (!codicefiscale.equals(other.codicefiscale))
			return false;
		if (cognome == null) {
			if (other.cognome != null)
				return false;
		} else if (!cognome.equals(other.cognome))
			return false;
		if (dataPresaInCarico == null) {
			if (other.dataPresaInCarico != null)
				return false;
		} else if (!dataPresaInCarico.equals(other.dataPresaInCarico))
			return false;
		if (matricola == null) {
			if (other.matricola != null)
				return false;
		} else if (!matricola.equals(other.matricola))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (ruoli == null) {
			if (other.ruoli != null)
				return false;
		} else if (!ruoli.equals(other.ruoli))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	public AnagraficaRuolo getRuoloPersonale() {
		return UtenteUtils.getAnagraficaRuoloPersonale(nome, cognome, codicefiscale, matricola);
	}

}
