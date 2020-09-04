package it.eng.consolepec.spagicclient.bean.request.fascicolo;

public class FascicoloRUEVariantiRequest extends GenericFascicoloRequest{

	@Override
	public TipoFascicoloRequest getTipo() {
		return TipoFascicoloRequest.FASCICOLO_RUE_VARIANTI;
	}

}
