package it.eng.portlet.consolepec.spring.bean.gestionepratiche;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;

import it.eng.consolepec.xmlplugin.factory.Pratica;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FileDTO;
import it.eng.portlet.consolepec.gwt.shared.model.TmpFileUploadDTO;

public interface IGestioneAllegati {

	String FLOAT_PREFIX = "float";

	@Value("#{portlet['tempDir']}")
	public abstract void setTempDir(String tempDir);

	public abstract String getTempDir();

	/**
	 * Inserisce un file presente sul filesystem, all'interno di una pratica (nuovo allegato). Il metodo si avvale di
	 *
	 * @param pratica
	 * @param tempFolderName
	 * @param tempFileName
	 * @throws IOException
	 */
	public abstract Pratica<?> aggiungiAllegatoFromFileSystem(String encPraticaPath, String tempFolderName, String tempFileName) throws IOException;

	/**
	 * upload con sorgente prelevata da altra pratica
	 *
	 * @param encPraticaDestinazionePath
	 * @param encPraticaSorgentePath
	 * @param fileName
	 * @return
	 */
	public abstract Pratica<?> aggiungiAllegatoFromPratica(String encPraticaDestinazionePath, String encPraticaSorgentePath, String fileName);

	/**
	 * Rimuove un allegato da una pratica, mediante servizio spagic
	 *
	 * @param pratica
	 * @param allegato
	 * @return
	 */
	public abstract Pratica<?> rimuoviAllegato(String encodedPath, AllegatoDTO allegato);

	/**
	 * Scarica da spagic, su fs locale, un allegato. Il metoodo ritorna il path relativo alla directory temporanea.
	 *
	 * @param encPraticaPath
	 * @param allegatoName
	 * @param versione
	 * @return
	 * @throws IOException
	 */
	public abstract String downloadAllegatoToFileSystem(String encPraticaPath, String allegatoName, String versione) throws IOException;

	/**
	 *
	 * @param uuid
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public abstract String downloadAllegatoToFileSystemFromUUID(String uuid) throws IOException;

	/**
	 *
	 * @param clientID
	 * @param uuid
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public abstract String downloadAllegatoOriginaleToFileSystemFromUUID(String clientID, String uuid, String fileName) throws IOException;

	/**
	 * 
	 * @param uuid
	 * @return
	 * @throws IOException
	 */
	public abstract String downloadAllegatoOriginaleToFileSystemFromUUID(String uuid, String fname) throws IOException;

	/**
	 * 
	 * @param pathFile
	 * @param versione
	 * @return
	 * @throws IOException
	 */
	public abstract String downloadVersioneAllegato(String clientID, String nomeFile, String versione) throws IOException;

	/**
	 * Recupera una lista di file temporanei
	 *
	 * @param tempFiles
	 * @return
	 * @throws IOException
	 */
	public abstract List<FileDTO> getFilesFromFileTmp(List<TmpFileUploadDTO> tempFiles) throws IOException;

	public abstract boolean eliminaFileTmp(String pathFile) throws IOException;

}
