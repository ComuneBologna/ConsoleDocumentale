package it.eng.cobo.consolepec.integration.sit.client.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

import javax.xml.bind.JAXBException;

import org.apache.http.client.methods.HttpPost;

import it.eng.cobo.consolepec.integration.sit.SitXmlSerializer;
import it.eng.cobo.consolepec.integration.sit.bean.SitRequest;
import it.eng.cobo.consolepec.integration.sit.bean.SitResponse;
import it.eng.cobo.consolepec.integration.sit.client.SitWsClient;
import it.eng.cobo.consolepec.integration.sit.exception.SitWsClientException;
import it.eng.cobo.consolepec.integration.sit.generated.Request;
import it.eng.cobo.consolepec.integration.sit.generated.Response;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class SitWsClientImpl implements SitWsClient {

	private static final String REQUEST_MYSTERIOUS_REQUIRED_HEADER = "xmlRequest=";
	private static final List<String> RESPONSE_UNSERIALIZABLE_TAG = Arrays.asList("<!DOCTYPE response SYSTEM \"dtd/RicercaVia_Risposta.dtd\">",
			"<!DOCTYPE response SYSTEM \"http://sitmappe.comune.bologna.it:80/toponomastica_3_1/dtd/RicercaZona_Risposta.dtd\">");

	private String sitUrl;
	private String proxyUrl;
	private Integer proxyPort;

	private final SitXmlSerializer serializer = new SitXmlSerializer();

	public SitWsClientImpl(String sitUrl) {
		this.sitUrl = sitUrl;
	}

	@Override
	public SitResponse validaDescrizioneVia(String via) throws SitWsClientException {
		SitRequest request = SitRequest.builder() //
				.servizioRicercaVia() //
				.nomeVia(via) //
				.build();
		return new SitResponse(invoke(request.getRequest()));
	}

	@Override
	public SitResponse validaIndirizzoCompleto(String via, String numeroCivico) throws SitWsClientException {
		SitRequest request = SitRequest.builder() //
				.servizioRicercaZona() //
				.nomeVia(via, numeroCivico) //
				.aggiungiData(Calendar.getInstance()) //
				.impostaServiceOutput() //
				.build();
		return new SitResponse(invoke(request.getRequest()));
	}

	@Override
	public SitResponse validaIndirizzoCompleto(String via, String numeroCivico, String esponente) throws SitWsClientException {
		SitRequest request = SitRequest.builder() //
				.servizioRicercaZona() //
				.nomeVia(via, numeroCivico, esponente) //
				.aggiungiData(Calendar.getInstance()) //
				.impostaServiceOutput() //
				.build();
		return new SitResponse(invoke(request.getRequest()));
	}

	@Override
	public SitResponse decodificaCodiceVia(String codice) throws SitWsClientException {
		SitRequest request = SitRequest.builder() //
				.servizioRicercaVia() //
				.codiceVia(codice) //
				.build();
		return new SitResponse(invoke(request.getRequest()));
	}

	@Override
	public SitResponse decodificaCodiceViaIndirizzoCompleto(String codiceVia, String numeroCivico, String esponente) throws SitWsClientException {
		SitRequest request = SitRequest.builder() //
				.servizioRicercaZona() //
				.codiceVia(codiceVia, numeroCivico, esponente) //
				.aggiungiData(Calendar.getInstance()) //
				.impostaServiceOutput() //
				.build();
		return new SitResponse(invoke(request.getRequest()));
	}

	public Response invoke(Request request) throws SitWsClientException {
		try {
			String serializedRequest = serializeRequest(request);
			log.debug("Richiesta in invio al SIT [{}]", serializedRequest);

			String serializedResponse = sendRequest(serializedRequest);
			log.debug("Risposta ricevuta dal SIT [{}]", serializedResponse);

			Response response = deserializeResponse(serializedResponse);
			if (Boolean.parseBoolean(response.getHeader().getIsError().getValueError())) {
				throw new SitWsClientException("Errore nella ricerca: " + response.getHeader().getMessage());
			}
			return response;
		} catch (JAXBException | IOException e) {
			log.error("Errore nell'invocazione dei servizi SIT", e);
			throw new SitWsClientException("Errore nell'invocazione dei servizi SIT", e);
		}
	}

	private String serializeRequest(Request request) throws JAXBException {
		StringBuilder sb = new StringBuilder(REQUEST_MYSTERIOUS_REQUIRED_HEADER);
		return sb.append(serializer.serialize(request)).toString();
	}

	private Response deserializeResponse(String serializedResponse) throws JAXBException {
		for (String s : RESPONSE_UNSERIALIZABLE_TAG) {
			serializedResponse = serializedResponse.replaceAll(s, "");
		}
		return serializer.deserialize(serializedResponse);
	}

	private String sendRequest(String request) throws IOException, SitWsClientException {
		if (this.sitUrl == null) {
			throw new SitWsClientException("URL del SIT non impostato");
		}
		HttpURLConnection connection = null;
		try {
			URL url = new URL(this.sitUrl);
			if (this.proxyUrl != null && this.proxyPort != null) {
				Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(this.proxyUrl, this.proxyPort));
				connection = (HttpURLConnection) url.openConnection(proxy);
			} else {
				connection = (HttpURLConnection) url.openConnection();
			}
			connection.setRequestMethod(HttpPost.METHOD_NAME);
			connection.setDoOutput(true);

			try (OutputStream output = connection.getOutputStream()) {
				output.write(request.getBytes("UTF-8"));
			}

			int responseCode = connection.getResponseCode();
			if (responseCode != HttpURLConnection.HTTP_OK) {
				throw new SitWsClientException("Errore durante l'invocazione del SIT, error response code: " + responseCode + ", " + connection.getResponseMessage());
			}

			String response = null;
			try (InputStream responseStream = connection.getInputStream(); Scanner scanner = new Scanner(responseStream)) {
				response = scanner.useDelimiter("\\A").hasNext() ? scanner.useDelimiter("\\A").next() : "";
			}

			return response;
		} finally {
			if (connection != null)
				connection.disconnect();
		}
	}

}
