package it.eng.portlet.consolepec.gwt.shared;

import java.util.HashMap;

import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.user.client.Window;

import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;

/**
 * Classe per la generazione di URL che non siano chiamate RPC ad actionhandler GWTP
 *
 * @author pluttero
 *
 */
public class UriMapping {

	public static final String KEY_FASCICOLO_PATH = "fascicoloPath";
	public static final String KEY_NOME_ALLEGATO = "nomeAllegato";
	private static final char PARAMS_SEPARATOR = ';';

	/**
	 * Genera l'URL per il download di un allegato
	 *
	 * @param pratica
	 * @param allegato
	 * @return
	 */
	public static SafeUri generaDownloadAllegatoServletURL(String clientID, AllegatoDTO allegato) {
		StringBuilder url = new StringBuilder();
		// il nome del file viene trasformato in esadecimale
		// TODO trovare un modo più elegante per l'encode client side
		String nomeFile = Base64Utils.URLencodeAlfrescoPath(allegato.getNome());
		String optionalFileName = allegato.getFolderOriginName() != null ? Base64Utils.URLencodeAlfrescoPath(allegato.getFolderOriginName()) : "null";
		url.append(Window.Location.getProtocol()).append("//").append(Window.Location.getHost()) //
				.append("/").append(LiferayPortletUnsecureActionImpl.getPortletContext()) //
				.append("/downloadallegato/clientID/").append(clientID).append("/fileName/").append(nomeFile) //
				.append("/optionalfilename/").append(optionalFileName) //
				.append("/bustafirma/").append("false");
		SafeUri uri = UriUtils.fromString(url.toString());
		return uri;
	}

	public static SafeUri generaDownalodFile(String fileName) {
		StringBuilder url = new StringBuilder();
		url.append(Window.Location.getProtocol()).append("//").append(Window.Location.getHost()) //
				.append("/").append(LiferayPortletUnsecureActionImpl.getPortletContext()) //
				.append("/download/forcedownload/y/filepath/").append(fileName);
		return UriUtils.fromString(url.toString());
	}

	public static SafeUri generaDownloadAllegatoServletURL(String uidAlfresco) {
		StringBuilder url = new StringBuilder();
		uidAlfresco = Base64Utils.URLencodeAlfrescoPath(uidAlfresco);
		url.append(Window.Location.getProtocol()).append("//").append(Window.Location.getHost()) //
				.append("/").append(LiferayPortletUnsecureActionImpl.getPortletContext()) //
				.append("/downloadallegato/uuid/").append(uidAlfresco).append("/bustafirma/").append("false");
		SafeUri uri = UriUtils.fromString(url.toString());
		return uri;
	}

	public static SafeUri generaDownloadAllegatoSbustatoServletURL(String clientID, String uuid, String nomeFile) {
		StringBuilder url = new StringBuilder();
		// il nome del file viene trasformato in esadecimale
		// TODO trovare un modo più elegante per l'encode client side
		nomeFile = Base64Utils.URLencodeAlfrescoPath(nomeFile);
		uuid = Base64Utils.URLencodeAlfrescoPath(uuid);

		url.append(Window.Location.getProtocol()).append("//").append(Window.Location.getHost()) //
				.append("/").append(LiferayPortletUnsecureActionImpl.getPortletContext()) //
				.append("/downloadallegato/clientID/").append(clientID).append("/fileName/").append(nomeFile).append("/uuid/").append(uuid).append("/bustafirma/").append("true");
		SafeUri uri = UriUtils.fromString(url.toString());
		return uri;
	}

	public static SafeUri generaDownloadAllegatoVersionato(String pathFile, String nomeFile, String versione) {
		StringBuilder url = new StringBuilder();
		nomeFile = Base64Utils.URLencodeAlfrescoPath(nomeFile);
		versione = Base64Utils.URLencodeAlfrescoPath(versione);
		url.append(Window.Location.getProtocol()).append("//").append(Window.Location.getHost()) //
				.append("/").append(LiferayPortletUnsecureActionImpl.getPortletContext()) //
				.append("/downloadallegato/clientID/").append(pathFile).append("/fileName/").append(nomeFile).append("/versione/").append(versione);

		SafeUri uri = UriUtils.fromString(url.toString());
		return uri;
	}

	public static SafeUri generaDownloadAllegatoSbustatoServletURL(String nomeFile, String uuid) {
		StringBuilder url = new StringBuilder();
		// il nome del file viene trasformato in esadecimale
		// TODO trovare un modo più elegante per l'encode client side
		nomeFile = Base64Utils.URLencodeAlfrescoPath(nomeFile);
		uuid = Base64Utils.URLencodeAlfrescoPath(uuid);

		url.append(Window.Location.getProtocol()).append("//").append(Window.Location.getHost()) //
				.append("/").append(LiferayPortletUnsecureActionImpl.getPortletContext()) //
				.append("/downloadallegato/uuid/").append(uuid).append("/fileName/").append(nomeFile).append("/bustafirma/").append("true");
		SafeUri uri = UriUtils.fromString(url.toString());
		return uri;
	}

	/**
	 * Genera l'URL per il download di uno zip contenente più allegati
	 *
	 * @param zipName
	 * @param zipDir
	 * @return
	 */
	public static SafeUri generaDownloadAllegatiZippati(String zipName, String zipDir) {
		// è la url per chiamare DownloadAllegatoServlet (che a sua volta chiama FileServlet)
		StringBuilder url = new StringBuilder();
		url.append(Window.Location.getProtocol()).append("//").append(Window.Location.getHost()) //
				.append("/").append(LiferayPortletUnsecureActionImpl.getPortletContext()) //
				.append("/downloadallegato/zipdir/").append(zipDir).append("/zipname/").append(zipName);
		SafeUri uri = UriUtils.fromString(url.toString());
		return uri;
	}

	public static String generaUploadAllegatoServletContextPath() {
		StringBuilder path = new StringBuilder();
		path.append("/").append(LiferayPortletUnsecureActionImpl.getPortletContext()).append("/upload");
		return path.toString();
	}

	public static SafeUri generaDownloadEstrazioniAmianto(String fileName, String fileDir) {
		// è la url per chiamare DownloadAllegatoServlet (che a sua volta chiama FileServlet)
		StringBuilder url = new StringBuilder();
		url.append(Window.Location.getProtocol()).append("//").append(Window.Location.getHost()) //
				.append("/").append(LiferayPortletUnsecureActionImpl.getPortletContext()) //
				.append("/downloadallegato/estrazioneamiantodir/").append(fileDir).append("/estrazioneamiantoname/").append(fileName);
		SafeUri uri = UriUtils.fromString(url.toString());
		return uri;
	}

	/**
	 * Ritorna http(s)://host:port
	 *
	 * @return
	 */
	public static String generaServerBaseURL() {
		StringBuilder url = new StringBuilder();
		url.append(Window.Location.getProtocol()).append("//").append(Window.Location.getHost());
		return url.toString();
	}

	/**
	 * Ritorna il context path della servlet per il download pubblico
	 *
	 * @return
	 */
	public static String generaDownloadPubblicoServletContextPath() {
		StringBuilder url = new StringBuilder();
		url.append("/").append(LiferayPortletUnsecureActionImpl.getPortletContext()).append("/downloadpubblico");
		return url.toString();
	}

	/**
	 * Ritorna un pathInfo (compresa la "/" iniziale") per costruire l'URL del download pubblico a partire dal path di Alfresco (in base64) di una pratica e dal nome del suo allegato da scaricare; la
	 * stringa risultante va poi decodificata con {@link #decodeDownloadPubblicoPathInfo}.
	 *
	 * @param fascicoloPath
	 * @param nomeAllegato
	 * @return
	 * @throws Exception
	 */
	public static String encodeDownloadPubblicoPathInfo(String fascicoloPath, String nomeAllegato) throws Exception {
		if (fascicoloPath == null || nomeAllegato == null)
			throw new Exception("errore creazione URL");
		StringBuffer sb = new StringBuffer().append(fascicoloPath).append(PARAMS_SEPARATOR).append(nomeAllegato);
		return "/" + Base64Utils.URLencodeAlfrescoPath(sb.toString());
	}

	/**
	 * Scompone un pathInfo (compresa la "/" iniziale") creato con {@link #encodeDownloadPubblicoPathInfo} in una mappa contenente il path di Alfresco (in base64) di una pratica e il nome del suo
	 * allegato ida scaricare (accessibili rispettivamente con {@link #KEY_FASCICOLO_PATH} e {@link #KEY_NOME_ALLEGATO}).
	 *
	 * @param encodedPathInfo
	 * @return
	 * @throws Exception
	 */
	public static HashMap<String, String> decodeDownloadPubblicoPathInfo(String encodedPathInfo) throws Exception {
		String decodedURL = Base64Utils.URLdecodeAlfrescoPath(encodedPathInfo.substring(1)); // rimuovo il primo carattere che è sempre una "/"
		String path = decodedURL.substring(0, (decodedURL.lastIndexOf(PARAMS_SEPARATOR)));
		String name = decodedURL.substring(decodedURL.lastIndexOf(PARAMS_SEPARATOR) + 1);
		if (path == null || path == "" || name == null || name == "")
			throw new Exception("errore formato URL");
		HashMap<String, String> hm = new HashMap<String, String>();
		hm.put(KEY_FASCICOLO_PATH, path);
		hm.put(KEY_NOME_ALLEGATO, name);
		return hm;
	}

}
