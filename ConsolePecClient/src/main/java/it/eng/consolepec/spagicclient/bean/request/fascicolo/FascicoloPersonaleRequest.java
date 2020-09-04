package it.eng.consolepec.spagicclient.bean.request.fascicolo;


public class FascicoloPersonaleRequest extends GenericFascicoloRequest {
	
	@Override
	public TipoFascicoloRequest getTipo() {
		return TipoFascicoloRequest.FASCICOLO_PERSONALE;
	}

}
