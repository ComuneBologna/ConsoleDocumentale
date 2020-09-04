package it.eng.portlet.consolepec.gwt.server;

import java.io.File;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.cobo.consolepec.util.io.ConsoleIOUtils;
import it.eng.portlet.consolepec.gwt.shared.action.Stampa;
import it.eng.portlet.consolepec.gwt.shared.action.StampaResult;
import it.eng.portlet.consolepec.spring.bean.gestionepratiche.IGestioneAllegati;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;
import it.eng.portlet.consolepec.spring.bean.stampe.GestioneStampe;
import it.eng.portlet.consolepec.spring.bean.stampe.GestioneStampe.StampaBean;

public abstract class StampaActionHandler<A extends Stampa<R>, R extends StampaResult> implements ActionHandler<A, R> {


	Logger logger = LoggerFactory.getLogger(StampaActionHandler.class);

	@Autowired
	IGestioneAllegati gestioneAllegati;
	@Autowired
	PraticaSessionUtil praticaSessionUtil;
	@Autowired
	UserSessionUtil userSessionUtil;
	@Autowired
	protected
	GestioneStampe gestioneStampe;


	public StampaActionHandler() {
	}

	@Override
	public R execute(A action, ExecutionContext context) throws ActionException {
		StampaBean bean = null;

		try{
			logger.info("Inizio stampa");
			bean = getStampa(action);
			String randomDirName = UUID.randomUUID().toString();
			File tempRandomDir = new File(gestioneAllegati.getTempDir(), randomDirName);
			tempRandomDir.mkdir();
			File stampa = new File(tempRandomDir, bean.getFileName());
			FileUtils.copyInputStreamToFile(bean.getFileStream(), stampa);
			logger.info("Stampa creata: {}", stampa.getAbsolutePath());
			return getResultSuccess(randomDirName, stampa.getName());
		} catch (Exception e) {
			logger.error("Errore in fase di creazione della stampa", e);
			return getResultFailed(e.getLocalizedMessage(), true);
		} finally {
			if (bean != null) {
				ConsoleIOUtils.closeStreams(bean.getFileStream());
			}
		}
	}

	public abstract StampaBean getStampa(A action) throws Exception;

	public abstract R getResultSuccess(String fileDir, String fileName);

	public abstract R getResultFailed(String errorMsg, Boolean error);

}
