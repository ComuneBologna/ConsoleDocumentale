package it.eng.consolepec.spagicclient.bean.request.fascicolo;

public class FascicoloSportSavenaRequest extends GenericFascicoloRequest {
	
	@Override
	public TipoFascicoloRequest getTipo() {
		return TipoFascicoloRequest.FASCICOLO_SPORT_SAVENA;
	}
}
