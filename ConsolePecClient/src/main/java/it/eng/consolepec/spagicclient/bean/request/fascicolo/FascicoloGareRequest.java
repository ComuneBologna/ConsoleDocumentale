package it.eng.consolepec.spagicclient.bean.request.fascicolo;

public class FascicoloGareRequest extends GenericFascicoloRequest {

	@Override
	public TipoFascicoloRequest getTipo() {
		return TipoFascicoloRequest.FASCICOLO_GARE;
	}

}
