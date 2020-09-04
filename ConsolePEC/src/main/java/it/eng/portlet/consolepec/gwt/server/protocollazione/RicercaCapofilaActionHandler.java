package it.eng.portlet.consolepec.gwt.server.protocollazione;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.consolepec.spagicclient.SpagicClientRicercaCapofila;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.CapofilaDetail;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.ProtocollazioneUtils;
import it.eng.portlet.consolepec.gwt.shared.action.RicercaCapofilaAction;
import it.eng.portlet.consolepec.gwt.shared.action.RicercaCapofilaResult;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

public class RicercaCapofilaActionHandler implements ActionHandler<RicercaCapofilaAction, RicercaCapofilaResult> {

	Logger logger = LoggerFactory.getLogger(RicercaCapofilaActionHandler.class);

	@Autowired
	SpagicClientRicercaCapofila spagicClientRicercaCapofila;
	@Autowired
	UserSessionUtil userSessionUtil;

	public RicercaCapofilaActionHandler() {}

	@Override
	public RicercaCapofilaResult execute(RicercaCapofilaAction action, ExecutionContext context) throws ActionException {
		RicercaCapofilaResult ricercaCapofilaResult = new RicercaCapofilaResult();
		try {
			logger.debug("Ricerca capofila {}/{}", action.getNumeroPg(), action.getAnnoPg());
			CapofilaDetail capofilaDetail = spagicClientRicercaCapofila.cerca(action.getNumeroPg(), action.getAnnoPg(), userSessionUtil.getUtenteSpagic());
			logger.debug(capofilaDetail.toString());
			ricercaCapofilaResult = convert(ricercaCapofilaResult, capofilaDetail);

			if (!ricercaCapofilaResult.isCapofila() || !ricercaCapofilaResult.isCompleto()) {
				ricercaCapofilaResult.setWarninig(true);
				ricercaCapofilaResult.setMessageWarninig(ProtocollazioneUtils.RICERCA_CAPOFILA_MESSAGE);
				return ricercaCapofilaResult;
			}
			return convert(ricercaCapofilaResult, capofilaDetail);

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			ricercaCapofilaResult.setError(true);
			ricercaCapofilaResult.setMessageError(e.getErrorMessage());

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			ricercaCapofilaResult.setError(true);
			ricercaCapofilaResult.setMessageError(ConsolePecConstants.ERROR_MESSAGE);
		}

		return ricercaCapofilaResult;
	}

	private static RicercaCapofilaResult convert(RicercaCapofilaResult ricercaCapofilaResult, CapofilaDetail capofilaDetail) {
		ricercaCapofilaResult.setAnnoPg(capofilaDetail.getAnnoPg());
		ricercaCapofilaResult.setAnnoPgCapofila(capofilaDetail.getAnnoPgCapofila());
		ricercaCapofilaResult.setCapofila(capofilaDetail.isCapofila());
		ricercaCapofilaResult.setCompleto(capofilaDetail.isCompleto());
		ricercaCapofilaResult.setDataProtocollazione(capofilaDetail.getDataProtocollazione());
		ricercaCapofilaResult.setNumeroPg(capofilaDetail.getNumeroPg());
		ricercaCapofilaResult.setNumeroPgCapofila(capofilaDetail.getNumeroPgCapofila());
		ricercaCapofilaResult.setOggetto(capofilaDetail.getOggetto());
		ricercaCapofilaResult.setRubrica(capofilaDetail.getRubrica());
		ricercaCapofilaResult.setSezione(capofilaDetail.getSezione());
		ricercaCapofilaResult.setTitolo(capofilaDetail.getTitolo());
		return ricercaCapofilaResult;
	}

	@Override
	public void undo(RicercaCapofilaAction action, RicercaCapofilaResult result, ExecutionContext context) throws ActionException {}

	@Override
	public Class<RicercaCapofilaAction> getActionType() {
		return RicercaCapofilaAction.class;
	}
}
