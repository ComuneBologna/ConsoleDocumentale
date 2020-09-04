package it.eng.portlet.consolepec.gwt.server.fascicolo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.portlet.consolepec.gwt.server.XMLPluginToDTOConverter;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.UploadAllegatoFascicolo;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.UploadAllegatoFascicoloResult;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.TmpFileUploadDTO;
import it.eng.portlet.consolepec.spring.bean.gestionepratiche.IGestioneAllegati;

public class UploadAllegatoFascicoloActionHandler implements ActionHandler<UploadAllegatoFascicolo, UploadAllegatoFascicoloResult> {

	@Autowired
	IGestioneAllegati gestAllg;
	@Autowired
	XMLPluginToDTOConverter praticaUtil;
	Logger logger = LoggerFactory.getLogger(UploadAllegatoFascicoloActionHandler.class);

	public UploadAllegatoFascicoloActionHandler() {}

	@Override
	public UploadAllegatoFascicoloResult execute(UploadAllegatoFascicolo action, ExecutionContext context) throws ActionException {
		logger.debug("Richiesto upload allegati su fascicolo {}", action.getClientID());
		String encPath = action.getClientID();
		try {
			Fascicolo fascicolo = null;
			for (TmpFileUploadDTO tmpFile : action.getTmpFiles()) {
				fascicolo = (Fascicolo) gestAllg.aggiungiAllegatoFromFileSystem(encPath, tmpFile.getDirName(), tmpFile.getFileName());
			}

			FascicoloDTO dto = praticaUtil.fascicoloToDettaglio(fascicolo);
			logger.debug("Caricato DTO fascicolo {}", dto);
			return new UploadAllegatoFascicoloResult(dto, false, null);

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			return new UploadAllegatoFascicoloResult(null, true, e.getErrorMessage());

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			return new UploadAllegatoFascicoloResult(null, true, ConsolePecConstants.ERROR_MESSAGE);

		}
	}

	@Override
	public void undo(UploadAllegatoFascicolo action, UploadAllegatoFascicoloResult result, ExecutionContext context) throws ActionException {}

	@Override
	public Class<UploadAllegatoFascicolo> getActionType() {
		return UploadAllegatoFascicolo.class;
	}
}
