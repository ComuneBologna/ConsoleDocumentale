package it.eng.consolepec.spagicclient.bean.request.fascicolo;

public class FascicoloCatastoSitCartografiaRequest extends GenericFascicoloRequest{

	@Override
	public TipoFascicoloRequest getTipo() {
		return TipoFascicoloRequest.FASCICOLO_CATASTO_SIT_CARTOGRAFIA;
	}

}
