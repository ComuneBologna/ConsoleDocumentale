package it.eng.consolepec.spagicclient.bean.request.fascicolo;

public class FascicoloSportSandonatoSanvitaleRequest extends GenericFascicoloRequest {
	
	@Override
	public TipoFascicoloRequest getTipo() {
		return TipoFascicoloRequest.FASCICOLO_SPORT_SANDONATO_SANVITALE;
	}
}
