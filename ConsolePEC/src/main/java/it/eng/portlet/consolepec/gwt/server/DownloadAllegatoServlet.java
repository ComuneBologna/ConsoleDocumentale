package it.eng.portlet.consolepec.gwt.server;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.StringTokenizer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.eng.cobo.consolepec.util.io.ConsoleIOUtils;
import it.eng.portlet.consolepec.gwt.shared.Base64Utils;
import it.eng.portlet.consolepec.spring.bean.gestionepratiche.IGestioneAllegati;

public class DownloadAllegatoServlet extends HttpServlet {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private final Logger logger = LoggerFactory.getLogger(DownloadAllegatoServlet.class);

	@Autowired
	IGestioneAllegati gestAllg;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init();
		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String clientID = null;
		String fileName = null;
		String optionalFileName = null;
		String pathInfo = request.getPathInfo();
		String uuid = null;
		Boolean bustaFirma = false;
		boolean forceDownload = false;
		String zipDir = null;
		String zipName = null;
		String estrazioniAmiantoDir = null;
		String estrazioniAmiantoName = null;
		String versione = null;
		logger.debug("Request path: {}", pathInfo);
		if (pathInfo == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		StringTokenizer st = new StringTokenizer(pathInfo, "/");

		while (st.hasMoreTokens()) {
			String tk = st.nextToken();
			if (tk.toLowerCase().equals("clientid") && st.hasMoreTokens()) {
				clientID = st.nextToken();
			} else if (tk.equalsIgnoreCase("filename") && st.hasMoreTokens()) {
				// il nome del file viene decodificato dall'esadecimale
				// vedere la classe it.eng.portlet.consolepec.gwt.shared.UriMapping
				fileName = Base64Utils.URLdecodeAlfrescoPath(st.nextToken());
			} else if (tk.equalsIgnoreCase("optionalfilename") && st.hasMoreTokens()) {
				optionalFileName = Base64Utils.URLdecodeAlfrescoPath(st.nextToken());
				if ("null".equalsIgnoreCase(optionalFileName)) {
					optionalFileName = null;
				}
			} else if (tk.equalsIgnoreCase("uuid") && st.hasMoreTokens()) {
				uuid = Base64Utils.URLdecodeAlfrescoPath(st.nextToken());
			} else if (tk.equalsIgnoreCase("bustafirma") && st.hasMoreTokens()) {
				String bustaFirmaString = st.nextToken();
				bustaFirma = Boolean.parseBoolean(bustaFirmaString);
			} else if (tk.equalsIgnoreCase("forcedownload") && st.hasMoreTokens()) {
				forceDownload = st.nextToken().equalsIgnoreCase("y");
			} else if (tk.equals("zipdir")) {
				zipDir = st.nextToken();
			} else if (tk.equals("zipname")) {
				zipName = st.nextToken();
			} else if (tk.equals("estrazioneamiantodir")) {
				estrazioniAmiantoDir = st.nextToken();
			} else if (tk.equals("estrazioneamiantoname")) {
				estrazioniAmiantoName = st.nextToken();
			} else if (tk.equals("versione")) {
				versione = Base64Utils.URLdecodeAlfrescoPath(st.nextToken());
			}
		}

		logger.debug("clientID: [{}] fileName: [{}] optionalFileName: [{}]", clientID, fileName, optionalFileName);

		String tempPath = null;

		if (clientID != null && fileName != null && !bustaFirma) {
			try {

				if (versione != null) {
					tempPath = gestAllg.downloadVersioneAllegato(clientID, fileName, versione);

				} else {
					tempPath = gestAllg.downloadAllegatoToFileSystem(clientID, fileName, null);
				}

				StringBuilder url = new StringBuilder("/download/");
				if (forceDownload) {
					url.append("forcedownload/y/");
				}

				url.append("filepath/").append(tempPath);
				request.getRequestDispatcher(url.toString()).forward(request, response);
			} catch (Exception e) {
				logger.error(e.getLocalizedMessage(), e);
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			} finally {
				String path = gestAllg.getTempDir() + File.separator + tempPath;
				File f = new File(path);
				delete(f);
			}
		}

		if (fileName != null && uuid != null && bustaFirma) {
			try {

				if (bustaFirma && fileName.toLowerCase().contains(".p7m")) {
					fileName = fileName.toLowerCase().split(".p7m")[0];
				}

				if (clientID != null) {
					tempPath = gestAllg.downloadAllegatoOriginaleToFileSystemFromUUID(clientID, uuid, fileName);
					StringBuilder url = new StringBuilder("/download/");
					if (forceDownload) {
						url.append("forcedownload/y/");
					}
					url.append("filepath/").append(tempPath);
					request.getRequestDispatcher(url.toString()).forward(request, response);

				} else {
					tempPath = gestAllg.downloadAllegatoOriginaleToFileSystemFromUUID(uuid, fileName);
					StringBuilder url = new StringBuilder("/download/");
					if (forceDownload) {
						url.append("forcedownload/y/");
					}
					url.append("filepath/").append(tempPath);
					request.getRequestDispatcher(url.toString()).forward(request, response);
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

		if (clientID == null && zipName != null && zipDir != null) {
			try {
				StringBuilder url = new StringBuilder("/download/");
				if (forceDownload) {
					url.append("forcedownload/y/");
				}
				url.append("filepath/").append(zipDir + File.separator + zipName);
				logger.debug("URL per il download: {}", url.toString());
				request.getRequestDispatcher(url.toString()).forward(request, response);
			} catch (Exception e) {
				logger.error(e.getLocalizedMessage(), e);
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			} finally {
				String path = gestAllg.getTempDir() + File.separator + zipDir + File.separator + zipName;
				File f = new File(path);
				delete(f);
			}
		}
		if (clientID == null && estrazioniAmiantoName != null && estrazioniAmiantoDir != null) {
			try {
				StringBuilder url = new StringBuilder("/download/");
				if (forceDownload) {
					url.append("forcedownload/y/");
				}
				url.append("filepath/").append(estrazioniAmiantoDir + File.separator + estrazioniAmiantoName);
				logger.debug("URL per il download: {}", url.toString());
				request.getRequestDispatcher(url.toString()).forward(request, response);
			} catch (Exception e) {
				logger.error(e.getLocalizedMessage(), e);
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			} finally {
				String path = gestAllg.getTempDir() + File.separator + estrazioniAmiantoDir + File.separator + estrazioniAmiantoName;
				File f = new File(path);
				delete(f);
			}
		}
		if (clientID == null && fileName == null && uuid != null) { // download per uidAlfresco
			try {
				tempPath = gestAllg.downloadAllegatoToFileSystemFromUUID(uuid);
				StringBuilder url = new StringBuilder("/download/");
				if (forceDownload) {
					url.append("forcedownload/y/");
				}
				url.append("filepath/").append(tempPath);
				request.getRequestDispatcher(url.toString()).forward(request, response);
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
