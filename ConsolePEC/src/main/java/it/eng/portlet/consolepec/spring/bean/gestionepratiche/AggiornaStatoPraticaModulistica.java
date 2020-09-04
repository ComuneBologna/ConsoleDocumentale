package it.eng.portlet.consolepec.spring.bean.gestionepratiche;

import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.portlet.consolepec.gwt.shared.action.modulistica.CambiaStatoPraticaModulistica;
import it.eng.portlet.consolepec.gwt.shared.action.modulistica.CambiaStatoPraticaModulisticaResult;

public interface AggiornaStatoPraticaModulistica {

	public CambiaStatoPraticaModulisticaResult cambiaStato(CambiaStatoPraticaModulistica cambiaStatoPraticaModulistica) throws SpagicClientException;

}
