package it.eng.consolepec.spagicclient.soaputil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpagicClientLogHandler implements SOAPHandler<SOAPMessageContext> {

	private Logger logger = LoggerFactory.getLogger(SpagicClientLogHandler.class);

	@Override
	public boolean handleMessage(SOAPMessageContext context) {
		SOAPMessage message = context.getMessage();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			message.writeTo(baos);
			boolean isOutboundMessage = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
			if (isOutboundMessage) {
				logger.trace("OUTBOUND MESSAGE -> {}", baos.toString());
			} else {
				logger.trace("OUTBOUND MESSAGE -> {}", baos.toString());
			}
		} catch (SOAPException e) {
			logger.error("SOAPException", e);
		} catch (IOException e) {
			logger.error("IOException", e);
		}

		finally {
			if (baos != null)
				try {
					baos.close();
				} catch (IOException e) {
					logger.error("IOException", e);
				}
		}

		return true;
	}

	@Override
	public boolean handleFault(SOAPMessageContext context) {
		SOAPMessage message = context.getMessage();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			message.writeTo(baos);
			logger.trace("FAULT ERRO -> {}", baos.toString());
		} catch (SOAPException e) {
			logger.error("SOAPException", e);
		} catch (IOException e) {
			logger.error("IOException", e);
		}

		finally {
			if (baos != null)
				try {
					baos.close();
				} catch (IOException e) {
					logger.error("IOException", e);
				}
		}

		return true;
	}

	@Override
	public void close(MessageContext context) {
		// TODO Auto-generated method stub

	}

	@Override
	public Set<QName> getHeaders() {
		// TODO Auto-generated method stub
		return null;
	}

}
