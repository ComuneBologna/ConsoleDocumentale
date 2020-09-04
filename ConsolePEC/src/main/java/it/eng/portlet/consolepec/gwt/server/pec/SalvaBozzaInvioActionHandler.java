package it.eng.portlet.consolepec.gwt.server.pec;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.consolepec.client.GestioneEmailOutClient;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.xmlplugin.pratica.email.PraticaEmail;
import it.eng.consolepec.xmlplugin.pratica.email.PraticaEmailOut;
import it.eng.portlet.consolepec.gwt.server.XMLPluginToDTOConverter;
import it.eng.portlet.consolepec.gwt.shared.Base64Utils;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.pec.SalvaBozzaInvio;
import it.eng.portlet.consolepec.gwt.shared.action.pec.SalvaBozzaInvioResult;
import it.eng.portlet.consolepec.gwt.shared.model.Destinatario;
import it.eng.portlet.consolepec.gwt.shared.model.PecOutDTO;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

public class SalvaBozzaInvioActionHandler implements ActionHandler<SalvaBozzaInvio, SalvaBozzaInvioResult> {

	@Autowired
	XMLPluginToDTOConverter xmlPluginToDTOConverter;

	@Autowired
	GestioneEmailOutClient gestioneEmailOutClient;

	@Autowired
	PraticaSessionUtil praticaSessionUtil;

	@Autowired
	UserSessionUtil userSessionUtil;

	private static final Logger logger = LoggerFactory.getLogger(SalvaBozzaInvioActionHandler.class);

	public SalvaBozzaInvioActionHandler() {

	}

	@Override
	public SalvaBozzaInvioResult execute(SalvaBozzaInvio action, ExecutionContext context) throws ActionException {

		PecOutDTO bozzaPecOut = action.getBozzaPecOut();
		SalvaBozzaInvioResult salvaBozza = new SalvaBozzaInvioResult(false, "", bozzaPecOut);

		try {
			String pathPratica = Base64Utils.URLdecodeAlfrescoPath(action.getIdEmailOut());
			String pathFascicolo = Base64Utils.URLdecodeAlfrescoPath(action.getIdFascicolo());

			List<String> destinatari = new ArrayList<String>();
			List<String> destinatariCC = new ArrayList<String>();

			for (Destinatario dest : bozzaPecOut.getDestinatari()) {
				destinatari.add(dest.getDestinatario());
			}

			for (Destinatario dest : bozzaPecOut.getDestinatariCC()) {
				destinatariCC.add(dest.getDestinatario());
			}

			LockedPratica lockedPratica = gestioneEmailOutClient.modificaBozza(pathFascicolo, pathPratica, bozzaPecOut.getMittente(), destinatari, destinatariCC, bozzaPecOut.getTitolo(),
					bozzaPecOut.getBody(), bozzaPecOut.getFirma(), userSessionUtil.getUtenteSpagic());

			PraticaEmail praticaEmail = (PraticaEmail) praticaSessionUtil.loadPraticaInSessione(lockedPratica);
			PraticaEmailOut out = (PraticaEmailOut) praticaSessionUtil.updatePraticaEmail(praticaEmail);
			bozzaPecOut = xmlPluginToDTOConverter.emailToDettaglioOUT(out);

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			salvaBozza.setError(true);
			salvaBozza.setMessError(e.getErrorMessage());

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			salvaBozza.setError(true);
			salvaBozza.setMessError(ConsolePecConstants.ERROR_MESSAGE);
		}

		return salvaBozza;
	}

	@Override
	public void undo(SalvaBozzaInvio action, SalvaBozzaInvioResult result, ExecutionContext context) throws ActionException {}

	@Override
	public Class<SalvaBozzaInvio> getActionType() {
		return SalvaBozzaInvio.class;
	}
}
