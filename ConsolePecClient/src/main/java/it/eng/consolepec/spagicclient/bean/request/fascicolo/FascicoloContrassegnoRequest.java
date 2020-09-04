package it.eng.consolepec.spagicclient.bean.request.fascicolo;

public class FascicoloContrassegnoRequest extends GenericFascicoloRequest {
	
	@Override
	public TipoFascicoloRequest getTipo() {
		return TipoFascicoloRequest.FASCICOLO_CONTRASSEGNO;
	}
}
