package it.eng.consolepec.spagicclient.bean.request.fascicolo;


public class FascicoloEntrateSanzioniRequest extends GenericFascicoloRequest {

	@Override
	public TipoFascicoloRequest getTipo() {
		return TipoFascicoloRequest.FASCICOLO_ENTRATE_SANZIONI_AMMINISTRATIVE;
	}

}
