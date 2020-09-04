package it.eng.consolepec.spagicclient.bean.request.fascicolo;

public class FascicoloAlboPretorioRequest extends GenericFascicoloRequest {
	
	@Override
	public TipoFascicoloRequest getTipo() {
		return TipoFascicoloRequest.FASCICOLO_ALBO_PRETORIO;
	}
}
