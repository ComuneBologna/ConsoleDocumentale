package it.eng.consolepec.xmlplugin.pratica.sport;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Setter;

public class DatiFascicoloSportSanDonatoSanVitale extends DatiFascicolo{
	
	public DatiFascicoloSportSanDonatoSanVitale(String folderPath, String consoleFileName, String provenienza, String titolo, String numeroPG, Date dataCreazione, Integer annoPG, String note, String idDocumentale, String utenteCreazione, String usernameCreazione, Utente inCaricoA,
			List<GruppoVisibilita> gruppiVisibilita, List<DatoAggiuntivo>  datiAggiuntivi) {
					super(TipologiaPratica.FASCICOLO_SPORT_SANDONATO_SANVITALE, folderPath, consoleFileName, provenienza, titolo, numeroPG, dataCreazione, annoPG, note, idDocumentale, utenteCreazione, usernameCreazione, inCaricoA, gruppiVisibilita, datiAggiuntivi);
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
		List<GruppoVisibilita> gruppiVisibilita = new ArrayList<GruppoVisibilita>();
		@Setter
		List<DatoAggiuntivo>  datiAggiuntivi = new ArrayList<>();
	
		@Override
		public DatiFascicoloSportSanDonatoSanVitale construct() {
			return new DatiFascicoloSportSanDonatoSanVitale(folderPath, consoleFileName, provenienza, titolo, numeroPG, dataCreazione, annoPG, note, idDocumentale, utenteCreazione, usernameCreazione, inCaricoA, gruppiVisibilita, datiAggiuntivi);

		}
	}
}
