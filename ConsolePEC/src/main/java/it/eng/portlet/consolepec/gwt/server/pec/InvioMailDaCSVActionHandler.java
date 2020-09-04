package it.eng.portlet.consolepec.gwt.server.pec;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.util.json.JsonFactory;
import it.eng.portlet.consolepec.gwt.server.rest.RestClientInvoker;
import it.eng.portlet.consolepec.gwt.server.rest.RestResponse;
import it.eng.portlet.consolepec.gwt.shared.Base64Utils;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.pec.InvioMailDaCSVAction;
import it.eng.portlet.consolepec.gwt.shared.action.pec.InvioMailDaCSVResult;
import it.eng.portlet.consolepec.gwt.shared.model.InvioCsvEsito;
import it.eng.portlet.consolepec.gwt.shared.model.InvioDaCSVInput;
import it.eng.portlet.consolepec.gwt.shared.model.InvioDaCsvBean;
import it.eng.portlet.consolepec.spring.bean.configurazioni.GestioneConfigurazioni;
import it.eng.portlet.consolepec.spring.bean.gestionepratiche.IGestioneAllegati;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

public class InvioMailDaCSVActionHandler implements ActionHandler<InvioMailDaCSVAction, InvioMailDaCSVResult> {

	private static final Logger logger = LoggerFactory.getLogger(InvioMailDaCSVActionHandler.class);

	@Autowired
	RestClientInvoker restClientInvoker;

	@Autowired
	UserSessionUtil userSessionUtil;

	@Autowired
	GestioneConfigurazioni gestioneConfigurazioni;

	@Autowired
	IGestioneAllegati gestioneAllegati;

	@Override
	public InvioMailDaCSVResult execute(InvioMailDaCSVAction action, ExecutionContext context) throws ActionException {

		logger.info("Inizio esecuzione InvioMailDaCSVActionHandler");
		InvioMailDaCSVResult result = new InvioMailDaCSVResult();

		try {
			AnagraficaRuolo ar = gestioneConfigurazioni.getAnagraficaRuoloByEtichetta(action.getInvioDaCsvBean().getAssegnatario());
			if (ar == null) {
				result.setError(true);
				result.setErrorMessage("Gruppo non valido");
			}

			if (action.getInvioDaCsvBean().getPosizioneIdDocumentaleFascicolo() != null) {
				action.getInvioDaCsvBean().setPosizioneIdDocumentaleFascicolo(action.getInvioDaCsvBean().getPosizioneIdDocumentaleFascicolo() - 1);
			}

			if (action.getInvioDaCsvBean().getPosizioneIndirizzoEmailDestinatario() != null) {
				action.getInvioDaCsvBean().setPosizioneIndirizzoEmailDestinatario(action.getInvioDaCsvBean().getPosizioneIndirizzoEmailDestinatario() - 1);
			}

			InvioDaCSVInput input = from(action.getInvioDaCsvBean(), ar);
			HttpEntity entity = new StringEntity(JsonFactory.defaultFactory().serialize(input), ContentType.APPLICATION_JSON);
			RestResponse output = restClientInvoker.customPost("/service/email/out/csv", null, entity);

			if (!output.isOk()) {
				result.setError(true);
				result.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);

			} else {
				result = JsonFactory.defaultFactory().deserialize(output.getJson(), InvioMailDaCSVResult.class);

				if (!result.isAsync()) {
					try {
						String fileName = UUID.randomUUID().toString() + "_esiti.csv";
						Path pathFile = Paths.get(gestioneAllegati.getTempDir() + File.separator + fileName);
						Files.createFile(pathFile);

						StringBuilder sb = new StringBuilder();
						sb.append("Riga");
						sb.append(';');
						sb.append("Identificativo documentale");
						sb.append(';');
						sb.append("Indirizzo Email");
						sb.append(';');
						sb.append("Errore");
						sb.append('\n');

						for (InvioCsvEsito esito : result.getEsiti()) {

							sb.append(esito.getRiga());
							sb.append(";");

							sb.append(esito.getIdDocumentale() != null && !esito.getIdDocumentale().isEmpty() ? esito.getIdDocumentale() : "-");
							sb.append(";");

							sb.append(esito.getIndirizzoEmail() != null && !esito.getIndirizzoEmail().isEmpty() ? esito.getIndirizzoEmail() : "-");
							sb.append(";");

							sb.append(esito.getDescrizioneErrore() != null && !esito.getDescrizioneErrore().isEmpty() ? esito.getDescrizioneErrore() : "-");
							sb.append("\n");

						}

						try (PrintWriter pw = new PrintWriter(pathFile.toFile())) {
							pw.write(sb.toString());
						}

						result.setEsitoFileName(fileName);

					} catch (Throwable e) {
						logger.error("Errore durante la generazione del file degli esiti", e);
					}
				}
			}

		} catch (Exception e) {
			logger.error("Errore durante l'esecuzione di InvioMailDaCSVActionHandler");
			result.setError(true);
			result.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);

		} finally {
			logger.info("Fine esecuzione InvioMailDaCSVActionHandler");

		}

		return result;
	}

	@Override
	public Class<InvioMailDaCSVAction> getActionType() {
		return InvioMailDaCSVAction.class;
	}

	@Override
	public void undo(InvioMailDaCSVAction action, InvioMailDaCSVResult result, ExecutionContext context) throws ActionException {}

	private static InvioDaCSVInput from(InvioDaCsvBean bean, AnagraficaRuolo ar) {

		InvioDaCSVInput res = new InvioDaCSVInput();
		res.setNomeAllegatoCSV(bean.getNomeAllegato());
		res.setIdDocumentaleFascicolo(Base64Utils.URLdecodeAlfrescoPath(bean.getClientIdFascicolo()));
		res.setSeparatoreCSV(bean.getSeparatoreCSV());
		res.setHeaderCSV(bean.getHeaderCSV());
		res.setPosizioneIdDocumentaleFascicolo(bean.getPosizioneIdDocumentaleFascicolo());
		res.setPosizioneIndirizzoEmailDestinatario(bean.getPosizioneIndirizzoEmailDestinatario());
		res.setAssegnatario(ar.getRuolo());
		res.setIdDocumentaleTemplate(Base64Utils.URLdecodeAlfrescoPath(bean.getClientIdTemplate()));
		res.setIndirizzoDestinatarioFromModello(bean.isIndirizzoDestinatarioFromModello());
		res.setPreValidazione(bean.isPreValidazione());
		return res;
	}

}
