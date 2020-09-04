package it.eng.consolepec.xmlplugin.pratica.email;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.eng.consolepec.xmlplugin.factory.DatiPraticaTaskAdapter;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Destinatario;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Interoperabile;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.ProtocollazionePEC;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Ricevuta;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.TipoEmail;

/**
 * Classe wrapper per modificare l'accesso a DatiEmail
 *
 *
 */

public class DatiPraticaEmailTaskAdapter extends DatiPraticaTaskAdapter {
	private DatiEmail datiemail;

	DatiPraticaEmailTaskAdapter(DatiEmail dp) {
		super(dp);
		this.datiemail = dp;
	}

	/*
	 * Metodi per la composizione di una mail in uscita *
	 */
	public void setMittente(String mittente) {
		datiemail.setMittente(mittente);
	}

	public void setOggetto(String oggetto) {
		datiemail.setOggetto(oggetto);
	}

	public void setDestinatarioPrincipale(String destinatarioPrincipale) {
		datiemail.setDestinatarioPrincipale(new Destinatario(destinatarioPrincipale, null, it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Destinatario.TipoEmail.esterno, false, false));
	}

	public void setBody(String body) {
		datiemail.setBody(body);
	}

	public void setFirma(String firma) {
		datiemail.setFirma(firma);
	}

	public void setTipoEmail(TipoEmail tipoEmail) {
		datiemail.setTipoEmail(tipoEmail);
	}

	public void setStato(DatiEmail.Stato stato) {
		datiemail.setStato(stato);
	}

	public void setDataInvio(Date dataInvio) {
		datiemail.setDataInvio(dataInvio);
	}

	public int getTotaleDestinatari() {
		return datiemail.getDestinatari().size() + (datiemail.getDestinatarioPrincipale() != null ? 1 : 0);
	}

	public void setMessageId(String messageID) {
		datiemail.setMessageID(messageID);
	}

	public void setProtocollazionePEC(ProtocollazionePEC protocollazionePEC) {
		if (datiemail.getProtocollazionePec() != null)
			throw new RuntimeException("La pratica e' gia' protocollata");
		datiemail.setProtocollazionePec(protocollazionePEC);
	}

	public void setInteroperabile(boolean interoperabile) {
		datiemail.setInteroperabile(new Interoperabile());

	}

	public void addRicevuta(Ricevuta ricevuta) {
		List<Ricevuta> ricevute = datiemail.getRicevute();
		ricevute.add(ricevuta);
		datiemail.setRicevute(ricevute);
	}

	public void setDestinatari(List<String> destinatari) {
		if (destinatari == null)
			destinatari = new ArrayList<String>();

		datiemail.getDestinatari().clear();

		for (String destinatario : destinatari) {
			datiemail.getDestinatari().add(new Destinatario(destinatario, it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Destinatario.TipoEmail.esterno));
		}
	}

	public void setDestinatariCC(List<String> destinatariCC) {
		if (destinatariCC == null)
			destinatariCC = new ArrayList<String>();

		datiemail.getDestinatariCC().clear();

		for (String destinatario : destinatariCC) {
			datiemail.getDestinatariCC().add(new Destinatario(destinatario, it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Destinatario.TipoEmail.esterno));
		}
	}

}
