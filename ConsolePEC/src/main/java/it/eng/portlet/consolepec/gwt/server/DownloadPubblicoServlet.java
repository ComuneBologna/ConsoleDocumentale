package it.eng.portlet.consolepec.gwt.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.util.Streams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.eng.cobo.consolepec.util.date.DateUtils;
import it.eng.cobo.consolepec.util.io.ConsoleIOUtils;
import it.eng.consolepec.spagicclient.SpagicClientDownloadDocument;
import it.eng.consolepec.spagicclient.SpagicClientHandleLockedMetadata;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.bean.request.download.DownloadRequest;
import it.eng.consolepec.spagicclient.bean.response.download.DownloadResponse;
import it.eng.consolepec.spagicclient.remoteproxy.ResponseWithAttachementsDto;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.factory.Pratica;
import it.eng.consolepec.xmlplugin.factory.PraticaFactory;
import it.eng.consolepec.xmlplugin.factory.XMLPraticaFactory;
import it.eng.portlet.consolepec.gwt.shared.Base64Utils;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.UriMapping;
import it.eng.portlet.consolepec.spring.bean.gestionepratiche.IGestioneAllegati;

public class DownloadPubblicoServlet extends HttpServlet {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private final Logger logger = LoggerFactory.getLogger(DownloadPubblicoServlet.class);

	@Autowired
	IGestioneAllegati gestAllg;
	@Autowired
	SpagicClientDownloadDocument spagicClientDownloadDocument;
	@Autowired
	SpagicClientHandleLockedMetadata spagicClientHandleLockedMetadata;

	String tempDir;

	@Value("#{portlet['tempDir']}")
	public void setTempDir(String tempDir) {
		this.tempDir = tempDir;
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init();
		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String clientID = null;
		String fileName = null;
		String pathInfo = request.getPathInfo();
		logger.debug("Request path: {}", pathInfo);
		if (pathInfo == null) {
			logger.error("Request path null");
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		try {
			HashMap<String, String> params = UriMapping.decodeDownloadPubblicoPathInfo(pathInfo);
			clientID = params.get(UriMapping.KEY_FASCICOLO_PATH);
			fileName = params.get(UriMapping.KEY_NOME_ALLEGATO);

		} catch (Exception e) {
			logger.error("Eccezione nella decodifica dell'URL", e);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		logger.debug("clientID: {} fileName: {}", clientID, fileName);
		String tempPath = null;
		if (clientID != null && fileName != null) {
			try {
				boolean isPubblicato = false;
				String alfrescoPath = Base64Utils.URLdecodeAlfrescoPath(clientID);

				LockedPratica loadMetadatiXml = spagicClientHandleLockedMetadata.loadMetadatiXml(alfrescoPath, new Utente("Download", "File", "Pubblico", "", "", Arrays.asList(""), new Date()));
				PraticaFactory praticaFactory = new XMLPraticaFactory();
				Pratica<?> pratica = praticaFactory.loadPratica(new StringReader(loadMetadatiXml.getMetadatiXml()));

				String alfrescoPathAllegato = null;
				String uuid = null;

				TreeSet<Allegato> allegati = pratica.getDati().getAllegati();
				for (Allegato allegato : allegati) {
					if (allegato.getNome().equals(fileName)) {
						if (allegato.getDataInizioPubblicazione() != null && allegato.getDataFinePubblicazione() != null) {
							logger.debug("trovato allegato corrispondente: {}", fileName);
							Date inizio = DateUtils.getMidnightDate(allegato.getDataInizioPubblicazione());
							Date fine = DateUtils.getMidnightDate(allegato.getDataFinePubblicazione());
							Date oggi = DateUtils.getMidnightToday();
							if ((inizio != null && fine != null) && (inizio.equals(oggi) || inizio.before(oggi)) && (fine.equals(oggi) || fine.after(oggi))) {
								logger.debug("l'allegato {} è nel periodo di pubblicazione: {} - {}", fileName, inizio, fine);
								isPubblicato = true;
								StringBuilder sb = new StringBuilder();
								sb.append(pratica.getDati().getFolderPath()).append("/").append(pratica.getSubFolderPath()).append("/").append(fileName);
								alfrescoPathAllegato = sb.toString();

								if(allegato.getFirmato().booleanValue() && !".p7m".equalsIgnoreCase(fileName.substring(fileName.length() -4))) {
									fileName += ".p7m";
								}
								break;
							}

							logger.debug("l'allegato {} NON è nel periodo di pubblicazione: {} - {}", fileName, inizio, fine);

						} else {
							isPubblicato = false;
						}
					}
				}

				if (isPubblicato) {

					DownloadRequest downloadRequestAllegato = new DownloadRequest();

					if (uuid != null) {
						downloadRequestAllegato.setUuid(uuid);

					} else {
						downloadRequestAllegato.setPath(alfrescoPathAllegato);
					}

					ResponseWithAttachementsDto<DownloadResponse> documentAllegato = spagicClientDownloadDocument.getDocument(downloadRequestAllegato);
					tempPath = getFile(documentAllegato, fileName);

					StringBuilder url = new StringBuilder("/download/");
					url.append("forcedownload/y/");
					url.append("filepath/").append(tempPath);
					request.getRequestDispatcher(url.toString()).forward(request, response);
				} else {
					logger.error("l'allegato {} non è pubblicato", fileName);
					response.sendError(ConsolePecConstants.HTTP_ERROR_PUBBLICAZIONE_TERMINATA);
				}
			} catch (Exception e) {
				logger.error(e.getLocalizedMessage(), e);
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			} finally {
				String path = gestAllg.getTempDir() + File.separator + tempPath;
				File f = new File(path);
				delete(f);
			}
		}

	}

	private String getFile(ResponseWithAttachementsDto<DownloadResponse> attachment, String fileName) throws IOException {
		Set<String> keySet = attachment.getAttachements().keySet();
		File pathDir = new File(this.tempDir);
		// fase due scrittura documento su di uno stream
		String dir = UUID.randomUUID().toString();
		File tempDir = new File(pathDir, dir);
		tempDir.mkdir();
		File tempFile = new File(tempDir, fileName);
		logger.debug("Path temporaneo: {}", tempFile.getAbsolutePath());
		ArrayList<String> idAttachements = new ArrayList<String>(keySet);
		String idAttachement = idAttachements.get(0);

		InputStream inputStream = attachment.getAttachements().get(idAttachement);
		FileOutputStream fos = new FileOutputStream(tempFile);
		Streams.copy(inputStream, fos, true);

		return dir + File.separator + fileName;
	}

	public void delete(File f) {
		if (Paths.get(gestAllg.getTempDir()).equals(f.toPath().getParent())) {

			if (f.exists()) {
				if (f.isDirectory()) {
					ConsoleIOUtils.deleteFolder(f);

				} else {
					ConsoleIOUtils.deleteFiles(f);
				}
			}

		} else {
			ConsoleIOUtils.deleteFolder(new File(f.getParent()));
		}
	}

}
