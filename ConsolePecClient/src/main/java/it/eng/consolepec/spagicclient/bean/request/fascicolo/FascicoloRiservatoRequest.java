package it.eng.consolepec.spagicclient.bean.request.fascicolo;

public class FascicoloRiservatoRequest extends GenericFascicoloRequest {

	@Override
	public TipoFascicoloRequest getTipo() {
		return TipoFascicoloRequest.FASCICOLO_RISERVATO;
	}

}
