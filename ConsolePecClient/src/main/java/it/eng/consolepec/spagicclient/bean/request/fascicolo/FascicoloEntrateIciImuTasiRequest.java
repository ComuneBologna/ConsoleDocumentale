package it.eng.consolepec.spagicclient.bean.request.fascicolo;


public class FascicoloEntrateIciImuTasiRequest extends GenericFascicoloRequest {

	@Override
	public TipoFascicoloRequest getTipo() {
		return TipoFascicoloRequest.FASCICOLO_ENTRATE_ICI_IMU_TASI;
	}

}
