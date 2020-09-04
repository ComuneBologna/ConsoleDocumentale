package it.eng.portlet.consolepec.gwt.shared.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Template Mail
 *
 * @author biagiot
 *
 */
public class TemplateDTO extends BaseTemplateDTO {

	private String oggettoMail;
	private String corpoMail;
	private String mittente;
	private List<String> destinatari = new ArrayList<String>();
	private List<String> destinatariCC = new ArrayList<String>();

	private boolean caricaAllegatoAbilitato, eliminaAllegatoAbilitato, creaComunicazioneButtonAbilitato;

	public TemplateDTO() {
		super();
	}

	public TemplateDTO(String alfrescoPath) {
		super(alfrescoPath);
	}

	public String getOggettoMail() {
		return oggettoMail;
	}

	public void setOggettoMail(String oggettoMail) {
		this.oggettoMail = oggettoMail;
	}

	public String getCorpoMail() {
		return corpoMail;
	}

	public void setCorpoMail(String corpoMail) {
		this.corpoMail = corpoMail;
	}

	public String getMittente() {
		return mittente;
	}

	public void setMittente(String mittente) {
		this.mittente = mittente;
	}

	public List<String> getDestinatari() {
		return destinatari;
	}

	public List<String> getDestinatariCC() {
		return destinatariCC;
	}

	public boolean isCaricaAllegatoAbilitato() {
		return caricaAllegatoAbilitato;
	}

	public void setCaricaAllegatoAbilitato(boolean caricaAllegatoAbilitato) {
		this.caricaAllegatoAbilitato = caricaAllegatoAbilitato;
	}

	public boolean isEliminaAllegatoAbilitato() {
		return eliminaAllegatoAbilitato;
	}

	public void setEliminaAllegatoAbilitato(boolean eliminaAllegatoAbilitato) {
		this.eliminaAllegatoAbilitato = eliminaAllegatoAbilitato;
	}

	public boolean isCreaComunicazioneButtonAbilitato() {
		return creaComunicazioneButtonAbilitato;
	}

	public void setCreaComunicazioneButtonAbilitato(boolean creaComunicazioneButtonAbilitato) {
		this.creaComunicazioneButtonAbilitato = creaComunicazioneButtonAbilitato;
	}

	@Override
	public void accept(ModelloVisitor v) {
		v.visit(this);
	}
}
