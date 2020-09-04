package it.eng.cobo.consolepec.integration.sit;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import it.eng.cobo.consolepec.integration.sit.generated.Request;
import it.eng.cobo.consolepec.integration.sit.generated.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SitXmlSerializer {

	private JAXBContext jaxbContext = null;

	private JAXBContext getJaxbContext() {
		if (jaxbContext == null) {
			synchronized (SitXmlSerializer.class) {
				if (jaxbContext == null) try {
					jaxbContext = JAXBContext.newInstance(Request.class, Response.class);
				} catch (JAXBException e) {
					log.error("Errore nel'istanziazione del jaxb context", e);
				}
			}
		}
		return jaxbContext;
	}

	public String serialize(Request request) throws JAXBException {
		Marshaller m = getJaxbContext().createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		StringWriter sw = new StringWriter();
		m.marshal(request, sw);
		return sw.toString();
	}

	public Response deserialize(String response) throws JAXBException {
		Unmarshaller um = getJaxbContext().createUnmarshaller();
		return (Response) um.unmarshal(new StringReader(response));
	}
}
