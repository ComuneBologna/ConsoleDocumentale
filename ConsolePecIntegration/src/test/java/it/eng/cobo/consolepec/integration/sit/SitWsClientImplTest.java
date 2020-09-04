package it.eng.cobo.consolepec.integration.sit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.BeforeClass;
import org.junit.Test;

import it.eng.cobo.consolepec.integration.sit.bean.SitRequest;
import it.eng.cobo.consolepec.integration.sit.client.impl.SitWsClientImpl;
import it.eng.cobo.consolepec.integration.sit.exception.SitWsClientException;
import it.eng.cobo.consolepec.integration.sit.generated.ObjectFactory;
import it.eng.cobo.consolepec.integration.sit.generated.Request;
import it.eng.cobo.consolepec.integration.sit.generated.Response;

/**
 * @author GiacomoFM
 * @since 22/nov/2018
 */
public class SitWsClientImplTest {

	public static final String SIT_URL = "http://sitmappe.comune.bologna.it/toponomastica_3_1/ricerca";

	private static SitWsClientImpl sitWsClient;
	private static ObjectFactory factory;

	@BeforeClass
	public static void setUp() {
		sitWsClient = new SitWsClientImpl(SIT_URL);
		factory = new ObjectFactory();
	}

	@Test
	public void controlloNomeVia() throws SitWsClientException {
		Request request = new Request();
		request.setServiceName(SitRequest.SERVIZIO_RICERCA_VIA);

		request.setServiceParams(factory.createRequestServiceParams());
		request.getServiceParams().setMaxRowsRet(100);

		request.getServiceParams().setIndirizzo(factory.createRequestServiceParamsIndirizzo());
		request.getServiceParams().getIndirizzo().setTipo(SitRequest.RICERCA_PER_NOME);
		request.getServiceParams().getIndirizzo().setEnteCod("01");

		request.getServiceParams().getIndirizzo().setViaDenomCod("via dei lamponi");

		Response response = sitWsClient.invoke(request);
		assertFalse("Presenza errori", Boolean.valueOf(response.getHeader().getIsError().getValueError()));
		assertEquals("Controllo corrispondeza via errato", "VIA DEI LAMPONI", response.getDataOutput().getVia().getViaDenom());
	}

	@Test
	public void controlloCodiceVia() throws SitWsClientException {
		Request request = new Request();
		request.setServiceName(SitRequest.SERVIZIO_RICERCA_VIA);

		request.setServiceParams(factory.createRequestServiceParams());
		request.getServiceParams().setMaxRowsRet(100);

		request.getServiceParams().setIndirizzo(factory.createRequestServiceParamsIndirizzo());
		request.getServiceParams().getIndirizzo().setTipo(SitRequest.RICERCA_PER_CODICE);
		request.getServiceParams().getIndirizzo().setEnteCod("01");

		request.getServiceParams().getIndirizzo().setViaDenomCod("30800");

		Response response = sitWsClient.invoke(request);
		assertFalse("Presenza errori", Boolean.valueOf(response.getHeader().getIsError().getValueError()));
		assertEquals("Controllo corrispondeza via errato", "VIA DEI LAMPONI", response.getDataOutput().getVia().getViaDenom());
	}

	@Test
	public void controlloIndirizzoNomeVia() throws SitWsClientException {
		Request request = new Request();
		request.setServiceName(SitRequest.SERVIZIO_RICERCA_ZONA);

		request.setServiceParams(factory.createRequestServiceParams());
		request.getServiceParams().setMaxRowsRet(100);

		request.getServiceParams().setZona(factory.createRequestServiceParamsZona());
		request.getServiceParams().getZona().setTipo("TUTTE");

		request.getServiceParams().setIndirizzo(factory.createRequestServiceParamsIndirizzo());
		request.getServiceParams().getIndirizzo().setCivico(factory.createRequestServiceParamsIndirizzoCivico());
		request.getServiceParams().getIndirizzo().setTipo(SitRequest.RICERCA_PER_NOME);
		request.getServiceParams().getIndirizzo().setEnteCod("01");

		request.getServiceParams().getIndirizzo().setViaDenomCod("via dei lamponi");
		request.getServiceParams().getIndirizzo().getCivico().setCivicoNum("34");

		Response response = sitWsClient.invoke(request);
		assertFalse("Presenza errori", Boolean.valueOf(response.getHeader().getIsError().getValueError()));
		assertEquals("Controllo corrispondeza via errato", "VIA DEI LAMPONI", response.getDataOutput().getIndirizzo().getVia().getViaDenom());
		assertEquals("Controllo corrispondenza civico errato", "34", response.getDataOutput().getIndirizzo().getCivico().getCivicoNum());
	}

	@Test
	public void controlloIndirizzoCodiceVia() throws SitWsClientException {
		Request request = new Request();
		request.setServiceName(SitRequest.SERVIZIO_RICERCA_ZONA);

		request.setServiceParams(factory.createRequestServiceParams());
		request.getServiceParams().setMaxRowsRet(100);

		request.getServiceParams().setZona(factory.createRequestServiceParamsZona());
		request.getServiceParams().getZona().setTipo("TUTTE");

		request.getServiceParams().setIndirizzo(factory.createRequestServiceParamsIndirizzo());
		request.getServiceParams().getIndirizzo().setCivico(factory.createRequestServiceParamsIndirizzoCivico());
		request.getServiceParams().getIndirizzo().setTipo(SitRequest.RICERCA_PER_CODICE);
		request.getServiceParams().getIndirizzo().setEnteCod("01");

		request.getServiceParams().getIndirizzo().setViaDenomCod("30800");
		request.getServiceParams().getIndirizzo().getCivico().setCivicoNum("34");

		Response response = sitWsClient.invoke(request);
		assertFalse("Presenza errori", Boolean.valueOf(response.getHeader().getIsError().getValueError()));
		assertEquals("Controllo corrispondeza via errato", "VIA DEI LAMPONI", response.getDataOutput().getIndirizzo().getVia().getViaDenom());
		assertEquals("Controllo corrispondenza civico errato", "34", response.getDataOutput().getIndirizzo().getCivico().getCivicoNum());
	}

}
