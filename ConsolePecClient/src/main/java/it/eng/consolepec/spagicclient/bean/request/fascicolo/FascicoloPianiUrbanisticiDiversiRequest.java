package it.eng.consolepec.spagicclient.bean.request.fascicolo;

public class FascicoloPianiUrbanisticiDiversiRequest extends GenericFascicoloRequest{

	@Override
	public TipoFascicoloRequest getTipo() {
		return TipoFascicoloRequest.FASCICOLO_PIANI_URBANISTICI_DIVERSI;
	}

}
