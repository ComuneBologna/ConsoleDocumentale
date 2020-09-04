package it.eng.portlet.consolepec.gwt.server.urbanistica;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.cobo.consolepec.commons.firmadigitale.FirmaDigitale;
import it.eng.cobo.consolepec.util.firmadigitale.FirmaDigitaleUtil;
import it.eng.consolepec.spagicclient.SpagicClientVerifySingnatureDocument;
import it.eng.portlet.consolepec.gwt.shared.action.urbanistica.DettaglioAllegatoFirmatoAction;
import it.eng.portlet.consolepec.gwt.shared.action.urbanistica.DettaglioAllegatoFirmatoResult;

/**
 * @author GiacomoFM
 * @since 13/feb/2018
 */
public class DettaglioAllegatoFirmatoActionHandler implements ActionHandler<DettaglioAllegatoFirmatoAction, DettaglioAllegatoFirmatoResult> {

	@Autowired
	private SpagicClientVerifySingnatureDocument spagicClientVerifySingnatureDocument;

	private static final Logger logger = LoggerFactory.getLogger(DettaglioAllegatoFirmatoActionHandler.class);

	@Override
	public DettaglioAllegatoFirmatoResult execute(DettaglioAllegatoFirmatoAction action, ExecutionContext ectx) throws ActionException {

		logger.info("INIZIO DettaglioAllegatoFirmatoResult");
		DettaglioAllegatoFirmatoResult result;

		try {
			FirmaDigitale firmaDigitale = spagicClientVerifySingnatureDocument.verificaFirmaDocumentoByUuid(action.getUuidAlfresco());
			if (FirmaDigitaleUtil.isFirmaDigitaleValida(firmaDigitale))
				result = new DettaglioAllegatoFirmatoResult(firmaDigitale);
			else
				result = new DettaglioAllegatoFirmatoResult("Firma non presente o non valida");

		} catch (Exception e) {
			logger.error("Errore durante la verifica della firma digitale per l'allegato", e);
			result = new DettaglioAllegatoFirmatoResult("Non &egrave; possibile verificare la firma");
		}

		logger.info("FINE DettaglioAllegatoFirmatoResult");
		return result;
	}

	@Override
	public Class<DettaglioAllegatoFirmatoAction> getActionType() {
		return DettaglioAllegatoFirmatoAction.class;
	}

	@Override
	public void undo(DettaglioAllegatoFirmatoAction arg0, DettaglioAllegatoFirmatoResult arg1, ExecutionContext arg2) throws ActionException {
		// ~
	}

}
