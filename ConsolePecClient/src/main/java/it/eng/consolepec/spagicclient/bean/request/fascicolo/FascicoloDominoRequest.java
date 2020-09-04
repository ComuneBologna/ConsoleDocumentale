package it.eng.consolepec.spagicclient.bean.request.fascicolo;

public class FascicoloDominoRequest extends GenericFascicoloRequest {

	@Override
	public TipoFascicoloRequest getTipo() {
		return TipoFascicoloRequest.FASCICOLO_DOMINO;
	}

}
