package it.eng.consolepec.spagicclient.bean.request.fascicolo;

public class FascicoloPRGRequest extends GenericFascicoloRequest{

	@Override
	public TipoFascicoloRequest getTipo() {
		return TipoFascicoloRequest.FASCICOLO_PRG;
	}

}
