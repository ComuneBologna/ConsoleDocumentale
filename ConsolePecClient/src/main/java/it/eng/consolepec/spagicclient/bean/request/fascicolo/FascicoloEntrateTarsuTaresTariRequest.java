package it.eng.consolepec.spagicclient.bean.request.fascicolo;

public class FascicoloEntrateTarsuTaresTariRequest extends GenericFascicoloRequest {

	@Override
	public TipoFascicoloRequest getTipo() {
		return TipoFascicoloRequest.FASCICOLO_ENTRATE_TARSU_TARES_TARI;
	}

}
