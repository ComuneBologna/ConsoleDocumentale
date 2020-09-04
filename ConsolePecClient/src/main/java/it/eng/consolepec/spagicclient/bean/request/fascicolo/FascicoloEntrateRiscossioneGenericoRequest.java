package it.eng.consolepec.spagicclient.bean.request.fascicolo;

public class FascicoloEntrateRiscossioneGenericoRequest extends GenericFascicoloRequest {

	@Override
	public TipoFascicoloRequest getTipo() {
		return TipoFascicoloRequest.FASCICOLO_ENTRATE_RISCOSSIONE_GENERICO;
	}

}
