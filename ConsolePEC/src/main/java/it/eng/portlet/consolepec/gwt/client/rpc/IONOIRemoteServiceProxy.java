package it.eng.portlet.consolepec.gwt.client.rpc;

import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.impl.RemoteServiceProxy;
import com.google.gwt.user.client.rpc.impl.RequestCallbackAdapter.ResponseReader;
import com.google.gwt.user.client.rpc.impl.RpcStatsContext;
import com.google.gwt.user.client.rpc.impl.Serializer;

/**
 * Classe specializzata di {@link RemoteServiceProxy} per generare un custom {@link AsyncCallback}
 * 
 * @author pluttero
 * 
 */
public class IONOIRemoteServiceProxy extends RemoteServiceProxy {

	protected IONOIRemoteServiceProxy(String moduleBaseURL, String remoteServiceRelativePath, String serializationPolicyName, Serializer serializer) {
		super(moduleBaseURL, remoteServiceRelativePath, serializationPolicyName, serializer);
	}

	@Override
	protected <T> RequestCallback doCreateRequestCallback(ResponseReader responseReader, String methodName, RpcStatsContext statsContext, AsyncCallback<T> callback) {
		/* creo un wrapper della callback */
		IONOIAsyncCallbackProxy<T> wrapper = new IONOIAsyncCallbackProxy<T>(callback);

		return super.doCreateRequestCallback(responseReader, methodName, statsContext, wrapper);
	}
}
