package it.eng.consolepec.xmlplugin.pratica.personale;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo;
import it.eng.consolepec.xmlplugin.factory.DatiPratica;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Setter;

public class DatiFascicoloPersonale extends DatiFascicolo {

	public DatiFascicoloPersonale(String folderPath, String consoleFileName, String provenienza, String titolo, Date dataCreazione, String note, String idDocumentale, String utenteCreazione, String usernameCreazione, List<GruppoVisibilita> gruppoVisibilita, List<DatoAggiuntivo> datiAggiuntivi) {
		super(TipologiaPratica.FASCICOLO_PERSONALE, folderPath, consoleFileName, provenienza, titolo, null, dataCreazione, null, note, idDocumentale, utenteCreazione, usernameCreazione, null, gruppoVisibilita,  datiAggiuntivi);
	}

	public static class Builder extends DatiFascicolo.Builder {
		public Builder() {
		}

		@Setter
		String folderPath, consoleFileName, provenienza, titolo, note, idDocumentale, utenteCreazione, usernameCreazione;
		@Setter
		Date dataCreazione;
		@Setter
		List<GruppoVisibilita> gruppiVisibilita = new ArrayList<DatiPratica.GruppoVisibilita>();
		@Setter
		List<DatoAggiuntivo> datiAggiuntivi = new ArrayList<>();
		
		@Override
		public DatiFascicoloPersonale construct() {
			return new DatiFascicoloPersonale(folderPath, consoleFileName, provenienza, titolo, dataCreazione, note, idDocumentale, utenteCreazione, usernameCreazione, gruppiVisibilita, datiAggiuntivi);

		}
	}

	@Override
	public List<ProtocollazioneCapofila> getProtocollazioniCapofila() {
		return new ArrayList<DatiFascicolo.ProtocollazioneCapofila>();
	}

	@Override
	public List<PraticaCollegata> getPraticheCollegate() {
		return new ArrayList<DatiFascicolo.PraticaCollegata>();
	}

}
