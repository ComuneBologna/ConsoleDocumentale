package it.eng.consolepec.spagicclient.bean.request.fascicolo;


public class FascicoloElettoraleElettoreRequest extends GenericFascicoloRequest {

	@Override
	public TipoFascicoloRequest getTipo() {
		return TipoFascicoloRequest.FASCICOLO_ELETTORALE_ELETTORE;
	}

}
