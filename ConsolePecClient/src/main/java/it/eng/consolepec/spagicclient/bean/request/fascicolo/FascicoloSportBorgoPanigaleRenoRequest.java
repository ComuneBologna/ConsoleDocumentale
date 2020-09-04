package it.eng.consolepec.spagicclient.bean.request.fascicolo;

public class FascicoloSportBorgoPanigaleRenoRequest extends GenericFascicoloRequest {
	
	@Override
	public TipoFascicoloRequest getTipo() {
		return TipoFascicoloRequest.FASCICOLO_SPORT_BORGOPANIGALE_RENO;
	}
}
