package it.eng.consolepec.spagicclient.bean.request.fascicolo;

public class FascicoloResiRequest extends GenericFascicoloRequest{

	@Override
	public TipoFascicoloRequest getTipo() {
		return TipoFascicoloRequest.FASCICOLO_RESI;
	}

}
