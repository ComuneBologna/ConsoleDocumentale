package it.eng.consolepec.spagicclient.bean.request.fascicolo;


public class FascicoloEntratePubblicitaRequest extends GenericFascicoloRequest {

	@Override
	public TipoFascicoloRequest getTipo() {
		return TipoFascicoloRequest.FASCICOLO_ENTRATE_PUBBLICITA_AUTORIZZAZIONE;
	}

}
