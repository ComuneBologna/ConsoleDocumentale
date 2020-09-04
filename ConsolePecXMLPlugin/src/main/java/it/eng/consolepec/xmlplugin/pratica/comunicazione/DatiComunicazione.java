package it.eng.consolepec.xmlplugin.pratica.comunicazione;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
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
public class DatiComunicazione extends DatiPratica {

	@Getter
	@Setter
	String codComunicazione;

	@Getter
	@Setter
	String descComunicazione;

	@Getter
	@Setter
	String idTemplate;

	@Getter
	@Setter
	Stato stato;

	@Getter
	List<Invio> invii = new ArrayList<Invio>();

	
	
	public DatiComunicazione(String folderPath, String consoleFileName, Date dataCreazione, String idDocumentale, String utenteCreazione, String usernameCreazione, Utente inCaricoA, List<GruppoVisibilita> gruppiVisibilita,
			 String codComunicazione, String descComunicazione, String idTemplate, Stato stato) {
		super();
		
		setFolderPath(folderPath);
		setConsoleFileName(consoleFileName);
		setDataCreazione(dataCreazione);
		setTipo(TipologiaPratica.COMUNICAZIONE);
		setIdDocumentale(idDocumentale);
		setUtenteCreazione(utenteCreazione);
		setInCaricoA(inCaricoA);
		setUsernameCreazione(usernameCreazione);
		setGruppiVisibilita(new TreeSet<DatiPratica.GruppoVisibilita>(gruppiVisibilita));
		

		this.codComunicazione = codComunicazione;
		this.descComunicazione = descComunicazione;
		this.idTemplate = idTemplate;
		this.stato = stato;
		
		
	}

	@EqualsAndHashCode
	public static class Invio {

		@Getter
		@Setter
		String flgTestProd;
		@Getter
		@Setter
		Integer numRecordTest;
		@Getter
		@Setter
		String pecDestinazioneTest;
		@Getter
		@Setter
		String codEsito;
	}

	public enum Stato {
		IN_GESTIONE("In gestione"), TERMINATA("Terminata");

		private String label;

		Stato(String label) {
			this.label = label;
		}

		public String getLabel() {
			return label;
		}
	}

	public static class Builder {

		@Setter
		String folderPath, consoleFileName,  idDocumentale, utenteCreazione, usernameCreazione;
		@Setter
		Utente inCaricoA;
		@Setter
		Date dataCreazione;
		@Setter
		List<GruppoVisibilita> gruppiVisibilita = new ArrayList<DatiPratica.GruppoVisibilita>();
		
		
		@Setter
		Integer idComunicazione;
		@Setter
		String codComunicazione;
		@Setter
		String descComunicazione;
		@Setter
		String idTemplate;
		@Setter
		Stato stato;

		public Builder() {
		}

		public DatiComunicazione construct() {
			return new DatiComunicazione(folderPath, consoleFileName, dataCreazione, idDocumentale, utenteCreazione, usernameCreazione, inCaricoA, gruppiVisibilita,
					codComunicazione, descComunicazione, idTemplate, stato);
		}

	}
	
}
