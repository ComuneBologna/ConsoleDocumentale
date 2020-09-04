/**
 *
 */
package it.eng.consolepec.spagicclient.remoteproxy.impl;

import javax.xml.bind.JAXBException;

import org.apache.commons.lang.StringEscapeUtils;

import it.bologna.comune.alfresco.download.attachment.REQUEST;
import it.bologna.comune.alfresco.download.attachment.SERVICERESPONSE;
import it.eng.consolepec.spagicclient.SpagicClientDownloadDocument;
import it.eng.consolepec.spagicclient.bean.request.download.DownloadRequest;
import it.eng.consolepec.spagicclient.bean.response.download.DownloadResponse;
import it.eng.consolepec.spagicclient.remoteproxy.ResponseWithAttachementsDto;
import it.eng.consolepec.spagicclient.remoteproxy.abs.AbstractSpagicClientRemoteProxy;
import it.eng.consolepec.spagicclient.remoteproxy.abs.SpagicClientSerializationUtil;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientErrorCode;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.servicename.ServiceNamesUtil;

/**
 * @author Roberto
 *
 */
public class SpagicClientDownloadDocumentRemoteProxy extends AbstractSpagicClientRemoteProxy<REQUEST, SERVICERESPONSE> implements SpagicClientDownloadDocument {

	protected SpagicClientDownloadDocumentRemoteProxy(String serviceProxyUrl, String alfrescoUsername, String alfrescoPassword, String serviceUsername, String servicePassword, String restServiceUrl) {
		super(serviceProxyUrl, alfrescoUsername, alfrescoPassword, serviceUsername, servicePassword, restServiceUrl);
	}

	@Override
	@Deprecated
	public ResponseWithAttachementsDto<SERVICERESPONSE> getDocument(REQUEST request) throws SpagicClientException {
		request.setUSER(getAlfrescoUsername());
		request.setPASSWORD(getAlfrescoPassword());
		ResponseWithAttachementsDto<SERVICERESPONSE> dto = invokeSpagicServiceWhitAttachementResponse(request);
		if (dto.getResponse().getCODE().equals("ERR"))
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);

		return dto;
	}

	@Override
	protected String getSpagicServiceId() {
		return ServiceNamesUtil.GET_DOC_BY_UUID;
	}

	@Override
	protected String getJaxbRequestToXml(REQUEST request) throws JAXBException {
		return SpagicClientSerializationUtil.getDownloadRequestToString(request);
	}

	@Override
	protected SERVICERESPONSE getXmlResponseToJaxb(String response) throws JAXBException {
		return SpagicClientSerializationUtil.getDownloadResponseToObject(response);
	}

	@Override
	public ResponseWithAttachementsDto<DownloadResponse> getDocument(DownloadRequest request) throws SpagicClientException {

		REQUEST request2 = new REQUEST();
		request2.setUSER(getAlfrescoUsername());
		request2.setPASSWORD(getAlfrescoPassword());
		request2.setUUIDDOC(request.getUuid());
		request2.setPATH(request.getPath());
		request2.setDELETEFILE(true);

		ResponseWithAttachementsDto<SERVICERESPONSE> dto = invokeSpagicServiceWhitAttachementResponse(request2);

		if (dto.getResponse().getCODE().equals("ERR"))
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);

		DownloadResponse downloadResponse = new DownloadResponse();
		downloadResponse.setCode(dto.getResponse().getCODE());
		downloadResponse.setDesc(dto.getResponse().getDESCR());
		downloadResponse.setRef_in_store(dto.getResponse().getREFINSTORE());
		downloadResponse.setDoc_name(StringEscapeUtils.unescapeXml(dto.getResponse().getDOCNAME()));

		ResponseWithAttachementsDto<DownloadResponse> responseWithAttachementsDto = new ResponseWithAttachementsDto<DownloadResponse>();
		responseWithAttachementsDto.setAttachements(dto.getAttachements());
		responseWithAttachementsDto.setResponse(downloadResponse);
		return responseWithAttachementsDto;
	}

}
