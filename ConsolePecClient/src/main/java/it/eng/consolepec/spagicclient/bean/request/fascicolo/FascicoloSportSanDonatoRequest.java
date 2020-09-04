package it.eng.consolepec.spagicclient.bean.request.fascicolo;

public class FascicoloSportSanDonatoRequest extends GenericFascicoloRequest {
	
	@Override
	public TipoFascicoloRequest getTipo() {
		return TipoFascicoloRequest.FASCICOLO_SPORT_SANDONATO;
	}
}
