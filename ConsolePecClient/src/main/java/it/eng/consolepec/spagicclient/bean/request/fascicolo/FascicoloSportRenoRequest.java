package it.eng.consolepec.spagicclient.bean.request.fascicolo;

public class FascicoloSportRenoRequest extends GenericFascicoloRequest {
	
	@Override
	public TipoFascicoloRequest getTipo() {
		return TipoFascicoloRequest.FASCICOLO_SPORT_RENO;
	}
}
