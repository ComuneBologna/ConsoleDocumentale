package it.eng.portlet.consolepec.gwt.server;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.bologna.comune.spagic.protocollazione.dettaglio.Gruppocampo;
import it.bologna.comune.spagic.protocollazione.dettaglio.Response;
import it.bologna.comune.spagic.protocollazione.dettaglio.Tiporec;
import it.eng.consolepec.spagicclient.SpagicClientDettaglioPraticaProtocollozione;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.GetConfigurazioneCampiProtocollazione;
import it.eng.portlet.consolepec.gwt.shared.action.GetConfigurazioneCampiProtocollazioneResult;
import it.eng.portlet.consolepec.gwt.shared.dto.Campo;

public class GetConfigurazioneCampiProtocollazioneActionHandler implements ActionHandler<GetConfigurazioneCampiProtocollazione, GetConfigurazioneCampiProtocollazioneResult> {

	private static Logger logger = LoggerFactory.getLogger(GetConfigurazioneCampiProtocollazioneActionHandler.class);

	@Autowired
	SpagicClientDettaglioPraticaProtocollozione spagicClientDettaglioPraticaProtocollozione;

	public GetConfigurazioneCampiProtocollazioneActionHandler() {}

	@Override
	public GetConfigurazioneCampiProtocollazioneResult execute(GetConfigurazioneCampiProtocollazione action, ExecutionContext context) throws ActionException {
		Response dettaglioPratica = null;
		try {
			logger.debug("Recupero configurazione campi per la tipologia di pratica {}", action.getId());
			dettaglioPratica = spagicClientDettaglioPraticaProtocollozione.getDettaglioPratica(action.getId().toString());
		} catch (SpagicClientException e) {
			logger.error("Errore nel recupero dei campi della pratica", e);
			return new GetConfigurazioneCampiProtocollazioneResult(new HashMap<String, Campo>(), e.getErrorMessage(), false);
		} catch (Exception e) {
			logger.error("Errore nel recupero dei campi della pratica", e);
			return new GetConfigurazioneCampiProtocollazioneResult(new HashMap<String, Campo>(), ConsolePecConstants.ERROR_MESSAGE, false);
		}

		HashMap<String, Campo> campi = createCampi(dettaglioPratica);
		return new GetConfigurazioneCampiProtocollazioneResult(campi);
	}

	private HashMap<String, Campo> createCampi(Response dettaglioPratica) {
		HashMap<String, Campo> campi = new HashMap<String, Campo>();

		List<Tiporec> tipiRecord = dettaglioPratica.getTiporec();

		for (Tiporec tiporec : tipiRecord) {

			String nomeRecord = tiporec.getValore();
			logger.debug("Recupero della configurazione dei campi per il tipo record: {}", nomeRecord);

			// setListaCampi(nomeRecord, tiporec.getCampo(), campi);

			setListaGruppiCampi(nomeRecord, tiporec.getGruppocampo(), campi);
		}

		return campi;
	}

	private void setListaGruppiCampi(String nomeRecord, List<Gruppocampo> gruppocampo, HashMap<String, Campo> campi) {

		for (Gruppocampo gruppo : gruppocampo) {
			setListaCampi(nomeRecord, gruppo.getDescrizione(), gruppo.getCampo(), campi);
		}

	}

	private void setListaCampi(String nomeRecord, String descrizione, List<it.bologna.comune.spagic.protocollazione.dettaglio.Campo> listaCampi, HashMap<String, Campo> campi) {
		for (it.bologna.comune.spagic.protocollazione.dettaglio.Campo campo : listaCampi) {
			campi.put(campo.getNome(), new Campo(campo.getNome(), campo.getValore(), campo.isVisibile(), campo.isModificabile(), campo.isObbligatorio(), nomeRecord, descrizione, campo.getLunghezza(),
					campo.getTipoWidget(), campo.getDescrizione(), campo.getPosizione(), campo.getMaxoccorrenze()));
		}

	}

	@Override
	public void undo(GetConfigurazioneCampiProtocollazione action, GetConfigurazioneCampiProtocollazioneResult result, ExecutionContext context) throws ActionException {}

	@Override
	public Class<GetConfigurazioneCampiProtocollazione> getActionType() {
		return GetConfigurazioneCampiProtocollazione.class;
	}
}
