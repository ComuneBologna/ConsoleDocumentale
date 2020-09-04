package it.eng.consolepec.spagicclient.bean.request.fascicolo;


public class FascicoloEntrateImposteSoggiornoRequest extends GenericFascicoloRequest {

	@Override
	public TipoFascicoloRequest getTipo() {
		return TipoFascicoloRequest.FASCICOLO_ENTRATE_IMPOSTE_SOGGIORNO;
	}

}
