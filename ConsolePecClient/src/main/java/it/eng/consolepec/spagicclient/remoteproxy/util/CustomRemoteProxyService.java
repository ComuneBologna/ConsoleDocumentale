package it.eng.consolepec.spagicclient.remoteproxy.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.soap.MTOMFeature;

import org.spagic.remoteproxyservice.Attachment;
import org.spagic.remoteproxyservice.JaxWSUtils;
import org.spagic.remoteproxyservice.MessageHeader;
import org.spagic.remoteproxyservice.RemoteProxy;
import org.spagic.remoteproxyservice.RemoteProxyRequest;
import org.spagic.remoteproxyservice.RemoteProxyResponse;
import org.spagic.remoteproxyservice.RemoteProxyService;

import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientErrorCode;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;

public class CustomRemoteProxyService {

	public static final int CONNECTIONTIMEOUT = 3600000;
	public static final int READTIMEOUT = 3600000;
	public static final int CHUNKSIZE = 8192;

	public static final String USERNAME = "USERNAME";
	public static final String PASSWORD = "PASSWORD";

	public static RemoteProxyResponse invokeSpagic(String request, String serviceProxyUrl, String username, String password, String spagicServiceId) {
		return invokeSpagic(request, serviceProxyUrl, username, password, spagicServiceId, null);
	}

	public static RemoteProxyResponse invokeSpagic(String request, String serviceProxyUrl, String username, String password, String spagicServiceId, String mimeTypes,
			Map<String, DataSource> streams) {

		// creazione nuovo remote proxy service
		RemoteProxy remoteProxyService = getRemoteProxyService(serviceProxyUrl);

		// header per autenticazione
		List<MessageHeader> authenticationHeaders = createAuthenticationHeader(username, password);

		// creazione request
		RemoteProxyRequest rpr = createRemoteProxyRequest(request, spagicServiceId, authenticationHeaders);

		// aggiungo eventuali allegati
		addAttachments(mimeTypes, rpr, streams);

		// invocazione del servizo
		RemoteProxyResponse response = remoteProxyService.invokeService(rpr);

		// check autorizzazione negata
		checkForSecurityException(response);

		return response;
	}

	public static RemoteProxyResponse invokeSpagic(String request, String serviceProxyUrl, String username, String password, String spagicServiceId, String mimeTypes, DataSource... streams) {

		// creazione nuovo remote proxy service
		RemoteProxy remoteProxyService = getRemoteProxyService(serviceProxyUrl);

		// header per autenticazione
		List<MessageHeader> authenticationHeaders = createAuthenticationHeader(username, password);

		// creazione request
		RemoteProxyRequest rpr = createRemoteProxyRequest(request, spagicServiceId, authenticationHeaders);

		// aggiungo eventuali allegati
		addAttachments(mimeTypes, rpr, streams);

		// invocazione del servizo
		RemoteProxyResponse response = remoteProxyService.invokeService(rpr);

		// check autorizzazione negata
		checkForSecurityException(response);

		return response;
	}

	// crea una nuova richiesta
	private static RemoteProxyRequest createRemoteProxyRequest(String request, String spagicServiceId, List<MessageHeader> authenticationHeaders) {
		RemoteProxyRequest rpr = new RemoteProxyRequest();
		rpr.getHeaders().addAll(authenticationHeaders);
		rpr.setSpagicServiceId(spagicServiceId);
		rpr.setBody(request);
		return rpr;
	}

	// imposta gli attachment
	private static void addAttachments(String mimeTypes, RemoteProxyRequest rpr, DataSource... streams) {
		if (streams != null) {
			for (DataSource ds : streams) {
				Attachment att = new Attachment();
				att.setName(ds.toString());
				att.setContentType(mimeTypes);
				att.setDh(new DataHandler(ds));
				rpr.getAttachments().add(att);
			}
		}
	}

	private static void addAttachments(String mimeTypes, RemoteProxyRequest rpr, Map<String, DataSource> streams) {
		if (streams != null) {
			for (Entry<String, DataSource> entry : streams.entrySet()) {
				Attachment att = new Attachment();
				att.setName(entry.getKey());
				att.setContentType(mimeTypes);
				att.setDh(new DataHandler(entry.getValue()));
				rpr.getAttachments().add(att);
			}
		}
	}

	// lancia un'eccezione in caso di errore di autenticazione server side
	private static void checkForSecurityException(RemoteProxyResponse invokeService) throws SpagicClientException {
		if (invokeService.getBody() == null) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}

		if (invokeService.getBody().contains("<SECURITY_EXCEPTION>")) {
			throw new SpagicClientException(SpagicClientErrorCode.EAUTHORIZATION, "Servizio non abilitato");
		}
	}

	// crea gli header per l'autenticazione
	private static List<MessageHeader> createAuthenticationHeader(String username, String password) {
		ArrayList<MessageHeader> list = new ArrayList<MessageHeader>();

		if (username != null && !username.isEmpty() && password != null && !password.isEmpty()) {
			MessageHeader mhUser = new MessageHeader();
			mhUser.setName(USERNAME);
			mhUser.setValue(username);
			list.add(mhUser);
			MessageHeader mhPwd = new MessageHeader();
			mhPwd.setName(PASSWORD);
			mhPwd.setValue(password);
			list.add(mhPwd);
		}
		return list;
	}

	// crea un nuovo remote proxy
	private static RemoteProxy getRemoteProxyService(String serviceProxyUrl) {
		RemoteProxyService service = null;
		if (RemoteProxyService.REMOTEPROXYSERVICE_WSDL_LOCATION == null) {
			String wsdlUrl = serviceProxyUrl + "?wsdl";
			URL wsdlURL;
			try {
				wsdlURL = new URL(wsdlUrl);
			} catch (MalformedURLException e) {
				return null;
			}
			service = new RemoteProxyService(wsdlURL, new QName("org:spagic:remoteproxyservice", "RemoteProxyService"));
		} else {
			service = new RemoteProxyService();
		}
		RemoteProxy remoteProxyPort = service.getRemoteProxyPort(new MTOMFeature());
		JaxWSUtils.setEndpointAddress(remoteProxyPort, serviceProxyUrl);
		JaxWSUtils.setTimeouts(remoteProxyPort, CONNECTIONTIMEOUT, READTIMEOUT);
		JaxWSUtils.setChunkSize(remoteProxyPort, CHUNKSIZE);

		Map<String, Object> requestContext = ((BindingProvider) remoteProxyPort).getRequestContext();

		// Jboss ws
		requestContext.put("javax.xml.ws.client.connectionTimeout", "" + CONNECTIONTIMEOUT);
		requestContext.put("javax.xml.ws.client.receiveTimeout", "" + READTIMEOUT);

		return remoteProxyPort;
	}

}
