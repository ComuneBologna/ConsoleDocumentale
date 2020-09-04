package it.eng.consolepec.xmlplugin.pratica.template;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DatiTemplateEmail extends DatiAbstractTemplate {

	@Getter
	@Setter
	String oggettoMail;
	@Getter
	@Setter
	String corpoMail;
	@Getter
	@Setter
	String mittente;
	@Getter
	@Setter
	List<String> destinatari = new ArrayList<String>();
	@Getter
	@Setter
	List<String> destinatariCC = new ArrayList<String>();

	public DatiTemplateEmail(String folderPath, String consoleFileName, Date dataCreazione, String idDocumentale, String utenteCreazione, String usernameCreazione, Utente inCaricoA,
			List<GruppoVisibilita> gruppiVisibilita, String nome, String descrizione, String oggettoMail, String corpoMail, String mittente, List<String> destinatari, List<String> destinatariCC,
			List<CampoTemplate> campi, List<String> fascicoliAbilitati) {
		super(folderPath, consoleFileName, dataCreazione, idDocumentale, utenteCreazione, usernameCreazione, inCaricoA, gruppiVisibilita, nome, descrizione, campi, fascicoliAbilitati);

		setTipo(TipologiaPratica.MODELLO_MAIL);

		setOggettoMail(oggettoMail);
		setCorpoMail(corpoMail);
		setMittente(mittente);
		setDestinatari(destinatari);
		setDestinatariCC(destinatariCC);

	}

	public static class Builder extends it.eng.consolepec.xmlplugin.pratica.template.DatiAbstractTemplate.Builder {

		@Setter
		String oggettoMail;
		@Setter
		String corpoMail;
		@Setter
		String mittente;
		@Setter
		List<String> destinatari = new ArrayList<String>();
		@Setter
		List<String> destinatariCC = new ArrayList<String>();

		public Builder() {
			super();
		}

		@Override
		public DatiTemplateEmail construct() {
			return new DatiTemplateEmail(folderPath, consoleFileName, dataCreazione, idDocumentale, utenteCreazione, usernameCreazione, inCaricoA, gruppiVisibilita, nome, descrizione, oggettoMail,
					corpoMail, mittente, destinatari, destinatariCC, campi, fascicoliAbilitati);
		}

	}
}
