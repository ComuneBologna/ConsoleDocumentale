package it.eng.portlet.consolepec.gwt.servermock;

import it.eng.portlet.consolepec.gwt.shared.action.GetConfigurazioneCampiProtocollazione;
import it.eng.portlet.consolepec.gwt.shared.action.GetConfigurazioneCampiProtocollazioneResult;
import it.eng.portlet.consolepec.gwt.shared.dto.Campo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class GetConfigurazioneCampiMockActionHandler implements ActionHandler<GetConfigurazioneCampiProtocollazione, GetConfigurazioneCampiProtocollazioneResult> {

	List<String> nomi = Arrays.asList("i1_tipo_protocollo", "i1_codice_utente", "i1_data_arrivo", "i1_ora_arrivo", "i1_tipo_protocollazione", "i1_codice_provenienza", "i1_provenienza", "i1_cf_provenienza", "i1_riferimento_provenienza", "i1_codice_destinatario", "i1_destinatario",
			"i1_cf_destinatario", "i1_tipo_protocollo_capofila", "i1_anno_protocollo_capofila", "i1_numero_protocollo_capofila", "i1_codice_titolo", "i1_codice_rubrica", "i1_codice_sezione", "i1_cella_protocollazione", "i1_cella_assegnazione", "i1_numero_allegati", "i1_documentazione_completa",
			"i1_tipologia_documento", "i1_oggetto", "i1_codice_via", "i1_numero_civico", "i1_esponente_civico", "i1_numero_interno", "i1_esponente_interno", "i1_fascicolo_riservato", "i1_sistema_immissione", "i1_utente_applicazione", "i1_tipo_applicazione", "i1_mezzo_spedizione",
			"i1_tipo_registro", "i2_cf_piva_nominativo", "i2_nominativo", "i2_tipo_nominativo");

	public GetConfigurazioneCampiMockActionHandler() {
	}

	@Override
	public GetConfigurazioneCampiProtocollazioneResult execute(GetConfigurazioneCampiProtocollazione action, ExecutionContext context) throws ActionException {
		HashMap<String, Campo> campi = createCampi();
		GetConfigurazioneCampiProtocollazioneResult res = new GetConfigurazioneCampiProtocollazioneResult(campi, "", false);
		return res;
	}

	private HashMap<String, Campo> createCampi() {
		HashMap<String, Campo> campi = new HashMap<String, Campo>();
		for (String nome : nomi) {
			Boolean modificabile = randomBoolean();
			Boolean obbligatorio = false;// randomBoolean();
			String valore;
			if (randomBoolean())
				valore = nome;
			else
				valore = "";

			if (!modificabile)
				valore = nome;

			Boolean visibile = true;// randomBoolean();
			//Campo campo = new Campo(nome, valore, visibile, modificabile, obbligatorio, "tipo", 3, TipoWidget.DATE.name());
			//campi.put(nome, campo);
		}
		return campi;
	}

	private Boolean randomBoolean() {
		Random r = new Random();
		return r.nextBoolean();

	}

	@Override
	public void undo(GetConfigurazioneCampiProtocollazione action, GetConfigurazioneCampiProtocollazioneResult result, ExecutionContext context) throws ActionException {
	}

	@Override
	public Class<GetConfigurazioneCampiProtocollazione> getActionType() {
		return GetConfigurazioneCampiProtocollazione.class;
	}
}
