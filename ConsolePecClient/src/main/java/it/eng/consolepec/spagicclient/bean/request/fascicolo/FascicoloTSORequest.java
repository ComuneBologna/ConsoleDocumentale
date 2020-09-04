package it.eng.consolepec.spagicclient.bean.request.fascicolo;


public class FascicoloTSORequest extends GenericFascicoloRequest {

	@Override
	public TipoFascicoloRequest getTipo() {
		return TipoFascicoloRequest.FASCICOLO_TSO;
	}

}
