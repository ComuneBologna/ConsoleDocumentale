package it.eng.consolepec.spagicclient.bean.request.fascicolo;


public class FascicoloElettoraleGenericoRequest extends GenericFascicoloRequest {

	@Override
	public TipoFascicoloRequest getTipo() {
		return TipoFascicoloRequest.FASCICOLO_ELETTORALE_GENERICO;
	}

}
