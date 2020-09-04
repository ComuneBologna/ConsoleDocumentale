package it.eng.cobo.consolepec.integration;

import java.io.ByteArrayOutputStream;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SOAPLoggingHandler implements SOAPHandler<SOAPMessageContext> {

	@Override
	public Set<QName> getHeaders() {
		return null;
	}

	@Override
	public boolean handleMessage(SOAPMessageContext smc) {
		logMessage(smc);
		return true;
	}

	@Override
	public boolean handleFault(SOAPMessageContext smc) {
		logMessage(smc);
		return true;
	}

	@Override
	public void close(MessageContext messageContext) {/**/}

	private static void logMessage(SOAPMessageContext smc) {
		boolean isOutboundMessage = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		if (isOutboundMessage) {
			log.debug("OUTBOUND MESSAGE\n");
			// System.out.println("\nOUTBOUND MESSAGE:");
		} else {
			log.debug("INBOUND MESSAGE\n");
			// System.out.println("\nINBOUND MESSAGE");
		}

		SOAPMessage message = smc.getMessage();
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			message.writeTo(baos);
			// message.writeTo(System.out);
			log.debug("   " + baos.toString());
		} catch (Exception e) {
			log.warn("Errore nel log del traffico", e);
		}
	}
}