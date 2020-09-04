package it.eng.consolepec.spagicclient.bean.request.fascicolo;

public class FascicoloEntrateIseeRequest extends GenericFascicoloRequest {
	
	@Override
	public TipoFascicoloRequest getTipo() {
		return TipoFascicoloRequest.FASCICOLO_ENTRATE_ISEE;
	}
}
