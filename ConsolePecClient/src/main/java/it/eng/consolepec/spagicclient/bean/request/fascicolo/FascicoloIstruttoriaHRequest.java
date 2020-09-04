package it.eng.consolepec.spagicclient.bean.request.fascicolo;

public class FascicoloIstruttoriaHRequest extends GenericFascicoloRequest{

	@Override
	public TipoFascicoloRequest getTipo() {
		return TipoFascicoloRequest.FASCICOLO_ISTRUTTORIA_H;
	}

}
