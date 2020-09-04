package it.eng.consolepec.spagicclient.bean.request.fascicolo;

public class FascicoloSportPortoSaragozzaRequest extends GenericFascicoloRequest {
	
	@Override
	public TipoFascicoloRequest getTipo() {
		return TipoFascicoloRequest.FASCICOLO_SPORT_PORTO_SARAGOZZA;
	}
}
