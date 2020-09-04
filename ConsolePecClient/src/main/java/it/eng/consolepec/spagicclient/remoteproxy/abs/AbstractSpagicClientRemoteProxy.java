package it.eng.consolepec.spagicclient.remoteproxy.abs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.activation.DataSource;
import javax.xml.bind.JAXBException;

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
import org.spagic.remoteproxyservice.RemoteProxyResponse;

import it.eng.cobo.consolepec.commons.spagic.Message;
import it.eng.cobo.consolepec.commons.spagic.Message.Attachment;
import it.eng.cobo.consolepec.util.io.ConsoleIOUtils;
import it.eng.cobo.consolepec.util.json.JsonFactory;
import it.eng.consolepec.spagicclient.remoteproxy.ResponseWithAttachementsDto;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientErrorCode;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.util.CustomRemoteProxyService;
import it.eng.consolepec.spagicclient.servicename.ServiceNamesUtil;
import it.eng.consolepec.spagicclient.util.HttpClientFactory;
import it.eng.consolepec.spagicclient.util.JsonLegacyConfiguration;
import lombok.AccessLevel;
import lombok.Getter;

public abstract class AbstractSpagicClientRemoteProxy<TRequest, TResponse> {

	protected static final Logger logger = LoggerFactory.getLogger(AbstractSpagicClientRemoteProxy.class);

	@Getter(AccessLevel.PROTECTED)
	private String serviceProxyUrl;

	@Getter(AccessLevel.PROTECTED)
	private String alfrescoUsername;

	@Getter(AccessLevel.PROTECTED)
	private String alfrescoPassword;

	@Getter(AccessLevel.PROTECTED)
	private String serviceUsername;

	@Getter(AccessLevel.PROTECTED)
	private String servicePassword;

	@Getter(AccessLevel.PROTECTED)
	private String restServiceUrl;

	private final JsonFactory jsonFactory = JsonFactory.jsonFactory(JsonLegacyConfiguration.class);

	protected AbstractSpagicClientRemoteProxy(String serviceProxyUrl, String alfrescoUsername, String alfrescoPassword, String serviceUsername, String servicePassword, String restServiceUrl) {
		super();
		this.serviceProxyUrl = serviceProxyUrl;
		this.alfrescoUsername = alfrescoUsername;
		this.alfrescoPassword = alfrescoPassword;
		this.serviceUsername = serviceUsername;
		this.servicePassword = servicePassword;
		this.restServiceUrl = restServiceUrl;
	}

	protected ResponseWithAttachementsDto<TResponse> invokeSpagicServiceWhitAttachementResponse(TRequest request) throws SpagicClientException {

		try {
			String xmlRequest = getJaxbRequestToXml(request);

			if (ServiceNamesUtil.CONSOLE_PEC_DISPATCHER.equals(getSpagicServiceId())) {
				it.eng.cobo.consolepec.commons.spagic.Message messageOUT = invokeLegacyService(xmlRequest, null);

				ResponseWithAttachementsDto<TResponse> response = new ResponseWithAttachementsDto<>();
				response.setResponse(getXmlResponseToJaxb(messageOUT.getBodyText()));
				HashMap<String, InputStream> attachments = new HashMap<>();

				for (Entry<String, Attachment> entry : messageOUT.getAttachments().entrySet()) {
					attachments.put(entry.getKey(), entry.getValue().getInputStream());
				}

				response.setAttachements(attachments);
				return response;

			}

			RemoteProxyResponse resp = CustomRemoteProxyService.invokeSpagic(xmlRequest, serviceProxyUrl, serviceUsername, servicePassword, getSpagicServiceId());
			if (resp.getBody() == null || resp.getBody().isEmpty()) {
				throw new Exception("Response del servizio non valida");
			}

			ResponseWithAttachementsDto<TResponse> response = new ResponseWithAttachementsDto<>();
			response.setResponse(getXmlResponseToJaxb(resp.getBody()));
			HashMap<String, InputStream> attachements = new HashMap<>();

			for (org.spagic.remoteproxyservice.Attachment attachment : resp.getAttachments()) {
				attachements.put(attachment.getName(), attachment.getDh().getInputStream());
			}

			response.setAttachements(attachements);
			return response;

		} catch (Exception e) {
			logger.error("Errore durante l'invocazione del servizio", e);
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION, SpagicClientErrorCode.EAPPLICATION.getDefaultMessage());
		}
	}

	protected TResponse invokeSpagicService(TRequest request) throws SpagicClientException {

		try {
			String xmlRequest = getJaxbRequestToXml(request);
			logger.trace("REQUEST: " + xmlRequest);

			if (ServiceNamesUtil.CONSOLE_PEC_DISPATCHER.equals(getSpagicServiceId())) {
				it.eng.cobo.consolepec.commons.spagic.Message messageOUT = invokeLegacyService(xmlRequest, null);
				TResponse response = getXmlResponseToJaxb(messageOUT.getBodyText());
				return response;

			}

			RemoteProxyResponse resp = CustomRemoteProxyService.invokeSpagic(xmlRequest, serviceProxyUrl, serviceUsername, servicePassword, getSpagicServiceId());
			if (resp.getBody() == null || resp.getBody().isEmpty()) {
				throw new Exception("Response del servizio non valida");
			}
			TResponse response = getXmlResponseToJaxb(resp.getBody());
			return response;

		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION, SpagicClientErrorCode.EAPPLICATION.getDefaultMessage());
		}
	}

	protected TResponse invokeSpagicServiceWhitAttachementRequest(TRequest request, String mimeTypes, Map<String, DataSource> streams) throws SpagicClientException {

		try {
			String xmlRequest = getJaxbRequestToXml(request);

			if (ServiceNamesUtil.CONSOLE_PEC_DISPATCHER.equals(getSpagicServiceId())) {
				it.eng.cobo.consolepec.commons.spagic.Message messageOUT = invokeLegacyService(xmlRequest, streams);
				TResponse response = getXmlResponseToJaxb(messageOUT.getBodyText());
				return response;

			}

			RemoteProxyResponse resp = CustomRemoteProxyService.invokeSpagic(xmlRequest, serviceProxyUrl, serviceUsername, servicePassword, getSpagicServiceId(), mimeTypes, streams);
			if (resp.getBody() == null || resp.getBody().isEmpty()) {
				throw new Exception("Response del servizio non valida");
			}
			TResponse response = getXmlResponseToJaxb(resp.getBody());
			return response;

		} catch (Exception e) {
			logger.error("Errore durante l'invocazione del servizio", e);
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION, SpagicClientErrorCode.EAPPLICATION.getDefaultMessage());
		}
	}

	private Message invokeLegacyService(String xmlRequest, Map<String, DataSource> streams) throws Exception {
		Message messageIN = new Message();
		messageIN.setBodyText(xmlRequest);

		File[] files = null;
		HttpPost httpPost = null;

		try {

			if (streams == null || streams.isEmpty()) {
				httpPost = new HttpPost(restServiceUrl);
				httpPost.setEntity(new StringEntity(jsonFactory.serialize(messageIN), ContentType.APPLICATION_JSON));

			} else {
				MultipartEntityBuilder builder = MultipartEntityBuilder.create() //
						.setMode(HttpMultipartMode.BROWSER_COMPATIBLE) //
						.addPart("request", new StringBody(jsonFactory.serialize(messageIN), ContentType.APPLICATION_JSON)); //

				files = new File[streams.size()];
				int i = 0;

				for (Entry<String, DataSource> stream : streams.entrySet()) {
					OutputStream os = null;

					try {
						File file = ConsoleIOUtils.createSubTempFile(System.getProperty("java.io.tmpdir"), stream.getKey());
						os = new FileOutputStream(file);
						ConsoleIOUtils.fastCopy(stream.getValue().getInputStream(), os);
						files[i] = file;
						builder.addPart("files", new FileBody(file));
						i++;

					} finally {
						ConsoleIOUtils.closeStreams(os);
					}
				}

				httpPost = new HttpPost(restServiceUrl + "/attachments");
				httpPost.setEntity(builder.build());
			}

			long current = System.currentTimeMillis();

			try (CloseableHttpResponse httpResponse = HttpClientFactory.httpClient().execute(httpPost)) {

				long end = System.currentTimeMillis();

				String service = getSpagicServiceId();
				if (xmlRequest != null) {
					int index1 = xmlRequest.indexOf("servicename");

					if (index1 != -1) {
						service = xmlRequest.substring(index1);
						int index2 = service.indexOf("=\"");

						if (index2 != -1) {
							service = service.substring(index2 + 2);
							int index3 = service.indexOf("\"");
							if (index3 != -1) {
								service = service.substring(0, index3);
							}
						}
					}
				}

				logger.debug("Servizio: {} - Tempo: {} ms", service, end - current);

				if (httpResponse == null || HttpStatus.SC_OK != httpResponse.getStatusLine().getStatusCode() || httpResponse.getEntity() == null) {
					throw new Exception("Risposta non valida del server; stato: " + (httpResponse != null ? httpResponse.getStatusLine().getStatusCode() : "non riconosciuto"));
				}

				Message messageOUT = jsonFactory.deserialize(EntityUtils.toString(httpResponse.getEntity()), Message.class);
				if (messageOUT == null || messageOUT.getBodyText() == null || messageOUT.getBodyText() == null) {
					throw new Exception("Errore durante la conversione della risposta del server");
				}

				return messageOUT;
			}

		} finally {
			if (files != null) {
				ConsoleIOUtils.deleteParentFolder(files);
			}
		}
	}

	/**
	 * @param request
	 * @return
	 * @throws JAXBException
	 */
	protected abstract String getJaxbRequestToXml(TRequest request) throws JAXBException;

	/**
	 * @param response
	 * @return
	 * @throws JAXBException
	 */
	protected abstract TResponse getXmlResponseToJaxb(String response) throws JAXBException;

	/**
	 * Ritorna l'id del servizio spagic da invocare
	 *
	 * @return
	 */
	protected abstract String getSpagicServiceId();

}
