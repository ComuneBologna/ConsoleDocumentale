package it.eng.consolepec.spagicclient.bean.request.fascicolo;

public class FascicoloAccessoAttiRequest extends GenericFascicoloRequest {
	
	@Override
	public TipoFascicoloRequest getTipo() {
		return TipoFascicoloRequest.FASCICOLO_ACCESSO_ATTI;
	}
}
