package it.eng.consolepec.xmlplugin.pratica.fatturazione;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DatiFascicoloFatturazione extends DatiFascicolo {

	public DatiFascicoloFatturazione(String folderPath, String consoleFileName, String provenienza, String titolo, String numeroPG, Date dataCreazione, Integer annoPG, String note, String idDocumentale, String utenteCreazione, String usernameCreazione, Utente inCaricoA,
			List<GruppoVisibilita> gruppiVisibilita, String numeroFattura, String ragioneSociale, String pIva, String codicePIva, List<DatoAggiuntivo> datiAggiuntivi) {
		super(TipologiaPratica.FASCICOLO_FATTURAZIONE_ELETTRONICA, folderPath, consoleFileName, provenienza, titolo, numeroPG, dataCreazione, annoPG, note, idDocumentale, utenteCreazione, usernameCreazione, inCaricoA, gruppiVisibilita, datiAggiuntivi);
		this.numeroFattura = numeroFattura;
		this.ragioneSociale = ragioneSociale;
		this.pIva = pIva;
		this.codicePIva = codicePIva;
	}

	@Getter
	private String numeroFattura, ragioneSociale, pIva, codicePIva;

	public static class Builder extends DatiFascicolo.Builder {
		public Builder() {

		}

		@Setter
		String folderPath, consoleFileName, provenienza, titolo, numeroPG, note, idDocumentale, numeroFattura, ragioneSociale, pIva, codicePIva, utenteCreazione, usernameCreazione;
		@Setter
		Utente inCaricoA;
		@Setter
		Date dataCreazione;
		@Setter
		Integer annoPG;
		@Setter
		List<GruppoVisibilita> gruppiVisibilita = new ArrayList<GruppoVisibilita>();
		@Setter
		List<DatoAggiuntivo> datiAggiuntivi = new ArrayList<>();
		
		@Override
		public DatiFascicoloFatturazione construct() {
			return new DatiFascicoloFatturazione(folderPath, consoleFileName, provenienza, titolo, numeroPG, dataCreazione, annoPG, note, idDocumentale, utenteCreazione, usernameCreazione, inCaricoA, gruppiVisibilita, numeroFattura, ragioneSociale, pIva, codicePIva, datiAggiuntivi);

		}

	}

}
