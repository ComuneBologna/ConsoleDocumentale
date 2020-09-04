package it.eng.consolepec.xmlplugin.pratica.email;

/**
 * Interfaccia da implementare per gestire gli eventi derivanti da azioni su pratica email
 * 
 * @author pluttero
 * 
 */
public interface PraticaEmailObserver {

	// public interface FileDownload {
	// /**
	// * La pratica ha ricevuto una richiesta di download allegato.
	// *
	// * @param alfrescoPath
	// */
	// public void onDownloadRequest(String alfrescoPath);
	// }

	/**
	 * La pratica è stata modificata. L'implementazione può decidere di serializzarla e renderla persistente.
	 */
	public void onPraticaModified();

}
