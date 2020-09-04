package it.eng.consolepec.spagicclient.util;

import org.apache.http.impl.client.CloseableHttpClient;

import it.eng.cobo.consolepec.util.http.HttpClientUtils;

/**
 *
 * @author biagio.tozzi
 *
 */
public class HttpClientFactory {

	private static CloseableHttpClient httpClient;

	public static CloseableHttpClient httpClient() {

		if (httpClient == null) {
			httpClient = HttpClientUtils.createMultithreadHttpClient(5000, 100, 120);
		}

		return httpClient;

	}
}
