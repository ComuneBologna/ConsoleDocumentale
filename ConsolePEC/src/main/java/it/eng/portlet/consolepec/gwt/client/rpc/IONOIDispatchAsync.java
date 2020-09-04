package it.eng.portlet.consolepec.gwt.client.rpc;

import it.eng.portlet.consolepec.gwt.client.actionhandler.registry.ConsolePecClientActionHandlerRegistry;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.client.ExceptionHandler;
import com.gwtplatform.dispatch.client.RpcDispatchAsync;
import com.gwtplatform.dispatch.shared.SecurityCookieAccessor;

public class IONOIDispatchAsync extends RpcDispatchAsync {

	@Inject
	public IONOIDispatchAsync(ExceptionHandler exceptionHandler, SecurityCookieAccessor securityCookieAccessor, ConsolePecClientActionHandlerRegistry consolePecClientActionHandlerRegistry) {
		super(exceptionHandler, securityCookieAccessor, consolePecClientActionHandlerRegistry );
	}

}
