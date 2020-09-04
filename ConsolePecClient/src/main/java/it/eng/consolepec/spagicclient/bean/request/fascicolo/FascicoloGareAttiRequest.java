package it.eng.consolepec.spagicclient.bean.request.fascicolo;

public class FascicoloGareAttiRequest extends GenericFascicoloRequest {

	@Override
	public TipoFascicoloRequest getTipo() {
		return TipoFascicoloRequest.FASCICOLO_GARE_ATTI;
	}

}
