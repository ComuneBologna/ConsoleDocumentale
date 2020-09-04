package it.eng.consolepec.xmlplugin.factory;

import java.util.List;

public interface PraticaObserver {

	public interface VersionLoad {

		public List<Versione> onLoadVersioni(String path, String nomeFile);
	
	}
	
	public interface VersionDownload {
		/**
		 * La pratica ha ricevuto una richiesta di download allegato di una specifica Versione.
		 * 
		 * @param alfrescoPath
		 */
		public void onDownloadRequest(String versionid);
	}

	public interface FileDownload {
		/**
		 * La pratica ha ricevuto una richiesta di download allegato.
		 * 
		 * @param alfrescoPath
		 */
		public void onDownloadRequest(String alfrescoPath);
	}

	public interface VerificaFirma {
		/**
		 * La pratica ha ricevuto una richiesta di verifica firma su allegato
		 * 
		 * @param alfrescoPath
		 */
		public void onVerificaFirma(String alfrescoPath);
	}
}
