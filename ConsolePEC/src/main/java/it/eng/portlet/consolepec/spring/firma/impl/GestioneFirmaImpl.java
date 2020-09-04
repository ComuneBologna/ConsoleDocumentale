package it.eng.portlet.consolepec.spring.firma.impl;

import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import it.eng.cobo.consolepec.commons.datigenerici.PreferenzeUtente.PreferenzeFirmaDigitale;
import it.eng.cobo.consolepec.commons.dto.firmadigitale.FirmaRequest;
import it.eng.cobo.consolepec.commons.exception.ApplicationException;
import it.eng.cobo.consolepec.util.json.JsonFactory;
import it.eng.consolepec.xmlplugin.factory.Pratica;
import it.eng.consolepec.xmlplugin.util.XmlPluginUtil;
import it.eng.portlet.consolepec.gwt.server.rest.ErrorResponse;
import it.eng.portlet.consolepec.gwt.server.rest.RestClientInvoker;
import it.eng.portlet.consolepec.gwt.server.rest.RestResponse;
import it.eng.portlet.consolepec.gwt.shared.Base64Utils;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO.TipologiaFirma;
import it.eng.portlet.consolepec.gwt.shared.model.TipologiaCaricamento;
import it.eng.portlet.consolepec.spring.bean.profilazione.GestioneProfilazioneUtente;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;
import it.eng.portlet.consolepec.spring.firma.GestioneFirma;

public class GestioneFirmaImpl implements GestioneFirma {
	private static final Logger logger = LoggerFactory.getLogger(GestioneFirmaImpl.class);

	@Autowired
	UserSessionUtil userSessionUtil;

	@Autowired
	RestClientInvoker restClientInvoker;

	@Autowired
	GestioneProfilazioneUtente gestioneProfilazioneUtente;

	@Autowired
	PraticaSessionUtil praticaSessionUtil;

	@Override
	public Pratica<?> firmaDocumento(String idDocumentale, List<String> allegati, CredenzialiFirma cf, TipologiaFirma tipologiaFirma, boolean isEMailOut) throws Exception {
		String username = cf.getUsername();
		String password = cf.getPassword();
		String otp = cf.getOtp();
		logger.debug("username: {} password is null: {} otp is null: {} ", username, password == null, otp == null);

		/* se non ci sono credenziali, provo a recuperarle dai custom fields liferay */
		if (username == null || password == null) {
			PreferenzeFirmaDigitale pfd = gestioneProfilazioneUtente.getPreferenzeFirmaDigitale();

			if (username == null && pfd.getUsername() != null) {
				username = pfd.getUsername();
			}

			if (password == null && pfd.getPassword() != null) {
				password = pfd.getPassword();
			}
		}

		FirmaRequest request = new FirmaRequest(allegati, username, password, otp, tipologiaFirma.name());
		HttpEntity entity = new StringEntity(JsonFactory.defaultFactory().serialize(request), ContentType.APPLICATION_JSON);
		RestResponse output = null;
		if (isEMailOut) {
			output = restClientInvoker.customPost("/service/fascicolo/" + idDocumentale + "/email/out/allegati/firma-digitale-remota", null, entity);
		} else {
			output = restClientInvoker.customPost("/service/fascicolo/" + idDocumentale + "/allegati/firma-digitale-remota", null, entity);
		}

		if (!output.isOk()) {
			logger.error("Errore durante l'invocazione del servizio di firma digitale - Response: {}", output.getJson());
			ErrorResponse error = RestClientInvoker.error(output.getJson());
			switch (HttpStatus.valueOf(error.getStatus()).series()) {
			case CLIENT_ERROR:
				throw new ApplicationException(error.getMessage(), true);
			default:
				throw new Exception(ConsolePecConstants.ERROR_MESSAGE);
			}
		}

		return praticaSessionUtil.loadPraticaFromEncodedPath(Base64Utils.URLencodeAlfrescoPath(XmlPluginUtil.convertAlfrescoPathFromIdDocumentale(idDocumentale)), TipologiaCaricamento.RICARICA);

	}
}
