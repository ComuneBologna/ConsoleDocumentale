/**
 * 
 */
package it.eng.consolepec.spagicclient;

import it.bologna.comune.alfresco.download.attachment.REQUEST;
import it.bologna.comune.alfresco.download.attachment.SERVICERESPONSE;
import it.eng.consolepec.spagicclient.bean.request.download.DownloadRequest;
import it.eng.consolepec.spagicclient.bean.response.download.DownloadResponse;
import it.eng.consolepec.spagicclient.remoteproxy.ResponseWithAttachementsDto;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;

/**
 * @author Roberto
 * 
 */
public interface SpagicClientDownloadDocument {

	/**
	 * 
	 * @param request
	 * @return
	 * @throws SpagicClientException
	 * 
	 * deprecato usare il metodo ResponseWithAttachementsDto<DownloadResponse> getDocument(DownloadRequest request)
	 */
	@Deprecated
	public ResponseWithAttachementsDto<SERVICERESPONSE> getDocument(REQUEST request) throws SpagicClientException;
	
	/**
	 * 
	 * @param request
	 * @return
	 * @throws SpagicClientException
	 */
	public ResponseWithAttachementsDto<DownloadResponse> getDocument(DownloadRequest request) throws SpagicClientException;
	

}
