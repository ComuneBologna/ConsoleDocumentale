package it.eng.consolepec.spagicclient.bean.request.fascicolo;

public class FascicoloSportSaragozzaRequest extends GenericFascicoloRequest {
	
	@Override
	public TipoFascicoloRequest getTipo() {
		return TipoFascicoloRequest.FASCICOLO_SPORT_SARAGOZZA;
	}
}
