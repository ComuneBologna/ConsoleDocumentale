package it.eng.consolepec.spagicclient.bean.request.fascicolo;

public class FascicoloEntrateRequest extends GenericFascicoloRequest {

	@Override
	public TipoFascicoloRequest getTipo() {
		return TipoFascicoloRequest.FASCICOLO_ENTRATE_RISCOSSIONE;
	}

}
