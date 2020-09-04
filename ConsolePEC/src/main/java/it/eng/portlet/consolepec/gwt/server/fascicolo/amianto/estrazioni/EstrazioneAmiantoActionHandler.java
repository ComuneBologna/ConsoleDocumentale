package it.eng.portlet.consolepec.gwt.server.fascicolo.amianto.estrazioni;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.commons.fileupload.util.Streams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.io.Files;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.consolepec.spagicclient.SpagicClientEstrazioneAmianto;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.search.SearchObjectSort;
import it.eng.consolepec.spagicclient.search.SearchObjectSort.SortType;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.amianto.EstrazioneAmianto;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.amianto.EstrazioneAmiantoResult;
import it.eng.portlet.consolepec.spring.bean.gestionepratiche.IGestioneAllegati;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

public class EstrazioneAmiantoActionHandler implements ActionHandler<EstrazioneAmianto, EstrazioneAmiantoResult> {

	@Autowired
	SpagicClientEstrazioneAmianto spagicClientEstrazioniAmianto;
	@Autowired
	IGestioneAllegati gestioneAllegati;
	@Autowired
	UserSessionUtil userSessionUtil;

	private final Logger logger = LoggerFactory.getLogger(EstrazioneAmiantoActionHandler.class);

	public EstrazioneAmiantoActionHandler() {}

	@Override
	public EstrazioneAmiantoResult execute(EstrazioneAmianto action, ExecutionContext context) throws ActionException {
		logger.info("");

		EstrazioneAmiantoResult actionResult = new EstrazioneAmiantoResult();
		try {
			InputStream inputStream = null;

			List<Object> parameters = new ArrayList<Object>();
			parameters.add(action.getInizio());
			parameters.add(action.getFine());

			logger.info("Invocazione servizio Spagic");
			inputStream = spagicClientEstrazioniAmianto.estrai("estrazione_globale", parameters, userSessionUtil.getUtenteSpagic());

			logger.info("Salvataggio file");

			File pathDir = new File(gestioneAllegati.getTempDir());
			// fase due scrittura documento su di uno stream
			String dir = UUID.randomUUID().toString();
			File tempDir = new File(pathDir, dir);
			File tempFile = new File(tempDir, ConsolePecConstants.ESTRAZIONI_AMIANTO_FILE_NAME_ODS);
			Files.createParentDirs(tempFile);
			logger.debug("Path temporaneo: {}", tempFile.getAbsolutePath());
			FileOutputStream fos = new FileOutputStream(tempFile);
			Streams.copy(inputStream, fos, true);

			actionResult.setFileName(tempFile.getName());
			actionResult.setFileDir(tempFile.getParentFile().getName());

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			actionResult.setError(true);
			actionResult.setMessageError(e.getErrorMessage());

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			actionResult.setError(true);
			actionResult.setMessageError(ConsolePecConstants.ERROR_MESSAGE);
		}

		return actionResult;
	}

	public static SearchObjectSort initSort(Map<String, SortType> sorts) {
		SearchObjectSort sort = SearchObjectSort.start();
		for (Entry<String, SortType> current : sorts.entrySet()) {
			sort.addSort(current.getKey(), it.eng.consolepec.spagicclient.search.SearchObjectSort.SortType.valueOf(current.getValue().name()));
		}
		return sort;
	}

	@Override
	public void undo(EstrazioneAmianto action, EstrazioneAmiantoResult result, ExecutionContext context) throws ActionException {}

	@Override
	public Class<EstrazioneAmianto> getActionType() {
		return EstrazioneAmianto.class;
	}

}
