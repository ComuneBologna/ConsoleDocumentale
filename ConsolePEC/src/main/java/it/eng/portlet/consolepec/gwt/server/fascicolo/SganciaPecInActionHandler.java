package it.eng.portlet.consolepec.gwt.server.fascicolo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.consolepec.spagicclient.SpagicClientSganciaEmailDaFascicolo;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.xmlplugin.factory.Pratica;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.portlet.consolepec.gwt.server.XMLPluginToDTOConverter;
import it.eng.portlet.consolepec.gwt.shared.Base64Utils;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.SganciaPecIn;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.SganciaPecInResult;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

public class SganciaPecInActionHandler implements ActionHandler<SganciaPecIn, SganciaPecInResult> {

	public SganciaPecInActionHandler() {}

	@Autowired
	SpagicClientSganciaEmailDaFascicolo spagicClientSganciaEmailDaFascicolo;
	@Autowired
	UserSessionUtil userSessionUtil;
	@Autowired
	PraticaSessionUtil praticaSessionUtil;
	@Autowired
	XMLPluginToDTOConverter xmlPluginToDTOConverter;

	Logger logger = LoggerFactory.getLogger(SganciaPecInActionHandler.class);

	@Override
	public SganciaPecInResult execute(SganciaPecIn action, ExecutionContext context) throws ActionException {
		try {
			String fascicoloPath = action.getFascicoloPath();
			String pecInPath = action.getPecInPath();
			logger.info("Path: fascicolo {} e pecIn {}", fascicoloPath, pecInPath);
			// Invocazione del servizio Spagic sganciaEmail
			LockedPratica sganciaEmail = spagicClientSganciaEmailDaFascicolo.sganciaEmail(Base64Utils.URLdecodeAlfrescoPath(pecInPath), Base64Utils.URLdecodeAlfrescoPath(fascicoloPath),
					userSessionUtil.getUtenteSpagic());
			// Recupera il fascicolo da Alfresco e lo mette in sessione. In sessione sono presenti tutte le pratiche che l'utente vede in quel momento, "pratiche aperte".
			// Quello che c'era prima viene sovrascritto da questo.
			Pratica<?> loadPraticaInSessione = praticaSessionUtil.loadPraticaInSessione(sganciaEmail);
			// Rimuove PEC_In dalla sessione.
			// Se serve caricarla nuovamente in sessione viene riscaricata da Alfresco.
			praticaSessionUtil.removePraticaFromEncodedPath(pecInPath);
			// Converte l'XML in DTO
			FascicoloDTO fascicoloToDettaglio = xmlPluginToDTOConverter.fascicoloToDettaglio((Fascicolo) loadPraticaInSessione);

			logger.debug("Oggetto SganciaPecIn creato con numero fascicolo: ", fascicoloToDettaglio.getNumeroFascicolo());
			return new SganciaPecInResult(fascicoloToDettaglio);

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			return new SganciaPecInResult(e.getErrorMessage(), true);

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			return new SganciaPecInResult(ConsolePecConstants.ERROR_MESSAGE, true);

		}
	}

	@Override
	public void undo(SganciaPecIn action, SganciaPecInResult result, ExecutionContext context) throws ActionException {}

	@Override
	public Class<SganciaPecIn> getActionType() {
		return SganciaPecIn.class;
	}
}
