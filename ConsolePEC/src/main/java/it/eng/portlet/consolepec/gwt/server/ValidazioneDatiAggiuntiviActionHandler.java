package it.eng.portlet.consolepec.gwt.server;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.consolepec.spagicclient.SpagicClientDatiAggiuntivi;
import it.eng.consolepec.spagicclient.bean.request.datiaggiuntivi.DatoAggiuntivo;
import it.eng.consolepec.spagicclient.bean.response.validazione.ValidazioneDatoAggiuntivo;
import it.eng.consolepec.spagicclient.bean.response.validazione.ValidazioneEdErroriDatiAggiuntivi;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.util.DatiAggiuntiviUtil;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.ValidazioneDatiAggiuntivi;
import it.eng.portlet.consolepec.gwt.shared.action.ValidazioneDatiAggiuntiviResult;
import it.eng.portlet.consolepec.gwt.shared.model.ValidazioneDatoAggiuntivoDTO;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

public class ValidazioneDatiAggiuntiviActionHandler implements ActionHandler<ValidazioneDatiAggiuntivi, ValidazioneDatiAggiuntiviResult> {

	@Autowired
	SpagicClientDatiAggiuntivi spagicClientDatiAggiuntivi;

	@Autowired
	UserSessionUtil userSessionUtil;

	@Autowired
	XMLPluginToDTOConverterUtil xmlPluginToDTOConverterUtil;

	private static final Logger logger = LoggerFactory.getLogger(ValidazioneDatiAggiuntiviActionHandler.class);

	public ValidazioneDatiAggiuntiviActionHandler() {
		super();
	}

	@Override
	public ValidazioneDatiAggiuntiviResult execute(ValidazioneDatiAggiuntivi action, ExecutionContext context) throws ActionException {
		try {
			logger.debug("Validazione indirizzi");

			List<DatoAggiuntivo> datiAgg = new ArrayList<DatoAggiuntivo>();
			for (it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo dag : action.getDatiAggiuntivi()) {
				datiAgg.add(DatiAggiuntiviUtil.datoCommonToDatoSpagic(dag));
			}

			ValidazioneEdErroriDatiAggiuntivi validazioneEdErrori = spagicClientDatiAggiuntivi.validaDatiAggiuntivi(datiAgg, userSessionUtil.getUtenteSpagic());

			// deserializzo l'output del servizio
			List<ValidazioneDatoAggiuntivoDTO> validazioneDatiAggiuntivi = new ArrayList<ValidazioneDatoAggiuntivoDTO>();
			boolean valido = true;
			for (ValidazioneDatoAggiuntivo va : validazioneEdErrori.getValidazione()) {

				DatoAggiuntivo valore = va.getDatoAggiuntivo();
				ValidazioneDatoAggiuntivoDTO vaDatoAgg = new ValidazioneDatoAggiuntivoDTO(DatiAggiuntiviUtil.datoSpagicToDatoCommon(valore), va.isValido());
				vaDatoAgg.getVieConLoStessoNome().addAll(va.getVieConLoStessoNome());

				valido = valido && va.isValido();

				validazioneDatiAggiuntivi.add(vaDatoAgg);

			}

			return new ValidazioneDatiAggiuntiviResult(validazioneDatiAggiuntivi, validazioneEdErrori.getErrori());

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			return new ValidazioneDatiAggiuntiviResult(e.getErrorMessage());

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			return new ValidazioneDatiAggiuntiviResult(ConsolePecConstants.ERROR_MESSAGE);

		}

	}

	@Override
	public void undo(ValidazioneDatiAggiuntivi action, ValidazioneDatiAggiuntiviResult result, ExecutionContext context) throws ActionException {

	}

	@Override
	public Class<ValidazioneDatiAggiuntivi> getActionType() {
		return ValidazioneDatiAggiuntivi.class;
	}
}
