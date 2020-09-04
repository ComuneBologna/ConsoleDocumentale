package it.eng.consolepec.spagicclient.bean.request.fascicolo;

public class FascicoloOrdiniRequest extends GenericFascicoloRequest{

	@Override
	public TipoFascicoloRequest getTipo() {
		return TipoFascicoloRequest.FASCICOLO_ORDINI;
	}

}
