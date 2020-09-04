package it.eng.portlet.consolepec.gwt.shared.model;

import java.math.BigInteger;
import java.util.TreeSet;

/**
 * Bean di una generica Email. Le versioni concrete sono quelle che specializzano IN or OUT.
 * 
 * @author pluttero
 * 
 */
public abstract class PecDTO extends PraticaDTO {
	private String body, tipoEmail, mittente, mailId;
	private TreeSet<String> idPraticheCollegate = new TreeSet<String>();
	private TreeSet<Destinatario> destinatari = new TreeSet<Destinatario>();
	private TreeSet<Destinatario> destinatariCC = new TreeSet<Destinatario>();
	private Destinatario destinatarioPrincipale;
	private boolean protocollabile;
	private TreeSet<String> destinatariInoltro = new TreeSet<String>();
	private BigInteger progressivoInoltro;
	private boolean interoperabile;
	private String replyTo;
	
	protected PecDTO() {
		super();// serialization
	}

	public PecDTO(String path) {
		super(path);
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
	
	public String getTipoEmail() {
		return tipoEmail;
	}

	public void setTipoEmail(String tipoEmail) {
		this.tipoEmail = tipoEmail;
	}

	public String getMittente() {
		return mittente;
	}

	public void setMittente(String mittente) {
		this.mittente = mittente;
	}

	public String getMailId() {
		return mailId;
	}

	public void setMailId(String mailId) {
		this.mailId = mailId;
	}

	public boolean isProtocollabile() {
		return protocollabile;
	}

	public void setProtocollabile(boolean protocollabile) {
		this.protocollabile = protocollabile;
	}

	public TreeSet<Destinatario> getDestinatari() {
		return destinatari;
	}

	public void setDestinatari(TreeSet<Destinatario> destinatari) {
		this.destinatari = destinatari;
	}

	public TreeSet<Destinatario> getDestinatariCC() {
		return destinatariCC;
	}

	public void setDestinatariCC(TreeSet<Destinatario> destinatariCC) {
		this.destinatariCC = destinatariCC;
	}

	public Destinatario getDestinatarioPrincipale() {
		return destinatarioPrincipale;
	}

	public void setDestinatarioPrincipale(Destinatario destinatarioPrincipale) {
		this.destinatarioPrincipale = destinatarioPrincipale;
	}

	public BigInteger getProgressivoInoltro() {
		return progressivoInoltro;
	}

	public void setProgressivoInoltro(BigInteger progressivoInoltro) {
		this.progressivoInoltro = progressivoInoltro;
	}

	public TreeSet<String> getDestinatariInoltro() {
		return destinatariInoltro;
	}

	public TreeSet<String> getIdPraticheCollegate() {
		return idPraticheCollegate;
	}
	public boolean isInteroperabile() {
		return interoperabile;
	}

	public void setInteroperabile(boolean interoperabile) {
		this.interoperabile = interoperabile;
	}

	public String getReplyTo() {
		return replyTo;
	}

	public void setReplyTo(String replyTo) {
		this.replyTo = replyTo;
	}
	

}
