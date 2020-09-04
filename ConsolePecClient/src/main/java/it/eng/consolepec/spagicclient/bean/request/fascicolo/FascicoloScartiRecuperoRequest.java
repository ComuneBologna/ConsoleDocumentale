package it.eng.consolepec.spagicclient.bean.request.fascicolo;

public class FascicoloScartiRecuperoRequest extends GenericFascicoloRequest{

	@Override
	public TipoFascicoloRequest getTipo() {
		return TipoFascicoloRequest.FASCICOLO_SCARTI_RECUPERO;
	}

}
