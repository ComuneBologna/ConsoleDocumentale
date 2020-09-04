package it.eng.portlet.consolepec.gwt.server.fascicolo;

import java.io.File;
import java.nio.file.Files;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.cobo.consolepec.commons.services.InputStreamMapper;
import it.eng.consolepec.client.UploadFileZipClient;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.xmlplugin.factory.Pratica;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.portlet.consolepec.gwt.server.XMLPluginToDTOConverter;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.UploadFileZipAction;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.UploadFileZipResult;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author GiacomoFM
 * @since 13/mar/2019
 */
@Slf4j
public class UploadFileZipActionHandler implements ActionHandler<UploadFileZipAction, UploadFileZipResult> {

	@Value("#{portlet['tempDir']}")
	private String tmpDir;

	@Autowired
	private UserSessionUtil userSessionUtil;
	@Autowired
	private PraticaSessionUtil praticaSessionUtil;
	@Autowired
	private XMLPluginToDTOConverter praticaUtil;
	@Autowired
	private UploadFileZipClient uploadFileZipClient;

	@Override
	public UploadFileZipResult execute(UploadFileZipAction action, ExecutionContext context) throws ActionException {
		try {
			File file = null;
			File dir = new File(tmpDir);
			for (File sourceDir : dir.listFiles()) {
				if (sourceDir.isDirectory() && sourceDir.getName().equals(action.getTmpDir())) {
					for (File source : sourceDir.listFiles()) {
						if (source.isFile() && StringEscapeUtils.unescapeHtml(action.getTmpFile()).equals(source.getName())) {
							file = source;
						}
					}
				}
			}
			InputStreamMapper ism = new InputStreamMapper(file.getName(), Files.newInputStream(file.toPath()));
			LockedPratica lockedPratica = uploadFileZipClient.uploadZip(action.getPathFascicolo(), ism, userSessionUtil.getUtenteSpagic());
			Pratica<?> pratica = praticaSessionUtil.loadPraticaInSessione(lockedPratica);
			return new UploadFileZipResult(praticaUtil.fascicoloToDettaglio((Fascicolo) pratica));

		} catch (SpagicClientException e) {
			log.error("Errore", e);
			return new UploadFileZipResult(e.getErrorMessage());

		} catch (Exception e) {
			log.error("Errore imprevisto", e);
			return new UploadFileZipResult(ConsolePecConstants.ERROR_MESSAGE);
		}
	}

	@Override
	public Class<UploadFileZipAction> getActionType() {
		return UploadFileZipAction.class;
	}

	@Override
	public void undo(UploadFileZipAction action, UploadFileZipResult result, ExecutionContext context) throws ActionException {
		// ~
	}

}
