package it.eng.portlet.consolepec.gwt.server.rest.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.eng.cobo.consolepec.commons.exception.ConsoleDocumentaleException;
import it.eng.cobo.consolepec.util.json.JsonFactory;
import it.eng.portlet.consolepec.gwt.server.rest.client.DriveClient;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Giacomo F.M.
 * @since 2019-06-27
 */
@Slf4j
public class DriveFileUploadServlet extends HttpServlet {

	private static final long serialVersionUID = 5946804035158982720L;

	@Value("#{portlet['tempDir']}")
	private String tempDir;

	@Autowired
	private DriveClient driveClient;
	@Autowired
	protected JsonFactory jsonFactory;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		File file = null;
		String metadati = null, ruolo = null;
		File tempFolder = new File(tempDir, UUID.randomUUID().toString());
		try {
			if (!tempFolder.mkdir()) {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Impossibile creare la cartella di archiviazione.");
				return;
			}

			if (ServletFileUpload.isMultipartContent(request)) {
				List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
				for (FileItem item : items) {
					if (item.isFormField()) {
						if ("metadati".equals(item.getFieldName())) {
							metadati = item.getString();
						}
						if ("ruolo".equals(item.getFieldName())) {
							ruolo = item.getString();
						}
					} else {
						if ("stream".equals(item.getFieldName())) {
							try {
								file = new File(tempFolder, FilenameUtils.getName(item.getName()));
								BufferedInputStream bis = new BufferedInputStream(item.getInputStream());
								BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
								int inByte;
								while ((inByte = bis.read()) != -1)
									bos.write(inByte);
								bis.close();
								bos.close();
							} catch (Exception e) {
								log.error("Errore nel caricamento del file", e);
								writeResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, false, "Errore nel caricamento del file");
								return;
							}
						}
					}
				}
			}

			if (metadati == null || file == null) {
				writeResponse(response, HttpServletResponse.SC_BAD_REQUEST, false, "Errore nella request, le parti non sono corrette");
				return;
			}

			it.eng.cobo.consolepec.commons.drive.File jsonFile = driveClient.creaFile(metadati, ruolo, file);
			writeResponse(response, HttpServletResponse.SC_OK, true, "File caricato correttamente con id: " + jsonFile.getId());
		} catch (FileUploadException e) {
			log.error("Errore nella lettura della request", e);
			writeResponse(response, HttpServletResponse.SC_BAD_REQUEST, false, "Errore nella request, le parti non sono corrette");
		} catch (ConsoleDocumentaleException e) {
			log.error("Errore nel caricamento del file", e);
			writeResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, false, e.getOutputMessage());
		} finally {
			delete(tempFolder);
		}
	}

	private static void delete(File tempFolder) {
		try {
			if (tempFolder.exists()) {
				FileUtils.deleteDirectory(tempFolder);
			}
		} catch (Exception e) {
			log.error("Directory non eliminata", e);
		}
	}

	private static void writeResponse(final HttpServletResponse response, final int status, final boolean ok, final String msg) {
		try {
			JSONObject json = new JSONObject();
			json.put(ConsolePecConstants.DRIVE_OK, ok);
			json.put(ConsolePecConstants.DRIVE_MESSAGE, msg);
			json.write(response.getWriter());

			response.getWriter().flush();
			response.setStatus(status);
			response.setContentType("text/html");
		} catch (IOException e) {
			log.error("Errore nella creazione della risposta", e);
		}
	}

}
