package it.eng.cobo.consolepec.util.http;

import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpResponse;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

public class HttpClientUtils {

	public static CloseableHttpClient createMultithreadHttpClient(int maxTotalOpenedConnection, int defaultMaxConnectionPerRoute, final int keepAliveTimeoutDefault) {

		PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
		connManager.setMaxTotal(maxTotalOpenedConnection);
		connManager.setDefaultMaxPerRoute(defaultMaxConnectionPerRoute);
		ConnectionKeepAliveStrategy keepAliveStrategy = null;

		if (keepAliveTimeoutDefault >= 0) {
			keepAliveStrategy = new ConnectionKeepAliveStrategy() {
				@Override
				public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
					HeaderElementIterator it = new BasicHeaderElementIterator(response.headerIterator(HTTP.CONN_KEEP_ALIVE));
					while (it.hasNext()) {
						HeaderElement he = it.nextElement();
						String param = he.getName();
						String value = he.getValue();
						if (value != null && param.equalsIgnoreCase("timeout")) {
							return Long.parseLong(value) * 1000;
						}
					}
					return keepAliveTimeoutDefault * 1000;
				}
			};
		}

		HttpClientBuilder builder = HttpClients.custom().setConnectionManager(connManager);

		if (keepAliveStrategy != null) {
			builder.setKeepAliveStrategy(keepAliveStrategy);
		}

		return builder.build();
	}

	public static CloseableHttpClient createBasicHttpClient(final int keepAliveTimeoutDefault) {

		ConnectionKeepAliveStrategy keepAliveStrategy = null;

		if (keepAliveTimeoutDefault >= 0) {
			keepAliveStrategy = new ConnectionKeepAliveStrategy() {
				@Override
				public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
					HeaderElementIterator it = new BasicHeaderElementIterator(response.headerIterator(HTTP.CONN_KEEP_ALIVE));
					while (it.hasNext()) {
						HeaderElement he = it.nextElement();
						String param = he.getName();
						String value = he.getValue();
						if (value != null && param.equalsIgnoreCase("timeout")) {
							return Long.parseLong(value) * 1000;
						}
					}
					return keepAliveTimeoutDefault * 1000;
				}
			};
		}

		HttpClientBuilder builder = HttpClients.custom().setConnectionManager(new BasicHttpClientConnectionManager());

		if (keepAliveStrategy != null) {
			builder.setKeepAliveStrategy(keepAliveStrategy);
		}

		return builder.build();
	}

}
