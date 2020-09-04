package it.eng.consolepec.spagicclient.soaputil;

import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.PortInfo;

public class SpagicClientHandlerResolver implements HandlerResolver {

	@SuppressWarnings("rawtypes")
	@Override
	public List<Handler> getHandlerChain(PortInfo portInfo) {
		List<Handler> hchain = new ArrayList<Handler>();
		hchain.add(new SpagicClientLogHandler());
		return hchain;
	}

}
