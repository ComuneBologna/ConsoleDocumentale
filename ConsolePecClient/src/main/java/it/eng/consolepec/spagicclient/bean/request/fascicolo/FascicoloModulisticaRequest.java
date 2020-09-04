package it.eng.consolepec.spagicclient.bean.request.fascicolo;

public class FascicoloModulisticaRequest extends GenericFascicoloRequest {
	
	@Override
	public TipoFascicoloRequest getTipo() {
		return TipoFascicoloRequest.FASCICOLO_MODULISTICA;
	}
}
