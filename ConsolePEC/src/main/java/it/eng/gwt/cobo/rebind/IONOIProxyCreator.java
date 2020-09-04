package it.eng.gwt.cobo.rebind;

import it.eng.portlet.consolepec.gwt.client.rpc.IONOIRemoteServiceProxy;

import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.user.client.rpc.impl.RemoteServiceProxy;
import com.google.gwt.user.rebind.rpc.ProxyCreator;

public class IONOIProxyCreator extends ProxyCreator {

	public IONOIProxyCreator(JClassType serviceIntf) {
		super(serviceIntf);
	}
	
	@Override
	protected Class<? extends RemoteServiceProxy> getProxySupertype() {
		return IONOIRemoteServiceProxy.class;
	}

}
