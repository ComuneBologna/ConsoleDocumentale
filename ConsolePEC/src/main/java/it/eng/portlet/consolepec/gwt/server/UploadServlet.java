package it.eng.portlet.consolepec.gwt.server;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.eng.cobo.consolepec.commons.exception.InvalidArgumentException;
import it.eng.cobo.consolepec.util.file.FileUtil;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.model.RispostaFileUploaderDTO;
import it.eng.portlet.consolepec.gwt.shared.model.TmpFileUploadDTO;

public class UploadServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private Logger logger = LoggerFactory.getLogger(UploadServlet.class);
	private String tempDir = "";
	private static final String ALPHABET = "0123456789";
	private final String REGEX_WIN = "^(?:[a-zA-Z]\\:(\\\\|\\/)|file\\:\\/\\/|\\\\\\\\|\\.(\\/|\\\\))([^\\\\\\/\\:\\*\\?\\<\\>\\\"\\|]+(\\\\|\\/){0,1})+$";

	@Value("#{portlet['tempDir']}")
	public void setTempDir(String tempDir) {
		this.tempDir = tempDir;
	}

	public String getTempDir() {
		return this.tempDir;
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
	}

	public boolean isMobileIos(HttpServletRequest request) {
		String ua = request.getHeader("User-Agent").toLowerCase();
		return (ua.contains("ipad") || ua.contains("ipod") || ua.contains("iphone"));
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		logger.debug("In doPost");
		File tempDir = null;
		List<FileItem> items = new ArrayList<FileItem>();
		PrintWriter out = response.getWriter();
		response.setHeader("Content-Type", "text/html");
		RispostaFileUploaderDTO risposta = new RispostaFileUploaderDTO();

		// Create a factory for disk-based file items
		FileItemFactory factory = new DiskFileItemFactory();
		// Create a new file upload handler
		ServletFileUpload fileUpload = new ServletFileUpload(factory);

		Long maxUploadBytes = Long.parseLong(getServletContext().getInitParameter("maxUploadSize"));
		logger.debug("Massima dimensione upload: {}", maxUploadBytes);
		fileUpload.setSizeMax(maxUploadBytes);
		String fileName = "";
		if (!ServletFileUpload.isMultipartContent(request)) {
			risposta.setError(true);
			risposta.setMessError("error multipart request not found");
			scriviRisposta(out, risposta);
			return;
		}

		try {

			items = fileUpload.parseRequest(request);

			if (items == null) {

				logger.debug("items is null : File not correctly uploaded");
				risposta.setError(true);
				risposta.setMessError("File not correctly uploaded");
				scriviRisposta(out, risposta);
				return;
			}

			Iterator<FileItem> iter = items.iterator();
			String dirGenerata = "";
			while (iter.hasNext()) {

				FileItem item = iter.next();

				// ///////////////////////////////////////////////////
				// http://commons.apache.org/fileupload/using.html //
				// ///////////////////////////////////////////////////

				fileName = item.getName();
				if (Pattern.matches(REGEX_WIN, item.getName()))
					fileName = item.getName().substring(item.getName().lastIndexOf("\\") + 1);

				logger.debug("fileName is : " + fileName);

				fileName = generaFileName(request, fileName);
				String typeMime = item.getContentType();
				logger.debug("typeMime is : " + typeMime);
				int sizeInBytes = (int) item.getSize();
				logger.debug("Size in bytes is : " + sizeInBytes);

				int i = 0;
				do {
					dirGenerata = UUID.randomUUID().toString();
					tempDir = new File(this.tempDir, dirGenerata);
					i++;
				} while (!tempDir.mkdirs() && i < 10);

				if (i == 10) {
					logger.debug("Non è stato possibile creare la dir temporanea");
					risposta.setError(true);
					risposta.setMessError("Non è stato possibile creare la dir temporanea");
					scriviRisposta(out, risposta);
					return;
				}

				File fileDaScrivere = new File(tempDir, fileName);
				logger.debug("Sto scrivendo: " + fileDaScrivere);
				item.write(fileDaScrivere);
				logger.debug("Il file è stato scritto ");

				risposta.setError(false);
				risposta.addTmpFiles(new TmpFileUploadDTO(fileName, dirGenerata.toString()));
			}

			this.scriviRisposta(out, risposta);

		} catch (SizeLimitExceededException e) {
			logger.debug("File size exceeds the limit");
			risposta.setError(true);
			risposta.setMessError("Il file " + fileName + " supera le dimensioni massime consentite. Dimensione massima consentita: "
					+ Long.parseLong(getServletContext().getInitParameter("maxUploadSize")) / 1024 / 1024 + " Mb.");
			scriviRisposta(out, risposta);
			return;
		} catch (Exception e) {
			logger.debug(e.getMessage(), e);
			risposta.setError(true);
			risposta.setMessError(ConsolePecConstants.ERROR_MESSAGE);
			scriviRisposta(out, risposta);
			FileUtils.deleteQuietly(tempDir);
			return;
		} finally {
			for (FileItem fileItem : items) {
				fileItem.delete();
			}
			pulisciDirectoryTemp();
		}

	}

	private String generaFileName(HttpServletRequest request, String fileName) throws InvalidArgumentException {
		if (isMobileIos(request) && fileName.equalsIgnoreCase("image.jpg")) {
			logger.warn("Identificato mobile ios con nome file image.jpg. Rinomino con sequenza casuale");
			Random rand = new Random();
			StringBuilder suffix = new StringBuilder();
			suffix.append("Immagine_");
			for (int i = 0; i < 5; i++) {
				suffix.append(ALPHABET.charAt(rand.nextInt(ALPHABET.length())));
			}
			suffix.append(".jpg");
			fileName = suffix.toString();
			logger.warn("Nuovo nome: {}", fileName);
		}

		return FileUtil.getValidFileName(fileName);
	}

	private void scriviRisposta(PrintWriter out, RispostaFileUploaderDTO risposta) {
		JSONObject rispostaObj = new JSONObject();
		try {
			// rispostaObj.put("error", risposta.isError());
			// rispostaObj.put("messError", risposta.getMessError());
			// rispostaObj.put("nomeFileCaricato", risposta.getNomeFileCaricato());
			// rispostaObj.put("tempDirGenerata", risposta.getTempDirGenerata());
			rispostaObj.put(RispostaFileUploaderDTO.ERR_KEY, risposta.isError());
			rispostaObj.put(RispostaFileUploaderDTO.ERRMSG_KEY, risposta.getMessError());
			rispostaObj.put(RispostaFileUploaderDTO.TMPFILES_KEY, risposta.getTmpFiles());
			rispostaObj.write(out);
			out.flush();
			out.close();
		} catch (JSONException e) {
			logger.debug("errore nello scrivere formato json", e);
		}

	}

	private void pulisciDirectoryTemp() {
		Long fileToDeleteAfter = Long.parseLong(getServletContext().getInitParameter("fileToDeleteAfter"));
		File tmpDirectory = new File(this.tempDir);
		if (tmpDirectory.exists()) {
			File[] listFiles = tmpDirectory.listFiles();
			for (File uploadedFile : listFiles) {
				if (uploadedFile.lastModified() + fileToDeleteAfter < System.currentTimeMillis()) {
					FileUtils.deleteQuietly(uploadedFile);
				}
			}
		}
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

}
