package it.eng.consolepec.spagicclient.bean.request.fascicolo;

public class FascicoloSportNavileRequest extends GenericFascicoloRequest {
	
	@Override
	public TipoFascicoloRequest getTipo() {
		return TipoFascicoloRequest.FASCICOLO_SPORT_NAVILE;
	}
}
