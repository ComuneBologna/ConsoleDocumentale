package it.eng.gwt.cobo.rebind;

import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.user.rebind.rpc.ProxyCreator;
import com.google.gwt.user.rebind.rpc.ServiceInterfaceProxyGenerator;

public class IONOIInterfaceProxyGenerator extends ServiceInterfaceProxyGenerator {

	@Override
	protected ProxyCreator createProxyCreator(JClassType remoteService) {
		return new IONOIProxyCreator(remoteService);
	}
}
