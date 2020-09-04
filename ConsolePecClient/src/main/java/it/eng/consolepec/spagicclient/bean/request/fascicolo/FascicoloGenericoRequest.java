package it.eng.consolepec.spagicclient.bean.request.fascicolo;

public class FascicoloGenericoRequest extends GenericFascicoloRequest{

	@Override
	public TipoFascicoloRequest getTipo() {
		return TipoFascicoloRequest.FASCICOLO_GENERICO;
	}

}
