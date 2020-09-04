package it.eng.consolepec.spagicclient.bean.request.fascicolo;


public class FascicoloElettoraleComunicazioniRequest extends GenericFascicoloRequest {

	@Override
	public TipoFascicoloRequest getTipo() {
		return TipoFascicoloRequest.FASCICOLO_ELETTORALE_COMUNICAZIONI;
	}

}
