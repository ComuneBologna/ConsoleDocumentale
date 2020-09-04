package it.eng.consolepec.spagicclient.remoteproxy.abs;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientErrorCode;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;

public class NewSpagicClientSerializationUtil {

	private static Logger logger = LoggerFactory.getLogger(NewSpagicClientSerializationUtil.class);

	private static JAXBContext jaxbContextGenerico;

	private static Map<Class<?>, InternalObjectFactory<?>> objectFactoryMap = new HashMap<Class<?>, InternalObjectFactory<?>>();

	static {
		add(it.bologna.comune.spagic.gestione.datiaggiuntivi.Request.class, new InternalObjectFactory<it.bologna.comune.spagic.gestione.datiaggiuntivi.Request>() {

			@Override
			public JAXBElement<it.bologna.comune.spagic.gestione.datiaggiuntivi.Request> createJaxbElement(it.bologna.comune.spagic.gestione.datiaggiuntivi.Request o) {
				return new it.bologna.comune.spagic.gestione.datiaggiuntivi.ObjectFactory().createRequest(o);
			}
		});
		add(it.bologna.comune.spagic.gestione.datiaggiuntivi.Response.class, new InternalObjectFactory<it.bologna.comune.spagic.gestione.datiaggiuntivi.Response>() {

			@Override
			public JAXBElement<it.bologna.comune.spagic.gestione.datiaggiuntivi.Response> createJaxbElement(it.bologna.comune.spagic.gestione.datiaggiuntivi.Response o) {
				return new it.bologna.comune.spagic.gestione.datiaggiuntivi.ObjectFactory().createResponse(o);
			}
		});
		add(it.bologna.comune.spagic.taskfirma.Request.class, new InternalObjectFactory<it.bologna.comune.spagic.taskfirma.Request>() {

			@Override
			public JAXBElement<it.bologna.comune.spagic.taskfirma.Request> createJaxbElement(it.bologna.comune.spagic.taskfirma.Request obj) {
				return new it.bologna.comune.spagic.taskfirma.ObjectFactory().createRequest(obj);
			}
		});
		add(it.bologna.comune.spagic.taskfirma.Response.class, new InternalObjectFactory<it.bologna.comune.spagic.taskfirma.Response>() {

			@Override
			public JAXBElement<it.bologna.comune.spagic.taskfirma.Response> createJaxbElement(it.bologna.comune.spagic.taskfirma.Response obj) {
				return new it.bologna.comune.spagic.taskfirma.ObjectFactory().createResponse(obj);
			}
		});
		add(it.bologna.comune.spagic.gestione.metadati.Request.class, new InternalObjectFactory<it.bologna.comune.spagic.gestione.metadati.Request>() {

			@Override
			public JAXBElement<it.bologna.comune.spagic.gestione.metadati.Request> createJaxbElement(it.bologna.comune.spagic.gestione.metadati.Request obj) {
				return new it.bologna.comune.spagic.gestione.metadati.ObjectFactory().createRequest(obj);
			}
		});
		add(it.bologna.comune.spagic.gestione.metadati.Response.class, new InternalObjectFactory<it.bologna.comune.spagic.gestione.metadati.Response>() {

			@Override
			public JAXBElement<it.bologna.comune.spagic.gestione.metadati.Response> createJaxbElement(it.bologna.comune.spagic.gestione.metadati.Response obj) {
				return new it.bologna.comune.spagic.gestione.metadati.ObjectFactory().createResponse(obj);
			}
		});

		add(it.bologna.comune.alfresco.creazione.template.RequestEmail.class, new InternalObjectFactory<it.bologna.comune.alfresco.creazione.template.RequestEmail>() {

			@Override
			public JAXBElement<it.bologna.comune.alfresco.creazione.template.RequestEmail> createJaxbElement(it.bologna.comune.alfresco.creazione.template.RequestEmail obj) {
				return new it.bologna.comune.alfresco.creazione.template.ObjectFactory().createRequestEmail(obj);
			}
		});
		add(it.bologna.comune.alfresco.creazione.template.RequestPdf.class, new InternalObjectFactory<it.bologna.comune.alfresco.creazione.template.RequestPdf>() {

			@Override
			public JAXBElement<it.bologna.comune.alfresco.creazione.template.RequestPdf> createJaxbElement(it.bologna.comune.alfresco.creazione.template.RequestPdf obj) {
				return new it.bologna.comune.alfresco.creazione.template.ObjectFactory().createRequestPdf(obj);
			}
		});
		add(it.bologna.comune.alfresco.creazione.template.RequestCopiaTemplate.class, new InternalObjectFactory<it.bologna.comune.alfresco.creazione.template.RequestCopiaTemplate>() {

			@Override
			public JAXBElement<it.bologna.comune.alfresco.creazione.template.RequestCopiaTemplate> createJaxbElement(it.bologna.comune.alfresco.creazione.template.RequestCopiaTemplate obj) {
				return new it.bologna.comune.alfresco.creazione.template.ObjectFactory().createRequestCopiaTemplate(obj);
			}
		});

		add(it.bologna.comune.spagic.modifica.fascicolo.Request.class, new InternalObjectFactory<it.bologna.comune.spagic.modifica.fascicolo.Request>() {

			@Override
			public JAXBElement<it.bologna.comune.spagic.modifica.fascicolo.Request> createJaxbElement(it.bologna.comune.spagic.modifica.fascicolo.Request obj) {
				return new it.bologna.comune.spagic.modifica.fascicolo.ObjectFactory().createRequest(obj);
			}
		});

		add(it.bologna.comune.spagic.aggiornapg.Request.class, new InternalObjectFactory<it.bologna.comune.spagic.aggiornapg.Request>() {

			@Override
			public JAXBElement<it.bologna.comune.spagic.aggiornapg.Request> createJaxbElement(it.bologna.comune.spagic.aggiornapg.Request obj) {
				return new it.bologna.comune.spagic.aggiornapg.ObjectFactory().createRequest(obj);
			}
		});

		add(it.bologna.comune.spagic.gestione.allegato.Request.class, new InternalObjectFactory<it.bologna.comune.spagic.gestione.allegato.Request>() {

			@Override
			public JAXBElement<it.bologna.comune.spagic.gestione.allegato.Request> createJaxbElement(it.bologna.comune.spagic.gestione.allegato.Request obj) {
				return new it.bologna.comune.spagic.gestione.allegato.ObjectFactory().createRequest(obj);
			}
		});
	}

	private static JAXBContext getJaxbContextGenerico() {
		if (jaxbContextGenerico == null) {
			synchronized (SpagicClientSerializationUtil.class) {
				if (jaxbContextGenerico == null) {
					try {

						jaxbContextGenerico = JAXBContext.newInstance(objectFactoryMap.keySet().toArray(new Class<?>[] {}));

					} catch (JAXBException e) {
						logger.info("Errore nella creazione del contesto Jaxb.", e);
					}
				}
			}

		}
		return jaxbContextGenerico;
	}

	private static <T> void add(Class<T> clazz, InternalObjectFactory<T> factory) {
		objectFactoryMap.put(clazz, factory);
	}

	public static <T> T deserialize(String xml, Class<T> clazz) {
		try {
			Unmarshaller unmarshaller = getJaxbContextGenerico().createUnmarshaller();
			return unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(xml.getBytes("utf-8"))), clazz).getValue();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static <T> String serialize(T obj) {
		try {
			Marshaller marshaller = getJaxbContextGenerico().createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();

			@SuppressWarnings("unchecked") JAXBElement<T> jaxbElement = ((InternalObjectFactory<T>) objectFactoryMap.get(obj.getClass())).createJaxbElement(obj);
			marshaller.marshal(jaxbElement, sw);
			return sw.toString();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	private interface InternalObjectFactory<T> {
		public JAXBElement<T> createJaxbElement(T obj);
	}

}
