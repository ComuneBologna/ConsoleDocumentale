package it.eng.cobo.consolepec.integration.sara.client.impl;

import javax.xml.ws.BindingProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.eng.cobo.consolepec.integration.sara.client.SaraOnlineClient;
import it.eng.cobo.consolepec.integration.sara.client.SaraOnlineConvertUtil;
import it.eng.cobo.consolepec.integration.sara.client.emissionecontrassegno.EmissioneContrassegnoRequest;
import it.eng.cobo.consolepec.integration.sara.client.emissionecontrassegno.EmissioneContrassegnoResponse;
import it.eng.cobo.consolepec.integration.sara.client.emissionepermesso.EmissionePermessoRequest;
import it.eng.cobo.consolepec.integration.sara.client.emissionepermesso.EmissionePermessoResponse;
import it.eng.cobo.consolepec.integration.sara.generated.contrassegni.WsSaraOnline;
import it.eng.cobo.consolepec.integration.sara.generated.contrassegni.WsSaraOnline_Service;

/**
 * 
 * @author biagiot
 * 
 */
public class SaraOnlineClientImpl implements SaraOnlineClient {

	private static final Logger logger = LoggerFactory.getLogger(SaraOnlineClientImpl.class);

	private final WsSaraOnline wsSaraOnlinePort;

	public SaraOnlineClientImpl(String endpointAddress) {
		WsSaraOnline_Service service = new WsSaraOnline_Service();
		wsSaraOnlinePort = service.getWsSaraOnline();
		BindingProvider bp = (BindingProvider) wsSaraOnlinePort;
		bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointAddress);
	}

	@Override
	public EmissionePermessoResponse emissionePermesso(EmissionePermessoRequest request) {
		try {
			logger.info("Start Emissione Permesso");
			it.eng.cobo.consolepec.integration.sara.generated.contrassegni.EmissionePermessoResponse responseJAXB = wsSaraOnlinePort.emissionePermesso(
					SaraOnlineConvertUtil.convertEmissionePermessoRequestToJAXB(request));
			return SaraOnlineConvertUtil.convertJAXBToEmissionePermessoResponse(responseJAXB);

		} finally {
			logger.info("End Emissione Permesso");
		}
	}

	@Override
	public EmissioneContrassegnoResponse emissioneContrassegno(EmissioneContrassegnoRequest request) {
		try {
			logger.info("Start Emissione Contrassegno");
			it.eng.cobo.consolepec.integration.sara.generated.contrassegni.EmissioneContrassegnoResponse responseJAXB = wsSaraOnlinePort.emissioneContrassegno(
					SaraOnlineConvertUtil.convertEmissioneContrassegnoRequestToJAXB(request));
			return SaraOnlineConvertUtil.convertJAXBToEmissioneContrassegnoResponse(responseJAXB);

		} finally {
			logger.info("End Emissione Contrassegno");
		}
	}

}
