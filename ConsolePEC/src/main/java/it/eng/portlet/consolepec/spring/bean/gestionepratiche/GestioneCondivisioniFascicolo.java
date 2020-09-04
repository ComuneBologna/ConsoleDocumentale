package it.eng.portlet.consolepec.spring.bean.gestionepratiche;

import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CondivisioneFascicolo;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CondivisioneFascicoloResult;

public interface GestioneCondivisioniFascicolo {

	public CondivisioneFascicoloResult gestioneCondivisioneFascicolo(CondivisioneFascicolo condivisioneFascicolo) throws SpagicClientException;

}
