package it.eng.portlet.consolepec.gwt.server;

import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.portlet.consolepec.gwt.server.protocollazione.ElenchiComboBoxProtocollazione;
import it.eng.portlet.consolepec.gwt.shared.action.GetComboBoxProtocollazione;
import it.eng.portlet.consolepec.gwt.shared.action.GetComboBoxProtocollazioneResult;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GetComboBoxProtocollazioneActionHandler implements ActionHandler<GetComboBoxProtocollazione, GetComboBoxProtocollazioneResult> {

	@Autowired
	ElenchiComboBoxProtocollazione comboBoxProtocollazione;

	public GetComboBoxProtocollazioneActionHandler() {}

	@Override
	public GetComboBoxProtocollazioneResult execute(GetComboBoxProtocollazione action, ExecutionContext context) throws ActionException {
		log.info("Passo per la classe GetComboBoxProtocollazioneActionHandler#execute");
		return new GetComboBoxProtocollazioneResult(comboBoxProtocollazione.getComboTitoli(), comboBoxProtocollazione.getComboRubriche(null), //
				comboBoxProtocollazione.getComboSezioni(null, null), comboBoxProtocollazione.getComboTipologiaDocumenti(), //
				comboBoxProtocollazione.getComboCodiciInterni(), comboBoxProtocollazione.getComboCodiciEsterni());
	}

	@Override
	public void undo(GetComboBoxProtocollazione action, GetComboBoxProtocollazioneResult result, ExecutionContext context) throws ActionException {/**/}

	@Override
	public Class<GetComboBoxProtocollazione> getActionType() {
		return GetComboBoxProtocollazione.class;
	}
}
