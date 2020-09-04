package it.eng.consolepec.spagicclient;

import it.bologna.comune.spagic.combo.protocollazione.Combos;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;

public interface SpagicClientGetComboBoxesProtocollazione {
	
	/**
	 * 
	 * @return
	 * @throws SpagicClientException
	 */
	public Combos getComboBoxes(Utente utente) throws SpagicClientException;

}
