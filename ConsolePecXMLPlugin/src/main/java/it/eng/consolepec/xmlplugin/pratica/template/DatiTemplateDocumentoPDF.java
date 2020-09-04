package it.eng.consolepec.xmlplugin.pratica.template;

import java.util.Date;
import java.util.List;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DatiTemplateDocumentoPDF extends DatiAbstractTemplate {

	@Getter
	@Setter
	String titoloFile;

	public DatiTemplateDocumentoPDF(String folderPath, String consoleFileName, Date dataCreazione, String idDocumentale, String utenteCreazione, String usernameCreazione, Utente inCaricoA,
			List<GruppoVisibilita> gruppiVisibilita, String nome, String descrizione, List<CampoTemplate> campi, List<String> fascicoliAbilitati, String titoloFile) {
		super(folderPath, consoleFileName, dataCreazione, idDocumentale, utenteCreazione, usernameCreazione, inCaricoA, gruppiVisibilita, nome, descrizione, campi, fascicoliAbilitati);

		setTipo(TipologiaPratica.MODELLO_PDF);
		setTitoloFile(titoloFile);
	}

	public static class Builder extends it.eng.consolepec.xmlplugin.pratica.template.DatiAbstractTemplate.Builder {

		@Getter
		@Setter
		String titoloFile;

		public Builder() {
			super();
		}

		@Override
		public DatiTemplateDocumentoPDF construct() {
			return new DatiTemplateDocumentoPDF(folderPath, consoleFileName, dataCreazione, idDocumentale, utenteCreazione, usernameCreazione, inCaricoA, gruppiVisibilita, nome, descrizione, campi,
					fascicoliAbilitati, titoloFile);
		}
	}

}
