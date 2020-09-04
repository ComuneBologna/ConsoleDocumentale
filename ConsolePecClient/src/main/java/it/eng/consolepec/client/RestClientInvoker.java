package it.eng.consolepec.client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.bologna.comune.alfresco.generic.service.Request;
import it.bologna.comune.alfresco.generic.service.Response;
import it.eng.cobo.consolepec.commons.services.InputStreamMapper;
import it.eng.cobo.consolepec.commons.spagic.Message;
import it.eng.cobo.consolepec.commons.spagic.Message.Attachment;
import it.eng.cobo.consolepec.util.io.ConsoleIOUtils;
import it.eng.cobo.consolepec.util.json.JsonFactory;
import it.eng.cobo.consolepec.util.json.JsonRawFactory;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.abs.SpagicClientRemoteProxyUtil;
import it.eng.consolepec.spagicclient.remoteproxy.abs.SpagicClientSerializationUtil;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientErrorCode;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.util.HttpClientFactory;
import it.eng.consolepec.spagicclient.util.JsonLegacyConfiguration;

public class RestClientInvoker implements ClientInvoker {

	protected static final Logger logger = LoggerFactory.getLogger(RestClientInvoker.class);

	private String serviceUrl;
	private final JsonRawFactory jsonCommonsFactory = new JsonRawFactory();
	private final JsonFactory jsonFactory = JsonFactory.jsonFactory(JsonLegacyConfiguration.class);

	public RestClientInvoker(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}

	@Override
	public <T> T invoke(String service, Utente utente, List<InputStreamMapper> streams, Object... request) throws SpagicClientException {
		logger.info("Invoco il servizio {}", service);

		try {
			Request genericRequest = new Request();
			genericRequest.setServicename(service);
			genericRequest.setRequestparam(jsonCommonsFactory.serialize(request));
			genericRequest.setUtente(SpagicClientRemoteProxyUtil.convert(utente));

			String xmlRequest = SpagicClientSerializationUtil.getRequestToString(genericRequest);

			Message messageIN = new Message();
			messageIN.setBodyText(xmlRequest);

			File[] files = null;
			HttpPost httpPost = null;

			try {

				if (streams == null || streams.isEmpty()) {
					httpPost = new HttpPost(serviceUrl);
					httpPost.setEntity(new StringEntity(jsonFactory.serialize(messageIN), ContentType.APPLICATION_JSON));

				} else {
					MultipartEntityBuilder builder = MultipartEntityBuilder.create() //
							.setMode(HttpMultipartMode.BROWSER_COMPATIBLE) //
							.addPart("request", new StringBody(jsonFactory.serialize(messageIN), ContentType.APPLICATION_JSON)); //

					files = new File[streams.size()];
					int i = 0;

					for (InputStreamMapper stream : streams) {
						OutputStream os = null;

						try {
							File file = ConsoleIOUtils.createSubTempFile(System.getProperty("java.io.tmpdir"), stream.getName());
							os = new FileOutputStream(file);
							ConsoleIOUtils.fastCopy(stream.getInputStream(), os);
							files[i] = file;
							builder.addPart("files", new FileBody(file));
							i++;

						} finally {
							ConsoleIOUtils.closeStreams(os);
						}
					}

					httpPost = new HttpPost(serviceUrl + "/attachments");
					httpPost.setEntity(builder.build());
				}

				long current = System.currentTimeMillis();

				try (CloseableHttpResponse httpResponse = HttpClientFactory.httpClient().execute(httpPost)) {

					long end = System.currentTimeMillis();
					logger.debug("Servizio: {} - Utente: {} - Tempo: {} ms", service, utente.getUsername(), end - current);

					if (httpResponse == null || HttpStatus.SC_OK != httpResponse.getStatusLine().getStatusCode() || httpResponse.getEntity() == null) {
						throw new Exception("Risposta non valida del server; stato: " + (httpResponse != null ? httpResponse.getStatusLine().getStatusCode() : "non riconosciuto"));
					}

					Message messageOUT = jsonFactory.deserialize(EntityUtils.toString(httpResponse.getEntity()), Message.class);
					if (messageOUT == null || messageOUT.getBodyText() == null || messageOUT.getBodyText() == null) {
						throw new Exception("Errore durante la conversione della risposta del server");
					}

					Response genericResponse = SpagicClientSerializationUtil.getResponseXmlToObject(messageOUT.getBodyText());
					if (genericResponse.getError() == null) {
						T instance = jsonCommonsFactory.deserialize(genericResponse.getResponseparam());
						setAttachment(instance, messageOUT);
						return instance;
					}

					logger.error("Errore durante l'invocazione del servizio {} : {}", service, genericResponse.getError().getMessage());
					throw SpagicClientRemoteProxyUtil.processErrorResponse(genericResponse.getError());
				}

			} finally {
				if (files != null) {
					ConsoleIOUtils.deleteParentFolder(files);
				}
			}

		} catch (Exception e) {
			logger.error("Errore durante l'invocazione del servizio", e);

			if (e instanceof SpagicClientException) {
				throw (SpagicClientException) e;
			}

			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	@Override
	public <T> T invoke(String service, Utente utente, Object... request) throws SpagicClientException {
		return invoke(service, utente, null, request);
	}

	/**
	 * Gestione della "serializzazione" degli allegati tramite l'annotazione @it.eng.cobo.consolepec.commons.services.Attachment
	 */
	private static <T> void setAttachment(final T instance, final Message response) throws IllegalArgumentException, IllegalAccessException {
		Field[] fields = instance.getClass().getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(it.eng.cobo.consolepec.commons.services.Attachment.class)) {
				field.setAccessible(true);

				if (field.getType().isAssignableFrom(Map.class)) {
					Map<String, InputStream> streams = new HashMap<String, InputStream>();

					for (Entry<String, Attachment> entry : response.getAttachments().entrySet()) {
						streams.put(entry.getKey(), entry.getValue().getInputStream());
					}

					field.set(instance, streams);

				} else {
					field.set(instance, response.getAttachment(field.getName()));
				}
			}
		}
	}
}
