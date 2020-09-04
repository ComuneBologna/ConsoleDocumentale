package it.eng.consolepec.xmlplugin.pratica.template;

import it.eng.consolepec.xmlplugin.factory.DatiPratica;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DatiAbstractTemplate extends DatiPratica {

	@Getter
	@Setter
	String nome;
	@Getter
	@Setter
	String descrizione;
	@Getter
	@Setter
	List<CampoTemplate> campi = new ArrayList<CampoTemplate>();
	@Getter
	@Setter
	List<String> fascicoliAbilitati = new ArrayList<>();
	@Getter
	@Setter
	Stato stato;

	public DatiAbstractTemplate(String folderPath, String consoleFileName, Date dataCreazione, String idDocumentale, String utenteCreazione, String usernameCreazione, Utente inCaricoA,
			List<GruppoVisibilita> gruppiVisibilita, String nome, String descrizione, List<CampoTemplate> campi, List<String> fascicoliAbilitati) {
		setFolderPath(folderPath);
		setConsoleFileName(consoleFileName);
		setDataCreazione(dataCreazione);
		setIdDocumentale(idDocumentale);
		setUtenteCreazione(utenteCreazione);
		setInCaricoA(inCaricoA);
		setUsernameCreazione(usernameCreazione);
		setGruppiVisibilita(new TreeSet<DatiPratica.GruppoVisibilita>(gruppiVisibilita));
		setNome(nome);
		setDescrizione(descrizione);
		setCampi(campi);
		setFascicoliAbilitati(fascicoliAbilitati);
	}



	public enum Stato {
		IN_GESTIONE("In gestione"), BOZZA("Bozza");

		private String label;

		Stato(String label) {
			this.label = label;
		}

		public String getLabel() {
			return label;
		}

		public static Stato fromLabel(String label) {
			for (Stato stato : Stato.values())
				if(stato.getLabel().equalsIgnoreCase(label))
					return stato;
			return null;
		}
	}

	public static class Builder {


		@Setter
		protected String folderPath, consoleFileName,  idDocumentale, utenteCreazione, usernameCreazione;
		@Setter
		protected Utente inCaricoA;
		@Setter
		protected Date dataCreazione;
		@Setter
		protected List<GruppoVisibilita> gruppiVisibilita = new ArrayList<DatiPratica.GruppoVisibilita>();
		@Setter
		protected String nome;
		@Setter
		protected String descrizione;
		@Setter
		protected List<CampoTemplate> campi = new ArrayList<CampoTemplate>();
		@Setter
		protected List<String> fascicoliAbilitati = new ArrayList<>();

		public Builder() {
		}

		public DatiAbstractTemplate construct() {
			return new DatiAbstractTemplate(folderPath, consoleFileName, dataCreazione, idDocumentale, utenteCreazione, usernameCreazione, inCaricoA, gruppiVisibilita,
					 nome,  descrizione, campi, fascicoliAbilitati);
		}
	}
}
