/**
 * 
 */
package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni;

import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.pratica.email.PraticaEmailIn;

/**
 * @author sebastiano
 * 
 */
public interface SganciaPecInApiTask extends ITaskApi {

	public void sganciaPecIn(PraticaEmailIn pecIn) throws PraticaException;
}
