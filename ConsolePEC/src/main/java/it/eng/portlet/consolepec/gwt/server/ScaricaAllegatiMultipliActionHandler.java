package it.eng.portlet.consolepec.gwt.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.ScaricaAllegatiMultipli;
import it.eng.portlet.consolepec.gwt.shared.action.ScaricaAllegatiMultipliResult;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.spring.bean.gestionepratiche.IGestioneAllegati;

public class ScaricaAllegatiMultipliActionHandler implements ActionHandler<ScaricaAllegatiMultipli, ScaricaAllegatiMultipliResult> {

	@Autowired
	IGestioneAllegati gestioneAllegati;

	Logger logger = LoggerFactory.getLogger(ScaricaAllegatiMultipliActionHandler.class);

	public ScaricaAllegatiMultipliActionHandler() {}

	@Override
	public ScaricaAllegatiMultipliResult execute(ScaricaAllegatiMultipli action, ExecutionContext context) throws ActionException {
		String clientID = action.getClientID();
		logger.debug("Download allegati per pratica: {}", clientID);
		StringBuilder sb = new StringBuilder();
		for (AllegatoDTO dto : action.getSelezionati()) {
			sb.append(dto.getNome()).append(" ");
		}
		logger.debug("Allegati selezionati: {}", sb.toString());
		ScaricaAllegatiMultipliResult result = null;
		try {
			Set<String> tmpFileScaricati = new HashSet<String>();
			for (AllegatoDTO allegato : action.getSelezionati()) {
				String tempFile = gestioneAllegati.getTempDir() + File.separator + gestioneAllegati.downloadAllegatoToFileSystem(clientID, allegato.getNome(), null);
				tmpFileScaricati.add(tempFile);
			}
			int buffer = 2048;
			String randomDirName = UUID.randomUUID().toString();
			File tempRandomDir = new File(gestioneAllegati.getTempDir(), randomDirName);
			tempRandomDir.mkdir();
			File zipFile = new File(tempRandomDir, ConsolePecConstants.DOWNLOAD_ZIP_NAME);
			logger.debug("Path temporaneo: {}", zipFile.getAbsolutePath());
			BufferedInputStream origin = null;
			FileOutputStream dest = new FileOutputStream(zipFile);
			ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest), Charset.forName("UTF-8"));
			byte data[] = new byte[buffer];
			for (String tmpFileScaricato : tmpFileScaricati) {
				File f = new File(tmpFileScaricato);
				FileInputStream fi = new FileInputStream(tmpFileScaricato);
				origin = new BufferedInputStream(fi, buffer);
				ZipEntry entry = new ZipEntry(f.getName());
				out.putNextEntry(entry);
				int count;
				while ((count = origin.read(data, 0, buffer)) != -1) {
					out.write(data, 0, count);
				}
				origin.close();
				// eliminazione file temporaneo (e directory che lo contiene)
				FileUtils.deleteDirectory(new File(f.getParent()));
			}
			out.close();
			dest.close();
			logger.debug("Zip creato: {}", zipFile.getAbsolutePath());
			result = new ScaricaAllegatiMultipliResult(randomDirName, zipFile.getName());
			result.setError(false);

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			result = new ScaricaAllegatiMultipliResult(null, null);
			result.setError(true);
			result.setErrorMsg(e.getErrorMessage());

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			result = new ScaricaAllegatiMultipliResult(null, null);
			result.setError(true);
			result.setErrorMsg(ConsolePecConstants.ERROR_MESSAGE);
		}

		return result;
	}

	@Override
	public void undo(ScaricaAllegatiMultipli action, ScaricaAllegatiMultipliResult result, ExecutionContext context) throws ActionException {

	}

	@Override
	public Class<ScaricaAllegatiMultipli> getActionType() {

		return ScaricaAllegatiMultipli.class;
	}
}
