package it.eng.portlet.consolepec.spring.bean.gestionepratiche;

import java.util.Map;

import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;

/**
 *
 * @author biagiot
 *
 */
public interface GestioneMetadatiPratica {

	public Map<String, String> getEtichetteMetadatiMap(String tipoPratica) throws SpagicClientException;
}
