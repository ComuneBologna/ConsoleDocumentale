package it.eng.consolepec.spagicclient.bean.response.validazione;

import java.util.ArrayList;
import java.util.List;

public class ValidazioneEdErroriDatiAggiuntivi {
	private List<String> errori = new ArrayList<String>();
	private List<ValidazioneDatoAggiuntivo> validazione = new ArrayList<ValidazioneDatoAggiuntivo>();
	public List<String> getErrori() {
		return errori;
	}
	public List<ValidazioneDatoAggiuntivo> getValidazione() {
		return validazione;
	}
}
