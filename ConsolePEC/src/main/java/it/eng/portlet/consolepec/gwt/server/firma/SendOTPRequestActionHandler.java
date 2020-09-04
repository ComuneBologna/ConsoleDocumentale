package it.eng.portlet.consolepec.gwt.server.firma;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.cobo.consolepec.commons.dto.firmadigitale.OTPRequest;
import it.eng.cobo.consolepec.commons.exception.ApplicationException;
import it.eng.cobo.consolepec.util.json.JsonFactory;
import it.eng.portlet.consolepec.gwt.server.rest.ErrorResponse;
import it.eng.portlet.consolepec.gwt.server.rest.RestClientInvoker;
import it.eng.portlet.consolepec.gwt.server.rest.RestResponse;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.firma.SendOTPRequest;
import it.eng.portlet.consolepec.gwt.shared.action.firma.SendOTPRequestResult;

public class SendOTPRequestActionHandler implements ActionHandler<SendOTPRequest, SendOTPRequestResult> {

	public SendOTPRequestActionHandler() {}

	private final Logger logger = LoggerFactory.getLogger(SendOTPRequestActionHandler.class);

	@Autowired
	RestClientInvoker restClientInvoker;

	@Override
	public SendOTPRequestResult execute(SendOTPRequest action, ExecutionContext context) throws ActionException {
		SendOTPRequestResult sendOTPRequestResult = new SendOTPRequestResult();

		OTPRequest request = new OTPRequest(action.getUsername(), action.getPassword(), action.getCredentialType());
		HttpEntity entity = new StringEntity(JsonFactory.defaultFactory().serialize(request), ContentType.APPLICATION_JSON);
		RestResponse output = null;

		try {
			output = restClientInvoker.customPost("/service/firma/credenziali/otp", null, entity);

		} catch (ApplicationException e) {
			logger.error("Errore durante l'invocazione del servizio: " + "/service/firma/credenziali/otp", e);
			sendOTPRequestResult.setError(true);
			sendOTPRequestResult.setMessage(ConsolePecConstants.ERROR_MESSAGE);
		}

		if (!output.isOk()) {
			logger.error("Errore durante l'invocazione del servizio: " + "/service/firma/credenziali/otp - Response: {}", output.getJson());
			sendOTPRequestResult.setError(true);
			ErrorResponse error = RestClientInvoker.error(output.getJson());
			switch (HttpStatus.valueOf(error.getStatus()).series()) {
			case CLIENT_ERROR:
				sendOTPRequestResult.setMessage(error.getMessage());
				break;
			default:
				sendOTPRequestResult.setMessage(ConsolePecConstants.ERROR_MESSAGE);
				break;
			}

		}

		return sendOTPRequestResult;
	}

	@Override
	public void undo(SendOTPRequest action, SendOTPRequestResult result, ExecutionContext context) throws ActionException {}

	@Override
	public Class<SendOTPRequest> getActionType() {
		return SendOTPRequest.class;
	}
}
