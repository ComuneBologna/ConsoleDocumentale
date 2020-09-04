package it.eng.consolepec.xmlplugin.pratica.riservato;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo;
import it.eng.consolepec.xmlplugin.factory.DatiPratica;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DatiFascicoloRiservato extends DatiFascicolo {

	public DatiFascicoloRiservato(String folderPath, String consoleFileName, String provenienza, String titolo, String numeroPG, Date dataCreazione, Integer annoPG, String note, String idDocumentale, String utenteCreazione, String usernameCreazione, Utente inCaricoA,
			List<GruppoVisibilita> gruppiVisibilita, List<DatoAggiuntivo> datiAggiuntivi) {
		super(TipologiaPratica.FASCICOLO_RISERVATO, folderPath, consoleFileName, provenienza, titolo, numeroPG, dataCreazione, annoPG, note, idDocumentale, utenteCreazione, usernameCreazione, inCaricoA, gruppiVisibilita, datiAggiuntivi);
	}

	public static class Builder extends DatiFascicolo.Builder {
		public Builder() {
		}

		@Setter
		String folderPath, consoleFileName, provenienza, titolo, numeroPG, note, idDocumentale, utenteCreazione, usernameCreazione;
		@Setter
		Utente inCaricoA;
		@Setter
		Date dataCreazione;
		@Setter
		Integer annoPG;
		@Setter
		List<GruppoVisibilita> gruppiVisibilita = new ArrayList<DatiPratica.GruppoVisibilita>();
		@Setter
		List<DatoAggiuntivo> datiAggiuntivi = new ArrayList<>();
		
		@Override
		public DatiFascicoloRiservato construct() {
			return new DatiFascicoloRiservato(folderPath, consoleFileName, provenienza, titolo, numeroPG, dataCreazione, annoPG, note, idDocumentale, utenteCreazione, usernameCreazione, inCaricoA, gruppiVisibilita, datiAggiuntivi);
		}

	}

}
