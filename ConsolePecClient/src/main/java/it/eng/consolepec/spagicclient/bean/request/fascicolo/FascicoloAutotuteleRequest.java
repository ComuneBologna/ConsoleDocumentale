package it.eng.consolepec.spagicclient.bean.request.fascicolo;

public class FascicoloAutotuteleRequest extends GenericFascicoloRequest{

	@Override
	public TipoFascicoloRequest getTipo() {
		return TipoFascicoloRequest.FASCICOLO_AUTOTUTELE;
	}

}
