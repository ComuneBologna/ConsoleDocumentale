package it.eng.consolepec.spagicclient.bean.request.fascicolo;

public class FascicoloEntrateAccessoAttiRequest extends GenericFascicoloRequest {

	@Override
	public TipoFascicoloRequest getTipo() {
		return TipoFascicoloRequest.FASCICOLO_ENTRATE_ACCESSO_ATTI;
	}

}
