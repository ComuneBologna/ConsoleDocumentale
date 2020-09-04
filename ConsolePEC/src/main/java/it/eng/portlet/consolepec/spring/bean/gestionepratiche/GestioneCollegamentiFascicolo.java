package it.eng.portlet.consolepec.spring.bean.gestionepratiche;

import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CollegamentoFascicoli;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CollegamentoFascicoliResult;

public interface GestioneCollegamentiFascicolo {

	public CollegamentoFascicoliResult gestioneCollegamentoFascicolo(CollegamentoFascicoli collegamentoFascicoli) throws SpagicClientException;

}
