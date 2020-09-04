package it.eng.consolepec.spagicclient.bean.response.inviomassivo;

import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;

import java.util.List;

public class ElencoDettagliComunicazioni {
	private List<LockedPratica> comunicazioni;
	private int totali;
	public ElencoDettagliComunicazioni(List<LockedPratica> comunicazioni, int totali) {
		super();
		this.comunicazioni = comunicazioni;
		this.totali = totali;
	}
	public List<LockedPratica> getComunicazioni() {
		return comunicazioni;
	}
	public int getTotali() {
		return totali;
	}
	
	
}
