package it.eng.cobo.consolepec.integration.maildispatcher.client.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import it.eng.cobo.consolepec.commons.exception.ApplicationException;
import it.eng.cobo.consolepec.integration.maildispatcher.bean.MailDispatcherResponse;
import it.eng.cobo.consolepec.integration.maildispatcher.client.MailDispatcherClient;
import it.eng.cobo.consolepec.util.zip.ZipUtils;

/**
 *
 * @author biagiot
 *
 */
public class MailDispatcherClientImpl implements MailDispatcherClient {

	private static final Logger logger = LoggerFactory.getLogger(MailDispatcherClientImpl.class);

	private String url;
	private final Gson gson = new Gson();

	public MailDispatcherClientImpl(String url) {
		this.url = url;
	}

	@Override
	public MailDispatcherResponse dispatch(File... files) throws ApplicationException {
		logger.info("MailDispatcherClient - Start");

		File zipFile = null;
		CloseableHttpResponse response = null;

		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			zipFile = createZip(files);

			if (!zipFile.exists()) {
				logger.error("Zip non creato");
				throw new IOException("Zip non creato");
			}

			logger.debug("Zip creato: {}", zipFile.getAbsolutePath());

			HttpEntity data = MultipartEntityBuilder.create() //
					.setMode(HttpMultipartMode.BROWSER_COMPATIBLE) //
					.addBinaryBody("zip_file", zipFile, ContentType.APPLICATION_OCTET_STREAM, "files.zip") //
					.build();

			HttpUriRequest request = RequestBuilder.post(url).setEntity(data).build();

			response = httpClient.execute(request);
			HttpEntity result = response.getEntity();
			String json = EntityUtils.toString(result);
			return gson.fromJson(json, MailDispatcherResponse.class);

		} catch (IOException e) {
			logger.error("Errore IO", e);
			throw new ApplicationException(e);

		} finally {

			try {
				FileUtils.deleteDirectory(zipFile.getParentFile());

			} catch (Throwable t) {
				logger.error("Errore durante l'eliminazione della cartella temporanea {}", zipFile.getParentFile().getAbsolutePath(), t);
			}

			try {
				response.close();
			} catch (IOException e) {}

			logger.info("MailDispatcherClient - End");
		}
	}

	private static File createZip(File... files) throws IOException {
		Path sourceParent = Paths.get(System.getProperty("java.io.tmpdir") + File.separator + UUID.randomUUID().toString() + File.separator + "files");
		Path sourceDir = Paths.get(sourceParent + File.separator + "files");
		Path zipPath = Paths.get(sourceParent + ".zip");

		try {
			Files.createDirectories(sourceDir);

			for (File file : files) {
				FileUtils.copyFileToDirectory(file, sourceDir.toFile());
			}

			ZipUtils.zipFolder(sourceParent, zipPath);

		} catch (Exception e) {
			logger.error("Errore durante la creazione dello zip", e);
			throw new IOException(e);
		}

		File zipFile = zipPath.toFile();
		return zipFile;
	}
}
