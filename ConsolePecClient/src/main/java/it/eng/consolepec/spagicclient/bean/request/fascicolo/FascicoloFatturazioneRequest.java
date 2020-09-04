package it.eng.consolepec.spagicclient.bean.request.fascicolo;

import lombok.Getter;
import lombok.Setter;

public class FascicoloFatturazioneRequest extends GenericFascicoloRequest {

	@Getter
	@Setter
	private String numeroFattura, ragioneSociale, pIva, codicePIva;
	
	@Override
	public TipoFascicoloRequest getTipo() {
		return TipoFascicoloRequest.FASCICOLO_FATTURAZIONE_ELETTRONICA;
	}

}
