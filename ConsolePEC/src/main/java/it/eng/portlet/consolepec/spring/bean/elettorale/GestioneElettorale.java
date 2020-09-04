package it.eng.portlet.consolepec.spring.bean.elettorale;

import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;

public interface GestioneElettorale {
	
	public void importa(String pathPratica) throws SpagicClientException;
	
	public void annulla(String pathPratica) throws SpagicClientException;

}
