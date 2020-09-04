package it.eng.cobo.consolepec.integration.sara.client.impl;

import javax.xml.ws.BindingProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.eng.cobo.consolepec.integration.sara.client.SaraWSProfilaturaClient;
import it.eng.cobo.consolepec.integration.sara.client.SaraWSProfilaturaProperties;
import it.eng.cobo.consolepec.integration.sara.client.SaraWSProfilaturaRequest.GetRuoliProfiloRequest;
import it.eng.cobo.consolepec.integration.sara.client.SaraWSProfilaturaRequest.GetRuoliProfiloResponse;
import it.eng.cobo.consolepec.integration.sara.client.SaraWsProfilaturaConverterUtil;
import it.eng.cobo.consolepec.integration.sara.generated.profilazione.WsProfilatura;
import it.eng.cobo.consolepec.integration.sara.generated.profilazione.WsProfilatura_Service;

/**
 * Impl. client per invocazione WS Profilatura SARA
 * 
 * @author biagiot
 *
 */
public class SaraWSProfilaturaClientImpl implements SaraWSProfilaturaClient {

	private static final Logger log = LoggerFactory.getLogger(SaraWSProfilaturaClientImpl.class);

	private SaraWSProfilaturaProperties saraWSProfilaturaProperties;
	private WsProfilatura wsProfilaturaPort;

	public SaraWSProfilaturaClientImpl(SaraWSProfilaturaProperties saraWSProfilaturaProperties) {
		this.saraWSProfilaturaProperties = saraWSProfilaturaProperties;

		WsProfilatura_Service service = new WsProfilatura_Service();
		wsProfilaturaPort = service.getWsProfilatura();
		BindingProvider bp = (BindingProvider) wsProfilaturaPort;
		bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, saraWSProfilaturaProperties.getProperties().get("endpoint"));
	}

	@Override
	public GetRuoliProfiloResponse getRuoliProfilo(String codiceFiscale) {
		log.info("Inizio invocazione servizio GetRuoliProfilo da SARA");

		try {
			GetRuoliProfiloRequest request = new GetRuoliProfiloRequest();
			request.setUsername(codiceFiscale);
			request.setMatricola(saraWSProfilaturaProperties.getProperties().get("matricola"));
			request.setPassword(saraWSProfilaturaProperties.getProperties().get("password"));
			return SaraWsProfilaturaConverterUtil.fromJaxb(wsProfilaturaPort.getRuoliProfilo(SaraWsProfilaturaConverterUtil.toJaxb(request)));

		} finally {
			log.info("Fine invocazione servizio GetRuoliProfilo da SARA");

		}
	}

}
