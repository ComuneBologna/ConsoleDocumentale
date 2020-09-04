package it.eng.portlet.consolepec.gwt.server.rest;

import org.springframework.beans.factory.annotation.Autowired;

import it.eng.cobo.consolepec.util.json.JsonFactory;

/**
 * @author Giacomo F.M.
 * @since 2019-06-03
 */
public abstract class ConsolePecRestClient {

	protected final JsonFactory jsonFactory = JsonFactory.defaultFactory();

	@Autowired
	protected RestClientInvoker restClientInvoker;

}
