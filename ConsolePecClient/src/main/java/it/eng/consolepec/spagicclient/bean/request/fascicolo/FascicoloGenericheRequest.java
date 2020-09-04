package it.eng.consolepec.spagicclient.bean.request.fascicolo;

public class FascicoloGenericheRequest extends GenericFascicoloRequest{

	@Override
	public TipoFascicoloRequest getTipo() {
		return TipoFascicoloRequest.FASCICOLO_GENERICHE;
	}

}
