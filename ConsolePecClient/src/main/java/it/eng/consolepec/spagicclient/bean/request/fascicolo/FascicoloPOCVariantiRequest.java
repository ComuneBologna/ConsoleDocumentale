package it.eng.consolepec.spagicclient.bean.request.fascicolo;

public class FascicoloPOCVariantiRequest extends GenericFascicoloRequest{

	@Override
	public TipoFascicoloRequest getTipo() {
		return TipoFascicoloRequest.FASCICOLO_POC_VARIANTI;
	}

}
