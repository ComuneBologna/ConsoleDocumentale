package it.eng.consolepec.spagicclient.bean.request.fascicolo;

public class FascicoloSportSantoStefanoRequest extends GenericFascicoloRequest {
	
	@Override
	public TipoFascicoloRequest getTipo() {
		return TipoFascicoloRequest.FASCICOLO_SPORT_SANTOSTEFANO;
	}
}
