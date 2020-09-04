package it.eng.consolepec.spagicclient.bean.request.fascicolo;

public class FascicoloSaluteSportRequest extends GenericFascicoloRequest {
	
	@Override
	public TipoFascicoloRequest getTipo() {
		return TipoFascicoloRequest.FASCICOLO_SALUTE_SPORT;
	}
}
