package it.eng.consolepec.spagicclient.bean.request.fascicolo;

public class FascicoloPSCVariantiRequest extends GenericFascicoloRequest{

	@Override
	public TipoFascicoloRequest getTipo() {
		return TipoFascicoloRequest.FASCICOLO_PSC_VARIANTI;
	}

}
