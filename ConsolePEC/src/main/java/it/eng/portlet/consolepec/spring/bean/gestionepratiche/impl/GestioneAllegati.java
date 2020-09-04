package it.eng.portlet.consolepec.spring.bean.gestionepratiche.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import it.bologna.comune.alfresco.versioning.AllVersions;
import it.bologna.comune.alfresco.versioning.Version;
import it.eng.cobo.consolepec.commons.atti.AllegatoAlfresco;
import it.eng.cobo.consolepec.commons.firmadigitale.Firmatario;
import it.eng.cobo.consolepec.util.firmadigitale.FirmaDigitaleUtil;
import it.eng.consolepec.client.DownloadAllegatoClient;
import it.eng.consolepec.spagicclient.SpagicClientDownloadDocument;
import it.eng.consolepec.spagicclient.SpagicClientDownlodAllegatoOriginale;
import it.eng.consolepec.spagicclient.SpagicClientGestioneAllegatoPratica;
import it.eng.consolepec.spagicclient.SpagicClientVersioning;
import it.eng.consolepec.spagicclient.bean.request.download.DownloadRequest;
import it.eng.consolepec.spagicclient.bean.response.download.DownloadResponse;
import it.eng.consolepec.spagicclient.remoteproxy.ResponseWithAttachementsDto;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.TipoFirma;
import it.eng.consolepec.xmlplugin.factory.Pratica;
import it.eng.consolepec.xmlplugin.factory.PraticaObserver.FileDownload;
import it.eng.consolepec.xmlplugin.factory.PraticaObserver.VersionLoad;
import it.eng.consolepec.xmlplugin.factory.Versione;
import it.eng.portlet.consolepec.gwt.server.VerificaFirmaInvoker;
import it.eng.portlet.consolepec.gwt.server.XMLPluginToDTOConverter;
import it.eng.portlet.consolepec.gwt.shared.Base64Utils;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FileDTO;
import it.eng.portlet.consolepec.gwt.shared.model.TmpFileUploadDTO;
import it.eng.portlet.consolepec.spring.bean.gestionepratiche.IGestioneAllegati;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

/**
 * Classe che gestisce il colloquio con spagic per l'aggiunta rimozione di allegati
 *
 * @author pluttero
 *
 */
public class GestioneAllegati implements IGestioneAllegati {

	@Autowired
	UserSessionUtil userSessionUtil;
	@Autowired
	SpagicClientGestioneAllegatoPratica spagicClientGestioneAllegatoPratica;
	@Autowired
	XMLPluginToDTOConverter praticaUtil;
	@Autowired
	PraticaSessionUtil praticaSessionUtil;
	@Autowired
	SpagicClientDownloadDocument spagicClientDownloadDocumentByPath;
	@Autowired
	VerificaFirmaInvoker verificaFirmaUtil;
	@Autowired
	SpagicClientVersioning spagicClientVersioning;
	@Autowired
	DownloadAllegatoClient downloadAllegatoClient;

	@Autowired
	SpagicClientDownlodAllegatoOriginale spagicClientDownlodAllegatoOriginale;

	String tempDir;
	Logger logger = LoggerFactory.getLogger(GestioneAllegati.class);

	/*
	 * (non-Javadoc)
	 *
	 * @see it.eng.portlet.consolepec.gwt.server.IGestioneAllegati#setTempDir(java .lang.String)
	 */
	@Override
	@Value("#{portlet['tempDir']}")
	public void setTempDir(String tempDir) {
		this.tempDir = tempDir;
	}

	@Override
	public String getTempDir() {
		return this.tempDir;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see it.eng.portlet.consolepec.gwt.server.IGestioneAllegati# aggiungiAllegatoFromFileSystem(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Pratica<?> aggiungiAllegatoFromFileSystem(String encPraticaPath, String tempFolderName, String tempFileName) throws IOException {
		String path = Base64Utils.URLdecodeAlfrescoPath(encPraticaPath);
		logger.debug("Aggiunta allegato: {} su pratica: {}", tempFileName, path);
		Pratica<?> result = null;
		File dir = new File(tempDir);
		for (File sourceDir : dir.listFiles()) {
			if (sourceDir.isDirectory() && sourceDir.getName().equals(tempFolderName)) {
				logger.debug("trovata directory: {}", sourceDir.getCanonicalPath());
				for (File source : sourceDir.listFiles()) {
					if (source.isFile() && tempFileName.equals(source.getName())) {
						try {
							logger.debug("trovato file con path: {}", sourceDir.getCanonicalPath());
							LockedPratica uploadFile = spagicClientGestioneAllegatoPratica.uploadFile(path, tempFileName, source, userSessionUtil.getUtenteSpagic());
							result = praticaSessionUtil.loadPraticaInSessione(uploadFile);
							logger.debug("Terminato caricamento");
						} catch (Exception ex) {
							logger.error("Errore.", ex);
						} finally {
							/* pulizia del disco */
							logger.debug("Elimino directory {}", tempFolderName);
							FileUtils.deleteQuietly(sourceDir);
						}

						return result;
					}
				}
			}
		}

		logger.error("Non è stato trovato il file da aggiungere su filesystem: {} {}", tempFolderName, tempFileName);
		return null;
	}

	@Override
	public List<FileDTO> getFilesFromFileTmp(List<TmpFileUploadDTO> tempFiles) throws IOException {

		List<FileDTO> result = new ArrayList<FileDTO>();

		for (TmpFileUploadDTO tmp : tempFiles) {
			String tempFolderName = tmp.getDirName();
			String tempFileName = tmp.getFileName();

			File dir = new File(tempDir);
			for (File sourceDir : dir.listFiles()) {
				if (sourceDir.isDirectory() && sourceDir.getName().equals(tempFolderName)) {
					logger.info("Trovata directory: {}", sourceDir.getCanonicalPath());
					for (File source : sourceDir.listFiles()) {
						if (source.isFile() && tempFileName.equals(source.getName())) {
							try {
								logger.info("Trovato file con path: {}", sourceDir.getCanonicalPath());
								logger.info("Effettuo la verifica della firma digitale");
								FileDTO dto = verificaFirmaUtil.verificaFirmaFile(source);

								if (dto != null)
									result.add(dto);

							} catch (Exception ex) {
								logger.error(ex.getLocalizedMessage(), ex);
								FileUtils.deleteQuietly(sourceDir);
							}
						}
					}
				}
			}
		}

		return result;
	}

	@Override
	public boolean eliminaFileTmp(String pathFile) throws IOException {
		Path path = Paths.get(pathFile);

		if (Files.exists(path) && !Files.isDirectory(path)) {
			File dir = new File(tempDir);

			for (File sourceDir : dir.listFiles()) {

				if (sourceDir.isDirectory() && sourceDir.getName().equals(path.getParent().getFileName().toString())) {
					FileUtils.deleteDirectory(path.getParent().toFile());
					return true;
				}

				if (sourceDir.isFile() && sourceDir.getName().equals(path.getFileName().toString())) {
					return Files.deleteIfExists(path);
				}
			}
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see it.eng.portlet.consolepec.gwt.server.IGestioneAllegati# aggiungiAllegatoFromPratica(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Pratica<?> aggiungiAllegatoFromPratica(String encPraticaDestinazionePath, String encPraticaSorgentePath, String fileName) {
		String pathSrc = Base64Utils.URLdecodeAlfrescoPath(encPraticaSorgentePath);
		String pathDst = Base64Utils.URLdecodeAlfrescoPath(encPraticaDestinazionePath);
		Pratica<?> result = null;
		logger.debug("Aggiunta allegato: {} da pratica: {} verso pratica: {}", fileName, pathSrc, pathDst);
		try {
			LockedPratica uploadFile = spagicClientGestioneAllegatoPratica.uploadFile(pathDst, pathSrc, fileName, userSessionUtil.getUtenteSpagic());
			result = praticaSessionUtil.loadPraticaInSessione(uploadFile);
			logger.debug("Terminato caricamento");
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see it.eng.portlet.consolepec.gwt.server.IGestioneAllegati#rimuoviAllegato (java.lang.String, it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO)
	 */
	@Override
	public Pratica<?> rimuoviAllegato(String encodedPath, AllegatoDTO allegato) {
		String praticaPath = Base64Utils.URLdecodeAlfrescoPath(encodedPath);
		logger.debug("Cancellazione allegato: {} su pratica: {}", allegato.getNome(), praticaPath);
		LockedPratica removeFile = spagicClientGestioneAllegatoPratica.removeFile(praticaPath, allegato.getNome(), userSessionUtil.getUtenteSpagic());
		Pratica<?> pratica = praticaSessionUtil.loadPraticaInSessione(removeFile);
		return pratica;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see it.eng.portlet.consolepec.gwt.server.IGestioneAllegati# downloadAllegatoToFileSystem(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String downloadAllegatoToFileSystem(String encPraticaPath, String allegatoName, String versione) throws IOException {
		String praticaPath = Base64Utils.URLdecodeAlfrescoPath(encPraticaPath);
		logger.debug("Download allegato nomeFile: {} id: {} versione: {}", allegatoName, praticaPath, versione);
		String result = null;
		Pratica<?> pratica = null;
		pratica = praticaSessionUtil.loadPraticaFromEncodedPath(encPraticaPath);

		final File pathDir = new File(this.tempDir);

		if (!pathDir.isDirectory() && !pathDir.mkdir()) {
			throw new IOException("directory temporanea inesistente o impossibile crearla : " + pathDir);

		} else if (pratica == null) {
			throw new IOException("Pratica con path: " + praticaPath + " non trovata");
		} else {
			Allegato allegato = null;
			// ricerco allegato su pratica
			for (Allegato allg : pratica.getDati().getAllegati()) {
				if (allg.getNome().equals(allegatoName)) {
					allegato = allg;
					logger.debug("Trovato allegato {}", allegato);
					break;
				}
			}
			if (versione != null) {
				logger.debug("Download della versione: {}", versione);
				logger.debug("Carico le versioni sulla pratica");
				pratica.loadVersioni(allegato, new VersionLoad() {

					@Override
					public List<Versione> onLoadVersioni(String path, String nomeFile) {
						List<Versione> versioni = new ArrayList<Versione>();
						AllVersions versions = spagicClientVersioning.getAllVersions(path, nomeFile, userSessionUtil.getUtenteSpagic());
						for (Version version : versions.getVersions()) {
							versioni.add(new Versione(version.getVersion(), version.getPecUser(), version.getNodeUuid(), version.getHash(), version.getCreationDate().toGregorianCalendar().getTime()));
						}
						return versioni;
					}
				});
				for (Versione v : allegato.getVersioni()) {
					if (v.getLabel().equals(versione)) {
						logger.debug("Trovata la versione: {} con alfrescoUUID: {}", v.getLabel(), v.getVersionid());
						DownloadRequest request = new DownloadRequest();
						request.setUuid(v.getVersionid());
						ResponseWithAttachementsDto<DownloadResponse> path = spagicClientDownloadDocumentByPath.getDocument(request);
						/**
						 *
						 * TODO PL Qui dobbiamo capire se sia firmato o no
						 *
						 */
						result = preparaStream(allegato, path, false);
					}
				}
			} else {
				logger.debug("Download ultima versione");
				final Allegato allegatoFin = allegato;
				final String[] resContainer = new String[1];
				pratica.downloadAllegato(allegato, new FileDownload() {

					@Override
					public void onDownloadRequest(String alfrescoPath) {
						logger.debug("Download path da alfresco {}", alfrescoPath);
						logger.debug("Invio richiesta a spagic");
						DownloadRequest request = new DownloadRequest();
						request.setPath(alfrescoPath);
						ResponseWithAttachementsDto<DownloadResponse> document = spagicClientDownloadDocumentByPath.getDocument(request);
						try {
							resContainer[0] = preparaStream(allegatoFin, document, allegatoFin.getFirmato());
						} catch (IOException e) {
							logger.error(e.getLocalizedMessage(), e);
						}
					}
				});
				result = resContainer[0];
			}
		}
		return result;

	}

	/**
	 * Serializza la risposta spagic sul filesystem. Ritorna la directory temporanea in cui il file è salvato
	 *
	 * @param allegato
	 * @param path
	 * @throws IOException
	 */
	private String preparaStream(Allegato allegato, ResponseWithAttachementsDto<DownloadResponse> path, boolean firmato) throws IOException {
		Set<String> keySet = path.getAttachements().keySet();
		File pathDir = new File(this.tempDir);
		// fase due scrittura documento su di uno stream
		String dir = UUID.randomUUID().toString();
		File tempDir = new File(pathDir, dir);
		tempDir.mkdir();
		String fname = allegato.getFolderOriginName() != null && !allegato.getFolderOriginName().isEmpty() ? allegato.getFolderOriginName() : allegato.getNome();
		if (firmato && allegato.getTipoFirma().equals(TipoFirma.CADES) && !fname.toLowerCase().endsWith(".p7m"))
			fname = fname + ".p7m";
		File tempFile = new File(tempDir, URLEncoder.encode(fname, "UTF-8"));
		logger.debug("Path temporaneo: {}", tempFile.getAbsolutePath());
		ArrayList<String> idAttachements = new ArrayList<String>(keySet);
		String idAttachement = idAttachements.get(0);

		InputStream inputStream = path.getAttachements().get(idAttachement);
		FileOutputStream fos = new FileOutputStream(tempFile);
		Streams.copy(inputStream, fos, true);

		return dir + File.separator + URLEncoder.encode(fname, "UTF-8");
	}

	@Override
	public String downloadAllegatoOriginaleToFileSystemFromUUID(String encPraticaPath, String uuid, String fname) throws IOException {
		String praticaPath = Base64Utils.URLdecodeAlfrescoPath(encPraticaPath);

		String result = null;
		Pratica<?> pratica = null;
		pratica = praticaSessionUtil.loadPraticaFromEncodedPath(encPraticaPath);

		final File pathDir = new File(this.tempDir);

		if (!pathDir.isDirectory() && !pathDir.mkdir()) {
			throw new IOException("directory temporanea inesistente o impossibile crearla : " + pathDir);

		} else if (pratica == null) {
			throw new IOException("Pratica con path: " + praticaPath + " non trovata");

		} else {
			String randomPath = UUID.randomUUID().toString();
			InputStream inputStream = spagicClientDownlodAllegatoOriginale.getDocumentByUuid(uuid);
			File tempDir = new File(pathDir, randomPath);
			tempDir.mkdir();

			File tempFile = new File(tempDir, fname);
			FileOutputStream outputStream = new FileOutputStream(tempFile);
			write(inputStream, outputStream);
			result = randomPath + File.separator + fname;
		}

		return result;
	}

	@Override
	public String downloadAllegatoOriginaleToFileSystemFromUUID(String uuid, String fname) throws IOException {
		String result = null;
		final File pathDir = new File(this.tempDir);

		String randomPath = UUID.randomUUID().toString();
		InputStream inputStream = spagicClientDownlodAllegatoOriginale.getDocumentByUuid(uuid);
		File tempDir = new File(pathDir, randomPath);
		tempDir.mkdir();

		File tempFile = new File(tempDir, fname);
		FileOutputStream outputStream = new FileOutputStream(tempFile);
		write(inputStream, outputStream);
		result = randomPath + File.separator + fname;

		return result;
	}

	@Override
	public String downloadVersioneAllegato(String clientID, String nomeFile, String versione) throws IOException {
		String result = null;

		String praticaPath = Base64Utils.URLdecodeAlfrescoPath(clientID);
		final File pathDir = new File(this.tempDir);
		String uuid = UUID.randomUUID().toString();
		AllegatoAlfresco aa = null;
		if (versione.startsWith(FLOAT_PREFIX)) {
			Float f = Float.valueOf(versione.replace(FLOAT_PREFIX, ""));
			aa = downloadAllegatoClient.downloadAllegato(userSessionUtil.getUtenteSpagic(), praticaPath, nomeFile, f);
		} else {
			aa = downloadAllegatoClient.downloadAllegato(userSessionUtil.getUtenteSpagic(), praticaPath, nomeFile, versione);
		}
		InputStream inputStream = aa.getInputStream();
		if (aa.getFirmaDigitale() != null && FirmaDigitaleUtil.isFirmato(aa.getFirmaDigitale()) && Firmatario.TipoFirma.CADES.equals(FirmaDigitaleUtil.getTipoFirma(aa.getFirmaDigitale()))
				&& !nomeFile.toLowerCase().endsWith(".p7m")) {
			nomeFile = nomeFile + ".p7m";
		}
		File tempDir = new File(pathDir, uuid);
		tempDir.mkdir();
		File tempFile = new File(tempDir, nomeFile);
		FileOutputStream outputStream = new FileOutputStream(tempFile);
		write(inputStream, outputStream);
		result = uuid + File.separator + nomeFile;
		return result;
	}

	@Override
	public String downloadAllegatoToFileSystemFromUUID(String uuid) throws IOException {
		String fname = null;
		DownloadRequest request = new DownloadRequest();
		String result = null;
		final File pathDir = new File(this.tempDir);

		request.setUuid(uuid);
		ResponseWithAttachementsDto<DownloadResponse> response = spagicClientDownloadDocumentByPath.getDocument(request);

		String randomPath = UUID.randomUUID().toString();
		File tempDir = new File(pathDir, randomPath);
		tempDir.mkdirs();
		if (!tempDir.exists() && !tempDir.isDirectory()) {
			throw new IOException("directory temporanea inesistente o impossibile crearla : " + tempDir);
		}

		InputStream inputStream = null;

		try {

			for (Entry<String, InputStream> entry : response.getAttachements().entrySet()) {
				inputStream = entry.getValue();
				fname = entry.getKey();
			}

			File tempFile = new File(tempDir, fname);
			FileOutputStream outputStream = new FileOutputStream(tempFile);
			write(inputStream, outputStream);
			result = randomPath + File.separator + fname;
		} catch (IOException e) {
			inputStream.close();
			throw new IOException("Il file con uuid " + uuid + " non esiste in Alfresco");
		}

		return result;
	}

	private static void write(InputStream inputStream, OutputStream outputStream) throws IOException {
		Streams.copy(inputStream, outputStream, false);
	}
}
