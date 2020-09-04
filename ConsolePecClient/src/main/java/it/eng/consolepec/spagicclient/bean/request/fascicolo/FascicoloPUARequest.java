package it.eng.consolepec.spagicclient.bean.request.fascicolo;

public class FascicoloPUARequest extends GenericFascicoloRequest{

	@Override
	public TipoFascicoloRequest getTipo() {
		return TipoFascicoloRequest.FASCICOLO_PUA;
	}

}
