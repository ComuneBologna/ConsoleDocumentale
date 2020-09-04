package it.eng.portlet.consolepec.gwt.server.rest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import com.google.gson.JsonObject;

import it.eng.cobo.consolepec.commons.exception.ApplicationException;
import it.eng.cobo.consolepec.commons.exception.ConsoleDocumentaleException;
import it.eng.cobo.consolepec.util.json.JsonFactory;
import it.eng.consolepec.spagicclient.util.HttpClientFactory;
import it.eng.portlet.consolepec.gwt.shared.SessionUtils;
import it.eng.portlet.consolepec.spring.bean.session.TipoLogin;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Giacomo F.M.
 * @since 2019-06-03
 */
@Slf4j
@RequiredArgsConstructor
public class RestClientInvoker {

	private static final JsonFactory jsonFactory = JsonFactory.defaultFactory();

	private static final String SCHEME = "http";
	private static final String SECURITY_PATH = "/security/login";
	private static final String API_PATH_REFRESH = "/service/refresh";

	private static final String AUTHORIZATION_HEADER = "Authorization";
	private static final String BEARER_BEGINNING = "Bearer ";

	private final UserSessionUtil userSessionUtil;
	private final String gatewayHost;
	private final String gatewayPort;
	private final String portletPassword;
	private final String federaPassword;
	private final String utenteEsternoPassword;
	
	private URI createURI(final String path, final String query) throws ApplicationException {
		try {
			return new URI(SCHEME, null, gatewayHost, Integer.parseInt(gatewayPort), path, query, null);

		} catch (URISyntaxException e) {
			log.error("Errore: {}", e);
			throw new ApplicationException("L'uri per l'invocazione del servizio rest non e' corretto");
		}
	}

	public boolean refreshAPI() throws ConsoleDocumentaleException {
		return post(API_PATH_REFRESH, null, null).isOk();
	}

	public boolean login() throws ApplicationException {
		HttpPost post = new HttpPost(createURI(SECURITY_PATH, null));
		JsonObject jo = new JsonObject();

		TipoLogin tipoLogin = userSessionUtil.getUtenteConsolePEC().getTipoLogin();
		String password = null;

		switch (tipoLogin) {
		case LDAP:
			password = portletPassword;
			break;

		case FEDERA:
			password = federaPassword;
			break;
		}

		if (userSessionUtil.getUtenteSpagic().isUtenteEsterno()) {
			password = utenteEsternoPassword;
		}

		jo.addProperty("username", userSessionUtil.getUtenteSpagic().getUsername());
		jo.addProperty("password", password);
		HttpEntity entity = new StringEntity(jo.toString(), ContentType.APPLICATION_JSON);
		post.setEntity(entity);

		try (CloseableHttpResponse response = HttpClientFactory.httpClient().execute(post)) {
			if (response != null) {
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					String token = jsonFactory.deserialize(readBody(response), Token.class).getToken();
					if (token == null || token.isEmpty()) {
						throw new ApplicationException("Nessun token definito nella response della login dei servizi REST");
					}
					userSessionUtil.getHttpSession().setAttribute(SessionUtils.SESSION_REST_TOKEN, token);
				}
				return response.getStatusLine().getStatusCode() == HttpStatus.SC_OK;
			}
			throw new ApplicationException("Nessuna risposta dall'invocazione del servizio di login: " + post.getURI());
		} catch (IOException e) {
			log.error(String.format("Errore durante l'esecuzione della richiesta di login: request[%s]", post), e);
			throw new ApplicationException(e, "Errore durante l'esecuzione della richiesta di login");
		}
	}

	@Data
	private static class Token {
		private String token;
	}

	private RestResponse execute(final HttpUriRequest request) throws ApplicationException {
		request.setHeader(AUTHORIZATION_HEADER, BEARER_BEGINNING + userSessionUtil.getHttpSession().getAttribute(SessionUtils.SESSION_REST_TOKEN));
		try (CloseableHttpResponse response = HttpClientFactory.httpClient().execute(request)) {
			if (response != null) {

				if (HttpStatus.SC_UNAUTHORIZED == response.getStatusLine().getStatusCode()) {
					login();
				}

				String json = readBody(response);
				return new RestResponse(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK, json);
			}

			throw new ApplicationException("Nessuna risposta dall'invocazione del servizio, request: " + request.getURI());

		} catch (IOException e) {
			log.error(String.format("Errore durante l'esecuzione della richiesta: request[%s]", request), e);
			throw new ApplicationException(e, "Errore durante l'esecuzione della richiesta");
		}
	}

	private static String readBody(HttpResponse response) throws ApplicationException {
		try {
			String content = EntityUtils.toString(response.getEntity());
			log.debug("Response del servizio: {}", content);
			return content;
		} catch (IOException e) {
			log.error("Errore nella lettura della response", e);
			throw new ApplicationException(e, "Errore nella lettura della response");
		}
	}

	public RestResponse get(final String path, final String query) throws ConsoleDocumentaleException {
		return execute(new HttpGet(createURI(path, query)));
	}

	public RestResponse post(final String path, final String query, final String body) throws ConsoleDocumentaleException {
		HttpPost post = new HttpPost(createURI(path, query));

		if (body != null) {
			HttpEntity entity = new StringEntity(body, ContentType.APPLICATION_JSON);
			post.setEntity(entity);
		}

		return execute(post);
	}

	public RestResponse customPost(final String path, final String query, final HttpEntity entity) throws ApplicationException {
		HttpPost post = new HttpPost(createURI(path, query));
		post.setEntity(entity);
		return execute(post);
	}

	public RestResponse put(final String path, final String query, final String body) throws ConsoleDocumentaleException {
		HttpPut put = new HttpPut(createURI(path, query));

		if (body != null) {
			HttpEntity entity = new StringEntity(body, ContentType.APPLICATION_JSON);
			put.setEntity(entity);
		}

		return execute(put);
	}

	public RestResponse customPut(final String path, final String query, final HttpEntity entity) throws ConsoleDocumentaleException {
		HttpPut put = new HttpPut(createURI(path, query));
		put.setEntity(entity);
		return execute(put);
	}

	public RestResponse delete(final String path, final String query) throws ConsoleDocumentaleException {
		HttpDelete delete = new HttpDelete(createURI(path, query));
		return execute(delete);
	}

	public static ErrorResponse error(String json) {
		try {
			return jsonFactory.deserialize(json, ErrorResponse.class);
		} catch (Exception e) {
			log.warn("Problema nella deserializzazione della risposta di errore: " + json, e);
			return null;
		}
	}

}
